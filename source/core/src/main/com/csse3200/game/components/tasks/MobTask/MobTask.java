package com.csse3200.game.components.tasks.MobTask;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.ProjectileEffects;
import com.csse3200.game.components.npc.DodgingComponent;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.DropFactory;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csse3200.game.components.tasks.MobTask.MobType;

/**
 * The AI Task for all general mobs. This task handles the sequencing for melee
 * and ranged mobs as well as animations for all mobs. Its sequence is based on
 * whether the mob is a melee or ranged mob which dictates its attack method.
 */
public class MobTask extends DefaultTask implements PriorityTask {

    // Constants
    private static final int PRIORITY = 3;
    private static final Vector2 MELEE_MOB_SPEED = new Vector2(0.7f,0.7f);
    private static final Vector2 MELEE_RANGE_SPEED = new Vector2(0.5f,0.5f);
    private static final int MELEE_DAMAGE = 10;
    private static final long MELEE_ATTACK_SPEED = 2000;
    private static final long RANGE_ATTACK_SPEED = 5000;
    private static final float MELEE_ATTACK_RANGE = 0f;

    private static final float CRYSTAL_DROP_RATE = 0.1f;
    private static final float SCRAP_DROP_RATE = 0.6f;

    private static final Logger logger = LoggerFactory.getLogger(MobTask.class);


    // Private variables
    private final MobType mobType;

    private State state = State.DEFAULT;
    private Entity mob;
    private AnimationRenderComponent animation;
    private MovementTask movementTask;
    private Entity target;
    private GameTime gameTime;
    private long lastTimeAttacked;
    private long dodgeEndTime;

    // Flags
    boolean melee;
    boolean runFlag = false;
    boolean meleeFlag = false;
    boolean targetInRange = false;
    boolean rangeAttackFlag = false;
    boolean meleeAttackFlag = false;
    boolean deathFlag = false;
    boolean canDodge = false;

    // Enums
    private enum State {
        RUN, ATTACK, DEATH, DEFAULT, DODGE
    }

    /**
     * constructor for the mob
     * @param mobType type of mob it is
     */
    public MobTask(MobType mobType) {
        this.mobType = mobType;
        gameTime = ServiceLocator.getTimeSource();
    }

    /**
     * constructor for the mob
     * @param mobType type of mob it is
     * @param canDodge ability to dodge projectiles
     */
    public MobTask(MobType mobType, boolean canDodge) {
        this.mobType = mobType;
        gameTime = ServiceLocator.getTimeSource();
        this.canDodge = true;
    }

    /**
     * starts general mob sequence, starts its movement task and initialises
     * some of its variables
     */
    @Override
    public void start() {
        super.start();
        mob = owner.getEntity();
        animation = mob.getComponent(AnimationRenderComponent.class);
        melee = mobType.isMelee();

        movementTask = new MovementTask(new Vector2(0f, mob.getPosition().y));
        movementTask.create(owner);
        movementTask.start();
        runFlag = true;
        changeState(State.RUN);
        lastTimeAttacked = gameTime.getTime();
        dodgeEndTime = gameTime.getTime();

        if (melee) {
            mob.getComponent(PhysicsMovementComponent.class).setSpeed(MELEE_MOB_SPEED);
            mob.getComponent(PhysicsMovementComponent.class).setNormalSpeed(MELEE_MOB_SPEED);
        } else {
            mob.getComponent(PhysicsMovementComponent.class).setSpeed(MELEE_RANGE_SPEED);
            mob.getComponent(PhysicsMovementComponent.class).setNormalSpeed(MELEE_RANGE_SPEED);
        }
    }

    /**
     * handles the sequencing of melee and range mobs and detects death state
     */
    @Override
    public void update() {

        if(mob.getCenterPosition().x <= 1) {
          mob.getComponent(CombatStatsComponent.class).setHealth(0);

        }

        // death check
        if ((mob.getComponent(CombatStatsComponent.class).getHealth() <= 0 && !deathFlag)) {
            // decrement engineer count
            // ! tests failing because of textbox
//            ServiceLocator.getGameEndService().updateEngineerCount();
            changeState(State.DEATH);
            animate();
            movementTask.stop();
            deathFlag = true;

            
        } else if (deathFlag && animation.isFinished()) {
            ServiceLocator.getWaveService().updateEnemyCount();
            mob.setFlagForDelete(true);
            dropCurrency();
        }

        // Uhhh
        if(gameTime.getTime() >= dodgeEndTime) {
          if (canDodge) {
            mob.getEvents().trigger("dodgeIncomingEntity",
                mob.getCenterPosition());
            if(mob.getComponent(DodgingComponent.class).isTargetVisible(mob.getCenterPosition())) {
                changeState(State.DODGE);
                animate();
                // movementTask.stop();
            }
        }
          dodgeEndTime = gameTime.getTime() + 500; // 500ms
        }

        switch (state) {
            case RUN -> {
                if (runFlag) {
                    movementTask.start();
                    animate();
                    runFlag = false;
                }
                if (melee && enemyDetected() && gameTime.getTime() - lastTimeAttacked >= MELEE_ATTACK_SPEED) {
                    changeState(State.ATTACK);
                    meleeAttackFlag = true;
                } else if (gameTime.getTime() - lastTimeAttacked >= RANGE_ATTACK_SPEED) {
                    changeState(State.ATTACK);
                    rangeAttackFlag = true;
                }
            }
            case ATTACK -> {
                if (melee && meleeAttackFlag) {
                    movementTask.stop();
                    animate();
                    meleeAttack();
                    meleeAttackFlag = false;
                } else if (!melee && rangeAttackFlag) {
                    movementTask.stop();
                    animate();
                    rangeAttack();
                    rangeAttackFlag = false;
                }
                if (animation.isFinished()) {
                    changeState(State.RUN);
                    runFlag = true;
                }
            }
            case DODGE -> {
                if (animation.isFinished()) {
                    movementTask.start();
                    changeState(State.RUN);
                    animate();
                    runFlag = true;
                }
            }
        }
    }

    /**
     * handles animation for all states and all possible mobs
     */
    private void animate() {
        switch (state) {
                    case RUN -> owner.getEntity().getEvents().trigger("mob_walk");
                    case ATTACK -> owner.getEntity().getEvents().trigger("mob_attack");
                    case DEATH -> {
                        owner.getEntity().getEvents().trigger("mob_death");
                        owner.getEntity().getEvents().trigger("splitDeath");
                    }
                    case DODGE -> owner.getEntity().getEvents().trigger("mob_dodge");
                    case DEFAULT -> owner.getEntity().getEvents().trigger("mob_default");
                }
    }

    /**
     * Changes the state of the mob.
     * 
     * @param state state to change current state to
     */
    private void changeState(State state) {
        this.state = state;
    }

    /**
     * detects if there's an enemy within range of 1 to the left of it
     * @return if there's an enemy in front of the mob or not
     */
    private boolean enemyDetected() {
        // if there's an entity within x of - 1 of mob
        Entity targetInFront = ServiceLocator.getEntityService().getEntityAtPositionLayer(
                mob.getPosition().x, mob.getPosition().y, PhysicsLayer.HUMANS);
        if (targetInFront == null) {
            targetInFront = ServiceLocator.getEntityService().getEntityAtPositionLayer(
                    mob.getPosition().x + 0.2f, mob.getPosition().y, PhysicsLayer.HUMANS);
        }
        if (targetInFront == null) {
            targetInFront = ServiceLocator.getEntityService().getEntityAtPositionLayer(
                    mob.getPosition().x, mob.getPosition().y + 0.2f, PhysicsLayer.HUMANS);
        }
        if (targetInFront == null) {
            targetInFront = ServiceLocator.getEntityService().getEntityAtPositionLayer(
                    mob.getPosition().x, mob.getPosition().y - 0.2f, PhysicsLayer.HUMANS);
        }
        if (targetInFront == null) {
            return false;
        }
        target = targetInFront;
        return true;
    }

    /**
     * hits the target directly in front of it
     */
    private void meleeAttack() {
        // toggle melee flag off
        meleeFlag = false;

        // check if target is null or not in range
        if (target == null) {
            return;
        }
        CombatStatsComponent targetCombatStats = target.getComponent(CombatStatsComponent.class);
        targetCombatStats.hit(MELEE_DAMAGE);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (!targetInRange) {
                    return; // stop if target in range
                }
                meleeFlag = true; // toggle melee flag off
            }
        }, MELEE_ATTACK_SPEED);
    }

    /**
     * Shoots a fireball projectile and updates lastTimeAttacked
     */
    private void rangeAttack() {
        Vector2 destination = new Vector2(0, mob.getPosition().y);
        Entity projectile = ProjectileFactory.createEffectProjectile(PhysicsLayer.HUMANS, destination,
                new Vector2(2, 2), ProjectileEffects.FIREBALL, false);
        projectile.setPosition(mob.getPosition());
        projectile.setScale(-1f, 1f);
        ServiceLocator.getEntityService().register(projectile);
        lastTimeAttacked = gameTime.getTime();
    }

    /**
     * @return priority of task
     */
    @Override
    public int getPriority() {
        return PRIORITY;
    }

    /**
     * Sets dodge flag of the mob
     * 
     * @param dodgeFlag If true, mob dodges projectile.
     */
    public void setDodge(boolean dodgeFlag) {
      this.canDodge = dodgeFlag;
    }

    private void dropCurrency() {
        float randomValue = MathUtils.random(0f,1f);
        logger.info("Random value: " + randomValue);
        Entity currency;
        if (randomValue <= SCRAP_DROP_RATE && randomValue > CRYSTAL_DROP_RATE) {
            currency = DropFactory.createScrapDrop();
            currency.setPosition(mob.getPosition().x,mob.getPosition().y);
            ServiceLocator.getEntityService().register(currency);
        } else if (randomValue <= CRYSTAL_DROP_RATE) {
            currency = DropFactory.createCrystalDrop();
            currency.setPosition(mob.getPosition().x,mob.getPosition().y);
            ServiceLocator.getEntityService().register(currency);
        }



    }
}
