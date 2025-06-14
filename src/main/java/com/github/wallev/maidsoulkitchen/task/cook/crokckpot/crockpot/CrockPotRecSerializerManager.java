package com.github.wallev.maidsoulkitchen.task.cook.crokckpot.crockpot;

import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ItemDefinition;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.ItemAmount;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec.MKRecipe;
import com.github.wallev.maidsoulkitchen.task.cook.crokckpot.crockpot.rec.FoodValue;
import com.github.wallev.maidsoulkitchen.task.cook.crokckpot.crockpot.rec.MKCrockPotRecipe;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.recipe.CrockPotRecipes;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.recipe.cooking.requirement.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.EnumUtils;

import java.util.*;

public class CrockPotRecSerializerManager extends RecSerializerManager<CrockPotCookingRecipe> {
    public static final String BLACK_REC = "crock_pot_cooking/wet_goop";
    private static final CrockPotRecSerializerManager INSTANCE = new CrockPotRecSerializerManager();

    public final Map<FoodCategory, List<Item>> FOOD_CATEGORY_INGREDIENT_MAP = new HashMap<>();
    public final Map<IRequirement, FoodCategory> REQUIREMENT_FOOD_CATEGORY_MAP = new HashMap<>();
    public final Map<IRequirement, List<Item>> REQUIREMENT_INGREDIENTY_MAP = new HashMap<>();
    public final Map<FoodCategory, FoodValue> REQUIREMENT_FOOD_VALUE_HASH_MAP = new HashMap<>();
    public final Set<Item> validIngres = Sets.newHashSet();

    public static CrockPotRecSerializerManager getInstance() {
        return INSTANCE;
    }

    protected CrockPotRecSerializerManager() {
        super(CrockPotRecipes.CROCK_POT_COOKING_RECIPE_TYPE.get());
    }

    private static void removeNoRequiresItems(List<Pair<RequirementCategoryMax, Set<Item>>> noRequires, Map<ItemDefinition, Long> available) {
        for (Pair<RequirementCategoryMax, Set<Item>> noRequire : noRequires) {
            Set<Item> nonItems = noRequire.getSecond();
            available.keySet().removeAll(nonItems);
        }
    }

    private static <IR extends IRequirement> boolean processRequires(int times, List<Pair<IR, Set<Item>>> requirementItems, Map<ItemDefinition, Long> available, int[] leftUnlockSlot, boolean[] single, List<Item> invIngredient, Map<Item, ItemAmount> itemTimes) {
        if (requirementItems.isEmpty()) {
            return true;
        }

        for (Pair<IR, Set<Item>> requirementItem : requirementItems) {
            Set<Item> quireItems = requirementItem.getSecond();
            for (int i = 0; i < times && leftUnlockSlot[0] > 0; i++) {
                boolean hasIngredient = false;
                for (Map.Entry<ItemDefinition, Long> entry : available.entrySet()) {
                    ItemDefinition key = entry.getKey();
                    Item item = key.item();

                    if (quireItems.contains(item)) {
                        hasIngredient = true;
                        leftUnlockSlot[0]--;
                        invIngredient.add(item);

                        int amount;
                        if (item.getMaxStackSize() == 1) {
                            single[0] = true;
                            ItemAmount itemAmount = new ItemAmount(1);
                            itemTimes.put(item, itemAmount);
                            amount = itemAmount.needCount();
                        } else {
                            ItemAmount itemAmount = itemTimes.computeIfAbsent(item, k -> new ItemAmount(1, 0));
                            itemAmount.addCount();
                            amount = itemAmount.needCount();
                        }

                        if (entry.getValue() < amount) {
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
        }
        return true;
    }

    @Override
    protected MaidRec recProcess(MKRecipe<CrockPotCookingRecipe> r, Map<ItemDefinition, Long> available, List<Item> invIngredient, boolean[] single, Map<Item, ItemAmount> itemTimes) {
        MKCrockPotRecipe cookRec = (MKCrockPotRecipe) r;

        HashMap<ItemDefinition, Long> retainAvailable = Maps.newHashMap(available);
        // 捕获所有合法的原材料
        retainAvailable.keySet().retainAll(cookRec.validInItemDefinitions());
        if (retainAvailable.isEmpty()) {
            return MaidRec.EMPTY;
        }

        // 移除不符合配方的原材料
        removeNoRequiresItems(cookRec.getNoRequires(), retainAvailable);
        if (retainAvailable.isEmpty()) {
            return MaidRec.EMPTY;
        }

        List<List<Pair<IRequirement, Set<Item>>>> needRequires = cookRec.getNeedRequires();

        int[] leftUnlockSlot = {4};
        // 正常情况下处理，一般来说一个配方会有四种供需的类型
        {
            for (List<Pair<IRequirement, Set<Item>>> needRequire : needRequires) {
                // 任意
                boolean processRequires = processRequires(1, needRequire, retainAvailable, leftUnlockSlot, single, invIngredient, itemTimes);
                if (!processRequires) {
                    return MaidRec.EMPTY;
                }
                if (leftUnlockSlot[0] == 0) {
                    return createCookRec(r, available, single, invIngredient, itemTimes);
                }
            }
        }

        // 如果上述处理完还有空余的格子，那就再来处理一遍，
        // 这个时候就还表明当前配方供需的类型不满足4种，
        // 有重复的，那就捕获重复的那种类型进行一次处理
        // 当然还是有瑕疵的，待优化
        // @todo optimize
        if (leftUnlockSlot[0] > 0) {
            for (List<Pair<IRequirement, Set<Item>>> needRequire : needRequires) {
                boolean processRequires = processRequires(needRequire.isEmpty() ? 0 : needRequire.size() >= leftUnlockSlot[0] ? 1 : leftUnlockSlot[0],
                        needRequire, retainAvailable, leftUnlockSlot, single, invIngredient, itemTimes);
                if (!processRequires) {
                    return MaidRec.EMPTY;
                }
                if (leftUnlockSlot[0] == 0) {
                    return createCookRec(r, available, single, invIngredient, itemTimes);
                }
            }
        }

        return MaidRec.EMPTY;

    }

    @Override
    protected void initRecs(Level level) {
        List<CrockPotCookingRecipe> recs = this.getRecsFromRm(level).stream()
                .filter(r -> !BLACK_REC.equals(r.getId().getPath()))
                .toList();

        for (FoodCategory value : FoodCategory.values()) {
            List<Item> items = FoodValuesDefinition.getMatchedItems(value, level).stream()
                    .map(ItemStack::getItem)
                    .toList();
            FOOD_CATEGORY_INGREDIENT_MAP.put(value, items);
        }

        for (CrockPotCookingRecipe crockPotCookingRecipe : recs) {
            for (IRequirement requirement : crockPotCookingRecipe.getRequirements()) {
                if (REQUIREMENT_INGREDIENTY_MAP.containsKey(requirement)) {
                    continue;
                } else if (requirement instanceof RequirementMustContainIngredient requirementMustContainIngredient) {
                    ItemStack[] items = requirementMustContainIngredient.getIngredient().getItems();
                    List<Item> list = Arrays.stream(items).map(ItemStack::getItem).toList();
                    REQUIREMENT_INGREDIENTY_MAP.put(requirementMustContainIngredient, list);
                } else if (requirement instanceof RequirementMustContainIngredientLessThan requirementMustContainIngredientLessThan) {
                    ItemStack[] items = requirementMustContainIngredientLessThan.getIngredient().getItems();
                    List<Item> list = Arrays.stream(items).map(ItemStack::getItem).toList();
                    REQUIREMENT_INGREDIENTY_MAP.put(requirementMustContainIngredientLessThan, list);
                } else {
                    JsonElement json = requirement.toJson();
                    JsonElement category2 = json.getAsJsonObject().get("category");
                    if (category2 == null) {
                        continue;
                    }
                    String category1 = category2.getAsString();
                    FoodCategory category = EnumUtils.getEnum(FoodCategory.class, category1.toUpperCase());
                    REQUIREMENT_FOOD_CATEGORY_MAP.put(requirement, category);
                    List<Item> items1 = FOOD_CATEGORY_INGREDIENT_MAP.get(category);
                    REQUIREMENT_INGREDIENTY_MAP.put(requirement, items1);
                }
            }
        }

        FOOD_CATEGORY_INGREDIENT_MAP.forEach((foodCategory, items) -> {
            HashMap<Item, Float> itemFloatHashMap = new HashMap<>();
            for (Item item : items) {
                FoodValues foodValues = FoodValuesDefinition.getFoodValues(item.getDefaultInstance(), level);
                itemFloatHashMap.put(item, foodValues.get(foodCategory));
            }
            this.validIngres.addAll(items);
            REQUIREMENT_FOOD_VALUE_HASH_MAP.put(foodCategory, new FoodValue(foodCategory, itemFloatHashMap));
        });

        this.recipes = recs.stream()
                .map(r -> ((MKRecipe<CrockPotCookingRecipe>) new MKCrockPotRecipe(r, REQUIREMENT_INGREDIENTY_MAP)))
                .toList();
    }

    @Override
    protected void initFuels() {
        this.fuels = createDefaultFuels();
    }
}
