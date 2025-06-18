package com.github.wallev.maidsoulkitchen.task.cook.crokckpot.crockpot.rec;

import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemDefinition;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec.MKRecipe;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.recipe.cooking.requirement.*;
import com.sihenzhang.crockpot.util.MathUtils;
import net.minecraft.world.item.Item;

import java.util.*;

public class MKCrockPotRecipe extends MKRecipe<CrockPotCookingRecipe> {
    protected List<Pair<RequirementCategoryMax, Set<Item>>> noRequires = new ArrayList<>();
    protected List<Pair<RequirementCategoryMax, Set<Item>>> maxRequires = new ArrayList<>();
    protected List<Pair<RequirementCategoryMaxExclusive, Set<Item>>> maxERequires = new ArrayList<>();
    protected List<Pair<RequirementCategoryMinExclusive, Set<Item>>> anyRequires = new ArrayList<>();
    protected List<Pair<RequirementCategoryMinExclusive, Set<Item>>> minERequires = new ArrayList<>();
    protected List<Pair<RequirementCategoryMin, Set<Item>>> minRequires = new ArrayList<>();
    protected List<Pair<RequirementMustContainIngredient, Set<Item>>> mustRequires = new ArrayList<>();
    protected List<Pair<RequirementMustContainIngredientLessThan, Set<Item>>> mustLessRequires = new ArrayList<>();

    protected List<List<Pair<IRequirement, Set<Item>>>> needRequires = new ArrayList<>();

    protected List<ItemDefinition> noRequiresItemDefinitions = new ArrayList<>();

    public MKCrockPotRecipe(CrockPotCookingRecipe rec, Map<IRequirement, List<Item>> REQUIREMENT_INGREDIENTY_MAP) {
        super(rec, false, Collections.emptyList(), rec.getResult());
        this.categorizeRequirements(REQUIREMENT_INGREDIENTY_MAP);
        this.validInItems = this.createValidItems();
        this.validInItemDefinitions = createValidItemDefinitionsFromItems(validInItems);
        this.needRequires = this.createNeedRequires();
        this.noRequiresItemDefinitions = this.createInValidItemDefinitionsFromItems();
    }

    private List<ItemDefinition> createInValidItemDefinitionsFromItems() {
        List<ItemDefinition> noRequiresItemDefinitions = new ArrayList<>();
        for (Pair<RequirementCategoryMax, Set<Item>> noRequire : noRequires) {
            Set<Item> itemSet = noRequire.getSecond();
            Set<ItemDefinition> validItemDefinitionsFromItems = createValidItemDefinitionsFromItems(itemSet);
            noRequiresItemDefinitions.addAll(validItemDefinitionsFromItems);
        }
        return noRequiresItemDefinitions;
    }

    private List<List<Pair<IRequirement, Set<Item>>>> createNeedRequires() {
        List<List<Pair<IRequirement, Set<Item>>>> needRequires = new ArrayList<>();

        // 任意
        addRequirement(needRequires, anyRequires);
        // 必须含有
        addRequirement(needRequires, mustRequires);
        // >=
        addRequirement(needRequires, minRequires);
        // >
        addRequirement(needRequires, minERequires);
        // <=
        addRequirement(needRequires, maxRequires);
        // <
        addRequirement(needRequires, maxERequires);

        return needRequires;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <IR extends IRequirement> void addRequirement(List<List<Pair<IRequirement, Set<Item>>>> needRequires, List<Pair<IR, Set<Item>>> requirementItems) {
        if (!requirementItems.isEmpty()) {
            needRequires.add((List) requirementItems);
        }
    }

    /**
     * 计算当前配方所包含的类型
     */
    private void categorizeRequirements(Map<IRequirement, List<Item>> REQUIREMENT_INGREDIENTY_MAP) {
        for (IRequirement requirement : this.rec().getRequirements()) {
            if (requirement instanceof RequirementCategoryMax requirementCategoryMax) {
                if (MathUtils.fuzzyIsZero(requirementCategoryMax.getMax())) {
                    addNoRequirements(Pair.of(requirementCategoryMax, getQuireItemSet(REQUIREMENT_INGREDIENTY_MAP, requirementCategoryMax)));
                } else {
                    addMaxRequirements(Pair.of(requirementCategoryMax, getQuireItemSet(REQUIREMENT_INGREDIENTY_MAP, requirementCategoryMax)));
                }
            } else if (requirement instanceof RequirementCategoryMinExclusive requirementCategoryMinExclusive) {
                if (MathUtils.fuzzyIsZero(requirementCategoryMinExclusive.getMin())) {
                    addAnyRequirements(Pair.of(requirementCategoryMinExclusive, getQuireItemSet(REQUIREMENT_INGREDIENTY_MAP, requirementCategoryMinExclusive)));
                } else {
                    addMinERequirements(Pair.of(requirementCategoryMinExclusive, getQuireItemSet(REQUIREMENT_INGREDIENTY_MAP, requirementCategoryMinExclusive)));
                }
            } else if (requirement instanceof RequirementCategoryMin requirementCategoryMin) {
                addMinRequirements(Pair.of(requirementCategoryMin, getQuireItemSet(REQUIREMENT_INGREDIENTY_MAP, requirementCategoryMin)));
            } else if (requirement instanceof RequirementCategoryMaxExclusive requirementCategoryMaxE) {
                addMaxERequirements(Pair.of(requirementCategoryMaxE, getQuireItemSet(REQUIREMENT_INGREDIENTY_MAP, requirementCategoryMaxE)));
            } else if (requirement instanceof RequirementMustContainIngredient requirementMustContainIngredient) {
                addMustRequirements(Pair.of(requirementMustContainIngredient, getQuireItemSet(REQUIREMENT_INGREDIENTY_MAP, requirementMustContainIngredient)));
            } else if (requirement instanceof RequirementMustContainIngredientLessThan requirementMustContainIngredientLessThan) {
                addMustLessRequirements(Pair.of(requirementMustContainIngredientLessThan, getQuireItemSet(REQUIREMENT_INGREDIENTY_MAP, requirementMustContainIngredientLessThan)));
            }
        }
    }

    protected Set<Item> createValidItems() {
        Set<Item> validItems = Sets.newHashSet();
        for (Pair<RequirementCategoryMinExclusive, Set<Item>> anyRequire : anyRequires) {
            validItems.addAll(anyRequire.getSecond());
        }
        for (Pair<RequirementMustContainIngredient, Set<Item>> mustRequire : mustRequires) {
            validItems.addAll(mustRequire.getSecond());
        }
        for (Pair<RequirementCategoryMax, Set<Item>> maxRequire : maxRequires) {
            validItems.addAll(maxRequire.getSecond());
        }
        for (Pair<RequirementCategoryMaxExclusive, Set<Item>> maxERequire : maxERequires) {
            validItems.addAll(maxERequire.getSecond());
        }
        for (Pair<RequirementCategoryMinExclusive, Set<Item>> minERequire : minERequires) {
            validItems.addAll(minERequire.getSecond());
        }
        for (Pair<RequirementCategoryMin, Set<Item>> minRequire : minRequires) {
            validItems.addAll(minRequire.getSecond());
        }
        for (Pair<RequirementMustContainIngredientLessThan, Set<Item>> mustLessRequire : mustLessRequires) {
            validItems.addAll(mustLessRequire.getSecond());
        }
        for (Pair<RequirementCategoryMax, Set<Item>> noRequire : noRequires) {
            validItems.removeAll(noRequire.getSecond());
        }

        return validItems;
    }

    private Set<Item> getQuireItemSet(Map<IRequirement, List<Item>> REQUIREMENT_INGREDIENTY_MAP, IRequirement requirement) {
        return new HashSet<>(getQuireItems(REQUIREMENT_INGREDIENTY_MAP, requirement));
    }

    private List<Item> getQuireItems(Map<IRequirement, List<Item>> REQUIREMENT_INGREDIENTY_MAP, IRequirement requirement) {
        return REQUIREMENT_INGREDIENTY_MAP.get(requirement);
    }

    private void addNoRequirements(Pair<RequirementCategoryMax, Set<Item>> requirementItems) {
        this.noRequires.add(requirementItems);
    }

    private void addMaxRequirements(Pair<RequirementCategoryMax, Set<Item>> requirementItems) {
        this.maxRequires.add(requirementItems);
    }

    private void addMaxERequirements(Pair<RequirementCategoryMaxExclusive, Set<Item>> requirementItems) {
        this.maxERequires.add(requirementItems);
    }

    private void addAnyRequirements(Pair<RequirementCategoryMinExclusive, Set<Item>> requirementItems) {
        this.anyRequires.add(requirementItems);
    }

    private void addMinERequirements(Pair<RequirementCategoryMinExclusive, Set<Item>> requirementItems) {
        this.minERequires.add(requirementItems);
    }

    private void addMinRequirements(Pair<RequirementCategoryMin, Set<Item>> requirementItems) {
        this.minRequires.add(requirementItems);
    }

    private void addMustRequirements(Pair<RequirementMustContainIngredient, Set<Item>> requirementItems) {
        this.mustRequires.add(requirementItems);
    }

    private void addMustLessRequirements(Pair<RequirementMustContainIngredientLessThan, Set<Item>> requirementItems) {
        this.mustLessRequires.add(requirementItems);
    }


    public List<Pair<RequirementCategoryMax, Set<Item>>> getNoRequires() {
        return noRequires;
    }

    public List<Pair<RequirementCategoryMax, Set<Item>>> getMaxRequires() {
        return maxRequires;
    }

    public List<Pair<RequirementCategoryMaxExclusive, Set<Item>>> getMaxERequires() {
        return maxERequires;
    }

    public List<Pair<RequirementCategoryMinExclusive, Set<Item>>> getAnyRequires() {
        return anyRequires;
    }

    public List<Pair<RequirementCategoryMinExclusive, Set<Item>>> getMinERequires() {
        return minERequires;
    }

    public List<Pair<RequirementCategoryMin, Set<Item>>> getMinRequires() {
        return minRequires;
    }

    public List<Pair<RequirementMustContainIngredient, Set<Item>>> getMustRequires() {
        return mustRequires;
    }

    public List<Pair<RequirementMustContainIngredientLessThan, Set<Item>>> getMustLessRequires() {
        return mustLessRequires;
    }

    public List<List<Pair<IRequirement, Set<Item>>>> getNeedRequires() {
        return needRequires;
    }

    public List<ItemDefinition> getNoRequiresItemDefinitions() {
        return noRequiresItemDefinitions;
    }
}
