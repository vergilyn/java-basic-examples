package com.vergilyn.examples.dsl.sql;

import org.junit.jupiter.api.Test;

import java.util.UUID;

public class IDGeneratorTest {

    public static final char[] DEFAULT_ALPHABET = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray();

    @Test
    void test() {
        char[] chars = UUID.randomUUID().toString().replace("-", "").toCharArray();

        System.out.println(new String(chars));
        System.out.println(generator(chars));
        System.out.println(generatorV2(chars));

        System.out.println(0xFF << 4 | 0xFF);

        for (int i = 0; i < chars.length; i++) {
            System.out.printf("%s >>>> digit: %s, int: %d\n", chars[i], Character.digit(chars[i], 16), (int)chars[i]);
        }
    }


    public String generator(char[] chars) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.length; i += 2) {
            int high = Character.digit(chars[i], 16) << 4;
            int low = Character.digit(chars[i + 1], 16);
            int idx = high | low;

            sb.append(DEFAULT_ALPHABET[idx % DEFAULT_ALPHABET.length]);
        }

        return sb.toString();
    }

    public String generatorV2(char[] chars) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.length; i += 2) {
            int idx = chars[i] << 8 | chars[i + 1];

            sb.append(DEFAULT_ALPHABET[idx % DEFAULT_ALPHABET.length]);
        }

        return sb.toString();
    }

}
