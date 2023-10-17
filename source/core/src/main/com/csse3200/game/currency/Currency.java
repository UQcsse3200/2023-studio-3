package com.csse3200.game.currency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Currency {
    private static final Logger logger = LoggerFactory.getLogger(Currency.class);

    // The logo of the currency
    private final String logoFilePath;

    // Stores the amount of currency there is
    private int amount;

    private final String name;

    /**
     * Constructor for the currency object.
     * @param logoFilePath the file path of the logo
     */
    public Currency(String logoFilePath, String name) {
        this.logoFilePath = logoFilePath;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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
        logger.debug(String.format("Modifying %s by %d", this.getClass().getSimpleName(), addedAmount));
        this.amount += addedAmount;
    }

    /**
     * Checks if you can buy some item with a given cost.
     *
     * @param cost the cost of the item.
     * @return boolean representing if you can buy that object or not.
     */
    public boolean canBuy(int cost) {
        return this.getAmount() >= cost;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": " + this.getAmount();
    }
}
