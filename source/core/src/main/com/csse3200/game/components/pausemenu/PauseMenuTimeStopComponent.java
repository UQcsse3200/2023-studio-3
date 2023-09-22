package com.csse3200.game.components.pausemenu;

import com.csse3200.game.components.Component;
import com.csse3200.game.services.ServiceLocator;

/**
 * Handles the pausing/resuming of time when the pause menu is brought up/put away.
 */
public class PauseMenuTimeStopComponent extends Component {
    public PauseMenuTimeStopComponent() {
    }

    @Override
    public void create() {
        ServiceLocator.getTimeSource().setTimeScale(0f);
    }

    @Override
    public void dispose() {
        ServiceLocator.getTimeSource().setTimeScale(1f);
    }
}
