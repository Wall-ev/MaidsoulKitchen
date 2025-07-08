package com.github.wallev.maidsoulkitchen.task.cook.cuisine.cuisine;

import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ingredient.RecIngredient;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemDefinition;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.itemdown.RecDataUse;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.IndexRange;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.ItemAmount;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidItem;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec.MKRecipe;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import dev.xkmc.cuisinedelight.content.recipe.BaseCuisineRecipe;
import dev.xkmc.cuisinedelight.content.recipe.CuisineRecipeMatch;
import dev.xkmc.cuisinedelight.init.registrate.CDItems;
import dev.xkmc.cuisinedelight.init.registrate.CDMisc;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.github.wallev.maidsoulkitchen.task.cook.cuisine.cuisine.CuisineRecSerializerManager.CuisineRecipeInfoProvider.getContainerDef;
import static com.github.wallev.maidsoulkitchen.task.cook.cuisine.cuisine.CuisineRecSerializerManager.CuisineRecipeInfoProvider.getKitchenShovelDef;


@TaskClassAnalyzer(TaskInfo.CD_CUISINE_SKILLET)
public class CuisineRecSerializerManager extends RecSerializerManager<BaseCuisineRecipe<?>> {
    private static final CuisineRecSerializerManager INSTANCE = new CuisineRecSerializerManager();

    protected CuisineRecSerializerManager() {
        super(CDMisc.RT_CUISINE.get());
    }

    public static CuisineRecSerializerManager getInstance() {
        return INSTANCE;
    }

    @Override
    public String getRecipeTypeId() {
        return CDMisc.RT_CUISINE.getId().toString();
    }


    @Override
    protected void initRecs(Level level) {
        this.recipes = this.createDefaultRecs(level).stream()
                .filter(r -> {
                    return !r.idStr().equals("cuisinedelight:suspicious_mix");
                }).toList();
    }

    @Override
    public LinkedList<MaidRec> createMaidRecs(List<MKRecipe<BaseCuisineRecipe<?>>> recs,
                                              Map<ItemDefinition, Long> available,
                                              BiConsumer<MKRecipe<BaseCuisineRecipe<?>>, IndexRange> successAdd,
                                              Predicate<MKRecipe<BaseCuisineRecipe<?>>> rIsValid,
                                              Predicate<RecDataUse> recDataUsePredicate,
                                              Consumer<Boolean> doneConsumer) {
        if (!available.containsKey(getKitchenShovelDef())) {
            doneConsumer.accept(true);
            return EMPTY_LIST;
        }
        return super.createMaidRecs(recs, available, successAdd, rIsValid, recDataUsePredicate, doneConsumer);
    }

    @Override
    protected boolean processRecIngres(MKRecipe<BaseCuisineRecipe<?>> r, Map<ItemDefinition, Long> available, List<ItemDefinition> invIngredient, boolean[] single, Map<ItemDefinition, ItemAmount> itemTimes) {
        return super.processRecIngres(r, available, invIngredient, single, itemTimes);
    }

    @Override
    protected List<MaidRec> createCookRec(MKRecipe<BaseCuisineRecipe<?>> r, Map<ItemDefinition, Long> available, boolean[] single, List<ItemDefinition> invIngredient, Map<ItemDefinition, ItemAmount> itemTimes) {
        ItemStack result = r.output();
        List<MaidItem> maidItems = new ArrayList<>();

        int canCookAmount = getMaxAmount(available, single, itemTimes);
        int amount = canCookAmount;
        boolean isSingle = single[0] || r.isSingle();
        int endAmount = 1;
        if (isSingle) {
            amount = 1;
            endAmount = canCookAmount;
        }

        for (ItemDefinition definition : invIngredient) {
            ItemAmount itemAmount = itemTimes.get(definition);
            itemAmount.setRecAmount(amount);
            int minAmount = itemAmount.getAmount();

            int count = amount * minAmount;
            maidItems.add(new MaidItem(definition, count));
            available.put(definition, available.get(definition) - (long) count * endAmount);
        }
        maidItems.remove(0);

        ItemAmount itemAmount = new ItemAmount(1);
        itemAmount.setTool(true);
        itemTimes.put(getKitchenShovelDef(), itemAmount);
        MaidRec maidRec = new MaidRec(r.rec(), 0, result, amount, getKitchenShovelDef().stack(), getContainerDef().stack(),
                maidItems, MaidItem.EMPTY);
        return this.generateRecs(maidRec, endAmount);
    }

    @Override
    protected RecipeInfoProvider<BaseCuisineRecipe<?>> createRecipeInfoProvider() {
        return new CuisineRecipeInfoProvider();
    }

    public static class CuisineRecipeInfoProvider extends RecipeInfoProvider<BaseCuisineRecipe<?>> {
        private static ItemDefinition CONTAINER_DEF;
        private static Ingredient CONTAINER_INGREDIENT;
        private static ItemDefinition KITCHEN_SHOVEL_DEF;
        private static Ingredient KITCHEN_SHOVEL_INGREDIENT;

        public static ItemDefinition getContainerDef() {
            if (CONTAINER_DEF == null) {
                CONTAINER_DEF = ItemDefinition.of(CDItems.PLATE.get().getDefaultInstance());
            }
            return CONTAINER_DEF;
        }

        public static Ingredient getContainerIngredient() {
            if (CONTAINER_INGREDIENT == null) {
                CONTAINER_INGREDIENT = Ingredient.of(CDItems.PLATE.get().getDefaultInstance());
            }
            return CONTAINER_INGREDIENT;
        }

        public static ItemDefinition getKitchenShovelDef() {
            if (KITCHEN_SHOVEL_DEF == null) {
                KITCHEN_SHOVEL_DEF = ItemDefinition.of(CDItems.SPATULA.get().getDefaultInstance());
            }
            return KITCHEN_SHOVEL_DEF;
        }

        public static Ingredient getKitchenShovelIngredient() {
            if (KITCHEN_SHOVEL_INGREDIENT == null) {
                KITCHEN_SHOVEL_INGREDIENT = Ingredient.of(CDItems.SPATULA.get());
            }
            return KITCHEN_SHOVEL_INGREDIENT;
        }

        @Override
        public List<RecIngredient> getIngredients(RecSerializerManager<BaseCuisineRecipe<?>> rsm, BaseCuisineRecipe<?> rec) {
            List<RecIngredient> list = new ArrayList<>();
            list.add(RecIngredient.of(getContainerIngredient()));
            list.addAll(rec.list.stream()
                    .map(CuisineRecipeMatch::ingredient)
                    .map(RecIngredient::of)
                    .toList());
            return list;
        }

        @Override
        public boolean isSingle(RecSerializerManager<BaseCuisineRecipe<?>> rsm, BaseCuisineRecipe<?> rec) {
            return true;
        }
    }
}
