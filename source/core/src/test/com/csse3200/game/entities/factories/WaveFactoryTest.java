package com.csse3200.game.entities.factories;

import com.csse3200.game.components.tasks.waves.LevelWaves;
import com.csse3200.game.components.tasks.waves.WaveClass;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.screens.GameLevelData;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.WaveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.csse3200.game.entities.Entity;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class WaveFactoryTest {

  private LevelWaves lvl1;
  private LevelWaves lvl2;
  private LevelWaves lvl3;

  private final int MIN_MELEE_HEALTH = 80;
  private final int MIN_RANGE_HEALTH = 60;
  private final int MIN_BOSS_HEALTH = 80;
  private final int LVL1_LVL = 1;
  private final int LVL2_LVL = 2;
  private final int LVL3_LVL = 3;
  private static final ArrayList<String> MELEE_MOBS = new ArrayList<>(Arrays.asList(
      "Skeleton", "Coat", "DragonKnight", "Necromancer"
  ));

  private static final ArrayList<ArrayList<String>> LVL1_WAVES_STRUC = new ArrayList<>(Arrays.asList(
      new ArrayList<>(Arrays.asList("Coat"
      )), new ArrayList<>(Arrays.asList("Coat", "WaterQueen"
      )), new ArrayList<>(Arrays.asList("WaterQueen", "SplittingWaterSlime"
      )), new ArrayList<>(Arrays.asList("Coat", "WaterQueen", "SplittingWaterSlime"
      ))
  ));

  private static final ArrayList<ArrayList<String>> LVL2_WAVES_STRUC = new ArrayList<>(Arrays.asList(
      new ArrayList<>(Arrays.asList("Skeleton"
      )), new ArrayList<>(Arrays.asList("Skeleton", "ArcaneArcher"
      )), new ArrayList<>(Arrays.asList("Skeleton", "Wizard"
      )), new ArrayList<>(Arrays.asList("Skeleton", "SplittingNightBorne"
      )), new ArrayList<>(Arrays.asList("Wizard", "SplittingNightBorne"
      )), new ArrayList<>(Arrays.asList("SplittingNightBorne", "Skeleton"
      )), new ArrayList<>(Arrays.asList("Wizard", "SplittingNightBorne"
      )), new ArrayList<>(Arrays.asList("ArcaneArcher", "SplittingNightBorne", "Wizard"
      )), new ArrayList<>(Arrays.asList("Skeleton", "ArcaneArcher", "Wizard", "SplittingNightBorne"
      ))
  ));

  private static final ArrayList<ArrayList<String>> LVL3_WAVES_STRUC = new ArrayList<>(Arrays.asList(
      new ArrayList<>(Arrays.asList("Necromancer"
      )), new ArrayList<>(Arrays.asList("Necromancer", "DodgingDragon"
      )), new ArrayList<>(Arrays.asList("Necromancer", "FireWorm"
      )), new ArrayList<>(Arrays.asList("Necromancer", "DeflectFireWizard"
      )), new ArrayList<>(Arrays.asList("DeflectFireWizard", "FireWorm"
      )), new ArrayList<>(Arrays.asList("DodgingDragon", "FireWorm"
      )), new ArrayList<>(Arrays.asList("DodgingDragon", "Necromancer"
      )), new ArrayList<>(Arrays.asList("FireWorm", "Necromancer"
      )), new ArrayList<>(Arrays.asList("DeflectFireWizard", "Necromancer"
      )), new ArrayList<>(Arrays.asList("DodgingDragon", "DeflectFireWizard", "Necromancer"
      )), new ArrayList<>(Arrays.asList("FireWorm", "Necromancer", "DodgingDragon"
      )), new ArrayList<>(Arrays.asList("FireWorm", "SplittingRocky", "Necromancer"
      )), new ArrayList<>(Arrays.asList("SplittingRocky", "DeflectFireWizard", "FireWorm"
      )), new ArrayList<>(Arrays.asList("DeflectFireWizard", "SplittingRocky", "Necromancer", "DodgingDragon", "FireWorm"
      ))
  ));

    private static final String[] waveSounds = {
            "sounds/waves/wave-start/Wave_Start_Alarm.ogg",
            "sounds/waves/wave-end/Wave_Over_01.ogg"
    };

    @BeforeEach
    void setUp() {
      GameTime gameTime = mock(GameTime.class);
      ServiceLocator.registerTimeSource(gameTime);
      ServiceLocator.registerPhysicsService(new PhysicsService());
      RenderService render = new RenderService();
      render.setDebug(mock(DebugRenderer.class));
      ServiceLocator.registerRenderService(render);
      ResourceService resourceService = mock(ResourceService.class);
      ServiceLocator.registerResourceService(resourceService);
      WaveService waveService = new WaveService();
      ServiceLocator.registerWaveService(waveService);
      ServiceLocator.getResourceService().loadSounds(waveSounds);

      lvl1 = WaveFactory.createLevel(LVL1_LVL);
      lvl2 = WaveFactory.createLevel(LVL2_LVL);
      lvl3 = WaveFactory.createLevel(LVL3_LVL);
    }

    @Test
    void createBaseWaves() {
      GameLevelData.setSelectedLevel(0);
      Entity level1 = WaveFactory.createWaves();
      assertNotNull(level1);

      GameLevelData.setSelectedLevel(1);
      Entity level2 = WaveFactory.createWaves();
      assertNotNull(level2);

      GameLevelData.setSelectedLevel(2);
      Entity level3 = WaveFactory.createWaves();
      assertNotNull(level3);
    }

    @Test
    void testCreateLevel() {
      assertNotNull(lvl1);
      assertNotNull(lvl2);
      assertNotNull(lvl3);
    }

    /**
     * The three following tests ensure that every wave in the level is created correctly
     * Since the waves are stored in a hashmap, by definition the mobs are unique and this
     * quality does not have to be checked.
     * */
    @Test
    void testLevel1Creation() {
      List<WaveClass> lvl1Mobs = lvl1.getWaves();
      int bossHealth = 500;
      int mobCount = 5;

      int waveNum = 1;
      for (WaveClass wave : lvl1Mobs) {
        int mobsRemaining = wave.getEntities().size() - 1;
        int totalMobsSpawned = 0;

        // check the number of mobs in a wave
        if (waveNum == 5 || waveNum == 1) {
          assertEquals(1, wave.getEntities().size(), "Wave should contain 1 mob.");
        } else if (waveNum == 2 || waveNum == 3) {
          assertEquals(2, wave.getEntities().size(), "Wave should contain 2 mobs.");
        } else {
          assertEquals(3, wave.getEntities().size(), "Wave should contain 3 mobs.");
        }


        if (waveNum != 5) {
          Set<String> mobNames = new HashSet<>(wave.getEntities().keySet());
          Set<String> expectedMobNames = new HashSet<>(LVL1_WAVES_STRUC.get(waveNum - 1));
          assertEquals(mobNames, expectedMobNames, "The mobs in the wave should be " + expectedMobNames + " .");

          for (String key: wave.getEntities().keySet()) {
            int[] values = wave.getEntities().get(key);
            assertTrue(values[0] > 1 && values[0] <= (mobCount - totalMobsSpawned - (2 * mobsRemaining)));
            totalMobsSpawned += values[0];
            mobsRemaining --;

            if (MELEE_MOBS.contains(key)) {
              assertEquals(values[1], MIN_MELEE_HEALTH + waveNum, "The health of the mob should be " + MIN_MELEE_HEALTH + waveNum + " .");
            } else {
              assertEquals(values[1], MIN_RANGE_HEALTH + waveNum, "The health of the mob should be " + MIN_RANGE_HEALTH + waveNum + " .");
            }
          }
        } else {
          assertEquals(wave.getEntities().keySet().size(), 1);
          for (String key: wave.getEntities().keySet()) {
            int[] values = wave.getEntities().get(key);
            assertEquals(values[1], bossHealth, "The health of the boss should be " + MIN_BOSS_HEALTH);
          }
        }
        mobCount ++;
        waveNum++;
      }
      assertEquals(6, waveNum, "The should be 5 waves making numWave 6.");
    }
    @Test
    void testLevel2Creation() {

      List<WaveClass> lvl2Mobs = lvl2.getWaves();
      int bossHealth = 1000;
      int mobCount = 6;

      int waveNum = 1;
      for (WaveClass wave : lvl2Mobs) {
        int mobsRemaining = wave.getEntities().size() - 1;
        int totalMobsSpawned = 0;

        // check the number of mobs in a wave
        if (waveNum == 1 || waveNum == 10) {
          assertEquals(1, wave.getEntities().size(), "Wave should contain 1 mob.");
        } else if (1 < waveNum && waveNum < 8) {
          assertEquals(2, wave.getEntities().size(), "Wave should contain 2 mobs.");
        } else if (waveNum == 8){
          assertEquals(3, wave.getEntities().size(), "Wave should contain 3 mobs.");
        } else {
          assertEquals(4, wave.getEntities().size(), "Wave should contain 4 mobs.");
        }

        if (waveNum != 10) {
          Set<String> mobNames = new HashSet<>(wave.getEntities().keySet());
          Set<String> expectedMobNames = new HashSet<>(LVL2_WAVES_STRUC.get(waveNum - 1));
          assertEquals(mobNames, expectedMobNames, "The mobs in the wave should be " + expectedMobNames + " .");

          for (String key: wave.getEntities().keySet()) {
            int[] values = wave.getEntities().get(key);

            assertTrue(values[0] > 1 && values[0] <= (mobCount - totalMobsSpawned - (2 * mobsRemaining)));
            totalMobsSpawned += values[0];
            mobsRemaining --;
            System.out.println("wave is: "+ wave);

            if (MELEE_MOBS.contains(key)) {
              System.out.println("the health is: " + values[1]);
              System.out.println("I want it to be: " + (MIN_MELEE_HEALTH + (waveNum * 2)));

              assertEquals(values[1], (MIN_MELEE_HEALTH + (waveNum * 2)), "The health of the mob should be " + (MIN_MELEE_HEALTH + (waveNum * 2)) + " .");
            } else {
              assertEquals(values[1], (MIN_RANGE_HEALTH + (waveNum * 2)), "The health of the mob should be " + (MIN_RANGE_HEALTH + (waveNum * 2)) + " .");
            }
          }

        } else {
          assertTrue(wave.getEntities().keySet().size() == 1);
          for (String key: wave.getEntities().keySet()) {
            int[] values = wave.getEntities().get(key);
            assertEquals(values[1], bossHealth, "The health of the boss should be " + MIN_BOSS_HEALTH);
          }
        }
        mobCount ++;
        waveNum++;
      }
      assertEquals(11, waveNum, "There should be 10 waves making numWave 11.");
    }
    @Test
    void testLevel3Creation() {

      List<WaveClass> lvl3Mobs = lvl3.getWaves();

      int bossHealth = 2000;
      int mobCount = 8;

      int waveNum = 1;
      for (WaveClass wave : lvl3Mobs) {
        int mobsRemaining = wave.getEntities().size() - 1;
        int totalMobsSpawned = 0;

        // check the number of mobs in a wave
        if (waveNum == 1 || waveNum == 15) {
          assertEquals(1, wave.getEntities().size(), "Wave should contain 1 mob.");
        } else if (1 < waveNum && waveNum < 10) {
          assertEquals(2, wave.getEntities().size(), "Wave should contain 2 mobs.");
        } else if (9 < waveNum && waveNum < 14){
          assertEquals(3, wave.getEntities().size(), "Wave should contain 3 mobs.");
        } else if (waveNum == 14){
          assertEquals(5, wave.getEntities().size(), "Wave should contain 4 mobs.");
        }

        if (waveNum != 15) {
          Set<String> mobNames = new HashSet<>(wave.getEntities().keySet());
          Set<String> expectedMobNames = new HashSet<>(LVL3_WAVES_STRUC.get(waveNum - 1));
          assertEquals(mobNames, expectedMobNames, "The mobs in the wave should be " + expectedMobNames + " .");

          for (String key: wave.getEntities().keySet()) {
            int[] values = wave.getEntities().get(key);

            assertTrue(values[0] > 1 && values[0] <= (mobCount - totalMobsSpawned - (2 * mobsRemaining)));
            totalMobsSpawned += values[0];
            mobsRemaining --;

            if (MELEE_MOBS.contains(key)) {
              assertEquals(values[1], (MIN_MELEE_HEALTH + (waveNum * 3)), "The health of the mob should be " + (MIN_MELEE_HEALTH + (waveNum * 2)) + " .");
            } else {
              assertEquals(values[1], (MIN_RANGE_HEALTH + (waveNum * 3)), "The health of the mob should be " + (MIN_RANGE_HEALTH + (waveNum * 2)) + " .");
            }
          }

        } else {
          assertEquals(wave.getEntities().keySet().size(), 1);
          for (String key: wave.getEntities().keySet()) {
            int[] values = wave.getEntities().get(key);
            assertEquals(values[1], bossHealth, "The health of the boss should be " + MIN_BOSS_HEALTH);
          }
        }
        mobCount ++;
        waveNum++;
      }
      assertEquals(16, waveNum, "There should be 15 waves making numWave 16.");
    }

}
