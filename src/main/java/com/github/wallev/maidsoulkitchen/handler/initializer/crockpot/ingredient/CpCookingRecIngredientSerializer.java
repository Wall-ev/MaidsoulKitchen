package com.github.wallev.maidsoulkitchen.handler.initializer.crockpot.ingredient;

import com.github.wallev.maidsoulkitchen.handler.base.mkrecipe.CrockPotCookingRec;
import com.github.wallev.maidsoulkitchen.handler.base.ingredient.AbstractCookRecIngredientSerializer;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.recipe.CrockPotRecipes;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.recipe.cooking.requirement.IRequirement;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCategoryMax;
import net.minecraft.world.item.Item;
import org.apache.commons.compress.utils.Lists;

import java.util.*;

public class CpCookingRecIngredientSerializer extends AbstractCookRecIngredientSerializer<CrockPotCookingRecipe, CrockPotCookingRec> {

    public CpCookingRecIngredientSerializer() {
        super(CrockPotRecipes.CROCK_POT_COOKING_RECIPE_TYPE.get());
    }

    private static void removeNoRequiresItems(List<Pair<RequirementCategoryMax, Set<Item>>> noRequires, Set<Item> itemSet) {
        for (Pair<RequirementCategoryMax, Set<Item>> noRequire : noRequires) {
            Set<Item> nonItems = noRequire.getSecond();
            itemSet.removeAll(nonItems);
        }
    }

    private static <IR extends IRequirement> boolean processRequires(int times, List<Pair<IR, Set<Item>>> requirementItems, Set<Item> itemSet, int[] leftUnlockSlot, boolean[] single, List<Item> invIngredient, Map<Item, Integer> itemTimes) {
        if (requirementItems.isEmpty()) return false;

        for (Pair<IR, Set<Item>> requirementItem : requirementItems) {
            Set<Item> quireItems = requirementItem.getSecond();
            for (int i = 0; i < times && leftUnlockSlot[0] > 0; i++) {
                boolean hasIngredient = false;
                for (Item item : itemSet) {
                    if (quireItems.contains(item)) {
                        leftUnlockSlot[0]--;
                        invIngredient.add(item);
                        if (item.getMaxStackSize() == 1) {
                            single[0] = true;
                            itemTimes.put(item, 1);
                        } else {
                            itemTimes.merge(item, 1, Integer::sum);
                        }
                        hasIngredient = true;
                        break;
                    }
                }
                if (!hasIngredient) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public List<Pair<Item, Integer>> getAmountIngredient2(CrockPotCookingRec cookRec, Map<Item, Integer> available) {
        Map<Item, Integer> retainAvailable = Maps.newHashMap(available);
        retainAvailable.keySet().retainAll(cookRec.getValidItems());
        if (retainAvailable.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Item> itemSet = new HashSet<>(retainAvailable.keySet());
        // 移除不符合配方的原材料
        removeNoRequiresItems(cookRec.getNoRequires(), itemSet);
        if (itemSet.isEmpty()) {
            return Collections.emptyList();
        }

        int[] leftUnlockSlot = {4};
        boolean[] single = {false};
        List<Item> invIngredient = Lists.newArrayList();
        Map<Item, Integer> itemTimes = Maps.newHashMap();

        // 正常情况下处理，一般来说一个配方会有四种供需的类型
        {
            // 任意
            boolean processAnyRequires = processRequires(1, cookRec.getAnyRequires(), itemSet, leftUnlockSlot, single, invIngredient, itemTimes);
            if (!processAnyRequires) {
                return Collections.emptyList();
            }
            if (leftUnlockSlot[0] == 0) {
                return createIngres(retainAvailable, true, itemTimes, single, invIngredient);
            }
            // 必须含有
            boolean processMustRequires = processRequires(1, cookRec.getMustRequires(), itemSet, leftUnlockSlot, single, invIngredient, itemTimes);
            if (!processMustRequires) {
                return Collections.emptyList();
            }
            if (leftUnlockSlot[0] == 0) {
                return createIngres(retainAvailable, true, itemTimes, single, invIngredient);
            }
            // >=
            boolean processMinRequires = processRequires(1, cookRec.getMinRequires(), itemSet, leftUnlockSlot, single, invIngredient, itemTimes);
            if (!processMinRequires) {
                return Collections.emptyList();
            }
            if (leftUnlockSlot[0] == 0) {
                return createIngres(retainAvailable, true, itemTimes, single, invIngredient);
            }
            // >
            boolean processMinERequires = processRequires(1, cookRec.getMinERequires(), itemSet, leftUnlockSlot, single, invIngredient, itemTimes);
            if (!processMinERequires) {
                return Collections.emptyList();
            }
            if (leftUnlockSlot[0] == 0) {
                return createIngres(retainAvailable, true, itemTimes, single, invIngredient);
            }
            // <=
            boolean processMaxRequires = processRequires(1, cookRec.getMaxRequires(), itemSet, leftUnlockSlot, single, invIngredient, itemTimes);
            if (!processMaxRequires) {
                return Collections.emptyList();
            }
            if (leftUnlockSlot[0] == 0) {
                return createIngres(retainAvailable, true, itemTimes, single, invIngredient);
            }
            // <
            boolean processMaxERequires = processRequires(1, cookRec.getMaxERequires(), itemSet, leftUnlockSlot, single, invIngredient, itemTimes);
            if (!processMaxERequires) {
                return Collections.emptyList();
            }
            if (leftUnlockSlot[0] == 0) {
                return createIngres(retainAvailable, true, itemTimes, single, invIngredient);
            }
        }

        // 如果上述处理完还有空余的格子，那就再来处理一遍，
        // 这个时候就还表明当前配方供需的类型不满足4种，
        // 有重复的，那就捕获重复的那种类型进行一次处理
        // 当然还是有瑕疵的，待优化
        // @todo optimize
        if (leftUnlockSlot[0] > 0) {
            // 任意
            boolean processAnyRequires = processRequires(cookRec.getAnyRequires().isEmpty() ? 0 : cookRec.getAnyRequires().size() >= leftUnlockSlot[0] ? 1 : leftUnlockSlot[0],
                    cookRec.getAnyRequires(), itemSet, leftUnlockSlot, single, invIngredient, itemTimes);
            if (!processAnyRequires) {
                return Collections.emptyList();
            }
            if (leftUnlockSlot[0] == 0) {
                return createIngres(retainAvailable, true, itemTimes, single, invIngredient);
            }
            // 必须含有
            boolean processMustRequires = processRequires(cookRec.getMustRequires().isEmpty() ? 0 : cookRec.getMustRequires().size() >= leftUnlockSlot[0] ? 1 : leftUnlockSlot[0],
                    cookRec.getMustRequires(), itemSet, leftUnlockSlot, single, invIngredient, itemTimes);
            if (!processMustRequires) {
                return Collections.emptyList();
            }
            if (leftUnlockSlot[0] == 0) {
                return createIngres(retainAvailable, true, itemTimes, single, invIngredient);
            }
            // >=
            boolean processMinRequires = processRequires(cookRec.getMinRequires().isEmpty() ? 0 : cookRec.getMinRequires().size() >= leftUnlockSlot[0] ? 1 : leftUnlockSlot[0],
                    cookRec.getMinRequires(), itemSet, leftUnlockSlot, single, invIngredient, itemTimes);
            if (!processMinRequires) {
                return Collections.emptyList();
            }
            if (leftUnlockSlot[0] == 0) {
                return createIngres(retainAvailable, true, itemTimes, single, invIngredient);
            }
            // >
            boolean processMinERequires = processRequires(cookRec.getMinERequires().isEmpty() ? 0 : cookRec.getMinERequires().size() >= leftUnlockSlot[0] ? 1 : leftUnlockSlot[0],
                    cookRec.getMinERequires(), itemSet, leftUnlockSlot, single, invIngredient, itemTimes);
            if (!processMinERequires) {
                return Collections.emptyList();
            }
            if (leftUnlockSlot[0] == 0) {
                return createIngres(retainAvailable, true, itemTimes, single, invIngredient);
            }
            // <=
            boolean processMaxRequires = processRequires(cookRec.getMaxRequires().isEmpty() ? 0 : cookRec.getMustRequires().size() >= leftUnlockSlot[0] ? 1 : leftUnlockSlot[0],
                    cookRec.getMaxRequires(), itemSet, leftUnlockSlot, single, invIngredient, itemTimes);
            if (!processMaxRequires) {
                return Collections.emptyList();
            }
            if (leftUnlockSlot[0] == 0) {
                return createIngres(retainAvailable, true, itemTimes, single, invIngredient);
            }
            // <
            boolean processMaxERequires = processRequires(cookRec.getMaxERequires().isEmpty() ? 0 : cookRec.getMaxERequires().size() >= leftUnlockSlot[0] ? 1 : leftUnlockSlot[0],
                    cookRec.getMaxERequires(), itemSet, leftUnlockSlot, single, invIngredient, itemTimes);
            if (!processMaxERequires) {
                return Collections.emptyList();
            }
            if (leftUnlockSlot[0] == 0) {
                return createIngres(retainAvailable, true, itemTimes, single, invIngredient);
            }
        }

        return Collections.emptyList();
    }

    @Override
    public boolean processInvIngres(CrockPotCookingRec cookRec, Map<Item, Integer> available, List<Item> invIngredient, Map<Item, Integer> itemTimes, boolean[] single) {
        return super.processInvIngres(cookRec, available, invIngredient, itemTimes, single);
    }
}
