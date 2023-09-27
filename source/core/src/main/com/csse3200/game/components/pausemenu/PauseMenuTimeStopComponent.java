package com.csse3200.game.components.pausemenu;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.services.ServiceLocator;

/**
 * Handles the pausing/resuming of time when the pause menu is brought up/put away.
 */
public class PauseMenuTimeStopComponent extends Component {

    private Array<Entity> freezeList;
    private Entity waveSpawner;

    /**
     * Initialises the component.
     * @param waveSpawner The entity that controls the wave spawning timer.
     */
    public PauseMenuTimeStopComponent(Entity waveSpawner) {
        this.waveSpawner = waveSpawner;
    }

    /**
     * Handles the pausing of the game entities when the pause menu is made.
     */
    @Override
    public void create() {
        waveSpawner.getEvents().trigger("toggleWaveTimer");
        freezeList = ServiceLocator.getEntityService().getEntities();
        for (Entity pauseTarget : freezeList) {
            if (pauseTarget.getId() != getEntity().getId()) {
                // ZA WARUDO! TOKI WO TOMARE!
                pauseTarget.setEnabled(false);
            }
        }
    }

    /**
     * Handles the un-pausing of the game entities when the pause menu is closed.
     */
    @Override
    public void dispose() {
        waveSpawner.getEvents().trigger("toggleWaveTimer");
        for (Entity pauseTarget : freezeList) {
                pauseTarget.setEnabled(true);
        }
    }
}
