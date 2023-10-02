package com.csse3200.game.screens;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.settingsmenu.SettingsMenuDisplay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

/**
 * Basic test class for SettingsMenuDisplay.
 * Ensures that the class can be instantiated and its methods can be called without errors.
 * Note: More comprehensive tests would involve verifying the behavior of the component.
 */
public class SettingsMenuDisplayTest {

    /**
     * Instance of the SettingsMenuDisplay to be tested.
     */
    private SettingsMenuDisplay settingsMenuDisplay;

    /**
     * Set up necessary objects and mocks before each test.
     * Mocked instance of the GdxGame for testing.
     */
    @BeforeEach
    public void setUp() {
        GdxGame mockGame = mock(GdxGame.class);
        settingsMenuDisplay = new SettingsMenuDisplay(mockGame);
    }

    /**
     * Test to ensure the create method does not throw errors.
     */
    @Test
    public void testCreate() {
        settingsMenuDisplay.create();
    }

    /**
     * Test to ensure the update method does not throw errors.
     */
    @Test
    public void testUpdate() {
        settingsMenuDisplay.update();
    }

    /**
     * Test to ensure the dispose method does not throw errors.
     */
    @Test
    public void testDispose() {
        settingsMenuDisplay.dispose();
    }
}