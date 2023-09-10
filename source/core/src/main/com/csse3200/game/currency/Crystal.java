package com.csse3200.game.currency;

public class Crystal extends Currency{

    private static final int STARTING_CURRENCY = 100;

    private static final String LOGO_FILE_PATH = "images/economy/crystal.png"; // The file path of the logo

    /**
     * Constructor for the crystal currency object.
     * Sets the amount of scrap to the starting amount.
     */
    public Crystal() {
        super(LOGO_FILE_PATH, "Crystal");
        this.setAmount(STARTING_CURRENCY);
    }
}
