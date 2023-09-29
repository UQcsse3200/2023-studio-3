package com.csse3200.game.components.projectile;

import com.csse3200.game.components.Component;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.audio.Sound;

public class OnCollisionAnimationController extends Component {
    private static final String COLLISION_SFX = "sounds/projectiles/on_collision.mp3";
    private static final String PLAYSOUND = "collisionStart";
    Sound onCollisionSound = ServiceLocator.getResourceService().getAsset(
            COLLISION_SFX, Sound.class);

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener(PLAYSOUND, this::animateCollision);
    }

    void animateCollision() {
        onCollisionSound.play();
    }
}
