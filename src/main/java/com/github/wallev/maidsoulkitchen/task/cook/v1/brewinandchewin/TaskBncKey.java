package com.github.wallev.maidsoulkitchen.task.cook.v1.brewinandchewin;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.ai.MaidCookMakeTask;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor.ICbeAccessor;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
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
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;
import umpaz.brewinandchewin.common.crafting.KegFermentingRecipe;
import umpaz.brewinandchewin.common.crafting.KegPouringRecipe;
import umpaz.brewinandchewin.common.registry.BnCBlocks;
import umpaz.brewinandchewin.common.registry.BnCRecipeTypes;

import java.util.*;
import java.util.stream.Collectors;

import static umpaz.brewinandchewin.common.block.entity.KegBlockEntity.isValidTemp;

public class TaskBncKey implements ICookTask<KegBlockEntity, KegFermentingRecipe> {
    // 配方所需的流体对应的itemStacks和原材料
    protected static final Map<KegFermentingRecipe, MaidKegRecipe> KEG_RECIPE_INGREDIENTS = new HashMap<>();
    // 流体容器
    protected static final Map<Fluid, List<ItemStack>> FLUID_CONTAINERS = new HashMap<>();

    private static BlockPos getSearchPos(EntityMaid maid) {
        return maid.hasRestriction() ? maid.getRestrictCenter() : maid.blockPosition().below();
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof KegBlockEntity;
    }

    @Override
    public RecipeType<KegFermentingRecipe> getRecipeType() {
        return BnCRecipeTypes.FERMENTING.get();
    }

    @Override
    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid maid, KegBlockEntity kegBlockEntity, MaidRecipesManager<KegFermentingRecipe> recManager) {
        ItemStackHandler inventory = kegBlockEntity.getInventory();
        KegFermentingRecipesManager kegFermentingRecipesManager = (KegFermentingRecipesManager) recManager;

        // 输出槽有未取出的物品
        if (!inventory.getStackInSlot(KegBlockEntity.OUTPUT_SLOT).isEmpty()) {
            return true;
        }

        boolean innerCanCook = ((ICbeAccessor) kegBlockEntity).innerCanCook$tlma();

        // 存在输出流体，待容器取出
        Fluid outputFluid = kegBlockEntity.getOutput().getFluid();
        List<ItemStack> outputFluidContainers = FLUID_CONTAINERS.getOrDefault(outputFluid, Collections.emptyList());
        if (!innerCanCook && recManager.hasOutputAdditionItem(itemStack -> outputFluidContainers.stream().anyMatch(stack -> stack.is(itemStack.getItem())))) {
            return true;
        }

        // 容器内部没有符合烹饪的原材料&&仓库存在可以烹饪的原材料

        if (!innerCanCook && kegFermentingRecipesManager.hasRecipeIngredientsWithTemp(kegBlockEntity.getTemperature())) {
            return true;
        }

        // 容器内部没有符合烹饪的原材料&&容器内部存在余下的材料
        boolean hasInput = false;
        if (!innerCanCook) {
            for (int i = 0; i < inventory.getSlots(); i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    hasInput = true;
                    break;
                }
            }
        }
        if (!innerCanCook && hasInput) {
            return true;
        }

        // 容器内部有流体并且仓库存在流体容器以及没在烹饪
        Fluid fluid = kegBlockEntity.getFluidTank().getFluid().getFluid();
        List<ItemStack> fluidContainers = FLUID_CONTAINERS.getOrDefault(fluid, Collections.emptyList());
        if (!innerCanCook && recManager.hasOutputAdditionItem(itemStack -> fluidContainers.stream().anyMatch(stack -> stack.is(itemStack.getItem())))) {
            return true;
        }

        return false;
    }

    @Override
    public void processCookMake(ServerLevel serverLevel, EntityMaid maid, KegBlockEntity kegBlockEntity, MaidRecipesManager<KegFermentingRecipe> recManager) {
        KegFermentingRecipesManager kegFermentingRecipesManager = (KegFermentingRecipesManager) recManager;

        ItemStackHandler inventory = kegBlockEntity.getInventory();
        IItemHandlerModifiable inputInv = recManager.getInputInv();
        IItemHandlerModifiable outputInv = recManager.getOutputInv();
        IItemHandlerModifiable outputAdditionInv = recManager.getOutputAdditionInv();

        // 输出槽有未取出的物品
        ItemStack output = inventory.getStackInSlot(KegBlockEntity.OUTPUT_SLOT);
        if (!output.isEmpty()) {
            ItemStack outputCopy = output.copy();
            ItemStack leftItemStack = ItemHandlerHelper.insertItemStacked(outputInv, outputCopy, false);
            output.shrink(outputCopy.getCount() - leftItemStack.getCount());

            kegBlockEntity.setChanged();
        }

        // 存在容器
        ItemStack container = inventory.getStackInSlot(KegBlockEntity.CONTAINER_SLOT);
        if (!container.isEmpty()) {
            ItemStack containerCopy = container.copy();
            ItemStack leftItemStack = ItemHandlerHelper.insertItemStacked(outputAdditionInv, containerCopy, false);
            container.shrink(containerCopy.getCount() - leftItemStack.getCount());

            kegBlockEntity.setChanged();
        }

        boolean innerCanCook = ((ICbeAccessor) kegBlockEntity).innerCanCook$tlma();

        // 存在输出流体，待容器取出
        Fluid outputFluid = kegBlockEntity.getOutput().getFluid();
        List<ItemStack> outputFluidContainers = FLUID_CONTAINERS.getOrDefault(outputFluid, Collections.emptyList());
        ItemStack outputAdditionItem = recManager.findOutputAdditionItem(itemStack -> outputFluidContainers.stream().anyMatch(stack -> stack.is(itemStack.getItem())));
        if (!innerCanCook && !outputAdditionItem.isEmpty()) {

            ItemStack copy = outputAdditionItem.copy();
            outputAdditionItem.setCount(0);
            List<ItemStack> extracted = kegBlockEntity.extractInWorld(kegBlockEntity, copy, copy.getCount(), false);
            extracted.add(copy);
            for (ItemStack stack : extracted) {
                ItemStack leftInsertedStack = ItemHandlerHelper.insertItemStacked(inputInv, stack, false);
                if (!leftInsertedStack.isEmpty()) {
                    maid.spawnAtLocation(leftInsertedStack);
                }
            }
        }

//         容器内部没有符合烹饪的原材料&&容器内部存在余下的材料
//         容器内部没有符合烹饪的原材料&&仓库存在可以烹饪的原材料
        boolean hasInput = false;
        if (!innerCanCook) {
            for (int i = 0; i < inventory.getSlots(); i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    ItemStack copy = stack.copy();
                    ItemStack leftItemStack = ItemHandlerHelper.insertItemStacked(inputInv, copy, false);
                    stack.shrink(copy.getCount() - leftItemStack.getCount());
                    if (!stack.isEmpty()) {
                        hasInput = true;
                    }
                }
            }

            kegBlockEntity.setChanged();
        }

        if (!innerCanCook && !hasInput && kegBlockEntity.getFluidTank().isEmpty() && kegFermentingRecipesManager.hasRecipeIngredientsWithTemp(kegBlockEntity.getTemperature())) {
            Pair<List<Integer>, List<List<ItemStack>>> recipeIngredient = kegFermentingRecipesManager.getRecipeIngredient(kegBlockEntity.getTemperature());
            if (hasEnoughIngredient(recipeIngredient.getFirst(), recipeIngredient.getSecond())) {

                int amount = recipeIngredient.getFirst().get(0);
                for (ItemStack itemStack : recipeIngredient.getSecond().get(0)) {
                    if (itemStack.isEmpty()) continue;
                    int count = itemStack.getCount();

                    if (count >= amount) {
                        List<ItemStack> extracted = kegBlockEntity.extractInWorld(kegBlockEntity, itemStack.copyWithCount(amount), amount, false);
                        itemStack.shrink(amount);
                        for (ItemStack stack : extracted) {
                            ItemStack leftInsertedStack = ItemHandlerHelper.insertItemStacked(inputInv, stack, false);
                            if (!leftInsertedStack.isEmpty()) {
                                maid.spawnAtLocation(leftInsertedStack);
                            }
                        }
                        break;
                    } else {
                        List<ItemStack> extracted = kegBlockEntity.extractInWorld(kegBlockEntity, itemStack.copyWithCount(count), count, false);
                        itemStack.shrink(count);
                        for (ItemStack stack : extracted) {
                            ItemStack leftInsertedStack = ItemHandlerHelper.insertItemStacked(inputInv, stack, false);
                            if (!leftInsertedStack.isEmpty()) {
                                maid.spawnAtLocation(leftInsertedStack);
                            }
                        }
                        amount -= count;
                        if (amount <= 0) {
                            break;
                        }
                    }
                }

                this.insertInputsStack(inventory, inputInv, kegBlockEntity, recipeIngredient);
                kegBlockEntity.setChanged();
            }
        }
    }

    private boolean hasEnoughIngredient(List<Integer> amounts, List<List<ItemStack>> ingredients) {
        boolean canInsert = true;

        int i = 0;
        for (List<ItemStack> ingredient : ingredients) {
            if (ingredient.isEmpty()) continue;

            int actualCount = amounts.get(i++);
            for (ItemStack itemStack : ingredient) {
                if (itemStack.isEmpty()) continue;

                actualCount -= itemStack.getCount();
                if (actualCount <= 0) {
                    break;
                }
            }

            if (actualCount > 0) {
                canInsert = false;
                break;
            }
        }

        return canInsert;
    }

    private void insertInputsStack(ItemStackHandler beInv, IItemHandlerModifiable ingreInputsInv, KegBlockEntity be, Pair<List<Integer>, List<List<ItemStack>>> ingredientPair) {
        List<Integer> amounts = ingredientPair.getFirst();
        List<List<ItemStack>> ingredients = ingredientPair.getSecond();

        for (int i = 0, j = 0; i < ingredients.size() - 1; i++, j++) {
            insertAndShrink(beInv, amounts.get(j + 1), ingredients, j + 1, i);
        }
        be.setChanged();
    }

    private void insertAndShrink(ItemStackHandler beInv, Integer amount, List<List<ItemStack>> ingredient, int ingredientIndex, int slotIndex) {
        for (ItemStack itemStack : ingredient.get(ingredientIndex)) {
            if (itemStack.isEmpty()) continue;
            int count = itemStack.getCount();

            if (count >= amount) {
                ItemStack leftInsertedStack = beInv.insertItem(slotIndex, itemStack.copyWithCount(amount), false);
                itemStack.shrink(amount - leftInsertedStack.getCount());
                break;
            } else {
                ItemStack leftInsertedStack = beInv.insertItem(slotIndex, itemStack.copyWithCount(count), false);
                itemStack.shrink(count - leftInsertedStack.getCount());
                amount -= count;
                if (amount <= 0) {
                    break;
                }
            }
        }
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        if (maid.level.isClientSide) {
            return Collections.emptyList();
        }

        MaidRecipesManager<KegFermentingRecipe> cookingPotRecipeMaidRecipesManager = getRecipesManager(maid);
        MaidFermentingMoveTask maidCookMoveTask = new MaidFermentingMoveTask(this, (KegFermentingRecipesManager) cookingPotRecipeMaidRecipesManager);
        MaidCookMakeTask<KegBlockEntity, KegFermentingRecipe> maidCookMakeTask = new MaidCookMakeTask<>(this, cookingPotRecipeMaidRecipesManager);
        return Lists.newArrayList(Pair.of(5, maidCookMoveTask), Pair.of(6, maidCookMakeTask));
    }

    @Override
    public List<KegFermentingRecipe> getRecipes(Level level) {
        KEG_RECIPE_INGREDIENTS.clear();

        if (KEG_RECIPE_INGREDIENTS.isEmpty()) {
            FLUID_CONTAINERS.clear();

            Map<Fluid, List<Pair<ItemStack, FluidStack>>> registriesFluidItems = new HashMap<>();
            for (Item item : ForgeRegistries.ITEMS.getValues()) {
                ItemStack defaultInstance = item.getDefaultInstance();
                IFluidHandlerItem iFluidHandlerItem = defaultInstance.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
                if (iFluidHandlerItem != null && iFluidHandlerItem instanceof FluidBucketWrapper fluidBucketWrapper && !fluidBucketWrapper.getFluid().isEmpty()) {
                    Fluid fluid = fluidBucketWrapper.getFluid().getFluid();

                    registriesFluidItems.merge(fluid, Lists.newArrayList(Pair.of(defaultInstance, fluidBucketWrapper.getFluid())), (a1, a2) -> {
                        a1.addAll(a2);
                        return a1;
                    });

                    ItemStack container = fluid.getBucket().getDefaultInstance().getCraftingRemainingItem();
                    if (!container.isEmpty()) {
                        FLUID_CONTAINERS.putIfAbsent(fluid, Lists.newArrayList(container));
                    }
                }
            }

            Map<Fluid, List<ItemStack>> fluidItemStacks = new HashMap<>();
            List<KegFermentingRecipe> KegFermentingRecipes = level.getRecipeManager().getAllRecipesFor(BnCRecipeTypes.FERMENTING.get());
            List<KegPouringRecipe> kegPouringRecipes = level.getRecipeManager().getAllRecipesFor(BnCRecipeTypes.KEG_POURING.get());


            for (KegFermentingRecipe kegFermentingRecipe : KegFermentingRecipes) {
                // 输入的流体
                FluidStack fluidIn = kegFermentingRecipe.getFluidIngredient();
                if (fluidIn != null && !fluidIn.isEmpty()) {
                    // 输入的流体容器
                    if (!FLUID_CONTAINERS.containsKey(fluidIn.getFluid())) {
                        List<KegPouringRecipe> matchKegPouringRecipes = kegPouringRecipes.stream()
                                .filter(pouringRecipe -> pouringRecipe.getRawFluid().isSame(fluidIn.getFluid()))
                                .toList();
                        List<ItemStack> list = matchKegPouringRecipes.stream()
                                .map(KegPouringRecipe::getContainer)
                                .filter(itemStack -> !itemStack.isEmpty())
                                .toList();

                        if (!list.isEmpty()) {
                            FLUID_CONTAINERS.put(fluidIn.getFluid(), Lists.newArrayList(list));
                        }

                    }

                    // 输入的流体所对应的ItemStack
                    List<ItemStack> fluidItems = fluidItemStacks.computeIfAbsent(fluidIn.getFluid(), fluid -> {
                        return kegPouringRecipes.stream()
                                .filter(pouringRecipe -> pouringRecipe.getRawFluid().isSame(fluid))
                                .map(kegPouringRecipe -> {
                                    ItemStack outputFluidItem = kegPouringRecipe.getOutput();
                                    int amount = kegPouringRecipe.getAmount();
                                    int amountTotal = fluidIn.getAmount();
                                    outputFluidItem.setCount(amountTotal / amount);
                                    return outputFluidItem;
                                }).toList();
                    });
                    if (registriesFluidItems.containsKey(fluidIn.getFluid())) {
                        List<ItemStack> fluidItems9 = new ArrayList<>(fluidItems);
                        registriesFluidItems.forEach((fluid, itemStacks) -> {
                            if (fluid.isSame(fluidIn.getFluid())) {
                                for (Pair<ItemStack, FluidStack> fluidStackPair : itemStacks) {
                                    ItemStack outputFluidItem = fluidStackPair.getFirst();
                                    int amount = fluidStackPair.getSecond().getAmount();
                                    int amountTotal = fluidIn.getAmount();
                                    outputFluidItem.setCount(amountTotal / amount);

                                    if (!fluidItems9.contains(outputFluidItem)) {
                                        fluidItems9.add(outputFluidItem);
                                    }

                                }
                            }
                        });

                        fluidItems = fluidItems9;
                    }


                    MaidKegRecipe maidKegFermentingRecipe = new MaidKegRecipe(fluidItems, kegFermentingRecipe.getIngredients());
                    KEG_RECIPE_INGREDIENTS.put(kegFermentingRecipe, maidKegFermentingRecipe);
                } else {
                    MaidKegRecipe maidKegFermentingRecipe = new MaidKegRecipe(Collections.emptyList(), kegFermentingRecipe.getIngredients());
                    KEG_RECIPE_INGREDIENTS.put(kegFermentingRecipe, maidKegFermentingRecipe);
                }

                // 输出的流体容器
                Fluid fluidOut = kegFermentingRecipe.getResultFluid();
                if (fluidOut != null && !(fluidOut instanceof EmptyFluid) && !FLUID_CONTAINERS.containsKey(fluidOut)) {
                    List<KegPouringRecipe> matchKegPouringRecipes = kegPouringRecipes.stream()
                            .filter(pouringRecipe -> pouringRecipe.getRawFluid().isSame(fluidOut))
                            .toList();

                    List<ItemStack> list = matchKegPouringRecipes.stream()
                            .map(KegPouringRecipe::getContainer)
                            .filter(container -> !container.isEmpty())
                            .toList();
                    FLUID_CONTAINERS.put(fluidOut, Lists.newArrayList(list));
                }
            }

            for (Fluid fluid : ForgeRegistries.FLUIDS.getValues()) {
                if (fluid instanceof EmptyFluid) continue;

                ItemStack container = fluid.getBucket().getDefaultInstance().getCraftingRemainingItem();
                if (container.isEmpty()) continue;

                FLUID_CONTAINERS.merge(fluid, Lists.newArrayList(container), (itemStacks, itemStacks2) -> {
                    itemStacks.addAll(itemStacks2);
                    return itemStacks;
                });
            }
        }
        return KEG_RECIPE_INGREDIENTS.keySet().stream().toList();
    }

    @Override
    public MaidRecipesManager<KegFermentingRecipe> getRecipesManager(EntityMaid maid) {
        return new KegFermentingRecipesManager(maid, this) {
            @Override
            protected List<KegFermentingRecipe> getFilterRecipes(List<KegFermentingRecipe> rec) {
                Set<Integer> temperates = searchAndCreateTemperate((ServerLevel) maid.level, maid);
                return super.getFilterRecipes(rec).stream()
                        .filter(kegFermentingRecipe -> {
                            for (Integer temperate : temperates) {
                                if (isValidTemp(temperate, kegFermentingRecipe.getTemperature())) {
                                    return true;
                                }
                            }
                            return false;
                        })
                        .collect(Collectors.toList());
            }
        };
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.BNC_KEY.uid;
    }

    @Override
    public ItemStack getIcon() {
        return BnCBlocks.KEG.get().asItem().getDefaultInstance();
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.BNC_KEY;
    }

    protected Set<Integer> searchAndCreateTemperate(ServerLevel worldIn, EntityMaid maid) {
        Set<Integer> worldBlockEntityTemperates = new HashSet<>();

        BlockPos centrePos = getSearchPos(maid);
        int searchRange = (int) maid.getRestrictRadius();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int y = 0; y <= 2; y = y > 0 ? -y : 1 - y) {
            for (int i = 0; i < searchRange; ++i) {
                for (int x = 0; x <= i; x = x > 0 ? -x : 1 - x) {
                    for (int z = x < i && x > -i ? i : 0; z <= i; z = z > 0 ? -z : 1 - z) {
                        mutableBlockPos.setWithOffset(centrePos, x, y + 1, z);
                        if (maid.isWithinRestriction(mutableBlockPos)) {
                            BlockEntity blockEntity = worldIn.getBlockEntity(mutableBlockPos);
                            if (blockEntity instanceof KegBlockEntity kegBlockEntity && !((ICbeAccessor) kegBlockEntity).innerCanCook$tlma()) {
                                worldBlockEntityTemperates.add(kegBlockEntity.getTemperature());
                            }
                        }
                    }
                }
            }
        }
        return worldBlockEntityTemperates;
    }

    public record MaidKegRecipe(List<ItemStack> inFluids, List<Ingredient> inItems) {
    }
}
