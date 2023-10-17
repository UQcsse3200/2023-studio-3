package com.csse3200.game.entities.factories;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.pausemenu.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.WaveService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(GameExtension.class)
public class PauseMenuFactoryTest {
    Entity entity;
    GdxGame game;

    @BeforeEach
    void beforeEach() {
        EntityService entityService = new EntityService();
        ServiceLocator.registerEntityService(entityService);
        WaveService waveService = new WaveService();
        ServiceLocator.registerWaveService(waveService);
        GameTime gameTime = new GameTime();
        ServiceLocator.registerTimeSource(gameTime);
        RenderService renderService = mock(RenderService.class);
        when(renderService.getStage()).thenReturn(mock(Stage.class));
        ServiceLocator.registerRenderService(renderService);
        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        resourceService.loadAll();

        game = mock(GdxGame.class);
        entity = PauseMenuFactory.createPauseMenu(game);
    }

    @Test
    void createsEntity() {
        assertNotNull(entity);
    }

    @Test
    void entityHasRequiredComponents() {
        assertNotNull(entity.getComponent(PauseMenuTimeStopComponent.class));
        assertNotNull(entity.getComponent(PauseMenuButtonComponent.class));
    }

    @Test
    void duplicateNotCreated() {
        Entity secondEntity = PauseMenuFactory.createPauseMenu(game);
        assertNull(secondEntity);
    }

    @Test
    void createsSecondEntityAfterFirstIsDisposed() {
        entity.dispose();
        Entity secondEntity = PauseMenuFactory.createPauseMenu(game);
        assertNotNull(secondEntity);
    }
}
