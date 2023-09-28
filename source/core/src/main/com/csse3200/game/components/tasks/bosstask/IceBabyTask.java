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
import com.csse3200.game.entities.factories.MobBossFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import net.dermetfan.gdx.physics.box2d.PositionController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.text.DecimalFormat;

public class IceBabyTask extends DefaultTask implements PriorityTask {
    private static final int PRIORITY = 3;
    private static final Vector2 ICEBABY_SPEED = new Vector2(1f, 1f);
    private static final int MOVE_FORWARD_DELAY = 30;
    private static final int SMASH_RADIUS = 3;
    private static final int SMASH_DAMAGE = 30;
    private static final int ATK3_DAMAGE = 50;
    private static final float WALK_DISTANCE = 3.0f;
    private static final float STOP_DISTANCE = 0.1f;
    private static final int Y_TOP_BOUNDARY = 6;
    private static final int Y_BOT_BOUNDARY = 1;
    private static final Logger logger = LoggerFactory.getLogger(IceBabyTask.class);
    private PhysicsEngine physics;
    private GameTime gameTime;
    private STATE iceBabyState = STATE.IDLE;
    private STATE prevState;
    private AnimationRenderComponent animation;
    private Entity iceBaby;
    private Vector2 currentPos;
    private Vector2 walkPos;
    private MovementTask walkTask;
    private static int xRightBoundary = 17;
    private static int xLeftBoundary = 12;
    private boolean aoe = true;
    private boolean startFlag = false;
    private boolean isWalking;
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

    public IceBabyTask() {
        physics = ServiceLocator.getPhysicsService().getPhysics();
        gameTime = ServiceLocator.getTimeSource();
    }

    //ice baby should be able to poop out little mobs - spawn new
    //ice baby can also do aoe attack based on the animation
    //ice baby does punches to towers once it is close

    @Override
    public void start() {
        super.start();
        iceBaby = owner.getEntity();
        animation = iceBaby.getComponent(AnimationRenderComponent.class);
        currentPos = iceBaby.getPosition();
        iceBaby.getComponent(PhysicsMovementComponent.class).setSpeed(ICEBABY_SPEED);
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

    private void changeState(STATE iceBabyState) {
        prevState = this.iceBabyState;
        this.iceBabyState = iceBabyState;
    }

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

    @Override
    public void update() {
        if (!startFlag) {
            return;
        }
        animate();
        currentPos = iceBaby.getPosition();
        int health = iceBaby.getComponent(CombatStatsComponent.class).getHealth();

        // handle initial demon transformation
        if (animation.getCurrentAnimation().equals(INTRO) && animation.isFinished()) {
            changeState(IceBabyTask.STATE.IDLE); // start sequence
        }
        if (health <= 0) {
            changeState(iceBabyState.DEATH);
            iceBaby.setFlagForDelete(true);
        }

        switch (iceBabyState) {
            case IDLE -> walk(getWalkPos());
            case ATK1 -> {
                   if (walkComplete()) {
                       ATK3();
                   } else {
                       spawnMob();
                   }
                }
            case ATK2, ATK3-> {
                if (animation.isFinished()) {
                    changeState(iceBabyState.IDLE);
                }
            }
        }
    }

    private void walk(Vector2 finalPos) {
        changeState(IceBabyTask.STATE.ATK1);
        isWalking = true;

        walkTask = new MovementTask(finalPos);
        walkTask.create(owner);
        walkTask.start();

        logger.debug("Ice baby walk starting");
    }

    /**
     * Returns a random position 3 units away for the demon to jump to.
     *
     * @return a position 3 units away from the demon to jump to
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

    private boolean walkComplete() {
        changeState(IceBabyTask.STATE.ATK1);
        if (currentPos.dst(walkPos) <= STOP_DISTANCE && isWalking) {
            applyAoeDamage(getNearbyHumans(SMASH_RADIUS), SMASH_DAMAGE); // do damage upon landing
            isWalking = false;
            walkTask.stop();
            return true;
        }
        return false;
    }

    private void ATK3() {
        changeState(IceBabyTask.STATE.ATK3);
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

    private void spawnMob() {
        changeState(iceBabyState.ATK2);
        Entity newMob = NPCFactory.createSplittingWaterSlime();
        newMob.setPosition((float) (iceBaby.getPosition().x + 0.5), (float) (iceBaby.getPosition().y + 0.5));
        ServiceLocator.getEntityService().register(newMob);
    }

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

    private Array<Entity> getNearbyHumans(int radius) {
        Array<Entity> nearbyEntities = ServiceLocator.getEntityService().
                getNearbyEntities(iceBaby, radius);
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


    @Override
    public int getPriority() {
        return PRIORITY;
    }

}
