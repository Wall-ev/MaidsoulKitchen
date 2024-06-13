package com.catbert.tlma.task.cook.bnc;

import com.catbert.tlma.TLMAddon;
import com.catbert.tlma.foundation.utility.Mods;
import com.catbert.tlma.mixin.bnc.KegBlockEntityAccessor;
import com.catbert.tlma.task.cook.common.TaskFdPot;
import com.catbert.tlma.task.cook.handler.MaidRecipesManager;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;
import umpaz.brewinandchewin.common.crafting.KegRecipe;
import umpaz.brewinandchewin.common.registry.BCBlocks;
import umpaz.brewinandchewin.common.registry.BCRecipeTypes;

import java.util.*;

@LittleMaidExtension
public class TaskBncKey extends TaskFdPot<KegRecipe, KegBlockEntity> {

    public static final ResourceLocation NAME = new ResourceLocation(TLMAddon.MOD_ID, "bnc_key");

    @Override
    public boolean modLoaded() {
        return Mods.BNC.isLoaded;
    }

    @Override
    public void insertInputStack(ItemStackHandler inventory, CombinedInvWrapper availableInv, BlockEntity blockEntity, Pair<Integer, List<List<ItemStack>>> ingredientPair) {
        Integer amount = ingredientPair.getFirst();
        List<List<ItemStack>> ingredients = ingredientPair.getSecond();

        if (hasEnoughIngredient(amount, ingredients)) {
            for (int i = getInputStartSlot(), j = 0; i < ingredients.size() + getInputStartSlot(); i++, j++) {
                if (ingredients.isEmpty()) continue;
                insertAndShrink(inventory, amount, ingredients, j, i);
            }
            blockEntity.setChanged();
        }

        updateIngredient(ingredientPair);
    }

    @Override
    public boolean hasEnoughIngredient(Integer amount, List<List<ItemStack>> ingredients) {
        boolean canInsert = true;

        for (List<ItemStack> ingredient : ingredients) {
            if (ingredient.isEmpty()) continue;
            int actualCount = amount;
            for (ItemStack itemStack : ingredient) {
                actualCount -= itemStack.getCount();
                if (actualCount <= 0) {
                    break;
                }
            }

            if (actualCount > 0) {
                canInsert = false;
                break;
            }
        }

        return canInsert;
    }

    @Override
    public MaidRecipesManager<KegRecipe> getRecipesManager(EntityMaid maid) {
        return new MaidRecipesManager<>(maid, getRecipeType(), false) {
            @Override
            protected List<Pair<Integer, List<List<ItemStack>>>> transform(List<Pair<Integer, List<Item>>> oriList) {
                Map<Item, List<ItemStack>> inventoryStack = this.getMaidInventory().getInventoryStack();
                return oriList.stream().map(p -> {
                    List<List<ItemStack>> list = p.getSecond().stream().map(item -> {
                        if (item == null) return new ArrayList<ItemStack>();
                        return inventoryStack.get(item);
                    }).toList();
                    return Pair.of(p.getFirst(), list);
                }).toList();
            }

            @Override
            protected Pair<Integer, List<Item>> getAmountIngredient(KegRecipe recipe, Map<Item, Integer> available) {
                List<Ingredient> ingredients = recipe.getIngredients();
                boolean[] canMake = {true};
                boolean[] single = {false};
                List<Item> invIngredient = new ArrayList<>();
                Map<Item, Integer> itemTimes = new HashMap<>();

                for (Ingredient ingredient : ingredients) {
                    boolean hasIngredient = false;
                    for (Item item : available.keySet()) {
                        ItemStack stack = item.getDefaultInstance();
                        if (ingredient.test(stack)) {
                            invIngredient.add(item);
                            hasIngredient = true;

                            if (stack.getMaxStackSize() == 1) {
                                single[0] = true;
                                itemTimes.put(item, 1);
                            } else {
                                itemTimes.merge(item, 1, Integer::sum);
                            }

                            break;
                        }
                    }

                    if (!hasIngredient) {
                        canMake[0] = false;
                        itemTimes.clear();
                        invIngredient.clear();
                        break;
                    }
                }

                // 饮酒作乐里的ingredient里包含了fluidItem...
                int size = ingredients.size();
                int il = getInputSize() - size;
                if (canMake[0] && il > 0) {
                    for (int i = 0; i < il; i++) {
                        invIngredient.add(size + i - 1, null);
                    }
                }

                if (!canMake[0] || invIngredient.stream().anyMatch(item -> {
                    if (item == null) return false;
                    return available.get(item) <= 0;
                })) {
                    return Pair.of(0, new ArrayList<>());
                }

                int maxCount = 64;
                if (single[0] || this.single) {
                    maxCount = 1;
                } else {
                    for (Item item : itemTimes.keySet()) {
                        if (item == null) continue;
                        maxCount = Math.min(maxCount, available.get(item) / itemTimes.get(item));
                    }
                }

                for (Item item : invIngredient) {
                    if (item == null) continue;
                    available.put(item, available.get(item) - maxCount);
                }

                return Pair.of(maxCount, invIngredient);
            }
        };
    }

    @Override
    public ItemStackHandler getItemStackHandler(KegBlockEntity be) {
        return be.getInventory();
    }

    @Override
    public Optional<KegRecipe> getMatchingRecipe(KegBlockEntity be, RecipeWrapper recipeWrapper) {
        return ((KegBlockEntityAccessor) be).getMatchingRecipe$tlma(recipeWrapper);
    }

    @Override
    public boolean canCook(KegBlockEntity be, KegRecipe recipe) {
        return ((KegBlockEntityAccessor) be).canCook$tlma(recipe, be.getLevel());
    }

    @Override
    public int getOutputSlot() {
        return KegBlockEntity.OUTPUT_SLOT;
    }

    @Override
    public int getInputSize() {
        return 5;
    }

    @Override
    public int getMealStackSlot() {
        return KegBlockEntity.DRINK_DISPLAY_SLOT;
    }

    @Override
    public int getContainerStackSlot() {
        return KegBlockEntity.CONTAINER_SLOT;
    }

    @Override
    public ItemStack getFoodContainer(KegBlockEntity blockEntity) {
        return blockEntity.getContainer();
    }

    @Override
    public boolean isHeated(KegBlockEntity be) {
        return true;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof KegBlockEntity;
    }

    @Override
    public RecipeType<KegRecipe> getRecipeType() {
        return BCRecipeTypes.FERMENTING.get();
    }

    @Override
    public ResourceLocation getUid() {
        return NAME;
    }

    @Override
    public ItemStack getIcon() {
        return BCBlocks.KEG.get().asItem().getDefaultInstance();
    }
}
