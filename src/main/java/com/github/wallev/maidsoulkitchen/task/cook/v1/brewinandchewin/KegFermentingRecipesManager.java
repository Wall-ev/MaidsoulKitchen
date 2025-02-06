package com.github.wallev.maidsoulkitchen.task.cook.v1.brewinandchewin;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import umpaz.brewinandchewin.common.crafting.KegFermentingRecipe;

import java.util.*;

import static umpaz.brewinandchewin.common.block.entity.KegBlockEntity.isValidTemp;

public class KegFermentingRecipesManager extends MaidRecipesManager<KegFermentingRecipe> {
    private Map<Integer, List<Integer>> temperateListIngredients;
    public KegFermentingRecipesManager(EntityMaid maid, TaskBncKeg task) {
        super(maid, task, false);

        temperateListIngredients = new HashMap<>();
    }

    public boolean hasRecipeIngredients() {
        return !temperateListIngredients.isEmpty();
    }

    public boolean hasRecipeIngredientsWithTemp(int temp) {
        for (Integer integer : temperateListIngredients.keySet()) {
            if (isValidTemp(temp, integer)) {
                return true;
            }
        }

        return false;

//        return !temperateListIngredients.getOrDefault(temp, Collections.emptyList()).isEmpty();
    }

    public Pair<List<Integer>, List<List<ItemStack>>> getRecipeIngredient(int temp) {
        int tempTemp = -1;
        for (Integer integer : temperateListIngredients.keySet()) {
            if (isValidTemp(temp, integer)) {
                tempTemp = integer;
                break;
            }
        }

        List<Integer> orDefault = temperateListIngredients.getOrDefault(tempTemp, Collections.emptyList());
        if (orDefault.isEmpty()) return Pair.of(Collections.emptyList(), Collections.emptyList());

        int remove = orDefault.remove(0);
        if (orDefault.isEmpty()) temperateListIngredients.remove(temp);
        return recipesIngredients.remove(remove);
    }

    @NotNull
    protected List<Pair<List<Integer>, List<Item>>> getRecIngreMake(Map<Item, Integer> available) {
        if (temperateListIngredients == null) {
            temperateListIngredients = new HashMap<>();
        }

        List<Pair<List<Integer>, List<Item>>> _make = new ArrayList<>();
        int index = 0;
        for (KegFermentingRecipe r : this.currentRecs) {
            Pair<List<Integer>, List<Item>> maxCount = this.getAmountIngredient(r, available);
            if (!maxCount.getFirst().isEmpty()) {
                _make.add(Pair.of(maxCount.getFirst(), maxCount.getSecond()));

                if (temperateListIngredients.containsKey(r.getTemperature())) {
                    temperateListIngredients.get(r.getTemperature()).add(index);
                } else {
                    temperateListIngredients.put(r.getTemperature(), Lists.newArrayList(index));
                }
                index++;
            }
        }
        repeat(_make, available, this.repeatTimes);
        return _make;
    }


    @Override
    protected Pair<List<Integer>, List<Item>> getAmountIngredient(KegFermentingRecipe recipe, Map<Item, Integer> available) {
        TaskBncKeg.MaidKegRecipe maidKettleRecipe = TaskBncKeg.KEG_RECIPE_INGREDIENTS.get(recipe);
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

            for (Item item : invIngredient.stream().skip(1).toList()) {
                countList.add(maxCount);
                available.put(item, available.get(item) - maxCount);
            }
        } else {
            countList.add(0, 0);
            invIngredient.add(0, ItemStack.EMPTY.getItem());
        }

        return Pair.of(countList, invIngredient);
    }
}
