package com.github.wallev.maidsoulkitchen.task.cook.v1.crokckpot;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.wallev.maidsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.api.task.v1.cook.IHandlerCookBe;
import com.github.wallev.maidsoulkitchen.api.task.v1.cook.IItemHandlerCook;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.RegisterData;
import com.github.wallev.maidsoulkitchen.inventory.tooltip.CrockPotTooltip;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.block.CrockPotBlocks;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import com.sihenzhang.crockpot.recipe.CrockPotRecipes;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.recipe.cooking.requirement.*;
import com.sihenzhang.crockpot.util.MathUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.apache.commons.lang3.EnumUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TaskCpCrockPot implements ICookTask<CrockPotBlockEntity, CrockPotCookingRecipe>, IHandlerCookBe<CrockPotBlockEntity>, IItemHandlerCook<CrockPotBlockEntity, CrockPotCookingRecipe> {
    private static final Map<CrockPotCookingRecipe, MaidRec<CrockPotCookingRecipe>> RECS = new HashMap<>();
    private static final Map<FoodCategory, List<Item>> FOOD_CATEGORY_INGREDIENT_MAP = new HashMap<>();
    private static final Map<IRequirement, FoodCategory> REQUIREMENT_FOOD_CATEGORY_MAP = new HashMap<>();
    private static final Map<IRequirement, List<Item>> REQUIREMENT_INGREDIENTY_MAP = new HashMap<>();
    private static final Map<FoodCategory, FoodValue> REQUIREMENT_FOOD_VALUE_HASH_MAP = new HashMap<>();
    private static final Set<Item> INVID_ITEMS = new HashSet<>();

    private static Set<Item> getQuireItemSet(IRequirement requirement) {
        return new HashSet<>(getQuireItems(requirement));
    }

    private static List<Item> getQuireItems(IRequirement requirement) {
        return REQUIREMENT_INGREDIENTY_MAP.get(requirement);
    }

    @NotNull
    private static Pair<List<Integer>, List<Item>> getListListPair(Map<Item, Integer> available, boolean[] single, Map<Item, Integer> itemTimes, List<Item> invIngredient) {
        int maxCount = 64;
        if (single[0]) {
            maxCount = 1;
        } else {
            for (Item item : itemTimes.keySet()) {
                maxCount = Math.min(maxCount, item.getDefaultInstance().getMaxStackSize());
                maxCount = Math.min(maxCount, available.get(item) / itemTimes.get(item));
            }
        }

        if (maxCount == 0) {
            return Pair.of(Collections.emptyList(), Collections.emptyList());
        }

        List<Integer> countList = new ArrayList<>();
        for (Item item : invIngredient) {
            countList.add(maxCount);
            available.put(item, available.get(item) - maxCount);
        }

        return Pair.of(countList, new ArrayList<>(invIngredient));
    }

    private boolean crockPotRecMatch(ServerLevel serverLevel, CrockPotBlockEntity blockEntity) {

        for (int i = 0; i < getInputSize(); i++) {
            if (blockEntity.getItemHandler().getStackInSlot(i).isEmpty()) {
                return false;
            }
        }

        CrockPotCookingRecipe.Wrapper recipeWrapper = blockEntity.getRecipeWrapper();
        if (recipeWrapper != null) {
            Optional<CrockPotCookingRecipe> recipeFor = CrockPotCookingRecipe.getRecipeFor(recipeWrapper, serverLevel);
            if (recipeFor.isPresent()) {
                return recipeFor.get().getId().getPath().equals("crock_pot_cooking/wet_goop");
            }
        }
        return false;
    }

    private static void categorizeRequirements(List<IRequirement> requirements, RecInfo1 recInfo1) {
        categorizeRequirements(requirements, recInfo1.getNoRequires(), recInfo1.getMaxRequires(), recInfo1.getMaxERequires(), recInfo1.getAnyRequires(), recInfo1.getMinERequires(), recInfo1.getMinRequires(), recInfo1.getMustRequires(), recInfo1.getMustLessRequires());
    }

    private static void categorizeRequirements(List<IRequirement> requirements, List<RequirementCategoryMax> noRequires, List<RequirementCategoryMax> maxRequires, List<RequirementCategoryMaxExclusive> maxERequires, List<RequirementCategoryMinExclusive> anyRequires, List<RequirementCategoryMinExclusive> minERequires, List<RequirementCategoryMin> minRequires, List<RequirementMustContainIngredient> mustRequires, List<RequirementMustContainIngredientLessThan> mustLessRequires) {
        for (IRequirement requirement : requirements) {
            if (requirement instanceof RequirementCategoryMax requirementCategoryMax) {
                if (MathUtils.fuzzyIsZero(requirementCategoryMax.getMax())) {
                    noRequires.add(requirementCategoryMax);
                } else {
                    maxRequires.add(requirementCategoryMax);
                }
            } else if (requirement instanceof RequirementCategoryMinExclusive requirementCategoryMinExclusive) {
                if (MathUtils.fuzzyIsZero(requirementCategoryMinExclusive.getMin())) {
                    anyRequires.add(requirementCategoryMinExclusive);
                } else {
                    minERequires.add(requirementCategoryMinExclusive);
                }
            } else if (requirement instanceof RequirementCategoryMin requirementCategoryMin) {
                minRequires.add(requirementCategoryMin);
            } else if (requirement instanceof RequirementCategoryMaxExclusive requirementCategoryMaxE) {
                maxERequires.add(requirementCategoryMaxE);
            } else if (requirement instanceof RequirementMustContainIngredient requirementMustContainIngredient) {
                mustRequires.add(requirementMustContainIngredient);
            } else if (requirement instanceof RequirementMustContainIngredientLessThan requirementMustContainIngredientLessThan) {
                mustLessRequires.add(requirementMustContainIngredientLessThan);
            }
        }
    }

    private static void removeNoRequiresItems(List<RequirementCategoryMax> noRequires, Set<Item> itemSet) {
        for (RequirementCategoryMax noRequire : noRequires) {
            Set<Item> quireItems = getQuireItemSet(noRequire);
            itemSet.removeAll(quireItems);
        }
    }

    private static boolean processAnyRequires(int times, List<RequirementCategoryMinExclusive> anyRequires, Set<Item> itemSet, int[] leftUnlockSlot, boolean[] single, List<Item> invIngredient, Map<Item, Integer> itemTimes, Map<Item, Integer> available) {
        if (anyRequires.isEmpty()) return true;

        for (RequirementCategoryMinExclusive anyRequire : anyRequires) {
            for (int i = 0; i < times && leftUnlockSlot[0] != 0; i++) {
                boolean hasIngredient = false;
                Set<Item> quireItems = getQuireItemSet(anyRequire);
                for (Item item : itemSet) {
                    if (quireItems.contains(item)) {
                        leftUnlockSlot[0]--;
                        ItemStack stack = item.getDefaultInstance();
                        invIngredient.add(item);
                        if (stack.getMaxStackSize() == 1) {
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

    private static boolean processMustRequires(int times, List<RequirementMustContainIngredient> mustRequires, Set<Item> itemSet, int[] leftUnlockSlot, boolean[] single, List<Item> invIngredient, Map<Item, Integer> itemTimes, Map<Item, Integer> available) {
        if (mustRequires.isEmpty()) return true;

        for (RequirementMustContainIngredient mustRequire : mustRequires) {
            for (int i = 0; i < times && leftUnlockSlot[0] != 0; i++) {
                boolean hasIngredient = false;
                Set<Item> quireItems = getQuireItemSet(mustRequire);
                int quantity = mustRequire.getQuantity();
                for (Item item : itemSet) {
                    if (!quireItems.contains(item)) continue;
                    for (int integer = 0; integer < available.get(item) - itemTimes.getOrDefault(item, 0); integer++) {
                        leftUnlockSlot[0]--;
                        ItemStack stack = item.getDefaultInstance();
                        invIngredient.add(item);
                        if (stack.getMaxStackSize() == 1) {
                            single[0] = true;
                            itemTimes.put(item, 1);
                        } else {
                            itemTimes.merge(item, 1, Integer::sum);
                        }
                        if (--quantity == 0) {
                            hasIngredient = true;
                            break;
                        }
                    }
                    if (quantity == 0) {
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

    private static boolean processMinRequires(int times, List<RequirementCategoryMin> minRequires, Set<Item> itemSet, int[] leftUnlockSlot, boolean[] single, List<Item> invIngredient, Map<Item, Integer> itemTimes, Map<Item, Integer> available) {
        if (minRequires.isEmpty()) return true;

        float mMinAmount = (float) leftUnlockSlot[0] / minRequires.size();
        for (RequirementCategoryMin minRequire : minRequires) {
            for (int i = 0; i < times && leftUnlockSlot[0] != 0; i++) {
                boolean hasIngredient = false;
                FoodValue foodValue = REQUIREMENT_FOOD_VALUE_HASH_MAP.get(minRequire.getCategory());
                Map<Item, Float> itemValues = foodValue.itemValues();
                float hasValue = 0;
                for (Map.Entry<Item, Integer> itemIntegerEntry : itemTimes.entrySet()) {
                    hasValue += itemValues.getOrDefault(itemIntegerEntry.getKey(), 0f) * itemIntegerEntry.getValue();
                }
                float min = minRequire.getMin() - hasValue;
                float singleMin = min / mMinAmount, value = 0;
                for (Item item : itemSet) {
                    if (!itemValues.containsKey(item)) continue;
                    float itemValue = itemValues.get(item);
                    if (itemValue < singleMin) continue;
                    for (int integer = 0; integer < available.get(item) - itemTimes.getOrDefault(item, 0); integer++) {
                        leftUnlockSlot[0]--;
                        ItemStack stack = item.getDefaultInstance();
                        invIngredient.add(item);
                        if (stack.getMaxStackSize() == 1) {
                            single[0] = true;
                            itemTimes.put(item, 1);
                        } else {
                            itemTimes.merge(item, 1, Integer::sum);
                        }
                        if ((value += itemValue) >= min) {
                            hasIngredient = true;
                            break;
                        }
                    }
                    if (value >= min) {
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

    private static boolean processMinERequires(int times, List<RequirementCategoryMinExclusive> minERequires, Set<Item> itemSet, int[] leftUnlockSlot, boolean[] single, List<Item> invIngredient, Map<Item, Integer> itemTimes, Map<Item, Integer> available) {
        if (minERequires.isEmpty()) return true;

        float mMinEAmount = (float) leftUnlockSlot[0] / minERequires.size();
        for (RequirementCategoryMinExclusive minERequire : minERequires) {
            for (int i = 0; i < times && leftUnlockSlot[0] != 0; i++) {
                boolean hasIngredient = false;
                FoodValue foodValue = REQUIREMENT_FOOD_VALUE_HASH_MAP.get(minERequire.getCategory());
                Map<Item, Float> itemValues = foodValue.itemValues();
                float hasValue = 0;
                for (Map.Entry<Item, Integer> itemIntegerEntry : itemTimes.entrySet()) {
                    hasValue += itemValues.getOrDefault(itemIntegerEntry.getKey(), 0f) * itemIntegerEntry.getValue();
                }
                float min = minERequire.getMin() - hasValue;
                float singleMin = min / mMinEAmount, value = 0;
                for (Item item : itemSet) {
                    if (!itemValues.containsKey(item)) continue;
                    float itemValue = itemValues.get(item);
                    if (itemValue <= singleMin) continue;
                    for (int integer = 0; integer < available.get(item) - itemTimes.getOrDefault(item, 0); integer++) {
                        leftUnlockSlot[0]--;
                        ItemStack stack = item.getDefaultInstance();
                        invIngredient.add(item);
                        if (stack.getMaxStackSize() == 1) {
                            single[0] = true;
                            itemTimes.put(item, 1);
                        } else {
                            itemTimes.merge(item, 1, Integer::sum);
                        }
                        if ((value += itemValue) > min) {
                            hasIngredient = true;
                            break;
                        }
                    }
                    if (value > min) {
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

    private static boolean processMaxRequires(int times, List<RequirementCategoryMax> maxRequires, Set<Item> itemSet, int[] leftUnlockSlot, boolean[] single, List<Item> invIngredient, Map<Item, Integer> itemTimes, Map<Item, Integer> available) {
        if (maxRequires.isEmpty()) return true;

        float mMaxAmount = (float) leftUnlockSlot[0] / maxRequires.size();
        for (RequirementCategoryMax maxRequire : maxRequires) {
            for (int i = 0; i < times && leftUnlockSlot[0] != 0; i++) {
                FoodValue foodValue = REQUIREMENT_FOOD_VALUE_HASH_MAP.get(maxRequire.getCategory());
                Map<Item, Float> itemValues = foodValue.itemValues();
                float hasValue = 0;
                for (Map.Entry<Item, Integer> itemIntegerEntry : itemTimes.entrySet()) {
                    hasValue += itemValues.getOrDefault(itemIntegerEntry.getKey(), 0f) * itemIntegerEntry.getValue();
                }
                float max = maxRequire.getMax() - hasValue;
                float singleMin = max / mMaxAmount, value = 0;
                if (singleMin <= 0) continue;
                for (Item item : itemSet) {
                    if (!itemValues.containsKey(item)) continue;
                    float itemValue = itemValues.get(item);
                    if (itemValue > singleMin) continue;
                    for (int integer = 0; integer < available.get(item) - itemTimes.getOrDefault(item, 0); integer++) {
                        leftUnlockSlot[0]--;
                        ItemStack stack = item.getDefaultInstance();
                        invIngredient.add(item);
                        if (stack.getMaxStackSize() == 1) {
                            single[0] = true;
                            itemTimes.put(item, 1);
                        } else {
                            itemTimes.merge(item, 1, Integer::sum);
                        }
                        if ((value += itemValue) >= max) {
                            break;
                        }
                    }
                    if (value >= max) {
                        break;
                    }
                }
            }
        }
        return true;
    }

    private static boolean processMaxERequires(int times, List<RequirementCategoryMaxExclusive> maxERequires, Set<Item> itemSet, int[] leftUnlockSlot, boolean[] single, List<Item> invIngredient, Map<Item, Integer> itemTimes, Map<Item, Integer> available) {
        if (maxERequires.isEmpty()) return true;

        float mMaxEAmount = (float) leftUnlockSlot[0] / maxERequires.size();
        for (RequirementCategoryMaxExclusive maxRequire : maxERequires) {
            for (int i = 0; i < times && leftUnlockSlot[0] != 0; i++) {
                FoodValue foodValue = REQUIREMENT_FOOD_VALUE_HASH_MAP.get(maxRequire.getCategory());
                Map<Item, Float> itemValues = foodValue.itemValues();
                float hasValue = 0;
                for (Map.Entry<Item, Integer> itemIntegerEntry : itemTimes.entrySet()) {
                    hasValue += itemValues.getOrDefault(itemIntegerEntry.getKey(), 0f) * itemIntegerEntry.getValue();
                }
                float max = maxRequire.getMax() - hasValue;
                float singleMin = max / mMaxEAmount, value = 0;
                if (singleMin <= 0) break;
                for (Item item : itemSet) {
                    if (!itemValues.containsKey(item)) continue;
                    float itemValue = itemValues.get(item);
                    if (itemValue >= singleMin) continue;
                    for (int integer = 0; integer < available.get(item) - itemTimes.getOrDefault(item, 0); integer++) {
                        leftUnlockSlot[0]--;
                        ItemStack stack = item.getDefaultInstance();
                        invIngredient.add(item);
                        if (stack.getMaxStackSize() == 1) {
                            single[0] = true;
                            itemTimes.put(item, 1);
                        } else {
                            itemTimes.merge(item, 1, Integer::sum);
                        }
                        if ((value += itemValue) >= max) {
                            break;
                        }
                    }
                    if (value >= max) {
                        break;
                    }
                }
            }
        }
        return true;
    }

    private static boolean processEAnyRequires(int times, List<RequirementCategoryMinExclusive> anyRequires, Set<Item> itemSet, int[] leftUnlockSlot, boolean[] single, List<Item> invIngredient, Map<Item, Integer> itemTimes, Map<Item, Integer> available) {
        if (anyRequires.isEmpty()) return true;

        int anyTime = leftUnlockSlot[0] / anyRequires.size();
        for (RequirementCategoryMinExclusive anyRequire : anyRequires) {

            for (int i = 0; i < anyTime && leftUnlockSlot[0] != 0; i++) {
                boolean hasIngredient = false;

                Set<Item> quireItems = getQuireItemSet(anyRequire);
                for (Item item : itemSet) {
                    if (quireItems.contains(item)) {
                        leftUnlockSlot[0]--;

                        ItemStack stack = item.getDefaultInstance();
                        invIngredient.add(item);

                        if (stack.getMaxStackSize() == 1) {
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

    private static boolean processEMustRequires(int times, List<RequirementMustContainIngredient> mustRequires, Set<Item> itemSet, int[] leftUnlockSlot, boolean[] single, List<Item> invIngredient, Map<Item, Integer> itemTimes, Map<Item, Integer> available) {
        if (mustRequires.isEmpty()) return true;

        int mustSize = leftUnlockSlot[0] / mustRequires.size();
        for (RequirementMustContainIngredient mustRequire : mustRequires) {
            for (int i = 0; i < mustSize && leftUnlockSlot[0] != 0; i++) {
                boolean hasIngredient = false;

                Set<Item> quireItems = getQuireItemSet(mustRequire);
                int quantity = mustRequire.getQuantity();
                for (Item item : itemSet) {
                    if (!quireItems.contains(item)) continue;
                    for (int integer = 0; integer < available.get(item) - itemTimes.getOrDefault(item, 0); integer++) {
                        leftUnlockSlot[0]--;

                        ItemStack stack = item.getDefaultInstance();
                        invIngredient.add(item);

                        if (stack.getMaxStackSize() == 1) {
                            single[0] = true;
                            itemTimes.put(item, 1);
                        } else {
                            itemTimes.merge(item, 1, Integer::sum);
                        }

                        if (--quantity == 0) {
                            hasIngredient = true;
                            break;
                        }
                    }

                    if (quantity == 0) {
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
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof CrockPotBlockEntity;
    }

    @Override
    public RecipeType<CrockPotCookingRecipe> getRecipeType() {
        return CrockPotRecipes.CROCK_POT_COOKING_RECIPE_TYPE.get();
    }

    @Override
    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid maid, CrockPotBlockEntity blockEntity, MaidRecipesManager<CrockPotCookingRecipe> recManager) {

        CombinedInvWrapper availableInv = maid.getAvailableInv(true);
        ItemStackHandler itemHandler = blockEntity.getItemHandler();
        boolean cooking = blockEntity.isCooking();
        boolean b = crockPotRecMatch(serverLevel, blockEntity);
        boolean findFuel = ItemsUtil.findStackSlot(availableInv, itemStack -> ForgeHooks.getBurnTime(itemStack, null) > 0) > -1;

        boolean canBurn = blockEntity.isBurning() || !itemHandler.getStackInSlot(4).isEmpty() || findFuel;

        if (!itemHandler.getStackInSlot(getOutputSlot()).isEmpty()) {
            return true;
        }

        if (!cooking && !b && canBurn && !recManager.getRecipesIngredients().isEmpty()) {
            return true;
        }

        if (!cooking && !b && hasInput(itemHandler)) {
            return true;
        }

        if (cooking && itemHandler.getStackInSlot(4).isEmpty() && findFuel) {
            return true;
        }

        return false;
    }

    @Override
    public void processCookMake(ServerLevel serverLevel, EntityMaid maid, CrockPotBlockEntity blockEntity, MaidRecipesManager<CrockPotCookingRecipe> recManager) {
        extract(serverLevel, maid, blockEntity, recManager);
        insert(serverLevel, maid, blockEntity, recManager);

        recManager.getCookInv().syncInv();
    }

    private void extract(ServerLevel serverLevel, EntityMaid maid, CrockPotBlockEntity blockEntity, MaidRecipesManager<CrockPotCookingRecipe> recManager) {
        CombinedInvWrapper availableInv = maid.getAvailableInv(true);
        IItemHandlerModifiable ingreInv = recManager.getInputInv();
        IItemHandlerModifiable outputInv = recManager.getOutputInv();
        ItemStackHandler beInv = blockEntity.getItemHandler();
        boolean cooking = blockEntity.isCooking();
        boolean b = crockPotRecMatch(serverLevel, blockEntity);

        boolean findFuel = ItemsUtil.findStackSlot(availableInv, itemStack -> ForgeHooks.getBurnTime(itemStack, null) > 0) > -1;

        boolean canBurn = blockEntity.isBurning() || !beInv.getStackInSlot(4).isEmpty() || findFuel;

        if (!beInv.getStackInSlot(getOutputSlot()).isEmpty()) {
            extractOutputStack(beInv, outputInv, blockEntity);
            blockEntity.setChanged();
        }

        if (!cooking && !b && hasInput(beInv)) {
            extractInputsStack(beInv, ingreInv, blockEntity);
            blockEntity.setChanged();
        }
        pickupAction(maid);

    }

    private void insert(ServerLevel serverLevel, EntityMaid maid, CrockPotBlockEntity blockEntity, MaidRecipesManager<CrockPotCookingRecipe> recManager) {
        CombinedInvWrapper availableInv = maid.getAvailableInv(true);
        ItemStackHandler beInv = blockEntity.getItemHandler();

        int stackSlot = ItemsUtil.findStackSlot(availableInv, itemStack -> ForgeHooks.getBurnTime(itemStack, null) > 0);
        boolean findFuel = stackSlot > -1;

        if (beInv.getStackInSlot(4).isEmpty() && findFuel) {
            ItemStack stackInSlot = availableInv.getStackInSlot(stackSlot);
            ItemStack insertItem = beInv.insertItem(4, stackInSlot.copy(), false);
            stackInSlot.shrink(stackInSlot.getCount() - insertItem.getCount());
            blockEntity.setChanged();
        }

        boolean canBurn = blockEntity.isBurning() || !beInv.getStackInSlot(4).isEmpty();
        boolean cooking = blockEntity.isCooking();
        boolean b = crockPotRecMatch(serverLevel, blockEntity);
        if (!cooking && !b && canBurn && !recManager.getRecipesIngredients().isEmpty()) {
            Pair<List<Integer>, List<List<ItemStack>>> recipeIngredient = recManager.getRecipeIngredient();
            if (recipeIngredient.getFirst().isEmpty()) return;
            insertInputsStack(beInv, availableInv, blockEntity, recipeIngredient);
            blockEntity.setChanged();
        }
        pickupAction(maid);

    }

    @Override
    public MaidRecipesManager<CrockPotCookingRecipe> getRecipesManager(EntityMaid maid) {
        return new MaidRecipesManager<>(maid, this, false) {
            @Override
            protected List<Pair<List<Integer>, List<Item>>> createIngres(Map<Item, Integer> available, boolean setRecipeIngres) {
                List<Pair<List<Integer>, List<Item>>> _make = new ArrayList<>();

                available.keySet().removeIf(item -> !INVID_ITEMS.contains(item));
                RecInfo1 recInfo1 = new RecInfo1();

                for (CrockPotCookingRecipe rec : this.currentRecs) {
                    recInfo1.clear();
                    Pair<List<Integer>, List<Item>> maxCount = getAmountIngredientA2(recInfo1, rec, available);
                    if (!maxCount.getFirst().isEmpty()) {
                        _make.add(Pair.of(maxCount.getFirst(), maxCount.getSecond()));
                    }
                }

                if (setRecipeIngres) {
                    setRecIngres(_make, available);
                }

                return _make;
            }
        };

    }

    protected Pair<List<Integer>, List<Item>> getAmountIngredientA2(RecInfo1 recInfo, CrockPotCookingRecipe recipe, Map<Item, Integer> available) {
        List<RequirementCategoryMax> noRequires = recInfo.noRequires;
        List<RequirementCategoryMax> maxRequires = recInfo.maxRequires;
        List<RequirementCategoryMaxExclusive> maxERequires = recInfo.maxERequires;
        List<RequirementCategoryMinExclusive> anyRequires = recInfo.anyRequires;
        List<RequirementCategoryMinExclusive> minERequires = recInfo.minERequires;
        List<RequirementCategoryMin> minRequires = recInfo.minRequires;
        List<RequirementMustContainIngredient> mustRequires = recInfo.mustRequires;
        List<RequirementMustContainIngredientLessThan> mustLessRequires = recInfo.mustLessRequires;

        List<IRequirement> requirements = recipe.getRequirements();
        categorizeRequirements(requirements, noRequires, maxRequires, maxERequires, anyRequires, minERequires, minRequires, mustRequires, mustLessRequires);

        Set<Item> itemSet = new HashSet<>(available.keySet());
        removeNoRequiresItems(noRequires, itemSet);

        if (itemSet.isEmpty()) {
            return Pair.of(Collections.emptyList(), Collections.emptyList());
        }

        int[] leftUnlockSlot = {4};
        boolean[] single = {false};
        List<Item> invIngredient = recInfo.invIngredient;
        Map<Item, Integer> itemTimes = recInfo.itemTimes;

        boolean processAnyRequires = processAnyRequires(1, anyRequires, itemSet, leftUnlockSlot, single, invIngredient, itemTimes, available);
        if (!processAnyRequires) {
            return Pair.of(Collections.emptyList(), Collections.emptyList());
        }
        if (leftUnlockSlot[0] == 0) {
            return getListListPair(available, single, itemTimes, invIngredient);
        }

        boolean processMustRequires = processMustRequires(1, mustRequires, itemSet, leftUnlockSlot, single, invIngredient, itemTimes, available);
        if (!processMustRequires) {
            return Pair.of(Collections.emptyList(), Collections.emptyList());
        }
        if (leftUnlockSlot[0] == 0) {
            return getListListPair(available, single, itemTimes, invIngredient);
        }

        boolean processMinRequires = processMinRequires(1, minRequires, itemSet, leftUnlockSlot, single, invIngredient, itemTimes, available);
        if (!processMinRequires) {
            return Pair.of(Collections.emptyList(), Collections.emptyList());
        }
        if (leftUnlockSlot[0] == 0) {
            return getListListPair(available, single, itemTimes, invIngredient);
        }

        boolean processMinERequires = processMinERequires(1, minERequires, itemSet, leftUnlockSlot, single, invIngredient, itemTimes, available);
        if (!processMinERequires) {
            return Pair.of(Collections.emptyList(), Collections.emptyList());
        }
        if (leftUnlockSlot[0] == 0) {
            return getListListPair(available, single, itemTimes, invIngredient);
        }

        boolean processMaxRequires = processMaxRequires(1, maxRequires, itemSet, leftUnlockSlot, single, invIngredient, itemTimes, available);
        if (!processMaxRequires) {
            return Pair.of(Collections.emptyList(), Collections.emptyList());
        }
        if (leftUnlockSlot[0] == 0) {
            return getListListPair(available, single, itemTimes, invIngredient);
        }

        boolean processMaxERequires = processMaxERequires(1, maxERequires, itemSet, leftUnlockSlot, single, invIngredient, itemTimes, available);
        if (!processMaxERequires) {
            return Pair.of(Collections.emptyList(), Collections.emptyList());
        }
        if (leftUnlockSlot[0] == 0) {
            return getListListPair(available, single, itemTimes, invIngredient);
        }

        if (leftUnlockSlot[0] > 0) {
            boolean processEAnyRequires = processAnyRequires(anyRequires.isEmpty() ? 0 : anyRequires.size() >= leftUnlockSlot[0] ? 1 : leftUnlockSlot[0], anyRequires, itemSet, leftUnlockSlot, single, invIngredient, itemTimes, available);
            if (!processEAnyRequires) {
                return Pair.of(Collections.emptyList(), Collections.emptyList());
            }
            if (leftUnlockSlot[0] == 0) {
                return getListListPair(available, single, itemTimes, invIngredient);
            }

            boolean processEMustRequires = processMustRequires(mustRequires.isEmpty() ? 0 : mustRequires.size() >= leftUnlockSlot[0] ? 1 : leftUnlockSlot[0], mustRequires, itemSet, leftUnlockSlot, single, invIngredient, itemTimes, available);
            if (!processEMustRequires) {
                return Pair.of(Collections.emptyList(), Collections.emptyList());
            }
            if (leftUnlockSlot[0] == 0) {
                return getListListPair(available, single, itemTimes, invIngredient);
            }

            boolean processEMinRequires = processMinRequires(minRequires.isEmpty() ? 0 : minRequires.size() >= leftUnlockSlot[0] ? 1 : leftUnlockSlot[0], minRequires, itemSet, leftUnlockSlot, single, invIngredient, itemTimes, available);
            if (!processEMinRequires) {
                return Pair.of(Collections.emptyList(), Collections.emptyList());
            }
            if (leftUnlockSlot[0] == 0) {
                return getListListPair(available, single, itemTimes, invIngredient);
            }

            boolean processEMinERequires = processMinERequires(minERequires.isEmpty() ? 0 : minERequires.size() >= leftUnlockSlot[0] ? 1 : leftUnlockSlot[0], minERequires, itemSet, leftUnlockSlot, single, invIngredient, itemTimes, available);
            if (!processEMinERequires) {
                return Pair.of(Collections.emptyList(), Collections.emptyList());
            }
            if (leftUnlockSlot[0] == 0) {
                return getListListPair(available, single, itemTimes, invIngredient);
            }

            boolean processEMaxRequires = processMaxRequires(maxRequires.isEmpty() ? 0 : maxRequires.size() >= leftUnlockSlot[0] ? 1 : leftUnlockSlot[0], maxRequires, itemSet, leftUnlockSlot, single, invIngredient, itemTimes, available);
            if (!processEMaxRequires) {
                return Pair.of(Collections.emptyList(), Collections.emptyList());
            }
            if (leftUnlockSlot[0] == 0) {
                return getListListPair(available, single, itemTimes, invIngredient);
            }

            boolean processEMaxERequires = processMaxERequires(maxERequires.isEmpty() ? 0 : maxERequires.size() >= leftUnlockSlot[0] ? 1 : leftUnlockSlot[0], maxERequires, itemSet, leftUnlockSlot, single, invIngredient, itemTimes, available);
            if (!processEMaxERequires) {
                return Pair.of(Collections.emptyList(), Collections.emptyList());
            }
            if (leftUnlockSlot[0] == 0) {
                return getListListPair(available, single, itemTimes, invIngredient);
            }
        }

        return Pair.of(Collections.emptyList(), Collections.emptyList());
    }

    protected Pair<List<Integer>, List<Item>> getAmountIngredientA1(RecInfo1 recInfo, CrockPotCookingRecipe recipe, Map<Item, Integer> available) {
        List<RequirementCategoryMax> noRequires = recInfo.noRequires;
        List<RequirementCategoryMax> maxRequires = recInfo.maxRequires;
        List<RequirementCategoryMaxExclusive> maxERequires = recInfo.maxERequires;

        List<RequirementCategoryMinExclusive> anyRequires = recInfo.anyRequires;
        List<RequirementCategoryMinExclusive> minERequires = recInfo.minERequires;
        List<RequirementCategoryMin> minRequires = recInfo.minRequires;


        List<RequirementMustContainIngredient> mustRequires = recInfo.mustRequires;
        List<RequirementMustContainIngredientLessThan> mustLessRequires = recInfo.mustLessRequires;


        List<IRequirement> requirements = recipe.getRequirements();
        for (IRequirement requirement : requirements) {
            if (requirement instanceof RequirementCategoryMax requirementCategoryMax) {
                if (MathUtils.fuzzyIsZero(requirementCategoryMax.getMax())) {
                    noRequires.add(requirementCategoryMax);
                } else {
                    maxRequires.add(requirementCategoryMax);
                }
            } else if (requirement instanceof RequirementCategoryMinExclusive requirementCategoryMinExclusive) {
                if (MathUtils.fuzzyIsZero(requirementCategoryMinExclusive.getMin())) {
                    anyRequires.add(requirementCategoryMinExclusive);
                } else {
                    minERequires.add(requirementCategoryMinExclusive);
                }
            } else if (requirement instanceof RequirementCategoryMin requirementCategoryMin) {
                minRequires.add(requirementCategoryMin);
            } else if (requirement instanceof RequirementCategoryMaxExclusive requirementCategoryMaxE) {
                maxERequires.add(requirementCategoryMaxE);
            } else if (requirement instanceof RequirementMustContainIngredient requirementMustContainIngredient) {
                mustRequires.add(requirementMustContainIngredient);
            } else if (requirement instanceof RequirementMustContainIngredientLessThan requirementMustContainIngredientLessThan) {
                mustLessRequires.add(requirementMustContainIngredientLessThan);
            }
        }

        Set<Item> itemSet = new HashSet<>(available.keySet());

        for (RequirementCategoryMax noRequire : noRequires) {
            Set<Item> quireItems = getQuireItemSet(noRequire);
            itemSet.removeAll(quireItems);
        }
        if (itemSet.isEmpty()) {
            return Pair.of(Collections.emptyList(), Collections.emptyList());
        }


        int[] leftUnlockSlot = {4};

        boolean[] single = {false};
        List<Item> invIngredient = new ArrayList<>();
        Map<Item, Integer> itemTimes = new HashMap<>();


        // 任意
        for (RequirementCategoryMinExclusive anyRequire : anyRequires) {
            boolean hasIngredient = false;

            Set<Item> quireItems = getQuireItemSet(anyRequire);
            for (Item item : itemSet) {
                if (quireItems.contains(item)) {
                    leftUnlockSlot[0]--;

                    ItemStack stack = item.getDefaultInstance();
                    invIngredient.add(item);

                    if (stack.getMaxStackSize() == 1) {
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
                return Pair.of(Collections.emptyList(), Collections.emptyList());
            }
        }
        if (leftUnlockSlot[0] == 0) {
            return getListListPair(available, single, itemTimes, invIngredient);
        }

        // 必须
        for (RequirementMustContainIngredient mustRequire : mustRequires) {
            boolean hasIngredient = false;

            Set<Item> quireItems = getQuireItemSet(mustRequire);
            int quantity = mustRequire.getQuantity();
            for (Item item : itemSet) {
                if (!quireItems.contains(item)) continue;
                for (int integer = 0; integer < available.get(item) - itemTimes.getOrDefault(item, 0); integer++) {
                    leftUnlockSlot[0]--;

                    ItemStack stack = item.getDefaultInstance();
                    invIngredient.add(item);

                    if (stack.getMaxStackSize() == 1) {
                        single[0] = true;
                        itemTimes.put(item, 1);
                    } else {
                        itemTimes.merge(item, 1, Integer::sum);
                    }

                    if (--quantity == 0) {
                        hasIngredient = true;
                        break;
                    }
                }

                if (quantity == 0) {
                    break;
                }
            }

            if (!hasIngredient) {
                return Pair.of(Collections.emptyList(), Collections.emptyList());
            }
        }
        if (leftUnlockSlot[0] == 0) {
            return getListListPair(available, single, itemTimes, invIngredient);
        }

        // >=
        float mMinAmount = (float) leftUnlockSlot[0] / minRequires.size();
        for (RequirementCategoryMin minRequire : minRequires) {
            boolean hasIngredient = false;

            FoodValue foodValue = REQUIREMENT_FOOD_VALUE_HASH_MAP.get(minRequire.getCategory());
            Map<Item, Float> itemValues = foodValue.itemValues();

            float hasValue = 0;
            for (Map.Entry<Item, Integer> itemIntegerEntry : itemTimes.entrySet()) {
                hasValue += itemValues.getOrDefault(itemIntegerEntry.getKey(), 0f) * itemIntegerEntry.getValue();
            }

            float min = minRequire.getMin() - hasValue;
            float singleMin = min / mMinAmount, value = 0;

            for (Item item : itemSet) {
                if (!itemValues.containsKey(item)) continue;

                float itemValue = itemValues.get(item);
                if (itemValue < singleMin) continue;

                for (int integer = 0; integer < available.get(item) - itemTimes.getOrDefault(item, 0); integer++) {

                    leftUnlockSlot[0]--;

                    ItemStack stack = item.getDefaultInstance();
                    invIngredient.add(item);

                    if (stack.getMaxStackSize() == 1) {
                        single[0] = true;
                        itemTimes.put(item, 1);
                    } else {
                        itemTimes.merge(item, 1, Integer::sum);
                    }

                    if ((value += itemValue) >= min) {
                        hasIngredient = true;
                        break;
                    }

                }

                if (value >= min) {
                    break;
                }
            }

            if (!hasIngredient) {
                return Pair.of(Collections.emptyList(), Collections.emptyList());
            }
        }
        if (leftUnlockSlot[0] == 0) {
            return getListListPair(available, single, itemTimes, invIngredient);
        }

        // >
        float mMinEAmount = (float) leftUnlockSlot[0] / minERequires.size();
        for (RequirementCategoryMinExclusive minERequire : minERequires) {
            boolean hasIngredient = false;

            FoodValue foodValue = REQUIREMENT_FOOD_VALUE_HASH_MAP.get(minERequire.getCategory());
            Map<Item, Float> itemValues = foodValue.itemValues();

            float hasValue = 0;
            for (Map.Entry<Item, Integer> itemIntegerEntry : itemTimes.entrySet()) {
                hasValue += itemValues.getOrDefault(itemIntegerEntry.getKey(), 0f) * itemIntegerEntry.getValue();
            }

            float min = minERequire.getMin() - hasValue;
            float singleMin = min / mMinEAmount, value = 0;

            for (Item item : itemSet) {
                if (!itemValues.containsKey(item)) continue;

                float itemValue = itemValues.get(item);
                if (itemValue <= singleMin) continue;

                for (int integer = 0; integer < available.get(item) - itemTimes.getOrDefault(item, 0); integer++) {
                    leftUnlockSlot[0]--;

                    ItemStack stack = item.getDefaultInstance();
                    invIngredient.add(item);

                    if (stack.getMaxStackSize() == 1) {
                        single[0] = true;
                        itemTimes.put(item, 1);
                    } else {
                        itemTimes.merge(item, 1, Integer::sum);
                    }

                    if ((value += itemValue) > min) {
                        hasIngredient = true;
                        break;
                    }

                }

                if (value > min) {
                    break;
                }
            }

            if (!hasIngredient) {
                return Pair.of(Collections.emptyList(), Collections.emptyList());
            }
        }

        // <=
        float mMaxAmount = (float) leftUnlockSlot[0] / maxRequires.size();
        for (RequirementCategoryMax maxRequire : maxRequires) {

            FoodValue foodValue = REQUIREMENT_FOOD_VALUE_HASH_MAP.get(maxRequire.getCategory());
            Map<Item, Float> itemValues = foodValue.itemValues();

            float hasValue = 0;
            for (Map.Entry<Item, Integer> itemIntegerEntry : itemTimes.entrySet()) {
                hasValue += itemValues.getOrDefault(itemIntegerEntry.getKey(), 0f) * itemIntegerEntry.getValue();
            }

            float max = maxRequire.getMax() - hasValue;
            float singleMin = max / mMaxAmount, value = 0;

            if (singleMin <= 0) continue;

            for (Item item : itemSet) {
                if (!itemValues.containsKey(item)) continue;

                float itemValue = itemValues.get(item);
                if (itemValue > singleMin) continue;

                for (int integer = 0; integer < available.get(item) - itemTimes.getOrDefault(item, 0); integer++) {

                    leftUnlockSlot[0]--;

                    ItemStack stack = item.getDefaultInstance();
                    invIngredient.add(item);

                    if (stack.getMaxStackSize() == 1) {
                        single[0] = true;
                        itemTimes.put(item, 1);
                    } else {
                        itemTimes.merge(item, 1, Integer::sum);
                    }

                    if ((value += itemValue) >= max) {
                        break;
                    }

                }

                if (value >= max) {
                    break;
                }
            }
        }
        if (leftUnlockSlot[0] == 0) {
            return getListListPair(available, single, itemTimes, invIngredient);
        }

        // <
        float mMaxEAmount = (float) leftUnlockSlot[0] / maxERequires.size();
        for (RequirementCategoryMaxExclusive maxRequire : maxERequires) {

            FoodValue foodValue = REQUIREMENT_FOOD_VALUE_HASH_MAP.get(maxRequire.getCategory());
            Map<Item, Float> itemValues = foodValue.itemValues();

            float hasValue = 0;
            for (Map.Entry<Item, Integer> itemIntegerEntry : itemTimes.entrySet()) {
                hasValue += itemValues.getOrDefault(itemIntegerEntry.getKey(), 0f) * itemIntegerEntry.getValue();
            }

            float max = maxRequire.getMax() - hasValue;
            float singleMin = max / mMaxEAmount, value = 0;

            if (singleMin <= 0) break;

            for (Item item : itemSet) {
                if (!itemValues.containsKey(item)) continue;

                float itemValue = itemValues.get(item);
                if (itemValue >= singleMin) continue;

                for (int integer = 0; integer < available.get(item) - itemTimes.getOrDefault(item, 0); integer++) {

                    leftUnlockSlot[0]--;

                    ItemStack stack = item.getDefaultInstance();
                    invIngredient.add(item);

                    if (stack.getMaxStackSize() == 1) {
                        single[0] = true;
                        itemTimes.put(item, 1);
                    } else {
                        itemTimes.merge(item, 1, Integer::sum);
                    }

                    if ((value += itemValue) >= max) {
                        break;
                    }

                }

                if (value >= max) {
                    break;
                }
            }
        }
        if (leftUnlockSlot[0] == 0) {
            return getListListPair(available, single, itemTimes, invIngredient);
        }

        if (leftUnlockSlot[0] > 0) {
            if(!anyRequires.isEmpty()) {
                int anyTime = leftUnlockSlot[0] / anyRequires.size();
                for (RequirementCategoryMinExclusive anyRequire : anyRequires) {

                    for (int i = 0; i < anyTime; i++) {
                        boolean hasIngredient = false;

                        Set<Item> quireItems = getQuireItemSet(anyRequire);
                        for (Item item : itemSet) {
                            if (quireItems.contains(item)) {
                                leftUnlockSlot[0]--;

                                ItemStack stack = item.getDefaultInstance();
                                invIngredient.add(item);

                                if (stack.getMaxStackSize() == 1) {
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
                            return Pair.of(Collections.emptyList(), Collections.emptyList());
                        }

                    }
                }
                if (leftUnlockSlot[0] == 0) {
                    return getListListPair(available, single, itemTimes, invIngredient);
                }
            }

            if (!mustRequires.isEmpty()) {
                int mustSize = leftUnlockSlot[0] / mustRequires.size();
                for (RequirementMustContainIngredient mustRequire : mustRequires) {
                    for (int i = 0; i < mustSize; i++) {
                        boolean hasIngredient = false;

                        Set<Item> quireItems = getQuireItemSet(mustRequire);
                        int quantity = mustRequire.getQuantity();
                        for (Item item : itemSet) {
                            if (!quireItems.contains(item)) continue;
                            for (int integer = 0; integer < available.get(item) - itemTimes.getOrDefault(item, 0); integer++) {
                                leftUnlockSlot[0]--;

                                ItemStack stack = item.getDefaultInstance();
                                invIngredient.add(item);

                                if (stack.getMaxStackSize() == 1) {
                                    single[0] = true;
                                    itemTimes.put(item, 1);
                                } else {
                                    itemTimes.merge(item, 1, Integer::sum);
                                }

                                if (--quantity == 0) {
                                    hasIngredient = true;
                                    break;
                                }
                            }

                            if (quantity == 0) {
                                break;
                            }
                        }

                        if (!hasIngredient) {
                            return Pair.of(Collections.emptyList(), Collections.emptyList());
                        }
                    }
                }
                if (leftUnlockSlot[0] == 0) {
                    return getListListPair(available, single, itemTimes, invIngredient);
                }
            }
        }

        return Pair.of(Collections.emptyList(), Collections.emptyList());
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.CP_CROCK_POT.uid;
    }

    @Override
    public ItemStack getIcon() {
        return CrockPotBlocks.CROCK_POT.get().asItem().getDefaultInstance();
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.CP_CROCK_POT;
    }

    @Override
    public ItemStackHandler getItemStackHandler(CrockPotBlockEntity crockPotBlockEntity) {
        return crockPotBlockEntity.getItemHandler();
    }

    @Override
    public int getOutputSlot() {
        return 5;
    }

    @Override
    public int getInputSize() {
        return 4;
    }

    @Override
    public ItemStackHandler getBeInv(CrockPotBlockEntity crockPotBlockEntity) {
        return crockPotBlockEntity.getItemHandler();
    }

    @Override
    public NonNullList<Ingredient> getIngredients(Recipe<?> recipe) {
        CrockPotCookingRecipe crockPotCookingRecipe = (CrockPotCookingRecipe) recipe;
        return RECS.get(crockPotCookingRecipe).ingredients();
    }

    public boolean hasEnoughFavor(EntityMaid maid) {
        return maid.getFavorabilityManager().getLevel() >= 2;
    }

    @Override
    public List<CrockPotCookingRecipe> getRecipes(Level level) {
        if (RECS.isEmpty()) {

            FOOD_CATEGORY_INGREDIENT_MAP.clear();
            REQUIREMENT_FOOD_CATEGORY_MAP.clear();

            for (FoodCategory value : FoodCategory.values()) {
                List<Item> items = FoodValuesDefinition.getMatchedItems(value, level).stream().map(ItemStack::getItem).toList();
                FOOD_CATEGORY_INGREDIENT_MAP.put(value, items);
            }

            List<CrockPotCookingRecipe> allRecipesFor = level.getRecipeManager().getAllRecipesFor(CrockPotRecipes.CROCK_POT_COOKING_RECIPE_TYPE.get())
                    .stream().filter(crockPotCookingRecipe -> !crockPotCookingRecipe.getId().getPath().equals("crock_pot_cooking/wet_goop")).toList();
            for (CrockPotCookingRecipe crockPotCookingRecipe : allRecipesFor) {

                NonNullList<Ingredient> nonNullList = NonNullList.create();
                List<List<Item>> lists = new ArrayList<>();
                for (IRequirement requirement : crockPotCookingRecipe.getRequirements()) {

                    if (REQUIREMENT_INGREDIENTY_MAP.containsKey(requirement)) {
                        List<Item> items1 = REQUIREMENT_INGREDIENTY_MAP.get(requirement);
                        lists.add(items1);
                        nonNullList.add(Ingredient.of(items1.stream().map(Item::getDefaultInstance)));
                    } else if (requirement instanceof RequirementMustContainIngredient requirementMustContainIngredient) {
                        ItemStack[] items = requirementMustContainIngredient.getIngredient().getItems();
                        List<Item> list = Arrays.stream(items).map(ItemStack::getItem).toList();
                        REQUIREMENT_INGREDIENTY_MAP.put(requirementMustContainIngredient, list);
                        lists.add(list);
                        nonNullList.add(Ingredient.of(items));
                    } else if (requirement instanceof RequirementMustContainIngredientLessThan requirementMustContainIngredientLessThan) {
                        ItemStack[] items = requirementMustContainIngredientLessThan.getIngredient().getItems();
                        List<Item> list = Arrays.stream(items).map(ItemStack::getItem).toList();
                        REQUIREMENT_INGREDIENTY_MAP.put(requirementMustContainIngredientLessThan, list);
                        lists.add(list);
                        nonNullList.add(Ingredient.of(items));
                    } else {
                        JsonElement json = requirement.toJson();
                        JsonElement category2 = json.getAsJsonObject().get("category");
                        if (category2 == null) continue;
                        String category1 = category2.getAsString();
                        FoodCategory category = EnumUtils.getEnum(FoodCategory.class, category1.toUpperCase());
                        REQUIREMENT_FOOD_CATEGORY_MAP.put(requirement, category);
                        List<Item> items1 = FOOD_CATEGORY_INGREDIENT_MAP.get(category);
                        REQUIREMENT_INGREDIENTY_MAP.put(requirement, items1);
                        lists.add(items1);
                        nonNullList.add(Ingredient.of(items1.stream().map(Item::getDefaultInstance)));
                    }

                }

                RECS.put(crockPotCookingRecipe, new MaidRec<>(crockPotCookingRecipe, lists, nonNullList, crockPotCookingRecipe.getResult()));
            }

            FOOD_CATEGORY_INGREDIENT_MAP.forEach((foodCategory, items) -> {
                HashMap<Item, Float> itemFloatHashMap = new HashMap<>();
                for (Item item : items) {
                    FoodValues foodValues = FoodValuesDefinition.getFoodValues(item.getDefaultInstance(), level);
                    itemFloatHashMap.put(item, foodValues.get(foodCategory));

                    INVID_ITEMS.add(item);
                }
                REQUIREMENT_FOOD_VALUE_HASH_MAP.put(foodCategory, new FoodValue(foodCategory, itemFloatHashMap));
            });

            return allRecipesFor;
        }

//        RECS.clear();
        return RECS.keySet().stream().toList();
    }

    @Override
    public Optional<TooltipComponent> getRecClientAmountTooltip(Recipe<?> recipe, boolean modeRandom, boolean overSize) {
        RecInfo1 recInfo1 = new RecInfo1();
        List<IRequirement> requirements = ((CrockPotCookingRecipe)recipe).getRequirements();
        categorizeRequirements(requirements, recInfo1);

        CrockPotTooltip crockPotTooltip = new CrockPotTooltip(recInfo1, REQUIREMENT_INGREDIENTY_MAP, modeRandom, overSize);
        return Optional.of(crockPotTooltip);
    }

    @Override
    public List<Component> getWarnComponent() {
        return List.of(Component.translatable("gui.maidsoulkitchen.btn.cook_guide.info.warn").withStyle(ChatFormatting.YELLOW),
                Component.translatable("gui.maidsoulkitchen.btn.cook_guide.info.warn.crockpot"));
    }

    public static class RecInfo1 {
        private final List<RequirementCategoryMax> noRequires = new ArrayList<>();
        private final List<RequirementCategoryMax> maxRequires = new ArrayList<>();
        private final List<RequirementCategoryMaxExclusive> maxERequires = new ArrayList<>();
        private final List<RequirementCategoryMinExclusive> anyRequires = new ArrayList<>();
        private final List<RequirementCategoryMinExclusive> minERequires = new ArrayList<>();
        private final List<RequirementCategoryMin> minRequires = new ArrayList<>();
        private final List<RequirementMustContainIngredient> mustRequires = new ArrayList<>();
        private final List<RequirementMustContainIngredientLessThan> mustLessRequires = new ArrayList<>();
        private final List<Item> invIngredient = new ArrayList<>();
        private final Map<Item, Integer> itemTimes = new HashMap<>();

        public void clear() {
            noRequires.clear();
            maxRequires.clear();
            maxERequires.clear();
            anyRequires.clear();
            minERequires.clear();
            minRequires.clear();
            mustRequires.clear();
            mustLessRequires.clear();

            invIngredient.clear();
            itemTimes.clear();
        }

        public List<RequirementCategoryMax> getNoRequires() {
            return noRequires;
        }

        public List<RequirementCategoryMax> getMaxRequires() {
            return maxRequires;
        }

        public List<RequirementCategoryMaxExclusive> getMaxERequires() {
            return maxERequires;
        }

        public List<RequirementCategoryMinExclusive> getAnyRequires() {
            return anyRequires;
        }

        public List<RequirementCategoryMinExclusive> getMinERequires() {
            return minERequires;
        }

        public List<RequirementCategoryMin> getMinRequires() {
            return minRequires;
        }

        public List<RequirementMustContainIngredient> getMustRequires() {
            return mustRequires;
        }

        public List<RequirementMustContainIngredientLessThan> getMustLessRequires() {
            return mustLessRequires;
        }

        public List<Item> getInvIngredient() {
            return invIngredient;
        }

        public Map<Item, Integer> getItemTimes() {
            return itemTimes;
        }
    }

    public record MaidRec<R extends Recipe<? extends Container>>(R rec, List<List<Item>> items,
                                                                 NonNullList<Ingredient> ingredients,
                                                                 ItemStack result) {
    }

    public record FoodValue(FoodCategory foodCategory, Map<Item, Float> itemValues) {
    }
}
