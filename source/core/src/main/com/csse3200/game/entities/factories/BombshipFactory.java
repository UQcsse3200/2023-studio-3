package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.player.BombShipAnimationController;
import com.csse3200.game.components.tasks.bombship.BombshipWanderTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.*;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * Factory to create non-playable human character (NPC) entities with predefined components.
 * These may be modified to become controllable characters in future sprints.
 *
 * <p>Each NPC entity type should have a creation method that returns a corresponding entity.
 * Predefined entity properties can be loaded from configs stored as json files which are defined in
 * "NPCConfigs".
 *
 * <p>If needed, this factory can be separated into more specific factories for entities with
 * similar characteristics.
 */
public class BombshipFactory {
  private static final int COMBAT_TASK_PRIORITY = 2;
  private static final int BOMBSHIP_RANGE = 30;
  private static final BombshipConfigs configs =
          FileLoader.readClass(BombshipConfigs.class, "configs/Bombship.json");
  private static final float HUMAN_SCALE_X = 1f;
  private static final float HUMAN_SCALE_Y = 0.8f;
  
  /**
   * Creates an Engineer entity, based on a base Human entity, with the appropriate components and animations
   *
   * @return entity
   */
  public static Entity createBombship() {
    Entity bombship = createBaseshipNPC();
    BaseEntityConfig config = configs.bombship;
    
    AnimationRenderComponent animator = new AnimationRenderComponent(
            new TextureAtlas("images/bombship/bombship.atlas"));
    animator.addAnimation("start", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("destroy", 0.1f, Animation.PlayMode.NORMAL);
    animator.addAnimation("idle", 0.2f, Animation.PlayMode.LOOP);
    AITaskComponent aiComponent = new AITaskComponent();
    
    bombship
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(animator)
            .addComponent(new BombShipAnimationController())
            .addComponent(aiComponent);
    
    bombship.getComponent(AITaskComponent.class).addTask(new BombshipWanderTask(COMBAT_TASK_PRIORITY, BOMBSHIP_RANGE));
    bombship.getComponent(AnimationRenderComponent.class).scaleEntity();
    bombship.setScale(HUMAN_SCALE_X, HUMAN_SCALE_Y);
    return bombship;
  }
  
  /**
   * Creates a generic human npc to be used as a base entity by more specific NPC creation methods.
   *
   * @return entity
   */
  public static Entity createBaseshipNPC() {
      return new Entity()
              .addComponent(new PhysicsComponent())
              .addComponent(new PhysicsMovementComponent())
              .addComponent(new ColliderComponent())
              .addComponent(new HitboxComponent().setLayer(PhysicsLayer.BOMBSHIP))
              .addComponent(new TouchAttackComponent(PhysicsLayer.NPC, 1.5f));
  }
  
  private BombshipFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}