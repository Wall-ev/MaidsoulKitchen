package com.github.wallev.maidsoulkitchen.task.cook.v1.youkaishomecoming;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.handler.VItemStackHelper;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.RegisterData;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.action.IMaidAction;
import com.github.wallev.maidsoulkitchen.util.FakePlayerUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import dev.xkmc.youkaishomecoming.content.item.fluid.IYHSake;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeBottleItem;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeFluid;
import dev.xkmc.youkaishomecoming.content.pot.ferment.*;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.EmptyFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

import static dev.xkmc.youkaishomecoming.content.pot.ferment.FermentationTankBlock.OPEN;

/**
 * 2025/03/09
 * 写的什么屎山，还请见谅，应该是半年时间吧，如果不出意外的话。
 * 后面会一起重构的，现在将就着用着先把。
 */
public class TaskYhcFermentationTank implements ICookTask<FermentationTankBlockEntity, FermentationRecipe<?>>, IMaidAction {
    // 配方所需的流体对应的itemStacks和原材料
    protected static final Map<SimpleFermentationRecipe, MaidFermentationRecipe> FERMENTATION_RECIPE_INGREDIENTS = new HashMap<>();
    // 流体容器
    protected static final Map<Fluid, List<ItemStack>> FLUID_CONTAINERS = new HashMap<>();

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.YHC_FERMENTATION_TANK;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof FermentationTankBlockEntity;
    }

    @Override
    public RecipeType<FermentationRecipe<?>> getRecipeType() {
        return YHBlocks.FERMENT_RT.get();
    }

    @Override
    public MaidRecipesManager<FermentationRecipe<?>> getRecipesManager(EntityMaid maid) {
        return new MaidRecipesManager<>(maid, this, true) {
            @Override
            protected Pair<List<Integer>, List<Item>> getAmountIngredient(FermentationRecipe<?> recipe, Map<Item, Integer> available) {
                MaidFermentationRecipe maidKettleRecipe = FERMENTATION_RECIPE_INGREDIENTS.get((SimpleFermentationRecipe) recipe);
                List<Item> invIngredient = new ArrayList<>();
                Map<Item, Integer> itemTimes = new HashMap<>();

                // 流体
                boolean hasFluidItem = false;
                int fluidItemAmount = 0;
                Item fluidItem = ItemStack.EMPTY.getItem();
                for (ItemStack ingredient : maidKettleRecipe.inFluids()) {
                    boolean hasIngredient = false;
                    for (Item item : available.keySet()) {
                        if (ingredient.is(item) && available.get(item) >= ingredient.getCount()) {
                            invIngredient.add(item);
                            hasIngredient = true;

                            if (item.getMaxStackSize() == 1) {
                                itemTimes.put(item, 1);
                            } else {
                                itemTimes.merge(item, 1, Integer::sum);
                            }

                            fluidItemAmount = ingredient.getCount();
                            fluidItem = item;

                            break;
                        }
                    }

                    if (hasIngredient) {
                        hasFluidItem = true;
                        break;
                    }
                }
                if (!maidKettleRecipe.inFluids().isEmpty() && !hasFluidItem) {
                    return Pair.of(Collections.emptyList(), Collections.emptyList());
                }

                // 原材料
                for (Ingredient ingredient : maidKettleRecipe.inItems()) {
                    boolean hasIngredient = false;
                    for (Item item : available.keySet()) {
                        ItemStack stack = item.getDefaultInstance();
                        if (ingredient.test(stack)) {
                            invIngredient.add(item);
                            hasIngredient = true;

                            if (stack.getMaxStackSize() == 1) {
                                itemTimes.put(item, 1);
                            } else {
                                itemTimes.merge(item, 1, Integer::sum);
                            }

                            break;
                        }
                    }

                    if (!hasIngredient) {
                        return Pair.of(Collections.emptyList(), Collections.emptyList());
                    }
                }

                // 检查是否缺少材料
                if (itemTimes.entrySet().stream().anyMatch(entry -> available.get(entry.getKey()) < entry.getValue())) {
                    return Pair.of(Collections.emptyList(), Collections.emptyList());
                }

                // 计算最大合成次数
                int maxCount = 1;

                // 计算每个物品的数量
                List<Integer> countList = new ArrayList<>();
                if (!maidKettleRecipe.inFluids().isEmpty()) {
                    countList.add(0, fluidItemAmount);
                    available.put(fluidItem, available.get(fluidItem) - fluidItemAmount);
                } else {
                    countList.add(0, 0);
                    invIngredient.add(0, ItemStack.EMPTY.getItem());
                }
                for (Item item : invIngredient.stream().skip(1).toList()) {
                    countList.add(maxCount);
                    available.put(item, available.get(item) - maxCount);
                }

                return Pair.of(countList, invIngredient);
            }
        };
    }

    @SuppressWarnings("all")
    @Override
    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid maid, FermentationTankBlockEntity blockEntity, MaidRecipesManager<FermentationRecipe<?>> recManager) {
        CombinedInvWrapper maidInv = maid.getAvailableInv(true);

        // 发酵桶是否在发酵
        FermentationDummyContainer cont = new FermentationDummyContainer(blockEntity.items, blockEntity.fluids);
        Optional<FermentationRecipe<?>> beRecipe = maid.level.getRecipeManager().getRecipeFor((RecipeType) YHBlocks.FERMENT_RT.get(), cont, maid.level);

        // 有未取出的流体并且没有在发酵
        FluidStack fluidInTank = blockEntity.fluids.getFluidInTank(0);
        Fluid fluid = fluidInTank.getFluid();
        if (!fluidInTank.isEmpty() && beRecipe.isEmpty()) {
            boolean hasFluidContainer;

            if (fluid instanceof SakeFluid sakeFluid) {
                ItemStack outputFluidContainers = sakeFluid.type.getContainer().getDefaultInstance();
                hasFluidContainer = recManager.hasOutputAdditionItem(itemStack -> itemStack.is(outputFluidContainers.getItem()));
            } else {
                List<ItemStack> outputFluidContainers = FLUID_CONTAINERS.getOrDefault(fluid, Collections.emptyList());
                hasFluidContainer = recManager.hasOutputAdditionItem(itemStack -> outputFluidContainers.stream().anyMatch(stack -> stack.is(itemStack.getItem())));
            }

            if (hasFluidContainer) {
                return true;
            }
        }

        // 发酵桶有多余的原材料（不可以发酵）以及发酵桶没有在发酵
        if (!blockEntity.items.isEmpty() && beRecipe.isEmpty()) {
            return true;
        }

        // 发酵桶没有在发酵并且有配方原材料
        if (fluidInTank.isEmpty() && blockEntity.items.isEmpty() && beRecipe.isEmpty() && blockEntity.inProgress() == 0) {
            if (!recManager.getRecipesIngredients().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("all")
    @Override
    public void processCookMake(ServerLevel serverLevel, EntityMaid maid, FermentationTankBlockEntity blockEntity, MaidRecipesManager<FermentationRecipe<?>> recManager) {
        CombinedInvWrapper maidInv = maid.getAvailableInv(true);
        IItemHandlerModifiable inputInv = recManager.getInputInv();
        IItemHandlerModifiable outputAdditionInv = recManager.getOutputAdditionInv();
        IItemHandlerModifiable outputInv = recManager.getOutputInv();

        boolean extracted = false;

        // 发酵桶是否在发酵
        FermentationDummyContainer cont = new FermentationDummyContainer(blockEntity.items, blockEntity.fluids);
        Optional<FermentationRecipe<?>> beRecipe = maid.level.getRecipeManager().getRecipeFor((RecipeType) YHBlocks.FERMENT_RT.get(), cont, maid.level);

        FluidStack fluidInTank = blockEntity.fluids.getFluidInTank(0);
        Fluid fluid = fluidInTank.getFluid();
        // 有未取出的流体并且没有在发酵
        if (!fluidInTank.isEmpty() && beRecipe.isEmpty()) {
            ItemStack fluidContainer;

            // 获取流体容器
            if (fluid instanceof SakeFluid sakeFluid) {
                ItemStack outputFluidContainers = sakeFluid.type.getContainer().getDefaultInstance();
                fluidContainer = recManager.findOutputAdditionItem(itemStack -> itemStack.is(outputFluidContainers.getItem()));
            } else {
                List<ItemStack> outputFluidContainers = FLUID_CONTAINERS.getOrDefault(fluid, Collections.emptyList());
                fluidContainer = recManager.findOutputAdditionItem(itemStack -> outputFluidContainers.stream().anyMatch(stack -> stack.is(itemStack.getItem())));
            }

            // 取出流体
            while (!fluidInTank.isEmpty() && !fluidContainer.isEmpty()) {
                ItemStack interactItem = VItemStackHelper.copyWithCount(fluidContainer, 1);
                ItemStack interactedItem = FakePlayerUtil.interactUseOnBlock(maid, blockEntity.getBlockPos(), interactItem.copy());

                if (!interactedItem.isEmpty()) {
                    if (!ItemStack.isSameItem(interactItem, interactedItem)) {
                        ItemStack leftItem = ItemHandlerHelper.insertItemStacked(outputInv, interactedItem, false);
                        fluidContainer.shrink(1);
                        if (!leftItem.isEmpty()) {
                            maid.spawnAtLocation(leftItem);
                        }
                    }
                } else if (fluid instanceof SakeFluid sakeFluid) {
                    ItemStack leftItem = ItemHandlerHelper.insertItemStacked(outputInv, sakeFluid.type.asStack(1), false);
                    fluidContainer.shrink(1);
                    if (!leftItem.isEmpty()) {
                        maid.spawnAtLocation(leftItem);
                    }
                }

                blockEntity.notifyTile();

                extracted = true;
            }

            // 将剩下的容器放回背包
            ItemStack leftItem = ItemHandlerHelper.insertItemStacked(outputAdditionInv, fluidContainer, false);
            if (!leftItem.isEmpty()) {
                maid.spawnAtLocation(leftItem);
            }
        }

        // 发酵桶有多余的原材料（不可以发酵）以及发酵桶没有在发酵
        if (!blockEntity.items.isEmpty() && beRecipe.isEmpty()) {
            FermentationItemContainer items = blockEntity.items;
            for(int i = 0; i < items.getContainerSize(); ++i) {
                ItemStack item = items.getItem(i);
                ItemStack leftItem = ItemHandlerHelper.insertItemStacked(inputInv, item.copy(), false);
                item.shrink(item.getCount() - leftItem.getCount());

                extracted = true;
            }
        }

        if (extracted) {
            pickupAction(maid);
        }


        // 发酵桶没有在发酵并且有配方原材料
        if (fluidInTank.isEmpty() && blockEntity.items.isEmpty() && beRecipe.isEmpty() && blockEntity.inProgress() == 0) {
            if (!recManager.getRecipesIngredients().isEmpty()) {
                Pair<List<Integer>, List<List<ItemStack>>> recipeIngredient = recManager.getRecipeIngredient();

                if (recipeIngredient.getFirst().isEmpty()) {
                    return;
                }

                // 填充流体
                List<ItemStack> fluidItems = recipeIngredient.getSecond().get(0);
                for (int times = 0; times < recipeIngredient.getFirst().get(0); ) {
                    for (ItemStack fluidItem : fluidItems) {
                        ItemStack interactItem = VItemStackHelper.copyWithCount(fluidItem, 1);
                        ItemStack interactedStack = FakePlayerUtil.interactUseOnBlock(maid, blockEntity.getBlockPos(), interactItem.copy());
                        if (!ItemStack.isSameItem(interactItem, interactedStack)) {
                            fluidItem.shrink(1);
                            ItemHandlerHelper.insertItemStacked(inputInv, interactedStack, false);
                        }
                        times++;
                    }
                }


                int i = 0;
                for (List<ItemStack> itemStacks : recipeIngredient.getSecond()) {

                    // 过滤掉第一批次的物资，那是用来填充流体的。
                    if (i++ < 1) {
                        continue;
                    }

                    Optional<ItemStack> first = itemStacks.stream().filter(stack -> !stack.isEmpty()).findFirst();
                    first.ifPresent(stack -> {
                        ItemStack copy = stack.copy();
                        copy.setCount(1);
                        if (blockEntity.items.canAddItem(copy)) {
                            ItemStack remain = blockEntity.items.addItem(copy);
                            if (remain.isEmpty()) {
                                stack.shrink(1);
                                blockEntity.notifyTile();
                            }
                        }
                    });
                }

                serverLevel.setBlockAndUpdate(blockEntity.getBlockPos(), blockEntity.getBlockState().setValue(OPEN, false));
                blockEntity.notifyTile();

                pickupAction(maid);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.YHC_FERMENTATION_TANK.uid;
    }

    @Override
    public ItemStack getIcon() {
        return YHBlocks.FERMENT.asStack();
    }

    @Override
    public List<FermentationRecipe<?>> getRecipes(Level level) {
        List<FermentationRecipe<?>> fermentationRecipes = ICookTask.super.getRecipes(level);
//        FERMENTATION_RECIPE_INGREDIENTS.clear();

        if (FERMENTATION_RECIPE_INGREDIENTS.isEmpty()) {
            FLUID_CONTAINERS.clear();

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
                    SakeFluid sakeFluid = sakeBottleItem.getFluid();
                    IYHSake iyhSake = sakeFluid.type;
                    Fluid rawFluid = sakeFluid.getSource();

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

            FLUID_CONTAINERS.putAll(fluidContainers1);

            for (FermentationRecipe<?> recipe : fermentationRecipes) {
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

                    MaidFermentationRecipe maidKegFermentingRecipe = new MaidFermentationRecipe(fluidItems, fermentationRecipe.ingredients);
                    FERMENTATION_RECIPE_INGREDIENTS.put(fermentationRecipe, maidKegFermentingRecipe);
                } else {
                    MaidFermentationRecipe maidKegFermentingRecipe = new MaidFermentationRecipe(Collections.emptyList(), fermentationRecipe.ingredients);
                    FERMENTATION_RECIPE_INGREDIENTS.put(fermentationRecipe, maidKegFermentingRecipe);
                }
            }
        }

        return fermentationRecipes;
    }

    @Override
    public NonNullList<Ingredient> getIngredients(Recipe<?> recipe) {
        SimpleFermentationRecipe fermentationRecipe = (SimpleFermentationRecipe) recipe;
        NonNullList<Ingredient> ingredinetNonNullList = NonNullList.create();

        MaidFermentationRecipe maidKegRecipe = FERMENTATION_RECIPE_INGREDIENTS.get(fermentationRecipe);
        if (!maidKegRecipe.inFluids.isEmpty()) {
            ingredinetNonNullList.add(Ingredient.of(maidKegRecipe.inFluids.stream()));
        } else {
            ingredinetNonNullList.add(Ingredient.EMPTY);
        }
        ingredinetNonNullList.addAll(maidKegRecipe.inItems());

        return ingredinetNonNullList;
    }

    @Override
    public ItemStack getResultItem(Recipe<?> recipe, RegistryAccess pRegistryAccess) {
        SimpleFermentationRecipe fermentationRecipe = (SimpleFermentationRecipe) recipe;
        Fluid fluid = fermentationRecipe.outputFluid.getFluid();
        if (fluid instanceof SakeFluid sakeFluid) {
            return sakeFluid.type.asStack(1);
        }
        return Items.AIR.getDefaultInstance();
    }

    public record MaidFermentationRecipe(List<ItemStack> inFluids, List<Ingredient> inItems) {
    }
}
