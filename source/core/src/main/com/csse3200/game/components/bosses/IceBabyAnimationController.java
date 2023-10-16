package com.csse3200.game.components.bosses;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.services.ServiceLocator;

public class IceBabyAnimationController extends Component {
    AnimationRenderComponent animator;

    /** Animation constants*/
    private static final String IDLE_ANIM = "idle";
    private static final String ATK1_ANIM = "1_atk";
    private static final String ATK2_ANIM = "2_atk";
    private static final String ATK3_ANIM = "3_atk";
    private static final String DEATH_ANIM = "death";
    private static final String INTRO_ANIM = "intro_or_revive";
    private static final String STAGGER_ANIM = "stagger";
    private static final String TAKEHIT_ANIM = "take_hit";
    private static final String WALK_ANIM = "walk";

    /** Event name constants*/
    private static final String IDLE = "startIdle";
    private static final String ATK1 = "start1_atk";
    private static final String ATK2 = "start2_atk";
    private static final String ATK3 = "start3_atk";
    private static final String DEATH = "startDeath";
    private static final String INTRO = "startIntro_or_revive";
    private static final String STAGGER = "startStagger";
    private static final String TAKEHIT = "startTake_hit";
    private static final String WALK = "startWalk";

    /** Sound variables */
    private static final String SPAWN_SOUND = "sounds/mobBoss/iceBabySound.mp3";
    Sound spawnSound = ServiceLocator.getResourceService().getAsset(
            SPAWN_SOUND, Sound.class);
    private static final String MOB_SPAWN_SOUND = "sounds/mobBoss/mobSpawnStomp.mp3";
    Sound mobSpawnSound = ServiceLocator.getResourceService().getAsset(
            MOB_SPAWN_SOUND, Sound.class);
    private static final String AOE_SOUND = "sounds/mobBoss/iceBabyAOE.mp3";
    Sound aoeSound = ServiceLocator.getResourceService().getAsset(
            AOE_SOUND, Sound.class);

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener(IDLE, this::animateIdle);
        entity.getEvents().addListener(ATK1, this::animateATK1);
        entity.getEvents().addListener(ATK2, this::animateATK2);
        entity.getEvents().addListener(ATK3, this::animateATK3);
        entity.getEvents().addListener(DEATH, this::animateDeath);
        entity.getEvents().addListener(INTRO, this::animateIntro);
        entity.getEvents().addListener(STAGGER, this::animateStagger);
        entity.getEvents().addListener(TAKEHIT, this::animateTakeHit);
        entity.getEvents().addListener(WALK, this::animateWalk);
    }

    void animateIdle() {
        animator.startAnimation(IDLE_ANIM);
    }
    void animateATK1() {
        animator.startAnimation(ATK1_ANIM);
    }
    void animateATK2() {
        animator.startAnimation(ATK2_ANIM);
        //volume does not work
        mobSpawnSound.setVolume(1000, 5.5f);
        mobSpawnSound.play();
    }
    void animateATK3() {
        animator.startAnimation(ATK3_ANIM);
        aoeSound.setVolume(1000, 5.5f);
        aoeSound.play();
    }
    void animateDeath() {
        animator.startAnimation(DEATH_ANIM);
    }
    void animateIntro() {
        animator.startAnimation(INTRO_ANIM);
        spawnSound.setVolume(1000, 5.5f);
        spawnSound.play();
    }
    void animateTakeHit() {
        animator.startAnimation(TAKEHIT_ANIM);
    }
    void animateStagger() {
        animator.startAnimation(STAGGER_ANIM);
    }
    void animateWalk() {
        animator.startAnimation(WALK_ANIM);
    }

}
