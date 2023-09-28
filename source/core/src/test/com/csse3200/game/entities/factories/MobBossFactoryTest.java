package com.csse3200.game.entities.factories;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.csse3200.game.components.*;
import com.csse3200.game.components.bosses.DemonAnimationController;
import com.csse3200.game.components.bosses.IceBabyAnimationController;
import com.csse3200.game.components.bosses.PatrickAnimationController;
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

@ExtendWith(GameExtension.class)
class MobBossFactoryTest {
    private final String[] atlas = {
            "images/mobboss/demon.atlas",
            "images/mobboss/patrick.atlas",
            "images/mobboss/iceBaby.atlas",
    };

    private final String[] animations = {
            "demon_walk",
            "demon_cleave",
            "demon_take_hit",
            "demon_idle",
            "demon_death",
            "demon_cast_spell",
            "demon_fire_breath",
            "demon_smash",
            "demon_take_hit",
            "idle",
            "move",
            "projectile_explosion",
            "projectile_idle",
            "take_hit",
            "transform",
            "transform_reverse",
            "idle",
            "1_atk",
            "2_atk",
            "3_atk",
            "death",
            "intro_or_revive",
            "stagger",
            "take_hit",
            "walk",
            "patrick_attack",
            "patrick_cast",
            "patrick_death",
            "patrick_hurt",
            "patrick_idle",
            "patrick_spell",
            "patrick_walk"
    };

    private static final String[] sounds = {
            "sounds/mobBoss/iceBabySound.mp3",
            "sounds/mobBoss/iceBabyAOE.mp3",
            "sounds/mobBoss/mobSpawnStomp.mp3"
    };

    @BeforeEach
    public void setUp() {
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
    public void createBaseBoss() {
        Entity mobBoss = MobBossFactory.createBaseBoss();
        assertNotNull(mobBoss);
    }

    @Test
    public void testBaseBossPhysicsComponent() {
        Entity boss = MobBossFactory.createBaseBoss();
        assertNotNull(boss.getComponent(PhysicsComponent.class),
                "Boss does not have Physics component");
    }

    @Test
    public void testBaseBossColliderComponent() {
        Entity boss = MobBossFactory.createBaseBoss();
        assertNotNull(boss.getComponent(ColliderComponent.class),
                "Boss does not have collider component");
    }

    @Test
    public void testBaseBossPhysicsMovementComponent() {
        Entity boss = MobBossFactory.createBaseBoss();
        assertNotNull(boss.getComponent(PhysicsMovementComponent.class),
                "Boss does not have Physics Movement component");
    }

    @Test
    public void testBaseBossHitboxComponent() {
        Entity boss = MobBossFactory.createBaseBoss();
        assertNotNull(boss.getComponent(HitboxComponent.class),
                "Boss does not have Hitbox component");
    }
    @Test
    public void testBaseBossTouchAttackComponent() {
        Entity boss = MobBossFactory.createBaseBoss();
        assertNotNull(boss.getComponent(TouchAttackComponent.class),
                "Boss does not have Touch attack component");
    }

    @Test
    public void testDemonCreation() {
        Entity Demon = MobBossFactory.createDemonBoss();
        assertNotNull(Demon);
    }

    @Test
    public void testDemonAnimationRenderComponent() {
        Entity demon = MobBossFactory.createDemonBoss();
        assertNotNull(demon.getComponent(AnimationRenderComponent.class),
                "Demon does not have an AnimationRenderComponent");
    }
    @Test
    public void testDemonAnimationController() {
        Entity Demon = MobBossFactory.createDemonBoss();
        assertNotNull(Demon.getComponent(DemonAnimationController.class),
                "Demon does not have an Animation Controller");
    }
    @Test
    public void testPatrickCreation() {
        Entity Patrick = MobBossFactory.createPatrickBoss();
        assertNotNull(Patrick);
    }

    @Test
    public void testPatrickAnimationRenderComponent() {
        Entity Patrick = MobBossFactory.createPatrickBoss();
        assertNotNull(Patrick.getComponent(AnimationRenderComponent.class),
                "Patrick does not have an AnimationRenderComponent");
    }
    @Test
    public void testPatrickAnimationController() {
        Entity Patrick = MobBossFactory.createPatrickBoss();
        assertNotNull(Patrick.getComponent(PatrickAnimationController.class),
                "Patrick does not have an Animation Controller");
    }

    @Test
    public void testIceBabyCreation() {
        Entity IceBaby = MobBossFactory.createIceBoss();
        assertNotNull(IceBaby);
    }

    @Test
    public void testIceBabyAnimationRenderComponent() {
        Entity IceBaby = MobBossFactory.createIceBoss();
        assertNotNull(IceBaby.getComponent(AnimationRenderComponent.class),
                "IceBaby does not have an AnimationRenderComponent");
    }
    @Test
    public void testIceBabyAnimationController() {
        Entity IceBaby = MobBossFactory.createIceBoss();
        assertNotNull(IceBaby.getComponent(IceBabyAnimationController.class),
                "IceBaby does not have an Animation Controller");
    }
}
