package com.csse3200.game.entities.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.npc.BossAnimationController;
import com.csse3200.game.components.npc.BossStatsDisplay;
import com.csse3200.game.components.tasks.MoveToMiddleTask;
import com.csse3200.game.components.tasks.RangeBossMovementTask;
import com.csse3200.game.components.tasks.TowerCombatTask;
import com.csse3200.game.components.tower.TowerAnimationController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BossKingConfigs;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;


public class BossKingFactory {

        private static final NPCConfigs configs =
                FileLoader.readClass(NPCConfigs.class, "configs/Boss.json");

        private static final int PRIORITY = 1;
        private static final int BOSS_MOB_AGRO_RANGE = 10;

        //Boss mob 1 starts here
    public static Entity createBossKing1(Entity target) {
        BossKingConfigs config = configs.BossKing; //has its own json file
        Entity bossKing1 = createBaseBoss(target);
        float middleX = Gdx.graphics.getWidth() / 2f; // Middle X position of the screen
        float middleY = Gdx.graphics.getHeight() / 2f; // Middle Y position of the screen
/**
        MoveToMiddleTask moveToMiddleTask1 = new MoveToMiddleTask(
                bossKing1, // Pass the owner entity
                new Vector2(middleX, middleY), // Target position (middle-right of screen)
                1f // Speed
        );
        AITaskComponent aiTaskComponent = new AITaskComponent()
                .addTask(moveToMiddleTask1);
// animation section
 **/
            AnimationRenderComponent animator1 =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService()
                                .getAsset("images/robot.atlas", TextureAtlas.class));
        animator1.addAnimation("Enabling", 0.3f, Animation.PlayMode.LOOP);
        animator1.addAnimation("Idle", 0.2f, Animation.PlayMode.NORMAL);
        animator1.addAnimation("Walk", 0.2f, Animation.PlayMode.REVERSED);
        animator1.addAnimation("Attack", 0.1f, Animation.PlayMode.LOOP);

        bossKing1
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator1)
                //.addComponent(new TextureRenderComponent("images/ghost.png"));
                .addComponent(new BossAnimationController());

        return bossKing1;
    }

    // boss mob 2 starts here
    public static Entity createBossKing2(Entity target) {
        BossKingConfigs config = configs.BossKing;
        Entity bossKing2 = createBaseBoss(target);
        float middleX = Gdx.graphics.getWidth() / 2f; // Middle X position of the screen
        float middleY = Gdx.graphics.getHeight() / 2f; // Middle Y position of the screen
/**
        MoveToMiddleTask moveToMiddleTask1 = new MoveToMiddleTask(
                bossKing2, // Pass the owner entity
                new Vector2(middleX, middleY), // Target position (middle-right of screen)
                1f // Speed
        );
        AITaskComponent aiTaskComponent = new AITaskComponent()
                .addTask(moveToMiddleTask1);
**/
        AnimationRenderComponent animator2 =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService()
                                .getAsset("images/turret01.atlas", TextureAtlas.class));
        animator2.addAnimation("idle", 0.3f, Animation.PlayMode.LOOP);
        animator2.addAnimation("stow", 0.2f, Animation.PlayMode.NORMAL);
        animator2.addAnimation("deploy", 0.2f, Animation.PlayMode.REVERSED);
        animator2.addAnimation("firing", 0.1f, Animation.PlayMode.LOOP);

        bossKing2
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator2)
                //.addComponent(new TextureRenderComponent("images/ghost.png"));
                .addComponent(new BossAnimationController());

        //bossKing2.getComponent(TextureRenderComponent.class).scaleEntity();
        return bossKing2;
    }

    public static Entity createBaseBoss(Entity target) {

        Entity boss =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 4f));
                        //.addComponent(new TouchAttackComponent(PhysicsLayer.OBSTACLE, 2f));


        PhysicsUtils.setScaledCollider(boss, 0.9f, 0.4f);
        return boss;
    }

    private BossKingFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}