package com.csse3200.game.entities.factories;

import com.csse3200.game.components.npc.DropComponent;
import com.csse3200.game.currency.Currency;
import com.csse3200.game.currency.Scrap;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.DropInputComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class DropFactory {

    /**
     * Creates a drop entity.
     * @return entity
     */

    // We can make different drops for different currencies
    public static Entity createScrapDrop() {
        Scrap scrap = new Scrap();
        Entity drop = new Entity()
            .addComponent(new TextureRenderComponent(scrap.getTexture()))
            .addComponent(new DropComponent(scrap));
        drop.getComponent(TextureRenderComponent.class).scaleEntity();
        drop.scaleHeight(0.5f);
        drop.scaleWidth(0.5f);
        return drop;
    }
}
