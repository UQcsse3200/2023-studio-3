package com.csse3200.game.services;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.ui.UIComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class GameEndServiceTest {

    GameEndService endService;
    @Mock
    RenderService renderService;

    @BeforeEach
    void setUp() {
        endService = new GameEndService();
        renderService = new RenderService();
//        uiComponent = spy(UIComponent.class);

        ServiceLocator.registerGameEndService(endService);
        ServiceLocator.registerRenderService(renderService);
    }

    @Test
    void shouldReturnCount() {
        assertEquals(5, ServiceLocator.getGameEndService().getEngineerCount());
    }

    @Test
    void shouldDecrementCount() {
        ServiceLocator.getGameEndService().updateEngineerCount();
        assertEquals(4, ServiceLocator.getGameEndService().getEngineerCount());
    }

    @Test
    void shouldEndGame() {
        for (int i = 0; i < 5; i++) {
            ServiceLocator.getGameEndService().updateEngineerCount();
        }
        assertTrue(ServiceLocator.getGameEndService().hasGameEnded());
    }
}
