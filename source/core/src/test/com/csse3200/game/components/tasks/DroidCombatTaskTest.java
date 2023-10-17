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
class DroidCombatTaskTest {
    DroidCombatTask droidCombatTask;

    @BeforeEach
    void setUp() {
        GameTime gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        droidCombatTask = new DroidCombatTask(1, 4);
    }

    @Test
    void testStartTriggersWalkEvent() {
        Entity entity = createDroid();
        EventListener0 walkListener = mock(EventListener0.class);
        // Deploy Droid in the walking state
        entity.getEvents().addListener(DroidCombatTask.WALK, walkListener);
        droidCombatTask.start();
        verify(walkListener).handle();
    }

    @Test
    void testUpdateTowerStateWithTargetInRange() {
        Entity entity = createDroid();
        entity.setPosition(10,10);

        Entity Target = createNPC();
        Target.setPosition(12,10);

        EventListener0 attackUp = mock(EventListener0.class);
        EventListener0 attackDown = mock(EventListener0.class);
        EventListener0 switchDown = mock(EventListener0.class);
        EventListener0 shootUp = mock(EventListener0.class);
        EventListener0 shootDown = mock(EventListener0.class);
        entity.getEvents().addListener(DroidCombatTask.ATTACK_UP, attackUp);
        entity.getEvents().addListener(DroidCombatTask.SHOOT_UP,shootUp);
        entity.getEvents().addListener(DroidCombatTask.ATTACK_DOWN, attackDown);
        entity.getEvents().addListener(DroidCombatTask.GO_DOWN,switchDown);
        entity.getEvents().addListener(DroidCombatTask.SHOOT_DOWN,shootDown);
        //Jump to IDLE state
        droidCombatTask.start();
        droidCombatTask.setState(DroidCombatTask.STATE.IDLE);

        ServiceLocator.getPhysicsService().getPhysics().update();
        entity.update();

        assertTrue(droidCombatTask.isTargetVisible());

        droidCombatTask.updateTowerState();
        // By default, Droid aims from top, so shoot from top
        verify(attackUp).handle();
        // shoot projectiles from top
        verify(shootUp).handle();
        assertEquals(DroidCombatTask.STATE.DOWN, droidCombatTask.getState());

        droidCombatTask.updateTowerState();
        // switch to aim downwards
        verify(switchDown).handle();
        assertEquals(DroidCombatTask.STATE.SHOOT_DOWN, droidCombatTask.getState());

        ServiceLocator.getPhysicsService().getPhysics().update();
        entity.update();
        // check if the target is still there to shoot from below
        assertTrue(droidCombatTask.isTargetVisible());

        droidCombatTask.updateTowerState();
        // Shoot from below
        verify(attackDown).handle();
        //shoot projectiles from below
        verify(shootUp).handle();
        // switch back to aim from top
        assertEquals(DroidCombatTask.STATE.UP, droidCombatTask.getState());
    }

    @Test
    void testUpdateTowerStateWithTargetNotInRange() {
        Entity entity = createDroid();
        entity.setPosition(10, 10);

        Entity Target = createNPC();
        Target.setPosition(15, 10);

        EventListener0 idle = mock(EventListener0.class);
        EventListener0 attackUp = mock(EventListener0.class);
        entity.getEvents().addListener(DroidCombatTask.IDLE, idle);
        entity.getEvents().addListener(DroidCombatTask.ATTACK_UP,attackUp);
        //Jump to IDLE state
        droidCombatTask.setState(DroidCombatTask.STATE.IDLE);

        ServiceLocator.getPhysicsService().getPhysics().update();
        entity.update();
        // Target out of range
        assertFalse(droidCombatTask.isTargetVisible());

        droidCombatTask.updateTowerState();
        // Droid will remain in Idle and will not shoot
        verify(idle).handle();
        verifyNoInteractions(attackUp);
        assertEquals(DroidCombatTask.STATE.IDLE, droidCombatTask.getState());
    }

    Entity createDroid() {
        AITaskComponent aiTaskComponent = new AITaskComponent().addTask(droidCombatTask);
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
