//package com.csse3200.game.input;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Graphics;
//import com.badlogic.gdx.graphics.g2d.TextureAtlas;
//import com.badlogic.gdx.maps.tiled.TiledMap;
//import com.badlogic.gdx.math.Vector2;
//import com.csse3200.game.GdxGame;
//import com.csse3200.game.components.CameraComponent;
//import com.csse3200.game.currency.Currency;
//import com.csse3200.game.entities.Entity;
//import com.csse3200.game.entities.EntityService;
//import com.csse3200.game.entities.factories.TowerFactory;
//import com.csse3200.game.extensions.GameExtension;
//import com.csse3200.game.physics.PhysicsService;
//import com.csse3200.game.rendering.DebugRenderer;
//import com.csse3200.game.rendering.RenderService;
//import com.csse3200.game.services.*;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(GameExtension.class)
//class BuildInputComponentTest {
//
//    private BuildInputComponent buildInputComponent;
//    private Entity baseTower;
//    private Entity weaponTower;
//    private Entity wallTower;
//    private Entity stunTower;
//    private Entity fireTower;
//    private Entity tntTower;
//    private Entity droidTower;
//    private String[] texture = {
//            "images/towers/turret_deployed.png",
//            "images/towers/turret01.png",
//            "images/towers/wall_tower.png",
//            "images/towers/fire_tower_atlas.png",
//            "images/towers/stun_tower.png",
//            "images/towers/DroidTower.png",
//            "images/towers/TNTTower.png"
//    };
//    private String[] atlas = {
//            "images/towers/turret01.atlas",
//            "images/towers/stun_tower.atlas",
//            "images/towers/fire_tower_atlas.atlas",
//            "images/towers/DroidTower.atlas",
//            "images/towers/TNTTower.atlas",
//            "images/towers/barrier.atlas"
//    };
//    private static final String[] sounds = {
//            "sounds/towers/gun_shot_trimmed.mp3",
//            "sounds/towers/deploy.mp3",
//            "sounds/towers/stow.mp3"
//    };
//
//    @BeforeEach
//    void setup() {
//        Gdx.graphics = mock(Graphics.class);
//        when(Gdx.graphics.getDeltaTime()).thenReturn(10f);
//
//        GameTime gameTime = mock(GameTime.class);
//        CameraComponent camera = mock(CameraComponent.class);
//        when(gameTime.getDeltaTime()).thenReturn(0.02f);
//        ServiceLocator.registerTimeSource(gameTime);
//        ServiceLocator.registerPhysicsService(new PhysicsService());
//        RenderService render = new RenderService();
//        render.setDebug(mock(DebugRenderer.class));
//        ServiceLocator.registerRenderService(render);
//
//        CurrencyService currencyService = new CurrencyService();
//        ResourceService resourceService = new ResourceService();
//        MapService mapService = mock(MapService.class);
//        when(mapService.getComponent().tileToWorldPosition(0,0)).thenReturn(new Vector2(0,0));
//        EntityService entityService = new EntityService();
//
//        ServiceLocator.registerResourceService(resourceService);
//        ServiceLocator.registerCurrencyService(currencyService);
//        ServiceLocator.registerMapService(mapService);
//        ServiceLocator.registerEntityService(entityService);
//
//        resourceService.loadTextures(texture);
//        resourceService.loadTextureAtlases(atlas);
//        resourceService.loadSounds(sounds);
//        resourceService.loadAll();
//
//        ServiceLocator.getResourceService()
//                .getAsset("images/towers/turret01.atlas", TextureAtlas.class);
//        baseTower = TowerFactory.createBaseTower();
//        weaponTower = TowerFactory.createWeaponTower();
//        wallTower = TowerFactory.createWallTower();
//        fireTower = TowerFactory.createFireTower();
//        stunTower = TowerFactory.createFireTower();
//        tntTower = TowerFactory.createTNTTower();
//        droidTower = TowerFactory.createDroidTower();
//
//        buildInputComponent = new BuildInputComponent(camera.getCamera());
//    }
//
//  @Test
//  void shouldUpdatePriority() {
//    int newPriority = 100;
//    InputComponent inputComponent = spy(InputComponent.class);
//
//    inputComponent.setPriority(newPriority);
//    verify(inputComponent).setPriority(newPriority);
//
//    int priority = inputComponent.getPriority();
//    verify(inputComponent).getPriority();
//
//    assertEquals(newPriority, priority);
//  }
//
//  @Test
//  void shouldRegisterOnCreate() {
//    InputService inputService = spy(InputService.class);
//    ServiceLocator.registerInputService(inputService);
//
//    InputComponent inputComponent = spy(InputComponent.class);
//    inputComponent.create();
//    verify(inputService).register(inputComponent);
//  }
//
//  @Test
//  void shouldHandleTouchDown() {
//      BuildInputComponent inputComponent = spy(BuildInputComponent.class);
//      assertFalse(inputComponent.touchDown( 5, 6, 7, 8));
//  }
//
//  @Test
//    void shouldRejectOccupiedTile() {
//        Vector2 tile = ServiceLocator.getMapService().getComponent().tileToWorldPosition(0, 0);
//        tntTower.setPosition(0,0);
//        assertFalse(buildInputComponent.touchDown(0,0, 7,8));
//  }
//
//  @Test
//    void shouldRejectInvalidTile() {
//
//  }
//
//  @Test
//    void shouldHandleMissingMapService() {
//
//  }
//
//  @Test
//    void shouldHandleMissingCurrencyService() {
//
//  }
//
//  @Test
//    void shouldHandleInvalidTower() {
//
//  }
//
//  @Test
//    void shouldHandleMissingEntityService() {
//
//  }
//}
