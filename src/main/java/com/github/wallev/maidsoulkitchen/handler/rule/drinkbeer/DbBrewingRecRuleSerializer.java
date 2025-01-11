package com.github.wallev.maidsoulkitchen.handler.rule.drinkbeer;

import com.github.wallev.maidsoulkitchen.handler.rec.CookRec;
import com.github.wallev.maidsoulkitchen.handler.rule.common.AbstractCookRecRuleSerializer;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import lekavar.lma.drinkbeer.recipes.BrewingRecipe;
import lekavar.lma.drinkbeer.registries.RecipeRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;

public class DbBrewingRecRuleSerializer extends AbstractCookRecRuleSerializer<BrewingRecipe, CookRec<BrewingRecipe>> {

    public DbBrewingRecRuleSerializer() {
        super(RecipeRegistry.RECIPE_TYPE_BREWING.get());
    }

    @Override
    public boolean processInvIngres(CookRec<BrewingRecipe> cookRec, Map<Item, Integer> available, List<Item> invIngredient, Map<Item, Integer> itemTimes, boolean[] single) {
        ItemStack beerCup = cookRec.getRec().getBeerCup();
        if (available.getOrDefault(beerCup.getItem(), 0) < beerCup.getCount()) {
            return false;
        }

        boolean processInvIngres = super.processInvIngres(cookRec, available, invIngredient, itemTimes, single);
        if (processInvIngres) {
            itemTimes.merge(beerCup.getItem(), beerCup.getCount(), Integer::sum);
        }

        return processInvIngres;
    }

    @Override
    protected List<Pair<Item, Integer>> createIngres(Map<Item, Integer> available, boolean queryInvIngres, Map<Item, Integer> itemTimes, boolean[] single, List<Item> invIngredient) {
        return super.createIngres(available, queryInvIngres, itemTimes, single, invIngredient);
    }

    @Override
    protected List<Pair<Item, Integer>> queryInvRecIngres(Map<Item, Integer> available, List<Item> invIngredient, int maxCount) {
        List<Pair<Item, Integer>> results = Lists.newArrayList();
        if (maxCount > 4) {
            for (Item item : invIngredient) {
                results.add(Pair.of(item, maxCount));
                available.put(item, available.get(item) - maxCount);
            }
        } else {
            for (int i = 0; i < invIngredient.size() - 1; i++) {
                Item item = invIngredient.get(i);
                results.add(Pair.of(item, maxCount));
                available.put(item, available.get(item) - maxCount);
            }

            Item item = invIngredient.get(invIngredient.size() - 1);
            results.add(Pair.of(item, 4));
            available.put(item, available.get(item) - 4);
        }

        return results;
    }
}
