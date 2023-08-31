package com.csse3200.game.components.tower;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class TowerUpgradeComponentTest {
    Entity entity;

    @BeforeEach
    void beforeEach() {entity = new Entity();}

    @Test
    void increaseAttackStat() {
        TowerUpgraderComponent towerUpgraderComponent = spy(TowerUpgraderComponent.class);
        CombatStatsComponent combatStatsComponent = new CombatStatsComponent(100,10);
        entity.addComponent(towerUpgraderComponent);
        entity.addComponent(combatStatsComponent);
        entity.create();
        entity.getEvents().trigger("upgradeTower", TowerUpgraderComponent.UPGRADE.ATTACK, 10);
        verify(towerUpgraderComponent).upgradeTower(TowerUpgraderComponent.UPGRADE.ATTACK, 10);
        assertEquals(20, combatStatsComponent.getBaseAttack());
    }
}
