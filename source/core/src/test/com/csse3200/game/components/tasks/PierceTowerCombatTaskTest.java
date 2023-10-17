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
class PierceTowerCombatTaskTest {
    PierceTowerCombatTask pierceTowerCombatTask;

    @BeforeEach
    void setUp() {
        GameTime gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        pierceTowerCombatTask = new PierceTowerCombatTask(1, 4);
    }

    /**
     * this test has been implement using the same logic as the tests implemented
     * in DroidCombatTaskTest by Mohamad Dabboussi
     */
    @Test
    void testStartTriggersIdleEvent() {
        Entity entity = createPierceTower();
        EventListener0 idleListener = mock(EventListener0.class);
        // Deploy Droid in the walking state
        entity.getEvents().addListener(PierceTowerCombatTask.IDLE, idleListener);
        pierceTowerCombatTask.start();
        verify(idleListener).handle();
    }

    /**
     * this test has been implement using the same logic as the tests implemented
     * in DroidCombatTaskTest by Mohamad Dabboussi
     */
    @Test
    void testUpdateTowerStateWithTargetInRange() {
        Entity entity = createPierceTower();
        entity.setPosition(10, 10);

        Entity target = createNPC();
        target.setPosition(12, 10);

        EventListener0 attack = mock(EventListener0.class);
        entity.getEvents().addListener(PierceTowerCombatTask.ATTACK, attack);
        //Jump to IDLE state
        pierceTowerCombatTask.start();
        pierceTowerCombatTask.setTowerState(PierceTowerCombatTask.STATE.IDLE);

        ServiceLocator.getPhysicsService().getPhysics().update();
        entity.update();

        assertTrue(pierceTowerCombatTask.isTargetVisible());

        pierceTowerCombatTask.updateTowerState();
        verify(attack).handle();
        assertEquals(PierceTowerCombatTask.STATE.ATTACK, pierceTowerCombatTask.getState());
    }

    /**
     * this test has been implement using the same logic as the tests implemented
     * in DroidCombatTaskTest by Mohamad Dabboussi
     */
    @Test
    void testUpdateTowerStateWithTargetNotInRange() {
        Entity entity = createPierceTower();
        entity.setPosition(10, 10);

        Entity target = createNPC();
        target.setPosition(15, 10);

        EventListener0 idle = mock(EventListener0.class);
        EventListener0 attack = mock(EventListener0.class);
        entity.getEvents().addListener(PierceTowerCombatTask.IDLE, idle);
        entity.getEvents().addListener(PierceTowerCombatTask.ATTACK, attack);

        pierceTowerCombatTask.setTowerState(PierceTowerCombatTask.STATE.IDLE);

        ServiceLocator.getPhysicsService().getPhysics().update();
        entity.update();
        assertFalse(pierceTowerCombatTask.isTargetVisible());

        pierceTowerCombatTask.updateTowerState();

        verify(idle).handle();
        verifyNoInteractions(attack);
        assertEquals(PierceTowerCombatTask.STATE.IDLE, pierceTowerCombatTask.getState());
    }

    Entity createPierceTower() {
        AITaskComponent aiTaskComponent = new AITaskComponent().addTask(pierceTowerCombatTask);
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
