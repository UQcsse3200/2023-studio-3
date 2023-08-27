package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.npc.BossAnimationController;
import com.csse3200.game.components.tasks.RangeBossMovementTask;
import com.csse3200.game.components.tasks.TowerCombatTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BaseEntityConfig;
import com.csse3200.game.entities.configs.BossKingConfigs;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

import static com.csse3200.game.entities.factories.NPCFactory.createBaseNPC;


public class BossKingFactory {

        private static final NPCConfigs configs =
                FileLoader.readClass(NPCConfigs.class, "configs/Boss.json");

        private static final int PRIORITY = 1;
        private static final int BOSS_MOB_AGRO_RANGE = 10;

        //Boss mob 1 starts here
    public static Entity createBossKing1(Entity target) {
        BossKingConfigs config = configs.BossKing; //has its own json file
        Entity bossKing1 = createBaseNPC(target);

        // task section for movement by AI component

        AITaskComponent aiTaskComponent = new AITaskComponent()
                .addTask(new TowerCombatTask(PRIORITY, BOSS_MOB_AGRO_RANGE));

        // animation section

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService()
                                .getAsset("images/robot.atlas", TextureAtlas.class));
        animator.addAnimation("Enabling", 0.05f, Animation.PlayMode.LOOP);
        animator.addAnimation("Walk", 0.05f, Animation.PlayMode.LOOP);
        animator.addAnimation("Attack", 0.05f, Animation.PlayMode.LOOP);

        //components section
        PhysicsMovementComponent physicsMovementComponent = bossKing1.getComponent(PhysicsMovementComponent.class);
        ColliderComponent colliderComponent = bossKing1.getComponent(ColliderComponent.class);
        HitboxComponent hitboxComponent = bossKing1.getComponent(HitboxComponent.class);
        TouchAttackComponent touchAttackComponent = bossKing1.getComponent(TouchAttackComponent.class);

        if (physicsMovementComponent == null) {
            bossKing1.addComponent(new PhysicsMovementComponent());
        }
        if (colliderComponent == null) {
            bossKing1.addComponent(new ColliderComponent());
        }
        if (touchAttackComponent == null){
            bossKing1.addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f));
        }
        if (hitboxComponent == null){
            bossKing1.addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE));
        }
        if(true) {
            bossKing1
                    .addComponent(new BossAnimationController())
                    .addComponent(new CombatStatsComponent(config.health, config.baseAttack));

        }


        return bossKing1;
    }

    // boss mob 2 starts here
    public static Entity createBossKing2(Entity target) {
        BossKingConfigs config = configs.BossKing;
        Entity bossKing2 = createBaseNPC(target);

        AITaskComponent aiTaskComponent = new AITaskComponent()
                .addTask(new RangeBossMovementTask(new Vector2(1,2), 5));

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService()
                                .getAsset("images/robot.atlas", TextureAtlas.class));
        animator.addAnimation("Enabling", 0.05f, Animation.PlayMode.LOOP);
        animator.addAnimation("Walk", 0.05f, Animation.PlayMode.LOOP);
        animator.addAnimation("Attack", 0.05f, Animation.PlayMode.LOOP);



        PhysicsMovementComponent physicsMovementComponent = bossKing2.getComponent(PhysicsMovementComponent.class);
        ColliderComponent colliderComponent = bossKing2.getComponent(ColliderComponent.class);
        HitboxComponent hitboxComponent = bossKing2.getComponent(HitboxComponent.class);
        TouchAttackComponent touchAttackComponent = bossKing2.getComponent(TouchAttackComponent.class);

        if (physicsMovementComponent == null) {
            bossKing2.addComponent(new PhysicsMovementComponent());
        }
        if (colliderComponent == null) {
            bossKing2.addComponent(new ColliderComponent());
        }
        if (touchAttackComponent == null){
            bossKing2.addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f));
        }
        if (hitboxComponent == null){
            bossKing2.addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE));
        }
        if(true) {
            bossKing2
                    .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                    .addComponent(new BossAnimationController());
        }

        return bossKing2;
    }

    private BossKingFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}