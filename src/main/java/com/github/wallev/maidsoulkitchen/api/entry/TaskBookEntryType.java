package com.github.wallev.maidsoulkitchen.api.entry;

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
