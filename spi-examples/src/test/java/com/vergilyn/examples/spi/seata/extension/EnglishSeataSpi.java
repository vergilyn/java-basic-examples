package com.vergilyn.examples.spi.seata.extension;

import com.vergilyn.examples.spi.seata.LoadLevel;

@LoadLevel(name = EnglishSeataSpi.NAME, order = EnglishSeataSpi.ORDER)
public class EnglishSeataSpi implements SeataSpi {
    public static final int ORDER = 1;

    public static final String NAME = "EnglishHello";

    @Override
    public String say() {
        return print("english", ORDER);
    }
}
