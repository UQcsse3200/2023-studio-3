package com.csse3200.game.entities.factories;


import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.CostComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WallTowerConfig;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.entities.configs.WeaponTowerConfig;
import com.csse3200.game.entities.configs.baseTowerConfigs;
import com.csse3200.game.files.FileLoader;

/**
 * Factory to create a tower entity.
 *
 * Predefined tower properties are loaded from a config stored as a json file and should have
 * the properties stores in 'baseTowerConfigs'.
 */
public class TowerFactory {

    private static final baseTowerConfigs configs =
            FileLoader.readClass(baseTowerConfigs.class, "configs/tower.json");



    public static Entity createWallTower() {
        Entity wall = createBaseTower();
        WallTowerConfig config = configs.wall;

        wall
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(new CostComponent(config.cost));

        return wall;

    }

    /**
     * Creates a weaponry tower that shoots at mobs
     * @return entity
     */
    public static Entity createWeaponTower() {
        Entity weapon = createBaseTower();
        WeaponTowerConfig config = configs.weapon;

        weapon
                .addComponent(new CombatStatsComponent(config.health,config.baseAttack))
                .addComponent(new CostComponent(config.cost));

        return weapon;

    }
    /**
     * Creates a generic tower entity to be used as a base entity by more specific tower creation methods.
     * @return entity
     */
    public static Entity createBaseTower() {
        // we're going to add more components later on
        Entity tower = new Entity()
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE)) // we might have to change the names of the layers
            .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody));

        //PhysicsUtils.setScaledCollider(tower, 0.5f, 0.2f); //values might vary according to entity scale value

        return tower;
    }
}