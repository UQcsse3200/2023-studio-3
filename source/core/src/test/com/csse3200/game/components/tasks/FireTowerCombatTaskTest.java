package com.csse3200.game.components.tasks;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FireTowerCombatTaskTest {
    FireTowerCombatTask fireTowerCombatTask;

    @BeforeEach
    void setUp() {
        GameTime gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        fireTowerCombatTask = new FireTowerCombatTask(1, 4);
    }

    /**
     * this test has been implement using the same logic as the tests implemented
     * in DroidCombatTaskTest by Mohamad Dabboussi
     */
    @Test
    void testStartTriggersIdleEvent() {
        Entity entity = createFireTower();
        EventListener0 idleListener = mock(EventListener0.class);
        // Deploy Droid in the walking state
        entity.getEvents().addListener(FireTowerCombatTask.IDLE, idleListener);
        fireTowerCombatTask.start();
        verify(idleListener).handle();
    }

    /**
     * this test has been implement using the same logic as the tests implemented
     * in DroidCombatTaskTest by Mohamad Dabboussi
     */
    @Test
    void testUpdateTowerStateWithTargetInRange() {
        Entity entity = createFireTower();
        entity.setPosition(10, 10);

        Entity target = createNPC();
        target.setPosition(12, 10);

        EventListener0 prepAttack = mock(EventListener0.class);
        EventListener0 attack = mock(EventListener0.class);
        entity.getEvents().addListener(FireTowerCombatTask.PREP_ATTACK, prepAttack);
        entity.getEvents().addListener(FireTowerCombatTask.ATTACK, attack);
        //Jump to IDLE state
        fireTowerCombatTask.start();
        fireTowerCombatTask.setTowerState(FireTowerCombatTask.STATE.IDLE);

        ServiceLocator.getPhysicsService().getPhysics().update();
        entity.update();

        assertTrue(fireTowerCombatTask.isTargetVisible());

        fireTowerCombatTask.updateTowerState();
        verify(prepAttack).handle();
        assertEquals(FireTowerCombatTask.STATE.PREP_ATTACK, fireTowerCombatTask.getState());

        ServiceLocator.getPhysicsService().getPhysics().update();
        entity.update();
        assertTrue(fireTowerCombatTask.isTargetVisible());

        fireTowerCombatTask.updateTowerState();
        verify(attack).handle();
        assertEquals(FireTowerCombatTask.STATE.ATTACK, fireTowerCombatTask.getState());
    }

    /**
     * this test has been implement using the same logic as the tests implemented
     * in DroidCombatTaskTest by Mohamad Dabboussi
     */
    @Test
    void testUpdateTowerStateWithTargetNotInRange() {
        Entity entity = createFireTower();
        entity.setPosition(10, 10);

        Entity target = createNPC();
        target.setPosition(15, 10);

        EventListener0 idle = mock(EventListener0.class);
        EventListener0 prepAttack = mock(EventListener0.class);
        entity.getEvents().addListener(FireTowerCombatTask.IDLE, idle);
        entity.getEvents().addListener(FireTowerCombatTask.PREP_ATTACK, prepAttack);

        fireTowerCombatTask.setTowerState(FireTowerCombatTask.STATE.IDLE);

        ServiceLocator.getPhysicsService().getPhysics().update();
        entity.update();
        assertFalse(fireTowerCombatTask.isTargetVisible());

        fireTowerCombatTask.updateTowerState();

        verify(idle).handle();
        verifyNoInteractions(prepAttack);
        assertEquals(FireTowerCombatTask.STATE.IDLE, fireTowerCombatTask.getState());
    }

    Entity createFireTower() {
        AITaskComponent aiTaskComponent = new AITaskComponent().addTask(fireTowerCombatTask);
        Entity entity = new Entity().addComponent(aiTaskComponent)
                .addComponent(new PhysicsComponent())
                .addComponent(new HitboxComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new CombatStatsComponent(100,10));
        entity.create();
        return entity;
    }

    Entity createNPC() {
        Entity Target = new Entity().addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new ColliderComponent())
                .addComponent(new PhysicsComponent());
        Target.create();
        return Target;
    }
}
