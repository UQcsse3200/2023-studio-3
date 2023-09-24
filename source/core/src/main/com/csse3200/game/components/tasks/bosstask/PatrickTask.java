package com.csse3200.game.components.tasks.bosstask;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.ProjectileEffects;
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

public class PatrickTask extends DefaultTask implements PriorityTask {

    // Constants
    private static final int PRIORITY = 3;
    private static final Vector2 PATRICK_SPEED = new Vector2(1f, 1f);
    private static final float MAX_RADIUS = 20f;
    private static final int ATTACK_DAMAGE = 100;

    // Private variables
    private static final Logger logger = LoggerFactory.getLogger(PatrickTask.class);
    private Vector2 currentPos;
    private PhysicsEngine physics;
    private GameTime gameTime;
    private PatrickState state = PatrickState.IDLE;
    private PatrickState prevState;
    private AnimationRenderComponent animation;
    private Entity patrick;
    private int shotsFired;
    private PatrickTeleportTask teleportTask;
    private Vector2 initialPos;
    private Entity meleeTarget;

    // Flags
    private boolean meleeFlag = false;
    private boolean rangeFlag = false;
    private boolean spawnFlag = false;
    private  enum PatrickState {
        IDLE, WALK, ATTACK, HURT, DEATH, CAST, SPELL, APPEAR
    }

    public PatrickTask() {
        physics = ServiceLocator.getPhysicsService().getPhysics();
        gameTime = ServiceLocator.getTimeSource();
    }

    @Override
    public void start() {
        super.start();
        patrick = owner.getEntity();
        animation = owner.getEntity().getComponent(AnimationRenderComponent.class); // get animation
        currentPos = owner.getEntity().getPosition(); // get current position
        patrick.getComponent(PhysicsMovementComponent.class).setSpeed(PATRICK_SPEED); // set speed
        spawnFlag = true;
        changeState(PatrickState.APPEAR);
    }

    @Override
    public void update() {
        animate();

        if (teleportTask.getStatus().equals(Status.FINISHED)) {
            changeState(PatrickState.APPEAR);
        }

        switch (state) {
            case APPEAR -> {
                if (animation.isFinished()) {
                    if (spawnFlag) {
                        meleeAttack();
                        meleeFlag = true;
                        spawnFlag = false;
                    } else if (meleeFlag) {
                        changeState(PatrickState.ATTACK);
                        meleeFlag = false;
                    } else if (rangeFlag) {
                        changeState(PatrickState.IDLE);
                        rangeFlag = false;
                    }
                }
            }
            case IDLE -> {
                if (animation.isFinished()) {
                    if (shotsFired == 3) {
                        shotsFired = 0;
                        changeState(PatrickState.CAST);
                    }
                    Entity projectile = ProjectileFactory.createEffectProjectile(PhysicsLayer.HUMANS,
                            new Vector2(0f, patrick.getPosition().y), new Vector2(2, 2),
                            getEffect(), false);
                    projectile.setPosition(patrick.getPosition().x, patrick.getPosition().y);
                    projectile.setScale(-1f, 1f);
                    ServiceLocator.getEntityService().register(projectile);
                    changeState(PatrickState.IDLE);
                    shotsFired++;
                }
            }
            case ATTACK -> {
                if (animation.isFinished()) {
                    meleeTarget.getComponent(CombatStatsComponent.class).hit(ATTACK_DAMAGE);
                    teleport(initialPos);
                }
            }
        }
    }

    /**
     * Changes the state of patrick
     * @param state state to be changed to
     */
    private void changeState(PatrickState state) {
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
            case IDLE -> {

            }
            default -> logger.debug("Patrick animation {state} not found");
        }
        prevState = state;
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    private ProjectileEffects getEffect() {
        int randomNumber = MathUtils.random(0, 3);
        switch (randomNumber) {
            case 1 -> {
                return ProjectileEffects.BURN;
            }
            case 2 -> {
                return ProjectileEffects.SLOW;
            }
            case 3 -> {
                return ProjectileEffects.STUN;
            }
            default -> {
                return ProjectileEffects.FIREBALL;
            }
        }
    }

    private void teleport(Vector2 pos) {
        teleportTask = new PatrickTeleportTask(patrick, pos);
    }

    private void meleeAttack() {
        initialPos = patrick.getPosition();
        meleeTarget = ServiceLocator.getEntityService().getClosestHuman(patrick);
        teleport(meleeTarget.getPosition());
    }
}
