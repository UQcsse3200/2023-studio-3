package com.csse3200.game.currency;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class CurrencyTest {

    @Test
    void shouldSetAndGetAmount() {
        Currency currency = mock(Currency.class, CALLS_REAL_METHODS);
        int value = 100;
        currency.setAmount(value);
        assertEquals(value, currency.getAmount());
    }

}
