package com.csse3200.game.input;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.TowerFactory;
import com.csse3200.game.screens.TowerType;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Input component for handling in-game tower building. Based on Team 5 implementation of
 * DropInputComponent
 */
public class BuildInputComponent extends InputComponent {
    private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
    private final EntityService entityService;
    private final Camera camera;
    private final String[] sounds = {
            "sounds/economy/buildSound.ogg",
            "sounds/ui/Switch/NA_SFUI_Vol1_switch_01.ogg"
    };
    private Sound buildSound;
    private Sound errorSound;

    /**
     * Constructor for the BuildInputComponent
     * @param camera the camera to be used, this is the camera that the game is rendered with
     */
    public BuildInputComponent(Camera camera) {
        this.entityService = ServiceLocator.getEntityService();
        this.camera = camera;
        loadSounds();
    }

    /**
     * Getter for the camera
     * @return the camera
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * When the mouse is clicked, this method is called.
     * Checks that the mouse is clicked on an empty tile
     * Instantiates a new Tower entity at the clicked location if valid, and decrements the
     * cost of the build from the currency pool
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button the button
     * @return true if successful, false otherwise
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        Vector3 worldCoordinates = new Vector3((float)  screenX , (float) screenY, 0);
        getCamera().unproject(worldCoordinates); // translate from screen to world coordinates
        Vector2 cursorPosition = new Vector2(worldCoordinates.x, worldCoordinates.y);

        // determine if the tile is unoccupied
        boolean tileOccupied = entityService.entitiesInTile((int)cursorPosition.x, (int)cursorPosition.y);
        logger.debug("Tile is occupied: " + tileOccupied );

        // check that no entities are occupying the tile
        if (!tileOccupied) {
            buildTower((int)cursorPosition.x, (int)cursorPosition.y);
            logger.debug("spawning a tower at {}, {}", cursorPosition.x, cursorPosition.y);
            return true;
        }
        return false;
    }

    /**
     * Instantiates and spawns the selected tower at the given x y coordinates on the tile map. Assumes that the given
     * x and y coordinate is valid and that the TowerType exists in the CurrencyService.
     *
     * @param x x-coordinate int value
     * @param y y-coordinate int value
     */
    public void buildTower(int x, int y) {
        // fetch the currently set TowerType in the currency service, and its associated build cost.
        TowerType tower = ServiceLocator.getCurrencyService().getTower();
        if (tower != null) {
            // fetch the price of the selected tower and attempt to instantiate
            int cost = Integer.parseInt(ServiceLocator.getCurrencyService().getTower().getPrice());

            if (cost <= ServiceLocator.getCurrencyService().getScrap().getAmount()) {
                Entity newTower = switch (tower) {
                    case WEAPON -> TowerFactory.createWeaponTower();
                    case INCOME -> TowerFactory.createIncomeTower();
                    case TNT    -> TowerFactory.createTNTTower();
                    case DROID  -> TowerFactory.createDroidTower();
                    case WALL   -> TowerFactory.createWallTower();
                    case FIRE   -> TowerFactory.createFireTower();
                    case STUN   -> TowerFactory.createStunTower();
                    case PIERCE -> TowerFactory.createPierceTower();
                    case FIREWORK -> TowerFactory.createFireworksTower();
                    case RICOCHET -> TowerFactory.createRicochetTower();
                };
                // build the selected tower
                newTower.setPosition(x, y);
                ServiceLocator.getEntityService().register(newTower);
                // Decrement currency and show a popup that reflects the cost of the build
                ServiceLocator.getCurrencyService().getScrap().modify(-cost);
                ServiceLocator.getCurrencyService().getDisplay().updateScrapsStats();
                ServiceLocator.getCurrencyService().getDisplay().currencyPopUp(x, y, -cost, 10);

                long soundId = buildSound.play();
                buildSound.setVolume(soundId, 0.4f);
            } else {
                // play a sound to indicate an invalid action
                long soundId = errorSound.play();
                errorSound.setVolume(soundId, 0.5f);
            }
        }
    }

    /**
     * Load the sound assets related to in-game tower building activity
     */
    private void loadSounds() {
        ServiceLocator.getResourceService().loadSounds(sounds);
        ServiceLocator.getResourceService().loadAll();
        buildSound = ServiceLocator.getResourceService().getAsset("sounds/economy/buildSound.ogg", Sound.class);
        errorSound = ServiceLocator.getResourceService().getAsset("sounds/ui/Switch/NA_SFUI_Vol1_switch_01.ogg", Sound.class);
    }
}
