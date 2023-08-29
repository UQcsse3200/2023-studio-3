package com.csse3200.game.components.npc;
import com.csse3200.game.components.Component;
import com.csse3200.game.currency.Currency;
public class DropComponent extends Component {
    private int value = 0;
    protected Currency currency;

    public DropComponent(Currency currency) {
        this.value = currency.getAmount();
        this.currency = currency;
    }

    public int getValue() {
        return this.value;
    }

    public Currency getCurrency() {
        return this.currency;
    }
    @Override
    public void dispose() {
        this.currency.setAmount(currency.getAmount() + this.value);
    }
}
