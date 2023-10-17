package com.csse3200.game.components.tower;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
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

@ExtendWith(GameExtension.class)
class TNTDamageComponentTest {
    private GameTime gameTime;
    private Entity Attacker;
    private Entity Target_1;
    private Entity Target_2;
    private Entity Entity_3;
    private final String[] texture = {"images/towers/TNTTower.png"};
    private final String[] atlas = {"images/towers/TNTTower.atlas"};

    @BeforeEach
    void setUp() {
        gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(1f);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        RenderService render = new RenderService();
        render.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(render);
        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        resourceService.loadTextures(texture);
        resourceService.loadTextureAtlases(atlas);
        resourceService.loadAll();
        Attacker = createAttacker((short) (1 << 3));
        Target_1 = createTarget((short) (1 << 3));
        Target_2 = createTarget((short) (1 << 3));
        Entity_3 = createTarget((short) (1 << 4));
        ServiceLocator.getEntityService().register(Attacker);
        ServiceLocator.getEntityService().register(Target_1);
        ServiceLocator.getEntityService().register(Target_2);
        ServiceLocator.getEntityService().register(Entity_3);
    }

    @Test
    void TestTNTDamageOnTargetsThatAreInRange() {
        Attacker.setPosition(10,10);
        Target_1.setPosition(12,10);// Same lane and inside radius

        Attacker.getEvents().trigger("TNTDamageStart");

        assertEquals(80, Target_1.getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    void TestTNTDamageOnTargetsThatAreNoTInRange() {
        Attacker.setPosition(10,10);
        Target_1.setPosition(15,10); // on the same lane but outside the radius
        Target_2.setPosition(11,12); // inside the radius but outside the lane

        Attacker.getEvents().trigger("TNTDamageStart");
        //Nothing should happen
        assertEquals(100, Target_2.getComponent(CombatStatsComponent.class).getHealth());
        assertEquals(100, Target_1.getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    void TestTNTDamageOnEntitiesThatAreNotTargets() {
        Attacker.setPosition(10,10);
        Entity_3.setPosition(12,10); // on the same lane and inside the radius but different target layer

        Attacker.getEvents().trigger("TNTDamageStart");
        //Nothing should happen
        assertEquals(100, Entity_3.getComponent(CombatStatsComponent.class).getHealth());

    }

    Entity createAttacker(short targetLayer) {
        Entity entity =
                new Entity()
                        .addComponent(new CombatStatsComponent(100, 20))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent())
                        .addComponent(new TNTDamageComponent(targetLayer,0,4));

        return entity;
    }

    Entity createTarget(short layer) {
        Entity target =
                new Entity()
                        .addComponent(new CombatStatsComponent(100, 10))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(layer));

        return target;
    }
}


