package com.csse3200.game.components.tower;

import com.csse3200.game.components.tower.TowerUpgraderComponent;
import com.csse3200.game.input.InputService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.events.listeners.EventListener2;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class TowerUpgradeComponentTest {

    @BeforeEach
    @Test
    void doesNotCrash() {
        Entity entity = new Entity();
        InputService inputService = new InputService();
        ServiceLocator.registerInputService(inputService);
        TowerUpgraderComponent towerUpgraderComponent = spy(TowerUpgraderComponent.class);
        entity.addComponent(towerUpgraderComponent);
        entity.create();
        entity.getEvents().trigger("upgradeTower", TowerUpgraderComponent.UPGRADE.ATTACK, 0);
        verify(towerUpgraderComponent).upgradeTower(TowerUpgraderComponent.UPGRADE.ATTACK, 0);
    }
}
