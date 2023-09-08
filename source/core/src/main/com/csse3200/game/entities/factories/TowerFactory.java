package com.csse3200.game.entities.factories;

import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.tasks.DroidCombatTask;
import com.csse3200.game.components.tasks.TNTTowerCombatTask;
import com.csse3200.game.components.tower.DroidAnimationController;
import com.csse3200.game.components.tower.TNTAnimationController;
import com.csse3200.game.components.tower.TNTDamageComponent;
import com.csse3200.game.entities.configs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.CostComponent;
import com.csse3200.game.components.tasks.TowerCombatTask;
import com.csse3200.game.components.tower.TowerAnimationController;
import com.csse3200.game.components.tasks.CurrencyTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
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

    private static final int COMBAT_TASK_PRIORITY = 2;
    private static final int WEAPON_TOWER_MAX_RANGE = 40;
    private static final int TNT_TOWER_MAX_RANGE = 6;
    private static final int TNT_TOWER_RANGE = 6;
    private static final int TNT_KNOCK_BACK_FORCE = 10;
    private static final String WALL_IMAGE = "images/towers/wallTower.png";
    private static final String TURRET_ATLAS = "images/towers/turret01.atlas";
    private static final String TNT_ATLAS = "images/towers/TNTTower.atlas";
    private static final String DROID_ATLAS = "images/towers/DroidTower.atlas";
    private static final float DROID_SPEED = 0.25f;
    private static final String DEFAULT_ANIM = "default";
    private static final String WALK_ANIM = "walk";
    private static final String DEATH_ANIM = "death";
    private static final String GO_UP = "goUp";
    private static final String GO_DOWN = "goDown";
    private static final String SHOOT_UP = "attackUp";
    private static final String SHOOT_DOWN = "attackDown";
    private static final float DEFAULT_SPEED= 0.2f;
    private static final String DIG_ANIM = "dig";
    private static final float DIG_SPEED = 0.2f;
    private static final String EXPLODE_ANIM = "explode";
    private static final float EXPLODE_SPEED = 0.2f;
    private static final String IDLE_ANIM = "idle";
    private static final float IDLE_SPEED = 0.3f;
    private static final String DEPLOY_ANIM = "deploy";
    private static final float DEPLOY_SPEED = 0.2f;
    private static final String STOW_ANIM = "stow";
    private static final float STOW_SPEED = 0.2f;
    private static final String FIRE_ANIM = "firing";
    private static final float FIRE_SPEED = 0.25f;
    private static final int INCOME_INTERVAL = 300;
    private static final int INCOME_TASK_PRIORITY = 1;

    private static final baseTowerConfigs configs =
            FileLoader.readClass(baseTowerConfigs.class, "configs/tower.json");
    /**
     * Creates an income tower that generates scrap
     * @return income
     */
    public static Entity createIncomeTower() {
        Entity income = createBaseTower();
        IncomeTowerConfig config = configs.income;

        // Create the CurrencyIncomeTask and add it to the AITaskComponent
        CurrencyTask currencyTask = new CurrencyTask(INCOME_TASK_PRIORITY, 3);

        int updatedInterval = 1;
        currencyTask.setInterval(updatedInterval);
        AITaskComponent aiTaskComponent = new AITaskComponent().addTask(currencyTask);

        income
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(new CostComponent(config.cost))
                .addComponent(new TextureRenderComponent("images/towers/mine_tower.png"))
                .addComponent(aiTaskComponent);


        return income;
    }

    public static Entity createWallTower() {
        Entity wall = createBaseTower();
        WallTowerConfig config = configs.wall;

        wall
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(new CostComponent(config.cost))
                .addComponent(new TextureRenderComponent(WALL_IMAGE));
        return wall;
    }


    /**
     * Create a type of TNT that explodes once it detects a mob within a certain range.
     * Upon detonation, the TNT will apply both knock-back and health damage to the affected mobs
     * @return entity
     */
    public static Entity createTNTTower() {
        Entity TNTTower = createBaseTower();
        TNTTowerConfigs config = configs.TNTTower;

        AITaskComponent aiTaskComponent = new AITaskComponent()
                .addTask(new TNTTowerCombatTask(COMBAT_TASK_PRIORITY, TNT_TOWER_MAX_RANGE));

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService()
                                .getAsset(TNT_ATLAS, TextureAtlas.class));

        animator.addAnimation(DIG_ANIM, DIG_SPEED, Animation.PlayMode.NORMAL);
        animator.addAnimation(DEFAULT_ANIM,DEFAULT_SPEED, Animation.PlayMode.NORMAL);
        animator.addAnimation(EXPLODE_ANIM,EXPLODE_SPEED, Animation.PlayMode.NORMAL);

        TNTTower
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(new CostComponent(config.cost))
                .addComponent(new TNTDamageComponent(PhysicsLayer.NPC,TNT_KNOCK_BACK_FORCE,TNT_TOWER_RANGE))
                .addComponent(aiTaskComponent)
                .addComponent(animator)
                .addComponent(new TNTAnimationController());

        TNTTower.getComponent(AnimationRenderComponent.class).scaleEntity();

        return TNTTower;
    }

    /**
     * This robotic unit is programmed to detect mobs within its vicinity and fire projectiles at them.
     * The droid has the capability to switch its aim from high to low positions, thereby providing a versatile attack strategy.
     * When it detects a mob, the droid releases a projectile that inflicts both physical damage and a slow-down effect on the target.
     * @return entity
     */
    public static Entity createDroidTower() {
        Entity DroidTower = createBaseTower();
        DroidTowerConfig config = configs.DroidTower;

        AITaskComponent aiTaskComponent = new AITaskComponent()
                .addTask(new DroidCombatTask(COMBAT_TASK_PRIORITY, WEAPON_TOWER_MAX_RANGE));

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService()
                                .getAsset(DROID_ATLAS, TextureAtlas.class));

        animator.addAnimation(IDLE_ANIM, DROID_SPEED, Animation.PlayMode.NORMAL);
        animator.addAnimation(SHOOT_UP,DROID_SPEED, Animation.PlayMode.NORMAL);
        animator.addAnimation(SHOOT_DOWN,DROID_SPEED, Animation.PlayMode.NORMAL);
        animator.addAnimation(WALK_ANIM,DROID_SPEED, Animation.PlayMode.NORMAL);
        animator.addAnimation(DEATH_ANIM,DROID_SPEED, Animation.PlayMode.NORMAL);
        animator.addAnimation(GO_UP,DROID_SPEED, Animation.PlayMode.NORMAL);
        animator.addAnimation(GO_DOWN,DROID_SPEED, Animation.PlayMode.NORMAL);



        DroidTower
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(new CostComponent(config.cost))
                .addComponent(new DroidAnimationController())
                .addComponent(animator)
                .addComponent(aiTaskComponent);

        return DroidTower;
    }


    /**
     * Creates a weaponry tower that shoots at mobs - This will most likely need to be extended
     * once other types of weapon towers are developed
     * @return entity
     */
    public static Entity createWeaponTower() {
        Entity weapon = createBaseTower();
        WeaponTowerConfig config = configs.weapon;

        // AiTaskComponent will run the tower task which carries out detection of targets and trigger events
        AITaskComponent aiTaskComponent = new AITaskComponent()
                .addTask(new TowerCombatTask(COMBAT_TASK_PRIORITY, WEAPON_TOWER_MAX_RANGE));

        // Contains all the animations that the tower will have
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService()
                                .getAsset(TURRET_ATLAS, TextureAtlas.class));
        animator.addAnimation(IDLE_ANIM, IDLE_SPEED, Animation.PlayMode.LOOP);
        animator.addAnimation(STOW_ANIM, STOW_SPEED, Animation.PlayMode.NORMAL);
        animator.addAnimation(DEPLOY_ANIM, DEPLOY_SPEED, Animation.PlayMode.REVERSED);
        animator.addAnimation(FIRE_ANIM, FIRE_SPEED, Animation.PlayMode.LOOP);

        weapon
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(new CostComponent(config.cost))
                .addComponent(aiTaskComponent)
                .addComponent(animator)
                .addComponent(new TowerAnimationController());

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
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE)) // TODO: we might have to change the names of the layers
                .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody));

        return tower;
    }
}