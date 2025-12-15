package com.github.wallev.maidsoulkitchen.compat.msm.common.util;

import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.google.common.collect.Lists;
import com.renyigesai.bakeries.init.BakeriesItems;
import com.renyigesai.bakeries.item.CakeRollItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class DynamicAddRecipeHelper {

    public static Map<ItemStack, DynamicRecipeData> MAP = new java.util.HashMap<>();

    public record DynamicRecipeData(
            ResourceLocation id,
            List<Ingredient> ingredients,
            List<Integer> ingredientCounts,
            List<ItemStack> output,
            Function<List<ItemStack>, @Nullable CraftGuideData> craftGuideSupplier
    ) {
    }

    public static DynamicRecipeData createDynamicRecipeData(ItemStack itemStack) {
        return MAP.get(itemStack);
    }

    public static DynamicRecipeData create(ItemStack stack) {
        if (stack.is(BakeriesItems.CAKE_ROLL.get())) {
            List<ItemStack> inventoryItems = CakeRollItem.getInventoryList(stack);

            List<Ingredient> allIngredients = new ArrayList<>();
            allIngredients.add(Ingredient.of(BakeriesItems.SILICONE_PAPER.get()));
            allIngredients.add(Ingredient.of(BakeriesItems.CUT_CAKE_BASE.get()));
            List<Ingredient> beInvItems = inventoryItems.stream()
                    .map(itemstack -> itemstack.copyWithCount(1))
                    .map(Ingredient::of)
                    .toList();
            allIngredients.addAll(beInvItems);

            List<Integer> ingredientCounts = allIngredients.stream().map(ingredient -> ingredient.getItems()[0].getCount()).toList();

            List<ItemStack> output = Lists.newArrayList(stack);

            String id;
            ResourceLocation stackId = getRegistryName(stack);
            id = stackId.toString();
            if (!inventoryItems.isEmpty()) {
                ItemStack itemStack = inventoryItems.get(0);
                ResourceLocation firstStack = getRegistryName(itemStack);
                id += "/" + firstStack.toString();
            }
            id = id.replaceAll(":", "_");

            return new DynamicRecipeData(
                    VResourceLocation.ofMod(id),
                    allIngredients,
                    ingredientCounts,
                    output,
                    (list) -> {
                        return null;
                    }
            );
        }

        return null;
    }

    public static ResourceLocation getRegistryName(ItemStack stack) {
        return ForgeRegistries.ITEMS.getKey(stack.getItem());
    }

}
