package com.github.catbert.tlma.task.cook.v1.common;

import com.github.catbert.tlma.task.cook.handler.v2.MaidRecipesManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.util.Pair;
import de.cristelknight.doapi.common.world.ImplementedInventory;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.*;

public abstract class TaskLdContainerCook<B extends BlockEntity & ImplementedInventory, R extends Recipe<? extends Container>> extends TaskLdBaseContainerCook<B, R> {
    @Override
    public void insertInputStack(Container inventory, IItemHandlerModifiable availableInv, BlockEntity blockEntity, Pair<List<Integer>, List<List<ItemStack>>> ingredientPair) {
        List<Integer> amounts = ingredientPair.getFirst();
        List<List<ItemStack>> ingredients = ingredientPair.getSecond();

        if (hasEnoughIngredient(amounts, ingredients)) {
            for (int i = getInputStartSlot(), j = 0; i < ingredients.size() + getInputStartSlot(); i++, j++) {
                if (ingredients.get(j) == null || ingredients.get(j).isEmpty()) continue;
                insertAndShrink(inventory, amounts.get(j), ingredients, j, i);
            }
            blockEntity.setChanged();
        }

        updateIngredient(ingredientPair);

    }

    @Override
    public boolean hasEnoughIngredient(List<Integer> amounts, List<List<ItemStack>> ingredients) {
        boolean canInsert = true;

        int i = 0;
        for (List<ItemStack> ingredient : ingredients) {
            if (ingredient == null || ingredient.isEmpty()) continue;
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

    @Override
    @SuppressWarnings("unchecked")
    public MaidRecipesManager<R> getRecipesManager(EntityMaid maid) {
        return new MaidRecipesManager<>(maid, this, false) {
            @Override
            protected Pair<List<Integer>, List<Item>> getAmountIngredient(R recipe, Map<Item, Integer> available) {
                List<Ingredient> ingredients = recipe.getIngredients();
                boolean[] canMake = {true};
                boolean[] single = {false};
                List<Item> invIngredient = new ArrayList<>();
                Map<Item, Integer> itemTimes = new HashMap<>();

                boolean hasStartContainer = extraStartRecipe(recipe, available, canMake, single, itemTimes, invIngredient);
                if (!hasStartContainer) return Pair.of(Collections.EMPTY_LIST, Collections.EMPTY_LIST);

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

                // 补全输入的格子其他空的格子，便于后续加入容器格子...
                // 得改...
                // 别问为什么有前后操作，去问let's do作者，
                // 暂时不想再单独区分了,明明是自成体系的，这里却还不同意...
                // 配方内部额外条件一些模组在原料前面，一些模组是在原料后面...
                int size = ingredients.size();
                int il = getInputSize() - 1 - size;
                if (canMake[0] && il > 0) {
                    for (int i = 0; i < il; i++) {
                        invIngredient.add(null);
                    }
                }

                boolean hasEndContainer = extraEndRecipe(recipe, available, canMake, single, itemTimes, invIngredient);

                if (!canMake[0] || !hasEndContainer || invIngredient.stream().anyMatch(item -> {
                    if (item == null) return false;
                    return available.get(item) <= 0;
                })) {
                    return Pair.of(Collections.EMPTY_LIST, Collections.EMPTY_LIST);
                }

                int maxCount = 64;
                if (single[0] || this.isSingle()) {
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

                return Pair.of(countList, invIngredient);
            }

            @Override
            protected boolean extraStartRecipe(R recipe, Map<Item, Integer> available, boolean[] single, boolean[] canMake, Map<Item, Integer> itemTimes, List<Item> invIngredient) {
               return tExtraStartRecipe(recipe, available, single, canMake, itemTimes, invIngredient);
            }

            @Override
            protected boolean extraEndRecipe(R recipe, Map<Item, Integer> available, boolean[] single, boolean[] canMake, Map<Item, Integer> itemTimes, List<Item> invIngredient) {
               return tExtraEndRecipe(recipe, available, single, canMake, itemTimes, invIngredient);
            }
        };
    }

    protected boolean tExtraStartRecipe(R recipe, Map<Item, Integer> available, boolean[] single, boolean[] canMake, Map<Item, Integer> itemTimes, List<Item> invIngredient) {
        return true;
    }

    protected boolean tExtraEndRecipe(R recipe, Map<Item, Integer> available, boolean[] single, boolean[] canMake, Map<Item, Integer> itemTimes, List<Item> invIngredient) {
        return true;
    }

    protected boolean extraRecipe(Item extraItem, R recipe, Map<Item, Integer> available, boolean[] single, boolean[] canMake, Map<Item, Integer> itemTimes, List<Item> invIngredient) {
        // 别问我为什么这里是硬编码,葡园酒香这里就是硬编码...
        boolean hasIngredient = false;
        for (Item item : available.keySet()) {
            ItemStack stack = item.getDefaultInstance();
            if (stack.is(extraItem)) {
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

            return false;
        }

        return true;
    }
}
