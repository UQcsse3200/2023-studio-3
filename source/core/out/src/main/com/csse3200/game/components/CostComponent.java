package com.csse3200.game.components;

/**
 * Component used to store information related to cost.
 * Any entities that necessitate a cost should register an instance of this class.
 */
public class CostComponent extends Component {
    private int cost;

    public CostComponent(int cost) {
        setCost(cost);

    }

    /**
     * Sets the entity's cost. Cost has a minimum bound of 0.
     * @param cost
     */
    public void setCost(int cost) {
        if(cost >= 0) {
            this.cost = cost;
        } else {
            this.cost = 0;
        }
    }

    /**
     * Returns the entity's cost
     * @return entity's cost
     */
    public int getCost() {
        return this.cost;
    }

}