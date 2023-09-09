package com.csse3200.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.tasks.TrajectTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Ricochet based on target layers.
 * Basically a bouncing effect that continues to bounce off entities
 * Possible extensions: Have a limitied amount of bounce until self-destruct.
 */
public class RicochetComponent extends Component {
  private short targetLayer;
  private HitboxComponent hitBoxComponent;

  public RicochetComponent(short targetLayer) {
    this.targetLayer = targetLayer;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);
    hitBoxComponent = entity.getComponent(HitboxComponent.class);
  }
  
  /**
   * After collision ends, make another fireball that spawns just before the original one. This assumes
   * that the original fireball is already deleted. Set TouchAttackComponent disposeOnHit to true.
   * @param me
   * @param other
   */
  private void onCollisionEnd(Fixture me, Fixture other) {
    if(hitBoxComponent.getFixture() != me) return;
    
    if(!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) return;
    
    Entity projectile = ((BodyUserData) me.getBody().getUserData()).entity;

    Entity newProjectile = ProjectileFactory.createRicochetFireball(PhysicsLayer.NPC, new Vector2(100, projectile.getPosition().y + getRandomNumFrom(-250, 250)), new Vector2(2f, 2f));
    newProjectile.setPosition((float) (projectile.getPosition().x -1.5), (float) (projectile.getPosition().y));

    // projectile.getComponent(AITaskComponent.class).addTask(new TrajectTask(new Vector2(100, projectile.getPosition().y + 50)));
    
    
    ServiceLocator.getEntityService().register(newProjectile);
  }

  private int getRandomNumFrom(int min, int max) {
    return (int) (Math.random() * (max - min) + min);
  }
}
