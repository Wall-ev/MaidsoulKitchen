package com.github.wallev.maidsoulkitchen.handler.serializer;

import com.github.wallev.maidsoulkitchen.handler.serializer.recipe.brewinandchewin.BrewinandchewinRecipeSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.recipe.crockpot.CrockPotRecipeSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.recipe.drinkbeer.DrinkBeerRecipeSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.recipe.farmersdelight.FarmersDelightRecipeSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.recipe.minecraft.MinecraftRecipeSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.recipe.minersdelight.MinersDelightRecipeSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.recipe.youkaishomecoming.YoukaisHomecomingRecipeSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.rule.AbstractCookRecSerializer;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;

public final class CookRecipeSerializerManager {

    private static final List<? extends AbstractCookRecSerializer<?>> COOK_REC_SERIALIZERS = Lists.newArrayList();
    private static final Map<RecipeType<?>, AbstractCookRecSerializer<?>> COOK_REC_SERIALIZERS_MAP = Maps.newHashMap();

    public static void register() {
        MinecraftRecipeSerializer.registerRecipeInitializer();
        FarmersDelightRecipeSerializer.registerRecipeInitializer();
        BrewinandchewinRecipeSerializer.registerRecipeInitializer();
        MinersDelightRecipeSerializer.registerRecipeInitializer();
        YoukaisHomecomingRecipeSerializer.registerRecipeInitializer();
        CrockPotRecipeSerializer.registerRecipeInitializer();
        DrinkBeerRecipeSerializer.registerRecipeInitializer();
    }

    public static void registerCookRecSerializer(AbstractCookRecSerializer<?> serializer) {
        RecipeType<?> recipeType = serializer.getRecipeType();
        COOK_REC_SERIALIZERS_MAP.put(recipeType, serializer);
    }

    public static void initialSerializerData(Level level) {
        for (AbstractCookRecSerializer<?> cookRecSerializer : COOK_REC_SERIALIZERS) {
            cookRecSerializer.init(level);
        }
    }

    public static List<? extends AbstractCookRecSerializer<?>> getSerializers() {
        return COOK_REC_SERIALIZERS;
    }

    public static Map<RecipeType<?>, ? extends AbstractCookRecSerializer<?>> getSerializerMap() {
        return COOK_REC_SERIALIZERS_MAP;
    }

    @SuppressWarnings("unchecked")
    public static <R extends Recipe<? extends Container>, S extends AbstractCookRecSerializer<R>> S getSerializer(RecipeType<R> recipeType) {
        return (S) COOK_REC_SERIALIZERS_MAP.get(recipeType);
    }
}
