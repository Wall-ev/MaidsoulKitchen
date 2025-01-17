package com.github.wallev.maidsoulkitchen.handler.base.rule;

import com.github.wallev.maidsoulkitchen.handler.base.container.AbstractCookBlockEntitySerializer;
import com.github.wallev.maidsoulkitchen.handler.base.container.ContainerSerializerType;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

public enum MaidCookBeActionType {
    DEFAULT(ContainerSerializerType.TAKE_OUTPUT, ContainerSerializerType.TAKE_INPUTS, ContainerSerializerType.INSERT_INGREDIENTS),
    CONTAINER(ContainerSerializerType.TAKE_OUTPUT, ContainerSerializerType.TAKE_OUTPUT_CONTAINER, ContainerSerializerType.INSERT_MEAL_CONTAINER,
            ContainerSerializerType.TAKE_INPUTS, ContainerSerializerType.INSERT_INGREDIENTS),
    ;

    public static final MaidCookBeActionType[] values = createValues();
    private final List<AbstractCookBlockEntitySerializer<?, ?, ?>> serializerRules;

    MaidCookBeActionType(List<AbstractCookBlockEntitySerializer<?, ?, ?>> serializerRules) {
        this.serializerRules = ImmutableList.copyOf(serializerRules);
    }

    MaidCookBeActionType(AbstractCookBlockEntitySerializer<?, ?, ?>... serializerRules) {
        this.serializerRules = ImmutableList.copyOf(serializerRules);
    }

    MaidCookBeActionType(ContainerSerializerType... serializerTypes) {
        this.serializerRules = ImmutableList.copyOf(createSerializerRules(serializerTypes));
    }

    public static List<AbstractCookBlockEntitySerializer<?, ?, ?>> createSerializerRules(AbstractCookBlockEntitySerializer<?, ?, ?>... ruleSerializer) {
        List<AbstractCookBlockEntitySerializer<?, ?, ?>> rules = Lists.newArrayList();
        rules.addAll(List.of(ruleSerializer));
        return rules;
    }

    public static List<AbstractCookBlockEntitySerializer<?, ?, ?>> createSerializerRules(ContainerSerializerType... serializerTypes) {
        List<AbstractCookBlockEntitySerializer<?, ?, ?>> rules = Lists.newArrayList();
        for (ContainerSerializerType serializerType : serializerTypes) {
            rules.add(serializerType.getSerializer());
        }
        return rules;
    }

    private static MaidCookBeActionType[] createValues() {
        return new MaidCookBeActionType[]{DEFAULT, CONTAINER};
    }

    public List<AbstractCookBlockEntitySerializer<?, ?, ?>> getSerializerRules() {
        return serializerRules;
    }

}
