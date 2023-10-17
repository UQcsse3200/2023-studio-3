package com.csse3200.game.components.tasks.bosstask;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.DropFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IceBabyTask extends DefaultTask implements PriorityTask {
    /** Constant names */
    private static final int PRIORITY = 3;
    private static final Vector2 ICEBABY_SPEED = new Vector2(1f, 1f);
    private static final float MOVE_FORWARD_DELAY = 30;
    private static final int SMASH_RADIUS = 3;
    private static final int SMASH_DAMAGE = 30;
    private static final int ATK3_DAMAGE = 50;
    private static final float WALK_DISTANCE = 3.0f;
    private static final float STOP_DISTANCE = 0.1f;
    private static final int Y_TOP_BOUNDARY = 6;
    private static final int Y_BOT_BOUNDARY = 1;
    private static final Logger logger = LoggerFactory.getLogger(IceBabyTask.class);
    /** Variable names */
    private STATE prevState;
    private AnimationRenderComponent animation;
    private Entity iceBaby;
    private Vector2 currentPos;
    private Vector2 walkPos;
    private MovementTask walkTask;
    private int xRightBoundary = 17;
    private int xLeftBoundary = 12;
    private boolean startFlag = false;
    private boolean isWalking;
    /** Animation constants */
    private static final String IDLE = "startIdle";
    private static final String ATK1 = "start1_atk";
    private static final String ATK2 = "start2_atk";
    private static final String ATK3 = "start3_atk";
    private static final String DEATH = "startDeath";
    private static final String INTRO = "startIntro_or_revive";
    private static final String STAGGER = "startStagger";
    private static final String TAKEHIT = "startTake_hit";
    private static final String WALK = "startWalk";
    private enum STATE {
        IDLE, ATK1, ATK2, ATK3, DEATH, INTRO, STAGGER, TAKEHIT, WALK
    }
    private STATE iceBabyState = STATE.IDLE;

    //ice baby should be able to poop out little mobs - spawn new
    //ice baby can also do aoe attack based on the animation
    //ice baby does punches to towers once it is close

    /**
     * Starts the Task and triggers for Ice Baby to be spawned
     */
    @Override
    public void start() {
        super.start();
        iceBaby = owner.getEntity();
        animation = iceBaby.getComponent(AnimationRenderComponent.class);
        currentPos = iceBaby.getPosition();
        iceBaby.getComponent(PhysicsMovementComponent.class).setSpeed(ICEBABY_SPEED);
        iceBaby.getComponent(PhysicsMovementComponent.class).setNormalSpeed(ICEBABY_SPEED);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                changeState(STATE.INTRO);
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
     * Updates the boss to start attacking and spawning new mobs
     */
    @Override
    public void update() {
        if (!startFlag) {
            return;
        }
        animate();
        currentPos = iceBaby.getPosition();
        int health = iceBaby.getComponent(CombatStatsComponent.class).getHealth();

        // handle initial demon transformation
        if (animation.getCurrentAnimation().equals("intro_or_revive") && animation.isFinished()) {
            changeState(STATE.IDLE); // start sequence
        }
        if (health <= 0) {
            changeState(STATE.DEATH);
            animate();
            if (animation.isFinished()) {
                iceBaby.setFlagForDelete(true);
                dropCurrency();
            }
        }

        switch (iceBabyState) {
            case IDLE -> walk(getWalkPos());
            case WALK -> {
                   if (walkComplete()) {
                       changeState(STATE.IDLE);
                   } else {
                       spawnMob();
                   }
                }
            case ATK1, ATK2 -> {
                if (animation.isFinished()) {
                    atk3();
                }
            }
            case ATK3 -> {
                if (animation.isFinished()) {
                    changeState(STATE.IDLE);
                }
            }
            case DEATH, INTRO, STAGGER, TAKEHIT -> {}
        }
    }

    /**
     * Changes the state of animation
     * @param state - the new animation
     */
    private void changeState(STATE state) {
        prevState = this.iceBabyState;
        this.iceBabyState = state;
    }

    /**
     * Trigger the specific animation to play
     */
    private void animate() {
        // Check if same animation is being called
        if (prevState.equals(iceBabyState)) {
            return; // skip rest of function
        }

        switch (iceBabyState) {
            case IDLE -> iceBaby.getEvents().trigger(IDLE);
            case WALK -> iceBaby.getEvents().trigger(WALK);
            case DEATH -> iceBaby.getEvents().trigger(DEATH);
            case ATK1 -> iceBaby.getEvents().trigger(ATK1);
            case ATK2 -> iceBaby.getEvents().trigger(ATK2);
            case ATK3 -> iceBaby.getEvents().trigger(ATK3);
            case TAKEHIT -> iceBaby.getEvents().trigger(TAKEHIT);
            case INTRO -> iceBaby.getEvents().trigger(INTRO);
            case STAGGER -> iceBaby.getEvents().trigger(STAGGER);
            default -> logger.debug("iceBaby animation {state} not found");
        }
        prevState = iceBabyState;
    }

    /**
     * Changes state of Ice Baby and moves it to the desired position.
     *
     * @param finalPos position for demon to jump to
     */
    private void walk(Vector2 finalPos) {
        changeState(STATE.WALK);
        animate();
        isWalking = true;

        walkTask = new MovementTask(finalPos);
        walkTask.create(owner);
        walkTask.start();

        logger.debug("Ice baby walk starting");
    }

    /**
     * Returns a random position 3 units away for the ice Baby to walk to.
     *
     * @return a position 3 units away
     */
    private Vector2 getWalkPos() {
        // check if boundary has shifted causing demon to be out of bounds
        if (currentPos.x > xRightBoundary) {
            walkPos = new Vector2(currentPos.x - WALK_DISTANCE, currentPos.y); //jump back into boundary
            return walkPos;
        }

        float randomAngle = MathUtils.random(0, 2 * MathUtils.PI);
        float x = WALK_DISTANCE * MathUtils.cos(randomAngle);
        float y = WALK_DISTANCE * MathUtils.sin(randomAngle);

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
        walkPos = new Vector2(finalX, finalY);
        return walkPos;
    }

    /**
     * Returns a boolean to confirm whether the ice baby has completed a walk or not.
     *
     * @return true if demon has completed walk or not
     */
    private boolean walkComplete() {
        changeState(STATE.ATK1);
        animate();
        if (currentPos.dst(walkPos) <= STOP_DISTANCE && isWalking && animation.isFinished()) {
            applyAoeDamage(getNearbyHumans(SMASH_RADIUS), SMASH_DAMAGE); // do damage upon landing
            isWalking = false;
            walkTask.stop();
            return true;
        }
        return false;
    }

    /**
     * Changes the state of the animation and deals damage to nearby humans
     */
    private void atk3() {
        changeState(STATE.ATK3);
        animate();
        Entity target = ServiceLocator.getEntityService().getClosestEntityOfLayer(iceBaby,
                PhysicsLayer.HUMANS);
        CombatStatsComponent targetCombatStats = target.
                getComponent(CombatStatsComponent.class);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                targetCombatStats.hit(ATK3_DAMAGE);
            }
        }, 2f);
    }

    /**
     * Creates a new mob triggered with the correct animation
     */
    private void spawnMob() {
        changeState(STATE.ATK2);
        Entity newMob = NPCFactory.createSplittingWaterSlime(80);
        newMob.setPosition((float) (iceBaby.getPosition().x + 0.5), (float) (iceBaby.getPosition().y + 0.5));
        ServiceLocator.getEntityService().register(newMob);
        ServiceLocator.getWaveService().setEnemyCount(ServiceLocator.getWaveService().getEnemyCount() + 1);
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
     * Returns a list of nearby entities with PhysicsLayer.HUMAN.
     *
     * @return nearby entities with the PhysicsLayer of HUMAN
     */
    private Array<Entity> getNearbyHumans(int radius) {
        Array<Entity> nearbyEntities = ServiceLocator.getEntityService().
                getNearbyEntities(iceBaby, radius);
        Array<Entity> nearbyHumans = new Array<>();

        // iterate through nearby entities checking if they have desired properties
        for (int i = 0; i < nearbyEntities.size; i++) {
            Entity targetEntity = nearbyEntities.get(i);
            HitboxComponent targetHitbox = targetEntity.getComponent(HitboxComponent.class);
            // check target hitbox and layer
            if (targetHitbox == null || (!PhysicsLayer.contains(PhysicsLayer.HUMANS, targetHitbox.
                    getLayer()))) {
                break;
            }
            nearbyHumans.add(targetEntity);
        }
        return nearbyHumans;
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
