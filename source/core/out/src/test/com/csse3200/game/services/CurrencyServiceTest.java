package com.csse3200.game.services;

import com.csse3200.game.components.gamearea.CurrencyDisplay;
import com.csse3200.game.currency.Scrap;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
class CurrencyServiceTest {
    @BeforeEach
    void setUp() {
        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        resourceService.loadSounds(new String[]{"sounds/economy/click.wav"});
        resourceService.loadAll();

    }
    @Test
    void shouldReturnScrap() {
        CurrencyService currencyService = new CurrencyService();
        assertEquals(Scrap.class, currencyService.getScrap().getClass());
    }

    @Test
    void shouldReturnDisplay() {
        CurrencyService currencyService = new CurrencyService();
        assertEquals(CurrencyDisplay.class, currencyService.getDisplay().getClass());
    }
}
