package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.*;
import com.csse3200.game.components.bosses.DemonAnimationController;
import com.csse3200.game.components.bosses.PatrickAnimationController;
import com.csse3200.game.components.bosses.IceBabyAnimationController;
import com.csse3200.game.components.npc.Boss1AnimationController;
import com.csse3200.game.components.npc.Boss2AnimationController;
import com.csse3200.game.components.tasks.bosstask.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.MobBossConfigs;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.*;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Where all entities of mob bosses are created
 */
public class MobBossFactory {

    private static final NPCConfigs configs = FileLoader.readClass(NPCConfigs.class, "configs/Boss.json");
    private static final int PRIORITY = 1;
    private static final int BOSS_MOB_AGRO_RANGE = 10;
    private static final int DEMON_HEALTH = 5000;
    private static final int DEMON_ATTACK = 0;
    private static final int PATRICK_ATTACK = 0;
    private static final int PATRICK_HEALTH = 2500;
    private static final int ICEBABY_ATTACK = 0;
    private static final int ICEBABY_HEALTH = 3000;

    /**
     * Creates new Demon boss with its correlating tasks and animations
     *
     * @return Demon boss
     */
    public static Entity createDemonBoss() {
        Entity demon = createBaseBoss();

        // Animation addition
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

        // AI task addition
        AITaskComponent aiTaskComponent = new AITaskComponent()
                .addTask(new DemonBossTask());

        // Component addition
        demon
                .addComponent(animator)
                .addComponent(new DemonAnimationController())
                .addComponent(aiTaskComponent)
                .addComponent(new CombatStatsComponent(DEMON_HEALTH, DEMON_ATTACK));

        // Scale demon
        demon.getComponent(AnimationRenderComponent.class).scaleEntity();
        demon.scaleHeight(5f);
        demon.scaleWidth(5f);
        return demon;
    }

    /**
     * Creates end state of demon boss
     *
     * @return Slimey Boy
     */
    public static Entity createSlimeyBoy() {
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

        // AI task addition
        AITaskComponent aiTaskComponent = new AITaskComponent()
                .addTask(new SlimeyBoyTask());

        // Component addition
        slimeyBoy
                .addComponent(animator)
                .addComponent(new DemonAnimationController())
                .addComponent(aiTaskComponent)
                .addComponent(new CombatStatsComponent(500, 0));

        // Scale demon
        slimeyBoy.getComponent(AnimationRenderComponent.class).scaleEntity();
        slimeyBoy.scaleHeight(5f);
        slimeyBoy.scaleWidth(5f);
        return slimeyBoy;
    }

    /**
     * Creates new Patrick boss with correlating tasks and animations
     *
     * @param health - health of the boss
     * @return Patrick Boss
     */
    public static Entity createPatrickBoss(int health) {
        Entity patrick = createBaseBoss();

        // Animation addition
        AnimationRenderComponent animator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("images/mobboss/patrick.atlas", TextureAtlas.class));
        animator.addAnimation("patrick_attack", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("patrick_cast", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("patrick_death", 0.2f, Animation.PlayMode.REVERSED);
        animator.addAnimation("patrick_hurt", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("patrick_idle", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("patrick_spell", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("patrick_walk", 0.2f, Animation.PlayMode.LOOP);

        // AI task addition
        AITaskComponent aiTaskComponent = new AITaskComponent()
                .addTask(new PatrickTask());

        // Component addition
        patrick
                .addComponent(animator)
                .addComponent(new PatrickAnimationController())
                .addComponent(aiTaskComponent)
                .addComponent(new CombatStatsComponent(PATRICK_HEALTH, PATRICK_ATTACK));

        // Scale demon
        patrick.getComponent(AnimationRenderComponent.class).scaleEntity();
        patrick.scaleHeight(4f);
        patrick.scaleWidth(4f);
        return patrick;
    }

    /**
     * Creates a patrick entity whose sole purpose is to display death animation
     * @return patrick death entity
     */
    public static Entity patrickDead() {
        Entity patrick = createBaseBoss();

        // Animation addition
        AnimationRenderComponent animator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("images/mobboss/patrick.atlas", TextureAtlas.class));
        animator.addAnimation("patrick_death", 0.2f, Animation.PlayMode.NORMAL);

        // AI task addition
        AITaskComponent aiTaskComponent = new AITaskComponent()
                .addTask(new PatrickDeathTask());

        // Component addition
        patrick
                .addComponent(animator)
                .addComponent(new PatrickAnimationController())
                .addComponent(aiTaskComponent)
                .addComponent(new CombatStatsComponent(1, 0));

        // Scale patrick
        patrick.getComponent(AnimationRenderComponent.class).scaleEntity();
        patrick.scaleHeight(4f);
        patrick.scaleWidth(4f);
        return patrick;
    }

    /**
     * Creates a new ice boss and adds its correlating animations and tasks
     *
     * @return - Ice Baby Boss
     */
    public static Entity createIceBoss() {
        Entity iceBaby = createBaseBoss();
        AITaskComponent aiTaskComponent = new AITaskComponent()
                .addTask(new IceBabyTask());

        AnimationRenderComponent animator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("images/mobboss/iceBaby.atlas", TextureAtlas.class));
        animator.addAnimation("idle", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("1_atk", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("2_atk", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("3_atk", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("death", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("intro_or_revive", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("stagger", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("take_hit", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("walk", 0.2f, Animation.PlayMode.NORMAL);

        iceBaby.addComponent(aiTaskComponent);

        iceBaby
                .addComponent(animator)
                .addComponent(new IceBabyAnimationController())
                .addComponent(aiTaskComponent)
                .addComponent(new CombatStatsComponent(ICEBABY_HEALTH, ICEBABY_ATTACK));

        iceBaby.getComponent(AnimationRenderComponent.class).scaleEntity();
        iceBaby.scaleHeight(4f);
        iceBaby.scaleWidth(4f);

        return iceBaby;
    }

    // Create Boss King 1
    public static Entity createMobBoss1(int numLane) {
        MobBossConfigs config = configs.mobBoss;
        Entity mobBoss1 = createBaseBoss();

        AITaskComponent aiTaskComponent1 = new AITaskComponent()
                .addTask(new FinalBossMovementTask(1f, numLane))
                .addTask(new MobBossDeathTask(1));;

        // Animation section
        AnimationRenderComponent animator1 = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("images/mobs/robot.atlas", TextureAtlas.class));
        animator1.addAnimation("Walk", 0.3f, Animation.PlayMode.LOOP_REVERSED);

        mobBoss1.addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator1)
                .addComponent(aiTaskComponent1)
                .addComponent(new Boss1AnimationController());

        mobBoss1.getComponent(AnimationRenderComponent.class).scaleEntity();
        mobBoss1.setScale(1f, 1f);

        return mobBoss1;
    }

    // Create Boss King 2
    public static Entity createMobBoss2() {
        MobBossConfigs config = configs.mobBoss;
        Entity mobBoss2 = createBaseBoss();

        AITaskComponent aiTaskComponent2 = new AITaskComponent()
                .addTask(new RangeBossTask(2f));

        // Animation section
        AnimationRenderComponent animator2 = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("images/mobs/boss2.atlas", TextureAtlas.class));
        animator2.addAnimation("boss_death", 0.3f, Animation.PlayMode.LOOP);
        animator2.addAnimation("Idle", 0.3f, Animation.PlayMode.LOOP);
        animator2.addAnimation("Walk", 0.3f, Animation.PlayMode.LOOP);
        animator2.addAnimation("Charging", 0.3f, Animation.PlayMode.LOOP_REVERSED);
        animator2.addAnimation("A1", 0.3f, Animation.PlayMode.LOOP);
        animator2.addAnimation("A2", 0.3f, Animation.PlayMode.LOOP);
        animator2.addAnimation("Hurt", 0.3f, Animation.PlayMode.LOOP);

        mobBoss2.addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator2)
                .addComponent(aiTaskComponent2)
                .addComponent(new Boss2AnimationController())
                .addComponent(new PhysicsComponent());

        mobBoss2.getComponent(AnimationRenderComponent.class).scaleEntity();
        mobBoss2.scaleHeight(3f);
        mobBoss2.scaleWidth(3f);

        return mobBoss2;
    }


    /**
     * Create base boss entity that all boss mobs will inherit
     * @return base mob boss entity
     */
    public static Entity createBaseBoss() {
        Entity boss = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new TouchAttackComponent(PhysicsLayer.HUMANS, 1.5f));

        PhysicsUtils.setScaledCollider(boss, 0.9f, 0.4f);

        return boss;
    }

    /**
     * Throw IllegalStateException
     */
    private MobBossFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
