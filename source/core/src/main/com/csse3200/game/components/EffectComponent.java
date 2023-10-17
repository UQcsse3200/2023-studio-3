package com.csse3200.game.components;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.tower.TowerUpgraderComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

public class EffectComponent extends Component {
    private static final long EFFECT_DURATION = 5000;
    private final GameTime gameTime;
    // effect flags
    private boolean burnFlag;
    private boolean slowFlag;
    private boolean isSlowed;
    private boolean stunFlag;
    private boolean isStunned;
    private boolean mob;
    private Entity host;
    private Entity target;
    private long lastTimeBurned;
    private long burnTime;
    private long slowTime;
    private long stunTime;
    private Vector2 defaultTargetSpeed;
    private PhysicsMovementComponent targetPhysics;
    private static final Vector2 STUN_SPEED = new Vector2(0f,0f);
    private static final long BURN_TICK = 1000;

    public EffectComponent(boolean mob) {
        this.mob = mob;
        this.gameTime = ServiceLocator.getTimeSource();
    }

    public void start() {
        burnTime = 0;
        slowTime = 0;
        stunTime = 0;
    }

    @Override
    public void update() {
        // update effect flags
        burnFlag = burnTime > gameTime.getTime();
        slowFlag = slowTime > gameTime.getTime();
        stunFlag = stunTime > gameTime.getTime();

        // apply burn effect
        if (burnFlag && gameTime.getTime() > lastTimeBurned + BURN_TICK) {
            burnEffect();
        }

        // apply slow effect
        if (mob) {
            if (slowFlag) {
                isSlowed = true;
                Vector2 half_speed = new Vector2(defaultTargetSpeed.x / 2, defaultTargetSpeed.y / 2);
                targetPhysics.setSpeed(half_speed);
            } else if (isSlowed) {
                isSlowed = false;
                targetPhysics.setSpeed(defaultTargetSpeed);
            }
        } else {
            if (slowFlag && !isSlowed) {
                isSlowed = true;
                target.getEvents().trigger("upgradeTower",
                        TowerUpgraderComponent.UPGRADE.FIRERATE, 2);
            } else if (!slowFlag && isSlowed) {
                isSlowed = false;
                target.getEvents().trigger("upgradeTower",
                        TowerUpgraderComponent.UPGRADE.FIRERATE, 5);
            }
        }

        // apply stun effect
        if (mob) {
            if (stunFlag) {
                if (defaultTargetSpeed == null) {
                    return;
                }
                targetPhysics.setSpeed(STUN_SPEED);
            } else if (isStunned) {
                isStunned = false;
                targetPhysics.setSpeed(defaultTargetSpeed);
            }
        } else {
            if (stunFlag && !isStunned) {
                stunEffect(true);
            } else if (!stunFlag && isStunned) {
                stunEffect(false);
            }
        }
    }
    public void applyEffect(ProjectileEffects effect, Entity host, Entity target) {
        this.host = host;
        this.target = target;

        targetPhysics = entity.getComponent(PhysicsMovementComponent.class);
        if (targetPhysics == null) {
            return;
        }
        defaultTargetSpeed = targetPhysics.getNormalSpeed();
        if (defaultTargetSpeed == null) {
            defaultTargetSpeed = new Vector2(1f,1f);
        }
        switch (effect) {
            case BURN -> {
                burnFlag = true;
                burnTime = gameTime.getTime() + EFFECT_DURATION;
                lastTimeBurned = gameTime.getTime();
            }
            case SLOW -> {
                slowFlag = true;
                slowTime = gameTime.getTime() + EFFECT_DURATION;
            }
            case STUN -> {
                stunFlag = true;
                stunTime = gameTime.getTime() + EFFECT_DURATION;
            }
            case FIREBALL -> {}
        }
    }

    private void burnEffect() {
        CombatStatsComponent hostCombat = this.host.getComponent(CombatStatsComponent.class);
        CombatStatsComponent targetCombat = this.target.getComponent(CombatStatsComponent.class);
        targetCombat.hit(hostCombat);
        lastTimeBurned = gameTime.getTime();
    }

    private void changeSpeed(Vector2 speed) {
        PhysicsMovementComponent targetPhysics = target.getComponent(
                PhysicsMovementComponent.class);
        if (targetPhysics == null) {
            return;
        }
        // Set mob speed
        targetPhysics.setSpeed(speed);
    }

    private void stunEffect(boolean stunned) {
        isStunned = true;
        AITaskComponent targetAI = target.getComponent(AITaskComponent.class);
        if (targetAI == null) {
            return;
        }
        if (stunned) {
            targetAI.disposeAll();
        } else {
            targetAI.restore();
        }
    }
}