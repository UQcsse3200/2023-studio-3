/**
 * Represents a pair of animation and texture for rendering in a game screen.
 * Provides easy access to the animation and its associated texture.
 *
 * @param <TextureRegion> The type of texture region used in the animation.
 */
package com.csse3200.game.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationTexturePair {
    public final Animation<TextureRegion> animation;
    public final Texture texture;

    /**
     * Creates an {@code AnimationTexturePair} with the specified animation and texture.
     *
     * @param animation The animation to be associated with this pair.
     * @param texture   The texture to be associated with this pair.
     */
    public AnimationTexturePair(Animation<TextureRegion> animation, Texture texture) {
        this.animation = animation;
        this.texture = texture;
    }

    /**
     * Gets the animation associated with this pair.
     *
     * @return The animation.
     */
    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    /**
     * Gets the texture associated with this pair.
     *
     * @return The texture.
     */
    public Texture getTexture() {
        return texture;
    }
}
