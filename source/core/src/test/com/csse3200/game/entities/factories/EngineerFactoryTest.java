package com.csse3200.game.entities.factories;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.player.HumanAnimationController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class EngineerFactoryTest {

    private final String[] atlas = {"images/engineers/engineer.atlas"};
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
        RenderService render = new RenderService();
        render.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(render);
        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        resourceService.loadTextureAtlases(atlas);
        resourceService.loadSounds(sounds);
        resourceService.loadAll();
    }

    @Test
    void createBaseHumanNPC() {
        Entity human = EngineerFactory.createBaseHumanNPC();
        assertNotNull(human);
    }

    @Test
    void testCreateEngineer() {
        // Check engineer exists after creation
        Entity engineer = EngineerFactory.createEngineer();
        assertNotNull(engineer, "Engineer entity should not be null after creation");
    }

    @Test
    void testDeleteEngineer() {
        Entity engineer = EngineerFactory.createEngineer();
        assertFalse(engineer.getFlagForDelete(), "Engineer flagForDelete should be false on creation");
        engineer.setFlagForDelete(true);
        assertTrue(engineer.getFlagForDelete(), "Engineer getflagForDelete should return true after being set");
    }

    @Test
    void testEngineerPhysicsComponents() {
        Entity engineer = EngineerFactory.createEngineer();
        assertNotNull(engineer.getComponent(PhysicsComponent.class),
                "Engineer should have a PhysicsComponent");
        assertNotNull(engineer.getComponent(PhysicsMovementComponent.class),
                "Engineer should have a PhysicsMovementComponent");
    }

    @Test
    void testEngineerColliderAndHitboxComponents() {
        Entity engineer = EngineerFactory.createEngineer();
        assertNotNull(engineer.getComponent(ColliderComponent.class),
                "Engineer should have a ColliderComponent");
        assertNotNull(engineer.getComponent(HitboxComponent.class),
                "Engineer should have a HitBoxComponent");
    }

    @Test
    void testEngineerTouchAttackAndCombatStatsComponents() {
        Entity engineer = EngineerFactory.createEngineer();
        assertNotNull(engineer.getComponent(TouchAttackComponent.class),
                "Engineer should have a TouchAttackComponent");
        assertNotNull(engineer.getComponent(CombatStatsComponent.class),
                "Engineer should have a CombatStatsComponent");
    }

    @Test
    void testEngineerAnimationAndAITaskComponents() {
        Entity engineer = EngineerFactory.createEngineer();
        assertNotNull(engineer.getComponent(HumanAnimationController.class),
                "Engineer should have a HumanAnimationController");
        assertNotNull(engineer.getComponent(AITaskComponent.class),
                "Engineer should have an AITaskComponent");
        assertNotNull(engineer.getComponent(AnimationRenderComponent.class),
                "Engineer should have an AnimationRenderComponent");
    }

    @Test
    void testEngineerAnimations() {
        Entity engineer = EngineerFactory.createEngineer();
        for (String animation : this.animations) {
            assertTrue(engineer.getComponent(AnimationRenderComponent.class).hasAnimation(animation),
                    ("Engineer AnimationRenderComponent should contain animation [" + animation + "]"));
        }
        for (String animation : this.animations) {
            engineer.getComponent(AnimationRenderComponent.class).startAnimation(animation);
            assert(Objects.equals(engineer.getComponent(AnimationRenderComponent.class).getCurrentAnimation(), animation));
        }
    }

    @Test
    void testEngineerConfig() {
        Entity engineer = EngineerFactory.createEngineer();
        assert(engineer.getComponent(CombatStatsComponent.class).getHealth() == 100);
        assert(engineer.getComponent(CombatStatsComponent.class).getBaseAttack() == 5);
    }

    /**
     * Adapted from TowerFactoryTest testAttackerCollisionWithWall by MohamadDab11
     */
    @Test
    void testEngineerCollisions() {
        Entity engineer = EngineerFactory.createEngineer();

        Entity attacker = createAttacker(engineer.getComponent(HitboxComponent.class).getLayer());

        engineer.setPosition(10f,10f);
        attacker.setPosition(10f,10f);
        engineer.create();

        assertEquals(100, engineer.getComponent(CombatStatsComponent.class).getHealth());

        ServiceLocator.getPhysicsService().getPhysics().update();

        assertEquals(90, engineer.getComponent(CombatStatsComponent.class).getHealth());

    }

    Entity createAttacker(short targetLayer) {
        Entity entity =
                new Entity()
                        .addComponent(new TouchAttackComponent(targetLayer))
                        .addComponent(new CombatStatsComponent(0, 10))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent());
        entity.create();
        return entity;
    }
}
