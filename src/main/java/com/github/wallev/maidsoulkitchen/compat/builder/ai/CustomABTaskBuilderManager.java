package com.github.wallev.maidsoulkitchen.compat.builder.ai;

import com.github.wallev.maidsoulkitchen.compat.builder.ai.ab.CustomABBehaviorBuilder;
import com.github.wallev.maidsoulkitchen.compat.builder.ai.builder.CustomBehaviorBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class CustomABTaskBuilderManager {

    public static final CustomABTaskBuilderManager INSTANCE = new CustomABTaskBuilderManager();
    private Map<ResourceLocation, CustomABBehaviorBuilder> customTaskMap = new HashMap<>();

    private CustomABTaskBuilderManager() {
    }

    public CustomBehaviorBuilder debugCreateTaskBase(String id) {
        ResourceLocation resourceLocation = createResourceLocation(id);
        return (CustomBehaviorBuilder) customTaskMap.computeIfAbsent(resourceLocation, rec -> new CustomBehaviorBuilder(rec, true));
    }

    public CustomBehaviorBuilder createTaskBase(String id) {
        ResourceLocation resourceLocation = createResourceLocation(id);
        return new CustomBehaviorBuilder(resourceLocation, true);
    }

    private ResourceLocation createResourceLocation(String name) {
        return new ResourceLocation("touhoulittlemaid", "custom_abtask." + name);
    }

}
