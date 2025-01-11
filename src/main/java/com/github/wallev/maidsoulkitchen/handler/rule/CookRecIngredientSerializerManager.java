package com.github.wallev.maidsoulkitchen.handler.rule;

import com.github.wallev.maidsoulkitchen.handler.rec.AbstractCookRec;
import com.github.wallev.maidsoulkitchen.handler.rule.common.AbstractCookRecRuleSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.recipe.brewinandchewin.BrewinandchewinRecipeSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.recipe.crockpot.CrockPotRecipeSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.recipe.drinkbeer.DrinkBeerRecipeSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.recipe.farmersdelight.FarmersDelightRecipeSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.recipe.minecraft.MinecraftRecipeSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.recipe.minersdelight.MinersDelightRecipeSerializer;
import com.github.wallev.maidsoulkitchen.handler.serializer.recipe.youkaishomecoming.YoukaisHomecomingRecipeSerializer;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;
import java.util.Map;

public final class CookRecIngredientSerializerManager {

    private static final List<? extends AbstractCookRecRuleSerializer<?, ?>> COOK_INGREDIENT_SERIALIZERS = Lists.newArrayList();
    private static final Map<RecipeType<?>, AbstractCookRecRuleSerializer<?, ?>> COOK_INGREDIENT_SERIALIZERS_MAP = Maps.newHashMap();

    public static void register() {
        MinecraftRecipeSerializer.registerIngredientSerializer();
        FarmersDelightRecipeSerializer.registerIngredientSerializer();
        BrewinandchewinRecipeSerializer.registerIngredientSerializer();
        MinersDelightRecipeSerializer.registerIngredientSerializer();
        YoukaisHomecomingRecipeSerializer.registerIngredientSerializer();

        CrockPotRecipeSerializer.registerIngredientSerializer();

        DrinkBeerRecipeSerializer.registerIngredientSerializer();
    }

    public static void registerCookRecSerializer(AbstractCookRecRuleSerializer<?, ?> serializer) {
        COOK_INGREDIENT_SERIALIZERS_MAP.put(serializer.getRecipeType(), serializer);
    }

    public static List<? extends AbstractCookRecRuleSerializer<?, ?>> getSerializers() {
        return COOK_INGREDIENT_SERIALIZERS;
    }

    public static Map<RecipeType<?>, ? extends AbstractCookRecRuleSerializer<?, ?>> getSerializerMap() {
        return COOK_INGREDIENT_SERIALIZERS_MAP;
    }

    @SuppressWarnings("unchecked")
    public static <R extends Recipe<? extends Container>, S extends AbstractCookRecRuleSerializer<R, AbstractCookRec<R>>> S getSerializer(RecipeType<R> recipeType) {
        return (S) COOK_INGREDIENT_SERIALIZERS_MAP.get(recipeType);
    }
}
