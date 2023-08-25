package com.csse3200.game.components.popupmenu;

import com.badlogic.gdx.InputProcessor;
import com.csse3200.game.input.InputComponent;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class PopupMenuInputComponent extends InputComponent{
    /** TO DO:
     * This component's end goal is to send a deactivation trigger when the
     * user clicks on anything  other than the menu entity, and reactivate it
     * if the user clicks on a tower, with the new tower's stats as per its
     * config file.
     * Current implementation step:  trigger a generic event whenever
     * the mouse is clicked, with no checks for the entity clicked on.
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
        System.out.println(9);
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
        System.out.println(9);
        entity.getEvents().trigger("popupEvent");
        return true;
    }
}
