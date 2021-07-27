/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.vergilyn.examples.usage.u0007;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @Auther: magenming
 * @Date: 2018/9/5 15:08
 */
public class ByteGroup {

    private List<Byte> byteContainer = new ArrayList<>();

    public byte[] toBytes() {
        byte[] bytes = new byte[byteContainer.size()];
        for (int i = 0; i < byteContainer.size(); i++) {
            bytes[i] = byteContainer.get(i);
        }
        return bytes;
    }

    public ByteGroup addBytes(byte[] bytes) {
        for (byte b : bytes) {
            byteContainer.add(b);
        }
        return this;
    }

    public int size() {
        return byteContainer.size();
    }

}
