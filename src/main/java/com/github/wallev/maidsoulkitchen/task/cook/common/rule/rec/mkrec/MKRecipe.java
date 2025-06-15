package com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec;

import com.github.wallev.maidsoulkitchen.foundation.utility.RecIngredient;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ItemDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MKRecipe<R extends Recipe<? extends Container>> {
    protected final R rec;
    protected final boolean single;

    private final RecIngredient tool;

    protected final List<ItemStack> inFluids;
    protected final List<RecIngredient> inItems;
    protected final ItemStack output;
    protected final ItemStack container;
    protected final ResourceLocation id;

    protected Set<Item> validInItems;
    protected Set<Item> validInFluids;

    protected Set<ItemDefinition> validInItemDefinitions;
    protected Set<ItemDefinition> validInFluidDefinitions;

    public MKRecipe(R rec, boolean single, RecIngredient tool, List<ItemStack> inFluids, List<RecIngredient> inItems, ItemStack output, ItemStack container, Set<Item> validInItems, Set<Item> validInFluids) {
        this.rec = rec;
        this.single = single;
        this.tool = tool;
        this.inFluids = inFluids;
        this.inItems = inItems;
        this.output = output;
        this.container = container;
        this.id = rec.getId();
        this.validInItems = validInItems;
        this.validInFluids = validInFluids;
        this.validInItemDefinitions = createValidItemDefinitionsFromItems(validInItems);
        this.validInFluidDefinitions = createValidItemDefinitionsFromItems(validInFluids);
    }

    public MKRecipe(R rec, boolean single, RecIngredient tool, List<ItemStack> inFluids, List<RecIngredient> inItems, ItemStack output, ItemStack container) {
        this.rec = rec;
        this.single = single;
        this.tool = tool;
        this.inFluids = inFluids;
        this.inItems = inItems;
        this.output = output;
        this.container = container;
        this.id = rec.getId();
        this.validInItems = createValidItemsFromIngredients(inItems);
        this.validInFluids = createValidItemsFromItemStacks(inFluids);
        this.validInItemDefinitions = createValidItemDefinitionsFromIngredients(inItems);
        this.validInFluidDefinitions = createValidItemDefinitionsFromItemStacks(inFluids);
    }

    public MKRecipe(R rec, boolean single, RecIngredient tool, List<RecIngredient> inItems, ItemStack output, ItemStack container) {
        this(rec, single, tool, List.of(), inItems, output, container);
    }

    public MKRecipe(R rec, boolean single, RecIngredient tool, List<RecIngredient> inItems, ItemStack output) {
        this(rec, single, tool, List.of(), inItems, output, ItemStack.EMPTY);
    }

    public MKRecipe(R rec, boolean single, List<ItemStack> inFluids, List<RecIngredient> inItems, ItemStack output, ItemStack container) {
        this(rec, single, RecIngredient.EMPTY, inFluids, inItems, output, container, createValidItemsFromIngredients(inItems), createValidItemsFromItemStacks(inFluids));
    }

    public MKRecipe(R rec, boolean single, List<ItemStack> inFluids, List<RecIngredient> inItems, ItemStack output) {
        this(rec, single, inFluids, inItems, output, ItemStack.EMPTY);
    }

    public MKRecipe(R rec, boolean single, Set<Item> validInItems, List<RecIngredient> inItems, ItemStack output) {
        this(rec, single, RecIngredient.EMPTY, List.of(), inItems, output, ItemStack.EMPTY, validInItems, Set.of());
    }

    public MKRecipe(R rec, boolean single, List<RecIngredient> inItems, ItemStack output) {
        this(rec, single, List.of(), inItems, output, ItemStack.EMPTY);
    }

    public MKRecipe(R rec, boolean single, List<RecIngredient> inItems, ItemStack output, ItemStack container) {
        this(rec, single, List.of(), inItems, output, container);
    }

    public static Set<Item> createValidItemsFromIngredients(List<RecIngredient> items) {
        return items.stream()
                .flatMap(RecIngredient -> Arrays.stream(RecIngredient.ingredient.getItems()))
                .map(ItemStack::getItem)
                .collect(Collectors.toSet());
    }

    public static Set<Item> createValidItemsFromItemStacks(List<ItemStack> items) {
        return items.stream()
                .map(ItemStack::getItem)
                .collect(Collectors.toSet());
    }

    public static Set<ItemDefinition> createValidItemDefinitionsFromIngredients(List<RecIngredient> items) {
        return items.stream()
                .flatMap(RecIngredient -> Arrays.stream(RecIngredient.ingredient.getItems()))
                .map(ItemDefinition::of)
                .collect(Collectors.toSet());
    }

    public static Set<ItemDefinition> createValidItemDefinitionsFromItemStacks(List<ItemStack> items) {
        return items.stream()
                .map(ItemDefinition::of)
                .collect(Collectors.toSet());
    }

    public static Set<ItemDefinition> createValidItemDefinitionsFromItems(Set<Item> items) {
        return items.stream()
                .map(ItemDefinition::of)
                .collect(Collectors.toSet());
    }

    public RecIngredient tool() {
        return tool;
    }

    public List<ItemStack> inFluids() {
        return inFluids;
    }

    public List<RecIngredient> inItems() {
        return inItems;
    }

    public ItemStack output() {
        return output;
    }

    public ItemStack container() {
        return container;
    }

    public Set<Item> validInItems() {
        return validInItems;
    }

    public Set<Item> validInFluids() {
        return validInFluids;
    }

    public Set<ItemDefinition> validInItemDefinitions() {
        return validInItemDefinitions;
    }

    public Set<ItemDefinition> validInFluidDefinitions() {
        return validInFluidDefinitions;
    }

    public R rec() {
        return rec;
    }

    public boolean isSingle() {
        return single;
    }

    public ResourceLocation id() {
        return id;
    }

    public String idStr() {
        return id().toString();
    }
}
