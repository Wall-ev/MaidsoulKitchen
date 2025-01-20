package com.github.wallev.maidsoulkitchen.handler.initializer;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.handler.api.IMaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.handler.base.recipe.AbstractCookRecInitializer;
import com.github.wallev.maidsoulkitchen.handler.initializer.brewinandchewin.BrewinandchewinRecipeInitializer;
import com.github.wallev.maidsoulkitchen.handler.initializer.crockpot.CrockPotRecipeInitializer;
import com.github.wallev.maidsoulkitchen.handler.initializer.drinkbeer.DrinkBeerRecipeInitializer;
import com.github.wallev.maidsoulkitchen.handler.initializer.farmersdelight.FarmersDelightRecipeInitializer;
import com.github.wallev.maidsoulkitchen.handler.initializer.minecraft.MinecraftRecipeInitializer;
import com.github.wallev.maidsoulkitchen.handler.initializer.minersdelight.MinersDelightRecipeInitializer;
import com.github.wallev.maidsoulkitchen.handler.initializer.youkaishomecoming.YoukaisHomecomingRecipeInitializer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;

public final class CookRecRecipeInitializerManager {

    private static List<? extends AbstractCookRecInitializer<?>> COOK_REC_SERIALIZERS;
    private static Map<RecipeType<?>, AbstractCookRecInitializer<?>> COOK_REC_SERIALIZERS_MAP;

    private CookRecRecipeInitializerManager() {
        COOK_REC_SERIALIZERS = Lists.newArrayList();
        COOK_REC_SERIALIZERS_MAP = Maps.newHashMap();
    }

    public static void register() {
        CookRecRecipeInitializerManager cookRecRecipeInitializerManager = new CookRecRecipeInitializerManager();

        MinecraftRecipeInitializer.registerRecipeInitializer(cookRecRecipeInitializerManager);
        FarmersDelightRecipeInitializer.registerRecipeInitializer(cookRecRecipeInitializerManager);
        BrewinandchewinRecipeInitializer.registerRecipeInitializer(cookRecRecipeInitializerManager);
        MinersDelightRecipeInitializer.registerRecipeInitializer(cookRecRecipeInitializerManager);
        YoukaisHomecomingRecipeInitializer.registerRecipeInitializer(cookRecRecipeInitializerManager);
//        CrockPotRecipeInitializer.registerRecipeInitializer(cookRecRecipeInitializerManager);
        DrinkBeerRecipeInitializer.registerRecipeInitializer(cookRecRecipeInitializerManager);

        for (IMaidsoulKitchen maidsoulKitchen : MaidsoulKitchen.EXTENSIONS) {
            maidsoulKitchen.addCookRecRecipeInitializer(cookRecRecipeInitializerManager);
        }

        COOK_REC_SERIALIZERS = ImmutableList.copyOf(COOK_REC_SERIALIZERS);
        COOK_REC_SERIALIZERS_MAP = ImmutableMap.copyOf(COOK_REC_SERIALIZERS_MAP);
    }

    public static void initializerData(Level level) {
        for (AbstractCookRecInitializer<?> cookRecSerializer : COOK_REC_SERIALIZERS_MAP.values()) {
            cookRecSerializer.init(level);
        }

        int a = 1;
    }

    public static List<? extends AbstractCookRecInitializer<?>> getInitializer() {
        return COOK_REC_SERIALIZERS;
    }

    public static Map<RecipeType<?>, ? extends AbstractCookRecInitializer<?>> getInitializerMap() {
        return COOK_REC_SERIALIZERS_MAP;
    }

    @SuppressWarnings("unchecked")
    public static <R extends Recipe<? extends Container>, S extends AbstractCookRecInitializer<R>> S getInitializer(RecipeType<R> recipeType) {
        return (S) COOK_REC_SERIALIZERS_MAP.get(recipeType);
    }

    public void registerCookRecInitializer(AbstractCookRecInitializer<?> serializer) {
        RecipeType<?> recipeType = serializer.getRecipeType();
        COOK_REC_SERIALIZERS_MAP.put(recipeType, serializer);
    }
}
