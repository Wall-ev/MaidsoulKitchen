package com.catbert.tlma.task.cook.handler.v2;

import com.catbert.tlma.task.cook.handler.MaidInventory;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.catbert.tlma.TLMAddon.LOGGER;

public class MaidRecipesManager<T extends Recipe<? extends Container>> {
    private final MaidInventory maidInventory;
    private final RecipeType<T> recipeType;
    private final boolean single;
    private List<Pair<List<Integer>, List<List<ItemStack>>>> recipesIngredients = new ArrayList<>();

    public MaidRecipesManager(EntityMaid maid, RecipeType<T> recipeType, boolean single) {
        this(maid, recipeType, single, false);
    }

    public MaidRecipesManager(EntityMaid maid, RecipeType<T> recipeType, boolean single, boolean createRecIng) {
        this.maidInventory = new MaidInventory(maid);
        this.recipeType = recipeType;
        this.single = single;

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

    @SuppressWarnings({"unchecked", "rawtypes"})
    private List<T> getAllRecipesFor() {
        List<T> allRecipesFor = this.maidInventory.getMaid().level().getRecipeManager().getAllRecipesFor((RecipeType) recipeType);
        allRecipesFor = new ArrayList<>(allRecipesFor);
        allRecipesFor = filterRecipes(allRecipesFor);
        shuffle(allRecipesFor);
        return allRecipesFor;
    }

    public List<Pair<List<Integer>, List<List<ItemStack>>>> getRecipesIngredients() {
        return recipesIngredients;
    }

    public Pair<List<Integer>, List<List<ItemStack>>> getRecipeIngredient() {
        LOGGER.info("MaidRecipesManager.getRecipeIngredient: ");
        LOGGER.info(recipesIngredients);
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

    @Nullable
    public ItemStack getItemStack(Item item) {
        Map<Item, List<ItemStack>> inventoryStack = maidInventory.getInventoryStack();
        List<ItemStack> itemStacks = inventoryStack.get(item);
        for (ItemStack itemStack : itemStacks) {
            if (!itemStack.isEmpty()) {
                return itemStack;
            }
        }
        return null;
    }

    protected List<T> filterRecipes(List<T> recipes) {
        return recipes;
    }

    private void createRecipesIngredients() {
        this.maidInventory.refreshInv();
//        recipesIngredients.clear();
        recipesIngredients = new ArrayList<>();

        List<Pair<List<Integer>, List<Item>>> _make = new ArrayList<>();
        Map<Item, Integer> available = new HashMap<>(this.maidInventory.getInventoryItem());

        for (T t : this.getAllRecipesFor()) {
            Pair<List<Integer>, List<Item>> maxCount = this.getAmountIngredient(t, available);
            if (!maxCount.getFirst().isEmpty()) {
                _make.add(Pair.of(maxCount.getFirst(), maxCount.getSecond()));
            }
        }

        this.recipesIngredients = transform(_make);

        LOGGER.info("MaidRecipesManager.createRecipesIngredients: " + this.maidInventory.getMaid());
        LOGGER.info(this.recipesIngredients);
    }

    protected List<Pair<List<Integer>, List<List<ItemStack>>>> transform(List<Pair<List<Integer>, List<Item>>> oriList) {
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
}
