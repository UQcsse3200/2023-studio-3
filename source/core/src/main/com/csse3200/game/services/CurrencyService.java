package com.csse3200.game.services;

import com.csse3200.game.currency.Scrap;

import java.util.ArrayList;
import com.csse3200.game.currency.Currency;

public class CurrencyService {

    private ArrayList<Currency> currencies;

    public CurrencyService() {
        this.currencies = new ArrayList<>();
        this.currencies.add(new Scrap());
    }

    public ArrayList<Currency> getCurrencies() {
        return this.currencies;
    }

    public Scrap getScrap() {
        return (Scrap) this.currencies.get(0);
    }
}
