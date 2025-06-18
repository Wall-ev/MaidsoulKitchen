package com.github.wallev.maidsoulkitchen.task.cook.kaleidoscopecookery.choppingboard;

import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ingredient.RecIngredient;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.IndexRange;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemDefinition;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.ItemAmount;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.ToolRecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec.MKRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.tag.TagItem;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.recipe.ChoppingBoardRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ChoppingBoardRecSerializerManager extends ToolRecSerializerManager<ChoppingBoardRecipe> {
    private static final ChoppingBoardRecSerializerManager INSTANCE = new ChoppingBoardRecSerializerManager();

    protected ChoppingBoardRecSerializerManager() {
        super(ModRecipes.CHOPPING_BOARD_RECIPE);
    }

    @Override
    public LinkedList<MaidRec> createMaidRecs(List<MKRecipe<ChoppingBoardRecipe>> recs, Map<ItemDefinition, Long> available, BiConsumer<MKRecipe<ChoppingBoardRecipe>, IndexRange> successAdd, Predicate<MKRecipe<ChoppingBoardRecipe>> rIsValid, Predicate<Map<ItemDefinition, ItemAmount>> itemUse) {
//        if (getTool(available) == null) {
//            return EMPTY_LIST;
//        }

        return super.createMaidRecs(recs, available, successAdd, rIsValid, itemUse);
    }

    @Override
    protected List<MaidRec> createCookRec(MKRecipe<ChoppingBoardRecipe> r, ItemStack tool, Map<ItemDefinition, Long> available, boolean[] single, List<ItemDefinition> invIngredient, Map<ItemDefinition, ItemAmount> itemTimes) {
        return super.createCookRec(r, tool, available, single, invIngredient, itemTimes);
    }
//
//    @Nullable
//    public static Item getTool(Map<ItemDefinition, Long> available) {
//        for (Item item : available.keySet()) {
//            if (ChoppingRecipeInfoProvider.TOOL_ITEMS.contains(item)) {
//                return item;
//            }
//        }
//        return null;
//    }

    public static ChoppingBoardRecSerializerManager getInstance() {
        return INSTANCE;
    }

    @Override
    protected ToolRecipeInfoProvider<ChoppingBoardRecipe> createRecipeInfoProvider() {
        return new ChoppingRecipeInfoProvider();
    }

    public static class ChoppingRecipeInfoProvider extends ToolRecipeInfoProvider<ChoppingBoardRecipe> {
//        public static final Ingredient TOOL = Ingredient.of(TagItem.KITCHEN_KNIFE);
        public static final Ingredient TOOL = Ingredient.of(TagItem.KITCHEN_KNIFE);
        public static final Set<Item> TOOL_ITEMS = Arrays.stream(TOOL.getItems())
                .map(ItemStack::getItem)
                .collect(Collectors.toSet());

        @Override
        public RecIngredient getTool(RecSerializerManager<ChoppingBoardRecipe> rsm, ChoppingBoardRecipe rec) {
            return RecIngredient.of(Ingredient.of(TagItem.KITCHEN_KNIFE));
        }
    }
}
