package com.csse3200.game.components;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CostComponentTest {

    private CostComponent costComponent;

    @BeforeEach
    public void setUp() {
        costComponent = new CostComponent(0); // initializing with default value
    }

    @Test
    public void testSetAndGetPositiveCost() {
        costComponent.setCost(100);
        assertEquals(100, costComponent.getCost(), "The cost should be set to 100");
    }

    @Test
    public void testSetAndGetZeroCost() {
        costComponent.setCost(0);
        assertEquals(0, costComponent.getCost(), "The cost should be set to 0");
    }

    @Test
    public void testNegativeCostIsNormalizedToZero() {
        costComponent.setCost(-100);
        assertEquals(0, costComponent.getCost(), "The cost should be normalized to 0 if set as negative");
    }

    @Test
    public void testCostComponentConstructorWithNegativeValue() {
        CostComponent tempComponent = new CostComponent(50);
        tempComponent.setCost(tempComponent.getCost() - 60);
        assertEquals(0, tempComponent.getCost(), "The cost should be 0");
    }

    @Test
    public void testCostComponentConstructorWithPositiveValue() {
        CostComponent tempComponent = new CostComponent(-50);
        tempComponent.setCost(tempComponent.getCost() + 60);
        assertEquals(60, tempComponent.getCost(), "The cost should be 60");
    }
}
