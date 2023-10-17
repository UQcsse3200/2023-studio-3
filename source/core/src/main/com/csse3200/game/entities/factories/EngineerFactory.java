package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.EffectComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.player.HumanAnimationController;
import com.csse3200.game.components.tasks.human.HumanWanderTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.*;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * Factory to create non-playable human character (NPC) entities with predefined components.
 *
 * These may be modified to become controllable characters in future sprints.
 *
 * <p>Each NPC entity type should have a creation method that returns a corresponding entity.
 * Predefined entity properties can be loaded from configs stored as json files which are defined in
 * "NPCConfigs".
 *
 * <p>If needed, this factory can be separated into more specific factories for entities with
 * similar characteristics.
 */
public class EngineerFactory {
  
  private static final int COMBAT_TASK_PRIORITY = 2;
  private static final int ENGINEER_RANGE = 10;
  private static final EngineerConfigs configs =
          FileLoader.readClass(EngineerConfigs.class, "configs/Engineers.json");
  
  private static final float HUMAN_SCALE_X = 1f;
  private static final float HUMAN_SCALE_Y = 0.8f;
  
  /**
   * Creates an Engineer entity, based on a base Human entity, with the appropriate components and animations
   *
   *
   * @return entity
   */
  public static Entity createEngineer() {
    Entity engineer = createBaseHumanNPC();
    BaseEntityConfig config = configs.engineer;
    
    AITaskComponent aiComponent = new AITaskComponent();
    AnimationRenderComponent animator = createAnimationRenderComponent();
    
    engineer
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(animator)
            .addComponent(new HumanAnimationController())
            .addComponent(aiComponent);
    
    engineer.getComponent(AITaskComponent.class).addTask(new HumanWanderTask(COMBAT_TASK_PRIORITY, ENGINEER_RANGE));
    engineer.getComponent(AnimationRenderComponent.class).scaleEntity();
    engineer.setScale(HUMAN_SCALE_X, HUMAN_SCALE_Y);
    return engineer;
  }
  
  public static AnimationRenderComponent createAnimationRenderComponent() {
    String atlasPath = "images/engineers/engineer.atlas";
    String atlasOutlinePath = "images/engineers/engineer_outline.atlas";
    AnimationRenderComponent animator = new AnimationRenderComponent(
            new TextureAtlas(atlasPath), new TextureAtlas(atlasOutlinePath));
    animator.addAnimation("walk_left", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("walk_right", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("walk_prep", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("idle_right", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("firing_auto", 0.05f, Animation.PlayMode.NORMAL);
    animator.addAnimation("firing_single", 0.05f, Animation.PlayMode.NORMAL);
    animator.addAnimation("prep", 0.05f, Animation.PlayMode.NORMAL);
    animator.addAnimation("hit", 0.01f, Animation.PlayMode.NORMAL);
    animator.addAnimation("death", 0.1f, Animation.PlayMode.NORMAL);

    animator.addAnimation("walk_left_outline", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("walk_right_outline", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("walk_prep_outline", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("idle_right_outline", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("firing_auto_outline", 0.05f, Animation.PlayMode.NORMAL);
    animator.addAnimation("firing_single_outline", 0.05f, Animation.PlayMode.NORMAL);
    animator.addAnimation("prep_outline", 0.05f, Animation.PlayMode.NORMAL);
    animator.addAnimation("hit_outline", 0.01f, Animation.PlayMode.NORMAL);
    animator.addAnimation("death_outline", 0.1f, Animation.PlayMode.NORMAL);
    return animator;
  }
  
  /**
   * Creates a generic human npc to be used as a base entity by more specific NPC creation methods.
   *
   * @return entity
   */
  public static Entity createBaseHumanNPC() {
    
    
    Entity human =
            new Entity()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent())
                    .addComponent(new ColliderComponent())
                    .addComponent(new EffectComponent(false))
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.ENGINEER))
                    .addComponent(new TouchAttackComponent(PhysicsLayer.NPC, 1.5f));
    
    
    PhysicsUtils.setScaledCollider(human, 0.9f, 0.4f);
    return human;
  }
  
  private EngineerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}