package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.npc.DeflectingComponent;
import com.csse3200.game.components.npc.DodgingComponent;
import com.csse3200.game.components.npc.DragonKnightAnimationController;
import com.csse3200.game.components.npc.FireWormAnimationController;
import com.csse3200.game.components.npc.GhostAnimationController;
import com.csse3200.game.components.npc.SkeletonAnimationController;
import com.csse3200.game.components.npc.SplitMoblings;
import com.csse3200.game.components.npc.WaterQueenAnimationController;
import com.csse3200.game.components.npc.WaterSlimeAnimationController;
import com.csse3200.game.components.npc.WizardAnimationController;
import com.csse3200.game.components.npc.XenoAnimationController;
import com.csse3200.game.components.tasks.MobAttackTask;
import com.csse3200.game.components.tasks.MobDodgeTask;
import com.csse3200.game.components.tasks.MobMeleeAttackTask;
import com.csse3200.game.components.tasks.MobRangedAttackTask;
import com.csse3200.game.components.tasks.MobShootTask;
import com.csse3200.game.components.tasks.MobWanderTask;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.Melee;
import com.csse3200.game.entities.PredefinedWeapons;
import com.csse3200.game.entities.configs.*;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;

/**
 * Factory to create non-playable character (NPC) entities with predefined components.
 *
 * <p>Each NPC entity type should have a creation method that returns a corresponding entity.
 * Predefined entity properties can be loaded from configs stored as json files which are defined in
 * "NPCConfigs".
 *
 * <p>If needed, this factory can be separated into more specific factories for entities with
 * similar characteristics.
 */
public class NPCFactory {
  private static final NPCConfigs configs =
      FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

  /**
   * Creates a ghost entity.
   *
   * @return entity
   */
  public static Entity createGhost() {
    Entity ghost = createMeleeBaseNPC();
    BaseEntityConfig config = configs.ghost;
    /**
    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/ghost.atlas", TextureAtlas.class));
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
  **/
    ghost
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
     //   .addComponent(animator)
             .addComponent(new TextureRenderComponent("images/mobs/satyr.png"));
     //   .addComponent(new GhostAnimationController());

    ghost.getComponent(TextureRenderComponent.class).scaleEntity();

    return ghost;
  }

  /**
   * Creates a ghost king entity.
   * 
   * @return entity
   */
  public static Entity createGhostKing() {
    Entity ghostKing = createMeleeBaseNPC();
    GhostKingConfig config = configs.ghostKing;

    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService()
                .getAsset("images/ghostKing.atlas", TextureAtlas.class));
    animator.addAnimation("float", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("angry_float", 0.2f, Animation.PlayMode.LOOP);

    ghostKing
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
        .addComponent(animator)
        .addComponent(new GhostAnimationController());

    ghostKing.getComponent(AnimationRenderComponent.class).scaleEntity();
    return ghostKing;
  }

  /**
   * Creates a fire worm entity.
   *
   * @return entity
   */
  public static Entity createSkeleton() {
    Entity skeleton = createMeleeBaseNPC();
    BaseEnemyConfig config = configs.xenoGrunt;
    ArrayList<Melee> melee = new ArrayList<>(Arrays.asList(PredefinedWeapons.sword, PredefinedWeapons.kick));
    // tester projectiles
    ArrayList<ProjectileConfig> projectiles = new ArrayList<>(Arrays.asList(PredefinedWeapons.fireBall, PredefinedWeapons.frostBall));
    ArrayList<Currency> drops = new ArrayList<>();

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/mobs/skeleton.atlas", TextureAtlas.class));
    animator.addAnimation("skeleton_walk", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("skeleton_attack", 0.1f);
    animator.addAnimation("skeleton_death", 0.1f);
    animator.addAnimation("default", 0.1f);
    skeleton
            .addComponent(new CombatStatsComponent(config.fullHeath, config.baseAttack, drops, melee, projectiles))
            .addComponent(animator)
            .addComponent(new SkeletonAnimationController());

    skeleton.getComponent(HitboxComponent.class).setAsBoxAligned(new Vector2(.3f, .5f), PhysicsComponent.AlignX.RIGHT, PhysicsComponent.AlignY.CENTER);
    skeleton.getComponent(AnimationRenderComponent.class).scaleEntity();

    return skeleton;
  }

  /**
   * Creates a wizard entity.
   *
   * @return entity
   */
  public static Entity createWizard() {
    Entity wizard = createRangedBaseNPC();
    BaseEnemyConfig config = configs.xenoGrunt;
    ArrayList<Melee> melee = new ArrayList<>(Arrays.asList(PredefinedWeapons.sword, PredefinedWeapons.kick));
    // tester projectiles
    ArrayList<ProjectileConfig> projectiles = new ArrayList<>(Arrays.asList(PredefinedWeapons.fireBall, PredefinedWeapons.frostBall));
    ArrayList<Currency> drops = new ArrayList<>();

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/mobs/wizard.atlas", TextureAtlas.class));
    animator.addAnimation("wizard_run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("wizard_attack", 0.1f);
    animator.addAnimation("wizard_death", 0.1f);
    animator.addAnimation("default", 0.1f);
    wizard
            .addComponent(new CombatStatsComponent(config.fullHeath, config.baseAttack, drops, melee, projectiles))
            .addComponent(animator)
            .addComponent(new WizardAnimationController());

    wizard.getComponent(HitboxComponent.class).setAsBoxAligned(new Vector2(.3f, .5f), PhysicsComponent.AlignX.RIGHT, PhysicsComponent.AlignY.CENTER);
    wizard.getComponent(AnimationRenderComponent.class).scaleEntity();

    return wizard;
  }
  /**
   * Creates a wizard entity.
   *
   * @return entity
   */
  public static Entity createWaterQueen() {
    Entity wizard = createRangedBaseNPC();
    BaseEnemyConfig config = configs.xenoGrunt;
    ArrayList<Melee> melee = new ArrayList<>(Arrays.asList(PredefinedWeapons.sword, PredefinedWeapons.kick));
    // tester projectiles
    ArrayList<ProjectileConfig> projectiles = new ArrayList<>(Arrays.asList(PredefinedWeapons.fireBall, PredefinedWeapons.frostBall));
    ArrayList<Currency> drops = new ArrayList<>();

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/mobs/water_queen.atlas", TextureAtlas.class));
    animator.addAnimation("water_queen_walk", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("water_queen_attack", 0.1f);
    animator.addAnimation("water_queen_death", 0.1f);
    animator.addAnimation("default", 0.1f);
    wizard
            .addComponent(new CombatStatsComponent(config.fullHeath, config.baseAttack, drops, melee, projectiles))
            .addComponent(animator)
            .addComponent(new WaterQueenAnimationController());

    wizard.getComponent(HitboxComponent.class).setAsBoxAligned(new Vector2(.3f, .5f), PhysicsComponent.AlignX.RIGHT, PhysicsComponent.AlignY.CENTER);
    wizard.getComponent(AnimationRenderComponent.class).scaleEntity();

    return wizard;
  }
  /**
   * Creates a wizard entity.
   *
   * @return entity
   */
  public static Entity createWaterSlime() {
    Entity waterSlime = createMeleeBaseNPC();
    BaseEnemyConfig config = configs.xenoGrunt;
    ArrayList<Melee> melee = new ArrayList<>(Arrays.asList(PredefinedWeapons.sword, PredefinedWeapons.kick));
    // tester projectiles
    ArrayList<ProjectileConfig> projectiles = new ArrayList<>(Arrays.asList(PredefinedWeapons.fireBall, PredefinedWeapons.frostBall));
    ArrayList<Currency> drops = new ArrayList<>();

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/mobs/water_slime.atlas", TextureAtlas.class));
    animator.addAnimation("water_slime_walk", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("water_slime_attack", 0.1f);
    animator.addAnimation("water_slime_death", 0.1f);
    animator.addAnimation("default", 0.1f);
    waterSlime
            .addComponent(new CombatStatsComponent(config.fullHeath, config.baseAttack, drops, melee, projectiles))
            .addComponent(animator)
            .addComponent(new SplitMoblings(7, 0.5f))
            .addComponent(new WaterSlimeAnimationController());

    waterSlime.getComponent(HitboxComponent.class).setAsBoxAligned(new Vector2(.3f, .5f), PhysicsComponent.AlignX.RIGHT, PhysicsComponent.AlignY.CENTER);
    waterSlime.getComponent(AnimationRenderComponent.class).scaleEntity();

    return waterSlime;
  }
  /**
   * Creates a fire worm entity.
   *
   * @return entity
   */
  public static Entity createFireWorm() {
    Entity fireWorm = createRangedBaseNPC();
    BaseEnemyConfig config = configs.xenoGrunt;
    ArrayList<Melee> melee = new ArrayList<>(Arrays.asList(PredefinedWeapons.sword, PredefinedWeapons.kick));
    // tester projectiles
    ArrayList<ProjectileConfig> projectiles = new ArrayList<>(Arrays.asList(PredefinedWeapons.fireBall, PredefinedWeapons.frostBall));
    ArrayList<Currency> drops = new ArrayList<>();

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/mobs/fire_worm.atlas", TextureAtlas.class));
    animator.addAnimation("fire_worm_walk", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("fire_worm_attack", 0.1f);
    animator.addAnimation("fire_worm_death", 0.1f);
    animator.addAnimation("default", 0.1f);
    fireWorm
            .addComponent(new CombatStatsComponent(config.fullHeath, config.baseAttack, drops, melee, projectiles))
            .addComponent(animator)
            .addComponent(new FireWormAnimationController());

    fireWorm.getComponent(HitboxComponent.class).setAsBoxAligned(new Vector2(.3f, .5f), PhysicsComponent.AlignX.RIGHT, PhysicsComponent.AlignY.CENTER);
    fireWorm.getComponent(AnimationRenderComponent.class).scaleEntity();

    return fireWorm;
  }
  /**
   * Creates a dragon Knight entity
   *
   * @return entity
   */
  public static Entity createDragonKnight() {
    Entity dragonKnight = createMeleeBaseNPC();
    BaseEnemyConfig config = configs.xenoGrunt;
    ArrayList<Melee> melee = new ArrayList<>(Arrays.asList(PredefinedWeapons.sword, PredefinedWeapons.kick));
    // tester projectiles
    ArrayList<ProjectileConfig> projectiles = new ArrayList<>(Arrays.asList(PredefinedWeapons.fireBall, PredefinedWeapons.frostBall));
    ArrayList<Currency> drops = new ArrayList<>();

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/mobs/dragon_knight.atlas", TextureAtlas.class));
    animator.addAnimation("dragon_knight_run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("dragon_knight_attack", 0.1f);
    animator.addAnimation("dragon_knight_death", 0.1f);
    animator.addAnimation("default", 0.1f);
    dragonKnight
            .addComponent(new CombatStatsComponent(config.fullHeath, config.baseAttack, drops, melee, projectiles))
            .addComponent(animator)
            .addComponent(new DragonKnightAnimationController());

    dragonKnight.getComponent(HitboxComponent.class).setAsBoxAligned(new Vector2(.3f, .5f), PhysicsComponent.AlignX.RIGHT, PhysicsComponent.AlignY.BOTTOM);
    dragonKnight.getComponent(AnimationRenderComponent.class).scaleEntity();

    return dragonKnight;
  }


  /**
   * Creates a xeno grunt entity.
   *
   * @return entity
   */
  public static Entity createXenoGrunt() {
    Entity xenoGrunt = createMeleeBaseNPC();
    BaseEnemyConfig config = configs.xenoGrunt;
    ArrayList<Melee> melee = new ArrayList<>(Arrays.asList(PredefinedWeapons.sword, PredefinedWeapons.kick));
    // tester projectiles
    ArrayList<ProjectileConfig> projectiles = new ArrayList<>(Arrays.asList(PredefinedWeapons.fireBall, PredefinedWeapons.frostBall));
    ArrayList<Currency> drops = new ArrayList<>();

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/mobs/xenoGrunt.atlas", TextureAtlas.class));
    animator.addAnimation("xeno_run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("xeno_hurt", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("xeno_shoot", 0.1f);
    animator.addAnimation("xeno_melee_1", 0.1f);
    animator.addAnimation("xeno_melee_2", 0.1f);
    animator.addAnimation("xeno_die", 0.1f);
    animator.addAnimation("default", 0.1f);
    xenoGrunt
            .addComponent(new CombatStatsComponent(config.fullHeath, config.baseAttack, drops, melee, projectiles))
            .addComponent(animator)
            .addComponent(new XenoAnimationController());

    xenoGrunt.getComponent(HitboxComponent.class).setAsBoxAligned(new Vector2(.3f, .5f), PhysicsComponent.AlignX.RIGHT, PhysicsComponent.AlignY.BOTTOM);
    xenoGrunt.getComponent(AnimationRenderComponent.class).scaleEntity();

    return xenoGrunt;
  }

  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @return entity
   */
  public static Entity createMeleeBaseNPC() {
    AITaskComponent aiComponent =
        new AITaskComponent()
            .addTask(new MobWanderTask(new Vector2(2f, 2f), 2f))
            .addTask(new MobMeleeAttackTask(2, 2f));
        // .addTask(new MeleeMobTask(new Vector2(2f, 2f), 2f));

            // .addTask(new MobAttackTask(2, 40));
    Entity npc =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.XENO))
            .addComponent(new TouchAttackComponent(PhysicsLayer.HUMANS))
            .addComponent(aiComponent);
    PhysicsUtils.setScaledCollider(npc, 0.3f, 0.5f);
    return npc;
  }
  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @return entity
   */
  public static Entity createRangedBaseNPC() {
    AITaskComponent aiComponent =
        new AITaskComponent()
            .addTask(new MobWanderTask(new Vector2(2f, 2f), 2f))
            .addTask(new MobRangedAttackTask(2, 2f));
        // .addTask(new MeleeMobTask(new Vector2(2f, 2f), 2f));

            // .addTask(new MobAttackTask(2, 40));
    Entity npc =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.XENO))
            .addComponent(new TouchAttackComponent(PhysicsLayer.HUMANS))
            .addComponent(aiComponent);
    PhysicsUtils.setScaledCollider(npc, 0.3f, 0.5f);
    return npc;
  }

  private NPCFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }

  // * COW'S TESTING ARENA DONT TOUCH
  public static Entity createSplittingXenoGrunt() {
    Entity splitXenoGrunt = createXenoGrunt()
        // add the scaling yourself. can also scale the X and Y component,
        // leading to some very interesting mob designs.
        .addComponent(new SplitMoblings(7, 0.5f))
        .addComponent(new DodgingComponent(PhysicsLayer.PROJECTILE, 0.25f));
    
    // * TEMPORARY TESTING FOR PROJECTILE DODGING
    splitXenoGrunt.getComponent(AITaskComponent.class).addTask(new MobDodgeTask(new Vector2(2f, 2f), 2f, 5));
    return splitXenoGrunt;
  }

  public static Entity createDodgingDragonKnight() {
    Entity fireWorm = createDragonKnight();

    fireWorm.addComponent(new DodgingComponent(PhysicsLayer.PROJECTILE, 0.25f));

   fireWorm.getComponent(AITaskComponent.class).addTask(new MobDodgeTask(new Vector2(2f, 2f), 2f, 5));

    return fireWorm;
  }

  public static Entity createDeflectXenoGrunt() {
    Entity deflectXenoGrunt = createXenoGrunt();
    deflectXenoGrunt.addComponent(new DeflectingComponent(
        PhysicsLayer.PROJECTILE, PhysicsLayer.TOWER, 10));

    return deflectXenoGrunt;
  }
}


