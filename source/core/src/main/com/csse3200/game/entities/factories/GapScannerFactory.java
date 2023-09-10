package com.csse3200.game.entities.factories;


import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.tasks.scanner.ScannerTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;

/**
 * Factory to create scanner entities that determine whether to spawn engineer entities.
 * These do not interact with any of the entities in the game area except to detect other entities
 *
 */
public class GapScannerFactory {

  /**
   * Creates a scanner entity
   * @return scanner entity
   */
  public static Entity createScanner() {
    Entity scanner = new Entity();

    AITaskComponent aiComponent = new AITaskComponent();

    scanner
            .addComponent(new PhysicsComponent())
            .addComponent(aiComponent);

    scanner.getComponent(AITaskComponent.class).addTask(new ScannerTask());
    return scanner;
  }

  private GapScannerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
