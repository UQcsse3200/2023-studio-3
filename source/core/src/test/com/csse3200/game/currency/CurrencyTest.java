package com.csse3200.game.currency;

import com.csse3200.game.components.npc.DropComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.DropFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class CurrencyTest {
    private Entity scrap;
    private String[] texture = {"images/economy/scrap.png"};

    @BeforeEach
    public void setUp() {
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(0.02f);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        RenderService render = new RenderService();
        render.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(render);
        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        resourceService.loadTextures(texture);
        resourceService.loadAll();
        scrap = DropFactory.createScrapDrop();
    }
    @Test
    void shouldCreateCurrency() {
        Currency currency = mock(Currency.class, CALLS_REAL_METHODS);
        assertEquals(0, currency.getAmount(),
                "The currency should be set to 0");
    }

    @Test
    void shouldCreateCurrencyWithAmount() {
        Currency currency = mock(Currency.class, CALLS_REAL_METHODS);
        currency = new Scrap();
        assertEquals(100, currency.getAmount(),
                "The currency should be set to 100");
    }

    @Test
    void scrapIsNotNull() {
        assertNotNull(scrap, "Scrap should not be null");
    }
    @Test
    void testCreateScrapComponentNotNull() {
        assertNotNull(scrap.getComponent(DropComponent.class),
                "Scrap component should not be null");
    }

    @Test
    void shouldSetAndGetAmount() {
        Currency currency = mock(Currency.class, CALLS_REAL_METHODS);
        int value = 100;
        currency.setAmount(value);
        assertEquals(value, currency.getAmount());
    }

    @Test
    void shouldModifyAmount() {
        Currency currency = mock(Currency.class, CALLS_REAL_METHODS);
        int value = 100;
        currency.setAmount(value);
        currency.modify(value);
        assertEquals(200, currency.getAmount());

        currency.modify(-100);
        assertEquals(100, currency.getAmount());
    }

    @Test
    void testCanBuy() {
        Currency currency = mock(Currency.class, CALLS_REAL_METHODS);
        currency.setAmount(100);

        assertFalse(currency.canBuy(200));
        assertTrue(currency.canBuy(60));
    }

}
