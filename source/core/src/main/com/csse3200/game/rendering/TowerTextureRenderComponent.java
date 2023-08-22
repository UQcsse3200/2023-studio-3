package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.services.ServiceLocator;

/** Render a static tower texture. */
// TODO: remove reimplentations of existing functionality and only declare overridden methods
public class TowerTextureRenderComponent extends RenderComponent {
  private final Texture texture;

  /**
   * @param type The type of tower to be instantiated.
   */
  public TowerTextureRenderComponent(String type) {
    this(ServiceLocator.getResourceService().getAsset("images/turret_deployed.png", Texture.class));

  }
//...
  /** @param texture Static tower texture to render. Will be scaled to the entity's scale. */
  public TowerTextureRenderComponent(Texture texture) {
    this.texture = texture;
  }

  /** Scale the entity to a width of 1 and a height matching the texture's ratio */
  public void scaleEntity() {
    entity.setScale(1f, (float) texture.getHeight() / texture.getWidth());
  }

  @Override
  protected void draw(SpriteBatch batch) {
    Vector2 position = entity.getPosition();
    Vector2 scale = entity.getScale();
    batch.draw(texture, position.x, position.y, scale.x, scale.y);
  }
}
