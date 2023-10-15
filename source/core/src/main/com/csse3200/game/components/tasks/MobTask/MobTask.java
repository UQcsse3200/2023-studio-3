package com.csse3200.game.components.tasks.MobTask;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.ProjectileEffects;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.npc.ArcaneArcherAnimationController;

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
    private static final float MELEE_ATTACK_RANGE = 0.2f;

    // Private variables
    private final MobType mobType;
    private State state = State.DEFAULT;
    private State prevState;
    private Entity mob;
    private AnimationRenderComponent animation;
    private MovementTask movementTask;
    private Entity target;
    private GameTime gameTime;
    private long lastTimeAttacked;

    // Flags
    boolean melee;
    boolean runFlag = false;
    boolean meleeFlag = false;
    boolean targetInRange = false;
    boolean rangeAttackFlag = false;
    boolean meleeAttackFlag = false;
    boolean deathFlag = false;

    // Enums
    private enum State {
        RUN, ATTACK, DEATH, DEFAULT
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
     * starts general mob sequence, starts its movement task and initialises
     * some of its variables
     */
    @Override
    public void start() {
        super.start();
        mob = owner.getEntity();
        animation = mob.getComponent(AnimationRenderComponent.class);
        mob.getComponent(PhysicsMovementComponent.class).setSpeed(MELEE_MOB_SPEED);
        melee = mobType.isMelee();

        movementTask = new MovementTask(new Vector2(0f, mob.getPosition().y));
        movementTask.create(owner);
        movementTask.start();
        runFlag = true;
        changeState(State.RUN);
        lastTimeAttacked = gameTime.getTime();

        if (melee) {
            mob.getComponent(PhysicsMovementComponent.class).setSpeed(MELEE_MOB_SPEED);
        } else {
            mob.getComponent(PhysicsMovementComponent.class).setSpeed(MELEE_RANGE_SPEED);
        }
    }

    /**
     * handles the sequencing of melee and range mobs and detects death state
     */
    @Override
    public void update() {

        // death check
        if (mob.getComponent(CombatStatsComponent.class).getHealth() <= 0 && !deathFlag) {
            changeState(State.DEATH);
            animate();
            movementTask.stop();
            deathFlag = true;
        } else if (deathFlag && animation.isFinished()) {
            mob.setFlagForDelete(true);
        }

        switch (state) {
            case RUN -> {
                if (runFlag) {
                    movementTask.start();
                    animate();
                    runFlag = false;
                }
                if (melee) {
                    if (enemyDetected()) {
                        if (gameTime.getTime() - lastTimeAttacked >= MELEE_ATTACK_SPEED) {
                            changeState(State.ATTACK);
                            meleeAttackFlag = true;
                        }
                    }
                } else {
                    if (gameTime.getTime() - lastTimeAttacked >= RANGE_ATTACK_SPEED) {
                        changeState(State.ATTACK);
                        rangeAttackFlag = true;
                    }
                }
            }
            case ATTACK -> {
                if (melee) {
                    if (meleeAttackFlag) {
                        movementTask.stop();
                        animate();
                        meleeAttack();
                        meleeAttackFlag = false;
                    }
                    if (animation.isFinished()) {
                        movementTask.start();
                        changeState(State.RUN);
                        runFlag = true;
                    }
                }
                if (!melee) {
                    if (rangeAttackFlag) {
                        movementTask.stop();
                        animate();
                        rangeAttack();
                        rangeAttackFlag = false;
                    }
                    if (animation.isFinished()) {
                        movementTask.start();
                        changeState(State.RUN);
                        runFlag = true;
                    }
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
                    case DEFAULT -> owner.getEntity().getEvents().trigger("mob_default");
                }
        // switch (mobType) {
        //     case SKELETON -> {
        //         switch (state) {
        //             case RUN -> owner.getEntity().getEvents().trigger("skeleton_walk");
        //             case ATTACK -> owner.getEntity().getEvents().trigger("skeleton_attack");
        //             case DEATH -> owner.getEntity().getEvents().trigger("skeleton_death");
        //             case DEFAULT -> owner.getEntity().getEvents().trigger("skeleton_default");
        //         }
        //     }
        //     case WIZARD -> {
        //         switch (state) {
        //             case RUN -> owner.getEntity().getEvents().trigger("wizard_run");
        //             case ATTACK -> owner.getEntity().getEvents().trigger("wizard_attack");
        //             case DEATH -> owner.getEntity().getEvents().trigger("wizard_death");
        //             case DEFAULT -> owner.getEntity().getEvents().trigger("default");
        //         }
        //     }
        //     case WATER_QUEEN -> {
        //         switch (state) {
        //             case RUN -> owner.getEntity().getEvents().trigger("water_queen_walk");
        //             case ATTACK -> owner.getEntity().getEvents().trigger("water_queen_attack");
        //             case DEATH -> owner.getEntity().getEvents().trigger("water_queen_death");
        //             case DEFAULT -> owner.getEntity().getEvents().trigger("default");
        //         }
        //     }
        //     case WATER_SLIME -> {
        //         switch (state) {
        //             case RUN -> owner.getEntity().getEvents().trigger("water_slime_walk");
        //             case ATTACK -> owner.getEntity().getEvents().trigger("water_slime_attack");
        //             case DEATH -> {
        //                 owner.getEntity().getEvents().trigger("water_slime_death");
        //                 owner.getEntity().getEvents().trigger("splitDeath");
        //             }
        //             case DEFAULT -> owner.getEntity().getEvents().trigger("default");
        //         }
        //     }
        //     case FIRE_WORM -> {
        //         switch (state) {
        //             case RUN -> owner.getEntity().getEvents().trigger("fire_worm_walk");
        //             case ATTACK -> owner.getEntity().getEvents().trigger("fire_worm_attack");
        //             case DEATH -> owner.getEntity().getEvents().trigger("fire_worm_death");
        //             case DEFAULT -> owner.getEntity().getEvents().trigger("default");
        //         }
        //     }
        //     case DRAGON_KNIGHT -> {
        //         switch (state) {
        //             case RUN -> owner.getEntity().getEvents().trigger("dragon_knight_run");
        //             case ATTACK -> owner.getEntity().getEvents().trigger("dragon_knight_attack");
        //             case DEATH -> owner.getEntity().getEvents().trigger("dragon_knight_death");
        //             case DEFAULT -> owner.getEntity().getEvents().trigger("default");
        //         }
        //     }
        //     case COAT -> {
        //         switch (state) {
        //             case RUN -> owner.getEntity().getEvents().trigger("coat_run");
        //             case ATTACK -> owner.getEntity().getEvents().trigger("coat_attack");
        //             case DEATH -> owner.getEntity().getEvents().trigger("coat_death");
        //             case DEFAULT -> owner.getEntity().getEvents().trigger("default");
        //         }
        //     }
        // }
    }

    /**
     * changes state of the mob
     * @param state state to change current state to
     */
    private void changeState(State state) {
        prevState = this.state;
        this.state = state;
    }

    /**
     * detects if there's an enemy within range of 1 to the left of it
     * @return if there's an enemy in front of the mob or not
     */
    private boolean enemyDetected() {
        // if there's an entity within x of - 1 of mob
        Entity target = ServiceLocator.getEntityService().getEntityAtPosition(
                mob.getPosition().x - MELEE_ATTACK_RANGE, mob.getPosition().y);
        if (target == null) {
            return false;
        }

        // layer checking
        HitboxComponent targetHitbox = target.getComponent(HitboxComponent.class);
        if (targetHitbox == null) {
            return false;
        }
        if (PhysicsLayer.contains(PhysicsLayer.HUMANS, targetHitbox.getLayer())) {
            this.target = target;
            return true;
        }
        return false;
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
			if(owner.getEntity().getComponent(ArcaneArcherAnimationController.class)!=null)
			{
				Entity newProjectile = ProjectileFactory.createPierceArrow(PhysicsLayer.HUMANS, new Vector2(0, owner.getEntity().getPosition().y), new Vector2(2f,2f));
				newProjectile.setPosition((float) (owner.getEntity().getPosition().x), (float) (owner.getEntity().getPosition().y));
				newProjectile.setScale(1.3f, 1.3f);
				ServiceLocator.getEntityService().register(newProjectile);
            }
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
}
