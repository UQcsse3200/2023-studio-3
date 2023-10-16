package com.csse3200.game.screens.HelpScreen;



import com.csse3200.game.GdxGame;
import com.csse3200.game.components.pausemenu.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.utils.Align.center;

public class PauseCompTutorial {

    public static Entity TutorialMenu(GdxGame game) {

        Entity TutorialMenu = new Entity()
                .addComponent(new PauseMenuTimeStopComponent())
                .addComponent(new HelpContinueButton())
                .addComponent(new TextureRenderComponent("images/HelpScreen/hs.jpg"));
        TutorialMenu.setScale(14f, 7f);
        TutorialMenu.setPosition(center+2.5f, center);
        ServiceLocator.getEntityService().register(TutorialMenu);
        return TutorialMenu;

    }
}
