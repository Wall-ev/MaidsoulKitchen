package com.github.wallev.maidsoulkitchen.handler.base.container;

import cpw.mods.util.Lazy;

public enum ContainerSerializerType {
    TAKE_OUTPUT(new TakeOutputSerializer<>()),
    TAKE_INPUTS(new TakeInputsSerializer<>()),
    TAKE_OUTPUT_CONTAINER(new TakeOutputContainerSerializer<>()),
    INSERT_INGREDIENTS(new InsertIngredientsSerializer<>()),
    INSERT_MEAL_CONTAINER(new InsertMealContainerSerializer<>()),
    ;
    public static final Lazy<ContainerSerializerType[]> VALUES = Lazy.of(values());

    private final AbstractCookBlockEntitySerializer<?, ?, ?> serializer;

    ContainerSerializerType(AbstractCookBlockEntitySerializer<?, ?, ?> serializer) {
        this.serializer = serializer;
    }

    public AbstractCookBlockEntitySerializer<?, ?, ?> getSerializer() {
        return serializer;
    }
}
