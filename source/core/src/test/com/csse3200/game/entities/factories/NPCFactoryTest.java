package com.csse3200.game.entities.factories;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.npc.DeflectingComponent;
import com.csse3200.game.components.npc.DodgingComponent;
import com.csse3200.game.components.npc.DragonKnightAnimationController;
import com.csse3200.game.components.npc.FireWormAnimationController;
import com.csse3200.game.components.npc.SkeletonAnimationController;
import com.csse3200.game.components.npc.SplitMoblings;
import com.csse3200.game.components.npc.WaterQueenAnimationController;
import com.csse3200.game.components.npc.WaterSlimeAnimationController;
import com.csse3200.game.components.npc.WizardAnimationController;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class NPCFactoryTest {

    private Entity rangedBaseNpc;
    private Entity meleeBaseNpc;

    private Entity waterSlime;
    private Entity waterQueen;
    private Entity fireWorm;
    private Entity dragonKnight;
    private Entity wizard;
    private Entity skeleton;

    private Entity splitWaterSlime;
    private Entity deflectWizard;
    private Entity dodgingDragonKnight;

    private Entity towerTarget;
    private Entity engineerTarget;
    private Entity playerTarget;

    private final String[] texture = {
            "images/towers/turret_deployed.png",
            "images/towers/turret01.png",
            "images/towers/wall_tower.png"
    };
    private final String[] atlas = {
            "images/towers/turret01.atlas",
        //     "images/mobs/rangedBaseNpc.atlas",
            "images/mobs/water_queen.atlas",
            "images/mobs/water_slime.atlas",
            "images/mobs/dragon_knight.atlas",
            "images/mobs/skeleton.atlas",
            "images/mobs/wizard.atlas",
            "images/mobs/fire_worm.atlas"
        };
    private static final String[] sounds = {
            "sounds/towers/gun_shot_trimmed.mp3",
            "sounds/towers/deploy.mp3",
            "sounds/towers/stow.mp3",
            "sounds/mobs/waterQueenSpell.mp3",
            "sounds/mobs/boneBreak.mp3",
            "sounds/mobs/fireWormRoar.mp3",
            "sounds/mobs/wizardSpell.mp3",
            "sounds/mobs/archerArrow.mp3",
            "sounds/mobs/coatAttack.mp3",
            "sounds/mobs/skeletonHit.mp3"
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
        
        
        //playerTarget = PlayerFactory.createPlayer();
        //towerTarget = TowerFactory.createBaseTower();
        //engineerTarget = EngineerFactory.createEngineer();
        // xenoGrunt = NPCFactory.createXenoGrunt();
        rangedBaseNpc = NPCFactory.createRangedBaseNPC();
        meleeBaseNpc = NPCFactory.createMeleeBaseNPC();

        waterSlime = NPCFactory.createBaseWaterSlime(60);
        waterQueen = NPCFactory.createWaterQueen(60);
        dragonKnight = NPCFactory.createDragonKnight(60);
        fireWorm = NPCFactory.createFireWorm(60);
        skeleton = NPCFactory.createSkeleton(60);
        wizard = NPCFactory.createWizard(60);
        
        splitWaterSlime = NPCFactory.createSplittingWaterSlime(60);
        deflectWizard = NPCFactory.createDeflectWizard(60);
        dodgingDragonKnight = NPCFactory.createDodgingDragonKnight(60);
    }

    @Test
    void testCreateRangedBaseNpcNotNull() {
        assertNotNull(rangedBaseNpc, "base Ranged NPC should not be null");
    }

    @Test
    void testCreateRangedBaseNpcHasColliderComponent() {
        assertNotNull(rangedBaseNpc.getComponent(ColliderComponent.class),
                "Fire Worm should have ColliderComponent");
    }

    @Test
    void testCreateRangedBaseNpcHasHitboxComponent() {
        assertNotNull(rangedBaseNpc.getComponent(HitboxComponent.class),
                "Fire Worm should have HitboxComponent");
    }

    @Test
    void testCreateRangedBaseNpcHasPhysicsComponent() {
        assertNotNull(rangedBaseNpc.getComponent(PhysicsComponent.class),
                "Fire Worm should have PhysicsComponent");
    }

    @Test
    void testCreateRangedBaseNpcHasPhysicsMovementComponent() {
        assertNotNull(rangedBaseNpc.getComponent(PhysicsMovementComponent.class),
                "Fire Worm should have PhysicsMovementComponent");
    }

    @Test
    void testCreateRangedBaseNpcHasAIComponent() {
        assertNotNull(rangedBaseNpc.getComponent(AITaskComponent.class),
                "Fire Worm should have PhysicsMovementComponent");
    }
    @Test
    void testCreateMeleeBaseNpcNotNull() {
        assertNotNull(meleeBaseNpc, "base Ranged NPC should not be null");
    }

    @Test
    void testCreateMeleeBaseNpcHasColliderComponent() {
        assertNotNull(meleeBaseNpc.getComponent(ColliderComponent.class),
                "Fire Worm should have ColliderComponent");
    }

    @Test
    void testCreateMeleeBaseNpcHasHitboxComponent() {
        assertNotNull(meleeBaseNpc.getComponent(HitboxComponent.class),
                "Fire Worm should have HitboxComponent");
    }

    @Test
    void testCreateMeleeBaseNpcHasPhysicsComponent() {
        assertNotNull(meleeBaseNpc.getComponent(PhysicsComponent.class),
                "Fire Worm should have PhysicsComponent");
    }

    @Test
    void testCreateMeleeBaseNpcHasPhysicsMovementComponent() {
        assertNotNull(meleeBaseNpc.getComponent(PhysicsMovementComponent.class),
                "Fire Worm should have PhysicsMovementComponent");
    }

    @Test
    void testCreateMeleeBaseNpcHasAIComponent() {
        assertNotNull(rangedBaseNpc.getComponent(AITaskComponent.class),
                "Fire Worm should have PhysicsMovementComponent");
    }

    @Test
    void testCreateWaterSlime() {
        assertNotNull(waterSlime, "Water Slime should not be null");
    }

    @Test
    void testWaterSlimeCombatStatsComponent() {
        assertEquals(60, waterSlime.getComponent(CombatStatsComponent.class).getHealth(),
                "Health should be 100");
        assertEquals(0, waterSlime.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "BaseAttack should be 0");
    }

    @Test
    void waterSlimeHasAnimationComponent() {
        assertNotNull(waterSlime.getComponent(AnimationRenderComponent.class),
                "Water Slime should have AnimationRenderComponent");
    }

    @Test
    void testCreateWaterSlimeHasAnimationController() {
        assertNotNull(waterSlime.getComponent(WaterSlimeAnimationController.class),
                "Water Slime should have an Animation Controller");
    }

    @Test
    void testSplitWaterSlime() {
        assertNotNull(splitWaterSlime, "Water Slime should not be Null");
    }

    @Test
    void testSplitWaterSlimeHasSplittingComponent() {
        Entity splitWaterSlime = NPCFactory.createSplittingWaterSlime(60);
        assertNotNull(splitWaterSlime.getComponent(SplitMoblings.class), 
                "Split water slimes should have a splitting component");
    }

    @Test
    void testCreateWaterQueenNotNull() {
        assertNotNull(waterQueen, "Water Queen should not be null");
    }

    @Test
    void testWaterQueenCombatStatsComponent() {
        assertEquals(60, waterQueen.getComponent(CombatStatsComponent.class).getHealth(),
                "Health should be 100");
        assertEquals(0, waterQueen.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "BaseAttack should be 0");
    }

    @Test
    void waterQueenHasAnimationComponent() {
        assertNotNull(waterQueen.getComponent(AnimationRenderComponent.class),
                "Water Queen should have AnimationRenderComponent");
    }

    @Test
    void testCreateWaterQueenHasAnimationController() {
        assertNotNull(waterQueen.getComponent(WaterQueenAnimationController.class),
                "Water Queen should have an Animation controller");
    }

    @Test
    void testCreateFireWormNotNull() {
        assertNotNull(fireWorm, "Fire Worm should not be null");
    }

    @Test
    void testFireWormCombatStatsComponent() {
        assertEquals(60, fireWorm.getComponent(CombatStatsComponent.class).getHealth(),
                "Health should be 100");
        assertEquals(0, fireWorm.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "BaseAttack should be 0");
    }

    @Test
    void fireWormHasAnimationComponent() {
        assertNotNull(fireWorm.getComponent(AnimationRenderComponent.class),
                "Fire Worm should have AnimationRenderComponent");
    }

    @Test
    void fireWormHasAnimationController() {
        assertNotNull(fireWorm.getComponent(FireWormAnimationController.class),
                "Fire Worm should have AnimationRenderComponent");
    }
    @Test
    void testCreateDragonKnightNotNull() {
        assertNotNull(dragonKnight, "Dragon Knight should not be null");
    }

    @Test
    void testDragonKnightCombatStatsComponent() {
        assertEquals(60, dragonKnight.getComponent(CombatStatsComponent.class).getHealth(),
                "Health should be 100");
        assertEquals(0, dragonKnight.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "BaseAttack should be 0");
    }

    @Test
    void dragonKnightHasAnimationComponent() {
        assertNotNull(dragonKnight.getComponent(AnimationRenderComponent.class),
                "Dragon Knight should have AnimationRenderComponent");
    }

    @Test
    void dragonKnightHasAnimationController() {
        assertNotNull(dragonKnight.getComponent(DragonKnightAnimationController.class),
                "Dragon Knight should have Animation Controller");
    }

    @Test
    void dodgingDragonKnightHasDodgingComponent() {
        assertNotNull(dodgingDragonKnight.getComponent(DodgingComponent.class),
                "Dragon Knight should have AnimationRenderComponent");
    }

    @Test
    void testCreateWizardNotNull() {
        assertNotNull(wizard, "Wizard should not be null");
    }

    @Test
    void testWizardCombatStatsComponent() {
        assertEquals(60, wizard.getComponent(CombatStatsComponent.class).getHealth(),
                "Health should be 100");
        assertEquals(0, wizard.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "BaseAttack should be 0");
    }

    @Test
    void wizardHasAnimationComponent() {
        assertNotNull(wizard.getComponent(AnimationRenderComponent.class),
                "Wizard should have AnimationRenderComponent");
    }

    @Test
    void wizardHasAnimationController() {
        assertNotNull(wizard.getComponent(WizardAnimationController.class),
                "Wizard should have Animation Controller");
    }

    @Test
    void dodgingWizardHasDeflectingComponent() {
        assertNotNull(deflectWizard.getComponent(DeflectingComponent.class),
                "Deflecting Wizard should have Deflecting component");
    }

    @Test
    void testCreateSkeletonNotNull() {
        assertNotNull(skeleton, "skeleton should not be null");
    }

    @Test
    void testSkeletonCombatStatsComponent() {
        assertEquals(60, skeleton.getComponent(CombatStatsComponent.class).getHealth(),
                "Health should be 100");
        assertEquals(0, skeleton.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "BaseAttack should be 0");
    }

    @Test
    void skeletonHasAnimationComponent() {
        assertNotNull(skeleton.getComponent(AnimationRenderComponent.class),
                "skeleton should have AnimationRenderComponent");
    }

    @Test
    void skeletonHasAnimationController() {
        assertNotNull(skeleton.getComponent(SkeletonAnimationController.class),
                "skeleton should have an Animation Controller");
    }


}
