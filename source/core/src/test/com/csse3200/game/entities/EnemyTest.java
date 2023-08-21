package com.csse3200.game.entities;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.configs.BaseEnemyConfig;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;


@ExtendWith(GameExtension.class)
public class EnemyTest {

    private String configString = "Enemy: %s Abilities: %s Drops: %s Views: %s State: %s Speed: %s Full Health: %s Current Health: %s Base Attack: %s";
    private String baseConfigString = "Enemy: %s Abilities: %s Drops: %s Views: %s State: %s Speed: 1 Full Health: 1 Current Health: 1 Base Attack: 0";

    @Test
    void shouldCreateEnemy() {
        List<Integer> drops = List.of(1, 2);
        List<String> views = List.of("a", "b", "c");
        BaseEnemyConfig enemyB = new BaseEnemyConfig(drops, views, "c");
        int id = enemyB.getId();

        BaseEnemyConfig enemy = new BaseEnemyConfig(drops, views, "c");
        assertEquals(enemy.toString(), String.format(baseConfigString, id + 1, "c", drops, views, "a"));

        BaseEnemyConfig enemy2 = new BaseEnemyConfig(4, 5, drops, views, "c", 6);
        assertEquals(enemy2.toString(), String.format(configString, id + 2, "c", drops, views, "a", "4", "5", "5", "6"));
    }

    @Test
    void idGeneration() {
        List<Integer> drops = List.of(1, 2);
        List<String> views = List.of("a", "b", "c");
        BaseEnemyConfig enemy1 = new BaseEnemyConfig(drops, views, "c");
        BaseEnemyConfig enemy2 = new BaseEnemyConfig(drops, views, "c");
        assertNotEquals(enemy1.getId(), enemy2.getId());
    }

    @Test
    void notEnoughViewException() {
        List<Integer> drops = List.of(1, 2);
        List<String> views = List.of("a", "b");
        assertThrows(IllegalArgumentException.class, () -> {
            BaseEnemyConfig enemy1 = new BaseEnemyConfig(drops, views, "c");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            BaseEnemyConfig enemy2 = new BaseEnemyConfig(1, 1, drops, views, "c", 1);
        });

    }

    @Test
    void testTakeDamage() {}

    @Test
    void takeDamageToDie() {}

    @Test
    void testDamageStateChange() {}

    @Test
    void testDrop() {}

    @Test
    void dropRandom() {}

    @Test
    void testDie() {}



}
