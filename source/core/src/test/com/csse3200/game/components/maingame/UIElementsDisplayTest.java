//package com.csse3200.game.components.maingame;
//
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.Skin;
//import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
//import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
//import com.csse3200.game.components.maingame.UIElementsDisplay;
//import com.csse3200.game.rendering.RenderService;
//import com.csse3200.game.screens.MainGameScreen;
//import com.csse3200.game.services.ResourceService;
//import com.csse3200.game.services.ServiceLocator;
//import com.csse3200.game.services.WaveService;
//import com.csse3200.game.ui.ButtonFactory;F
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//
//import java.security.Provider;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNull;
//import static org.mockito.Mockito.*;
//
//public class UIElementsDisplayTest {
//
//    private UIElementsDisplay display;
//
//    @Before
//    public void setUp() {
//        RenderService renderService = mock(RenderService.class);
//        ResourceService resourceService = mock(ResourceService.class);
//        WaveService waveService = mock(WaveService.class);
//
//        display = new UIElementsDisplay();
//
//        ServiceLocator.registerRenderService(renderService);
//        ServiceLocator.registerResourceService(resourceService);
//        ServiceLocator.registerWaveService(waveService);
//
//        when(ServiceLocator.getWaveService().getEnemyCount()).thenReturn(10);
//        when(ServiceLocator.getWaveService().getNextWaveTime()).thenReturn(10000L); // Example time
//        when(ServiceLocator.getWaveService().getDisplay()).thenReturn(display);
//
//    }
//
//    @Test
//    public void testUpdateMobCount() {
//        TextButton mobButton = display.getRemainingMobsButton();
//        assertEquals("Mobs:10", mobButton.getText());
//
//        display.updateMobCount();
//        assertEquals("Mobs:9", mobButton.getText());
//    }
//
//    @Test
//    public void testCreateTimerButton() {
//        assertNull(display.getTimerButton());
//        display.createTimerButton();
//        assertEquals("Next wave in:10", display.getTimerButton().getText());
//    }
//}
