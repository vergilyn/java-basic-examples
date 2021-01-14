package com.vergilyn.examples.spi.seata.extension;

import com.vergilyn.examples.spi.seata.LoadLevel;

@LoadLevel(name = ChineseSeataSpi.NAME, order = ChineseSeataSpi.ORDER)
public class ChineseSeataSpi implements SeataSpi {
    public static final int ORDER = Integer.MAX_VALUE;

    public static final String NAME = "ChineseHello";

    @Override
    public String say() {
        return print("chinese", ORDER);
    }
}
