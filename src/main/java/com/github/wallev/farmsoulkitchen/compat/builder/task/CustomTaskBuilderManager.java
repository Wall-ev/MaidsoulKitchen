package com.github.wallev.farmsoulkitchen.compat.builder.task;

import com.github.wallev.farmsoulkitchen.compat.builder.task.ab.CustomTaskABBaseBuilder;
import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.wallev.farmsoulkitchen.compat.builder.task.builder.*;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class CustomTaskBuilderManager {
    public static final CustomTaskBuilderManager INSTANCE = new CustomTaskBuilderManager();
    private Map<ResourceLocation, CustomTaskABBaseBuilder> customTaskMap = new HashMap<>();

    private CustomTaskBuilderManager() {
    }

    public CustomTaskBaseBuilder debugCreateTaskBase(String id) {
        ResourceLocation resourceLocation = createResourceLocation(id);
        return (CustomTaskBaseBuilder) customTaskMap.computeIfAbsent(resourceLocation, rec -> new CustomTaskBaseBuilder(rec, true));
    }

    public CustomTaskAttackBuilder debugCreateTaskAttack(String id) {
        ResourceLocation resourceLocation = createResourceLocation(id);
        return (CustomTaskAttackBuilder) customTaskMap.computeIfAbsent(resourceLocation, rec -> new CustomTaskAttackBuilder(rec, true));
    }

    public CustomTaskRangedAttackBuilder debugCreateTaskRangedAttack(String id) {
        ResourceLocation resourceLocation = createResourceLocation(id);
        return (CustomTaskRangedAttackBuilder) customTaskMap.computeIfAbsent(resourceLocation, rec -> new CustomTaskRangedAttackBuilder(rec, true));
    }

    public CustomTaskFeedBuilder debugCreateTaskFeed(String id) {
        ResourceLocation resourceLocation = createResourceLocation(id);
        return (CustomTaskFeedBuilder) customTaskMap.computeIfAbsent(resourceLocation, rec -> new CustomTaskFeedBuilder(rec, true));
    }

    public CustomTaskFarmBuilder debugCreateTaskFarm(String id) {
        ResourceLocation resourceLocation = createResourceLocation(id);
        TouhouLittleMaid.LOGGER.info("debugCreateTaskFarm: " + resourceLocation);
        TouhouLittleMaid.LOGGER.info("debugCreateTaskFarm: " + customTaskMap.get(resourceLocation));
        return (CustomTaskFarmBuilder) customTaskMap.computeIfAbsent(resourceLocation, rec -> new CustomTaskFarmBuilder(rec, true));
    }



    public CustomTaskBaseBuilder createCustomTaskBase(String id) {
        ResourceLocation resourceLocation = createResourceLocation(id);
        return CustomTaskBaseBuilder.create(resourceLocation);
    }

    public CustomTaskAttackBuilder createCustomTaskAttack(String id) {
        ResourceLocation resourceLocation = createResourceLocation(id);
        return CustomTaskAttackBuilder.create(resourceLocation);
    }

    public CustomTaskRangedAttackBuilder createCustomTaskRangedAttack(String id) {
        ResourceLocation resourceLocation = createResourceLocation(id);
        return CustomTaskRangedAttackBuilder.create(resourceLocation);
    }

    public CustomTaskFeedBuilder createCustomTaskFeed(String id) {
        ResourceLocation resourceLocation = createResourceLocation(id);
        return CustomTaskFeedBuilder.create(resourceLocation);
    }

    public CustomTaskFarmBuilder createCustomTaskFarm(String id) {
        ResourceLocation resourceLocation = createResourceLocation(id);
        return CustomTaskFarmBuilder.create(resourceLocation);
    }

    public void clearCustomTask() {

    }

    private ResourceLocation createResourceLocation(String name) {
        return new ResourceLocation("touhoulittlemaid", "custom_task." + name);
    }
}
