package com.csse3200.game.components.tasks.bosstask;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.ProjectileEffects;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.DropFactory;
import com.csse3200.game.entities.factories.MobBossFactory;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The AI Task for the demon boss entity. The demon boss will first play its transform animation
 * before beginning its sequence. Its sequence is based on its state and the different game
 * scenarios that happen in game dictate its state.
 */
public class DemonBossTask extends DefaultTask implements PriorityTask {

    // Constants
    private static final int PRIORITY = 3;
    private static final Vector2 DEMON_SPEED = new Vector2(1f, 1f);
    private static final float JUMP_DISTANCE = 3.0f;
    private static final double Y_TOP_BOUNDARY = 5.5;
    private static final double Y_BOT_BOUNDARY = 0.5;
    private static final int BREATH_ANIM_TIME = 2;
    private static final int SMASH_RADIUS = 3;
    private static final float MOVE_FORWARD_DELAY = 15;
    private static final float BREATH_DURATION = 4.2f;
    private static final int SMASH_DAMAGE = 30;
    private static final int CLEAVE_DAMAGE = 50;
    private static final int HEAL_TIMES = 10;
    private static final int HEALTH_TO_ADD = 10;
    private static final int SLIMEY_BOY_HEALTH = 500;
    private static final int SLIMES_SPAWNED = 2;
    private static final int SPAWN_RADIUS = 2;

    // Private variables
    private static final Logger logger = LoggerFactory.getLogger(DemonBossTask.class);
    private Vector2 currentPos;
    private MovementTask jumpTask;
    private DemonState state = DemonState.IDLE;
    private DemonState prevState;
    private AnimationRenderComponent animation;
    private Entity demon;
    private int numBalls = 3;
    private static int xRightBoundary = 17;
    private static int xLeftBoundary = 12;
    private ProjectileEffects effect = ProjectileEffects.BURN;
    private boolean aoe = true;
    private Array<Entity> nearbyEntities;

    // Flags
    private boolean startFlag = false;
    private boolean isJumping;
    private boolean halfHealthFlag = false;
    private boolean isHealing = false;
    private boolean isSpawning = false;

    /**
     * The different demon states.
     */
    private enum DemonState {
        TRANSFORM, IDLE, CAST, CLEAVE, DEATH, BREATH, SMASH, TAKE_HIT, WALK
    }

    /**
     * Starts transform animation, triggers idle animation which starts 
     * sequence, and dynamically shifts the demons boundary to the left.
     */
    @Override
    public void start() {
        super.start();
        demon = owner.getEntity();
        animation = demon.getComponent(AnimationRenderComponent.class); // get animation
        currentPos = demon.getPosition(); // get current position
        demon.getComponent(PhysicsMovementComponent.class).setSpeed(DEMON_SPEED); // set speed
        demon.getComponent(PhysicsMovementComponent.class).setNormalSpeed(DEMON_SPEED);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                changeState(DemonState.TRANSFORM);
                animate();
                demon.getEvents().trigger("demon_spawn_sound");
                startFlag = true;
            }
        }, 0.1f);

        // shift demon's boundary left every 30s
        for (int i = 1; i < 6; i++) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    xLeftBoundary -= 2;
                    xRightBoundary -= 2;
                }
            }, MOVE_FORWARD_DELAY * i);
        }
    }

    /**
     * This function is called every frame and is responsible for updating the 
     * animation, the position and the state scenarios.
     */
    @Override
    public void update() {
      // * Don't know if this is actually needed.
      if(ServiceLocator.getGameEndService().hasGameEnded()) {
        stop();
      }
        // give game time to load in then start
        if (!startFlag) {
            return;
        }

        animate();
        currentPos = demon.getPosition();
        int health = demon.getComponent(CombatStatsComponent.class).getHealth();

        // handle initial demon transformation
        if (animation.getCurrentAnimation().equals("transform") && animation.isFinished()) {
            changeState(DemonState.IDLE); // start sequence
        }

        // detect death stage
        if (health <= 0) {
            // spawn slimey boy
            Entity slimey = MobBossFactory.createSlimeyBoy(SLIMEY_BOY_HEALTH);
            slimey.setPosition(demon.getPosition().x, demon.getPosition().y);
            slimey.setScale(5f, 5f);
            ServiceLocator.getEntityService().register(slimey);
            demon.setFlagForDelete(true);
            dropCurrency();

        }

        // detect half health
        if (health <= demon.getComponent(CombatStatsComponent.class).getMaxHealth() / 2 &&
                !halfHealthFlag) {
            changeState(DemonState.TAKE_HIT);
        }

        // detect sequence changes and runs the relevant state accordingly
        switch (state) {
            case IDLE -> jump(getJumpPos());
            case SMASH -> {
                if (jumpComplete()) {
                    if (nearbyEntities != null && !nearbyEntities.isEmpty()) {
                        cleave();
                    } else {
                        fireBreath();
                    }
                }
            }
            case BREATH, CLEAVE -> {
                if (animation.isFinished()) {
                    changeState(DemonState.CAST);
                    isSpawning = true;
                    spawnDemonSlimes();
                }
            }
            case CAST -> {
                if (!isHealing && !isSpawning) {
                    changeState(DemonState.IDLE);
                }
            }
            case TRANSFORM -> {
                if (health <= 0 && animation.isFinished()) {
                    changeState(DemonState.DEATH);
                }
            }
            case TAKE_HIT -> {
                if (animation.isFinished()) {
                    halfHealth();
                    halfHealthFlag = true;
                }
            }
            case DEATH, WALK -> {}
        }
    }

    /**
     * Changes the state of the demon.
     * 
     * @param state state to be changed to
     */
    private void changeState(DemonState state) {
        prevState = this.state;
        this.state = state;
    }

    /**
     * Changes the animation of the demon if a state change occurs.
     */
    private void animate() {
        // Check if same animation is being called
        if (prevState.equals(state)) {
            return; // skip rest of function
        }

        switch (state) {
            case CAST -> demon.getEvents().trigger("demon_cast_spell");
            case IDLE -> demon.getEvents().trigger("demon_idle");
            case WALK -> demon.getEvents().trigger("demon_walk");
            case DEATH -> demon.getEvents().trigger("demon_death");
            case BREATH -> demon.getEvents().trigger("demon_fire_breath");
            case SMASH -> demon.getEvents().trigger("demon_smash");
            case CLEAVE -> demon.getEvents().trigger("demon_cleave");
            case TAKE_HIT -> demon.getEvents().trigger("demon_take_hit");
            case TRANSFORM -> demon.getEvents().trigger("transform");
            default -> logger.debug("Demon animation {state} not found");
        }
        prevState = state;
    }

    /**
     * Returns the priority of this task.
     * 
     * @return priority of task
     */
    @Override
    public int getPriority() {
        return PRIORITY;
    }

//    /**
//     * Returns a list of nearby entities with PhysicsLayer.HUMAN.
//     *
//     * @return nearby entities with the PhysicsLayer of HUMAN
//     */
//    private Array<Entity> getNearbyHumans(int radius) {
//        Array<Entity> nearbyEntities = ServiceLocator.getEntityService().
//                getNearbyEntities(demon, radius);
//        Array<Entity> nearbyHumans = new Array<>();
//
//        // iterate through nearby entities checking if they have desired properties
//        for (int i = 0; i < nearbyEntities.size; i++) {
//            Entity targetEntity = nearbyEntities.get(i);
//            HitboxComponent targetHitbox = targetEntity.getComponent(HitboxComponent.class);
//            if (targetHitbox == null) {
//                break;
//            }
//
//            // check target layer
//            if (!PhysicsLayer.contains(PhysicsLayer.HUMANS, targetHitbox.
//                    getLayer())) {
//                break;
//            }
//
//            nearbyHumans.add(targetEntity);
//        }
//        return nearbyHumans;
//    }

    /**
     * Changes state of demon and moves it to the desired position.
     * 
     * @param finalPos position for demon to jump to
     */
    private void jump(Vector2 finalPos) {
        changeState(DemonState.SMASH);
        isJumping = true;

        demon.getEvents().trigger("demon_roar_sound");

        // play landing sound
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                demon.getEvents().trigger("demon_landing_sound");
            }
        }, 1.8f);

        jumpTask = new MovementTask(finalPos);
        jumpTask.create(owner);
        jumpTask.start();

        logger.debug("Demon jump starting");
    }

    /**
     * Returns a random position 3 units away for the demon to jump to.
     *
     * @return a position 3 units away from the demon to jump to
     */
    private Vector2 getJumpPos() {
        // check if boundary has shifted causing demon to be out of bounds
        Vector2 jumpPos;
        if (currentPos.x > xRightBoundary) {
            jumpPos = new Vector2(currentPos.x - JUMP_DISTANCE, currentPos.y); //jump back into boundary
            return jumpPos;
        }

        // jump backwards if right next to tower
        if (currentPos.dst(ServiceLocator.getEntityService().getClosestEntityOfLayer(
                demon, PhysicsLayer.HUMANS).getPosition()) < 2f) {
            jumpPos = new Vector2(currentPos.x + JUMP_DISTANCE, currentPos.y);
            //TODO: Check whether jumpPos needs to be returned
        }

        float randomAngle = MathUtils.random(0, 2 * MathUtils.PI);
        float x = JUMP_DISTANCE * MathUtils.cos(randomAngle);
        float y = JUMP_DISTANCE * MathUtils.sin(randomAngle);

        // check boundaries
        if (x + currentPos.x > xRightBoundary || x + currentPos.x < xLeftBoundary) {
            x *= -1;
        }
        if (y + currentPos.y > Y_TOP_BOUNDARY || y + currentPos.y < Y_BOT_BOUNDARY) {
            y *= -1;
        }

        // get final jump position
        float finalX = x + currentPos.x;
        float finalY = y + currentPos.y;
        jumpPos = new Vector2(finalX, finalY);
        return jumpPos;
    }

    /**
     * Returns a boolean to confirm whether the demon has completed a jump or not.
     *
     * @return if demon has completed jump or not
     */
    private boolean jumpComplete() {
        if (animation.isFinished() && isJumping) {
            nearbyEntities = ServiceLocator.getEntityService().getEntitiesInRadiusOfLayer(
                    demon, SMASH_RADIUS, PhysicsLayer.HUMANS);
            applyAoeDamage(nearbyEntities, SMASH_DAMAGE); // do damage upon landing
            isJumping = false;
            jumpTask.stop();
            return true;
        }
//        if (currentPos.dst(jumpPos) <= STOP_DISTANCE && isJumping) {
//            applyAoeDamage(getNearbyHumans(SMASH_RADIUS), SMASH_DAMAGE); // do damage upon landing
//            isJumping = false;
//            jumpTask.stop();
//            return true;
//        }
        return false;
    }

    /**
     * Changes current breath attack with the given parameters.
     * 
     * @param numBalls numbers of projectiles to be fired
     * @param effect effect the projectile will apply
     * @param aoe whether the effect will be applied in a radius or not
     */
    public void changeBreathAttack(int numBalls, ProjectileEffects effect, boolean aoe) {
        this.numBalls = numBalls;
        this.effect = effect;
        this.aoe = aoe;
    }

    /**
     * Fire breath attack that launches an amount of projectiles with a given effect at the humans.
     */
    private void fireBreath() {
        changeState(DemonState.BREATH);
        demon.getEvents().trigger("demon_breath_in_sound");

        float delay = (BREATH_DURATION - BREATH_ANIM_TIME) / numBalls;

        float startAngle = (float) Math.toRadians(135);
        float endAngle = (float) Math.toRadians(225);
        float angleIncrement = (endAngle - startAngle) / (numBalls - 1);

        // spawn projectiles
        for (int i = 0; i < numBalls; i++) {
            // calculate unit vectors for projectiles
            float currentAngle = startAngle + i * angleIncrement;
            float x = MathUtils.cos(currentAngle) * 20;
            float y = MathUtils.sin(currentAngle) * 20;
            Vector2 destination = new Vector2(x, y);

            // Create burn projectiles
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                  // service locator getting a service could be anything here.
                  if(ServiceLocator.getTimeSource() == null) {
                    stop();
                    return; // prevent current iteration from running.
                  }
                    Entity projectile = ProjectileFactory.createEffectProjectile(PhysicsLayer.HUMANS, destination,
                            new Vector2(2, 2), effect, aoe);
                    projectile.setPosition(demon.getPosition().x, demon.getPosition().y);
                    projectile.setScale(-1f, 1f);
                    ServiceLocator.getEntityService().register(projectile);
                }
            }, delay * i + BREATH_ANIM_TIME);
        }
    }

    /**
     * Applies aoe damage to nearby human entities.
     * 
     * @param targets array of human entities to deal damage to
     */
    private void applyAoeDamage(Array<Entity> targets, int damage) {
        for (int i = 0; i < targets.size; i++) {
            Entity targetEntity = targets.get(i);

            CombatStatsComponent targetCombatStats = targetEntity.
                    getComponent(CombatStatsComponent.class);
            if (targetCombatStats != null) {
                targetCombatStats.hit(damage);
            }
        }
    }

    /**
     * Change state to cleave and deals damage to target
     */
    private void cleave() {
        changeState(DemonState.CLEAVE);
        demon.getEvents().trigger("demon_roar_sound");
        Entity target = ServiceLocator.getEntityService().getClosestEntityOfLayer(demon,
                PhysicsLayer.HUMANS);
        CombatStatsComponent targetCombatStats = target.
                getComponent(CombatStatsComponent.class);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                demon.getEvents().trigger("demon_cleave_sound");
                targetCombatStats.hit(CLEAVE_DAMAGE);
            }
        }, 2f);
    }

    /**
     * When at half health demon starts healing by a certain amount every second
     */
    private void halfHealth() {
        changeState(DemonState.CAST);
        isHealing = true;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                isHealing = false;
            }
        }, (float) HEAL_TIMES / 2);

        // add health every 10s
        for (int i = 0; i < HEAL_TIMES; i++) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    demon.getEvents().trigger("demon_heal_sound");
                    demon.getComponent(CombatStatsComponent.class).addHealth(HEALTH_TO_ADD);
                }
            }, (float) i /2);
        }
    }

    private void spawnDemonSlimes() {
        for (int i = 0; i < SLIMES_SPAWNED; i++) {
            demon.getEvents().trigger("spawn_demon_slime");
            int finalI = i;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                Entity slime = MobBossFactory.createSlimeyBoy(100);
                float angle = MathUtils.random(0f, MathUtils.PI2);
                float distance = MathUtils.random(0f, SPAWN_RADIUS);

                float x = demon.getPosition().x + distance * MathUtils.cos(angle);
                float y = demon.getPosition().y + distance * MathUtils.sin(angle);

                // boundary check
                if (x > xRightBoundary || x < xLeftBoundary) {
                    x = demon.getPosition().x;
                }
                if (y > Y_TOP_BOUNDARY || y < Y_BOT_BOUNDARY) {
                    y = demon.getPosition().y;
                }

                Vector2 spawnLocation = new Vector2(x, y);
                slime.setPosition(spawnLocation);
                ServiceLocator.getEntityService().register(slime);

                if (finalI == SLIMES_SPAWNED - 1) {
                    isSpawning = false;
                }
                }
            }, (float) (i + 1) * 2);
        }
    }

    private void dropCurrency() {
        // Create and register 5 crystal drops around the bossPosition
        for (int i = 0; i < 5; i++) {
            Entity crystal = DropFactory.createCrystalDrop();

            // Calculate positions around the bossPosition
            float offsetX = MathUtils.random(-1f, 1f); // Adjust the range as needed
            float offsetY = MathUtils.random(-1f, 1f);
            float dropX = owner.getEntity().getPosition().x + offsetX;
            float dropY = owner.getEntity().getPosition().y + offsetY;
            crystal.setPosition(dropX, dropY);
            ServiceLocator.getEntityService().register(crystal);
        }
    }
}