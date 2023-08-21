package com.csse3200.game.currency;

public class Scrap extends Currency {

    private static final int STARTING_CURRENCY = 100;

    private static final String LOGO_FILE_PATH = "core/assets/scrap.png";

    public Scrap() {
        super(LOGO_FILE_PATH, "Scrap");
        this.setAmount(STARTING_CURRENCY);
    }
}
