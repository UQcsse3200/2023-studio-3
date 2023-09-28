package com.csse3200.game.components.tasks.bosstask;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.ProjectileEffects;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.MobBossFactory;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import net.dermetfan.gdx.physics.box2d.PositionController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.Serial;
import java.text.DecimalFormat;

public class IceBabyTask extends DefaultTask implements PriorityTask {
    private static final int PRIORITY = 3;
    private static final Vector2 ICEBABY_SPEED = new Vector2(1f, 1f);
    private PhysicsEngine physics;
    private GameTime gameTime;
    private IceBabyState state = IceBabyState.IDLE;
    private AnimationRenderComponent animation;
    private Entity iceBaby;
    private Vector2 currentPos;
    private static final String IDLE = "startIdle";
    private static final String ATK1 = "start1_atk";
    private static final String ATK2 = "start2_atk";
    private static final String ATK3 = "start3_atk";
    private static final String DEATH = "startDeath";
    private static final String INTRO = "startIntro_or_revive";
    private static final String STAGGER = "startStagger";
    private static final String TAKEHIT = "startTake_hit";
    private static final String WALK = "startWalk";
    private enum IceBabyState {
        IDLE, ATK1, ATK2, ATK3, DEATH, INTRO, STAGGER, TAKEHIT, WALK
    }

    public IceBabyTask() {
        physics = ServiceLocator.getPhysicsService().getPhysics();
        gameTime = ServiceLocator.getTimeSource();
    }

    //ice baby should be able to poop out little mobs - spawn new
    //ice baby can also do aoe attack based on the animation
    //ice baby does punches to towers once it is close

    @Override
    public void start() {
        super.start();
        iceBaby = owner.getEntity();
        animation = iceBaby.getComponent(AnimationRenderComponent.class);
        currentPos = iceBaby.getPosition();
        iceBaby.getComponent(PhysicsMovementComponent.class).setSpeed(ICEBABY_SPEED);
        iceBaby.getEvents().trigger(INTRO);

    }

    @Override
    public void update() {

    }

    private void applyAoeDamage(Array<Entity> targets, int damage) {
        for (int i = 0; i < targets.size; i++) {
            Entity targetEntity = targets.get(i);

            CombatStatsComponent targetCombatStats = targetEntity.
                    getComponent(CombatStatsComponent.class);
            if (targetCombatStats != null) {
                targetCombatStats.hit(damage);
            }
        }
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

}
