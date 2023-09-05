package com.csse3200.game.currency;

public class Scrap extends Currency {

    private static final int STARTING_CURRENCY = 100;

    private static final String LOGO_FILE_PATH = "images/economy/scrap.png"; // The file path of the logo

    /**
     * Constructor for the scrap currency object.
     * Sets the amount of scrap to the starting amount.
     */
    public Scrap() {
        super(LOGO_FILE_PATH, "Scrap");
        this.setAmount(STARTING_CURRENCY);
    }
}
