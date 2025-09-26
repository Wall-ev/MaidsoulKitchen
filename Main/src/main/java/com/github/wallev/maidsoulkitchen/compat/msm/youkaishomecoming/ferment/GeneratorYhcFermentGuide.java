package com.github.wallev.maidsoulkitchen.compat.msm.youkaishomecoming.ferment;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.ICookingRecipeGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.custom.SneakCommonUseAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.TargetUtil;
import com.github.wallev.maidsoulkitchen.compat.msm.youkaishomecoming.base.IYhcRecipe;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import dev.xkmc.youkaishomecoming.content.item.fluid.IYHFluidHolder;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeBottleItem;
import dev.xkmc.youkaishomecoming.content.item.fluid.YHFluid;
import dev.xkmc.youkaishomecoming.content.pot.ferment.FermentationRecipe;
import dev.xkmc.youkaishomecoming.content.pot.ferment.FermentationTankBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.ferment.SimpleFermentationRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.EmptyFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOptionSet;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.generator.cache.RecipeIngredientCache;

import java.util.*;

@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_YHC_FERMENT)
public class GeneratorYhcFermentGuide implements ICookingRecipeGuideGenerator<FermentationRecipe<?>>, IYhcRecipe<SimpleFermentationRecipe> {
    private final Map<FermentationRecipe<?>, Ingredient> inFluidItems = new HashMap<>();
    private final Map<Fluid, List<ItemStack>> fluidContainers = new HashMap<>();
    private final Map<Fluid, List<Pair<ItemStack, Integer>>> fluidItems = new HashMap<>();

    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return true;
    }

    @Override
    @NotNull
    public ResourceLocation getType() {
        return VResourceLocation.createTypeMod(YHBlocks.FERMENT_RT.getId());
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return null;
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof FermentationTankBlockEntity;
    }

    @Override
    public RecipeType<FermentationRecipe<?>> getRecipeType() {
        return YHBlocks.FERMENT_RT.get();
    }

    @Override
    public void generateSteps(BlockPos pos, Level level, FermentationRecipe<?> recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        CraftGuideOperator2.forEachSingleItem(realItems, craftGuide::addItemUse);
        craftGuide.addEmptyUse();
        int fermentationTime = recipe.getFermentationTime();
        craftGuide.addIdle(fermentationTime + 10);
        if (needContainer) {
            CraftGuideOperator2.forEachSingleItem(containers, itemStack -> {
                craftGuide.addItemUse(itemStack, outputs.get(0).copyWithCount(1));
            });
        } else {
            craftGuide.addStep(new CraftGuideStepData(
                    TargetUtil.makeTargetVirtualNoSide(pos),
                    List.of(),
                    List.of(),
                    SneakCommonUseAction.TYPE,
                    ActionOptionSet.with(SneakCommonUseAction.SNEAK, true)
            ));
            craftGuide.addItemPickup(outputs);
//            for (ItemStack output : outputs) {
//                for (int i = 0; i < output.getCount(); i++) {
//                    craftGuide.addEmptyUse(output.copyWithCount(1));
//                }
//            }
        }

    }

    @Override
    public void onCache(RecipeManager manager) {
        this.clear();
        this.buildFluidItems(manager);
    }

    private void clear() {
        this.fluidContainers.clear();
        this.fluidItems.clear();
    }

    public void buildFluidItems(RecipeManager manager) {

        Map<Fluid, List<ItemStack>> fluidContainers = new HashMap<>();
        // 流体容器
        for (Fluid fluid : ForgeRegistries.FLUIDS.getValues()) {
            if (fluid instanceof EmptyFluid)
                continue;

            ItemStack container = fluid.getBucket().getDefaultInstance().getCraftingRemainingItem();
            if (container.isEmpty())
                continue;

            if (fluidContainers.containsKey(fluid)) {
                List<ItemStack> itemStacks = fluidContainers.getOrDefault(fluid, Collections.emptyList());
                if (itemStacks.stream().noneMatch(itemStack1 -> itemStack1.is(container.getItem()))) {
                    itemStacks.add(container);
                }
            } else {
                fluidContainers.put(fluid, Lists.newArrayList(container));
            }
        }

        // 妖归流体物品和流体容器
        Map<Fluid, List<Pair<ItemStack, Integer>>> fluidItems = new HashMap<>();
        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            if (item instanceof SakeBottleItem sakeBottleItem) {
                YHFluid fluid = sakeBottleItem.getFluid();

                IYHFluidHolder iyhSake = fluid.type;
                Fluid rawFluid = fluid.getSource();

                // 流体物品
                if (fluidItems.containsKey(rawFluid)) {
                    List<Pair<ItemStack, Integer>> oFluidItems = fluidItems.getOrDefault(rawFluid, Collections.emptyList());
                    if (oFluidItems.stream().noneMatch(pair1 -> pair1.getFirst().is(item))) {
                        fluidItems.get(rawFluid).add(Pair.of(item.getDefaultInstance(), iyhSake.amount()));
                    }
                } else {
                    fluidItems.put(rawFluid, Lists.newArrayList(Pair.of(item.getDefaultInstance(), iyhSake.amount())));
                }

                // 流体容器
                ItemStack container = iyhSake.getContainer().getDefaultInstance();
                if (!container.isEmpty()) {
                    if (fluidContainers.containsKey(rawFluid)) {
                        List<ItemStack> itemStacks = fluidContainers.getOrDefault(rawFluid, Collections.emptyList());
                        if (itemStacks.stream().noneMatch(itemStack1 -> itemStack1.is(container.getItem()))) {
                            itemStacks.add(container);
                        }
                    } else {
                        fluidContainers.put(rawFluid, Lists.newArrayList(container));
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
                    // 流体物品
                    if (fluidItems.containsKey(rawFluid)) {
                        List<Pair<ItemStack, Integer>> oFluidItems = fluidItems.getOrDefault(rawFluid, Collections.emptyList());
                        if (oFluidItems.stream().noneMatch(pair1 -> pair1.getFirst().is(defaultInstance.getItem()))) {
                            fluidItems.get(rawFluid).add(Pair.of(defaultInstance, fluidStack.getAmount()));
                        }
                    } else {
                        fluidItems.put(rawFluid, Lists.newArrayList(Pair.of(defaultInstance, fluidStack.getAmount())));
                    }

                    // 流体容器
                    ItemStack container = fluidBucketWrapper.getContainer().getCraftingRemainingItem();
                    if (!container.isEmpty()) {
                        if (fluidContainers.containsKey(rawFluid)) {
                            List<ItemStack> itemStacks = fluidContainers.getOrDefault(rawFluid, Collections.emptyList());
                            if (itemStacks.stream().noneMatch(itemStack1 -> itemStack1.is(container.getItem()))) {
                                itemStacks.add(container);
                            }
                        } else {
                            fluidContainers.put(rawFluid, Lists.newArrayList(container));
                        }
                    }
                }
            }
        }
        this.fluidContainers.putAll(fluidContainers);
        this.fluidItems.putAll(fluidItems);


        consumeRecipes(manager, (oRecipe) -> {
            if (!shouldCacheRecipe(oRecipe))
                return;
            if (!isValidRecipe(oRecipe))
                return;

            SimpleFermentationRecipe recipe = this.castRecipe(oRecipe);

            // 输入的流体
            FluidStack fluidIn = recipe.inputFluid;
            if (fluidIn != null && !fluidIn.isEmpty()) {
                List<ItemStack> inFluidItems = new ArrayList<>();
                if (fluidItems.keySet().stream().anyMatch(fluid -> fluid.isSame(fluidIn.getFluid()))) {
                    fluidItems.forEach((fluid, itemStacks) -> {
                        if (fluid.isSame(fluidIn.getFluid())) {
                            for (Pair<ItemStack, Integer> fluidStackPair : itemStacks) {
                                ItemStack outputFluidItem = fluidStackPair.getFirst().copy();
                                int amount = fluidStackPair.getSecond();
                                int amountTotal = fluidIn.getAmount();
                                outputFluidItem.setCount(Math.max(1, amountTotal / amount));

                                if (inFluidItems.stream().noneMatch(itemStack -> itemStack.is(outputFluidItem.getItem()) && itemStack.getCount() == outputFluidItem.getCount())) {
                                    inFluidItems.add(outputFluidItem);
                                }

                            }
                        }
                    });
                }

                Ingredient fluidIngredient = Ingredient.of(inFluidItems.stream());
                List<Ingredient> allInputs = this.getAllInputs(recipe);

                ResourceLocation recipeId = recipe.getId();
                RecipeIngredientCache.addRecipeCache(recipeId, allInputs);

                this.inFluidItems.put(recipe, fluidIngredient);
            } else {
                List<Ingredient> allInputs = this.getAllInputs(recipe);

                ResourceLocation recipeId = recipe.getId();
                RecipeIngredientCache.addRecipeCache(recipeId, allInputs);

                this.inFluidItems.put(recipe, Ingredient.EMPTY);
            }
        });
    }

    @Override
    public List<Ingredient> getInputs(FermentationRecipe<?> recipe) {
        List<Ingredient> allInputs = Lists.newArrayList();
        Ingredient fluidIngredient = this.inFluidItems.getOrDefault(recipe, Ingredient.EMPTY);
        if (!fluidIngredient.isEmpty()) {
            allInputs.add(fluidIngredient);
        }
        List<Ingredient> allInputsNoFluid = this.castRecipe(recipe).ingredients;
        allInputs.addAll(allInputsNoFluid);

        return allInputs;
    }

    @Override
    public List<Ingredient> getContainers(FermentationRecipe<?> recipe) {
        SimpleFermentationRecipe simpleFermentationRecipe = castRecipe(recipe);
        FluidStack outputFluid = simpleFermentationRecipe.outputFluid;
        Fluid fluid = outputFluid.getFluid();
        List<ItemStack> containers = fluidContainers.getOrDefault(fluid, List.of());
        if (fluid instanceof YHFluid sakeFluid) {
            containers = fluidContainers.getOrDefault(fluid, List.of()).stream()
                    .map(itemStack -> itemStack.copyWithCount(sakeFluid.type.count()))
                    .toList();
        }
        if (containers.isEmpty()) {
            return List.of();
        }
        return List.of(Ingredient.of(containers.stream()));
    }

    @Override
    public List<ItemStack> getOutputs(FermentationRecipe<?> recipe, RegistryAccess registryAccess) {
        SimpleFermentationRecipe sFermentationRecipe = (SimpleFermentationRecipe) recipe;
        FluidStack outputFluid = sFermentationRecipe.outputFluid;
        Fluid fluid = outputFluid.getFluid();
        ItemStack output;
        if (fluid instanceof YHFluid sakeFluid) {
            output = sakeFluid.type.asStack(sakeFluid.type.count());
        } else if (!sFermentationRecipe.defaultContainer.isEmpty() && !sFermentationRecipe.defaultBottle.isEmpty()){
            output = sFermentationRecipe.defaultBottle;
        } else {
            List<ItemStack> oResults = Lists.newArrayList();
            for (ItemStack result : sFermentationRecipe.results) {
                boolean grow = false;
                for (ItemStack oResult : oResults) {
                    if (ItemStack.isSameItemSameTags(result, oResult)) {
                        oResult.grow(result.getCount());
                        grow = true;
                        break;
                    }
                }
                if (!grow) {
                    oResults.add(result.copy());
                }
            }

            return oResults;
        }

        return List.of(output);
    }

    @Override
    public Item getBlockItemForTranslate() {
        return YHBlocks.FERMENT.asItem();
    }
}
