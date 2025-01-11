package com.github.wallev.maidsoulkitchen.handler.serializer.crockpot.recipe;

import com.github.wallev.maidsoulkitchen.handler.base.mkrecipe.CrockPotCookingRec;
import com.github.wallev.maidsoulkitchen.handler.base.recipe.AbstractCookRecSerializer;
import com.github.wallev.maidsoulkitchen.task.cook.v1.crokckpot.TaskCrockPot;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.recipe.CrockPotRecipes;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.recipe.cooking.requirement.*;
import com.sihenzhang.crockpot.util.MathUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.EnumUtils;

import java.util.*;

public class CrockPotCookingPotRecSerializer extends AbstractCookRecSerializer<CrockPotCookingRecipe> {
    private static final Map<CrockPotCookingRecipe, TaskCrockPot.MaidRec<CrockPotCookingRecipe>> RECS = new HashMap<>();
    private static final Map<FoodCategory, List<Item>> FOOD_CATEGORY_INGREDIENT_MAP = new HashMap<>();
    private static final Map<IRequirement, FoodCategory> REQUIREMENT_FOOD_CATEGORY_MAP = new HashMap<>();
    private static final Map<IRequirement, List<Item>> REQUIREMENT_INGREDIENTY_MAP = new HashMap<>();
    private static final Map<FoodCategory, TaskCrockPot.FoodValue> REQUIREMENT_FOOD_VALUE_HASH_MAP = new HashMap<>();

    public CrockPotCookingPotRecSerializer() {
        super(CrockPotRecipes.CROCK_POT_COOKING_RECIPE_TYPE.get());
    }

//    @todo optimize
    @Override
    protected void initialize(Level level) {
        this.initRecipes(level);

        for (FoodCategory value : FoodCategory.values()) {
            List<Item> items = FoodValuesDefinition.getMatchedItems(value, level).stream().map(ItemStack::getItem).toList();
            FOOD_CATEGORY_INGREDIENT_MAP.put(value, items);
        }

        for (CrockPotCookingRecipe crockPotCookingRecipe : this.cookRecs) {

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

            RECS.put(crockPotCookingRecipe, new TaskCrockPot.MaidRec<>(crockPotCookingRecipe, lists, nonNullList, crockPotCookingRecipe.getResult()));
        }

        FOOD_CATEGORY_INGREDIENT_MAP.forEach((foodCategory, items) -> {
            HashMap<Item, Float> itemFloatHashMap = new HashMap<>();
            for (Item item : items) {
                FoodValues foodValues = FoodValuesDefinition.getFoodValues(item.getDefaultInstance(), level);
                itemFloatHashMap.put(item, foodValues.get(foodCategory));
            }
            this.validIngres.addAll(items);
            REQUIREMENT_FOOD_VALUE_HASH_MAP.put(foodCategory, new TaskCrockPot.FoodValue(foodCategory, itemFloatHashMap));
        });


        for (CrockPotCookingRecipe rec : this.cookRecs) {
            List<Item> resultItem = Lists.newArrayList(getResultItem(rec, level).getItem());
            CrockPotCookingRec crockPotCookingRec = new CrockPotCookingRec(rec, Collections.emptyList(), resultItem);
            this.categorizeRequirements(crockPotCookingRec);
            this.cookRecData.put(rec, crockPotCookingRec);
        }
    }


    /**
     * 计算当前配方所包含的类型
     */
    private void categorizeRequirements(CrockPotCookingRec crockPotCookingRec) {
        for (IRequirement requirement : crockPotCookingRec.getRec().getRequirements()) {
            if (requirement instanceof RequirementCategoryMax requirementCategoryMax) {
                if (MathUtils.fuzzyIsZero(requirementCategoryMax.getMax())) {
                    crockPotCookingRec.addNoRequirements(Pair.of(requirementCategoryMax, getQuireItemSet(requirementCategoryMax)));
                } else {
                    crockPotCookingRec.addMaxRequirements(Pair.of(requirementCategoryMax, getQuireItemSet(requirementCategoryMax)));
                }
            } else if (requirement instanceof RequirementCategoryMinExclusive requirementCategoryMinExclusive) {
                if (MathUtils.fuzzyIsZero(requirementCategoryMinExclusive.getMin())) {
                    crockPotCookingRec.addAnyRequirements(Pair.of(requirementCategoryMinExclusive, getQuireItemSet(requirementCategoryMinExclusive)));
                } else {
                    crockPotCookingRec.addMinERequirements(Pair.of(requirementCategoryMinExclusive, getQuireItemSet(requirementCategoryMinExclusive)));
                }
            } else if (requirement instanceof RequirementCategoryMin requirementCategoryMin) {
                crockPotCookingRec.addMinRequirements(Pair.of(requirementCategoryMin, getQuireItemSet(requirementCategoryMin)));
            } else if (requirement instanceof RequirementCategoryMaxExclusive requirementCategoryMaxE) {
                crockPotCookingRec.addMaxERequirements(Pair.of(requirementCategoryMaxE, getQuireItemSet(requirementCategoryMaxE)));
            } else if (requirement instanceof RequirementMustContainIngredient requirementMustContainIngredient) {
                crockPotCookingRec.addMustRequirements(Pair.of(requirementMustContainIngredient, getQuireItemSet(requirementMustContainIngredient)));
            } else if (requirement instanceof RequirementMustContainIngredientLessThan requirementMustContainIngredientLessThan) {
                crockPotCookingRec.addMustLessRequirements(Pair.of(requirementMustContainIngredientLessThan, getQuireItemSet(requirementMustContainIngredientLessThan)));
            }
        }
    }

    private static Set<Item> getQuireItemSet(IRequirement requirement) {
        return new HashSet<>(getQuireItems(requirement));
    }

    private static List<Item> getQuireItems(IRequirement requirement) {
        return REQUIREMENT_INGREDIENTY_MAP.get(requirement);
    }

    /**
     * 获取对应配方类型的所有配方
     * 这应该和initialize一起使用
     *
     * @param level Level
     * @return 对应的配方类型的所有配方
     */
    @Override
    protected List<CrockPotCookingRecipe> getRecipes(Level level) {
        return super.getRecipes(level).stream()
                .filter(crockPotCookingRecipe -> !crockPotCookingRecipe.getId().getPath().equals("crock_pot_cooking/wet_goop"))
                .toList();
    }

    @Override
    public void clear() {
        super.clear();
        FOOD_CATEGORY_INGREDIENT_MAP.clear();
        REQUIREMENT_FOOD_CATEGORY_MAP.clear();
    }
}
