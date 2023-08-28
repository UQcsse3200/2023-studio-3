package com.csse3200.game.services;

import com.csse3200.game.components.gamearea.CurrencyDisplay;
import com.csse3200.game.currency.Scrap;

import java.util.ArrayList;
import com.csse3200.game.currency.Currency;

public class CurrencyService {

    private ArrayList<Currency> currencies;
    private CurrencyDisplay display;

    public CurrencyService() {
        this.currencies = new ArrayList<>();
        this.currencies.add(new Scrap());
        this.display = new CurrencyDisplay();
    }

    public ArrayList<Currency> getCurrencies() {
        return this.currencies;
    }

    public Scrap getScrap() {
        return (Scrap) this.currencies.get(0);
    }

    public CurrencyDisplay getDisplay() {
        return display;
    }
}
