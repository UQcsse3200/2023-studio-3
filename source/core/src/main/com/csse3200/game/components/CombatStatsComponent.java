package com.csse3200.game.components;

import com.csse3200.game.entities.Melee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Random;

/**
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage it combat should have an instance of this class registered. This class can be
 * extended for more specific combat needs.
 */
public class CombatStatsComponent extends Component {

  private static final Logger logger = LoggerFactory.getLogger(CombatStatsComponent.class);
  private int health;
  private int baseAttack;
  private int speed;
  private final int fullHealth;
  private String state;
  private ArrayList<String> views;
  private ArrayList<Integer> drops;
  private ArrayList<Melee> closeRangeAbilities;
  private ArrayList<String> longRangeAbilities; //TODO change String to Projectiles

  public CombatStatsComponent(int health, int baseAttack) {
    setHealth(health);
    setBaseAttack(baseAttack);
    this.fullHealth = health;
  }

  public CombatStatsComponent(int health, int baseAttack, int speed, ArrayList<String> views, ArrayList<Integer> drops, ArrayList<Melee> closeRangeAbilities, ArrayList<String> longRangeAbilities) {
    setHealth(health);
    this.fullHealth = health;
    setBaseAttack(baseAttack);
    this.speed = speed;
    if (views.size() < 3) {
        throw new IllegalArgumentException("Enemy must have at least 3 views");
    }
    this.views = views;
    this.state = views.get(0);
    this.drops = drops;
    this.closeRangeAbilities = closeRangeAbilities;
    this.longRangeAbilities = longRangeAbilities;
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

    processState();

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
      processState();
  }

  /**
   * Returns the entity's base attack damage.
   *
   * @return base attack damage
   */
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

  public void hit(CombatStatsComponent attacker) {
    int newHealth = getHealth() - attacker.getBaseAttack();
    setHealth(newHealth);
  }

  /**
   * pick a random number from range 0 to the size of the list provided
   * */
  public int pickRandom(ArrayList pickFrom) {
      Random rand = new Random();
      return rand.nextInt(pickFrom.size());
  }

  public Integer drop() {
      return this.drops.get(pickRandom(this.drops));
  }

  //TODO change to detect if it is close range or long range
  public void attack() {

//      String ability = this.abilities.get(pickRandom(this.abilities));
    }

    public void processState() {
        if (this.health <= (this.fullHealth * 0.33)) {
            this.state = this.views.get(2);
        } else if (this.health <= (this.fullHealth * 0.66)) {
            this.state = this.views.get(1);
        }
    }
}
