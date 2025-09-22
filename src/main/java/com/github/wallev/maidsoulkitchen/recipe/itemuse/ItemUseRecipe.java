package com.github.wallev.maidsoulkitchen.recipe.itemuse;

import com.github.wallev.maidsoulkitchen.init.ModRecipes;
import com.google.gson.JsonArray;
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
import studio.fantasyit.maid_storage_manager.craft.context.common.CommonUseAction;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.ParametersAreNullableByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ParametersAreNonnullByDefault
@ParametersAreNullableByDefault
public class ItemUseRecipe implements Recipe<Container> {
    /**
     * 获取水源条件
     * SINGLE: 单源水源
     * MULTIPLE: 无线水源
     */
    public enum Condition {
        SINGLE {
            @Override
            public CommonUseAction.USE_TYPE toUseType() {
                return CommonUseAction.USE_TYPE.SINGLE;
            }
        },
        LONG {
            @Override
            public CommonUseAction.USE_TYPE toUseType() {
                return CommonUseAction.USE_TYPE.LONG;
            }
        },
        ;

        public abstract CommonUseAction.USE_TYPE toUseType();

        @Override
        public String toString() {
            return name().toLowerCase(Locale.ENGLISH);
        }

        public static Condition of(String name) {
            return Condition.valueOf(name.toUpperCase(Locale.ENGLISH));
        }
    }

    public final ResourceLocation id;
    public final List<ItemStack> inputs;
    public final List<ItemStack> results;

    public final Condition condition;

    public ItemUseRecipe(ResourceLocation id, List<ItemStack> inputs, List<ItemStack> results, Condition condition) {
        this.id = id;
        this.inputs = inputs;
        this.results = results;
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
        NonNullList<Ingredient> ingredients = NonNullList.createWithCapacity(inputs.size());
        inputs.forEach(input -> {
            ingredients.add(Ingredient.of(input));
        });
        return ingredients;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return results.get(0);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ITEM_USE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.ITEM_USE_RECIPE;
    }

    public static class Serializer implements RecipeSerializer<ItemUseRecipe> {

        @Override
        public ItemUseRecipe fromJson(ResourceLocation pRecipeId, JsonObject jsonObject) {
            List<ItemStack> inputs = new ArrayList<>();
            JsonArray inputsJson = jsonObject.getAsJsonArray("inputs");
            inputsJson.forEach(inputElement -> {
                ItemStack itemStack = ShapedRecipe.itemStackFromJson((JsonObject) inputElement);
                inputs.add(itemStack);
            });

            List<ItemStack> results = new ArrayList<>();
            JsonArray resultJson = jsonObject.getAsJsonArray("results");
            resultJson.forEach(inputElement -> {
                ItemStack itemStack = ShapedRecipe.itemStackFromJson((JsonObject) inputElement);
                results.add(itemStack);
            });


            Condition condition = Condition.of(GsonHelper.getAsString(jsonObject, "condition"));
            return new ItemUseRecipe(pRecipeId, inputs, results, condition);
        }

        @Override
        @Nullable
        public ItemUseRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            byte inputSize = pBuffer.readByte();
            List<ItemStack> inputs = new ArrayList<>();
            for (byte i = 0; i < inputSize; i++) {
                inputs.add(pBuffer.readItem());
            }

            byte resultSize = pBuffer.readByte();
            List<ItemStack> results = new ArrayList<>();
            for (byte i = 0; i < resultSize; i++) {
                results.add(pBuffer.readItem());
            }

            Condition condition = Condition.of(pBuffer.readUtf());
            return new ItemUseRecipe(pRecipeId, inputs, results, condition);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ItemUseRecipe pRecipe) {
            List<ItemStack> inputs = pRecipe.inputs;
            pBuffer.writeByte(inputs.size());
            inputs.forEach(pBuffer::writeItem);

            List<ItemStack> results = pRecipe.results;
            pBuffer.writeByte(results.size());
            results.forEach(pBuffer::writeItem);

            pBuffer.writeUtf(pRecipe.condition.toString());
        }
    }

}
