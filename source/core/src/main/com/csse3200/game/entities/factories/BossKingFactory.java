package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.*;
import com.csse3200.game.components.npc.BossAnimationController;
import com.csse3200.game.components.tasks.FinalBossTask;
import com.csse3200.game.components.tasks.RangeBossTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BossKingConfigs;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.*;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class BossKingFactory {

    private static final NPCConfigs configs = FileLoader.readClass(NPCConfigs.class, "configs/Boss.json");
    private static final int PRIORITY = 1;
    private static final int BOSS_MOB_AGRO_RANGE = 10;

    // Create Boss King 1
    public static Entity createBossKing1(Entity target, int numLane) {
        BossKingConfigs config = configs.BossKing;
        Entity bossKing1 = createBaseBoss(target);

        AITaskComponent aiTaskComponent1 = new AITaskComponent().addTask(new FinalBossTask(1f, numLane));

        // Animation section
        AnimationRenderComponent animator1 = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("images/mobs/rangeBossRight.atlas", TextureAtlas.class));
        animator1.addAnimation("Walk", 0.3f, Animation.PlayMode.LOOP);

        bossKing1.addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator1)
                .addComponent(aiTaskComponent1)
                .addComponent(new BossAnimationController());

        bossKing1.getComponent(AnimationRenderComponent.class).scaleEntity();
        bossKing1.setScale(-1f, 1f);

        return bossKing1;
    }

    // Create Boss King 2
    public static Entity createBossKing2(Entity target) {
        BossKingConfigs config = configs.BossKing;
        Entity bossKing2 = createBaseBoss(target);

        AITaskComponent aiTaskComponent2 = new AITaskComponent().addTask(new RangeBossTask(2f));

        // Animation section
        AnimationRenderComponent animator2 = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("images/mobs/robot.atlas", TextureAtlas.class));
        animator2.addAnimation("Walk", 0.3f, Animation.PlayMode.LOOP);

        bossKing2.addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator2)
                .addComponent(aiTaskComponent2)
                .addComponent(new BossAnimationController());

        bossKing2.getComponent(AnimationRenderComponent.class).scaleEntity();
        bossKing2.scaleHeight(-0.5f);
        bossKing2.scaleWidth(0.5f);

        return bossKing2;
    }

    // Create the base boss entity
    private static Entity createBaseBoss(Entity target) {
        Entity boss = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new TouchAttackComponent(PhysicsLayer.HUMANS, 1.5f));

        PhysicsUtils.setScaledCollider(boss, 0.9f, 0.4f);

        return boss;
    }

    private BossKingFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
