package com.csse3200.game.components.popupmenu;

import com.csse3200.game.input.InputComponent;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class PopupMenuInputComponent extends InputComponent{
    /**
     * !!!
     * NOTE: THIS CLASS IS OBSOLETE
     * !!!
     */

    public PopupMenuInputComponent() {super(1);}
    // Note: will need to change constructor's priority when merging with other
    // branches that add other input components.
    /**
     * Triggers "popupEvent" when the mouse is clicked.
     *
     * @return whether the input was processed
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        entity.getEvents().trigger("popupEvent");
        return true;
    }

    /**
     * Triggers "popupEvent" when the mouse is clicked.
     *
     * @return whether the input was processed
     */
    @Override
    public boolean touchDown(float screenX, float screenY, int pointer, int button) {
        entity.getEvents().trigger("popupEvent");
        return true;
    }
}
