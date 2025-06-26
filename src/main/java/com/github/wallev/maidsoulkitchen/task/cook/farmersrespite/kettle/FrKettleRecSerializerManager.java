package com.github.wallev.maidsoulkitchen.task.cook.farmersrespite.kettle;

import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ingredient.RecIngredient;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.FluidRecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec.MKRecipe;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
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
import umpaz.farmersrespite.common.crafting.KettlePouringRecipe;
import umpaz.farmersrespite.common.crafting.KettleRecipe;
import umpaz.farmersrespite.common.registry.FRRecipeTypes;

import java.util.*;


@TaskClassAnalyzer(TaskInfo.FR_KETTLE)
public class FrKettleRecSerializerManager extends FluidRecSerializerManager<KettleRecipe> {
    private static final FrKettleRecSerializerManager INSTANCE = new FrKettleRecSerializerManager();

    protected FrKettleRecSerializerManager() {
        super(FRRecipeTypes.BREWING.get());
    }

    public static FrKettleRecSerializerManager getInstance() {
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

        List<KettlePouringRecipe> kettlePouringRecipes = level.getRecipeManager().getAllRecipesFor(FRRecipeTypes.KETTLE_POURING.get());
        List<KettleRecipe> kettleRecipes = level.getRecipeManager().getAllRecipesFor(FRRecipeTypes.BREWING.get());
        for (KettlePouringRecipe kettlePouringRecipe : kettlePouringRecipes) {
            Fluid rawFluid = kettlePouringRecipe.getFluid();
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
        fluidContainers = fluidContainers1;

        List<MKRecipe<KettleRecipe>> mkRecipes = new ArrayList<>();
        for (KettleRecipe recipe : kettleRecipes) {
            List<ItemStack> inFluids = Collections.emptyList();
            ItemStack output = ItemStack.EMPTY;

            // 输入的流体
            FluidStack fluidIn = recipe.getFluidIn();
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
                }
            }

            // 输出的流体容器
            FluidStack fluidOut = recipe.getFluidOut();
            if (fluidOut != null && !fluidOut.isEmpty()) {
                List<KettlePouringRecipe> matchKegPouringRecipes = kettlePouringRecipes.stream()
                        .filter(pouringRecipe -> pouringRecipe.getFluid().isSame(fluidOut.getFluid()))
                        .toList();

                if (!matchKegPouringRecipes.isEmpty()) {
                    output = matchKegPouringRecipes.get(0).getOutput();
                }
            }

            mkRecipes.add(new MKRecipe<>(recipe, true, inFluids, RecIngredient.from(recipe.getIngredients()), output, recipeInfoProvider.getContainer(this, recipe)));
        }
        this.recipes = mkRecipes;
    }

    @Override
    protected FluidRecipeInfoProvider<KettleRecipe> createRecipeInfoProvider() {
        return new KettleRecipeInfoProvider();
    }

    public static class KettleRecipeInfoProvider extends FluidRecipeInfoProvider<KettleRecipe> {

        @Override
        public Fluid getOutputFluid(RecSerializerManager<KettleRecipe> rsm, KettleRecipe rec) {
            return rec.getFluidOut().getFluid();
        }
    }
}
