package com.csse3200.game.services;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.screens.TowerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * A simplified implementation of the Service Locator pattern:
 * <a href="https://martinfowler.com/articles/injection.html#UsingAServiceLocator">...</a>
 *
 * <p>Allows global access to a few core game services.
 * Warning: global access is a trap and should be used <i>extremely</i> sparingly.
 * Read the wiki for details (<a href="https://github.com/UQcsse3200/game-engine/wiki/Service-Locator">...</a>).
 */
public class ServiceLocator {
  private static final Logger logger = LoggerFactory.getLogger(ServiceLocator.class);
  private static CurrencyService currencyService;
  private static EntityService entityService;
  private static RenderService renderService;
  private static PhysicsService physicsService;
  private static GameTime timeSource;
  private static InputService inputService;
  private static ResourceService resourceService;
  private static GameEndService gameEndService;
  private static WaveService waveService;
  private static MapService mapService;

  private static Array<TowerType> towerTypes = new Array<>();

  public static CurrencyService getCurrencyService() {
      return currencyService;
  }

  public static EntityService getEntityService() {
    return entityService;
  }

  public static RenderService getRenderService() {
    return renderService;
  }

  public static PhysicsService getPhysicsService() {
    return physicsService;
  }

  public static GameTime getTimeSource() {
    return timeSource;
  }

  public static InputService getInputService() {
    return inputService;
  }

  public static ResourceService getResourceService() {
    return resourceService;
  }

  public static GameEndService getGameEndService() {
    return gameEndService;
  }

  public static WaveService getWaveService() { return waveService; }

  public static MapService getMapService() { return mapService; }

  public static void registerCurrencyService(CurrencyService service) {
    logger.debug("Registering currency service {}", service);
    currencyService = service;
  }

  public static void registerEntityService(EntityService service) {
    logger.debug("Registering entity service {}", service);
    entityService = service;
  }

  public static void registerRenderService(RenderService service) {
    logger.debug("Registering render service {}", service);
    renderService = service;
  }

  public static void registerPhysicsService(PhysicsService service) {
    logger.debug("Registering physics service {}", service);
    physicsService = service;
  }

  public static void registerTimeSource(GameTime source) {
    logger.debug("Registering time source {}", source);
    timeSource = source;
  }

  public static void registerInputService(InputService source) {
    logger.debug("Registering input service {}", source);
    inputService = source;
  }

  public static void registerResourceService(ResourceService source) {
    logger.debug("Registering resource service {}", source);
    resourceService = source;
  }

  public static void registerGameEndService(GameEndService source) {
    logger.debug("Registering game end service service {}", source);
    gameEndService = source;
  }
  public static void registerWaveService(WaveService source) {
    logger.debug("Registering wave service {}", source);
    waveService = source;
  }

  public static void registerMapService(MapService source) {
    logger.debug("Registering wave service {}", source);
    mapService = source;
  }

  public static void setTowerTypes(Array<TowerType> selectedTowers) {

    towerTypes.clear();
    towerTypes.addAll(selectedTowers);
  }

  public static Array<TowerType> getTowerTypes() {
    return towerTypes;
  }

  public static void clear() {
    entityService = null;
    renderService = null;
    physicsService = null;
    timeSource = null;
    inputService = null;
    resourceService = null;
    gameEndService = null;
    waveService = null;
    mapService = null;
    towerTypes.clear();
  }

  private ServiceLocator() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
