package com.csse3200.game.input;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.tasks.TowerCombatTask;
import com.csse3200.game.components.tower.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class UpgradeUIComponent extends InputComponent {
    private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
    private final EntityService entityService;
    private final Camera camera;
    private final Stage stage;

    private int value;

    // Create a map to store upgrade tables for each turret entity
    private Map<Entity, Table> upgradeTables = new HashMap<>();

    /**
     * Constructor for the UpgradeUIComponent
     * @param camera the camera to be used, this is the camera that the game is rendered with
     */
    public UpgradeUIComponent(Camera camera, Stage stage) {
        this.value = ServiceLocator.getCurrencyService().getScrap().getAmount();
        this.entityService = ServiceLocator.getEntityService();
        this.camera = camera;
        this.stage = stage;
    }

    /**
     * Getter for the camera
     * @return the camera
     */
    public Camera getCamera() {
        return camera;
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Clear all existing upgrade tables


        Vector3 worldCoordinates = new Vector3((float) screenX, (float) screenY, 0);
        getCamera().unproject(worldCoordinates);
        Vector2 cursorPosition = new Vector2(worldCoordinates.x, worldCoordinates.y);
        Entity clickedEntity = entityService.getEntityAtPosition(cursorPosition.x, cursorPosition.y);

        //temp fix to prevent upgrading of new towers
        if (clickedEntity!= null && (clickedEntity.getComponent(RicochetTowerAnimationController.class) != null ||
                clickedEntity.getComponent(PierceTowerAnimationController.class) != null ||
                clickedEntity.getComponent(FireworksTowerAnimationController.class) != null)) {
            return false;
        }
        //


        if (clickedEntity != null && clickedEntity.getComponent(TowerUpgraderComponent.class) != null && clickedEntity.getComponent(TNTDamageComponent.class) == null) {
//            logger.info("clicked a turret that is upgradable!");
            clearUpgradeTables();
            // Check if there is an existing upgrade table for this turret entity
            Table existingUpgradeTable = upgradeTables.get(clickedEntity);

            if (existingUpgradeTable != null) {
                // If an upgrade table already exists, show it
                stage.addActor(existingUpgradeTable);
            } else {
                // If no upgrade table exists, create and store a new one
                Table newUpgradeTable = createUpgradeTable(clickedEntity);
                Vector2 UICoordinates = stage.screenToStageCoordinates(new Vector2(screenX, screenY));
                newUpgradeTable.setPosition(UICoordinates.x, UICoordinates.y);
                stage.addActor(newUpgradeTable);

                // Store the new upgrade table in the map
                upgradeTables.put(clickedEntity, newUpgradeTable);
            }

            return true;
        }
        return false;
    }

    // Create a method to clear all existing upgrade tables
    private void clearUpgradeTables() {
        for (Table upgradeTable : upgradeTables.values()) {
            upgradeTable.remove();
        }
        upgradeTables.clear();
    }

    private Table createUpgradeTable(Entity turretEntity) {
        Table upgradeTable = new Table();
        upgradeTable.top();
        upgradeTable.defaults().pad(0).space(0);
        upgradeTable.setSize(60, 60);
        Table innerUpgradeTable = new Table();
        innerUpgradeTable.top();
        innerUpgradeTable.defaults().pad(10).space(0).padBottom(1);
        innerUpgradeTable.setSize(60, 60);
        // set table background
        String imageFilePath = "images/ui/Sprites/UI_Glass_Frame_Standard_01a.png";
        String upgradeButtonFilePath = "images/economy/scrapBanner.png";
        Drawable drawableBackground = new TextureRegionDrawable(new TextureRegion(new Texture(imageFilePath)));
        innerUpgradeTable.setBackground(drawableBackground);

        Drawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture("images/ui/Sprites/UI_Glass_Button_Small_Lock_01a2.png")));
        Drawable econDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(upgradeButtonFilePath)));
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(
                drawable, drawable, drawable, new BitmapFont());
        TextButton.TextButtonStyle econStyle = new TextButton.TextButtonStyle(
                econDrawable, econDrawable, econDrawable, new BitmapFont());
        // create button
        int maxHealth = turretEntity.getComponent(CombatStatsComponent.class).getMaxHealth();
        int currentHealth = turretEntity.getComponent(CombatStatsComponent.class).getHealth();
        turretEntity.getComponent(CombatStatsComponent.class).setHealth(5); // for testing
        int attack = turretEntity.getComponent(CombatStatsComponent.class).getBaseAttack();
        float fireRate = turretEntity.getComponent(UpgradableStatsComponent.class).getAttackRate();
        Label healthLabel = new Label(String.format("%d/%d", currentHealth, maxHealth), createLabelStyle());
        Label attackLabel = new Label(String.format("%d", attack), createLabelStyle());
        Label fireRateLabel = new Label(String.format("%.2f", fireRate), createLabelStyle());
        Table costDisplay = new Table();
        costDisplay.setWidth(0);
        costDisplay.setBackground(drawableBackground);
        // Create an Image for the scrap icon
        Drawable costDrawable = new TextureRegionDrawable(new TextureRegion(new Texture("images/economy/scrap.png")));
        Image costImage = new Image(costDrawable);
        costDisplay.add(costImage).center();
        costImage.setScaling(Scaling.none);
        Label costDisplayLabel = new Label("100", createLabelStyle());
        costDisplay.add(costDisplayLabel).padLeft(0);

        TextButton closeButton = new TextButton("X", style);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradeTable.remove();
                // Remove the upgrade table from the map
                upgradeTables.remove(turretEntity);
            }
        });

        // Create an Image for the icons
        Drawable healthIconDrawable = new TextureRegionDrawable(new TextureRegion(new Texture("images/health.png")));
        Image healthIconImage = new Image(healthIconDrawable);

        Drawable attackIconDrawable = new TextureRegionDrawable(new TextureRegion(new Texture("images/attack.png")));
        Image attackIconImage = new Image(attackIconDrawable);

        Drawable fireRateDrawable = new TextureRegionDrawable(new TextureRegion(new Texture("images/hourglass.png")));
        Image fireRateImage = new Image(fireRateDrawable);


        Drawable healthStyle = new TextureRegionDrawable(new TextureRegion(new Texture("images/heart_upgrade.png")));
        ImageButton upgradeHealth = new ImageButton(healthStyle);
        upgradeHealth.setScale(0.8f);
        upgradeHealth.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                value = ServiceLocator.getCurrencyService().getScrap().getAmount();
                logger.info("clicked");
                if (value >= 100) {
                    value -= 100;
                    ServiceLocator.getCurrencyService().getScrap().setAmount(value);
                    ServiceLocator.getCurrencyService().getDisplay().updateScrapsStats();

                    turretEntity.getComponent(TowerUpgraderComponent.class).upgradeTower(TowerUpgraderComponent.UPGRADE.MAXHP, 10);
                    int currentHealth = turretEntity.getComponent(CombatStatsComponent.class).getHealth();
                    int maxHealth = turretEntity.getComponent(CombatStatsComponent.class).getMaxHealth();
                    healthLabel.setText(String.format("%d/%d", currentHealth, maxHealth));
                }
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                costDisplayLabel.setText("10");
                costDisplay.setVisible(true);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                costDisplay.setVisible(false);
            }
        });

        Drawable attackStyle = new TextureRegionDrawable(new TextureRegion(new Texture("images/damage_upgrade.png")));
        ImageButton upgradeAttack = new ImageButton(attackStyle);
        upgradeAttack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                value = ServiceLocator.getCurrencyService().getScrap().getAmount();
                if (value >= 10) {
                    value -= 10;
                    ServiceLocator.getCurrencyService().getScrap().setAmount(value);
                    ServiceLocator.getCurrencyService().getDisplay().updateScrapsStats();
                    turretEntity.getComponent(TowerUpgraderComponent.class).upgradeTower(TowerUpgraderComponent.UPGRADE.ATTACK, 5);

                    int attack = turretEntity.getComponent(CombatStatsComponent.class).getBaseAttack();
                    attackLabel.setText(String.format("%d", attack));
                }
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                costDisplayLabel.setText("10");
                costDisplay.setVisible(true);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                costDisplay.setVisible(false);
            }
        });



        Drawable asStyle = new TextureRegionDrawable(new TextureRegion(new Texture("images/hourglass_upgrade.png")));
        ImageButton upgradeFireRate = new ImageButton(asStyle);
        upgradeFireRate.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                value = ServiceLocator.getCurrencyService().getScrap().getAmount();
                if (value >= 10) {
                    value -= 10;
                    ServiceLocator.getCurrencyService().getScrap().setAmount(value);
                    ServiceLocator.getCurrencyService().getDisplay().updateScrapsStats();
                    float newFireRate = turretEntity.getComponent(UpgradableStatsComponent.class).getAttackRate() + 0.2f;
                    turretEntity.getComponent(UpgradableStatsComponent.class).setAttackRate(newFireRate);
                    turretEntity.getComponent(TowerUpgraderComponent.class).upgradeTower(TowerUpgraderComponent.UPGRADE.FIRERATE, (int) newFireRate * 5);

                    float fireRate = turretEntity.getComponent(UpgradableStatsComponent.class).getAttackRate();
                    fireRateLabel.setText(String.format("%.2f", fireRate));
                }

            }


            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                costDisplayLabel.setText("10");
                costDisplay.setVisible(true);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                costDisplay.setVisible(false);
            }
        });

        Drawable repair = new TextureRegionDrawable(new TextureRegion(new Texture("images/hammer.png")));
        ImageButton repairButton = new ImageButton(repair);
        repairButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                value = ServiceLocator.getCurrencyService().getScrap().getAmount();
                if (value >= 100) {
                    value -= 100;
                    ServiceLocator.getCurrencyService().getScrap().setAmount(value);
                    ServiceLocator.getCurrencyService().getDisplay().updateScrapsStats();
                    turretEntity.getComponent(TowerUpgraderComponent.class).upgradeTower(TowerUpgraderComponent.UPGRADE.REPAIR, 0);
                    int currentHealth = turretEntity.getComponent(CombatStatsComponent.class).getHealth();
                    healthLabel.setText(String.format("%d/%d", currentHealth, maxHealth));
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                costDisplayLabel.setText("10");
                costDisplay.setVisible(true);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                costDisplay.setVisible(false);
            }


        });

        innerUpgradeTable.row();
        innerUpgradeTable.add(healthIconImage).padRight(5).width(32).height(32);  // Add health icon
        innerUpgradeTable.add(healthLabel).expandX().left();
        innerUpgradeTable.row();
        TextButton upgradeIncome = null;
        if (turretEntity.getComponent(IncomeUpgradeComponent.class) != null) {
            Drawable incomeDrawable = new TextureRegionDrawable(new TextureRegion(new Texture("images/economy/scrap.png")));
            Image incomeImage = new Image(incomeDrawable);
            Label incomeLabel = new Label(String.format("%.2f", turretEntity.getComponent(IncomeUpgradeComponent.class).getIncomeRate()), createLabelStyle());
            innerUpgradeTable.add(incomeImage).padRight(5).width(32).height(32);
            innerUpgradeTable.add(incomeLabel).expandX().left();
            innerUpgradeTable.row();

            Drawable income = new TextureRegionDrawable(new TextureRegion(new Texture("images/scrap_upgrade.png")));
            ImageButton attackStyleButton = new ImageButton(asStyle);
            attackStyleButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    value = ServiceLocator.getCurrencyService().getScrap().getAmount();
                    if (value >= 10 && turretEntity.getComponent(IncomeUpgradeComponent.class).getIncomeRate() >= 10) {
                        value -= 10;
                        ServiceLocator.getCurrencyService().getScrap().setAmount(value);
                        ServiceLocator.getCurrencyService().getDisplay().updateScrapsStats();
                        float newIncome = turretEntity.getComponent(IncomeUpgradeComponent.class).getIncomeRate() - 5;
                        turretEntity.getComponent(IncomeUpgradeComponent.class).setIncomeRate(newIncome);
                        turretEntity.getComponent(TowerUpgraderComponent.class).upgradeTower(TowerUpgraderComponent.UPGRADE.INCOME, (int) newIncome);
                        incomeLabel.setText(String.format("%.2f", newIncome));
                    }

                }


                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    costDisplayLabel.setText("10");
                    costDisplay.setVisible(true);
                }
                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    costDisplay.setVisible(false);
                }
            });
        }
        if (attack != 0) {
            innerUpgradeTable.add(attackIconImage).padRight(5).width(32).height(32);  // Add attack icon
            innerUpgradeTable.add(attackLabel).expandX().left();
            innerUpgradeTable.row();

            innerUpgradeTable.add(fireRateImage).padRight(5).width(32).height(32);  // Add fire rate icon
            innerUpgradeTable.add(fireRateLabel).expandX().right();
            innerUpgradeTable.row();
        }
        innerUpgradeTable.add(upgradeHealth).expandX().fillX();
        if (attack != 0) {
            innerUpgradeTable.add(upgradeAttack).expandX().fillX();
            innerUpgradeTable.add(upgradeFireRate).expandX().fillX();
        }
        if (upgradeIncome != null) {
            innerUpgradeTable.add(upgradeIncome).expandX().fillX();
        }
        innerUpgradeTable.add(repairButton).expandX().fillX();
        upgradeTable.add(closeButton).right().row();
        upgradeTable.add(innerUpgradeTable).center().expand().row();

        upgradeTable.add(costDisplay).left();
        costDisplay.setVisible(false);
        upgradeTable.setVisible(true);

        return upgradeTable;
    }

    private LabelStyle createLabelStyle() {
        LabelStyle style = new LabelStyle();
        style.font = new BitmapFont();
        style.fontColor = Color.WHITE;
        return style;
    }
}
