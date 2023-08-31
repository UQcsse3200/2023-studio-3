package com.csse3200.game.components.tower;

import com.csse3200.game.components.Component;

/**
 * Listens for an event from the popup menu to upgrade
 *     the turret entity this component is attached to.
 */
public class TowerUpgraderComponent extends Component {
    public enum UPGRADE {
        ATTACK, MAXHP, FIRERATE
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("upgradeTower", this::upgradeTower);
    }

    /**
     * Determines which type of upgrade to perform based on arguments provided by the event trigger.
     * @param upgradeType An enum indicating the type of upgrade to do
     * @param value How much the upgrade should change the tower's stats, where applicable
     */
    void upgradeTower(UPGRADE upgradeType, int value) {
        switch (upgradeType) {
            case ATTACK -> {/*Not implemented yet*/}
            case MAXHP -> {/*Not implemented yet either*/}
            case FIRERATE -> {/*Not implemented at the present moment*/}
        }
    }
}
