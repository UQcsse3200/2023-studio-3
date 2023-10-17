package com.csse3200.game.components.tower;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.tasks.CurrencyTask;
import com.csse3200.game.components.tasks.TowerCombatTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class TowerUpgraderComponentTest {
    Entity entity;
    TowerUpgraderComponent towerUpgraderComponent;
    CombatStatsComponent combatStatsComponent;

    @BeforeEach
    void beforeEach() {
        entity = new Entity();
        towerUpgraderComponent = spy(TowerUpgraderComponent.class);
        combatStatsComponent = new CombatStatsComponent(100,10);
    }

    @Test
    void increaseAttackStat() {
        entity.addComponent(towerUpgraderComponent);
        entity.addComponent(combatStatsComponent);
        entity.create();
        entity.getEvents().trigger("upgradeTower", TowerUpgraderComponent.UPGRADE.ATTACK, 10);
        verify(towerUpgraderComponent).upgradeTower(TowerUpgraderComponent.UPGRADE.ATTACK, 10);
        assertEquals(20, combatStatsComponent.getBaseAttack());
    }

    @Test
    void increaseMaxHealthStat() {
        entity.addComponent(towerUpgraderComponent);
        entity.addComponent(combatStatsComponent);
        entity.create();
        entity.getEvents().trigger("upgradeTower", TowerUpgraderComponent.UPGRADE.MAXHP, 50);
        verify(towerUpgraderComponent).upgradeTower(TowerUpgraderComponent.UPGRADE.MAXHP, 50);
        assertEquals(150, combatStatsComponent.getMaxHealth());
    }

    @Test
    void increaseFireRate() {
        entity.addComponent(towerUpgraderComponent);
        AITaskComponent aiTaskComponent = new AITaskComponent();
        ServiceLocator.registerPhysicsService(mock(PhysicsService.class));
        ServiceLocator.registerTimeSource(mock(GameTime.class));
        TowerCombatTask towerCombatTask = new TowerCombatTask(10, 10, 1);
        aiTaskComponent.addTask(towerCombatTask);
        entity.addComponent(aiTaskComponent);
        towerCombatTask.start();
        entity.create();
        entity.getEvents().trigger("upgradeTower", TowerUpgraderComponent.UPGRADE.FIRERATE, 60);
        verify(towerUpgraderComponent).upgradeTower(TowerUpgraderComponent.UPGRADE.FIRERATE, 60);
        assertEquals(((float) 1 /12), towerCombatTask.getFireRateInterval());
    }

    @Test
    void divideByZeroDefaultToIgnore() {
        entity.addComponent(towerUpgraderComponent);
        AITaskComponent aiTaskComponent = new AITaskComponent();
        ServiceLocator.registerPhysicsService(mock(PhysicsService.class));
        ServiceLocator.registerTimeSource(mock(GameTime.class));
        TowerCombatTask towerCombatTask = new TowerCombatTask(10, 10, 1);
        aiTaskComponent.addTask(towerCombatTask);
        entity.addComponent(aiTaskComponent);
        towerCombatTask.start();
        entity.create();
        entity.getEvents().trigger("upgradeTower", TowerUpgraderComponent.UPGRADE.FIRERATE, 60);
        verify(towerUpgraderComponent).upgradeTower(TowerUpgraderComponent.UPGRADE.FIRERATE, 60);
        assertEquals((1/12f), towerCombatTask.getFireRateInterval());
    }

    @Test
    void incomeRate() {
        entity.addComponent(towerUpgraderComponent);
        AITaskComponent aiTaskComponent = new AITaskComponent();
        ServiceLocator.registerTimeSource(mock(GameTime.class));
        CurrencyTask currencyTask = new CurrencyTask(10, 10);
        aiTaskComponent.addTask(currencyTask);
        entity.addComponent(aiTaskComponent);
        currencyTask.start();
        entity.create();
        entity.getEvents().trigger("upgradeTower", TowerUpgraderComponent.UPGRADE.INCOME, 60);
        verify(towerUpgraderComponent).upgradeTower(TowerUpgraderComponent.UPGRADE.INCOME, 60);
        assertEquals(60, currencyTask.getInterval());
    }
}
