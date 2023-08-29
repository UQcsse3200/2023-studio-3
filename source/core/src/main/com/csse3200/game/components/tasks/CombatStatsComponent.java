package com.csse3200.game.components;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.Melee;
import com.csse3200.game.entities.Weapon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Random;

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
public class CombatStatsComponent extends Component {

  private static final Logger logger = LoggerFactory.getLogger(CombatStatsComponent.class);
  private int health;
  private int baseAttack;
  private final int fullHealth;
  private String state;
  private ArrayList<Integer> drops;
  private ArrayList<Melee> closeRangeAbilities;
  private ArrayList<Weapon> longRangeAbilities; //TODO change String to Projectiles

  public CombatStatsComponent(int health, int baseAttack) {
    setHealth(health);
    setBaseAttack(baseAttack);
    this.fullHealth = health;
    this.state = "fullHealth";
  }

  public CombatStatsComponent(int health, int baseAttack,
                              ArrayList<Integer> drops,
                              ArrayList<Melee> closeRangeAbilities,
                              ArrayList<Weapon> longRangeAbilities) {
    setHealth(health);
    this.fullHealth = health;
    setBaseAttack(baseAttack);
    this.drops = drops;
    this.closeRangeAbilities = closeRangeAbilities;
    this.longRangeAbilities = longRangeAbilities;
    this.state = "fullHealth";
  }

  /**
   * Returns true if the entity's has 0 health, otherwise false.
   *
   * @return is player dead
   */
  public Boolean isDead() {
    return health == 0;
  }

  /**
   * Returns the entity's health.
   *
   * @return entity's health
   */
  public int getHealth() {
    return health;
  }

  /**
   * Sets the entity's health. Health has a minimum bound of 0.
   *
   * @param health health
   */
  public void setHealth(int health) {
    if (health >= 0) {
      this.health = health;
    } else {
      this.health = 0;
    }

    if (entity != null) {
      entity.getEvents().trigger("updateHealth", this.health);
    }
  }

  /**
   * Adds to the player's health. The amount added can be negative.
   *
   * @param health health to add
   */
  public void addHealth(int health) {
    setHealth(this.health + health);
    changeState();
  }

  /**
   * Returns the entity's base attack damage.
   *
   * @return base attack damage
   */
  public void hit(Integer damage) {
    int newHealth = getHealth() - damage;
    setHealth(newHealth);
    changeState();
  }
  public int getBaseAttack() {
    return baseAttack;
  }

  /**
   * Sets the entity's attack damage. Attack damage has a minimum bound of 0.
   *
   * @param attack Attack damage
   */
  public void setBaseAttack(int attack) {
    if (attack >= 0) {
      this.baseAttack = attack;
    } else {
      logger.error("Can not set base attack to a negative attack value");
    }
  }

  /**
   * Decrease the health of the entity based on the damage provided.
   * */

  /**
   * pick a random number from range 0 to the size of the list provided
   * */
  public int pickRandom(ArrayList pickFrom) {
    Random rand = new Random();
    return rand.nextInt(pickFrom.size());
  }

  //TODO: this will be changed to drop an item and load it to the screen
  public Integer drop() {
    return this.drops.get(pickRandom(this.drops));
  }

  /**
   * If there are long range attacks provided, return a random one, else
   * return null
   * */
  public Weapon longRangeAttack() {
    if (this.longRangeAbilities == null) {
      return null;
    }
    return this.longRangeAbilities.get(pickRandom(this.longRangeAbilities));
  }

  /**
   * Check if the target is within range of any of the close range attacks.
   * Return a list of close range attacks which can be used
   * */
  public ArrayList<Melee> withinRange(Entity target) {
    float distance = this.entity.getPosition().dst(target.getPosition());
    ArrayList<Melee> withinRange = new ArrayList<>();

    if (this.closeRangeAbilities == null) {
      return withinRange;
    }
    for (Melee melee : this.closeRangeAbilities) {
      if (distance <= melee.getAttackRange()) {
        withinRange.add(melee);
      }
    }
    return withinRange;
  }

  /**
   * Return a close range attack if the target is within range, else
   * return a long range attack
   * */
  public Weapon getWeapon(Entity target) {
    ArrayList<Melee> withinRange = withinRange(target);
    if (withinRange.size() > 0) {
      return withinRange.get(pickRandom(withinRange));
    }

    return longRangeAttack();
  }

  /**
   * Change the state of the enemy based on the health
   * */
  public void changeState() {
    if (this.health <= (this.fullHealth * 0.33)) {
      this.state = "lowHealth";
    } else if (this.health <= (this.fullHealth * 0.66)) {
      this.state = "midHealth";
    } else {
      this.state = "fullHealth";
    }
  }

  /**
   * Return the state of the enemy
   * fullHealth: above 66% of full health
   * midHealth: above 33% of full health
   * lowHealth: below 33% of full health
   * */
  public String getState() {
    return this.state;
  }
}
