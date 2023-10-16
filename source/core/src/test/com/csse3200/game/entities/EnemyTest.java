package com.csse3200.game.entities;


import static org.junit.jupiter.api.Assertions.*;
import com.csse3200.game.entities.configs.BaseEnemyConfig;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;


@ExtendWith(GameExtension.class)
class EnemyTest {

    private ArrayList<Currency> drops = new ArrayList<>();
//    private ArrayList<Currency> drops = new ArrayList<>(Arrays.asList(1,2));

    private ArrayList<String> views = new ArrayList<>(Arrays.asList("a", "b", "c"));

    private ArrayList<String> abilities = new ArrayList<>(Arrays.asList("c"));

    private String configString = "Enemy: %s Drops: %s Close Range: %s Long Range: %s" +
            " Speed: %s Full Health: %s Current Health: %s Base Attack: %s";
    private String baseConfigString = "Enemy: %s Drops: %s Close Range: %s Long Range: %s " +
            "Speed: 1 Full Health: 1 Current Health: 1 Base Attack: 0";

    private ArrayList<Melee> closeRange = new ArrayList<>(Arrays.asList(
            new Melee(3, 1, "fire", 1, 1)
    ));

    private ArrayList<Weapon> longRange = new ArrayList<>(Arrays.asList(
            new Melee(6, 1, "fire", 1, 1)
    ));

    // Check that both constructors create an enemy with the correct values
    @Test
    void shouldCreateEnemy() {
        BaseEnemyConfig enemyB = new BaseEnemyConfig(drops, closeRange, longRange);
        int id = enemyB.getId();

        BaseEnemyConfig enemy = new BaseEnemyConfig(drops, closeRange, longRange);
        assertEquals(String.format(baseConfigString, id + 1, drops, closeRange, longRange),
                enemy.toString());

        BaseEnemyConfig enemy2 = new BaseEnemyConfig(
                4, 5, drops, closeRange, longRange, 6);

        assertEquals(
                String.format(configString, id + 2, drops, closeRange, longRange, "4", "5", "5", "6"),
                enemy2.toString());
    }

    // Check the generated ids are unique and are incremented correctly
    @Test
    void idGeneration() {
        BaseEnemyConfig enemy1 = new BaseEnemyConfig(drops, closeRange, longRange);

        BaseEnemyConfig enemy2 = new BaseEnemyConfig(drops, closeRange, longRange);
        assertNotEquals(enemy1.getId(), enemy2.getId());
        assertEquals(enemy1.getId() + 1, enemy2.getId());
    }

}
