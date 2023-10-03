package com.csse3200.game.components.tower;

import com.csse3200.game.components.Component;

public class UpgradableStatsComponent extends Component {
    private float attackRate;

    public UpgradableStatsComponent(float attackRate) {
        setAttackRate(attackRate);
    }

    public void setAttackRate(float attackRate) {
        this.attackRate = attackRate;
    }

    public float getAttackRate() {
        return attackRate;
    }
}
