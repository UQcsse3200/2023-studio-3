package com.csse3200.game.components.tasks.MobTask;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class MobTask extends DefaultTask implements PriorityTask {

    // Constants
    private static final int PRIORITY = 3;
    private static final Vector2 MELEE_MOB_SPEED = new Vector2(1f,1f);
    private static final Vector2 MELEE_RANGE_SPEED = new Vector2(0.7f,0.7f);
    private static final int MELEE_DAMAGE = 10;
    private static final float MELEE_ATTACK_SPEED = 1.5f;

    // Private variables
    MobType mobType;
    State state = State.DEFAULT;
    State prevState;
    Entity mob;
    AnimationRenderComponent animation;
    MovementTask movementTask;
    Entity target;

    // Flags
    boolean melee;
    boolean runFlag = false;
    boolean meleeFlag = false;
    boolean targetInRange = false;

    // Enums
    private enum State {
        RUN, ATTACK, DEATH, DEFAULT
    }

    public MobTask(MobType mobType) {
        this.mobType = mobType;
    }

    @Override
    public void start() {
        super.start();
        mob = owner.getEntity();
        animation = mob.getComponent(AnimationRenderComponent.class);
        mob.getComponent(PhysicsMovementComponent.class).setSpeed(MELEE_MOB_SPEED);
        melee = mobType.isMelee();

        movementTask = new MovementTask(new Vector2(0f, mob.getPosition().y));
        movementTask.create(owner);
        movementTask.start();
        runFlag = true;
        changeState(State.RUN);

        if (melee) {
            mob.getComponent(PhysicsMovementComponent.class).setSpeed(MELEE_MOB_SPEED);
        } else {
            mob.getComponent(PhysicsMovementComponent.class).setSpeed(MELEE_RANGE_SPEED);
        }
    }

    @Override
    public void update() {

        // death check
        if (mob.getComponent(CombatStatsComponent.class).getHealth() <= 0) {
            changeState(State.DEATH);
        }

        switch (state) {
            case RUN -> {
                if (runFlag) {
                    animate();
                    runFlag = false;
                }
                if (melee) {
                    if (enemyDetected()) {
                        targetInRange = true;
                        changeState(State.ATTACK);
                        meleeFlag = true;
                    } else {
                        targetInRange = false;
                    }
                } else {
                    rangeAttack();
                }
            }
            case ATTACK -> {
                if (meleeFlag) {
                    if (!targetInRange) {
                        changeState(State.RUN);
                        meleeFlag = false;
                        runFlag = true;
                    }
                    meleeAttack();
                    animate();
                }
            }
            case DEATH -> {
                animate();
                if (animation.isFinished()) {
                    mob.setFlagForDelete(true);
                }
            }
        }
    }

    private void animate() {
        switch (mobType) {
            case SKELETON -> {
                switch (state) {
                    case RUN -> owner.getEntity().getEvents().trigger("skeleton_walk");
                    case ATTACK -> owner.getEntity().getEvents().trigger("skeleton_attack");
                    case DEATH -> owner.getEntity().getEvents().trigger("skeleton_death");
                    case DEFAULT -> owner.getEntity().getEvents().trigger("skeleton_default");
                }
            }
            case WIZARD -> {
                switch (state) {
                    case RUN -> owner.getEntity().getEvents().trigger("wizard_run");
                    case ATTACK -> owner.getEntity().getEvents().trigger("wizard_attack");
                    case DEATH -> owner.getEntity().getEvents().trigger("wizard_death");
                    case DEFAULT -> owner.getEntity().getEvents().trigger("default");
                }
            }
            case WATER_QUEEN -> {
                switch (state) {
                    case RUN -> owner.getEntity().getEvents().trigger("water_queen_walk");
                    case ATTACK -> owner.getEntity().getEvents().trigger("water_queen_attack");
                    case DEATH -> owner.getEntity().getEvents().trigger("water_queen_death");
                    case DEFAULT -> owner.getEntity().getEvents().trigger("default");
                }
            }
            case WATER_SLIME -> {
                switch (state) {
                    case RUN -> owner.getEntity().getEvents().trigger("water_slime_walk");
                    case ATTACK -> owner.getEntity().getEvents().trigger("water_slime_attack");
                    case DEATH -> owner.getEntity().getEvents().trigger("water_slime_death");
                    case DEFAULT -> owner.getEntity().getEvents().trigger("default");
                }
            }
            case FIRE_WORM -> {
                switch (state) {
                    case RUN -> owner.getEntity().getEvents().trigger("fire_worm_walk");
                    case ATTACK -> owner.getEntity().getEvents().trigger("fire_worm_attack");
                    case DEATH -> owner.getEntity().getEvents().trigger("fire_worm_death");
                    case DEFAULT -> owner.getEntity().getEvents().trigger("default");
                }
            }
            case DRAGON_KNIGHT -> {
                switch (state) {
                    case RUN -> {
                        owner.getEntity().getEvents().trigger("dragon_knight_run");
                    }
                    case ATTACK -> owner.getEntity().getEvents().trigger("dragon_knight_attack");
                    case DEATH -> owner.getEntity().getEvents().trigger("dragon_knight_death");
                    case DEFAULT -> owner.getEntity().getEvents().trigger("default");
                }
            }
        }
    }

    private void changeState(State state) {
        prevState = this.state;
        this.state = state;
    }

    private boolean enemyDetected() {
        // if there's an entity within x of - 1 of mob
        Entity target = ServiceLocator.getEntityService().getEntityAtPosition(
                mob.getPosition().x - 1, mob.getPosition().y);
        if (target == null) {
            return false;
        }

        // layer checking
        HitboxComponent targetHitbox = target.getComponent(HitboxComponent.class);
        if (targetHitbox == null) {
            return false;
        }
        if (PhysicsLayer.contains(PhysicsLayer.HUMANS, targetHitbox.getLayer())) {
            this.target = target;
            return true;
        }
        return false;
    }

    private void meleeAttack() {
        // toggle melee flag off
        meleeFlag = false;

        // check if target is null or not in range
        if (target == null) {
            return;
        }
        CombatStatsComponent targetCombatStats = target.getComponent(CombatStatsComponent.class);
        targetCombatStats.hit(MELEE_DAMAGE);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (!targetInRange) {
                    return; // stop if target in range
                }
                meleeFlag = true; // toggle melee flag off
            }
        }, MELEE_ATTACK_SPEED);
    }



    @Override
    public int getPriority() {
        return PRIORITY;
    }
}
