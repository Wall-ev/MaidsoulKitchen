package com.github.wallev.maidsoulkitchen.handler.base.mkrecipe;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.recipe.cooking.requirement.*;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CrockPotCookingRec extends AbstractCookRec<CrockPotCookingRecipe> {
    protected final List<Pair<RequirementCategoryMax, Set<Item>>> noRequires = new ArrayList<>();
    protected final List<Pair<RequirementCategoryMax, Set<Item>>> maxRequires = new ArrayList<>();
    protected final List<Pair<RequirementCategoryMaxExclusive, Set<Item>>> maxERequires = new ArrayList<>();
    protected final List<Pair<RequirementCategoryMinExclusive, Set<Item>>> anyRequires = new ArrayList<>();
    protected final List<Pair<RequirementCategoryMinExclusive, Set<Item>>> minERequires = new ArrayList<>();
    protected final List<Pair<RequirementCategoryMin, Set<Item>>> minRequires = new ArrayList<>();
    protected final List<Pair<RequirementMustContainIngredient, Set<Item>>> mustRequires = new ArrayList<>();
    protected final List<Pair<RequirementMustContainIngredientLessThan, Set<Item>>> mustLessRequires = new ArrayList<>();

    public CrockPotCookingRec(CrockPotCookingRecipe rec, List<List<Item>> ingres, List<Item> result) {
        super(rec, ingres, result);
    }

    @Override
    protected Set<Item> createValidItems() {
        Set<Item> validItems = Sets.newHashSet();
        for (Pair<RequirementCategoryMax, Set<Item>> maxRequire : maxRequires) {
            validItems.addAll(maxRequire.getSecond());
        }
        for (Pair<RequirementCategoryMaxExclusive, Set<Item>> maxERequire : maxERequires) {
            validItems.addAll(maxERequire.getSecond());
        }
        for (Pair<RequirementCategoryMinExclusive, Set<Item>> anyRequire : anyRequires) {
            validItems.addAll(anyRequire.getSecond());
        }
        for (Pair<RequirementCategoryMinExclusive, Set<Item>> minERequire : minERequires) {
            validItems.addAll(minERequire.getSecond());
        }
        for (Pair<RequirementCategoryMin, Set<Item>> minRequire : minRequires) {
            validItems.addAll(minRequire.getSecond());
        }
        for (Pair<RequirementMustContainIngredient, Set<Item>> mustRequire : mustRequires) {
            validItems.addAll(mustRequire.getSecond());
        }
        for (Pair<RequirementMustContainIngredientLessThan, Set<Item>> mustLessRequire : mustLessRequires) {
            validItems.addAll(mustLessRequire.getSecond());
        }
        for (Pair<RequirementCategoryMax, Set<Item>> noRequire : noRequires) {
            validItems.retainAll(noRequire.getSecond());
        }

        return validItems;
    }

    public void addNoRequirements(Pair<RequirementCategoryMax, Set<Item>> requirementItems) {
        this.noRequires.add(requirementItems);
    }

    public void addMaxRequirements(Pair<RequirementCategoryMax, Set<Item>> requirementItems) {
        this.maxRequires.add(requirementItems);
    }

    public void addMaxERequirements(Pair<RequirementCategoryMaxExclusive, Set<Item>> requirementItems) {
        this.maxERequires.add(requirementItems);
    }

    public void addAnyRequirements(Pair<RequirementCategoryMinExclusive, Set<Item>> requirementItems) {
        this.anyRequires.add(requirementItems);
    }

    public void addMinERequirements(Pair<RequirementCategoryMinExclusive, Set<Item>> requirementItems) {
        this.minERequires.add(requirementItems);
    }

    public void addMinRequirements(Pair<RequirementCategoryMin, Set<Item>> requirementItems) {
        this.minRequires.add(requirementItems);
    }

    public void addMustRequirements(Pair<RequirementMustContainIngredient, Set<Item>> requirementItems) {
        this.mustRequires.add(requirementItems);
    }

    public void addMustLessRequirements(Pair<RequirementMustContainIngredientLessThan, Set<Item>> requirementItems) {
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
}