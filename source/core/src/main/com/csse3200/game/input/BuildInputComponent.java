package com.csse3200.game.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.TowerFactory;
import com.csse3200.game.screens.TowerType;
import com.csse3200.game.services.CurrencyService;
import com.csse3200.game.services.ServiceLocator;


/**
 * Input component for handling in-game tower building. Based on Team 5 implementation of
 * DropInputComponent
 */
public class BuildInputComponent extends InputComponent {
    private final EntityService entityService;
    private final Camera camera;
    private final String[] sounds = {
            "sounds/economy/buildSound.ogg",
            "sounds/ui/switch/switch_01.ogg"
    };
    private Sound buildSound;
    private Sound errorSound;
    private Array<TowerType> towers = new Array<>();
    private boolean multipleTowerBuild = false;

    /**
     * Constructor for the BuildInputComponent
     * @param camera the camera to be used, this is the camera that the game is rendered with
     */
    public BuildInputComponent(Camera camera) {
        this.entityService = ServiceLocator.getEntityService();
        this.camera = camera;
        loadSounds();
        towers.addAll(ServiceLocator.getTowerTypes());

        TowerType[] defaultTowerTypes = {
              TowerType.TNT,
              TowerType.DROID,
              TowerType.INCOME,
              TowerType.WALL,
              TowerType.WEAPON
        };
        Array<TowerType> defaultTowers = new Array<>();
        defaultTowers.addAll(defaultTowerTypes);

        if (towers.isEmpty() || towers.size < 5) {
            for (TowerType tower : defaultTowers) {
                if (towers.size < 5 && !towers.contains(tower, true)) {
                    towers.add(tower);
                }
            }
        }

        ServiceLocator.setTowerTypes(towers);
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

        Vector3 worldCoordinates = new Vector3(screenX , screenY, 0);
        getCamera().unproject(worldCoordinates); // translate from screen to world coordinates
        Vector2 cursorPosition = new Vector2(worldCoordinates.x, worldCoordinates.y);

        // determine if the tile is unoccupied
        boolean tileOccupied = entityService.entitiesInTile((int)cursorPosition.x, (int)cursorPosition.y);

        // check that no entities are occupying the tile
        if (!tileOccupied) {
            return buildTower((int)cursorPosition.x, (int)cursorPosition.y);
        } else {
            return false;
        }
    }

    /**
     * Configures shortcut keys for building towers. Pressing the shortcut key
     * sets the 'tower to build' variable in CurrencyService
     *
     * @return whether the input was processed
     * @see InputProcessor#keyDown(int)
     */
    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.NUM_1 -> {
                ServiceLocator.getCurrencyService().setTowerType(towers.get(0));
                return true;
            }
            case Input.Keys.NUM_2 -> {
                ServiceLocator.getCurrencyService().setTowerType(towers.get(1));
                return true;
            }
            case Input.Keys.NUM_3 -> {
                ServiceLocator.getCurrencyService().setTowerType(towers.get(2));
                return true;
            }
            case Input.Keys.NUM_4 -> {
                ServiceLocator.getCurrencyService().setTowerType(towers.get(3));
                return true;
            }
            case Input.Keys.NUM_5 -> {
                ServiceLocator.getCurrencyService().setTowerType(towers.get(4));
                return true;
            }
            case Input.Keys.CONTROL_LEFT -> {
                // After multiple placement, deselect tower and prevent further builds
                ServiceLocator.getCurrencyService().setTowerType(null);
                multipleTowerBuild = false;
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    /**
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return true if the multipleBuild key is down, otherwise false
     */
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.CONTROL_LEFT) {
            multipleTowerBuild = true;
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
    public boolean buildTower(int x, int y) {
        // fetch the currently set TowerType in the currency service, and its associated build cost.
        CurrencyService currencyService = ServiceLocator.getCurrencyService();
        TowerType tower = currencyService.getTower();
        if (tower != null) {
            // fetch the price of the selected tower and attempt to instantiate
            int cost = Integer.parseInt(currencyService.getTower().getPrice());


            if (canAfford(cost)) {

                createTower(tower, x, y, cost);
                long soundId = buildSound.play();
                buildSound.setVolume(soundId, 0.4f);

                // deselect the tower after building if not in multiple placement mode
                if (!multipleTowerBuild) {
                    ServiceLocator.getCurrencyService().setTowerType(null);
                }
                return true;

            } else {
                // play a sound to indicate an invalid action
                long soundId = errorSound.play();
                errorSound.setVolume(soundId, 1f);
                ServiceLocator.getCurrencyService().getDisplay().scrapBalanceFlash();
            }
        }
        return false;
    }

    /**
     * Creates the new tower instance
     * @param tower the TowerType currently set
     * @param x the x position int the tower will occupy
     * @param y the y position int the tower will occupy
     */
    private void createTower(TowerType tower, int x, int y, int cost) {
        if (tower == null) {
            return;
        }
        Entity newTower = switch (tower) {
            case WEAPON -> TowerFactory.createWeaponTower();
            case INCOME -> TowerFactory.createIncomeTower();
            case TNT -> TowerFactory.createTNTTower();
            case DROID -> TowerFactory.createDroidTower();
            case WALL -> TowerFactory.createWallTower();
            case FIRE -> TowerFactory.createFireTower();
            case STUN -> TowerFactory.createStunTower();
            case PIERCE -> TowerFactory.createPierceTower();
            case FIREWORK -> TowerFactory.createFireworksTower();
            case RICOCHET -> TowerFactory.createRicochetTower();
        };
        // build the selected tower
        newTower.setPosition(x, y);
        entityService.register(newTower);

        // Decrement currency and show a popup that reflects the cost of the build
        ServiceLocator.getCurrencyService().getScrap().modify(-cost);
        ServiceLocator.getCurrencyService().getDisplay().updateScrapsStats();
        ServiceLocator.getCurrencyService().getDisplay().currencyPopUp(x, y, -cost, 10);
    }

    /**
     * Determines if there are sufficient resources for a tower build
     * @param cost integer representing the cost of the intended build
     * @return true if the cost is less than or equal to the current balance, false otherwise
     */
    private boolean canAfford(int cost) {
        return (cost <= ServiceLocator.getCurrencyService().getScrap().getAmount());
    }

    /**
     * Load the sound assets related to in-game tower building activity
     */
    private void loadSounds() {
        ServiceLocator.getResourceService().loadSounds(sounds);
        ServiceLocator.getResourceService().loadAll();
        buildSound = ServiceLocator.getResourceService()
                .getAsset("sounds/economy/buildSound.ogg", Sound.class);
        errorSound = ServiceLocator.getResourceService()
                .getAsset("sounds/ui/switch/switch_01.ogg", Sound.class);
    }
}
