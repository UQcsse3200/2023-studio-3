package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.*;
import com.csse3200.game.components.bosses.DemonAnimationController;
import com.csse3200.game.components.bosses.PatrickAnimationController;
import com.csse3200.game.components.bosses.IceBabyAnimationController;
import com.csse3200.game.components.tasks.bosstask.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.*;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Where all entities of mob bosses are created
 */
public class MobBossFactory {

    private static final NPCConfigs configs = FileLoader.readClass(NPCConfigs.class, "configs/Boss.json");
    private static final int ATTACK = configs.mobBoss.baseAttack;

    /**
     * Creates new Demon Boss with tasks and animations.
     * 
     * @param health value for the Demon Boss's health
     * @return a Demon Boss Entity
     */
    public static Entity createDemonBoss(int health) {
        Entity demon = createBaseBoss();

        // Animation
        AnimationRenderComponent animator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("images/mobboss/demon.atlas",
                        TextureAtlas.class));
        animator.addAnimation("demon_cast_spell", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("demon_cleave", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("demon_death", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("demon_fire_breath", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("demon_idle", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("demon_smash", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("demon_take_hit", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("demon_walk", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("transform", 0.2f, Animation.PlayMode.NORMAL);

        // Adds AI task 
        AITaskComponent aiTaskComponent = new AITaskComponent()
                .addTask(new DemonBossTask());

        // Adds components
        demon
                .addComponent(animator)
                .addComponent(new DemonAnimationController())
                .addComponent(aiTaskComponent)
                .addComponent(new CombatStatsComponent(health, ATTACK));

        // Scale Demon Boss
        demon.getComponent(AnimationRenderComponent.class).scaleEntity();
        demon.scaleHeight(5f);
        demon.scaleWidth(5f);
        return demon;
    }

    /**
     * Creates end state of demon boss
     *
     * @param health value for Slimey Boy's health
     * @return a Slimey Boy Entity
     */
    public static Entity createSlimeyBoy(int health) {
        Entity slimeyBoy = createBaseBoss();

        // Animation
        AnimationRenderComponent animator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("images/mobboss/demon.atlas",
                        TextureAtlas.class));
        animator.addAnimation("idle", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("move", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("projectile_explosion", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("projectile_idle", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("take_hit", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("transform", 0.2f, Animation.PlayMode.REVERSED);

        // Adds AI task
        AITaskComponent aiTaskComponent = new AITaskComponent()
                .addTask(new SlimeyBoyTask());

        // Adds components
        slimeyBoy
                .addComponent(animator)
                .addComponent(new DemonAnimationController())
                .addComponent(aiTaskComponent)
                .addComponent(new CombatStatsComponent(health, 0));

        // Scale Slimey Boy
        slimeyBoy.getComponent(AnimationRenderComponent.class).scaleEntity();
        slimeyBoy.scaleHeight(5f);
        slimeyBoy.scaleWidth(5f);
        return slimeyBoy;
    }

    /**
     * Creates new Patrick boss with correlating tasks and animations
     *
     * @param health value for the Patrick Boss's health
     * @return a Patrick Boss
     */
    public static Entity createPatrickBoss(int health) {
        Entity patrick = createBaseBoss();

        // Animation
        AnimationRenderComponent animator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("images/mobboss/patrick.atlas", TextureAtlas.class));
        animator.addAnimation("patrick_attack", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("patrick_cast", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("patrick_death", 0.2f, Animation.PlayMode.REVERSED);
        animator.addAnimation("patrick_hurt", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("patrick_idle", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("patrick_spell", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("patrick_walk", 0.2f, Animation.PlayMode.LOOP);

        // Adds AI task
        AITaskComponent aiTaskComponent = new AITaskComponent()
                .addTask(new PatrickTask());

        // Adds components
        patrick
                .addComponent(animator)
                .addComponent(new PatrickAnimationController())
                .addComponent(aiTaskComponent)
                .addComponent(new CombatStatsComponent(health, ATTACK));

        // Scale Patrick Boss
        patrick.getComponent(AnimationRenderComponent.class).scaleEntity();
        patrick.scaleHeight(4f);
        patrick.scaleWidth(4f);
        return patrick;
    }

    /**
     * Creates a Patrick Boss Entity whose sole purpose is to display the Patrick death animation.
     * 
     * @return a Patrick Death Entity
     */
    public static Entity patrickDead() {
        Entity patrick = createBaseBoss();

        // Animation
        AnimationRenderComponent animator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("images/mobboss/patrick.atlas", TextureAtlas.class));
        animator.addAnimation("patrick_death", 0.2f, Animation.PlayMode.NORMAL);

        // Adds AI task
        AITaskComponent aiTaskComponent = new AITaskComponent()
                .addTask(new PatrickDeathTask());

        // Adds components
        patrick
                .addComponent(animator)
                .addComponent(new PatrickAnimationController())
                .addComponent(aiTaskComponent)
                .addComponent(new CombatStatsComponent(1, 0));

        // Scale Patrick Boss
        patrick.getComponent(AnimationRenderComponent.class).scaleEntity();
        patrick.scaleHeight(4f);
        patrick.scaleWidth(4f);
        return patrick;
    }

    /**
     * Creates a new ice boss and adds its correlating animations and tasks
     *
     * @param health value for the Ice Boss's health
     * @return an Ice Baby Boss Entity
     */
    public static Entity createIceBoss(int health) {
        Entity iceBaby = createBaseBoss();
        AITaskComponent aiTaskComponent = new AITaskComponent()
                .addTask(new IceBabyTask());

        AnimationRenderComponent animator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("images/mobboss/iceBaby.atlas", TextureAtlas.class));
        animator.addAnimation("idle", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("1_atk", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("2_atk", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("3_atk", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("death", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("intro_or_revive", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("stagger", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("take_hit", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("walk", 0.2f, Animation.PlayMode.NORMAL);

        iceBaby.addComponent(aiTaskComponent);

        iceBaby
                .addComponent(animator)
                .addComponent(new IceBabyAnimationController())
                .addComponent(aiTaskComponent)
                .addComponent(new CombatStatsComponent(health, ATTACK));

        iceBaby.getComponent(AnimationRenderComponent.class).scaleEntity();
        iceBaby.scaleHeight(4f);
        iceBaby.scaleWidth(4f);

        return iceBaby;
    }

    /**
     * Creates a base boss entity with Components that all Mob Bosses will inherit.
     * 
     * @return a base mob boss entity
     */
    public static Entity createBaseBoss() {

        return new Entity()
                .addComponent(new PhysicsComponent())
//                .addComponent(new ColliderComponent())
                .addComponent(new EffectComponent(false))
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new TouchAttackComponent(PhysicsLayer.HUMANS, 1.5f));

//        PhysicsUtils.setScaledCollider(boss, 0.9f, 0.4f);
    }

    /**
     * Throw IllegalStateException
     */
    private MobBossFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
