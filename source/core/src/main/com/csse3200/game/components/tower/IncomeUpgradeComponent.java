package com.csse3200.game.components.tower;

import com.csse3200.game.components.Component;

public class IncomeUpgradeComponent extends Component {
    private float incomeRate;

    public IncomeUpgradeComponent(float incomeRate) {
        setIncomeRate(incomeRate);
    }

    public void setIncomeRate(float incomeRate) {
        this.incomeRate = incomeRate;
    }

    public float getIncomeRate() {
        return incomeRate;
    }
}
