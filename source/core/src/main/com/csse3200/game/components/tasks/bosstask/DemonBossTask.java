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
import com.csse3200.game.entities.factories.MobBossFactory;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.GameTime;
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
    private static final float STOP_DISTANCE = 0.1f;
    private static final float JUMP_DISTANCE = 3.0f;
    private static final int Y_TOP_BOUNDARY = 6;
    private static final int Y_BOT_BOUNDARY = 1;
    private static final int BREATH_ANIM_TIME = 2;
    private static final int SMASH_RADIUS = 3;
    private static final int MOVE_FORWARD_DELAY = 30;
    private static final float BREATH_DURATION = 4.2f;
    private static final int SMASH_DAMAGE = 30;
    private static final int CLEAVE_DAMAGE = 50;
    private static final int HEAL_TIMES = 10;
    private static final int HEALTH_TO_ADD = 10;

    // Private variables
    private static final Logger logger = LoggerFactory.getLogger(DemonBossTask.class);
    private Vector2 currentPos;
    private final PhysicsEngine physics;
    private final GameTime gameTime;
    private Vector2 jumpPos;
    private MovementTask jumpTask;
    private DemonState state = DemonState.IDLE;
    private DemonState prevState;
    private AnimationRenderComponent animation;
    private Entity demon;
    private int numBalls = 6;
    private static int xRightBoundary = 17;
    private static int xLeftBoundary = 12;
    private ProjectileEffects effect = ProjectileEffects.BURN;
    private boolean aoe = true;

    // Flags
    private boolean startFlag = false;
    private boolean isJumping;
    private boolean halfHealthFlag = false;
    private boolean isHealing = false;

    /**
     * The different demon states.
     */
    private enum DemonState {
        TRANSFORM, IDLE, CAST, CLEAVE, DEATH, BREATH, SMASH, TAKE_HIT,
        WALK, TRANSFORM_REVERSE, SLIME_IDLE, SLIME_MOVE, PROJECTILE_EXPLOSION,
        PROJECTILE_IDLE, SLIME_TAKE_HIT
    }

    /**
     * The demon boss task constructor. Initialises the physics and time.
     */
    public DemonBossTask() {
        physics = ServiceLocator.getPhysicsService().getPhysics();
        gameTime = ServiceLocator.getTimeSource();
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

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                changeState(DemonState.TRANSFORM);
                animate();
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

        // detect death stages
        if (health <= 0) {
            // spawn slimey boy
            Entity slimey = MobBossFactory.createSlimeyBoy();
            slimey.setPosition(demon.getPosition().x, demon.getPosition().y);
            slimey.setScale(5f, 5f);
            ServiceLocator.getEntityService().register(slimey);
            demon.setFlagForDelete(true);
        }

        // detect half health
        if (health <= demon.getComponent(CombatStatsComponent.class).getMaxHealth() / 2 &&
                !halfHealthFlag) {
            halfHealth();
            halfHealthFlag = true;
        }

        // detect sequence changes and runs the relevant state accordingly
        switch (state) {
            case IDLE -> jump(getJumpPos());
            case SMASH -> {
                if (jumpComplete()) {
                    if (getNearbyHumans(SMASH_RADIUS).isEmpty()) {
                        fireBreath();
                    }
                    else {
                        cleave();
                    }
                }
            }
            case BREATH, CLEAVE -> {
                if (animation.isFinished()) {
                    changeState(DemonState.IDLE);
                }
            }
            case CAST -> {
                if (!isHealing) {
                    changeState(DemonState.IDLE);
                }
            }
            case TRANSFORM -> {
                if (health <= 0) {
                    if (animation.isFinished()) {
                        changeState(DemonState.DEATH);
                    }
                }
            }
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
            case TRANSFORM_REVERSE -> demon.getEvents().trigger("transform_reverse");
            case SLIME_IDLE -> demon.getEvents().trigger("idle");
            case SLIME_MOVE -> demon.getEvents().trigger("move");
            case SLIME_TAKE_HIT -> demon.getEvents().trigger("take_hit");
            case PROJECTILE_IDLE -> demon.getEvents().trigger("projectile_explosion");
            case PROJECTILE_EXPLOSION -> demon.getEvents().trigger("projectile_idle");
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

    /**
     * Returns a list of nearby entities with PhysicsLayer.HUMAN.
     * 
     * @return nearby entities with the PhysicsLayer of HUMAN
     */
    private Array<Entity> getNearbyHumans(int radius) {
        Array<Entity> nearbyEntities = ServiceLocator.getEntityService().
                getNearbyEntities(demon, radius);
        Array<Entity> nearbyHumans = new Array<>();

        // iterate through nearby entities checking if they have desired properties
        for (int i = 0; i < nearbyEntities.size; i++) {
            Entity targetEntity = nearbyEntities.get(i);
            HitboxComponent targetHitbox = targetEntity.getComponent(HitboxComponent.class);
            if (targetHitbox == null) {
                break;
            }

            // check target layer
            if (!PhysicsLayer.contains(PhysicsLayer.HUMANS, targetHitbox.
                    getLayer())) {
                break;
            }

            nearbyHumans.add(targetEntity);
        }
        return nearbyHumans;
    }

    /**
     * Changes state of demon and moves it to the desired position.
     * 
     * @param finalPos position for demon to jump to
     */
    private void jump(Vector2 finalPos) {
        changeState(DemonState.SMASH);
        isJumping = true;

        jumpTask = new MovementTask(finalPos);
        jumpTask.create(owner);
        jumpTask.start();

        logger.debug("Demon jump starting");
    }

    /**
     * Returns a a random position 3 units away for the demon to jump to.
     * 
     * @return a position 3 units away from the demon to jump to
     */
    private Vector2 getJumpPos() {
        // check if boundary has shifted causing demon to be out of bounds
        if (currentPos.x > xRightBoundary) {
            jumpPos = new Vector2(currentPos.x - JUMP_DISTANCE, currentPos.y); //jump back into boundary
            return jumpPos;
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
        if (currentPos.dst(jumpPos) <= STOP_DISTANCE && isJumping) {
            applyAoeDamage(getNearbyHumans(SMASH_RADIUS), SMASH_DAMAGE); // do damage upon landing
            isJumping = false;
            jumpTask.stop();
            return true;
        }
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
     * Returns the closest human entity from a given array.
     * 
     * @param targets array of human entities
     * @return closest human entity
     */
    private Entity getClosestHuman(Array<Entity> targets) {
        Entity closestEntity = null;
        float closestDistance = SMASH_RADIUS;

        for (int i = 0; i < targets.size; i++) {
            Entity targetEntity = targets.get(i);
            Vector2 targetPosition = targetEntity.getPosition();
            float distance = currentPos.dst(targetPosition);

            if (distance < closestDistance) {
                closestEntity = targetEntity;
                closestDistance = distance;
            }
        }
        return closestEntity;
    }

    /**
     * Change state to cleave and deals damage to target.
     */
    private void cleave() {
        changeState(DemonState.CLEAVE);
        Entity target = ServiceLocator.getEntityService().getClosestEntityOfLayer(demon,
                PhysicsLayer.HUMANS);
        CombatStatsComponent targetCombatStats = target.
                getComponent(CombatStatsComponent.class);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
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
        }, HEAL_TIMES);

        // add health every 10s
        for (int i = 0; i < HEAL_TIMES; i++) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    demon.getComponent(CombatStatsComponent.class).addHealth(HEALTH_TO_ADD);
                }
            }, (float) i /2);
        }
    }
}

