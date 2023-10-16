package com.csse3200.game.screens.HelpScreen;



import com.csse3200.game.GdxGame;
import com.csse3200.game.components.pausemenu.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.utils.Array;

public class PauseCompTutorial {

    public static Entity TutorialMenu(GdxGame game) {

            Entity TutorialMenu = new Entity()
                    .addComponent(new PauseMenuTimeStopComponent())
                    .addComponent(new PauseMenuContinueButton())
                    .addComponent(new TextureRenderComponent("images/ui/Sprites/UI_Glass_Toggle_Bar_01a.png"));
            TutorialMenu.setScale(8, 8);
            TutorialMenu.setPosition(6.3f, 2f);
            ServiceLocator.getEntityService().register(TutorialMenu);
            return TutorialMenu;

        }
    }


