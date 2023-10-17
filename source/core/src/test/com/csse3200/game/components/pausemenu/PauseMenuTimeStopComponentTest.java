package com.csse3200.game.components.pausemenu;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.WaveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class PauseMenuTimeStopComponentTest {
    Entity pauseMenu;
    Entity entity;
    @BeforeEach
    void beforeEach() {
        EntityService entityService = new EntityService();
        ServiceLocator.registerEntityService(entityService);
        WaveService waveService = new WaveService();
        ServiceLocator.registerWaveService(waveService);
        GameTime gameTime = new GameTime();
        ServiceLocator.registerTimeSource(gameTime);
        entity = mock(Entity.class);
        when(entity.getId()).thenReturn(-1); //Ensure it does not coincide with the pause menu's ID
        entityService.register(entity);
        PauseMenuTimeStopComponent timeStopComponent = new PauseMenuTimeStopComponent();
        pauseMenu = new Entity();
        pauseMenu.addComponent(timeStopComponent);
    }

    @Test
    void pausesEntities() {
        ServiceLocator.getEntityService().register(pauseMenu);
        verify(entity).setEnabled(false);
    }

    @Test
    void unpausesEntitiesWhenDisposed() {
        ServiceLocator.getEntityService().register(pauseMenu);
        pauseMenu.dispose();
        verify(entity).setEnabled(true);
    }

    @Test
    void doesNotPauseNewEntities() {
        ServiceLocator.getEntityService().register(pauseMenu);
        Entity lateEntity = mock(Entity.class);
        when(entity.getId()).thenReturn(-1); //Ensure it does not coincide with the pause menu's ID
        ServiceLocator.getEntityService().register(lateEntity);
        verify(lateEntity, times(0)).setEnabled(false);
    }
}
