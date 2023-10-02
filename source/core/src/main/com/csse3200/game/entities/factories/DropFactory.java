package com.csse3200.game.entities.factories;

import com.csse3200.game.components.npc.DropComponent;
import com.csse3200.game.currency.Crystal;
import com.csse3200.game.currency.Scrap;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.TextureRenderComponent;

public class DropFactory {

    /**
     * Creates a drop entity. This entity will have a texture and a drop component.
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
        drop.setLayer(0);
        return drop;
    }

    public static Entity createCrystalDrop() {
        Crystal crystal = new Crystal();
        Entity drop = new Entity()
                .addComponent(new TextureRenderComponent(crystal.getTexture()))
                .addComponent(new DropComponent(crystal));
        drop.getComponent(TextureRenderComponent.class).scaleEntity();
        drop.scaleHeight(0.5f);
        drop.scaleWidth(0.5f);
        drop.setLayer(0);
        return drop;
    }
}
