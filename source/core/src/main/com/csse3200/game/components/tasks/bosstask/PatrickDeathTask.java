package com.csse3200.game.components.tasks.bosstask;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.MobBossFactory;
import com.csse3200.game.services.ServiceLocator;

public class PatrickDeathTask extends DefaultTask implements PriorityTask {

    @Override
    public void start() {
        Vector2 curPos = owner.getEntity().getPosition();
        Entity patrick = MobBossFactory.patrickDead();
        patrick.setPosition(curPos);
        patrick.setScale(4f,4f);
        ServiceLocator.getEntityService().register(patrick);
        patrick.getEvents().trigger("patrick_death");
        owner.getEntity().setFlagForDelete(true);

        // delete after half a second
//        Timer.schedule(new Timer.Task() {
//            @Override
//            public void run() {
//                patrick.setFlagForDelete(true);
//            }
//        }, 0.5f);
    }
    @Override
    public int getPriority() {
        return 3;
    }
}
