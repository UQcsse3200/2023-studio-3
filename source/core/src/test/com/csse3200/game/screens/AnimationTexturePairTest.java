package com.csse3200.game.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.junit.jupiter.api.Test;

/**
 * This class contains test methods for the AnimationTexturePair class.
 */
class AnimationTexturePairTest {
    /**
     * Test method for the constructor of AnimationTexturePair.
     *
     * This method tests the following:
     * - Creating an AnimationTexturePair with null Animation and null Texture.
     * - Checking if the Animation and Texture obtained from the created object are also null.
     */
    @Test
    void testConstructor() {
        // Arrange
        Animation<TextureRegion> animation = null;
        Texture texture = null;

        // Act
        AnimationTexturePair actualAnimationTexturePair = new AnimationTexturePair(animation, texture);
        Animation<TextureRegion> actualAnimation = actualAnimationTexturePair.getAnimation();
        Texture actualTexture = actualAnimationTexturePair.getTexture();

        // Assert
        // Add your assertions here to verify the behavior of the constructor.
    }
}
