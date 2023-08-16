package com.csse3200.game.entities.factories;

import com.csse3200.game.currency.Currency;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.TextureRenderComponent;

/**
 * Factory to create a drop entity.
 *
 */
public class DropFactory {
    public static Entity createDrop(int x, int y, int value){
        Entity drop =
                new Entity().addComponent(new TextureRenderComponent("PLACEHOLDER"));
        // This will be, Currency.getTexturePath(), once the ServiceLocator is implemented
        drop.scaleHeight(0.5f);
        drop.scaleWidth(0.5f);
        drop.setPosition(x, y); //Position called by Mob class
        //some sort of function call that sets value of drop.
        drop.getComponent(TextureRenderComponent.class).scaleEntity();
        return drop;
    }
    private DropFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}



