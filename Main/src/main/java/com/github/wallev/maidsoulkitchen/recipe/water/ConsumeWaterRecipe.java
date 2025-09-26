package com.github.wallev.maidsoulkitchen.recipe.water;

import com.github.wallev.maidsoulkitchen.init.ModRecipes;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.ParametersAreNullableByDefault;
import java.util.Locale;

@ParametersAreNonnullByDefault
@ParametersAreNullableByDefault
public class ConsumeWaterRecipe implements Recipe<Container> {
    /**
     * 获取水源条件
     * SINGLE: 单源水源
     * MULTIPLE: 无线水源
     */
    public enum Condition {
        SINGLE,
        MULTIPLE,
        ;

        @Override
        public String toString() {
            return name().toLowerCase(Locale.ENGLISH);
        }

        public static Condition of(String name) {
            return Condition.valueOf(name.toUpperCase(Locale.ENGLISH));
        }
    }

    public final ResourceLocation id;
    public final ItemStack input;
    public final ItemStack result;

    public final Condition condition;

    public ConsumeWaterRecipe(ResourceLocation id, ItemStack input, ItemStack result, Condition condition) {
        this.id = id;
        this.input = input;
        this.result = result;
        this.condition = condition;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return false;
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.createWithCapacity(1);
        ingredients.add(Ingredient.of(input));
        return ingredients;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return result;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.CONSUME_WATER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.CONSUME_WATER_RECIPE;
    }

    public static class Serializer implements RecipeSerializer<ConsumeWaterRecipe> {

        @Override
        public ConsumeWaterRecipe fromJson(ResourceLocation pRecipeId, JsonObject jsonObject) {
            ItemStack input = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "input"));
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
            Condition condition = Condition.of(GsonHelper.getAsString(jsonObject, "condition"));
            return new ConsumeWaterRecipe(pRecipeId, input, result, condition);
        }

        @Override
        @Nullable
        public ConsumeWaterRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            ItemStack input = pBuffer.readItem();
            ItemStack result = pBuffer.readItem();
            Condition condition = Condition.of(pBuffer.readUtf());
            return new ConsumeWaterRecipe(pRecipeId, input, result, condition);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ConsumeWaterRecipe pRecipe) {
            pBuffer.writeItem(pRecipe.input);
            pBuffer.writeItem(pRecipe.result);
            pBuffer.writeUtf(pRecipe.condition.toString());
        }
    }

}
