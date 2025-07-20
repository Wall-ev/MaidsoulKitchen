package com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.client.tooltip.RecipeDataTooltip;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.cook.v0.CookData;
import com.github.wallev.maidsoulkitchen.init.MkItems;
import com.github.wallev.maidsoulkitchen.item.ItemCulinaryHub;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ingredient.RecIngredient;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemDefinition;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.itemdown.RecDataUse;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.IndexRange;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec.MKRecipe;
import com.github.wallev.maidsoulkitchen.util.ItemStackUtil;
import com.github.wallev.maidsoulkitchen.util.MathUtil;
import com.google.common.collect.Lists;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class RecSerializerManager<R extends Recipe<? extends Container>> {
    public static final LinkedList<MaidRec> EMPTY_LIST = new LinkedList<>();

    protected final RecipeType<R> recipeType;
    protected final RecipeInfoProvider<R> recipeInfoProvider;
    // @Final
    protected List<MKRecipe<R>> recipes;

    protected List<ItemStack> fuels;

    protected RecSerializerManager(RecipeType<R> recipeType) {
        this.recipeType = recipeType;
        this.recipeInfoProvider = this.createRecipeInfoProvider();
    }

    protected RecipeInfoProvider<R> createRecipeInfoProvider() {
        return RecipeInfoProvider.getInstance();
    }

    public LinkedList<MaidRec> createMaidRecs(List<MKRecipe<R>> recs,
                                              Map<ItemDefinition, Long> available,
                                              BiConsumer<MKRecipe<R>, IndexRange> successAdd,
                                              Predicate<MKRecipe<R>> rIsValid,
                                              Predicate<RecDataUse> recDataUsePredicate,
                                              Consumer<Boolean> doneConsumer) {
        LinkedList<MaidRec> maidRecs = new LinkedList<>();
        IndexRange indexRange = new IndexRange();
        RecDataUse recDataUse = new RecDataUse();

        int index = 0;
        for (MKRecipe<R> r : recs) {
            if (!rIsValid.test(r)) {
                continue;
            }

            List<MaidRec> maidRec = this.createMaidRec(r, available, recDataUse);

            int size = maidRec.size();
            if (size == 0) {
                continue;
            }

            boolean test = recDataUsePredicate.test(recDataUse);
            if (test) {
                maidRecs.addAll(maidRec);
                indexRange.set(index, size);
                successAdd.accept(r, indexRange);
                index += size;
            } else {
                doneConsumer.accept(true);
                break;
            }
        }

        return maidRecs;
    }

    @SuppressWarnings("all")
    protected List<MaidRec> createMaidRec(MKRecipe<R> r, Map<ItemDefinition, Long> available, RecDataUse recDataUse) {
        List<ItemDefinition> invIngredient = new ArrayList<>();
        Map<ItemDefinition, ItemAmount> itemTimes = new HashMap<>();
        boolean[] single = {false};

        List<MaidRec> maidRecs = recProcess(r, available, invIngredient, single, itemTimes);
        if (!maidRecs.isEmpty()) {
            recDataUse.set(itemTimes, maidRecs.size());
        }
        return maidRecs;
    }

    protected List<MaidRec> recProcess(MKRecipe<R> r, Map<ItemDefinition, Long> available, List<ItemDefinition> invIngredient, boolean[] single, Map<ItemDefinition, ItemAmount> itemTimes) {
        boolean processRecIngres = processRecIngres(r, available, invIngredient, single, itemTimes);
        if (!processRecIngres) {
            return Collections.emptyList();
        }

        return createCookRec(r, available, single, invIngredient, itemTimes);
    }

    protected boolean processRecIngres(MKRecipe<R> r, Map<ItemDefinition, Long> available, List<ItemDefinition> invIngredient, boolean[] single, Map<ItemDefinition, ItemAmount> itemTimes) {
        for (RecIngredient ingredient : r.inItems()) {
            boolean hasIngredient = false;
            for (Map.Entry<ItemDefinition, Long> entry : available.entrySet()) {
                ItemDefinition key = entry.getKey();
                Long value = entry.getValue();
                Item item = key.item();

                ItemStack stack = key.toStack(value);
                int test = ingredient.test(stack);
                if (test > 0) {
                    invIngredient.add(key);
                    hasIngredient = true;

                    int amount;
                    if (stack.getMaxStackSize() == 1) {
                        single[0] = true;
                        ItemAmount itemAmount = new ItemAmount(test);
                        itemTimes.put(key, itemAmount);
                        amount = itemAmount.needCount();
                    } else {
                        ItemAmount itemAmount = itemTimes.computeIfAbsent(key, k -> new ItemAmount(test, 0));
                        itemAmount.addCount();
                        amount = itemAmount.needCount();
                    }

                    if (value < amount) {
                        return false;
                    } else {
                        break;
                    }

                }
            }

            if (!hasIngredient) {
                return false;
            }
        }
        return true;
    }

    protected List<MaidRec> createCookRec(MKRecipe<R> r, Map<ItemDefinition, Long> available, boolean[] single, List<ItemDefinition> invIngredient, Map<ItemDefinition, ItemAmount> itemTimes) {
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
            int minAmount = itemAmount.getAmount();
            itemAmount.setRecAmount(amount);

            int count = amount * minAmount;
            maidItems.add(new MaidItem(definition, count));
            available.put(definition, available.get(definition) - (long) count * endAmount);
        }

        MaidRec maidRec = new MaidRec(r.rec(), result, amount, maidItems);
        return this.generateRecs(maidRec, endAmount);
    }

    protected List<MaidRec> createCookRec(MKRecipe<R> r, ItemStack tool, Map<ItemDefinition, Long> available, boolean[] single, List<ItemDefinition> invIngredient, Map<ItemDefinition, ItemAmount> itemTimes) {
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
        MaidRec maidRec = new MaidRec(r.rec(), result, amount, tool, maidItems);

        return this.generateRecs(maidRec, endAmount);
    }

    protected List<MaidRec> generateRecs(MaidRec maidRec, int count) {
        List<MaidRec> maidRecList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            maidRecList.add(maidRec);
        }
        return maidRecList;
    }

    protected int getMaxAmount(Map<ItemDefinition, Long> available, boolean[] single, Map<ItemDefinition, ItemAmount> itemTimes) {
        int maxCount = 64;
        for (ItemDefinition itemDefinition : itemTimes.keySet()) {
            if (itemDefinition.getMaxStackSize() == 1) {
                // 最大份量为 64 份；
                // getStack(item).getMaxStackSize()：该种物品的最大堆叠数量，比如 鸡蛋为16个，那么他的最大份量就只能是16份；
                // available.get(item)：该种物品在背包中的数量；
                maxCount = MathUtil.min(maxCount, (int) (available.get(itemDefinition) / itemTimes.get(itemDefinition).needCount()));
            } else {
                // 最大份量为 64 份；
                // getStack(item).getMaxStackSize()：该种物品的最大堆叠数量，比如 鸡蛋为16个，那么他的最大份量就只能是16份；
                // available.get(item)：该种物品在背包中的数量；
                maxCount = MathUtil.min(maxCount, itemDefinition.getMaxStackSize(), (int) (available.get(itemDefinition) / itemTimes.get(itemDefinition).needCount()));
            }
        }
        return maxCount;
    }

    protected final ItemStack getStack(Item item) {
        return ItemStackUtil.getItemStack(item);
    }

    public final List<MKRecipe<R>> getRecipes(Level level) {
//        if (this.recipes == null) {
            this.initRecs(level);
//        }

        return this.recipes;
    }

    protected void initRecs(Level level) {
        this.recipes = createDefaultRecs(level);
    }

    protected void initFuels() {
        this.fuels = Collections.emptyList();
    }

    protected final List<R> getRecsFromRm(Level level) {
        return this.getRecsFromRm(level, this.recipeType);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected final List<R> getRecsFromRm(Level level, RecipeType recipeType) {
        return level.getRecipeManager().getAllRecipesFor(recipeType);
    }

    protected final List<MKRecipe<R>> createDefaultRecs(Level level) {
        return createTypeRecs(level, recipeType);
    }

    protected final List<MKRecipe<R>> createTypeRecs(Level level, RecipeType<?> recipeType) {
        return this.getRecsFromRm(level, recipeType).stream()
                .map(this::createMKRecipe)
                .toList();
    }

    protected MKRecipe<R> createMKRecipe(R r) {
        List<RecIngredient> ingredients = recipeInfoProvider.getIngredients(this, r);
        ItemStack output = recipeInfoProvider.getOutput(this, r);
        ItemStack container = recipeInfoProvider.getContainer(this, r);
        boolean single = recipeInfoProvider.isSingle(this, r);
        return new MKRecipe<>(r, single, ingredients, output, container);
    }

    protected MKRecipe<R> createMKRecipe(R r, List<ItemStack> inFluids) {
        List<RecIngredient> ingredients = recipeInfoProvider.getIngredients(this, r);
        ItemStack output = recipeInfoProvider.getOutput(this, r);
        ItemStack container = recipeInfoProvider.getContainer(this, r);
        boolean single = recipeInfoProvider.isSingle(this, r);
        return new MKRecipe<>(r, single, inFluids, ingredients, output, container);
    }

    public RecipeType<R> getRecipeType() {
        return recipeType;
    }

    public String getRecipeTypeId() {
        return this.recipeType.toString();
    }

    public boolean isItem(List<ItemStack> itemStacks, Item item) {
        return ItemStackUtil.isItem(itemStacks, item);
    }

    public boolean isItem(List<ItemStack> itemStacks, ItemStack itemStack) {
        return isItem(itemStacks, itemStack.getItem());
    }

    public FluidRecSerializerManager<R> toFluid() {
        return to();
    }

    @SuppressWarnings("unchecked")
    public <RSM extends RecSerializerManager<R>> RSM to() {
        return (RSM) this;
    }

    @OnlyIn(Dist.CLIENT)
    public Optional<TooltipComponent> getRecClientAmountTooltip(MKRecipe<?> recipe, boolean modeIsBlacklist, boolean overSize, CookData cookData, EntityMaid maid) {
        List<RecipeDataTooltip.TooltipRecIngredient> tooltips = new ArrayList<>();
        List<ItemStack> inFluids = recipe.inFluids();
        List<RecIngredient> ingredients = recipe.inItems();
        if (!ingredients.isEmpty() || !inFluids.isEmpty()) {
            List<Ingredient> ingredientAll = new ArrayList<>();
            if (!inFluids.isEmpty()) {
                ingredientAll.add(Ingredient.of(inFluids.stream()));
            }

            if (!ingredients.isEmpty()) {
                List<Ingredient> ingredients1 = ingredients.stream()
                        .map(r -> r.ingredient)
                        .toList();
                ingredientAll.addAll(ingredients1);
            }

            tooltips.add(this.getTooltipReIngreIngredient(ingredientAll, maid));
        }
        ItemStack container = recipe.container();
        if (!container.isEmpty()) {
            tooltips.add(this.getTooltipRecOutputContainerIngredient(List.of(Ingredient.of(container)), maid));
        }
        List<ItemStack> fuels = this.getFuels();
//        if (!fuels.isEmpty()) {
//            tooltips.add(this.getTooltipRecFuelIngredient(List.of(Ingredient.of(fuels.stream())), maid));
//        }

        RecipeDataTooltip.TooltipRecipeData tooltipRecipeData = new RecipeDataTooltip.TooltipRecipeData(cookData, recipe.id().toString(), tooltips, this.getTooltipRecResultIngredient(recipe, maid), modeIsBlacklist, overSize);
        return Optional.of(tooltipRecipeData);
    }

    @OnlyIn(Dist.CLIENT)
    protected RecipeDataTooltip.TooltipRecIngredient getTooltipReIngreIngredient(List<Ingredient> ingredientList, EntityMaid maid) {
        List<List<RecipeDataTooltip.IngredientSourceType>> list = new ArrayList<>();
        list.add(List.of(RecipeDataTooltip.IngredientSourceType.MAIN_HAND, RecipeDataTooltip.IngredientSourceType.OFF_HAND, RecipeDataTooltip.IngredientSourceType.MAID_BACKPACK));
        list.add(List.of(RecipeDataTooltip.IngredientSourceType.HUB_INGREDIENT));
        int resultRuleMatchIndex = ItemCulinaryHub.hasItem(maid) ? 1 : 0;
        RecipeDataTooltip.TooltipRecIngredient tooltipRecResultIngredient = new RecipeDataTooltip.TooltipRecIngredient(ingredientList, list, RecipeDataTooltip.IngredientType.MANDATORY, resultRuleMatchIndex);
        return tooltipRecResultIngredient;
    }

    @OnlyIn(Dist.CLIENT)
    protected RecipeDataTooltip.TooltipRecIngredient getTooltipRecResultIngredient(MKRecipe<?> recipe, EntityMaid maid) {
        ItemStack resultClient = recipe.output();
        List<List<RecipeDataTooltip.IngredientSourceType>> list = new ArrayList<>();
        list.add(List.of(RecipeDataTooltip.IngredientSourceType.MAIN_HAND, RecipeDataTooltip.IngredientSourceType.OFF_HAND, RecipeDataTooltip.IngredientSourceType.MAID_BACKPACK));
        list.add(List.of(RecipeDataTooltip.IngredientSourceType.HUB_OUTPUT));
        int resultRuleMatchIndex = ItemCulinaryHub.hasItem(maid) ? 1 : 0;
        RecipeDataTooltip.TooltipRecIngredient tooltipRecResultIngredient = new RecipeDataTooltip.TooltipRecIngredient(List.of(Ingredient.of(resultClient)), list, RecipeDataTooltip.IngredientType.OUTPUT, resultRuleMatchIndex);
        return tooltipRecResultIngredient;
    }

    @OnlyIn(Dist.CLIENT)
    protected RecipeDataTooltip.TooltipRecIngredient getTooltipRecOutputContainerIngredient(List<Ingredient> outputContainers, EntityMaid maid) {
        List<List<RecipeDataTooltip.IngredientSourceType>> list = new ArrayList<>();
        list.add(Lists.newArrayList(RecipeDataTooltip.IngredientSourceType.MAIN_HAND, RecipeDataTooltip.IngredientSourceType.OFF_HAND, RecipeDataTooltip.IngredientSourceType.MAID_BACKPACK));
        list.add(Lists.newArrayList(RecipeDataTooltip.IngredientSourceType.HUB_OUTPUT_ADDITION));
        int containerRuleMatchIndex = ItemCulinaryHub.hasItem(maid) ? 1 : 0;
        RecipeDataTooltip.TooltipRecIngredient tooltipRecContainerSources = new RecipeDataTooltip.TooltipRecIngredient(outputContainers, list, RecipeDataTooltip.IngredientType.MAYBE, containerRuleMatchIndex);
        return tooltipRecContainerSources;
    }

    public List<ItemStack> getFuels() {
        if (this.fuels == null) {
            this.initFuels();
        }

        return this.fuels;
    }

    @OnlyIn(Dist.CLIENT)
    protected RecipeDataTooltip.TooltipRecIngredient getTooltipRecFuelIngredient(List<Ingredient> fuels, EntityMaid maid) {
        List<List<RecipeDataTooltip.IngredientSourceType>> list = new ArrayList<>();
        list.add(Lists.newArrayList(RecipeDataTooltip.IngredientSourceType.MAIN_HAND, RecipeDataTooltip.IngredientSourceType.OFF_HAND, RecipeDataTooltip.IngredientSourceType.MAID_BACKPACK));
        int containerRuleMatchIndex = 0;
        RecipeDataTooltip.TooltipRecIngredient tooltipRecContainerSources = new RecipeDataTooltip.TooltipRecIngredient(fuels, list, RecipeDataTooltip.IngredientType.MAYBE, containerRuleMatchIndex);
        return tooltipRecContainerSources;
    }

    public final List<ItemStack> createDefaultFuels() {
        return ItemStackUtil.getDefaultFuels();
    }

    /**
     * 配方信息基础模板
     */
    public static class RecipeInfoProvider<R extends Recipe<? extends Container>> {
        @SuppressWarnings("rawtypes")
        private static final RecipeInfoProvider INSTANCE = new RecipeInfoProvider<>();

        @SuppressWarnings("unchecked")
        public static <R extends Recipe<? extends Container>> RecipeInfoProvider<R> getInstance() {
            return (RecipeInfoProvider<R>) INSTANCE;
        }

        public List<RecIngredient> getIngredients(RecSerializerManager<R> rsm, R rec) {
            return RecIngredient.from(rec.getIngredients());
        }

        public ItemStack getOutput(RecSerializerManager<R> rsm, R rec) {
            return rec.getResultItem(RegistryAccess.EMPTY);
        }

        public ItemStack getContainer(RecSerializerManager<R> rsm, R rec) {
            return ItemStack.EMPTY;
        }

        public boolean isSingle(RecSerializerManager<R> rsm, R rec) {
            return false;
        }

        @SuppressWarnings("unchecked")
        public final <RP0 extends RecipeInfoProvider<R0>, R0 extends Recipe<? extends Container>> RP0 to() {
            return (RP0) this;
        }
    }
}
