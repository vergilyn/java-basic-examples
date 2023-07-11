package com.vergilyn.examples.usage.u0016.dto;

import java.util.List;

public interface ReceiverContext {

    List<String> fetchReceivers();

    AbstractMsg<?> removeReceivers(List<String> removeReceivers);
}
