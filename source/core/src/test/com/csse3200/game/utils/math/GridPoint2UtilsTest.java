package com.csse3200.game.utils.math;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GridPoint2UtilsTest {

    /**
     * testZero() ensures that GridPoint2Utils.ZERO constant is (0, 0).
     * GridPoint2Utils.ZERO is a constant representing the point (0, 0).
     */
    @Test
    public void testZero() {
        GridPoint2 zero = GridPoint2Utils.ZERO;
        assertEquals(0, zero.x);
        assertEquals(0, zero.y);
    }

    /**
     * testInstantiation() checks if private constructor of GridPoint2Utils throws an exception
     * while creating an instance.
     */
    @Test
    public void testInstantiation() {
        assertThrows(IllegalStateException.class, () -> {
            GridPoint2Utils.createInstance();
        });
    }
}