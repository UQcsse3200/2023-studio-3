package com.csse3200.game.entities.factories;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
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
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import java.security.Provider;
import java.util.Arrays;

@ExtendWith(GameExtension.class)
public class TowerFactoryTest {

    private Entity baseTower;
    private Entity weaponTower;
    private Entity wallTower;
    private Entity stunTower;
    private Entity fireTower;
    private String[] texture = {
            "images/towers/turret_deployed.png",
            "images/towers/turret01.png",
            "images/towers/wallTower.png",
            "images/towers/fire_tower_atlas.png",
            "images/towers/stun_tower.png"
    };
    private String[] atlas = {
            "images/towers/turret01.atlas",
            "images/towers/stun_tower.atlas",
            "images/towers/fire_tower_atlas.atlas"
    };
    private static final String[] sounds = {
            "sounds/towers/gun_shot_trimmed.mp3",
            "sounds/towers/deploy.mp3",
            "sounds/towers/stow.mp3"
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
        stunTower = TowerFactory.createFireTower();
    }

    @Test
    public void testCreateBaseTowerNotNull() {

        assertNotNull(baseTower, "Base tower should not be null");
        assertNotNull(weaponTower, "Weaponry tower should not be null");
        assertNotNull(wallTower, "Wall tower should not be null");
        assertNotNull(stunTower, "Stun tower must not be null");
        assertNotNull(fireTower, "Stun tower must not be null");
    }

    @Test
    public void testCreateBaseTowerHasColliderComponent() {
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
    }

    @Test
    public void testCreateBaseTowerHasHitboxComponent() {
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
    }

    @Test
    public void testCreateBaseTowerHasPhysicsComponent() {
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
    }

    @Test
    public void testCreateBaseTowerPhysicsComponentStaticBody() {
        PhysicsComponent physicsComponent = baseTower.getComponent(PhysicsComponent.class);
        PhysicsComponent physicsComponent1 = weaponTower.getComponent(PhysicsComponent.class);
        PhysicsComponent physicsComponent2 = wallTower.getComponent(PhysicsComponent.class);
        PhysicsComponent physicsComponent3 = stunTower.getComponent(PhysicsComponent.class);
        PhysicsComponent physicsComponent4 = fireTower.getComponent(PhysicsComponent.class);

        assertTrue(physicsComponent.getBody().getType() == BodyType.StaticBody,
                "PhysicsComponent should be of type StaticBody");
        assertTrue(physicsComponent1.getBody().getType() == BodyType.StaticBody,
                "PhysicsComponent1 should be of type StaticBody");
        assertTrue(physicsComponent2.getBody().getType() == BodyType.StaticBody,
                "PhysicsComponent2 should be of type StaticBody");
        assertTrue(physicsComponent3.getBody().getType() == BodyType.StaticBody,
                "StunTower's PhysicsComponent should be of type StaticBody");
        assertTrue(physicsComponent4.getBody().getType() == BodyType.StaticBody,
                "FireTower's PhysicsComponent should be of type StaticBody");
    }

    @Test
    public void testWeaponTowerCombatStatsComponentAndCostComponent() {

        assertEquals(10, weaponTower.getComponent(CombatStatsComponent.class).getHealth(),
                "Health should be 10");
        assertEquals(10, weaponTower.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "BaseAttack should be 10");
        assertEquals(10, weaponTower.getComponent(CostComponent.class).getCost(),
                "Cost should be 10");
        assertEquals(10, fireTower.getComponent(CombatStatsComponent.class).getHealth(),
                "Fire Tower health must be 10");
        assertEquals(10, fireTower.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "Fire Tower base attack must be 10");
        assertEquals(10, fireTower.getComponent(CostComponent.class).getCost(),
                "Fire Tower cost must 10");
        assertEquals(10, stunTower.getComponent(CombatStatsComponent.class).getHealth(),
                "Stun Tower health must be 10");
        assertEquals(5, stunTower.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "Stun Tower base attack must be 10");
        assertEquals(10, fireTower.getComponent(CostComponent.class).getCost(),
                "Stun Tower cost must 10");
    }

    @Test
    public void testWallTowerCombatStatsComponentAndCostComponent() {

        assertTrue(wallTower.getComponent(CombatStatsComponent.class).getHealth() == 20,
                "Health should be 20");
        assertTrue(wallTower.getComponent(CombatStatsComponent.class).getBaseAttack() == 0,
                "BaseAttack should be 0");
        assertTrue(wallTower.getComponent(CostComponent.class).getCost() == 5,
                "Cost should be 5");

    }

    @Test
    public void weaponTowerHasAnimationComponent() {
        assertNotNull(weaponTower.getComponent(AnimationRenderComponent.class));
        assertNotNull(stunTower.getComponent(AnimationRenderComponent.class));
        assertNotNull(fireTower.getComponent(AnimationRenderComponent.class));
    }

    @Test
    public void testAttackerCollisionWithWall() {
        Entity attacker = createAttacker(wallTower.getComponent(HitboxComponent.class).getLayer());

        wallTower.setPosition(10f,10f);
        attacker.setPosition(10f,10f);
        wallTower.create();

        assertEquals(20, wallTower.getComponent(CombatStatsComponent.class).getHealth());

        ServiceLocator.getPhysicsService().getPhysics().update();

        assertEquals(10, wallTower.getComponent(CombatStatsComponent.class).getHealth());

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

