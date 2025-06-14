package com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec;

import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ItemDefinition;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec.MKRecipe;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.EmptyFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public abstract class FluidRecSerializerManager<R extends Recipe<? extends Container>> extends RecSerializerManager<R> {
    // 流体容器
    protected Map<Fluid, List<ItemStack>> fluidContainers = new HashMap<>();

    protected FluidRecSerializerManager(RecipeType<R> recipeType) {
        super(recipeType);
    }

    public List<ItemStack> fluidContainer(Fluid fluid) {
        return fluidContainers.getOrDefault(fluid, Collections.emptyList());
    }

    @Override
    protected abstract FluidRecipeInfoProvider<R> createRecipeInfoProvider();

    protected void initRecs(Level level) {
        this.initFluidRecs(level);
    }

    protected abstract void initFluidRecs(Level level);

    @Override
    protected MaidRec recProcess(MKRecipe<R> r, Map<ItemDefinition, Long> available, List<Item> invIngredient, boolean[] single, Map<Item, ItemAmount> itemTimes) {
        ItemStack fluidItemAmount = processRecFluids(available, invIngredient, single, itemTimes, r.inFluids());
        if (fluidItemAmount == null) {
            return MaidRec.EMPTY;
        }

        boolean processRecIngres = super.processRecIngres(r, available, invIngredient, single, itemTimes);
        if (!processRecIngres) {
            return MaidRec.EMPTY;
        }

        ItemStack result = r.output();
        List<MaidItem> maidItems = new ArrayList<>();
        int recAmount = getMaxAmount(available, single, itemTimes);
        Item fluidItem = fluidItemAmount.getItem();
        for (Item item : invIngredient) {
            if (fluidItem == item) {
                continue;
            }
            int minAmount = itemTimes.get(item).getAmount();
            int count = recAmount * minAmount;

            maidItems.add(new MaidItem(item, count));
            ItemDefinition itemDefinition = ItemDefinition.of(item);
            available.put(itemDefinition, available.get(itemDefinition) - count);
        }

        return new MaidRec(r.rec(), result, recAmount, maidItems, new MaidItem(fluidItem, fluidItemAmount.getCount() * recAmount));
    }

    protected ItemStack processRecFluids(Map<ItemDefinition, Long> available, List<Item> invIngredient, boolean[] single, Map<Item, ItemAmount> itemTimes, List<ItemStack> inFluids) {
        for (ItemStack inFluid : inFluids) {
            for (Map.Entry<ItemDefinition, Long> entry : available.entrySet()) {
                ItemDefinition key = entry.getKey();
                Item item = key.item();

                ItemStack stack = key.stack();
                if (inFluid.is(stack.getItem()) && available.get(item) >= inFluid.getCount()) {
                    invIngredient.add(item);

                    int amount;
                    ItemAmount itemAmount;
                    if (stack.getMaxStackSize() == 1) {
                        single[0] = true;
                        itemAmount = new ItemAmount(inFluid.getCount(), 1);
                        itemTimes.put(item, itemAmount);
                        amount = itemAmount.needCount();
                    } else {
                        itemAmount = itemTimes.computeIfAbsent(item, k -> {
                            ItemAmount itemAmount1 = new ItemAmount(inFluid.getCount(), 1);
                            return itemAmount1;
                        });
                        amount = itemAmount.needCount();
                    }

                    if (entry.getValue() < amount) {
                        return null;
                    } else {
                        return inFluid;
                    }

                }
            }
        }
        return null;
    }

    protected void queryItemFluids(Map<Fluid, List<Pair<ItemStack, Integer>>> fluidItems1, Map<Fluid, List<ItemStack>> fluidContainers1) {
        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            if (modFluidItem(item, fluidItems1, fluidContainers1)) continue;

            ItemStack defaultInstance = item.getDefaultInstance().copy();
            IFluidHandlerItem iFluidHandlerItem = defaultInstance.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
            if (iFluidHandlerItem instanceof FluidBucketWrapper fluidBucketWrapper) {
                FluidStack fluidStack = fluidBucketWrapper.getFluid();
                Fluid rawFluid = fluidStack.getRawFluid();

                if (!fluidStack.isEmpty() && !(rawFluid instanceof EmptyFluid)) {

                    if (fluidItems1.containsKey(rawFluid)) {
                        List<Pair<ItemStack, Integer>> fluidItems2 = fluidItems1.getOrDefault(rawFluid, Collections.emptyList());
                        if (fluidItems2.stream().noneMatch(pair1 -> pair1.getFirst().is(defaultInstance.getItem()))) {
                            fluidItems1.get(rawFluid).add(Pair.of(defaultInstance, fluidStack.getAmount()));
                        }
                    } else {
                        fluidItems1.put(rawFluid, Lists.newArrayList(Pair.of(defaultInstance, fluidStack.getAmount())));
                    }

                    ItemStack container = fluidBucketWrapper.getContainer().getCraftingRemainingItem();
                    if (!container.isEmpty()) {
                        if (fluidContainers1.containsKey(rawFluid)) {
                            List<ItemStack> itemStacks = fluidContainers1.getOrDefault(rawFluid, Collections.emptyList());
                            if (itemStacks.stream().noneMatch(itemStack1 -> itemStack1.is(container.getItem()))) {
                                itemStacks.add(container);
                            }
                        } else {
                            fluidContainers1.put(rawFluid, Lists.newArrayList(container));
                        }
                    }
                }
            }
        }
    }

    protected boolean modFluidItem(Item item, Map<Fluid, List<Pair<ItemStack, Integer>>> fluidItems1, Map<Fluid, List<ItemStack>> fluidContainers1) {
//        if (item instanceof SakeBottleItem sakeBottleItem) {
//            YHFluid fluid = sakeBottleItem.getFluid();
//
//            IYHFluidHolder iyhSake = fluid.type;
//            Fluid rawFluid = fluid.getSource();
//
//            if (fluidItems1.containsKey(rawFluid)) {
//                List<Pair<ItemStack, Integer>> fluidItems2 = fluidItems1.getOrDefault(rawFluid, Collections.emptyList());
//                if (fluidItems2.stream().noneMatch(pair1 -> pair1.getFirst().is(item))) {
//                    fluidItems1.get(rawFluid).add(Pair.of(item.getDefaultInstance(), iyhSake.amount()));
//                }
//            } else {
//                fluidItems1.put(rawFluid, Lists.newArrayList(Pair.of(item.getDefaultInstance(), iyhSake.amount())));
//            }
//
//            ItemStack container = iyhSake.getContainer().getDefaultInstance();
//            if (!container.isEmpty()) {
//                if (fluidContainers1.containsKey(rawFluid)) {
//                    List<ItemStack> itemStacks = fluidContainers1.getOrDefault(rawFluid, Collections.emptyList());
//                    if (itemStacks.stream().noneMatch(itemStack1 -> itemStack1.is(container.getItem()))) {
//                        itemStacks.add(container);
//                    }
//                } else {
//                    fluidContainers1.put(rawFluid, Lists.newArrayList(container));
//                }
//            }
//            return true;
//        }
        return false;
    }

    protected void queryRegistryFluids(Map<Fluid, List<ItemStack>> fluidContainers0) {
        for (Fluid fluid : ForgeRegistries.FLUIDS.getValues()) {
            if (fluid instanceof EmptyFluid) {
                continue;
            }

            ItemStack container = fluid.getBucket().getDefaultInstance().getCraftingRemainingItem();
            if (!container.isEmpty()) {
                if (fluidContainers0.containsKey(fluid)) {
                    List<ItemStack> itemStacks = fluidContainers0.getOrDefault(fluid, Collections.emptyList());
                    if (isItem(itemStacks, container)) {
                        itemStacks.add(container);
                    }
                } else {
                    fluidContainers0.put(fluid, Lists.newArrayList(container));
                }
            }
        }
    }

    public abstract static class FluidRecipeInfoProvider<R extends Recipe<? extends Container>> extends RecipeInfoProvider<R> {
        @Override
        public ItemStack getContainer(RecSerializerManager<R> rsm, R rec) {
            Fluid outputFluid = this.getOutputFluid(rsm, rec);
            FluidRecSerializerManager<R> rsm1 = rsm.to();
            List<ItemStack> stacks = rsm1.fluidContainers.getOrDefault(outputFluid, Collections.emptyList());
            return stacks.isEmpty() ? ItemStack.EMPTY : stacks.get(0);
        }

        public abstract Fluid getOutputFluid(RecSerializerManager<R> rsm, R rec);
    }
}
