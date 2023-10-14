package com.csse3200.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import static com.csse3200.game.ui.UIComponent.skin;

/**
 * This class provides static methods for creating various types of TextButtons with different styles.
 */
public class ButtonFactory {
    private static Skin defaultSkin;

    // Static initializer block to initialize the default skin  -- THIS IS HANDLED BY UICOMPONENT
//    static {
//        defaultSkin = createDefaultSkin();
//    }
//
//    private static Skin createDefaultSkin() {
//
//        // Define the button style with the background image
//        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
//        style.font = skin.getFont("default");
//        style.up = new TextureRegionDrawable(new TextureRegion(new Texture("images/ui/Sprites/UI_Glass_Button_Large_Lock_01a1.png"))); // Set the button background to the loaded image
//
//        skin.add("default", style);
//        return skin;
//    }

    /**
     * Creates a TextButton with the specified text using the default skin.
     *
     * @param text The text to display on the button.
     * @return The created TextButton.
     */
    public static TextButton createButton(String text) {
        TextButton button = new TextButton(text, skin);
//        button.getLabel().setFontScale(0.8f); // Adjust text size
        button.pad(20f); // Adjust padding
        return button;
    }

    /**
     * Creates a custom TextButton with the specified text and a custom image.
     *
     * @param text            The text to display on the button.
     * @param customImagePath The path to the custom image for the button.
     * @return The created custom TextButton.
     */
    public static TextButton createCustomButton(String text, String customImagePath) {
        // Create a custom button with a PNG image
        Texture customTexture = new Texture(Gdx.files.internal(customImagePath));
        TextureRegionDrawable customDrawable = new TextureRegionDrawable(customTexture);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = defaultSkin.getFont("default");
        style.up = customDrawable;

        TextButton button = new TextButton(text, style);
        button.getLabel().setFontScale(0.8f); // Adjust text size
        button.pad(10f); // Adjust padding

        return button;
    }

    /**
     * Creates a custom TextButton with the specified text and an image from a TextureAtlas.
     *
     * @param text      The text to display on the button.
     * @param atlasPath The path to the TextureAtlas containing the button image.
     * @return The created custom TextButton.
     */
    public static TextButton createCustomButtonWithAtlas(String text, String atlasPath) {
        // Create a custom button with a TextureAtlas
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(atlasPath));
        Skin customSkin = new Skin(atlas);

//        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
//        style.font = defaultSkin.getFont("default"); // Use the default font
        TextButton button = new TextButton(text, customSkin);

        button.getLabel().setFontScale(0.8f); // Adjust text size
        button.pad(10f); // Adjust padding

        return button;
    }
}