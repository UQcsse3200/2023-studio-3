package com.csse3200.game.components.tasks.waves;

/**
 * This class represent a tuple of a String and an int which when used represent
 * a mob name and health. This is used to reduce the amount of HashMaps and
 * for simplicity and not confusing the data in the structure. It can be expanded to
 * include more information if required.
 * */

public class Tuple {

  public String mob;

  public int value;

  public Tuple(String mob, int value) {
    this.mob = mob;
    this.value = value;
  }

  @Override
  public String toString(){
    return "Mob: " + mob + " Health: " + value;
  }
}
