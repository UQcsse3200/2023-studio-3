package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.ProjectileEffects;
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

    public MobEffectComponent() {
        gameTime = ServiceLocator.getTimeSource();
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

        // apply effects

    }
    public void applyEffect(ProjectileEffects effect) {
        switch (effect) {
            case BURN -> burnTime = gameTime.getTime() + EFFECT_DURATION;
            case SLOW -> slowTime = gameTime.getTime() + EFFECT_DURATION;
            case STUN -> stunTime = gameTime.getTime() + EFFECT_DURATION;
        }
    }
}
