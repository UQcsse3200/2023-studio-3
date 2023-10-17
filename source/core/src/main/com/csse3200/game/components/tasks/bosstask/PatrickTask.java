package com.csse3200.game.components.tasks.bosstask;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.ProjectileEffects;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.MobBossFactory;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Patrick boss task that controls the boss' sequence and actions based on a predetermined sequence
 * and the boss' current hp
 */
public class PatrickTask extends DefaultTask implements PriorityTask {

    // Constants
    private static final int PRIORITY = 3;
    private static final Vector2 PATRICK_SPEED = new Vector2(1f, 1f);
    private static final int ATTACK_DAMAGE = 100;
    private static final float RANGE_MIN_X = 10f;
    private static final float RANGE_MAX_X = 18f;
    private static final float RANGE_MIN_Y = 1f;
    private static final float RANGE_MAX_Y = 6f;
    private static final int HALF_HEALTH_ATTACKS = 5;

    // Private variables
    private static final Logger logger = LoggerFactory.getLogger(PatrickTask.class);
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

    /**
     * What is called when the patrick task is assigned
     */
    @Override
    public void start() {
        super.start();
        patrick = owner.getEntity();
        animation = owner.getEntity().getComponent(AnimationRenderComponent.class); // get animation
        patrick.getComponent(PhysicsMovementComponent.class).setSpeed(PATRICK_SPEED); // set speed
        patrick.getComponent(PhysicsMovementComponent.class).setNormalSpeed(PATRICK_SPEED);

        // give game time to load
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                changeState(PatrickState.APPEAR);
                patrick.getEvents().trigger("patrick_appear_sound");
                patrick.getEvents().trigger("patrick_spawn_sound");
                startFlag = true;
                spawnFlag = true;
            }
        }, 0.1f);
    }

    /**
     * Updates the sequence every frame
     */
    @Override
    public void update() {
        // give game time to load
        if (!startFlag) {
            return;
        } else if (teleportFlag) { // update teleport task while teleporting
            teleportTask.update();
            if (teleportTask.getStatus().equals(Status.FINISHED)) {
                teleportFlag = false;
                changeState(PatrickState.APPEAR);
            } else {
                return;
            }
        }

        // check if patrick is dead
        if (patrick.getComponent(CombatStatsComponent.class).getHealth() <= 0) {
            // play patrick death animation
            Entity deadPatrick = MobBossFactory.patrickDead();
            deadPatrick.setPosition(patrick.getPosition().x, patrick.getPosition().y);
            deadPatrick.setScale(4f, 4f);
            ServiceLocator.getEntityService().register(deadPatrick);
            patrick.getEvents().trigger("patrick_scream_sound");
            patrick.setFlagForDelete(true);
        }

        animate();
        int health = patrick.getComponent(CombatStatsComponent.class).getHealth();

        // detect half health
        if (health <= patrick.getComponent(CombatStatsComponent.class).getMaxHealth() / 2 &&
                !halfHealthFlag) {
            patrick.getEvents().trigger("patrick_scream_sound");
            halfHealth();
            halfHealthFlag = true;
        }

        // handle state switches
        switch (state) {
            case APPEAR -> {
                if (spawnFlag && animation.isFinished()) {
                    meleeAttack();
                    spawnFlag = false;
                } else if (meleeFlag) {
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            patrick.getEvents().trigger("patrick_hit_sound");
                        }
                    }, 1f);
                    changeState(PatrickState.ATTACK);
                    meleeFlag = false;
                } else if (rangeFlag && shotsFired > 2) {
                    rangeFlag = false;
                    meleeAttack();
                    shotsFired = 0; // reset shots fired
                } else if (rangeFlag) {
                    changeState(PatrickState.IDLE);
                }
            }
            case IDLE -> {
                if (animation.isFinished()) {
                    rangeAttack();
                }
            }
            case ATTACK -> {
                if (animation.isFinished()) {
                    meleeTarget.getComponent(CombatStatsComponent.class).hit(ATTACK_DAMAGE);
                    teleport(initialPos);
                    rangeFlag = true;
                }
            }
            case WALK, HURT, DEATH, SPELL -> {}
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

    /**
     * @return priority of this task
     */
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

    /**
     * Teleports patrick to the position given
     * @param pos position for patrick to teleport to
     */
    private void teleport(Vector2 pos) {
        teleportTask = new PatrickTeleportTask(patrick, pos);
        teleportTask.create(owner);
        teleportTask.start();
        teleportFlag = true;
    }

    /**
     * Patrick teleports to the closest human entity and attacks it
     */
    private void meleeAttack() {
        initialPos = patrick.getPosition();
        meleeTarget = ServiceLocator.getEntityService().getClosestEntityOfLayer(
                patrick, PhysicsLayer.HUMANS);
        // check if melee target exists
        if (meleeTarget == null) {
            return;
        }
        teleport(meleeTarget.getPosition());
        meleeFlag = true;
    }

    /**
     * spawns a random effect projectile and increments the shots fired counter
     * @param destination destination for projectile to travel to
     */
    private void spawnRandProjectile(Vector2 destination, boolean aoe) {
        // spawn random projectile
        Entity projectile = ProjectileFactory.createEffectProjectile(PhysicsLayer.HUMANS,
                destination, new Vector2(2, 2),
                getEffect(), aoe);
        projectile.setPosition(patrick.getPosition().x, patrick.getPosition().y);
        projectile.setScale(-1f, 1f);
        ServiceLocator.getEntityService().register(projectile);
    }

    /**
     * teleports to a random location within given range
     */
    private void randomTeleport() {
        // teleport to random position
        float randomX = MathUtils.random(RANGE_MIN_X, RANGE_MAX_X);
        float randomY = MathUtils.random(RANGE_MIN_Y, RANGE_MAX_Y);
        teleport(new Vector2(randomX, randomY));
    }

    /**
     * performs a random teleport and a range attack
     */
    private void rangeAttack() {
        randomTeleport();
        spawnRandProjectile(new Vector2(0f, patrick.getPosition().y), false);
        shotsFired++;
    }

    /**
     * when patrick is at half health, he spawns a bunch of random aoe effect projectiles
     */
    private void halfHealth() {
        float startAngle = (float) Math.toRadians(135);
        float endAngle = (float) Math.toRadians(225);
        float angleIncrement = (endAngle - startAngle) / (HALF_HEALTH_ATTACKS - 1);

        for (int i = 0; i < HALF_HEALTH_ATTACKS; i++) {

            // calculate unit vectors for projectiles
            float currentAngle = startAngle + i * angleIncrement;
            float x = MathUtils.cos(currentAngle) * 20;
            float y = MathUtils.sin(currentAngle) * 20;
            Vector2 destination = new Vector2(x, y);
            spawnRandProjectile(destination, true);
        }
        if (shotsFired == HALF_HEALTH_ATTACKS) {
            meleeFlag = true;
        }
    }
}
