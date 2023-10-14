package com.csse3200.game.services;

import com.csse3200.game.components.gamearea.CurrencyDisplay;
import com.csse3200.game.currency.Crystal;
import com.csse3200.game.currency.Scrap;

import java.lang.reflect.Array;
import java.util.ArrayList;
import com.csse3200.game.currency.Currency;
import com.csse3200.game.screens.TowerType;

public class CurrencyService {

    private ArrayList<Currency> currencies;
    private CurrencyDisplay display;
    private TowerType tower = null;

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

    /**
     * Sets the tower type to build - triggered by pressing a tower build button in-game
     * newTower can be a towertype or a null value to indicate clearing the value?
     * @param newTower The towertype to be set for building, null if deselecting
     */
    public void setTowerType(TowerType newTower) {
        if (tower == newTower) {
            tower = null;
        } else {
            tower = newTower;
        }
    }

    public TowerType getTower() {
        return tower;
    }
}
