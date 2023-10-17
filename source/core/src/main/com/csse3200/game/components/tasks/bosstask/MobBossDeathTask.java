package com.csse3200.game.components.tasks.bosstask;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.DropFactory;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

//CODE IS REDUNDANT ///

/**
 * Task that prints a message to the terminal whenever it is called.
 */
public class MobBossDeathTask extends DefaultTask implements PriorityTask {
    private static final int INTERVAL = 1; // time interval to scan for towers in

    private final int priority;
    private Vector2 bossPosition = new Vector2(10f,10f);
    private GameTime timeSource;
    private long endTime;

    private int bossHealth;

    /**
     * @param priority Task priority when shooting (0 when not chasing).
     */
    public MobBossDeathTask(int priority) {
        this.priority = priority;
        timeSource = ServiceLocator.getTimeSource();
    }

    @Override
    public void start() {
        super.start();
        // gets starting health
        this.bossHealth = owner.getEntity().getComponent(CombatStatsComponent.class).getHealth();
        //sets mob position
        this.bossPosition = owner.getEntity().getCenterPosition();
        //sets endTime
        endTime = timeSource.getTime() + (INTERVAL * 500);
    }

    @Override
    public void update() {
        if (timeSource.getTime() >= endTime) {
            updateMobBossState();
            endTime = timeSource.getTime() + (INTERVAL * 1000);
        }
    }

    public void updateMobBossState() {

        bossHealth = owner.getEntity().getComponent(CombatStatsComponent.class).getHealth();
        // TODO: inset a bit that picks from a list of drop options and drops this

        if (bossIsDead(bossHealth)) {
            killboss();
            dropCurrency();
        }

    }

    @Override
    public int getPriority() {
        if (status == Status.ACTIVE) {
            return getActivePriority();
        }

        return getInactivePriority();
    }

    private int getActivePriority() {
        if (bossHealth > 0) {
            return -1;
        }
        return priority;
    }

    private int getInactivePriority() {
        if (bossHealth <= 0) {
            return priority;
        }
        return -1;
    }
    private boolean bossIsDead(int bosshealth) {
        return bosshealth <= 0;
    }

    private void killboss() {
        owner.getEntity().getEvents().trigger("boss_death");
        owner.getEntity().dispose();
    }

    private void dropCurrency() {
        // Create and register 5 crystal drops around the bossPosition
        for (int i = 0; i < 5; i++) {
            Entity crystal = DropFactory.createCrystalDrop();

            // Calculate positions around the bossPosition
            float offsetX = MathUtils.random(-1f, 1f); // Adjust the range as needed

            float dropX = bossPosition.x + offsetX;

            crystal.setPosition(dropX, bossPosition.y);
            ServiceLocator.getEntityService().register(crystal);
        }
    }

}
