package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 @author antz
 @version 1.0.0
 November 2022
 See https://github.com/antzGames/libGDX-cameraShake for more information.

 Loosely based on 'Mastering LibGDX Game Development' - Chapter 9 - Camera Shake
 Book: https://www.amazon.com/Mastering-LibGDX-Game-Development-Patrick/dp/1785289365

 */

public class CameraShaker {
    private Camera camera;
    private boolean isShaking = false;
    private float origShakeRadius;
    private float minimumShakeRadius;
    private float radiusFallOffFactor;
    private float shakeRadius;
    private float randomAngle;
    private float timer;
    private Vector3 offset;
    private Vector3 currentPosition;
    private Vector3 origPosition;

    /**
     * Constructor
     *
     * @param camera                supports any camera implementation
     * @param shakeRadius           original was set to 30.0f, must be greater than 0
     * @param minimumShakeRadius    original was set to 2.0f, must be greater than 0 and less than shakeRadius
     * @param radiusFallOffFactor   original was set to 0.9f, must be greater than 0 and less than 1
     */
    public CameraShaker(Camera camera, float shakeRadius, float minimumShakeRadius, float radiusFallOffFactor){
        checkParameters(shakeRadius, minimumShakeRadius, radiusFallOffFactor);
        this.camera = camera;
        this.offset = new Vector3();
        this.currentPosition = new Vector3();
        this.origPosition = camera.position.cpy();
        reset();
    }

    /**
     * Constructor - simple version
     *
     * Use this constructor to create a camera shaker with default values
     *
     * shakeRadius = 0.05f;			// must be positive
     * minimumShakeRadius = 0.001f;		// must be positive and less than shakeRadius, aim for 5-15% of shake radius
     * radiusFallOffFactor = 0.8f;	// must be greater than 0 and less than 1
     *
     * @param camera supports any camera implementation
     */
    public CameraShaker(Camera camera){
        shakeRadius = 0.05f;
        minimumShakeRadius = 0.001f;
        radiusFallOffFactor = 0.8f;
        checkParameters(shakeRadius, minimumShakeRadius, radiusFallOffFactor);
        this.camera = camera;
        this.offset = new Vector3();
        this.currentPosition = new Vector3();
        this.origPosition = camera.position.cpy();
        reset();
    }

    /**
     * Call this after a player collision/impact/explosion to start the camera shaking.
     */
    public void startShaking(){
        reset();
        isShaking = true;
    }

    /**
     * Always call this in your game's main update/render method.
     *
     * Make sure batch.setProjectionMatrix(camera.combined) is set prior to call.
     */
    public void update(float delta){
        if (!isCameraShaking()) return;

        // only update camera shake 60 times a second max
        timer += delta;
        if (timer >= 1f/60f) {
            computeCameraOffset();
            computeCurrentPosition();
            diminishShake();
            camera.position.set(currentPosition);
            camera.update();
            timer = 0;
        }
    }

    /**
     * Called by diminishShake() when minimum shake radius reached.
     *
     * But you can also stop a camera shake by calling this method if needed.
     */
    public void reset(){
        shakeRadius = origShakeRadius;
        isShaking = false;
        seedRandomAngle();
        currentPosition = origPosition.cpy();
        timer = 0;

    }

    /**
     *  This allows to reconfigure parameters. Check if not shaking before calling, or else current shake will end.
     *
     * @param shakeRadius           original was set to 30.0f, must be greater than 0
     * @param minimumShakeRadius    original was set to 2.0f, must be greater than 0 and less than shakeRadius
     * @param radiusFallOffFactor   original was set to 0.9f, must be greater than 0 and less than 1
     */
    public void resetAndReconfigure(float shakeRadius, float minimumShakeRadius, float radiusFallOffFactor){
        checkParameters(shakeRadius, minimumShakeRadius, radiusFallOffFactor);
        isShaking = false;
        seedRandomAngle();
        currentPosition = origPosition.cpy();
        timer = 0;
    }

    /**
     * You can check if camera is currently shaking.
     *
     * @return is the camera currently shaking.
     */
    public boolean isCameraShaking(){
        return isShaking;
    }

    /**
     Private methods below
     */

    /**
     * Seeds a random angle between 1 and 360 degrees.
     */
    private void seedRandomAngle(){
        randomAngle = MathUtils.random(1, 360);
    }

    /**
     * Computes the camera offset based on the current shake radius and random angle.
     */
    private void computeCameraOffset(){
        float sine = MathUtils.sinDeg(randomAngle);
        float cosine = MathUtils.cosDeg(randomAngle);
        offset.x = cosine * shakeRadius;
        offset.y = sine * shakeRadius;
    }

    /**
     * Computes the current camera position based on the original position and the offset.
     */
    private void computeCurrentPosition() {
        currentPosition.x = origPosition.x + offset.x;
        currentPosition.y = origPosition.y + offset.y;
    }

    /**
     * Reduces the shake effect over time based on the fall off factor.
     */
    private void diminishShake(){
        if(shakeRadius < minimumShakeRadius){
            reset();
            return;
        }
        isShaking = true;
        shakeRadius *= radiusFallOffFactor;
        randomAngle = MathUtils.random(1, 360);
    }

    /**
     * Validates and adjusts the provided parameters for the shake effect.
     *
     * @param shakeRadius         The intended maximum shake radius.
     * @param minimumShakeRadius  The intended minimum shake radius.
     * @param radiusFallOffFactor The intended fall off factor for shake radius.
     */
    private void checkParameters(float shakeRadius, float minimumShakeRadius, float radiusFallOffFactor) {
        // validation checks on parameters
        if (radiusFallOffFactor >= 1f) radiusFallOffFactor = 0.9f;
        if (radiusFallOffFactor <= 0) radiusFallOffFactor = 0.9f;
        if (shakeRadius <= 0) shakeRadius = 30f;
        if (minimumShakeRadius < 0) minimumShakeRadius = 0;
        if (minimumShakeRadius >= shakeRadius)
            minimumShakeRadius = 0.15f * shakeRadius;

        this.shakeRadius = shakeRadius;
        this.origShakeRadius = shakeRadius;
        this.minimumShakeRadius = minimumShakeRadius;
        this.radiusFallOffFactor = radiusFallOffFactor;
    }

    /**
     * Retrieves the current shake radius.
     *
     * @return The current shake radius.
     */
    public float getShakeRadius() {
        return shakeRadius;
    }

    /**
     * Retrieves the minimum shake radius.
     *
     * @return The minimum shake radius.
     */
    public float getMinimumRadius() {
        return minimumShakeRadius;
    }

    /**
     * Retrieves the fall off factor for the shake radius.
     *
     * @return The radius fall off factor.
     */
    public float getFallOffFactor() {
        return radiusFallOffFactor;
    }

    /**
     * Retrieves the original position of the camera before the shake effect began.
     *
     * @return The original position as a {@link Vector3} object.
     */
    public Vector3 getOrigPosition() {
        return origPosition;
    }

}