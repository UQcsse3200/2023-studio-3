package com.csse3200.game.components.npc;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.ProjectileEffects;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

public class MobEffectComponent extends Component {
    private static final long EFFECT_DURATION = 5000;
    private GameTime gameTime;
    // effect flags
    private boolean burnFlag;
    private boolean slowFlag;
    private boolean stunFlag;
    private long burnTime;
    private long slowTime;
    private long stunTime;
    private static long BURN_TICK = 1000;

    public MobEffectComponent() {
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
        if (burnTime > gameTime.getTime()) {
            burnFlag = true;
        } else {
            burnFlag = false;
        }
        if (slowTime > gameTime.getTime()) {
            slowFlag = true;
        } else {
            slowFlag = false;
        }
        if (stunTime > gameTime.getTime()) {
            stunFlag = true;
        } else {
            stunFlag = false;
        }
    }
    public void applyEffect(ProjectileEffects effect, Entity host, Entity target) {
        switch (effect) {
            case BURN -> {
                burnTime = gameTime.getTime() + EFFECT_DURATION;
                burnEffect(host, target);
            }
            case SLOW -> {
                slowTime = gameTime.getTime() + EFFECT_DURATION;
                slowEffect(host, target);
            }
            case STUN -> {
                stunTime = gameTime.getTime() + EFFECT_DURATION;
                stunEffect(host, target);
            }
        }
    }

    private void burnEffect(Entity host, Entity target) {
        long lastTimeHit = gameTime.getTime();
        while(burnFlag) {
            CombatStatsComponent hostCombat = host.getComponent(CombatStatsComponent.class);
            CombatStatsComponent targetCombat = target.getComponent(CombatStatsComponent.class);
            if (gameTime.getTime() > lastTimeHit + BURN_TICK) {
                lastTimeHit = gameTime.getTime();
                targetCombat.hit(hostCombat);
            }
        }
    }

    private void slowEffect(Entity host, Entity target) {

    }

    private void stunEffect(Entity host, Entity target) {

    }
}
