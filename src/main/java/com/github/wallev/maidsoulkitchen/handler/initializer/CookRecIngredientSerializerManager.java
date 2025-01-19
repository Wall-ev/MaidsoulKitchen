package com.github.wallev.maidsoulkitchen.handler.initializer;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.handler.api.IMaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.handler.base.mkrecipe.AbstractCookRec;
import com.github.wallev.maidsoulkitchen.handler.base.ingredient.AbstractCookRecIngredientSerializer;
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

import java.util.List;
import java.util.Map;

public final class CookRecIngredientSerializerManager {

    private static List<? extends AbstractCookRecIngredientSerializer<?, ?>> COOK_INGREDIENT_SERIALIZERS;
    private static Map<RecipeType<?>, AbstractCookRecIngredientSerializer<?, ?>> COOK_INGREDIENT_SERIALIZERS_MAP;
    private CookRecIngredientSerializerManager() {
        COOK_INGREDIENT_SERIALIZERS = Lists.newArrayList();
        COOK_INGREDIENT_SERIALIZERS_MAP = Maps.newHashMap();
    }

    public static void register() {
        CookRecIngredientSerializerManager cookRecIngredientSerializerManager = new CookRecIngredientSerializerManager();

        MinecraftRecipeInitializer.registerIngredientSerializer(cookRecIngredientSerializerManager);
        FarmersDelightRecipeInitializer.registerIngredientSerializer(cookRecIngredientSerializerManager);
        BrewinandchewinRecipeInitializer.registerIngredientSerializer(cookRecIngredientSerializerManager);
        MinersDelightRecipeInitializer.registerIngredientSerializer(cookRecIngredientSerializerManager);
        YoukaisHomecomingRecipeInitializer.registerIngredientSerializer(cookRecIngredientSerializerManager);

        CrockPotRecipeInitializer.registerIngredientSerializer(cookRecIngredientSerializerManager);

        DrinkBeerRecipeInitializer.registerIngredientSerializer(cookRecIngredientSerializerManager);

        for (IMaidsoulKitchen maidsoulKitchen : MaidsoulKitchen.EXTENSIONS) {
            maidsoulKitchen.addCookRecIngredientSerializer(cookRecIngredientSerializerManager);
        }

        COOK_INGREDIENT_SERIALIZERS = ImmutableList.copyOf(COOK_INGREDIENT_SERIALIZERS);
        COOK_INGREDIENT_SERIALIZERS_MAP = ImmutableMap.copyOf(COOK_INGREDIENT_SERIALIZERS_MAP);
    }

    public void registerCookRecSerializer(AbstractCookRecIngredientSerializer<?, ?> serializer) {
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
