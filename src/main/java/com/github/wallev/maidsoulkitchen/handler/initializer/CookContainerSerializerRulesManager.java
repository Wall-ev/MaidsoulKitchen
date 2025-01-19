package com.github.wallev.maidsoulkitchen.handler.initializer;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.handler.api.IMaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.handler.base.container.AbstractCookBlockEntitySerializer;
import com.github.wallev.maidsoulkitchen.handler.base.rule.MaidCookBeActionType;
import com.github.wallev.maidsoulkitchen.handler.initializer.farmersdelight.FarmersDelightRecipeInitializer;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CookContainerSerializerRulesManager {

    private static Map<RecipeType<?>, List<AbstractCookBlockEntitySerializer<?, ?, ?>>> COOK_CONTAINER_SERIALIZER_RULES_MAP;

    private CookContainerSerializerRulesManager() {
        COOK_CONTAINER_SERIALIZER_RULES_MAP = new HashMap<>();
    }

    public static void register() {
        CookContainerSerializerRulesManager cookContainerSerializerRulesManager = new CookContainerSerializerRulesManager();
        FarmersDelightRecipeInitializer.registerContainerSerializerRule(cookContainerSerializerRulesManager);

        for (IMaidsoulKitchen maidsoulKitchen : MaidsoulKitchen.EXTENSIONS) {
            maidsoulKitchen.addCookContainerSerializerRules(cookContainerSerializerRulesManager);
        }

        COOK_CONTAINER_SERIALIZER_RULES_MAP = ImmutableMap.copyOf(COOK_CONTAINER_SERIALIZER_RULES_MAP);
    }

    public void registerCookRecSerializerRule(RecipeType<?> recipeType, List<AbstractCookBlockEntitySerializer<?, ?, ?>> serializerRules) {
        COOK_CONTAINER_SERIALIZER_RULES_MAP.put(recipeType, serializerRules);
    }

    public void registerCookRecSerializerRule(RecipeType<?> recipeType, MaidCookBeActionType maidCookBeActionType) {
        registerCookRecSerializerRule(recipeType, maidCookBeActionType.getSerializerRules());
    }

    public static List<AbstractCookBlockEntitySerializer<?, ?, ?>> getSerializerRules(RecipeType<?> recipeType) {
        return COOK_CONTAINER_SERIALIZER_RULES_MAP.get(recipeType);
    }
}
