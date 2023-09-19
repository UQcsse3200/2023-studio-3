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
    private static final Vector2 DEMON_JUMP_SPEED = new Vector2(1f, 1f);
    private static final float STOP_DISTANCE = 0.1f;
    private static final int BURN_BALLS = 5;
    private static final int X_LENGTH = 20; // for projectile destination calculations
    private static final float JUMP_DISTANCE = 3.0f;
    private static final int xRightBoundary = 17;
    private static final int xLeftBoundary = 1;
    private static final int yTopBoundary = 6;
    private static final int yBotBoundary = 1;

    // Private variables
    private static final Logger logger = LoggerFactory.getLogger(DemonBossTask.class);
    private Vector2 currentPos;
    private final PhysicsEngine physics;
    private final GameTime gameTime;
    private Vector2 jumpPos;
    private MovementTask jumpTask;
    private boolean isJumping;
    private DemonState state = DemonState.IDLE;
    private DemonState prevState;
    private AnimationRenderComponent animation;
    private Entity demon;
    private boolean waitFlag = false;
    private boolean timerFlag;
    private int numBalls = 5;
    private ProjectileEffects effect = ProjectileEffects.FIREBALL;
    private boolean aoe = true;

    // Enums
    private enum AnimState {
        TRANSFORM(6.4f),
        CLEAVE(3f),
        DEATH(4.4f),
        BREATH(4.2f),
        SMASH(3.6f),
        TAKE_HIT(1f);

        private final float duration;

        private AnimState(float duration) {
            this.duration = duration;
        }

        public float getDuration() {
            return duration;
        }
    }

    private enum DemonState {
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
        animation = owner.getEntity().getComponent(AnimationRenderComponent.class); // get animation
        currentPos = owner.getEntity().getPosition(); // get current position

        // Handle initial transform animation
        changeState(DemonState.TRANSFORM);
        animate();
        if (animation.getCurrentAnimation().equals("transform")) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    changeState(DemonState.IDLE);
                }
            }, 6.4f); // Delay in seconds
        }
    }

    @Override
    public void update() {
        animate();
        currentPos = demon.getPosition();

        switch (state) {
            case IDLE -> { jump(getJumpPos()); }
            case SMASH -> {
                if (jumpComplete()) {
                    fireBreath();
                }
            }
            case BREATH -> {
                if (!timerFlag) {
                    waitTask(AnimState.BREATH.getDuration());
                    timerFlag = true;
                }
                if (!waitFlag) {
                    changeState(DemonState.IDLE);
                    timerFlag = false;
                }
            }
        }
    }

    /**
     * Starts a timer when called and returns true. When timer is complete,
     * false will be returned
     * @param duration time to set the timer for
     * @return true or false depending if the timer is on
     */
    private void waitTask(float duration) {
        waitFlag = true;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                waitFlag = false;
            }
        }, duration);
    }
    private void changeState(DemonState state) {
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
            case BREATH -> demon.getEvents().trigger("demon_fire_breath");
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
        changeState(DemonState.SMASH);
        isJumping = true;

        jumpTask = new MovementTask(finalPos);
        jumpTask.create(owner);
        demon.getComponent(PhysicsMovementComponent.class).setSpeed(DEMON_JUMP_SPEED);
        jumpTask.start();

        logger.debug("Demon jump starting");
    }

    private Vector2 getJumpPos() {
        float randomAngle = MathUtils.random(0, 2 * MathUtils.PI);
        float x = JUMP_DISTANCE * MathUtils.cos(randomAngle);
        float y = JUMP_DISTANCE * MathUtils.sin(randomAngle);

        // check boundaries
        if (x + currentPos.x > xRightBoundary || x + currentPos.x < xLeftBoundary) { x *= -1; }
        if (y + currentPos.y > yTopBoundary || y + currentPos.y < yBotBoundary) { y *= -1; }

        // get final jump position
        float finalX = x + currentPos.x;
        float finalY = y + currentPos.y;
        jumpPos = new Vector2(finalX, finalY);
        return jumpPos;
    }

    private boolean isAtTarget() {
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

    public void changeBreathAttack(int numBalls, ProjectileEffects effect, boolean aoe) {
        this.numBalls = numBalls;
        this.effect = effect;
        this.aoe = aoe;
    }

    private void fireBreath() {
        changeState(DemonState.BREATH);

        long delay = (long) AnimState.BREATH.getDuration() / numBalls;

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
            Entity projectile = ProjectileFactory.createEffectProjectile(PhysicsLayer.HUMANS, destination,
                    new Vector2(2, 2), effect, aoe);
            projectile.setPosition(demon.getPosition().x, demon.getPosition().y);
            ServiceLocator.getEntityService().register(projectile);
        }
    }
}
