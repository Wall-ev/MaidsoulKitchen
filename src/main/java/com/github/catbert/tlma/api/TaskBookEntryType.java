package com.github.catbert.tlma.api;

public enum TaskBookEntryType {

    FRUIT_FARM("fruit"),
    BERRY_FARM("berry"),
    COOK("cook"),
    OTHER("other");

    public final String name;
    TaskBookEntryType(String name) {
        this.name = name;
    }
}
