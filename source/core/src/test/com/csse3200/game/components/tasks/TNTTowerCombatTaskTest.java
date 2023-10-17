package com.csse3200.game.components.tasks;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.csse3200.game.ai.tasks.AITaskComponent;
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
class TNTTowerCombatTaskTest {
    TNTTowerCombatTask tntTowerCombatTask;

    @BeforeEach
    void setUp() {
        GameTime gameTime = mock(GameTime.class);

        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());

        tntTowerCombatTask = new TNTTowerCombatTask(2,4);
    }

    @Test
    void testStartTriggersDefaultEvent() {
        Entity entity = createTNT();

        EventListener0 defaultStartListener = mock(EventListener0.class);
        entity.getEvents().addListener(TNTTowerCombatTask.DEFAULT, defaultStartListener);

        tntTowerCombatTask.start();

        verify(defaultStartListener).handle();
    }

    @Test
    void testUpdateTowerStateWithTargetInRange() {
        Entity entity = createTNT();
        entity.setPosition(10,10);

        Entity Target = createNPC();
        Target.setPosition(12,10);

        EventListener0 dig = mock(EventListener0.class);
        EventListener0 explode = mock(EventListener0.class);
        EventListener0 damage = mock(EventListener0.class);
        // still in idle
        assertEquals(TNTTowerCombatTask.STATE.IDLE, tntTowerCombatTask.getState());
        entity.getEvents().addListener(TNTTowerCombatTask.DIG, dig);
        entity.getEvents().addListener(TNTTowerCombatTask.EXPLOSION,explode);
        entity.getEvents().addListener(TNTTowerCombatTask.DAMAGE,damage);

        ServiceLocator.getPhysicsService().getPhysics().update();
        entity.update();
        // TNT saw the target
        assertTrue(tntTowerCombatTask.isTargetVisible());

        tntTowerCombatTask.updateTowerState();
        // TNT just Dug into the ground
        verify(dig).handle();
        // READY TO EXPLODE !!!
        assertEquals(TNTTowerCombatTask.STATE.EXPLODE, tntTowerCombatTask.getState());

        tntTowerCombatTask.updateTowerState();

        // BOOOOOOOOM !!
        verify(explode).handle();
        // Apply Damage and Knock-back to Target
        verify(damage).handle();

        // Ready to dispose TNT
        assertEquals(TNTTowerCombatTask.STATE.REMOVE, tntTowerCombatTask.getState());

        tntTowerCombatTask.updateTowerState();
        // Set flag to dispose
        assertTrue(tntTowerCombatTask.isReadyToDelete());
    }

    @Test
    void testStayAtIdleWhenNoTargetInRange() {
        Entity entity = createTNT();
        entity.setPosition(10,10);

        Entity Target = createNPC();
        Target.setPosition(15,10);

        EventListener0 defaultStartListener = mock(EventListener0.class);
        // still in idle
        assertEquals(TNTTowerCombatTask.STATE.IDLE, tntTowerCombatTask.getState());
        entity.getEvents().addListener(TNTTowerCombatTask.DIG, defaultStartListener);

        ServiceLocator.getPhysicsService().getPhysics().update();
        entity.update();
        // Target not in range
        assertFalse(tntTowerCombatTask.isTargetVisible());

        tntTowerCombatTask.updateTowerState();

        verifyNoInteractions(defaultStartListener);
        // still in idle
        assertEquals(TNTTowerCombatTask.STATE.IDLE, tntTowerCombatTask.getState());
    }

    Entity createTNT() {
        AITaskComponent aiTaskComponent = new AITaskComponent().addTask(tntTowerCombatTask);
        Entity entity = new Entity().addComponent(aiTaskComponent).
                addComponent(new PhysicsComponent())
                .addComponent(new HitboxComponent())
                .addComponent(new ColliderComponent());
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
