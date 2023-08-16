package com.csse3200.game.currency;

public abstract class Currency {

    // The logo of the currency
    private final String logoFilePath;

    // Stores the amount of currency there is
    private int amount;

    /**
     * Constructor for the currency object.
     * @param logoFilePath
     */
    public Currency(String logoFilePath) {
        this.logoFilePath = logoFilePath;
    }

    /**
     * Getter for the logo of the currency.
     *
     * @return the logo of the currency in a Texture format.
     */
    public String getTexture() {
        return this.logoFilePath;
    }

    /**
     * This method increments the currency amount by a given amount.
     * Added amount is assumed to be larger than negative the currency's amount.
     *
     * @requires addedAmount >= -this.amount
     * @param addedAmount The amount the currency will be incremented by.
     */
    public void modify(int addedAmount) {
        this.amount += addedAmount;
    }

    /**
     * Checks if you can buy some item with a given cost.
     *
     * @param cost the cost of the item.
     * @return boolean representing if you can buy that object or not.
     */
    public boolean canBuy(int cost) {
        return this.amount >= cost;
    }
}
