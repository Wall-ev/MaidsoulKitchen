package com.github.wallev.maidsoulkitchen.task.cook.kaleidoscopecookery.cookery;

import com.github.wallev.maidsoulkitchen.foundation.utility.RecIngredient;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ItemDefinition;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.ItemAmount;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidItem;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec.MKRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.recipe.PotRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static com.github.wallev.maidsoulkitchen.task.cook.kaleidoscopecookery.cookery.PotRecSerializerManager.PotRecipeInfoProvider.*;

public class PotRecSerializerManager extends RecSerializerManager<PotRecipe> {
    private static final PotRecSerializerManager INSTANCE = new PotRecSerializerManager();

    protected PotRecSerializerManager() {
        super(ModRecipes.POT_RECIPE);
    }

    public static PotRecSerializerManager getInstance() {
        return INSTANCE;
    }

    @Override
    public LinkedList<MaidRec> createMaidRecs(List<MKRecipe<PotRecipe>> recs, Map<ItemDefinition, Long> available, BiConsumer<MKRecipe<PotRecipe>, Integer> successAdd, Predicate<MKRecipe<PotRecipe>> rIsValid) {
        if (!available.containsKey(ItemDefinition.of(KITCHEN_SHOVEL))) {
            return EMPTY_LIST;
        }
        return super.createMaidRecs(recs, available, successAdd, rIsValid);
    }

    @Override
    protected boolean processRecIngres(MKRecipe<PotRecipe> r, Map<ItemDefinition, Long> available, List<Item> invIngredient, boolean[] single, Map<Item, ItemAmount> itemTimes) {
        return super.processRecIngres(r, available, invIngredient, single, itemTimes);
    }

    @Override
    protected MaidRec createCookRec(MKRecipe<PotRecipe> r, Map<ItemDefinition, Long> available, boolean[] single, List<Item> invIngredient, Map<Item, ItemAmount> itemTimes) {
        ItemStack result = r.output();
        List<MaidItem> maidItems = new ArrayList<>();
        int recAmount = getMaxAmount(available, single, itemTimes);
        for (Item item : invIngredient) {
            int minAmount = itemTimes.get(item).getAmount();

            int count = recAmount * minAmount;
            maidItems.add(new MaidItem(item, count));
            ItemDefinition itemDefinition = ItemDefinition.of(item);
            available.put(itemDefinition, available.get(itemDefinition) - count);
        }
        maidItems.remove(0);
        if (r.rec().isNeedBowl()) {
            maidItems.remove(maidItems.size() - 1);
        }

        return new MaidRec(r.rec(), result, recAmount, OIL.getDefaultInstance(), KITCHEN_SHOVEL.getDefaultInstance(), CONTAINER.getDefaultInstance(),
                maidItems, MaidItem.EMPTY);
    }

    @Override
    protected PotRecipeInfoProvider createRecipeInfoProvider() {
        return new PotRecipeInfoProvider();
    }

    public static class PotRecipeInfoProvider extends RecipeInfoProvider<PotRecipe> {
        public static final Item OIL = ModItems.OIL.get();
        public static final Item CONTAINER = Items.BOWL;
        public static final Item KITCHEN_SHOVEL = ModItems.KITCHEN_SHOVEL.get();
        public static final Item FLINT = Items.FLINT_AND_STEEL;
        public static final Ingredient CONTAINER_INGREDIENT = Ingredient.of(CONTAINER.getDefaultInstance());

        @Override
        public List<RecIngredient> getIngredients(RecSerializerManager<PotRecipe> rsm, PotRecipe rec) {
            List<Ingredient> list = new ArrayList<>();
            list.add(Ingredient.of(OIL));

            for (Ingredient ingredient : rec.getIngredients()) {
                if (!ingredient.isEmpty()) {
                    list.add(ingredient);
                }
            }

            if (rec.isNeedBowl()) {
                list.add(Ingredient.of(CONTAINER));
            }

            return RecIngredient.to(list);
        }
    }
}
