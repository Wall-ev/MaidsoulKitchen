package com.github.wallev.maidsoulkitchenlegacy.task.cook.youkaishomecoming.ferment;

import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ingredient.RecIngredient;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.FluidRecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec.MKRecipe;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import dev.xkmc.youkaishomecoming.content.item.fluid.IYHSake;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeBottleItem;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeFluid;
import dev.xkmc.youkaishomecoming.content.pot.ferment.FermentationRecipe;
import dev.xkmc.youkaishomecoming.content.pot.ferment.SimpleFermentationRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.EmptyFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;


public class FermentationRecSerializerLegacyManager extends FluidRecSerializerManager<FermentationRecipe<?>> {
    private static final FermentationRecSerializerLegacyManager INSTANCE = new FermentationRecSerializerLegacyManager();

    protected FermentationRecSerializerLegacyManager() {
        super(YHBlocks.FERMENT_RT.get());
    }

    public static FermentationRecSerializerLegacyManager getInstance() {
        return INSTANCE;
    }

    @Override
    protected FluidRecipeInfoProvider<FermentationRecipe<?>> createRecipeInfoProvider() {
        return new FermentationRecipeMKRecipe();
    }

    @Override
    protected void initFluidRecs(Level level) {
        List<FermentationRecipe<?>> recipes = this.getRecsFromRm(level);

        Map<Fluid, List<Pair<ItemStack, Integer>>> fluidItems1 = new HashMap<>();
        Map<Fluid, List<ItemStack>> fluidContainers1 = new HashMap<>();
        for (Fluid fluid : ForgeRegistries.FLUIDS.getValues()) {
            if (fluid instanceof EmptyFluid) continue;

            ItemStack container = fluid.getBucket().getDefaultInstance().getCraftingRemainingItem();
            if (!container.isEmpty()) {
                if (fluidContainers1.containsKey(fluid)) {
                    List<ItemStack> itemStacks = fluidContainers1.getOrDefault(fluid, Collections.emptyList());
                    if (itemStacks.stream().noneMatch(itemStack1 -> itemStack1.is(container.getItem()))) {
                        itemStacks.add(container);
                    }
                } else {
                    fluidContainers1.put(fluid, Lists.newArrayList(container));
                }
            }
        }
        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            if (item instanceof SakeBottleItem sakeBottleItem) {
                SakeFluid fluid = sakeBottleItem.getFluid();

                IYHSake iyhSake = fluid.type;
                Fluid rawFluid = fluid.getSource();

                if (fluidItems1.containsKey(rawFluid)) {
                    List<Pair<ItemStack, Integer>> fluidItems2 = fluidItems1.getOrDefault(rawFluid, Collections.emptyList());
                    if (fluidItems2.stream().noneMatch(pair1 -> pair1.getFirst().is(item))) {
                        fluidItems1.get(rawFluid).add(Pair.of(item.getDefaultInstance(), iyhSake.amount()));
                    }
                } else {
                    fluidItems1.put(rawFluid, Lists.newArrayList(Pair.of(item.getDefaultInstance(), iyhSake.amount())));
                }

                ItemStack container = iyhSake.getContainer().getDefaultInstance();
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
                continue;
            }

            ItemStack defaultInstance = item.getDefaultInstance().copy();
            IFluidHandlerItem iFluidHandlerItem = defaultInstance.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
            if (iFluidHandlerItem != null && iFluidHandlerItem instanceof FluidBucketWrapper fluidBucketWrapper) {
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
        this.fluidContainers = fluidContainers1;

        List<MKRecipe<FermentationRecipe<?>>> mkRecipes = new ArrayList<>();
        for (FermentationRecipe<?> recipe : recipes) {
            SimpleFermentationRecipe fermentationRecipe = (SimpleFermentationRecipe) recipe;

            // 输入的流体
            FluidStack fluidIn = fermentationRecipe.inputFluid;
            if (fluidIn != null && !fluidIn.isEmpty()) {
                List<ItemStack> fluidItems = new ArrayList<>();
                if (fluidItems1.keySet().stream().anyMatch(fluid -> fluid.isSame(fluidIn.getFluid()))) {
                    fluidItems1.forEach((fluid, itemStacks) -> {
                        if (fluid.isSame(fluidIn.getFluid())) {
                            for (Pair<ItemStack, Integer> fluidStackPair : itemStacks) {
                                ItemStack outputFluidItem = fluidStackPair.getFirst().copy();
                                int amount = fluidStackPair.getSecond();
                                int amountTotal = fluidIn.getAmount();
                                outputFluidItem.setCount(Math.max(1, amountTotal / amount));

                                if (fluidItems.stream().noneMatch(itemStack -> itemStack.is(outputFluidItem.getItem()) && itemStack.getCount() == outputFluidItem.getCount())) {
                                    fluidItems.add(outputFluidItem);
                                }

                            }
                        }
                    });
                }

                mkRecipes.add(this.createMKRecipe(recipe, fluidItems));
            } else {
                mkRecipes.add(this.createMKRecipe(recipe));
            }
        }
        this.recipes = mkRecipes;
    }

    public static class FermentationRecipeMKRecipe extends FluidRecipeInfoProvider<FermentationRecipe<?>> {

        @Override
        public List<RecIngredient> getIngredients(RecSerializerManager<FermentationRecipe<?>> rsm, FermentationRecipe<?> rec) {
            SimpleFermentationRecipe sFermentationRecipe = (SimpleFermentationRecipe) rec;
            return RecIngredient.from(sFermentationRecipe.ingredients);
        }

        @Override
        public ItemStack getOutput(RecSerializerManager<FermentationRecipe<?>> rsm, FermentationRecipe<?> rec) {
            SimpleFermentationRecipe sFermentationRecipe = (SimpleFermentationRecipe) rec;
            FluidStack outputFluid = sFermentationRecipe.outputFluid;
            Fluid fluid = outputFluid.getFluid();
            if (fluid instanceof SakeFluid sakeFluid) {
                return sakeFluid.type.asStack(1);
            }
            return ItemStack.EMPTY;
        }

        @Override
        public Fluid getOutputFluid(RecSerializerManager<FermentationRecipe<?>> rsm, FermentationRecipe<?> rec) {
            SimpleFermentationRecipe sFermentationRecipe = (SimpleFermentationRecipe) rec;
            FluidStack outputFluid = sFermentationRecipe.outputFluid;
            return outputFluid.getFluid();
        }
    }
}
