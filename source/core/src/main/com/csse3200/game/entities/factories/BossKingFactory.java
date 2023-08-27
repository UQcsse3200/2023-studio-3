package com.csse3200.game.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.npc.BossAnimationController;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BossKingConfig;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import static com.csse3200.game.factories.NPCFactory.createBaseNPC;

public class BossKingFactory {

        private static final NPCConfigs configs =
                FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

        public static Entity createBossKing1(Entity target) {
            BossKingConfig config = configs.BossKing;
            Entity bossKing1 = createBaseNPC(target);
            /**
            AnimationRenderComponent animator =
                    new AnimationRenderComponent(
                            ServiceLocator.getResourceService()
                                    .getAsset("images/robot.atlas", TextureAtlas.class));
            animator.addAnimation("Enabling", 0.05f, Animation.PlayMode.LOOP);
            animator.addAnimation("Walk", 0.05f, Animation.PlayMode.LOOP);
            animator.addAnimation("Attack", 0.05f, Animation.PlayMode.LOOP);
            animator.addAnimation("Shutdown", 0.05f, Animation.PlayMode.LOOP);
            **/

            bossKing1
                    .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                    //.addComponent(new BossAnimationController());
                    .addComponent(new TextureRenderComponent("images/turret-01.png"));
;
            return bossKing1;
        }

    public static Entity createBossKing2(Entity target) {
        BossKingConfig config = configs.BossKing;
        Entity bossKing2 = createBaseNPC(target);
        /**
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService()
                                .getAsset("images/robot.atlas", TextureAtlas.class));
        animator.addAnimation("Enabling", 0.05f, Animation.PlayMode.LOOP);
        animator.addAnimation("Walk", 0.05f, Animation.PlayMode.LOOP);
        animator.addAnimation("Attack", 0.05f, Animation.PlayMode.LOOP);
        animator.addAnimation("Shutdown", 0.05f, Animation.PlayMode.LOOP);
        **/

        bossKing2
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(new TextureRenderComponent("images/turret-01.png"));
    //.addComponent(new BossAnimationController());
        return bossKing2;
    }

    private BossKingFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}