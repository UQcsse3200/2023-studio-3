package com.csse3200.game.screens;

import com.csse3200.game.GdxGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

/**
 * Basic test class for MainMenuScreen.
 * Ensures that the class can be instantiated and its methods can be called without errors.
 */
public class MainMenuScreenTest {

    /**
     * Instance of the MainMenuScreen to be tested.
     */
    private MainMenuScreen mainMenuScreen;

    /**
     * Set up necessary objects and mocks before each test.
     * Mocked instance of the GdxGame for testing.
     */
    @BeforeEach
    public void setUp() {
        GdxGame mockGame = mock(GdxGame.class);
        mainMenuScreen = new MainMenuScreen(mockGame);
    }

    /**
     * Test to ensure the render method does not throw errors.
     */
    @Test
    public void testRender() {
        mainMenuScreen.render(0.1f);  // Using a sample delta time
    }

    /**
     * Test to ensure the resize method does not throw errors.
     */
    @Test
    public void testResize() {
        mainMenuScreen.resize(800, 600);  // Using sample width and height
    }

    /**
     * Test to ensure the pause and resume methods do not throw errors.
     */
    @Test
    public void testPauseAndResume() {
        mainMenuScreen.pause();
        mainMenuScreen.resume();
    }

    /**
     * Test to ensure the dispose method does not throw errors.
     */
    @Test
    public void testDispose() {
        mainMenuScreen.dispose();
    }
}