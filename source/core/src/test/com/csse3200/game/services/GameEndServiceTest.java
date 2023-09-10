package com.csse3200.game.services;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(GameExtension.class)
class GameEndServiceTest {

    GameEndService endService;

    @BeforeEach
    void setUp() {
        endService = new GameEndService();
        ServiceLocator.registerGameEndService(endService);
    }

    @Test
    void shouldReturnCount() {
        assertEquals(5, ServiceLocator.getGameEndService().getEngineerCount());
    }

//    @Test
//    void shouldDecrementCount() {
//        ServiceLocator.getGameEndService().updateEngineerCount();
//        assertEquals(4, ServiceLocator.getGameEndService().getEngineerCount());
//    }
//
//    @Test
//    void shouldEndGame() {
//        for (int i = 0; i < 5; i++) {
//            ServiceLocator.getGameEndService().updateEngineerCount();
//        }
//        assertTrue(ServiceLocator.getGameEndService().hasGameEnded());
//    }
}
