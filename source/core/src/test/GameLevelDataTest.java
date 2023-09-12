import com.csse3200.game.screens.GameLevelData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameLevelDataTest {

    @Test
    void shouldInitializeSelectedLevelToMinusOne() {
        assertEquals(-1, GameLevelData.getSelectedLevel());
    }

    @Test
    void shouldSetSelectedLevel() {
        GameLevelData.setSelectedLevel(2);
        assertEquals(2, GameLevelData.getSelectedLevel());
    }

    @Test
    void shouldSetSelectedLevelToZero() {
        GameLevelData.setSelectedLevel(0);
        assertEquals(0, GameLevelData.getSelectedLevel());
    }

    @Test
    void shouldSetSelectedLevelToNegativeValue() {
        GameLevelData.setSelectedLevel(-5);
        assertEquals(-5, GameLevelData.getSelectedLevel());
    }
}
