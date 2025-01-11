package com.github.wallev.maidsoulkitchen.handler.serializer;

import com.github.wallev.maidsoulkitchen.handler.base.mkrecipe.AbstractCookRec;
import com.github.wallev.maidsoulkitchen.handler.base.ingredient.AbstractCookRecIngredientSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.brewinandchewin.BrewinandchewinRecipeSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.crockpot.CrockPotRecipeSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.drinkbeer.DrinkBeerRecipeSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.farmersdelight.FarmersDelightRecipeSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.minecraft.MinecraftRecipeSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.minersdelight.MinersDelightRecipeSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.youkaishomecoming.YoukaisHomecomingRecipeSerializer;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;
import java.util.Map;

public final class CookRecIngredientSerializerManager {

    private static final List<? extends AbstractCookRecIngredientSerializer<?, ?>> COOK_INGREDIENT_SERIALIZERS = Lists.newArrayList();
    private static final Map<RecipeType<?>, AbstractCookRecIngredientSerializer<?, ?>> COOK_INGREDIENT_SERIALIZERS_MAP = Maps.newHashMap();

    public static void register() {
        MinecraftRecipeSerializer.registerIngredientSerializer();
        FarmersDelightRecipeSerializer.registerIngredientSerializer();
        BrewinandchewinRecipeSerializer.registerIngredientSerializer();
        MinersDelightRecipeSerializer.registerIngredientSerializer();
        YoukaisHomecomingRecipeSerializer.registerIngredientSerializer();

        CrockPotRecipeSerializer.registerIngredientSerializer();

        DrinkBeerRecipeSerializer.registerIngredientSerializer();
    }

    public static void registerCookRecSerializer(AbstractCookRecIngredientSerializer<?, ?> serializer) {
        COOK_INGREDIENT_SERIALIZERS_MAP.put(serializer.getRecipeType(), serializer);
    }

    public static List<? extends AbstractCookRecIngredientSerializer<?, ?>> getSerializers() {
        return COOK_INGREDIENT_SERIALIZERS;
    }

    public static Map<RecipeType<?>, ? extends AbstractCookRecIngredientSerializer<?, ?>> getSerializerMap() {
        return COOK_INGREDIENT_SERIALIZERS_MAP;
    }

    @SuppressWarnings("unchecked")
    public static <R extends Recipe<? extends Container>, S extends AbstractCookRecIngredientSerializer<R, AbstractCookRec<R>>> S getSerializer(RecipeType<R> recipeType) {
        return (S) COOK_INGREDIENT_SERIALIZERS_MAP.get(recipeType);
    }
}
