package com.csse3200.game.components.popupmenu;

import com.csse3200.game.input.InputService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class PopupMenuInputComponentTest {

    @Test
    void handlesTouchDownEntityIncluded(){
        EventListener0 listener = mock(EventListener0.class);
        Entity entity = new Entity();
        InputService inputService = new InputService();
        ServiceLocator.registerInputService(inputService);
        PopupMenuInputComponent popupMenuInputComponent = new PopupMenuInputComponent();//spy(PopupMenuInputComponent.class);
        entity.addComponent(popupMenuInputComponent);
        inputService.register(popupMenuInputComponent);
        entity.getEvents().addListener("popupEvent", listener);
        entity.create();
        assertTrue(popupMenuInputComponent.touchDown(5, 6, 7, 8));
        verify(listener).handle();
    }
}
