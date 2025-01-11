package com.github.wallev.maidsoulkitchen.handler.base.ingredient;

import com.github.wallev.maidsoulkitchen.handler.base.mkrecipe.AbstractCookRec;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.*;

@SuppressWarnings("deprecation")
public abstract class AbstractCookRecIngredientSerializer<R extends Recipe<? extends Container>, CR extends AbstractCookRec<R>> {
    protected final RecipeType<R> recipeType;

    public AbstractCookRecIngredientSerializer(RecipeType<R> recipeType) {
        this.recipeType = recipeType;
    }

    public RecipeType<R> getRecipeType() {
        return recipeType;
    }

    /**
     * 获取当前所有符合的原料
     *
     * @param available     容器中现有的原料
     * @param invIngredient 当前需要的原料
     * @param maxCount      最大份量
     * @return 符合的原料
     */
    protected List<Pair<Item, Integer>> queryInvRecIngres(Map<Item, Integer> available, List<Item> invIngredient, int maxCount) {
        List<Pair<Item, Integer>> results = Lists.newArrayList();
        for (Item item : invIngredient) {
            results.add(Pair.of(item, maxCount));
            available.put(item, available.get(item) - maxCount);
        }
        return results;
    }

    /**
     * 获取当前所有符合的原料的最大份量（最大为64份）
     *
     * @param available 容器中现有的原料
     * @param single    是否只能使用一份原料
     * @param itemTimes 每一个原料所需要的份量
     * @return 最大份量
     */
    protected static int queryMaxCount(Map<Item, Integer> available, boolean[] single, Map<Item, Integer> itemTimes) {
        int maxCount = 64;
        if (single[0]) {
            maxCount = 1;
        } else {
            for (Item item : itemTimes.keySet()) {
                maxCount = Math.min(maxCount, item.getMaxStackSize());
                maxCount = Math.min(maxCount, available.get(item) / itemTimes.get(item));
            }
        }
        return maxCount;
    }

    /**
     * 检查当前容器中是否有足够的原料
     *
     * @param available 容器中现有的原料
     * @param itemTimes 每一个原料所需要的份量
     * @return 是否含有足够的原料
     */
    protected static boolean hasEnoughIngres(Map<Item, Integer> available, Map<Item, Integer> itemTimes) {
        for (Map.Entry<Item, Integer> itemIntegerEntry : itemTimes.entrySet()) {
            if (available.get(itemIntegerEntry.getKey()) < itemIntegerEntry.getValue()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取当前所有符合的原料
     *
     * @param cookRec   当前需要获取的菜谱
     * @param available 容器中现有的原料
     * @return 符合的原料
     */
    public List<Pair<Item, Integer>> getAmountIngredient2(CR cookRec, Map<Item, Integer> available) {
        HashMap<Item, Integer> retainAvailable = Maps.newHashMap(available);
        retainAvailable.keySet().retainAll(cookRec.getValidItems());
        if (retainAvailable.isEmpty()) {
            return Collections.emptyList();
        }

        List<Item> invIngredient = new ArrayList<>();
        Map<Item, Integer> itemTimes = new HashMap<>();
        boolean[] single = {cookRec.isSingle()};

        boolean queryInvIngres = processInvIngres(cookRec, retainAvailable, invIngredient, itemTimes, single);

        return createIngres(retainAvailable, queryInvIngres, itemTimes, single, invIngredient);
    }

    protected List<Pair<Item, Integer>> createIngres(Map<Item, Integer> available, boolean queryInvIngres, Map<Item, Integer> itemTimes, boolean[] single, List<Item> invIngredient) {
        if (!queryInvIngres || !hasEnoughIngres(available, itemTimes)) {
            return Collections.emptyList();
        }

        int maxCount = queryMaxCount(available, single, itemTimes);

        return queryInvRecIngres(available, invIngredient, maxCount);
    }

    protected Pair<List<Integer>, List<Item>> getAmountIngredient(CR cookRec, Map<Item, Integer> available) {
        List<List<Item>> recIngres = cookRec.getIngres();
        Set<Item> itemSet = available.keySet();

        List<Item> invIngredient = new ArrayList<>();
        List<Integer> countList = new ArrayList<>();
        Map<Item, Integer> itemTimes = new HashMap<>();
        boolean[] single = {false};

        boolean queryInvIngres = processInvIngres(recIngres, itemSet, invIngredient, itemTimes, single);

        if (!queryInvIngres || !hasEnoughIngres(available, itemTimes)) {
            return Pair.of(Collections.emptyList(), Collections.emptyList());
        }

        int maxCount = queryMaxCount(available, single, itemTimes);

        for (Item item : invIngredient) {
            countList.add(maxCount);
            available.put(item, available.get(item) - maxCount);
        }

        return Pair.of(countList, invIngredient);
    }

    /**
     * 处理来自容器（比如女仆的背包）的原料
     * <p>自动捕捉符合配方的原料
     *
     * @param cookRec       对应的配方信息
     * @param available     容器中存在的原料以及存在的份量
     * @param invIngredient 缓存的容器的配方原料
     * @param single        最后得出的份量是否为单份
     * @param itemTimes     每一个原料所需要的份量
     * @return 最终容器中时候含有符合配方的原料
     */
    public boolean processInvIngres(CR cookRec, Map<Item, Integer> available, List<Item> invIngredient, Map<Item, Integer> itemTimes, boolean[] single) {
        List<List<Item>> recIngres = cookRec.getIngres();
        Set<Item> itemSet = available.keySet();
        return processInvIngres(recIngres, itemSet, invIngredient, itemTimes, single);
    }

    /**
     * 处理来自容器（比如女仆的背包）的原料
     * <p>自动捕捉符合配方的原料
     *
     * @param recIngres     配方所需要的原料
     * @param itemSet       容器中存在的原料
     * @param invIngredient 缓存的容器的配方原料
     * @param single        最后得出的份量是否为单份
     * @param itemTimes     每一个原料所需要的份量
     * @return 最终容器中时候含有符合配方的原料
     */
    protected boolean processInvIngres(List<List<Item>> recIngres, Set<Item> itemSet, List<Item> invIngredient, Map<Item, Integer> itemTimes, boolean[] single) {
        for (List<Item> recIngre : recIngres) {
            boolean hasIngredient = false;
            for (Item item : itemSet) {
                if (recIngre.contains(item)) {
                    invIngredient.add(item);
                    hasIngredient = true;
                    if (item.getMaxStackSize() == 1) {
                        single[0] = true;
                        itemTimes.put(item, 1);
                    } else {
                        itemTimes.merge(item, 1, Integer::sum);
                    }

                    break;
                }
            }

            if (!hasIngredient) {
                itemTimes.clear();
                invIngredient.clear();
                return false;
            }
        }

        return true;
    }
}