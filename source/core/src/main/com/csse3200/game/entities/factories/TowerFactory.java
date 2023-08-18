package com.csse3200.game.entities.factories;


import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;


public class TowerFactory {

    public static Entity createBaseTower() {
        // we're going to add more components later on
        Entity tower = new Entity()
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE)) // we might have to change the names of the layers
            .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody));

        return tower;
    }
}