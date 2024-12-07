package com.github.wallev.farmsoulkitchen.task.cook.v1.common;

import com.github.wallev.farmsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.*;

public abstract class TaskFdCiCook<B extends BlockEntity, R extends Recipe<? extends Container>> extends TaskFdPot<B, R> {

    @Override
    public void insertInputsStack(ItemStackHandler beInv, IItemHandlerModifiable maidInv, B be, Pair<List<Integer>, List<List<ItemStack>>> ingredientPair) {
        List<Integer> amounts = ingredientPair.getFirst();
        List<List<ItemStack>> ingredients = ingredientPair.getSecond();

        if (hasEnoughIngredient(amounts, ingredients)) {
            for (int i = getInputStartSlot(), j = 0; i < ingredients.size() + getInputStartSlot(); i++, j++) {
                if (ingredients.get(j).isEmpty()) continue;
                insertAndShrink(beInv, amounts.get(i), ingredients, j, i);
            }
            be.setChanged();
        }

        updateIngredient(ingredientPair);
    }

    @Override
    public boolean hasEnoughIngredient(List<Integer> amounts, List<List<ItemStack>> ingredients) {
        boolean canInsert = true;

        int i = 0;
        for (List<ItemStack> ingredient : ingredients) {
            if (ingredient.isEmpty()) continue;

            int actualCount = amounts.get(i++);
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

    public List<Pair<List<Integer>, List<List<ItemStack>>>> rmTransform(Map<Item, List<ItemStack>> inventoryStack, List<Pair<List<Integer>, List<Item>>> oriList) {
        return oriList.stream().map(p -> {
            List<List<ItemStack>> list = p.getSecond().stream().map(item -> {
                if (item == null) return new ArrayList<ItemStack>();
                return inventoryStack.get(item);
            }).toList();
            return Pair.of(p.getFirst(), list);
        }).toList();
    }

    @SuppressWarnings("unchecked")
    public Pair<List<Integer>, List<Item>> rmGetAmountIngredient(R recipe, Map<Item, Integer> available, boolean isSingle) {
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
        // 实在不明白为啥要把fluidItem加入ingredient里，
        // 然后又在需要配方判断的时候把fluidItem去掉....
        int size = ingredients.size();
        int il = getInputSize() - size;
        if (canMake[0] && il > 0) {
            for (int i = 0; i < il; i++) {
                invIngredient.add(size - 1 + i , null);
            }
        }

        if (!canMake[0] || invIngredient.stream().anyMatch(item -> {
            if (item == null) return false;
            return available.get(item) <= 0;
        })) {
            return Pair.of(Collections.EMPTY_LIST, Collections.EMPTY_LIST);
        }

        int maxCount = 64;
        if (single[0] || isSingle) {
            maxCount = 1;
        } else {
            for (Item item : itemTimes.keySet()) {
                if (item == null) continue;
                maxCount = Math.min(maxCount, available.get(item) / itemTimes.get(item));
            }
        }

        List<Integer> countList = new ArrayList<>();
        for (Item item : invIngredient) {
            if (item == null) {
                countList.add(0);
            } else {
                countList.add(maxCount);
                available.put(item, available.get(item) - maxCount);
            }
        }

        return Pair.of(countList, new ArrayList<>(invIngredient));
    }

    @Override
    public MaidRecipesManager<R> getRecipesManager(EntityMaid maid) {
        return new MaidRecipesManager<>(maid, this, false) {
            @Override
            protected List<Pair<List<Integer>, List<List<ItemStack>>>> transform(List<Pair<List<Integer>, List<Item>>> oriList, Map<Item, Integer> available) {
                return rmTransform(this.getCookInv().getInventoryStack(), oriList);
            }

            @Override
            protected Pair<List<Integer>, List<Item>> getAmountIngredient(R recipe, Map<Item, Integer> available) {
                return rmGetAmountIngredient(recipe, available, this.isSingle());
            }
        };
    }

}
