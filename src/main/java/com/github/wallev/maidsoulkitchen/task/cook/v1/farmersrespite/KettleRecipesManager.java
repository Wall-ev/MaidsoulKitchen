package com.github.wallev.maidsoulkitchen.task.cook.v1.farmersrespite;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import umpaz.farmersrespite.common.crafting.KettleRecipe;

import java.util.*;

public class KettleRecipesManager extends MaidRecipesManager<KettleRecipe> {
    public KettleRecipesManager(EntityMaid maid, TaskFrKettle task) {
        super(maid, task, false);
    }

    @Override
    protected Pair<List<Integer>, List<Item>> getAmountIngredient(KettleRecipe recipe, Map<Item, Integer> available) {
        TaskFrKettle.MaidKettleRecipe maidKettleRecipe = TaskFrKettle.KEY_RECIPE_INGREDIENTS.get(recipe);
        List<Item> invIngredient = new ArrayList<>();
        Map<Item, Integer> itemTimes = new HashMap<>();
        boolean[] single = {false};

        // 流体
        boolean hasFluidItem = false;
        int fluidItemAmount = 0;
        Item fluidItem = ItemStack.EMPTY.getItem();
        for (ItemStack ingredient : maidKettleRecipe.inFluids()) {
            boolean hasIngredient = false;
            for (Item item : available.keySet()) {
                if (ingredient.is(item) && available.get(item) >= ingredient.getCount()) {
                    invIngredient.add(item);
                    hasIngredient = true;

                    if (item.getMaxStackSize() == 1) {
                        single[0] = true;
                        itemTimes.put(item, 1);
                    } else {
                        itemTimes.merge(item, 1, Integer::sum);
                    }

                    fluidItemAmount = ingredient.getCount();
                    fluidItem = item;
                    single[0] = true;

                    break;
                }
            }

            if (hasIngredient) {
                hasFluidItem = true;
                break;
            }
        }
        if (!maidKettleRecipe.inFluids().isEmpty() && !hasFluidItem) {
            return Pair.of(Collections.emptyList(), Collections.emptyList());
        }

        // 原材料
        for (Ingredient ingredient : maidKettleRecipe.inItems()) {
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
                return Pair.of(Collections.emptyList(), Collections.emptyList());
            }
        }

        // 检查是否缺少材料
        if (itemTimes.entrySet().stream().anyMatch(entry -> available.get(entry.getKey()) < entry.getValue())) {
            return Pair.of(Collections.emptyList(), Collections.emptyList());
        }

        // 计算最大合成次数
        int maxCount = 64;
        if (single[0]) {
            maxCount = 1;
        } else {
            for (Item item : itemTimes.keySet()) {
                maxCount = Math.min(maxCount, item.getDefaultInstance().getMaxStackSize());
                maxCount = Math.min(maxCount, available.get(item) / itemTimes.get(item));
            }
        }

        // 计算每个物品的数量
        List<Integer> countList = new ArrayList<>();
        if (!maidKettleRecipe.inFluids().isEmpty()) {
            countList.add(0, fluidItemAmount);
            available.put(fluidItem, available.get(fluidItem) - fluidItemAmount);
        } else {
            countList.add(0, 0);
            invIngredient.add(0, ItemStack.EMPTY.getItem());
        }
        for (Item item : invIngredient.stream().skip(1).toList()) {
            countList.add(maxCount);
            available.put(item, available.get(item) - maxCount);
        }

        return Pair.of(countList, invIngredient);
    }
}
