package com.csse3200.game.components.tasks.waves;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class LevelWaves extends Entity {
    List<WaveClass> waves = new ArrayList<>();
    private float spawnDelay;
    private GameTime gameTime;
    private long startTime;
    private Random rand = new Random();
    private int currentRandom = 0;
    private int previousRandom = 0;
    private int mobIndex;
    private int waveIndex;
    private int numWaves;

    /**
     * Constructor for the LevelWaves class
     * @param spawnDelay time to elapse between each wave
     */
    public LevelWaves(int spawnDelay) {
        this.spawnDelay = spawnDelay;
        this.gameTime = ServiceLocator.getTimeSource();
        this.startTime = this.gameTime.getTime();
        this.mobIndex = 0;
        this.waveIndex = 0;
        this.numWaves = 0;
    }

    /**
     * Add a wave to the level and increment the total number of waves
     * @param wave to be added
     */
    public void addWave(WaveClass wave) {
        this.numWaves++;
        this.waves.add(wave);
    }

    /**
     * Retrieve a wave at an index in the list
     * @param index wave number to be retireved
     * @return instance of a wave class
     */
    public WaveClass getWave(int index) {
        return this.waves.get(index);
    }

    /**
     * Spawn the wave and all the associated mobs for that wave
     */
    public void spawnWave() {
        if (gameTime.getTime() >= startTime + spawnDelay * 1000) {
            do {
                currentRandom = rand.nextInt(1, 7);
            } while (currentRandom == previousRandom);
            GridPoint2 randomPos = new GridPoint2(19, currentRandom);
            this.getEvents().trigger("spawnWave", waves.get(waveIndex)
                    .getMobs().get(mobIndex), randomPos);
            startTime = gameTime.getTime();
            mobIndex++;
            previousRandom = currentRandom;
        } else if (mobIndex == waves.get(waveIndex).getSize()) {
            this.getEvents().trigger("waveFinishedSpawning");
            mobIndex = 0;
        }
    }

    /**
     * Set the wave index
     * @param index
     */
    public void setWaveIndex(int index) {
        this.waveIndex = index;
    }

    /**
     * Get the total number of waves in this level
     * @return number of waves in the level
     */
    public int getNumWaves() {
        return this.numWaves;
    }
}