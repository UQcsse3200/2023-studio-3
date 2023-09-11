package com.csse3200.game.components.tower;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;

/**
 * Listens for an event from the popup menu to upgrade
 *     the turret entity this component is attached to.
 */
public class TowerUpgraderComponent extends Component {
    /**
     * A set of specifiers allowing upgrade event triggers to specify the type of upgrade being requested.
     */
    public enum UPGRADE {
        ATTACK, MAXHP, FIRERATE, REPAIR
    }

    /**
     * Called when the entity is created and registered.
     * Sets up an event listener to detect when an upgrade request is sent to this tower.
     */
    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("upgradeTower", this::upgradeTower);
    }

    /**
     * Determines which type of upgrade to perform based on arguments provided by the event trigger.
     * Note: The fire rate upgrade is in shots per minute.
     *
     * @param upgradeType An enum indicating the type of upgrade to do
     * @param value How much the upgrade should change the tower's stats, if applicable
     */
    void upgradeTower(UPGRADE upgradeType, int value) {
        switch (upgradeType) {
            case ATTACK -> upgradeTowerAttack(value);
            case MAXHP -> upgradeTowerMaxHealth(value);
            case FIRERATE -> getEntity().getEvents().trigger("addFireRate", value);
            case REPAIR -> repairTower();
        }
    }

    /**
     * Increases the tower's attack stat.
     *
     * @param increase The amount that the attack stat should increase by.
     */
    void upgradeTowerAttack(int increase) {
        int oldAttack = getEntity().getComponent(CombatStatsComponent.class).getBaseAttack();
        getEntity().getComponent(CombatStatsComponent.class).setBaseAttack(oldAttack + increase);
    }

    /**
     * Increases the tower's maximum health, and restores the tower's health to the new maximum.
     *
     * @param increase The amount that the max health stat should increase by.
     */
    void upgradeTowerMaxHealth(int increase) {
        int oldMaxHealth = getEntity().getComponent(CombatStatsComponent.class).getMaxHealth();
        getEntity().getComponent(CombatStatsComponent.class).setMaxHealth(oldMaxHealth + increase);
        getEntity().getComponent(CombatStatsComponent.class).setHealth(oldMaxHealth + increase);
    }

    /**
     * Restores the tower's health to its maximum health.
     */
    void repairTower() {
        int maxHealth = getEntity().getComponent(CombatStatsComponent.class).getMaxHealth();
        getEntity().getComponent(CombatStatsComponent.class).setHealth(maxHealth);
    }
}
