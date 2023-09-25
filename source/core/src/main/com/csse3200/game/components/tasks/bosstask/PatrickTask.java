package com.csse3200.game.components.tasks.bosstask;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
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
    private static final float RANGE_MIN_X = 10f;
    private static final float RANGE_MAX_X = 18f;
    private static final float RANGE_MIN_Y = 1f;
    private static final float RANGE_MAX_Y = 6f;
    private static final int HALF_HEALTH_ATTACKS = 5;

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
    private boolean halfHealthFlag = false;
    private boolean teleportFlag = false;
    private boolean startFlag = false;
    private  enum PatrickState {
        IDLE, WALK, ATTACK, HURT, DEATH, SPELL, APPEAR
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

        // give game time to load
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                changeState(PatrickState.APPEAR);
                animate();
                startFlag = true;
                spawnFlag = true;
            }
        }, 0.1f);
    }

    @Override
    public void update() {
        // give game time to load
        if (!startFlag) {
            return;
        }
        animate();
        int health = patrick.getComponent(CombatStatsComponent.class).getHealth();

        // check if patrick has just teleported
        if (teleportFlag && teleportTask.getStatus().equals(Status.FINISHED)) {
            changeState(PatrickState.APPEAR);
            teleportFlag = false;
        }

        // detect half health
        if (health <= patrick.getComponent(CombatStatsComponent.class).getMaxHealth() / 2 &&
                !halfHealthFlag) {
            halfHealth();
            halfHealthFlag = true;
        }

        // handle state switches
        switch (state) {
            case APPEAR -> {
                if (spawnFlag) {
                    meleeAttack();
                    spawnFlag = false;
                } else if (meleeFlag) {
                    changeState(PatrickState.ATTACK);
                    meleeFlag = false;
                } else if (rangeFlag) {
                    changeState(PatrickState.IDLE);
                    rangeFlag = false;
                }
            }
            case IDLE -> {
                if (animation.isFinished()) {
                    rangeAttack();
                }
            }
            case ATTACK -> {
                if (animation.isFinished()) {
                    if (halfHealthFlag) {

                        break;
                    }
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
            case IDLE -> owner.getEntity().getEvents().trigger("patrick_idle");
            case WALK -> owner.getEntity().getEvents().trigger("patrick_walk");
            case HURT -> owner.getEntity().getEvents().trigger("patrick_hurt");
            case SPELL -> owner.getEntity().getEvents().trigger("patrick_spell");
            case APPEAR -> owner.getEntity().getEvents().trigger("patrick_death");
            case ATTACK -> owner.getEntity().getEvents().trigger("patrick_attack");
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
        teleportFlag = true;
        teleportTask = new PatrickTeleportTask(patrick, pos);
    }

    private void meleeAttack() {
        initialPos = patrick.getPosition();
        meleeTarget = ServiceLocator.getEntityService().getClosestHuman(patrick);
        System.out.println(ServiceLocator.getEntityService().getEntities());
        System.out.println(ServiceLocator.getEntityService().getClosestHuman(patrick));
        teleport(meleeTarget.getPosition());
        changeState(PatrickState.ATTACK);
        meleeFlag = true;
    }

    private void spawnRandProjectile(Vector2 destination) {
        // spawn random projectile
        Entity projectile = ProjectileFactory.createEffectProjectile(PhysicsLayer.HUMANS,
                destination, new Vector2(2, 2),
                getEffect(), false);
        projectile.setPosition(patrick.getPosition().x, patrick.getPosition().y);
        projectile.setScale(-1f, 1f);
        ServiceLocator.getEntityService().register(projectile);
        shotsFired++;
    }

    private void randomTeleport() {
        // teleport to random position
        float randomX = MathUtils.random(RANGE_MIN_X, RANGE_MAX_X);
        float randomY = MathUtils.random(RANGE_MIN_Y, RANGE_MAX_Y);
        teleport(new Vector2(randomX, randomY));
    }

    private void rangeAttack() {
        // detect if 3 shots have been fired
        if (shotsFired == 3) {
            shotsFired = 0;
            rangeFlag = false;
            spawnFlag = true;
        }
        randomTeleport();
        spawnRandProjectile(new Vector2(0f, patrick.getPosition().y));
    }

    private void halfHealth() {
        float startAngle = (float) Math.toRadians(135);
        float endAngle = (float) Math.toRadians(225);
        float angleIncrement = (endAngle - startAngle) / (HALF_HEALTH_ATTACKS - 1);

        for (int i = 0; i < HALF_HEALTH_ATTACKS; i++) {
            randomTeleport();

            // calculate unit vectors for projectiles
            float currentAngle = startAngle + i * angleIncrement;
            float x = MathUtils.cos(currentAngle) * 20;
            float y = MathUtils.sin(currentAngle) * 20;
            Vector2 destination = new Vector2(x, y);
            spawnRandProjectile(destination);
        }
        if (shotsFired == HALF_HEALTH_ATTACKS) {
            meleeFlag = true;
        }
    }
}
