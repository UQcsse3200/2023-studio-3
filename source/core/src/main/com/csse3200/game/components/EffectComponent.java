package com.csse3200.game.components;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.ProjectileEffects;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

public class EffectComponent extends Component {
    private static final long EFFECT_DURATION = 5000;
    private GameTime gameTime;
    // effect flags
    private boolean burnFlag;
    private boolean slowFlag;
    private boolean stunFlag;
    private Entity host;
    private Entity target;
    private long lastTimeBurned;
    private long burnTime;
    private long slowTime;
    private long stunTime;
    private static long BURN_TICK = 1000;

    public EffectComponent() {
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

        // apply effects
        if (burnFlag && gameTime.getTime() > lastTimeBurned + BURN_TICK) {
            burnEffect();
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
                slowEffect(host, target);
            }
            case STUN -> {
                stunFlag = true;
                stunTime = gameTime.getTime() + EFFECT_DURATION;
                stunEffect(host, target);
            }
        }
    }

    private void burnEffect() {
        CombatStatsComponent hostCombat = this.host.getComponent(CombatStatsComponent.class);
        CombatStatsComponent targetCombat = this.target.getComponent(CombatStatsComponent.class);
        targetCombat.hit(hostCombat);
        lastTimeBurned = gameTime.getTime();
    }

    private void slowEffect(Entity host, Entity target) {

    }

    private void stunEffect(Entity host, Entity target) {

    }
}
