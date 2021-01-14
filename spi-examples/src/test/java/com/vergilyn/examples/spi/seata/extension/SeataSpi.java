package com.vergilyn.examples.spi.seata.extension;

public interface SeataSpi {
    String say();

    default String print(String name, int order){
        return String.format("seata-spi >>>> %s, order = %d", name, order);
    }
}
