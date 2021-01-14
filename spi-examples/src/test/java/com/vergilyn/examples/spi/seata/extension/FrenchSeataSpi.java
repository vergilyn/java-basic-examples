package com.vergilyn.examples.spi.seata.extension;

import com.vergilyn.examples.spi.seata.LoadLevel;

/**
 * The type French hello.
 *
 * @author Otis.z
 */
@LoadLevel(name = FrenchSeataSpi.NAME, order = FrenchSeataSpi.ORDER)
public class FrenchSeataSpi implements SeataSpi {
    public static final int ORDER = 2;

    public static final String NAME = "FrenchHello";

    @Override
    public String say() {
        return print("french", ORDER);
    }
}
