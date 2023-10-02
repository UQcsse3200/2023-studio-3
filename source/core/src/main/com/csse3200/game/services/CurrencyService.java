package com.csse3200.game.services;

import com.csse3200.game.components.gamearea.CurrencyDisplay;
import com.csse3200.game.currency.Crystal;
import com.csse3200.game.currency.Scrap;

import java.lang.reflect.Array;
import java.util.ArrayList;
import com.csse3200.game.currency.Currency;

public class CurrencyService {

    private ArrayList<Currency> currencies;
    private CurrencyDisplay display;
//    private ArrayList<>

    /**
     * Constructor for the CurrencyService class
     */
    public CurrencyService() {
        this.currencies = new ArrayList<>(); // Creates an array list of currencies
        this.currencies.add(new Scrap()); // Adds a scrap currency to the array list
        this.currencies.add(new Crystal());
        this.display = new CurrencyDisplay(); // Creates a new currency display
    }

    public ArrayList<Currency> getCurrencies() {
        return this.currencies;
    }

    /**
     * Returns the scrap currency
     * @return Scrap object
     */
    public Scrap getScrap() {
        return (Scrap) this.currencies.get(0); // Returns the scrap currency
    }

    /**
     * Returns the crystal currency
     * @return Crystal object
     */
    public Crystal getCrystal() {
        return (Crystal) this.currencies.get(1);
    }

    public CurrencyDisplay getDisplay() {
        return display;
    }
}
