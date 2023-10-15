package com.csse3200.game.entities.factories;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.*;
import com.csse3200.game.components.bosses.DemonAnimationController;
import com.csse3200.game.components.bosses.IceBabyAnimationController;
import com.csse3200.game.components.bosses.PatrickAnimationController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
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

    private Entity baseBoss;
    private Entity demon;
    private Entity slimeyBoy;
    private Entity patrick;
    private Entity deadPatrick;
    private Entity iceBaby;
    
    private final String[] texture = {
        "images/mobboss/demon.png",
        "images/mobboss/demon2.png",
        "images/mobboss/patrick.png",
        "images/mobboss/iceBaby.png"
    };

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
            "sounds/mobBoss/mobSpawnStomp.mp3",
            "sounds/mobBoss/demonBreath.mp3",
            "sounds/mobBoss/demonSpawn.wav",
            "sounds/mobBoss/demonAttack.wav",
            "sounds/mobBoss/demonBreathIn.mp3",
            "sounds/mobBoss/demonLand.mp3",
            "sounds/mobBoss/demonJump.mp3",
            "sounds/mobBoss/demonHeal.mp3",
            "sounds/mobBoss/demonCleave.mp3",
            "sounds/mobBoss/demonDeath.mp3",
            "sounds/mobBoss/slimeySplat.mp3",
            "sounds/mobBoss/slimeJump.mp3",
            "sounds/mobBoss/slimePop.mp3",
            "sounds/mobBoss/patrickAttack.mp3",
            "sounds/mobBoss/patrickAppear.mp3",
            "sounds/mobBoss/patrickScream.mp3",
            "sounds/mobBoss/patrickSpell.mp3",
            "sounds/mobBoss/patrickSpawn.mp3",
            "sounds/mobBoss/patrickCast.mp3",
            "sounds/mobBoss/patrickThunder.mp3",
            "sounds/mobBoss/patrickHit.mp3",
            "sounds/mobBoss/spawnDemonSlime.mp3"
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
        resourceService.loadTextures(texture);
        resourceService.loadTextureAtlases(atlas);
        resourceService.loadSounds(sounds);
        resourceService.loadAll();
        ServiceLocator.getResourceService()
                .getAsset("images/mobboss/demon.atlas", TextureAtlas.class);
        baseBoss = MobBossFactory.createBaseBoss();
        demon = MobBossFactory.createDemonBoss(80);
        slimeyBoy = MobBossFactory.createSlimeyBoy(80);
        patrick = MobBossFactory.createPatrickBoss(80);
        deadPatrick = MobBossFactory.patrickDead();
        iceBaby = MobBossFactory.createIceBoss(80);
    }

    @Test
    void testCreateMobBossNotNull() {
        assertNotNull(baseBoss, "Base Boss should not be null.");
        assertNotNull(demon, "Demon Boss should not be null.");
        assertNotNull(slimeyBoy, "Slimey Boy should not be null.");
        assertNotNull(patrick, "Patrick Boss should not be null.");
        assertNotNull(deadPatrick, "Dead Patrick Boss should not be null.");
        assertNotNull(iceBaby, "Ice Baby Boss should not be null.");
    }

    @Test
    void testMobBossPhysicsComponent() {
        assertNotNull(baseBoss.getComponent(PhysicsComponent.class),
                "Base Boss does not have physics component.");
        assertNotNull(demon.getComponent(PhysicsComponent.class),
                "Demon Boss does not have physics component.");
        assertNotNull(slimeyBoy.getComponent(PhysicsComponent.class),
                "Slimey Boy does not have physics component.");
        assertNotNull(patrick.getComponent(PhysicsComponent.class),
                "Patrick Boss does not have physics component.");
        assertNotNull(deadPatrick.getComponent(PhysicsComponent.class),
                "Dead Patrick Boss does not have physics component.");
        assertNotNull(iceBaby.getComponent(PhysicsComponent.class),
                "Ice Baby Boss does not have physics component.");
    }

//    @Test
//    void testMobBossColliderComponent() {
//        assertNotNull(baseBoss.getComponent(ColliderComponent.class),
//                "Base Boss does not have collider component.");
//        assertNotNull(demon.getComponent(ColliderComponent.class),
//                "Demon Boss does not have collider component.");
//        assertNotNull(slimeyBoy.getComponent(ColliderComponent.class),
//                "Slimey Boy does not have collider component.");
//        assertNotNull(patrick.getComponent(ColliderComponent.class),
//                "Patrick Boss does not have collider component.");
//        assertNotNull(deadPatrick.getComponent(ColliderComponent.class),
//                "Dead Patrick Boss does not have collider component.");
//        assertNotNull(iceBaby.getComponent(ColliderComponent.class),
//                "Ice Baby Boss does not have collider component.");
//    }

    @Test
    void testMobBossPhysicsMovementComponent() {
        assertNotNull(baseBoss.getComponent(PhysicsMovementComponent.class),
                "Base Boss does not have physics movement component.");
        assertNotNull(demon.getComponent(PhysicsMovementComponent.class),
                "Demon Boss does not have physics movement component.");
        assertNotNull(slimeyBoy.getComponent(PhysicsMovementComponent.class),
                "Slimey Boy does not have physics movement component.");
        assertNotNull(patrick.getComponent(PhysicsMovementComponent.class),
                "Patrick Boss does not have physics movement component.");
        assertNotNull(deadPatrick.getComponent(PhysicsMovementComponent.class),
                "Dead Patrick Boss does not have physics movement component.");
        assertNotNull(iceBaby.getComponent(PhysicsMovementComponent.class),
                "Ice Baby Boss does not have physics movement component.");
    }

    @Test
    void testMobBossHitboxComponent() {
        assertNotNull(baseBoss.getComponent(HitboxComponent.class),
                "Base Boss does not have hitbox component.");
        assertNotNull(demon.getComponent(HitboxComponent.class),
                "Demon Boss does not have hitbox component.");
        assertNotNull(slimeyBoy.getComponent(HitboxComponent.class),
                "Slimey Boy does not have hitbox component.");
        assertNotNull(patrick.getComponent(HitboxComponent.class),
                "Patrick Boss does not have hitbox component.");
        assertNotNull(deadPatrick.getComponent(HitboxComponent.class),
                "Dead Patrick Boss does not have hitbox component.");
        assertNotNull(iceBaby.getComponent(HitboxComponent.class),
                "Ice Baby Boss does not have hitbox component.");
    }
    @Test
    void testMobBossTouchAttackComponent() {
        assertNotNull(baseBoss.getComponent(TouchAttackComponent.class),
                "Base Boss does not have touch attack component.");
        assertNotNull(demon.getComponent(TouchAttackComponent.class),
                "Base Boss does not have touch attack component.");
        assertNotNull(slimeyBoy.getComponent(TouchAttackComponent.class),
                "Slimey Boy does not have touch attack component.");
        assertNotNull(patrick.getComponent(TouchAttackComponent.class),
                "Patrick Boss does not have touch attack component.");
        assertNotNull(deadPatrick.getComponent(TouchAttackComponent.class),
                "Dead Patrick Boss does not have touch attack component.");
        assertNotNull(iceBaby.getComponent(TouchAttackComponent.class),
                "Ice Baby Boss does not have touch attack component.");
    }

    @Test
    void testMobBossCombatStats(){
        assertEquals(80, demon.getComponent(CombatStatsComponent.class).getHealth(),
                "Demon Boss health should be 5000.");
        assertEquals(0, demon.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "Demon Boss base attack should be 0.");
        assertEquals(80, slimeyBoy.getComponent(CombatStatsComponent.class).getHealth(),
                "Slimey Boy health should be 500.");
        assertEquals(0, slimeyBoy.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "Slimey Boy base attack should be 0.");
        assertEquals(80, patrick.getComponent(CombatStatsComponent.class).getHealth(),
                "Patrick Boss health should be 2500.");
        assertEquals(0, patrick.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "Patrick Boss base attack should be 0.");
        assertEquals(1, deadPatrick.getComponent(CombatStatsComponent.class).getHealth(),
                "Dead Patrick Boss health should be 1.");
        assertEquals(0, deadPatrick.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "Dead Patrick Boss base attack should be 0.");
        assertEquals(80, iceBaby.getComponent(CombatStatsComponent.class).getHealth(),
                "Ice Baby Boss health should be 3000.");
        assertEquals(0, iceBaby.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "Ice Baby Boss base attack should be 0.");
    }

    @Test
    void testMobBossAnimationRenderComponent() {
        assertNotNull(demon.getComponent(AnimationRenderComponent.class),
                "Demon Boss does not have an animation render component.");
        assertNotNull(slimeyBoy.getComponent(AnimationRenderComponent.class),
                "Slimey Boy does not have an animation render component.");
        assertNotNull(patrick.getComponent(AnimationRenderComponent.class),
                "Patrick Boss does not have an animation render component.");
        assertNotNull(deadPatrick.getComponent(AnimationRenderComponent.class),
                "Dead Patrick Boss does not have an animation render component.");
        assertNotNull(iceBaby.getComponent(AnimationRenderComponent.class),
                "Ice Baby does not have an animation render component.");
    }
    
    @Test
    void testMobBossAnimationController() {
        assertNotNull(demon.getComponent(DemonAnimationController.class),
                "Demon Boss does not have an animation controller.");
        assertNotNull(slimeyBoy.getComponent(DemonAnimationController.class),
                "Slimey Boy does not have an animation controller.");
        assertNotNull(patrick.getComponent(PatrickAnimationController.class),
                "Patrick Boss does not have an animation controller.");
        assertNotNull(deadPatrick.getComponent(PatrickAnimationController.class),
                "Dead Patrick Boss does not have an animation controller.");
        assertNotNull(iceBaby.getComponent(IceBabyAnimationController.class),
                "Ice Baby Boss does not have an animation controller.");
    }
}
