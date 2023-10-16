package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.tasks.human.EngineerCombatTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.EngineerFactory;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class EngineerCombatTaskTest {
    private EngineerCombatTask COMBAT_TASK;
    private Entity MOCK_ENGINEER;
    
    private final String[] atlas = {"images/engineers/engineer.atlas"};
    private final String[] projectileAtlas = {"images/projectiles/engineer_projectile.atlas"};
    private static final String[] sounds = {
            "sounds/engineers/firing_auto.mp3",
            "sounds/engineers/firing_single.mp3"
    };
    
    private final String[] animations = {
            "idle_right",
            "walk_left",
            "walk_right",
            "walk_prep",
            "prep",
            "firing_auto",
            "firing_single",
            "hit",
            "death"
    };
    
    @BeforeEach
    void setUp() {
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(0.02f);
        
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        RenderService render = new RenderService();
        render.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(render);
        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        resourceService.loadTextureAtlases(atlas);
        resourceService.loadTextureAtlases(projectileAtlas);
        resourceService.loadSounds(sounds);
        resourceService.loadAll();
        
        // Create a mock engineer and add the combat task.
        int MAX_RANGE = 10;
        COMBAT_TASK = new EngineerCombatTask(MAX_RANGE);
        MOCK_ENGINEER = EngineerFactory.createEngineer();
        MOCK_ENGINEER.getComponent(AITaskComponent.class).addTask(COMBAT_TASK);
        COMBAT_TASK.create(MOCK_ENGINEER.getComponent(AITaskComponent.class));
    }
    
    /**
     * Tests that the task correctly triggers an idle event.
     */
    @Test
    void testIdleEvent() {
        
        // Add a listener to the engineer to test for the idle animation trigger.
        EventListener0 idleListener = mock(EventListener0.class);
        MOCK_ENGINEER.getEvents().addListener("idleRight", idleListener);
        
        // Start the task.
        COMBAT_TASK.start();
        
        // Update physics and check whether the event is triggered.
        updatePhysics();
        verify(idleListener).handle();
    }
    
    /**
     * Adapted from EngineerFactoryTest: Author - The-AhmadAA
     *
     * Tests that the task correctly switches to the
     * firing state and triggers a firing event.
     */
    @Test
    void testFiringEvent() {
        
        // Create an attacker entity.
        Entity MOCK_ATTACKER = createAttacker(PhysicsLayer.NPC);
        
        // Add a listener to the engineer to test for the firing animation trigger.
        EventListener0 firingListener = mock(EventListener0.class);
        MOCK_ENGINEER.getEvents().addListener("firingSingleStart", firingListener);
        
        // Set testing positions for the engineer and the attacker and start the task.
        MOCK_ENGINEER.setPosition(10,10);
        MOCK_ATTACKER.setPosition(12,10);
        COMBAT_TASK.start();
        
        // Update physics and check whether the event is triggered.
        updatePhysics();
        verify(firingListener).handle();
    }
    
    /**
     * Adapted from EngineerFactoryTest: Author - The-AhmadAA
     *
     * Test that the task can switch the state back to idle after mob isn't visible.
     */
    @Test
    void testIdleAfterMobKilled() {
        // Create an attacker entity.
        Entity MOCK_ATTACKER = createAttacker(PhysicsLayer.NPC);
        
        // Set testing positions for the engineer and the attacker and start the task.
        MOCK_ENGINEER.setPosition(10,10);
        MOCK_ATTACKER.setPosition(12,10);
        COMBAT_TASK.start();
        
        // Update physics, move the attacker out of range
        // and check whether the event is triggered.
        updatePhysics();
        MOCK_ATTACKER.dispose();
        EventListener0 idleListener = mock(EventListener0.class);
        MOCK_ENGINEER.getEvents().addListener("idleRight", idleListener);
        updatePhysics();
        
        // Verify the idle animation trigger's occurrence if the attacker is gone.
        if (!COMBAT_TASK.isTargetVisible()) {
            verify(idleListener).handle();
            
        // Fail the test if the attacked remains visible after deletion.
        } else {
            fail();
        }
    }
    
    @Test
    void testProjectileFired() {
        // Create an attacker entity.
        Entity MOCK_ATTACKER = createAttacker(PhysicsLayer.NPC);
        
        // Set testing positions for the engineer and the attacker and start the task.
        MOCK_ENGINEER.setPosition(10,10);
        MOCK_ATTACKER.setPosition(12,10);
        COMBAT_TASK.start();
        
        // Update physics and check whether a projectile is fired.
        updatePhysics();
        EventListener0 projectileListener = mock(EventListener0.class);
        MOCK_ENGINEER.getEvents().addListener("engineerProjectileFired", projectileListener);
        updatePhysics();
        
        verify(projectileListener).handle();
        
    }
    
    /**
     * Adapted from EngineerFactoryTest: Author - The-AhmadAA
     */
    Entity createAttacker(short targetLayer) {
        Entity entity =
                new Entity()
                        .addComponent(new TouchAttackComponent(targetLayer))
                        .addComponent(new CombatStatsComponent(0, 10))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(targetLayer));
        entity.create();
        return entity;
    }
    
    /**
     * Update the game physics and the task state.
     */
    void updatePhysics() {
        ServiceLocator.getPhysicsService().getPhysics().update();
        COMBAT_TASK.updateEngineerState();
    }
}