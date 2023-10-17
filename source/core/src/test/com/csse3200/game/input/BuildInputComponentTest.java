package com.csse3200.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.screens.TowerType;
import com.csse3200.game.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class BuildInputComponentTest {

    private BuildInputComponent buildInputComponent;
    EntityService entityService;
    ResourceService resourceService;

    @BeforeEach
    void setup() {
        Gdx.graphics = mock(Graphics.class);
        when(Gdx.graphics.getDeltaTime()).thenReturn(10f);

        GameTime gameTime = mock(GameTime.class);
        CameraComponent camera = mock(CameraComponent.class);
        when(camera.getCamera()).thenReturn(mock(Camera.class));
        when(gameTime.getDeltaTime()).thenReturn(0.02f);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        RenderService render = new RenderService();
        render.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(render);

        CurrencyService currencyService = new CurrencyService();
        resourceService = mock(ResourceService.class);
        MapService mapService = mock(MapService.class);
        when(mapService.getComponent()).thenReturn(mock(TerrainComponent.class));
        entityService = mock(EntityService.class);

        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerCurrencyService(currencyService);
        ServiceLocator.registerMapService(mapService);
        ServiceLocator.registerEntityService(entityService);

        buildInputComponent = new BuildInputComponent(camera.getCamera());
    }

  @Test
  void shouldUpdatePriority() {
    int newPriority = 100;
    InputComponent inputComponent = spy(InputComponent.class);

    inputComponent.setPriority(newPriority);
    verify(inputComponent).setPriority(newPriority);

    int priority = inputComponent.getPriority();
    verify(inputComponent).getPriority();

    assertEquals(newPriority, priority);
  }

  @Test
  void shouldRegisterOnCreate() {
    InputService inputService = spy(InputService.class);
    ServiceLocator.registerInputService(inputService);

    InputComponent inputComponent = spy(InputComponent.class);
    inputComponent.create();
    verify(inputService).register(inputComponent);
  }

  @Test
  void shouldHandleTouchDown() {
      when(entityService.entitiesInTile(5, 5)).thenReturn(false);
      assertFalse(buildInputComponent.touchDown( 5, 5, 7, 8));
  }

  @Test
  void shouldRejectOccupiedOrInvalidTile() {
        // entitiesInTile checks for out of bounds condition as well
        when(entityService.entitiesInTile(5, 5)).thenReturn(true);
        assertFalse(buildInputComponent.touchDown(5,5, 7,8),
                "Attempting to build on an existing tower should return False");
  }

  @Test
  void shouldHandleMissingMapService() {
        when(ServiceLocator.getMapService()).thenReturn(null);
        assertFalse(buildInputComponent.touchDown(5,5,7,8));
  }

  @Test
  void shouldHandleMissingCurrencyService() {
        when(ServiceLocator.getCurrencyService()).thenReturn(null);
        assertFalse(buildInputComponent.touchDown(5,5,7,8));
  }

  @Test
  void shouldHandleNullTowerName() {
        TowerType towerType = mock(TowerType.class);
        when(towerType.getTowerName()).thenReturn(null);
        ServiceLocator.getCurrencyService().setTowerType(towerType);
  }

  @Test
  void shouldHandleNullTowerDesc() {
        TowerType towerType = mock(TowerType.class);
        when(towerType.getDescription()).thenReturn(null);
        ServiceLocator.getCurrencyService().setTowerType(towerType);
  }

  @Test
  void shouldHandleNullTowerCost() {
        TowerType towerType = mock(TowerType.class);
        when(towerType.getPrice()).thenReturn(null);
        ServiceLocator.getCurrencyService().setTowerType(towerType);
  }

  @Test
  void shouldHandleInvalidTowerName() {
        TowerType towerType = mock(TowerType.class);
        when(towerType.getTowerName()).thenReturn(null);
        ServiceLocator.getCurrencyService().setTowerType(towerType);
  }

  @Test
  void shouldHandleMissingEntityService() {
        when(ServiceLocator.getEntityService()).thenReturn(null);
        assertFalse(buildInputComponent.touchDown(5,5,7,8));
  }

  @Test
  void shouldReturnTowerOne() {
        assert(ServiceLocator.getCurrencyService().getTower() == null);
        buildInputComponent.keyUp(Input.Keys.NUM_1);
        assert(ServiceLocator.getCurrencyService().getTower() ==
                ServiceLocator.getTowerTypes().get(0));
  }

    @Test
    void shouldReturnTowerTwo() {
        assert(ServiceLocator.getCurrencyService().getTower() == null);
        buildInputComponent.keyUp(Input.Keys.NUM_2);
        assert(ServiceLocator.getCurrencyService().getTower() ==
                ServiceLocator.getTowerTypes().get(1));
    }

    @Test
    void shouldReturnTowerThree() {
        assert(ServiceLocator.getCurrencyService().getTower() == null);
        buildInputComponent.keyUp(Input.Keys.NUM_3);
        assert(ServiceLocator.getCurrencyService().getTower() ==
                ServiceLocator.getTowerTypes().get(2));
    }

    @Test
    void shouldReturnTowerFour() {
        assert(ServiceLocator.getCurrencyService().getTower() == null);
        buildInputComponent.keyUp(Input.Keys.NUM_4);
        assert(ServiceLocator.getCurrencyService().getTower() ==
                ServiceLocator.getTowerTypes().get(3));
    }

    @Test
    void shouldReturnTowerFive() {
        assert(ServiceLocator.getCurrencyService().getTower() == null);
        buildInputComponent.keyUp(Input.Keys.NUM_5);
        assert(ServiceLocator.getCurrencyService().getTower() ==
                ServiceLocator.getTowerTypes().get(4));
    }
}
