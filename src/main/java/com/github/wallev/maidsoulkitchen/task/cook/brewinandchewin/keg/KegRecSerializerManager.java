package com.github.wallev.maidsoulkitchen.task.cook.brewinandchewin.keg;

import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.FluidRecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec.MKRecipe;
import com.github.wallev.verhelper.server.item.VItemStack;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
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
import umpaz.brewinandchewin.client.utility.BnCFluidItemDisplays;
import umpaz.brewinandchewin.common.crafting.KegFermentingRecipe;
import umpaz.brewinandchewin.common.crafting.KegPouringRecipe;
import umpaz.brewinandchewin.common.registry.BnCRecipeTypes;

import java.util.*;


public class KegRecSerializerManager extends FluidRecSerializerManager<KegFermentingRecipe> {
    private static final KegRecSerializerManager INSTANCE = new KegRecSerializerManager();

    protected KegRecSerializerManager() {
        super(BnCRecipeTypes.FERMENTING.get());
    }

    public static KegRecSerializerManager getInstance() {
        return INSTANCE;
    }

    @Override
    protected void initFluidRecs(Level level) {
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

        List<KegPouringRecipe> kettlePouringRecipes = level.getRecipeManager().getAllRecipesFor(BnCRecipeTypes.KEG_POURING.get());
        List<KegFermentingRecipe> kettleRecipes = level.getRecipeManager().getAllRecipesFor(BnCRecipeTypes.FERMENTING.get());
        for (KegPouringRecipe kettlePouringRecipe : kettlePouringRecipes) {
            Fluid rawFluid = kettlePouringRecipe.getRawFluid();
            if (rawFluid instanceof EmptyFluid) {
                continue;
            }

            ItemStack itemStack = kettlePouringRecipe.getOutput().copy();
            if (!itemStack.isEmpty()) {
                if (fluidItems1.containsKey(rawFluid)) {
                    List<Pair<ItemStack, Integer>> itemStacks = fluidItems1.getOrDefault(rawFluid, Collections.emptyList());
                    if (itemStacks.stream().noneMatch(itemStack1 -> itemStack1.getFirst().is(itemStack.getItem()))) {
                        itemStacks.add(Pair.of(itemStack, kettlePouringRecipe.getAmount()));
                    }
                } else {
                    fluidItems1.put(rawFluid, Lists.newArrayList(Pair.of(itemStack, kettlePouringRecipe.getAmount())));
                }
            }

            ItemStack container = VItemStack.copyWithCount(kettlePouringRecipe.getContainer(), 1);
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
        this.fluidContainers = fluidContainers1;

        List<MKRecipe<KegFermentingRecipe>> mkRecipes = new ArrayList<>();
        for (KegFermentingRecipe recipe : kettleRecipes) {
            List<ItemStack> inFluids = Collections.emptyList();

            // 输入的流体
            FluidStack fluidIn = recipe.getFluidIngredient();
            if (fluidIn != null && !fluidIn.isEmpty()) {
                if (fluidItems1.keySet().stream().anyMatch(fluid -> fluid.isSame(fluidIn.getFluid()))) {
                    List<ItemStack> fluidItems = new ArrayList<>();
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

                    inFluids = fluidItems;
                } else {
                    ItemStack otherFluidItem = BnCFluidItemDisplays.getFluidItemDisplay(level.registryAccess(), fluidIn);
                    inFluids = List.of(otherFluidItem);
                }
            }

            mkRecipes.add(this.createMKRecipe(recipe, inFluids));
        }
        this.recipes = mkRecipes;
    }

    @Override
    protected FluidRecipeInfoProvider<KegFermentingRecipe> createRecipeInfoProvider() {
        return new KegFermentationRecipeInfoProvider();
    }

    public static class KegFermentationRecipeInfoProvider extends FluidRecipeInfoProvider<KegFermentingRecipe> {

        @Override
        public Fluid getOutputFluid(RecSerializerManager<KegFermentingRecipe> rsm, KegFermentingRecipe rec) {
            return rec.getResultFluid();
        }
    }
}
