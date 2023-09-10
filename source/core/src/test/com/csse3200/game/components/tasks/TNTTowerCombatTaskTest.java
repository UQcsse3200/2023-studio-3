package com.csse3200.game.components.tasks;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.TowerFactory;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TNTTowerCombatTaskTest {



    Entity entity;

    TNTTowerCombatTask tntTowerCombatTask;

    @BeforeEach
    void setUp() {
        GameTime gameTime = mock(GameTime.class);

        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());

        tntTowerCombatTask = new TNTTowerCombatTask(2,4);
    }

    @Test
    public void testStartTriggersDefaultEvent() {

        Entity entity = createTNT();

        EventListener0 defaultStartListener = mock(EventListener0.class);
        entity.getEvents().addListener(TNTTowerCombatTask.DEFAULT, defaultStartListener);

        tntTowerCombatTask.start();

        verify(defaultStartListener).handle();
    }

    @Test
    public void testUpdateTowerStateIdleMode() {

        Entity entity = createTNT();
        entity.setPosition(10,10);

        Entity Target = createNPC();
        Target.setPosition(12,10);

        EventListener0 defaultStartListener = mock(EventListener0.class);
        // still in idle
        assertEquals(TNTTowerCombatTask.STATE.IDLE, tntTowerCombatTask.getState());
        entity.getEvents().addListener(TNTTowerCombatTask.DIG, defaultStartListener);

        ServiceLocator.getPhysicsService().getPhysics().update();
        entity.update();
        assertTrue(tntTowerCombatTask.isTargetVisible());

        tntTowerCombatTask.updateTowerState();

        verify(defaultStartListener).handle();
        // ready to explode
        assertEquals(TNTTowerCombatTask.STATE.EXPLODE, tntTowerCombatTask.getState());
    }

    @Test
    public void testGetPriority() {
        // Arrange
        tntTowerCombatTask.readToDelete = false;

        // Act
        int priority = tntTowerCombatTask.getPriority();

        // Assert

        assertEquals(2, priority);
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
