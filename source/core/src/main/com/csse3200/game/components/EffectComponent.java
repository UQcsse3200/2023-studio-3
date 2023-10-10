package com.csse3200.game.components;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.ProjectileEffects;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.components.tower.TowerUpgraderComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
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
    private Vector2 initialSpeed;
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
        if (slowFlag && !isSlowed) {
            slowEffect(2);
        } else if (!slowFlag && isSlowed) {
            isSlowed = false;
            slowEffect(5);
        }

        // apply stun effect
        if (mob) {
            if (stunFlag) {
                if (initialSpeed == null) {
                    return;
                }
                target.getComponent(PhysicsMovementComponent.class).setSpeed(STUN_SPEED);
            } else {
                if (target == null) {
                    return;
                }
                target.getComponent(PhysicsMovementComponent.class).setSpeed(initialSpeed);
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
                initialSpeed = entity.getComponent(PhysicsMovementComponent.class).getSpeed();
            }
        }
    }

    private void burnEffect() {
        CombatStatsComponent hostCombat = this.host.getComponent(CombatStatsComponent.class);
        CombatStatsComponent targetCombat = this.target.getComponent(CombatStatsComponent.class);
        targetCombat.hit(hostCombat);
        lastTimeBurned = gameTime.getTime();
    }

    private void slowEffect(int amount) {
        isSlowed = true;
        if (PhysicsLayer.contains(PhysicsLayer.HUMANS,
                target.getComponent(HitboxComponent.class).getLayer())) {
            // if slowing human
            target.getEvents().trigger("upgradeTower",
                    TowerUpgraderComponent.UPGRADE.FIRERATE, amount);
        } else if (PhysicsLayer.contains(PhysicsLayer.NPC,
                target.getComponent(HitboxComponent.class).getLayer())) {
            // if slowing npc
            PhysicsMovementComponent targetPhysics = target.getComponent(
                    PhysicsMovementComponent.class);
            if (targetPhysics == null) {
                return;
            }

            // Halve the mob speed
            targetPhysics.setSpeed(new Vector2(targetPhysics.getSpeed().x/2,
                    targetPhysics.getSpeed().y/2));
        }
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
