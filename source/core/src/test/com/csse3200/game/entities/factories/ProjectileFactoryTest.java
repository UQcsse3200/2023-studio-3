package com.csse3200.game.entities.factories;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.components.*;
import com.csse3200.game.components.projectile.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsLayer;
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
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import java.security.Provider;
import java.util.Arrays;

@ExtendWith(GameExtension.class)
class ProjectileFactoryTest {

    private final String[] atlas = {
            "images/projectiles/mobProjectile.atlas",
            "images/projectiles/basic_projectile.atlas",
            "images/projectiles/mobKing_projectile.atlas",
            "images/projectiles/engineer_projectile.atlas",
            "images/projectiles/stun_effect.atlas",
            "images/projectiles/burn_effect.atlas",
            "images/projectiles/snow_ball.atlas"
    };

    private final String[] animations = {
            "rotate",
            "projectile",
            "projectileFinal",
            "mob_boss",
            "mob_bossFinal"
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
        resourceService.loadAll();      
    }
    
    @Test
    public void createBaseProjectile() {
        Entity projectile = ProjectileFactory.createBaseProjectile(PhysicsLayer.NPC, new Vector2(0.1f, 0.1f), new Vector2(1f, 1f));
        assertNotNull(projectile);
    }

    @Test
    public void testBaseProjectileColliderComponent() {
        Entity projectile = ProjectileFactory.createBaseProjectile(PhysicsLayer.NPC, new Vector2(0.1f, 0.1f), new Vector2(1f, 1f));
        assertNotNull(projectile.getComponent(ColliderComponent.class), 
                "Projectile does not have a ColliderComponent");
    }

    @Test
    public void testBaseProjectileTouchAttackComponent() {
        Entity projectile = ProjectileFactory.createBaseProjectile(PhysicsLayer.NPC, new Vector2(0.1f, 0.1f), new Vector2(1f, 1f));
        assertNotNull(projectile.getComponent(TouchAttackComponent.class), 
                "Projectile does not have a TouchAttackComponent");
    }

    @Test
    public void testBaseProjectileDeleteOnMapEdgeComponent() {
        Entity projectile = ProjectileFactory.createBaseProjectile(PhysicsLayer.NPC, new Vector2(0.1f, 0.1f), new Vector2(1f, 1f));
        assertNotNull(projectile.getComponent(DeleteOnMapEdgeComponent.class), 
                "Projectile does not have a DeleteOnMapEdgeComponent");
    }

    @Test
    public void testBaseProjectileSpeed() {
        Vector2 testSpeed = new Vector2(1f, 1f);
        Entity projectile = ProjectileFactory.createBaseProjectile(PhysicsLayer.NPC, new Vector2(0.1f, 0.1f), testSpeed);
        assertEquals(testSpeed, projectile.getComponent(PhysicsMovementComponent.class).getSpeed(), 
                "Projectile speed does not match testSpeed");
    }
    
    @Test
    public void testBaseProjectileHitbox() {
        Entity projectile = ProjectileFactory.createBaseProjectile(PhysicsLayer.NPC, new Vector2(0.1f, 0.1f), new Vector2(1f, 1f));
        assertNotNull(projectile.getComponent(HitboxComponent.class),
                "Projectile does not contain Hotbox component");
    }

    @Test
    public void testBaseProjectilePhysics() {
        Entity projectile = ProjectileFactory.createBaseProjectile(PhysicsLayer.NPC, new Vector2(0.1f, 0.1f), new Vector2(1f, 1f));
        assertNotNull(projectile.getComponent(PhysicsComponent.class),
        "Projectile does not have Physics component");
    }
    
    @Test
    public void testBaseProjectilePhysicsMovement() {
        Entity projectile = ProjectileFactory.createBaseProjectile(PhysicsLayer.NPC, new Vector2(0.1f, 0.1f), new Vector2(1f, 1f));
        assertNotNull(projectile.getComponent(PhysicsMovementComponent.class),
                "Projectile does not have PhysicsMovement component");
    }

    @Test
    public void testFireBallProjectileCreation() {
        Entity fireBall = ProjectileFactory.createFireBall(PhysicsLayer.NPC, new Vector2(0.1f, 0.1f), new Vector2(1f, 1f));
        assertNotNull(fireBall);
    }   

    @Test
    public void testFireBallAnimationRenderComponent() {
        Entity fireBall = ProjectileFactory.createFireBall(PhysicsLayer.NPC, new Vector2(0.1f, 0.1f), new Vector2(1f, 1f));
        assertNotNull(fireBall.getComponent(AnimationRenderComponent.class),
                "Fire Ball does not have an AnimationRenderComponent");
    }   
    @Test
    public void testFireBallAnimationController() {
        Entity fireBall = ProjectileFactory.createFireBall(PhysicsLayer.NPC, new Vector2(0.1f, 0.1f), new Vector2(1f, 1f));
        assertNotNull(fireBall.getComponent(ProjectileAnimationController.class), 
                "Fire Ball does not have an Animation Controller");
    }   

    @Test
    public void createMobBallProjectile() {
        Entity mobBallProjectile = ProjectileFactory.createMobBall(PhysicsLayer.HUMANS, new Vector2(0.1f, 0.1f), new Vector2(1f, 1f));
        assertNotNull(mobBallProjectile, "Mob King Ball is Null");
    }
    
    @Test
    public void testMobBallProjectileAnimationRenderComponent() {
        Entity mobBallProjectile = ProjectileFactory.createMobBall(PhysicsLayer.HUMANS, new Vector2(0.1f, 0.1f), new Vector2(1f, 1f));
        assertNotNull(mobBallProjectile.getComponent(AnimationRenderComponent.class), 
                "Mob Ball Projectile does not have an AnimationRenderComponent");
    }

    @Test
    public void testMobBallProjectileAnimationController() {
        Entity mobBallProjectile = ProjectileFactory.createMobBall(PhysicsLayer.HUMANS, new Vector2(0.1f, 0.1f), new Vector2(1f, 1f));
        assertNotNull(mobBallProjectile.getComponent(MobProjectileAnimationController.class), 
                "Mob Ball Projectile does not have an AnimationController");
    }

    @Test
    public void testMobKingBallCreation() {
        Entity mobKingBall = ProjectileFactory.createMobKingBall(PhysicsLayer.TOWER, new Vector2(0.1f, 0.1f), new Vector2(1f, 1f));
        assertNotNull(mobKingBall, "Mob King Ball is null");
    }
    
    @Test
    public void testMobKingBallAnimationRenderComponent() {
        Entity mobKingBall = ProjectileFactory.createMobKingBall(PhysicsLayer.TOWER, new Vector2(0.1f, 0.1f), new Vector2(1f, 1f));
        assertNotNull(mobKingBall.getComponent(AnimationRenderComponent.class),
                "Mob King Ball does not have AnimationRenderComponent");
    }

    @Test
    public void testMobKingBallAnimationController() {
        Entity mobKingBall = ProjectileFactory.createMobKingBall(PhysicsLayer.TOWER, new Vector2(0.1f, 0.1f), new Vector2(1f, 1f));
        assertNotNull(mobKingBall.getComponent(MobKingProjectAnimController.class),
                "Mob King Ball does not have Animation Controller");
    }

    @Test
    public void testEngineerBulletCreation() {
        Entity engineerBullet = ProjectileFactory.createEngineerBullet(PhysicsLayer.NPC, new Vector2(0.1f, 0.1f), new Vector2(1f, 1f));
        assertNotNull(engineerBullet, "engineerBullet is null");
    }
    
    @Test
    public void testEngineerBulletAnimationRenderComponent() {
        Entity engineerBulllet = ProjectileFactory.createEngineerBullet(PhysicsLayer.NPC, new Vector2(0.1f, 0.1f), new Vector2(1f, 1f));
        assertNotNull(engineerBulllet.getComponent(AnimationRenderComponent.class),
                "Engineer Bullet does not have AnimationRenderComponent");
    }

    @Test
    public void testEngineerAnimationController() {
        Entity engineerBullet = ProjectileFactory.createEngineerBullet(PhysicsLayer.NPC, new Vector2(0.1f, 0.1f), new Vector2(1f, 1f));
        assertNotNull(engineerBullet.getComponent(EngineerBulletsAnimationController.class),
                "Engineer Bullet does not have Animation Controller");
    }

    @Test
    public void testStunProjectileCreation() {
        Entity stunProjectile = ProjectileFactory.createEffectProjectile(PhysicsLayer.NPC, new Vector2(0.1f,
                0.1f), new Vector2(2,2), ProjectileEffects.STUN, false);
        assertNotNull(stunProjectile, "stunProjectile is null");
    }

    @Test
    public void testStunProjectileAnimationRenderComponent() {
        Entity stunProjectile = ProjectileFactory.createEffectProjectile(PhysicsLayer.NPC, new Vector2(0.1f,01f),
                new Vector2(2,2), ProjectileEffects.STUN, false);
        assertNotNull(stunProjectile.getComponent(AnimationRenderComponent.class),
                "Stun Projectile does not have AnimationRenderComponent");
    }

    @Test
    public void testStunProjectileAnimationController() {
        Entity stunProjectile = ProjectileFactory.createEffectProjectile(PhysicsLayer.TOWER, new Vector2(0.1f, 0.1f)
                , new Vector2(2,2), ProjectileEffects.STUN, false);
        assertNotNull(stunProjectile.getComponent(StunEffectProjectileAnimationController.class),
                "Stun Projectile does not have Animation Controller");
    }

    @Test
    public void testBurnProjectileCreation() {
        Entity burnProjectile = ProjectileFactory.createEffectProjectile(PhysicsLayer.NPC, new Vector2(0.1f,
                0.1f), new Vector2(2,2), ProjectileEffects.BURN, false);
        assertNotNull(burnProjectile, "burnProjectile is null");
    }

    @Test
    public void testBurnProjectileAnimationRenderComponent() {
        Entity burnProjectile = ProjectileFactory.createEffectProjectile(PhysicsLayer.NPC, new Vector2(0.1f,01f),
                new Vector2(2,2), ProjectileEffects.BURN, false);
        assertNotNull(burnProjectile.getComponent(AnimationRenderComponent.class),
                "Burn Projectile does not have AnimationRenderComponent");
    }
    @Test
    public void testBurnProjectileAnimationController() {
        Entity burnProjectile = ProjectileFactory.createEffectProjectile(PhysicsLayer.TOWER, new Vector2(0.1f, 0.1f)
                , new Vector2(2,2), ProjectileEffects.BURN, false);
        assertNotNull(burnProjectile.getComponent(BurnEffectProjectileAnimationController.class),
                "Burn Projectile does not have Animation Controller");
    }

    @Test
    public void testSlowProjectileCreation() {
        Entity slowProjectile = ProjectileFactory.createEffectProjectile(PhysicsLayer.NPC, new Vector2(0.1f,
                0.1f), new Vector2(2,2), ProjectileEffects.SLOW, false);
        assertNotNull(slowProjectile, "slowProjectile is null");
    }

    @Test
    public void testSlowProjectileAnimationRenderComponent() {
        Entity slowProjectile = ProjectileFactory.createEffectProjectile(PhysicsLayer.NPC, new Vector2(0.1f,01f),
                new Vector2(2,2), ProjectileEffects.SLOW, false);
        assertNotNull(slowProjectile.getComponent(AnimationRenderComponent.class),
                "Slow Projectile does not have AnimationRenderComponent");
    }
    @Test
    public void testSlowProjectileAnimationController() {
        Entity slowProjectile = ProjectileFactory.createEffectProjectile(PhysicsLayer.TOWER, new Vector2(0.1f, 0.1f)
                , new Vector2(2,2), ProjectileEffects.SLOW, false);
        assertNotNull(slowProjectile.getComponent(SnowBallProjectileAnimationController.class),
                "Slow Projectile does not have Animation Controller");
    }
}

