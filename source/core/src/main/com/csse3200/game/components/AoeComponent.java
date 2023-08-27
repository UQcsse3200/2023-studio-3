package com.csse3200.game.components;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

import com.badlogic.gdx.utils.Array;

public class AoeComponent extends Component {
    private final float radius;

    /**
     * Constructor for the AoEComponent.
     *
     * @param radius The radius of the area-of-effect.
     */
    public AoeComponent(float radius) {
        this.radius = radius;
    }

    /**
     * Apply damage to all entities within the area of effect (radius).
     */
    public void applyDamage() {
        Entity hostEntity = getEntity();
        CombatStatsComponent hostCombatStats = hostEntity.getComponent(CombatStatsComponent.class);

        if (hostCombatStats == null) {
            // The host entity does not have a CombatStatsComponent to deal damage
            return;
        }

        Array<Entity> nearbyEntities = ServiceLocator.getEntityService().getNearbyEntities(hostEntity, radius);

        for (Entity targetEntity : nearbyEntities) {
            CombatStatsComponent targetCombatStats = targetEntity.getComponent(CombatStatsComponent.class);
            if (targetCombatStats != null) {
                targetCombatStats.hit(hostCombatStats);
            }
        }
    }
}
