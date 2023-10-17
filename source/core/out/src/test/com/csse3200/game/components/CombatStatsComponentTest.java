package com.csse3200.game.components;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(GameExtension.class)
class CombatStatsComponentTest {
  @Test
  void shouldSetGetHealth() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20);
    assertEquals(100, combat.getHealth());

    combat.setHealth(150);
    assertEquals(150, combat.getHealth());

    combat.setHealth(-50);
    assertEquals(0, combat.getHealth());
  }

  @Test
  void shouldCheckIsDead() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20);
    assertFalse(combat.isDead());

    combat.setHealth(0);
    assertTrue(combat.isDead());
  }

  @Test
  void shouldAddHealth() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20);
    combat.addHealth(-500);
    assertEquals(0, combat.getHealth());

    combat.addHealth(100);
    combat.addHealth(-20);
    assertEquals(80, combat.getHealth());
  }

  @Test
  void shouldSetGetBaseAttack() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20);
    assertEquals(20, combat.getBaseAttack());

    combat.setBaseAttack(150);
    assertEquals(150, combat.getBaseAttack());

    combat.setBaseAttack(-50);
    assertEquals(150, combat.getBaseAttack());
  }

    /* Check that the drop is Random. This test is based on probability and may return false.
       * If false, run again before making changes*/
  @Test
  void shouldBeRandom() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20);
    ArrayList<Integer> possible = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    ArrayList<Integer> dropped = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
        dropped.add(combat.pickRandom(possible));
    }
    assertFalse(Arrays.stream(dropped.toArray()).allMatch(t -> t == dropped.get(0)));
  }

    /* Check that changing the damage will change to the correct state */
    @Test
    void testDamageStateChange() {
        CombatStatsComponent stats = new CombatStatsComponent(100, 20);
        assertEquals("fullHealth", stats.getState());

        stats.hit(34);
        assertEquals("midHealth", stats.getState());

        stats.hit(33);
        assertEquals("lowHealth", stats.getState());
    }
}
