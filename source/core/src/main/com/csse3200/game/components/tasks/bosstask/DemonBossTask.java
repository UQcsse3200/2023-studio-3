package com.csse3200.game.components.tasks.bosstask;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.ProjectileEffects;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.components.tasks.WaitTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.math.MathUtils;

public class DemonBossTask extends DefaultTask implements PriorityTask {

    // Constants
    private static final int PRIORITY = 3;
    private static final Vector2 DEMON_JUMP_SPEED = new Vector2(2f, 2f);
    private static final float STOP_DISTANCE = 0.1f;
    private static final float TIME_INTERVAL = 10f; // 10 seconds
    private static final int BURN_BALLS = 5;
    private static final int X_LENGTH = 20; // for projectile destination calculations

    // Private variables
    private static final Logger logger = LoggerFactory.getLogger(DemonBossTask.class);
    private Vector2 currentPos;
    private final PhysicsEngine physics;
    private final GameTime gameTime;
    private Vector2 jumpPos;
    private MovementTask jumpTask;
    private boolean isJumping;
    private DEMON_STATE state = DEMON_STATE.IDLE;
    private DEMON_STATE prevState;
    private AnimationRenderComponent animation;
    private Entity demon;
    private float elapsedTime = 0f;
    Array<Double> yArray = new Array<>();
    private boolean isBreath;
    private boolean waitFlag = false;

    private enum DEMON_STATE {
        TRANSFORM, IDLE, CAST, CLEAVE, DEATH, BREATH, SMASH, TAKE_HIT, WALK
    }

    public DemonBossTask() {
        physics = ServiceLocator.getPhysicsService().getPhysics();
        gameTime = ServiceLocator.getTimeSource();
    }

    @Override
    public void start() {
        super.start();
        demon = owner.getEntity();
        changeState(DEMON_STATE.TRANSFORM);
        animation = owner.getEntity().getComponent(AnimationRenderComponent.class); // get animation
        currentPos = owner.getEntity().getPosition(); // get current position

        // temporary fix for not wait task and NORMAL not working
        animate();
        if (animation.getCurrentAnimation().equals("transform")) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    changeState(DEMON_STATE.IDLE);
                }
            }, 6.4f); // Delay in seconds
        }
    }

    @Override
    public void update() {
        animate();
        System.out.println(state);
        currentPos = demon.getPosition();

        switch (state) {
            case IDLE -> {jump(getJumpPos());}
            case SMASH -> {
                if (jumpComplete()) {
                    fireBreath();
                    isBreath = true;
                }
            }
            case BREATH -> {
                if (!isBreath) {
                    changeState(DEMON_STATE.IDLE);
                    waitFlag = false;
                } else {
                    if (waitFlag) {
                        return;
                    }
                    waitTask();
                }
            }
        }
    }

    private void waitTask() {
        waitFlag = true;
        // to be replaced by wait task
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                isBreath = false;
            }
        }, 4.2f); // Delay in seconds
    }
    private void changeState(DEMON_STATE state) {
        prevState = this.state;
        this.state = state;
    }

    private void animate() {
        // Check if same animation is being called
        if (prevState.equals(state)) {
            return; // skip rest of function
        }

        switch (state) {
            case CAST -> {demon.getEvents().trigger("demon_cast_spell");}
            case IDLE -> demon.getEvents().trigger("demon_idle");
            case WALK -> demon.getEvents().trigger("demon_walk");
            case DEATH -> demon.getEvents().trigger("demon_death");
            case BREATH -> demon.getEvents().trigger("demon_breath");
            case SMASH -> demon.getEvents().trigger("demon_smash");
            case CLEAVE -> demon.getEvents().trigger("demon_cleave");
            case TAKE_HIT -> demon.getEvents().trigger("demon_take_hit");
            case TRANSFORM -> demon.getEvents().trigger("transform");
            default -> logger.debug("Demon animation {state} not found");
        }

        prevState = state;
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    private void jump(Vector2 finalPos) {
        changeState(DEMON_STATE.SMASH);

        jumpTask = new MovementTask(finalPos);
        jumpTask.create(owner);
        demon.getComponent(PhysicsMovementComponent.class).setSpeed(DEMON_JUMP_SPEED);
        jumpTask.start();
        isJumping = true;

        logger.debug("Demon jump starting");

    }

    private Vector2 getJumpPos() {
        // check where demon can jump
        float jumpMinX = currentPos.x - 4;
        float jumpMaxX = currentPos.x + 4;
        float jumpMinY = currentPos.y - 4;
        float jumpMaxY = currentPos.y + 4;

        if (jumpMinX < 1) {
            jumpMinX = 1;
        } else if (jumpMaxX > 18) {
            jumpMaxX = 18;
        } else if (jumpMinY < 1) {
            jumpMinX = 1;
        } else if (jumpMinY > 7) {
            jumpMaxY = 7;
        }

        // generate random jump pos
        float randomX = MathUtils.random(jumpMinX, jumpMaxX);
        float randomY = MathUtils.random(jumpMinY, jumpMaxY);
        return jumpPos = new Vector2(randomX, randomY);
    }

    private boolean isAtTarget() {
        System.out.println(currentPos);
        System.out.println(jumpPos);
        return currentPos.dst(jumpPos) <= STOP_DISTANCE;
    }

    private boolean jumpComplete() {
        if (isAtTarget() && isJumping) {
            isJumping = false;
            jumpTask.stop();
            return true;
        }
        return false;
    }

    private void fireBreath() {
        changeState(DEMON_STATE.BREATH);

        // add constant y changes to burn projectile
        yArray.add(Math.sqrt(3));
        yArray.add(1/Math.sqrt(3));
        yArray.add(0d);
        yArray.add(-1/Math.sqrt(3));
        yArray.add(-Math.sqrt(3));

        // spawn breath balls
        for (int i = 0; i < BURN_BALLS; i++) {

            // calculate destination of burn balls
            float x = demon.getPosition().x - X_LENGTH;
            float y = (float) (demon.getPosition().y + yArray.get(i) * X_LENGTH);
            Vector2 destination = new Vector2(x, y);

            // create burn projectiles
            Entity projectile = ProjectileFactory.createEffectProjectile(PhysicsLayer.HUMANS, destination,
                    new Vector2(2,2), ProjectileEffects.BURN, false);
            projectile.setPosition(demon.getPosition().x, demon.getPosition().y);
            ServiceLocator.getEntityService().register(projectile);
        }
    }
}
