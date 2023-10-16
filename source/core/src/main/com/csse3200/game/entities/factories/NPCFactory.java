package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.npc.ArcaneArcherAnimationController;
import com.csse3200.game.components.npc.CoatAnimationController;
import com.csse3200.game.components.npc.DeflectingComponent;
import com.csse3200.game.components.npc.DodgingComponent;
import com.csse3200.game.components.npc.DragonKnightAnimationController;
import com.csse3200.game.components.npc.FireWormAnimationController;
import com.csse3200.game.components.npc.GhostAnimationController;
import com.csse3200.game.components.npc.NightBorneAnimationController;
import com.csse3200.game.components.npc.SkeletonAnimationController;
import com.csse3200.game.components.npc.SplitMoblings;
import com.csse3200.game.components.npc.WaterQueenAnimationController;
import com.csse3200.game.components.npc.WaterSlimeAnimationController;
import com.csse3200.game.components.npc.WizardAnimationController;
import com.csse3200.game.components.npc.XenoAnimationController;
import com.csse3200.game.components.tasks.MobDodgeTask;
import com.csse3200.game.components.tasks.MobMeleeAttackTask;
import com.csse3200.game.components.tasks.MobRangedAttackTask;
import com.csse3200.game.components.tasks.MobTask.MobTask;
import com.csse3200.game.components.tasks.MobTask.MobType;
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
//  public static Entity createSkeleton(int health) {
//    Entity skeleton = createBaseNPC(int health);
  public static Entity createSkeleton(int health) {
    Entity skeleton = createBaseNPC();
    ArrayList<Currency> drops = new ArrayList<>();

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/mobs/skeleton.atlas", TextureAtlas.class));
    animator.addAnimation("skeleton_walk", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("skeleton_attack", 0.1f);
    animator.addAnimation("skeleton_death", 0.1f);
    animator.addAnimation("skeleton_freeze", 0.1f);
    animator.addAnimation("default", 0.1f);

    AITaskComponent aiTaskComponent = new AITaskComponent()
            .addTask(new MobTask(MobType.SKELETON));

    skeleton
            .addComponent(new CombatStatsComponent(health, 0, drops))
            .addComponent(animator)
            .addComponent(new SkeletonAnimationController())
            .addComponent(aiTaskComponent);

    skeleton.getComponent(HitboxComponent.class).setAsBoxAligned(new Vector2(.3f, .5f), PhysicsComponent.AlignX.RIGHT, PhysicsComponent.AlignY.CENTER);
    skeleton.getComponent(AnimationRenderComponent.class).scaleEntity();

    return skeleton;
  }

  /**
   * Creates a wizard entity.
   *
   * @return entity
   */
  public static Entity createWizard(int health) {
    Entity wizard = createBaseNPC();
    ArrayList<Currency> drops = new ArrayList<>();

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/mobs/wizard.atlas", TextureAtlas.class));
    animator.addAnimation("wizard_run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("wizard_attack", 0.1f);
    animator.addAnimation("wizard_death", 0.1f);
    animator.addAnimation("wizard_freeze", 0.1f);
    animator.addAnimation("default", 0.1f);
    AITaskComponent aiTaskComponent = new AITaskComponent()
            .addTask(new MobTask(MobType.WIZARD));

    wizard
            .addComponent(new CombatStatsComponent(health, 0, drops))
            .addComponent(animator)
            .addComponent(new WizardAnimationController())
            .addComponent(aiTaskComponent);

    wizard.getComponent(HitboxComponent.class).setAsBoxAligned(new Vector2(.3f, .5f), PhysicsComponent.AlignX.RIGHT, PhysicsComponent.AlignY.CENTER);
    wizard.getComponent(AnimationRenderComponent.class).scaleEntity();

    return wizard;
  }
  /**
   * Creates a water queen entity.
   *
   * @return entity
   */
  public static Entity createWaterQueen(int health) {
    Entity waterQueen = createBaseNPC();
    ArrayList<Currency> drops = new ArrayList<>();

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/mobs/water_queen.atlas", TextureAtlas.class));
    animator.addAnimation("water_queen_walk", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("water_queen_attack", 0.1f);
    animator.addAnimation("water_queen_death", 0.1f);
    animator.addAnimation("water_queen_freeze", 0.1f);
    animator.addAnimation("default", 0.1f);

    AITaskComponent aiTaskComponent = new AITaskComponent()
            .addTask(new MobTask(MobType.WATER_QUEEN));

    waterQueen
            .addComponent(new CombatStatsComponent(health, 0, drops))
            .addComponent(animator)
            .addComponent(new WaterQueenAnimationController())
            .addComponent(aiTaskComponent);

    waterQueen.getComponent(HitboxComponent.class).setAsBoxAligned(new Vector2(.3f, .5f), PhysicsComponent.AlignX.RIGHT, PhysicsComponent.AlignY.CENTER);
    waterQueen.getComponent(AnimationRenderComponent.class).scaleEntity();

    return waterQueen;
  }
  /**
   * Creates a water slime entity.
   *
   * @return entity
   */
  public static Entity createBaseWaterSlime(int health) {
    Entity waterSlime = createBaseNPC();
    ArrayList<Currency> drops = new ArrayList<>();

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/mobs/water_slime.atlas", TextureAtlas.class));
    animator.addAnimation("water_slime_walk", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("water_slime_attack", 0.1f);
    animator.addAnimation("water_slime_death", 0.2f);
    animator.addAnimation("water_slime_freeze", 0.2f);
    animator.addAnimation("default", 0.1f);

    AITaskComponent aiTaskComponent = new AITaskComponent()
            .addTask(new MobTask(MobType.WATER_SLIME));

    waterSlime
            .addComponent(new CombatStatsComponent(health, 0, drops))
            .addComponent(animator)
            .addComponent(new WaterSlimeAnimationController())
            .addComponent(aiTaskComponent);

    waterSlime.getComponent(HitboxComponent.class).setAsBoxAligned(new Vector2(.3f, .5f), PhysicsComponent.AlignX.RIGHT, PhysicsComponent.AlignY.CENTER);
    waterSlime.getComponent(AnimationRenderComponent.class).scaleEntity();

    return waterSlime;
  }
  /**
   * Creates a fire worm entity.
   *
   * @return entity
   */
  public static Entity createFireWorm(int health) {
    Entity fireWorm = createBaseNPC();
    ArrayList<Currency> drops = new ArrayList<>();

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/mobs/fire_worm.atlas", TextureAtlas.class));
    animator.addAnimation("fire_worm_walk", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("fire_worm_attack", 0.1f);
    animator.addAnimation("fire_worm_death", 0.1f);
    animator.addAnimation("fire_worm_freeze", 0.1f);
    animator.addAnimation("default", 0.1f);

    AITaskComponent aiTaskComponent = new AITaskComponent()
            .addTask(new MobTask(MobType.FIRE_WORM));

    fireWorm
            .addComponent(new CombatStatsComponent(health, 0, drops))
            .addComponent(animator)
            .addComponent(new FireWormAnimationController())
            .addComponent(aiTaskComponent);

    fireWorm.getComponent(HitboxComponent.class).setAsBoxAligned(new Vector2(.3f, .5f), PhysicsComponent.AlignX.RIGHT, PhysicsComponent.AlignY.CENTER);
    fireWorm.getComponent(AnimationRenderComponent.class).scaleEntity();

    return fireWorm;
  }
  /**
   * Creates a dragon Knight entity
   *
   * @return entity
   */
  public static Entity createDragonKnight(int health) {
    Entity dragonKnight = createBaseNPC();
    ArrayList<Currency> drops = new ArrayList<>();

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/mobs/dragon_knight.atlas", TextureAtlas.class));
    animator.addAnimation("dragon_knight_run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("dragon_knight_attack", 0.1f);
    animator.addAnimation("dragon_knight_death", 0.1f);
    animator.addAnimation("dragon_knight_freeze", 0.1f);
    animator.addAnimation("default", 0.1f);

    AITaskComponent aiTaskComponent = new AITaskComponent()
            .addTask(new MobTask(MobType.DRAGON_KNIGHT));

    dragonKnight
            .addComponent(new CombatStatsComponent(health, 0, drops))
            .addComponent(animator)
            .addComponent(new DragonKnightAnimationController())
            .addComponent(aiTaskComponent);

    dragonKnight.getComponent(HitboxComponent.class).setAsBoxAligned(new Vector2(.3f, .5f), PhysicsComponent.AlignX.RIGHT, PhysicsComponent.AlignY.BOTTOM);
    dragonKnight.getComponent(AnimationRenderComponent.class).scaleEntity();

    return dragonKnight;
  }

  public static Entity createCoat(int health) {
    Entity coat = createBaseNPC();
    ArrayList<Currency> drops = new ArrayList<>();

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/mobs/coat.atlas", TextureAtlas.class));
    animator.addAnimation("coat_run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("coat_attack", 0.1f);
    animator.addAnimation("coat_death", 0.1f);
    animator.addAnimation("default", 0.1f);

    AITaskComponent aiTaskComponent = new AITaskComponent()
            .addTask(new MobTask(MobType.COAT));

    coat
            .addComponent(new CombatStatsComponent(health, 0, drops))
            .addComponent(animator)
            .addComponent(new CoatAnimationController())
            .addComponent(aiTaskComponent);

    coat.getComponent(HitboxComponent.class).setAsBoxAligned(new Vector2(.3f, .5f), PhysicsComponent.AlignX.RIGHT, PhysicsComponent.AlignY.BOTTOM);
    coat.getComponent(AnimationRenderComponent.class).scaleEntity();

    return coat;
  }

  public static Entity createNightBorne(int health) {
    Entity coat = createBaseNPC();
    ArrayList<Currency> drops = new ArrayList<>();

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/mobs/night_borne.atlas", TextureAtlas.class));
    animator.addAnimation("night_borne_run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("night_borne_attack", 0.1f);
    animator.addAnimation("night_borne_death", 0.1f);
    animator.addAnimation("default", 0.1f);

    AITaskComponent aiTaskComponent = new AITaskComponent()
            .addTask(new MobTask(MobType.NIGHT_BORNE));

    coat
            .addComponent(new CombatStatsComponent(health, 0, drops))
            .addComponent(animator)
            .addComponent(new NightBorneAnimationController())
            .addComponent(aiTaskComponent);

    coat.getComponent(HitboxComponent.class).setAsBoxAligned(new Vector2(.3f, .5f), PhysicsComponent.AlignX.RIGHT, PhysicsComponent.AlignY.BOTTOM);
    coat.getComponent(AnimationRenderComponent.class).scaleEntity();

    return coat;
  }

  public static Entity createArcaneArcher(int health) {
    Entity coat = createBaseNPC();
    ArrayList<Currency> drops = new ArrayList<>();

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/mobs/arcane_archer.atlas", TextureAtlas.class));
    animator.addAnimation("arcane_archer_run", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("arcane_archer_attack", 0.1f);
    animator.addAnimation("arcane_archer_death", 0.1f);
    animator.addAnimation("arcane_archer_dodge", 0.1f);
    animator.addAnimation("default", 0.1f);

    AITaskComponent aiTaskComponent = new AITaskComponent()
            .addTask(new MobTask(MobType.ARCANE_ARCHER));

    coat
            .addComponent(new CombatStatsComponent(health, 0, drops))
            .addComponent(animator)
            .addComponent(new ArcaneArcherAnimationController())
            .addComponent(aiTaskComponent);

    coat.getComponent(HitboxComponent.class).setAsBoxAligned(new Vector2(.3f, .5f), PhysicsComponent.AlignX.RIGHT, PhysicsComponent.AlignY.BOTTOM);
    coat.getComponent(AnimationRenderComponent.class).scaleEntity();

    return coat;
  }

  public static Entity createGregRangeMob(int health) {
    Entity fireWorm = createBaseNPC();
    ArrayList<Currency> drops = new ArrayList<>();

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/mobs/fire_worm.atlas", TextureAtlas.class));
    animator.addAnimation("fire_worm_walk", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("fire_worm_attack", 0.1f);
    animator.addAnimation("fire_worm_death", 0.1f);
    animator.addAnimation("default", 0.1f);

    AITaskComponent aiTaskComponent = new AITaskComponent()
            .addTask(new MobTask(MobType.FIRE_WORM));

    fireWorm
            .addComponent(new CombatStatsComponent(health, 0, drops))
            .addComponent(animator)
            .addComponent(new FireWormAnimationController())
            .addComponent(aiTaskComponent);

    fireWorm.getComponent(HitboxComponent.class).setAsBoxAligned(new Vector2(.3f, .5f), PhysicsComponent.AlignX.RIGHT, PhysicsComponent.AlignY.CENTER);
    fireWorm.getComponent(AnimationRenderComponent.class).scaleEntity();

    return fireWorm;
  }


  /**
   * Creates a xeno grunt entity.
   *
   * @return entity
   */
  public static Entity createXenoGrunt(int health) {
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
    animator.addAnimation("xeno_freeze", 0.1f);
    animator.addAnimation("default", 0.1f);
    xenoGrunt
            .addComponent(new CombatStatsComponent(health, config.baseAttack, drops, melee, projectiles))
//            .addComponent(new CombatStatsComponent(config.fullHeath, config.baseAttack, drops, melee, projectiles))
            .addComponent(animator)
            .addComponent(new XenoAnimationController());

    xenoGrunt.getComponent(HitboxComponent.class).setAsBoxAligned(new Vector2(.3f, .5f), PhysicsComponent.AlignX.RIGHT, PhysicsComponent.AlignY.BOTTOM);
    xenoGrunt.getComponent(AnimationRenderComponent.class).scaleEntity();

    return xenoGrunt;
  }

  public static Entity createBaseNPC() {
    Entity npc =
            new Entity()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent())
                    .addComponent(new ColliderComponent())
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                    .addComponent(new TouchAttackComponent(PhysicsLayer.HUMANS));
    PhysicsUtils.setScaledCollider(npc, 0.3f, 0.5f);
    return npc;
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
        //     .addTask(new MobAttackTask(2, 2f));
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
        //     .addTask(new MobAttackTask(2, 2f));
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
  public static Entity createSplittingXenoGrunt(int health) {
    Entity splitXenoGrunt = createXenoGrunt(health)
        // add the scaling yourself. can also scale the X and Y component,
        // leading to some very interesting mob designs.
        .addComponent(new SplitMoblings(7, 0.5f))
        .addComponent(new DodgingComponent(PhysicsLayer.PROJECTILE, 0.25f));

    // * TEMPORARY TESTING FOR PROJECTILE DODGING
    splitXenoGrunt.getComponent(AITaskComponent.class).addTask(new MobDodgeTask(MobType.DRAGON_KNIGHT, 5));
    // splitXenoGrunt.getComponent(AITaskComponent.class).addTask(new MobDodgeTask(new Vector2(2f, 2f), 2f, 5));
    return splitXenoGrunt;
  }

  /**
   * Create Splitting water slime 
   * 
   * @require Entity to have a "splitDeath"
   * @return
   */
  public static Entity createSplittingWaterSlime(int health) {
    Entity splitWaterSlime = createBaseWaterSlime(health)

        .addComponent(new SplitMoblings(7, 0.5f));
        
    return splitWaterSlime;
  }

  /**
   * Create Splitting night borne
   * 
   * @require Entity to have a "splitDeath"
   * @return
   */
  public static Entity createSplittingNightBorne(int health) {
    Entity splitWaterSlime = createNightBorne(health)

        .addComponent(new SplitMoblings(7, 0.5f));
        
    return splitWaterSlime;
  }

  /**
   * Create a dodging Dragon Knight
   * 
   * @return
   */
  public static Entity createDodgingDragonKnight(int health) {
    Entity dodgeKnight = createDragonKnight(health);

    dodgeKnight.addComponent(new DodgingComponent(PhysicsLayer.PROJECTILE, 0.25f));
    // dodgeKnight.getComponent(AITaskComponent.class).addTask(new MobDodgeTask(new Vector2(2f, 2f), 2f, 5));
    dodgeKnight.getComponent(AITaskComponent.class).
    addTask(new MobDodgeTask(MobType.DRAGON_KNIGHT, 5));
    PhysicsUtils.setScaledCollider(dodgeKnight, 0.3f, 0.7f);
    dodgeKnight.setScale(0.3f, 0.7f);

    return dodgeKnight;
  }

  /**
   * Create a dodging Arcane Archer
   * 
   * @return
   */
  public static Entity createDodgingArcaneArcher(int health) {
    Entity dodgeKnight = createArcaneArcher(health);

    dodgeKnight.addComponent(new DodgingComponent(PhysicsLayer.PROJECTILE, 0.25f));
    // dodgeKnight.getComponent(AITaskComponent.class).addTask(new MobDodgeTask(new Vector2(2f, 2f), 2f, 5));
    dodgeKnight.getComponent(AITaskComponent.class).
    addTask(new MobDodgeTask(MobType.DRAGON_KNIGHT, 5));
    PhysicsUtils.setScaledCollider(dodgeKnight, 0.3f, 0.7f);
    dodgeKnight.setScale(0.3f, 0.7f);

    return dodgeKnight;
  }

//  public static Entity createDeflectXenoGrunt(int health) {
//    Entity deflectXenoGrunt = createXenoGrunt(health);
//    deflectXenoGrunt.addComponent(new DeflectingComponent(
  /**
   * Creates a wizard that can deflect bullets
   * @return
   */
  public static Entity createDeflectWizard(int health) {
    Entity deflectWizard = createWizard(health);
    deflectWizard.addComponent(new DeflectingComponent(
        PhysicsLayer.PROJECTILE, PhysicsLayer.TOWER, 10));

    return deflectWizard;
  }
}


