package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.OrthographicCamera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class CameraShakerTest {

    private CameraShaker cameraShaker;
    private OrthographicCamera camera;

    @BeforeEach
    public void setUp() {
        camera = new OrthographicCamera();
        cameraShaker = new CameraShaker(camera);
    }

    @Test
    public void testStartShaking() {
        assertFalse(cameraShaker.isCameraShaking());
        cameraShaker.startShaking();
        assertTrue(cameraShaker.isCameraShaking());
    }

    @Test
    public void testReset() {
        cameraShaker.startShaking();
        cameraShaker.reset();
        assertFalse(cameraShaker.isCameraShaking());
        assertEquals(cameraShaker.getOrigPosition(), camera.position);
    }

    @Test
    public void testDefaultConstructorValues() {
        assertEquals(0.05f, cameraShaker.getShakeRadius(), 0.001f);
        assertEquals(0.001f, cameraShaker.getMinimumRadius(), 0.001f);
        assertEquals(0.8f, cameraShaker.getFallOffFactor(), 0.001f);
    }

}
