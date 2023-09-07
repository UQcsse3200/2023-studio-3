package com.csse3200.game.components.player;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Melee;
import com.csse3200.game.entities.Weapon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage it combat should have an instance of this class registered. This class can be
 * extended for more specific combat needs.
 *
 * health: the current health of the entity
 * baseAttack: the base attack of the entity
 * fullHealth: the full health of the entity
 * state: the current state of the entity (full health above 66%, half health above 33%, low health below 33%)
 * drops: the items that the entity drops when it dies
 * closeRangeAbilities: the Melee abilities (close range) of the entity
 * longRangeAbilities: the Projectile abilities (long range) of the entity
 *
 */
public class HumanCombatStatsComponent extends CombatStatsComponent {

  public HumanCombatStatsComponent(int health, int baseAttack) {
    super(health, baseAttack);
  }

  /**
   * Decrease the health of the entity based on the damage provided.
   * */
  @Override
  public void hit(Integer damage) {
    int newHealth = getHealth() - damage;
    setHealth(newHealth);
    entity.getEvents().trigger("hitStart");
    changeState();
  }
}
