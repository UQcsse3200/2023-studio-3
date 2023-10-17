package com.csse3200.game.entities.factories;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.CostComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
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
class TowerFactoryTest {
    private Entity baseTower;
    private Entity weaponTower;
    private Entity wallTower;
    private Entity stunTower;
    private Entity fireTower;
    private Entity tntTower;
    private Entity droidTower;
    private String[] texture = {
            "images/towers/turret_deployed.png",
            "images/towers/turret01.png",
            "images/towers/wall_tower.png",
            "images/towers/fire_tower_atlas.png",
            "images/towers/stun_tower.png",
            "images/towers/DroidTower.png",
            "images/towers/TNTTower.png"
    };
    private String[] atlas = {
            "images/towers/turret01.atlas",
            "images/towers/stun_tower.atlas",
            "images/towers/fire_tower_atlas.atlas",
            "images/towers/DroidTower.atlas",
            "images/towers/TNTTower.atlas",
            "images/towers/barrier.atlas"
    };
    private static final String[] sounds = {
            "sounds/towers/gun_shot_trimmed.mp3",
            "sounds/towers/deploy.mp3",
            "sounds/towers/stow.mp3",
            "sounds/towers/Desert-Eagle-Far-Single-Gunshot.mp3",
            "sounds/towers/5.56_single_shot.mp3",
            "sounds/towers/explosion.mp3",
            "sounds/towers/eco_tower_ping.mp3",
            "sounds/towers/ar15_single_shot_far.mp3"

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
                .getAsset("images/towers/turret01.atlas", TextureAtlas.class);
        baseTower = TowerFactory.createBaseTower();
        weaponTower = TowerFactory.createWeaponTower();
        wallTower = TowerFactory.createWallTower();
        fireTower = TowerFactory.createFireTower();
        stunTower = TowerFactory.createStunTower();
        tntTower = TowerFactory.createTNTTower();
        droidTower = TowerFactory.createDroidTower();
    }

    @Test
    void testCreateBaseTowerNotNull() {
        assertNotNull(baseTower, "Base tower should not be null");
        assertNotNull(weaponTower, "Weaponry tower should not be null");
        assertNotNull(wallTower, "Wall tower should not be null");
        assertNotNull(stunTower, "Stun tower must not be null");
        assertNotNull(fireTower, "Fire tower must not be null");
        assertNotNull(tntTower, "TNT tower must not be null");
        assertNotNull(droidTower, "Droid tower must not be null");
    }

    @Test
    void testCreateBaseTowerHasColliderComponent() {
        assertNotNull(baseTower.getComponent(ColliderComponent.class),
                "Base tower should have ColliderComponent");
        assertNotNull(weaponTower.getComponent(ColliderComponent.class),
                "Weaponry tower should have ColliderComponent");
        assertNotNull(wallTower.getComponent(ColliderComponent.class),
                "Wall tower should have ColliderComponent");
        assertNotNull(stunTower.getComponent(ColliderComponent.class),
                "Stun Tower should have ColliderComponent");
        assertNotNull(fireTower.getComponent(ColliderComponent.class),
                "Fire tower should have ColliderComponent");
        assertNotNull(tntTower.getComponent(ColliderComponent.class),
                "TNT tower should have ColliderComponent");
        assertNotNull(droidTower.getComponent(ColliderComponent.class),
                "Droid tower should have ColliderComponent");
    }

    @Test
    void testCreateBaseTowerHasHitboxComponent() {
        assertNotNull(baseTower.getComponent(HitboxComponent.class),
                "Base tower should have HitboxComponent");
        assertNotNull(weaponTower.getComponent(HitboxComponent.class),
                "Weaponry tower should have HitboxComponent");
        assertNotNull(wallTower.getComponent(HitboxComponent.class),
                "Wall tower should have HitboxComponent");
        assertNotNull(stunTower.getComponent(HitboxComponent.class),
                "Stun tower should have HitboxComponent");
        assertNotNull(fireTower.getComponent(HitboxComponent.class),
                "Fire tower should have HitboxComponent");
        assertNotNull(tntTower.getComponent(HitboxComponent.class),
                "TNT tower should have HitboxComponent");
        assertNotNull(droidTower.getComponent(HitboxComponent.class),
                "Droid tower should have HitboxComponent");
    }

    @Test
    void testCreateBaseTowerHasPhysicsComponent() {
        assertNotNull(baseTower.getComponent(PhysicsComponent.class),
                "Base tower should have PhysicsComponent");
        assertNotNull(weaponTower.getComponent(PhysicsComponent.class),
                "Weaponry tower should have PhysicsComponent");
        assertNotNull(wallTower.getComponent(PhysicsComponent.class),
                "Wall tower should have PhysicsComponent");
        assertNotNull(stunTower.getComponent(PhysicsComponent.class),
                "Stun tower should have PhysicsComponent");
        assertNotNull(fireTower.getComponent(PhysicsComponent.class),
                "Fire tower should have PhysicsComponent");
        assertNotNull(tntTower.getComponent(PhysicsComponent.class),
                "TNT tower should have PhysicsComponent");
        assertNotNull(droidTower.getComponent(PhysicsComponent.class),
                "Droid tower should have PhysicsComponent");
    }

    @Test
    void testCreateBaseTowerPhysicsComponentStaticBody() {
        PhysicsComponent physicsComponent = baseTower.getComponent(PhysicsComponent.class);
        PhysicsComponent physicsComponent1 = weaponTower.getComponent(PhysicsComponent.class);
        PhysicsComponent physicsComponent2 = wallTower.getComponent(PhysicsComponent.class);
        PhysicsComponent physicsComponent3 = stunTower.getComponent(PhysicsComponent.class);
        PhysicsComponent physicsComponent4 = fireTower.getComponent(PhysicsComponent.class);
        PhysicsComponent physicsComponent5 = tntTower.getComponent(PhysicsComponent.class);
        PhysicsComponent physicsComponent6 = droidTower.getComponent(PhysicsComponent.class);

        assertSame(BodyType.StaticBody, physicsComponent.getBody().getType(),
                "PhysicsComponent should be of type StaticBody");
        assertSame(BodyType.StaticBody, physicsComponent1.getBody().getType(),
                "PhysicsComponent1 should be of type StaticBody");
        assertSame(BodyType.StaticBody, physicsComponent2.getBody().getType(),
                "PhysicsComponent2 should be of type StaticBody");
        assertSame(BodyType.StaticBody, physicsComponent3.getBody().getType(),
                "StunTower's PhysicsComponent should be of type StaticBody");
        assertSame(BodyType.StaticBody, physicsComponent4.getBody().getType(),
                "FireTower's PhysicsComponent should be of type StaticBody");
        assertSame(BodyType.StaticBody, physicsComponent5.getBody().getType(),
                "TNT tower's PhysicsComponent should be of type StaticBody");
        assertSame(BodyType.StaticBody, physicsComponent6.getBody().getType(),
                "Droid Tower's PhysicsComponent should be of type StaticBody");
    }

    @Test
    void testWeaponTowerCombatStatsComponentAndCostComponent() {
        assertEquals(75, weaponTower.getComponent(CombatStatsComponent.class).getHealth(),
                "Health should be 75");
        assertEquals(15, weaponTower.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "BaseAttack should be 15");
        assertEquals(50, weaponTower.getComponent(CostComponent.class).getCost(),
                "Cost should be 50");
        assertEquals(100, fireTower.getComponent(CombatStatsComponent.class).getHealth(),
                "Fire Tower health must be 100");
        assertEquals(25, fireTower.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "Fire Tower base attack must be 25");
        assertEquals(300, fireTower.getComponent(CostComponent.class).getCost(),
                "Fire Tower cost must 300");
        assertEquals(100, stunTower.getComponent(CombatStatsComponent.class).getHealth(),
                "Stun Tower health must be 100");
        assertEquals(25, stunTower.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "Stun Tower base attack must be 25");
        assertEquals(500, stunTower.getComponent(CostComponent.class).getCost(),
                "Stun Tower cost must 500");
        assertEquals(10, tntTower.getComponent(CombatStatsComponent.class).getHealth(),
                "TNT Tower health must be 10");
        assertEquals(5, tntTower.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "TNT Tower base attack must be 5");
        assertEquals(50, tntTower.getComponent(CostComponent.class).getCost(),
                "TNT Tower cost must 50");
        assertEquals(100, droidTower.getComponent(CombatStatsComponent.class).getHealth(),
                "Droid Tower health must be 100");
        assertEquals(25, droidTower.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "Droid Tower base attack must be 25");
        assertEquals(300, droidTower.getComponent(CostComponent.class).getCost(),
                "Droid Tower cost must 300");
    }

    @Test
    void testWallTowerCombatStatsComponentAndCostComponent() {
        assertEquals(300, wallTower.getComponent(CombatStatsComponent.class).getHealth(), "Health should be 300");
        assertEquals(0, wallTower.getComponent(CombatStatsComponent.class).getBaseAttack(), "BaseAttack should be 0");
        assertEquals(200, wallTower.getComponent(CostComponent.class).getCost(), "Cost should be 200");
    }

    @Test
    void weaponTowerHasAnimationComponent() {
        assertNotNull(weaponTower.getComponent(AnimationRenderComponent.class));
        assertNotNull(stunTower.getComponent(AnimationRenderComponent.class));
        assertNotNull(fireTower.getComponent(AnimationRenderComponent.class));
        assertNotNull(tntTower.getComponent(AnimationRenderComponent.class));
        assertNotNull(droidTower.getComponent(AnimationRenderComponent.class));
    }

    @Test
    void testAttackerCollisionWithWall() {
        Entity attacker = createAttacker(wallTower.getComponent(HitboxComponent.class).getLayer());

        wallTower.setPosition(10f,10f);
        attacker.setPosition(10f,10f);
        wallTower.create();

        assertEquals(300, wallTower.getComponent(CombatStatsComponent.class).getHealth());

        ServiceLocator.getPhysicsService().getPhysics().update();

        assertEquals(290, wallTower.getComponent(CombatStatsComponent.class).getHealth());
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

