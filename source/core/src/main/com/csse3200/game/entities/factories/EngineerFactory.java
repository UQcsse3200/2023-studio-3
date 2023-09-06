package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.npc.GhostAnimationController;
import com.csse3200.game.components.npc.XenoAnimationController;
import com.csse3200.game.components.player.HumanAnimationController;
import com.csse3200.game.components.tasks.ShootTask;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.components.tasks.human.HumanWanderTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.Melee;
import com.csse3200.game.entities.PredefinedWeapons;
import com.csse3200.game.entities.Weapon;
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
  private static final EngineerConfigs configs =
      FileLoader.readClass(EngineerConfigs.class, "configs/Engineers.json");

  private static final float HUMAN_SCALE_X = 1.5f;
  private static final float HUMAN_SCALE_Y = 1.2f;

  /**
   * Creates an Engineer entity, based on a base Human entity, with the appropriate components and animations
   *
   *
   * @return entity
   */
  public static Entity createEngineer() {
    Entity engineer = createBaseHumanNPC();
    BaseEntityConfig config = configs.engineer;

    AnimationRenderComponent animator = new AnimationRenderComponent(
            new TextureAtlas("images/engineers/engineer.atlas"));
    animator.addAnimation("walk_left", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("walk_right", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("idle_right", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("firing", 0.1f, Animation.PlayMode.NORMAL);
    animator.addAnimation("hit", 0.1f, Animation.PlayMode.NORMAL);
    animator.addAnimation("death", 0.1f, Animation.PlayMode.NORMAL);


    engineer
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
        .addComponent(animator)
        .addComponent(new HumanAnimationController());

    engineer.getComponent(AnimationRenderComponent.class).scaleEntity();
    engineer.setScale(HUMAN_SCALE_X, HUMAN_SCALE_Y);
    return engineer;
  }

  /**
   * Creates a generic human npc to be used as a base entity by more specific NPC creation methods.
   *
   * @return entity
   */
  public static Entity createBaseHumanNPC() {
    AITaskComponent aiComponent =
        new AITaskComponent()
            .addTask(new HumanWanderTask(new Vector2(5, 10f), 2f));

    Entity human =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
            .addComponent(new TouchAttackComponent(PhysicsLayer.NPC, 1.5f))
            .addComponent(aiComponent);

    PhysicsUtils.setScaledCollider(human, 0.9f, 0.4f);
    return human;
  }

  private EngineerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}


