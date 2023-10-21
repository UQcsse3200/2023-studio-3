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
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlimeyBoyTask extends DefaultTask implements PriorityTask {

    // Constants
    private static final int PRIORITY = 3;
    private static final Vector2 SLIMEY_SPEED = new Vector2(0.5f, 0.5f);
    private static final Vector2 DEFAULT_POS = new Vector2(0, 4);
    private static final float MAX_RADIUS = 40f;

    // Private variables
    private static final Logger logger = LoggerFactory.getLogger(SlimeyBoyTask.class);
    private Entity slimey;
    private AnimationRenderComponent animation;
    private Vector2 currentPos;
    private SlimeState state = SlimeState.IDLE; // set initial state to random unused state
    private SlimeState prevState;
    private Entity targetEntity;
    private final GameTime gameTime;
    private long lastTimeBounced;
    private static final long BOUNCE_TIME = 1600;

    /**
     * States that the slime cycles through
     */
    private enum SlimeState {
        IDLE, MOVE, PROJECTILE_EXPLOSION, PROJECTILE_IDLE, TAKE_HIT, TRANSFORM
    }

    /**
     * Constructor for this task
     */
    public SlimeyBoyTask() {
        gameTime = ServiceLocator.getTimeSource();
    }

    /**
     * What is called when task is assigned
     */
    @Override
    public void start() {
        super.start();
        slimey = owner.getEntity();
        animation = owner.getEntity().getComponent(AnimationRenderComponent.class); // get animation
        currentPos = owner.getEntity().getPosition(); // get current position
        slimey.getComponent(PhysicsMovementComponent.class).setSpeed(SLIMEY_SPEED); // set speed
        changeState(SlimeState.TRANSFORM);
        slimey.getEvents().trigger("demon_death_sound");
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                slimey.getEvents().trigger("slime_pop_sound");
            }
        }, 6f);
    }

    /**
     * What is run every frame update
     */
    @Override
    public void update() {
        animate();
        currentPos = slimey.getPosition();
        int health = slimey.getComponent(CombatStatsComponent.class).getHealth();

        switch (state) {
            case TRANSFORM -> {
                if (animation.isFinished()) {
                    seekAndDestroy();
                }
            }
            case MOVE -> {
                if (targetFound()) {
                    // do aoe damage based on how much health slime has left
                    applyAoeDamage(ServiceLocator.getEntityService().getEntitiesInRadiusOfLayer(
                            slimey, MAX_RADIUS, PhysicsLayer.HUMANS), health);
                    changeState(SlimeState.TAKE_HIT);
                }
                if (gameTime.getTime() - lastTimeBounced >= BOUNCE_TIME) {
                    lastTimeBounced = gameTime.getTime();
                    slimey.getEvents().trigger("slime_jump_sound");
                }
            }
            case TAKE_HIT -> {
                slimey.getEvents().trigger("slimey_splat_sound");
                slimey.setFlagForDelete(true);
            }
            case IDLE, PROJECTILE_EXPLOSION, PROJECTILE_IDLE -> {}
        }
    }

    /**
     * Changes the state of the demon
     * @param state state to be changed to
     */
    private void changeState(SlimeState state) {
        prevState = this.state;
        this.state = state;
    }

    /**
     * Changes the animation of the demon if a state change occurs
     */
    private void animate() {
        // Check if same animation is being called
        if (prevState.equals(state)) {
            return; // skip rest of function
        }

        switch (state) {
            case IDLE -> slimey.getEvents().trigger("idle");
            case MOVE -> slimey.getEvents().trigger("move");
            case PROJECTILE_EXPLOSION -> slimey.getEvents().trigger("projectile_explosion");
            case PROJECTILE_IDLE -> slimey.getEvents().trigger("projectile_idle");
            case TAKE_HIT -> slimey.getEvents().trigger("take_hit");
            case TRANSFORM -> slimey.getEvents().trigger("transform");
            default -> logger.debug("Slimey boy animation {state} not found");
        }
        prevState = state;
    }

    /**
     * Find the closest human entity and start moving towards them
     */
    private void seekAndDestroy() {
        lastTimeBounced = gameTime.getTime() - BOUNCE_TIME;
        changeState(SlimeState.MOVE);

        targetEntity = ServiceLocator.getEntityService().getClosestEntityOfLayer(
                slimey, PhysicsLayer.HUMANS);
        Vector2 targetPos;
        if (targetEntity == null) {
            targetPos = DEFAULT_POS;
        } else {
            targetPos = targetEntity.getPosition();
        }
        MovementTask slimeyMovementTask = new MovementTask(targetPos);
        slimeyMovementTask.create(owner);
        slimeyMovementTask.start();
        slimey.getComponent(PhysicsMovementComponent.class).setSpeed(SLIMEY_SPEED);
    }

    /**
     * @return if target has been reached or not
     */
    private boolean targetFound() {
        if (targetEntity == null) {
            return false;
        }
        return currentPos.dst(targetEntity.getPosition()) < 1;
    }

    /**
     * Applies aoe damage to nearby human entities
     * @param targets array of human entities to deal damage to
     */
    private void applyAoeDamage(Array<Entity> targets, int damage) {
        for (int i = 0; i < targets.size; i++) {
            CombatStatsComponent targetCombatStats = targets.get(i).getComponent(CombatStatsComponent.class);
            if (targetCombatStats != null) {
                targetCombatStats.hit(damage);
            }
        }
    }

    /**
     * @return priority of class
     */
    @Override
    public int getPriority() {
        return PRIORITY;
    }


}
