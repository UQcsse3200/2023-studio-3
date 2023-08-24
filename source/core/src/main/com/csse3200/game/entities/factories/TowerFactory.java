package com.csse3200.game.entities.factories;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.CostComponent;
import com.csse3200.game.components.tasks.TowerCombatTask;
import com.csse3200.game.components.tasks.TowerIdleTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WallTowerConfig;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.entities.configs.WeaponTowerConfig;
import com.csse3200.game.entities.configs.baseTowerConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Factory to create a tower entity.
 *
 * Predefined tower properties are loaded from a config stored as a json file and should have
 * the properties stores in 'baseTowerConfigs'.
 */
public class TowerFactory {

    private static final int WEAPON_SCAN_INTERVAL = 1;
    private static final int COMBAT_TASK_PRIORITY = 2;
    public static final int WEAPON_TOWER_MAX_RANGE = 500;
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
     * Creates a weaponry tower that shoots at mobs - This will most likely need to be extended
     * once other types of weapon towers are developed
     * @return entity
     */
    public static Entity createWeaponTower() {
        Entity weapon = createBaseTower();
        WeaponTowerConfig config = configs.weapon;

////         TODO: uncomment once tasks are finalised - will break build if included before
        AITaskComponent aiTaskComponent = new AITaskComponent()
                .addTask(new TowerIdleTask(WEAPON_SCAN_INTERVAL))
                .addTask(new TowerCombatTask(COMBAT_TASK_PRIORITY, WEAPON_TOWER_MAX_RANGE));
//
//        // TODO: uncomment once animations are finalised - will break build if included before
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService()
                                .getAsset("images/turret.atlas", TextureAtlas.class));
        animator.addAnimation("idle", 0.5f, Animation.PlayMode.LOOP);
        animator.addAnimation("stow", 0.3f, Animation.PlayMode.NORMAL);
        animator.addAnimation("deploy", 0.2f, Animation.PlayMode.REVERSED);
        animator.addAnimation("firing", 0.2f, Animation.PlayMode.LOOP);

        weapon
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(new CostComponent(config.cost))
                .addComponent(aiTaskComponent)
                .addComponent(animator);
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