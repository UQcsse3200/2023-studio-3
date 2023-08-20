package com.csse3200.game.entities.factories;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TowerFactoryTest {

    private Entity baseTower;

    @BeforeEach
    public void setUp() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        baseTower = TowerFactory.createBaseTower();
    }

    @Test
    public void testCreateBaseTowerNotNull() {
        assertNotNull(baseTower, "Base tower should not be null");
    }

    @Test
    public void testCreateBaseTowerHasColliderComponent() {
        assertNotNull(baseTower.getComponent(ColliderComponent.class),
                "Base tower should have ColliderComponent");
    }

    @Test
    public void testCreateBaseTowerHasHitboxComponent() {
        assertNotNull(baseTower.getComponent(HitboxComponent.class),
                "Base tower should have HitboxComponent");
    }

    @Test
    public void testCreateBaseTowerHasPhysicsComponent() {
        assertNotNull(baseTower.getComponent(PhysicsComponent.class),
                "Base tower should have PhysicsComponent");
    }

    @Test
    public void testCreateBaseTowerPhysicsComponentStaticBody() {
        PhysicsComponent physicsComponent = baseTower.getComponent(PhysicsComponent.class);
        assertTrue(physicsComponent.getBody().getType() == BodyType.StaticBody,
                "PhysicsComponent should be of type StaticBody");
    }
}
