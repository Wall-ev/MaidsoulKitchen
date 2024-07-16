package com.github.catbert.tlma.task.cook.handler.v2;

import com.github.catbert.tlma.api.IAddonMaid;
import com.github.catbert.tlma.api.task.v1.cook.ICookTask;
import com.github.catbert.tlma.entity.passive.CookTaskData;
import com.github.catbert.tlma.task.cook.handler.MaidInventory;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.*;

public class MaidRecipesManager<T extends Recipe<? extends Container>> {
    private final MaidInventory maidInventory;
    private final ICookTask<?, T> task;
    private final boolean single;
    private int repeatTimes = 0;
    private List<T> recipes;
    private List<Pair<List<Integer>, List<List<ItemStack>>>> recipesIngredients = new ArrayList<>();

    public MaidRecipesManager(EntityMaid maid, ICookTask<?, T> task, boolean single) {
        this(maid, task, single, false);
    }

    public MaidRecipesManager(EntityMaid maid, ICookTask<?, T> task, boolean single, boolean createRecIng) {
        this.maidInventory = new MaidInventory(maid);
        this.single = single;
        this.task = task;

        CookTaskData cookTaskData1 = ((IAddonMaid) maid).getCookTaskData1();
        List<String> recipeIds = cookTaskData1.getTaskRule(this.task.getUid().toString()).getRecipeIds();
        this.recipes = this.getAllRecipesFor().stream()
                .filter(r -> recipeIds.contains(r.getId().toString()))
                .toList();

        if (createRecIng) {
            this.createRecipesIngredients();
        }
    }

    public boolean isSingle() {
        return single;
    }

    public MaidInventory getMaidInventory() {
        return maidInventory;
    }

    private List<T> getAllRecipesFor() {
//        List<T> allRecipesFor = this.maidInventory.getMaid().level().getRecipeManager().getAllRecipesFor((RecipeType) recipeType);
        Level level = this.maidInventory.getMaid().level();
        List<T> allRecipesFor = task.getRecipes(level);
        allRecipesFor = new ArrayList<>(allRecipesFor);
//        allRecipesFor = filterRecipes(allRecipesFor);
        shuffle(allRecipesFor);
        return allRecipesFor;
    }

    public List<Pair<List<Integer>, List<List<ItemStack>>>> getRecipesIngredients() {
        return recipesIngredients;
    }

    public Pair<List<Integer>, List<List<ItemStack>>> getRecipeIngredient() {
//        LOGGER.info("MaidRecipesManager.getRecipeIngredient: ");
//        LOGGER.info(recipesIngredients);
        if (recipesIngredients.isEmpty()) return null;
        int size = recipesIngredients.size();
        Pair<List<Integer>, List<List<ItemStack>>> integerListPair = recipesIngredients.get(0);
        List<Pair<List<Integer>, List<List<ItemStack>>>> pairs = recipesIngredients.subList(1, size);
        recipesIngredients = pairs;
        return integerListPair;
    }

    public void checkAndCreateRecipesIngredients(EntityMaid maid) {
        // 缓存的配方原料没了
        if (!recipesIngredients.isEmpty()) return;
        // 是否为上一次的背包以及手上的物品
        if (isLastInv(maid)) return;
        createRecipesIngredients();
    }

    private boolean isLastInv(EntityMaid maid) {

        CombinedInvWrapper availableInv = maid.getAvailableInv(true);
        List<ItemStack> lastInvStack = maidInventory.getLastInvStack();
        if (availableInv.getSlots() != lastInvStack.size()) return false;

        for (int i = 0; i < availableInv.getSlots(); i++) {
            ItemStack stackInSlot = availableInv.getStackInSlot(i);
            ItemStack cacheStack = lastInvStack.get(i);
            if (!(stackInSlot.is(cacheStack.getItem()) && stackInSlot.getCount() == cacheStack.getCount())) {
                return false;
            }
        }

        return true;
    }

    public ItemStack getItemStack(Item item) {
        Map<Item, List<ItemStack>> inventoryStack = maidInventory.getInventoryStack();
        List<ItemStack> itemStacks = inventoryStack.get(item);
        for (ItemStack itemStack : itemStacks) {
            if (!itemStack.isEmpty()) {
                return itemStack;
            }
        }
        return ItemStack.EMPTY;
    }

    protected List<T> filterRecipes(List<T> recipes) {
        return recipes;
    }

    private void createRecipesIngredients() {
        this.maidInventory.refreshInv();
//        recipesIngredients.clear();
//        recipesIngredients = new ArrayList<>();

        List<Pair<List<Integer>, List<Item>>> _make = new ArrayList<>();
        Map<Item, Integer> available = new HashMap<>(this.maidInventory.getInventoryItem());

        for (T t : this.recipes) {
            Pair<List<Integer>, List<Item>> maxCount = this.getAmountIngredient(t, available);
            if (!maxCount.getFirst().isEmpty()) {
                _make.add(Pair.of(maxCount.getFirst(), maxCount.getSecond()));
            }
        }

        repeat(_make, available, this.repeatTimes);

        this.recipesIngredients = transform(_make, available);

//        LOGGER.info("MaidRecipesMrecipesIngredients = {ImmutableCollections$ListN@44015}  size = 2anager.createRecipesIngredients: " + this.maidInventory.getMaid());
//        LOGGER.info(this.recipesIngredients);
    }

    protected void repeat(List<Pair<List<Integer>, List<Item>>> oriList, Map<Item, Integer> available, int times) {
        ArrayList<Pair<List<Integer>, List<Item>>> oriPairs = new ArrayList<>(oriList);
        for (int l = 0; l < times; l++) {
            for (Pair<List<Integer>, List<Item>> listListPair : oriPairs) {
                List<Integer> first = listListPair.getFirst();
                List<Item> second = listListPair.getSecond();

                boolean canRepeat = true;
                for (int i = 0; i < second.size(); i++) {
                    Integer availableCount = available.get(second.get(i));
                    if (availableCount < first.get(i)) {
                        canRepeat = false;
                        break;
                    }
                }

                if (canRepeat) {
                    for (int i = 0; i < second.size(); i++) {
                        Item item = second.get(i);
                        available.put(item, available.get(item) - first.get(i));
                    }
                    oriList.add(listListPair);
                }
            }
        }
    }

//    private List<Pair<List<Integer>, List<Item>>> repeat(List<Pair<List<Integer>, List<Item>>> oriList) {
//        Map<Item, Integer> available = new HashMap<>(this.maidInventory.getInventoryItem());
//        List<Pair<List<Integer>, List<Item>>> list = new ArrayList<>(oriList);
//        for (Pair<List<Integer>, List<Item>> listListPair : oriList) {
//            List<Integer> first = listListPair.getFirst();
//            List<Item> second = listListPair.getSecond();
//
//            boolean canRepeat = true;
//            for (int i = 0; i < second.size(); i++) {
//                Integer availableCount = available.get(second.get(i));
//                if (availableCount < first.get(i)) {
//                    canRepeat = false;
//                    break;
//                }
//            }
//
//            if (canRepeat) {
//                for (int i = 0; i < second.size(); i++) {
//                    Item item = second.get(i);
//                    available.put(item, available.get(item) - first.get(i));
//                }
//                list.add(listListPair);
//            }
//        }
//        return list;
//    }

    protected List<Pair<List<Integer>, List<List<ItemStack>>>> transform(List<Pair<List<Integer>, List<Item>>> oriList, Map<Item, Integer> available ) {
        Map<Item, List<ItemStack>> inventoryStack = this.maidInventory.getInventoryStack();
        List<Pair<List<Integer>, List<List<ItemStack>>>> list1 = oriList.stream().map(p -> {
            List<List<ItemStack>> list = p.getSecond().stream().map(item -> {
                return inventoryStack.get(item);
//                return inventoryStack.getOrDefault(item, new ArrayList<>());
            }).toList();
            return Pair.of(p.getFirst(), list);
        }).toList();
        return list1;
    }

    protected Pair<List<Integer>, List<Item>> getAmountIngredient(T recipe, Map<Item, Integer> available) {
        List<Ingredient> ingredients = recipe.getIngredients();
        boolean[] canMake = {true};
        boolean[] single = {false};
        List<Item> invIngredient = new ArrayList<>();
        Map<Item, Integer> itemTimes = new HashMap<>();

        extraStartRecipe(recipe, available, canMake, single, itemTimes, invIngredient);

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

        extraEndRecipe(recipe, available, canMake, single, itemTimes, invIngredient);

        if (!canMake[0] || invIngredient.stream().anyMatch(item -> available.get(item) <= 0)) {
            return Pair.of(new ArrayList<>(), new ArrayList<>());
        }

        int maxCount = 64;
        if (single[0] || this.single) {
            maxCount = 1;
        } else {
            for (Item item : itemTimes.keySet()) {
                maxCount = Math.min(maxCount, item.getDefaultInstance().getMaxStackSize());
                maxCount = Math.min(maxCount, available.get(item) / itemTimes.get(item));
            }
        }

        List<Integer> countList = new ArrayList<>();
        for (Item item : invIngredient) {
            countList.add(maxCount);
            available.put(item, available.get(item) - maxCount);
        }

        return Pair.of(countList, invIngredient);
    }

    protected boolean extraStartRecipe(T recipe, Map<Item, Integer> available, boolean[] single, boolean[] canMake, Map<Item, Integer> itemTimes, List<Item> invIngredient) {
        return true;
    }

    protected boolean extraEndRecipe(T recipe, Map<Item, Integer> available, boolean[] single, boolean[] canMake, Map<Item, Integer> itemTimes, List<Item> invIngredient) {
        return true;
    }

    private void shuffle(List<T> recipes) {
        Collections.shuffle(recipes);
    }

    public void setRepeatTimes(int repeatTimes) {
        this.repeatTimes = repeatTimes;
    }

    public int getRepeatTimes() {
        return repeatTimes;
    }
}
