//package com.github.wallev.maidsoulkitchen.task.cook.kaleidoscopecookery.cookery;
//
//import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
//import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ingredient.RecIngredient;
//import com.github.wallev.maidsoulkitchen.task.cook.common.manager.IndexRange;
//import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemDefinition;
//import com.github.wallev.maidsoulkitchen.task.cook.common.inv.itemdown.RecDataUse;
//import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.ItemAmount;
//import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidItem;
//import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
//import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
//import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec.MKRecipe;
//import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
//import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
//import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
//import com.github.ysbbbbbb.kaleidoscopecookery.recipe.PotRecipe;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.item.crafting.Ingredient;
//
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.function.BiConsumer;
//import java.util.function.Consumer;
//import java.util.function.Predicate;
//
//import static com.github.wallev.maidsoulkitchen.task.cook.kaleidoscopecookery.cookery.PotRecSerializerManager.PotRecipeInfoProvider.*;
//
//@TaskClassAnalyzer(TaskInfo.KC_POT)
//public class PotRecSerializerManager extends RecSerializerManager<PotRecipe> {
//    private static final PotRecSerializerManager INSTANCE = new PotRecSerializerManager();
//
//    protected PotRecSerializerManager() {
//        super(ModRecipes.POT_RECIPE);
//    }
//
//    public static PotRecSerializerManager getInstance() {
//        return INSTANCE;
//    }
//
//    @Override
//    public LinkedList<MaidRec> createMaidRecs(List<MKRecipe<PotRecipe>> recs,
//                                              Map<ItemDefinition, Long> available,
//                                              BiConsumer<MKRecipe<PotRecipe>, IndexRange> successAdd,
//                                              Predicate<MKRecipe<PotRecipe>> rIsValid,
//                                              Predicate<RecDataUse> recDataUsePredicate,
//                                              Consumer<Boolean> doneConsumer) {
//        if (!available.containsKey(getKitchenShovelDef())) {
//            doneConsumer.accept(true);
//            return EMPTY_LIST;
//        }
//        return super.createMaidRecs(recs, available, successAdd, rIsValid, recDataUsePredicate, doneConsumer);
//    }
//
//    @Override
//    protected boolean processRecIngres(MKRecipe<PotRecipe> r, Map<ItemDefinition, Long> available, List<ItemDefinition> invIngredient, boolean[] single, Map<ItemDefinition, ItemAmount> itemTimes) {
//        return super.processRecIngres(r, available, invIngredient, single, itemTimes);
//    }
//
//    @Override
//    protected List<MaidRec> createCookRec(MKRecipe<PotRecipe> r, Map<ItemDefinition, Long> available, boolean[] single, List<ItemDefinition> invIngredient, Map<ItemDefinition, ItemAmount> itemTimes) {
//        ItemStack result = r.output();
//        List<MaidItem> maidItems = new ArrayList<>();
//
//        int canCookAmount = getMaxAmount(available, single, itemTimes);
//        int amount = canCookAmount;
//        boolean isSingle = single[0] || r.isSingle();
//        int endAmount = 1;
//        if (isSingle) {
//            amount = 1;
//            endAmount = canCookAmount;
//        }
//
//        for (ItemDefinition definition : invIngredient) {
//            ItemAmount itemAmount = itemTimes.get(definition);
//            itemAmount.setRecAmount(amount);
//            int minAmount = itemAmount.getAmount();
//
//            int count = amount * minAmount;
//            maidItems.add(new MaidItem(definition, count));
//            available.put(definition, available.get(definition) - (long) count * endAmount);
//        }
//        maidItems.remove(0);
//        if (r.rec().isNeedBowl()) {
//            maidItems.remove(maidItems.size() - 1);
//        }
//
//        ItemAmount itemAmount = new ItemAmount(1);
//        itemAmount.setTool(true);
//        itemTimes.put(getKitchenShovelDef(), itemAmount);
//        MaidRec maidRec = new MaidRec(r.rec(), r.rec().getTime(), result, amount, getOil().getDefaultInstance(), getKitchenShovel().getDefaultInstance(), getContainer().getDefaultInstance(),
//                maidItems, MaidItem.EMPTY);
//        return this.generateRecs(maidRec, endAmount);
//    }
//
//    @Override
//    protected PotRecipeInfoProvider createRecipeInfoProvider() {
//        return new PotRecipeInfoProvider();
//    }
//
//    public static class PotRecipeInfoProvider extends RecipeInfoProvider<PotRecipe> {
//        private static Item OIL;
//        private static Ingredient OIL_IND;
//        private static Item CONTAINER;
//        private static Item KITCHEN_SHOVEL;
//        private static Item FLINT;
//        private static Ingredient CONTAINER_INGREDIENT;
//        private static ItemDefinition KITCHEN_SHOVEL_DEF;
//
//        public static Item getOil() {
//            if (OIL == null) {
//                OIL = ModItems.OIL.get();
//            }
//            return OIL;
//        }
//
//        public static Ingredient getOilIngredient() {
//            if (OIL_IND == null) {
//                OIL_IND = Ingredient.of(getOil());
//            }
//            return OIL_IND;
//        }
//
//        public static Item getContainer() {
//            if (CONTAINER == null) {
//                CONTAINER = Items.BOWL;
//            }
//            return CONTAINER;
//        }
//
//        public static Item getKitchenShovel() {
//            if (KITCHEN_SHOVEL == null) {
//                KITCHEN_SHOVEL = ModItems.KITCHEN_SHOVEL.get();
//            }
//            return KITCHEN_SHOVEL;
//        }
//
//        public static Item getFlint() {
//            if (FLINT == null) {
//                FLINT = Items.FLINT;
//            }
//            return FLINT;
//        }
//
//        public static Ingredient getContainerIngredient() {
//            if (CONTAINER_INGREDIENT == null) {
//                CONTAINER_INGREDIENT = Ingredient.of(getContainer().getDefaultInstance());
//            }
//            return CONTAINER_INGREDIENT;
//        }
//
//        public static ItemDefinition getKitchenShovelDef() {
//            if (KITCHEN_SHOVEL_DEF == null) {
//                KITCHEN_SHOVEL_DEF = ItemDefinition.of(getKitchenShovel().getDefaultInstance());
//            }
//            return KITCHEN_SHOVEL_DEF;
//        }
//
//        @Override
//        public List<RecIngredient> getIngredients(RecSerializerManager<PotRecipe> rsm, PotRecipe rec) {
//            List<Ingredient> list = new ArrayList<>();
//            list.add(getOilIngredient());
//
//            for (Ingredient ingredient : rec.getIngredients()) {
//                if (!ingredient.isEmpty()) {
//                    list.add(ingredient);
//                }
//            }
//
//            if (rec.isNeedBowl()) {
//                list.add(getContainerIngredient());
//            }
//
//            return RecIngredient.from(list);
//        }
//
//        @Override
//        public boolean isSingle(RecSerializerManager<PotRecipe> rsm, PotRecipe rec) {
//            return true;
//        }
//    }
//}
