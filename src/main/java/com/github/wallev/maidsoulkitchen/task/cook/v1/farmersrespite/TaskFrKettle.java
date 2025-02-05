package com.github.wallev.maidsoulkitchen.task.cook.v1.farmersrespite;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor.ICbeAccessor;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
import umpaz.farmersrespite.common.block.entity.KettleBlockEntity;
import umpaz.farmersrespite.common.crafting.KettlePouringRecipe;
import umpaz.farmersrespite.common.crafting.KettleRecipe;
import umpaz.farmersrespite.common.registry.FRItems;
import umpaz.farmersrespite.common.registry.FRRecipeTypes;

import java.util.*;

public class TaskFrKettle implements ICookTask<KettleBlockEntity, KettleRecipe> {
    // 配方所需的流体对应的itemStacks和原材料
    protected static final Map<KettleRecipe, MaidKettleRecipe> KEY_RECIPE_INGREDIENTS = new HashMap<>();
    // 流体容器
    protected static final Map<Fluid, List<ItemStack>> FLUID_CONTAINERS = new HashMap<>();

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof KettleBlockEntity;
    }

    @Override
    public RecipeType<KettleRecipe> getRecipeType() {
        return FRRecipeTypes.BREWING.get();
    }

    @Override
    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid maid, KettleBlockEntity kettleBlockEntity, MaidRecipesManager<KettleRecipe> recManager) {
        ItemStackHandler inventory = kettleBlockEntity.getInventory();

        // 输出槽有未取出的物品
        if (!inventory.getStackInSlot(KettleBlockEntity.OUTPUT_SLOT).isEmpty()) {
            return true;
        }

        boolean innerCanCook = kettleBlockEntity.isHeated() && ((ICbeAccessor) kettleBlockEntity).tlmk$innerCanCook();

        // 存在输出流体，待容器取出
        Fluid outputFluid = kettleBlockEntity.getOutput().getFluid();
        List<ItemStack> outputFluidContainers = FLUID_CONTAINERS.getOrDefault(outputFluid, Collections.emptyList());
        if (!innerCanCook && recManager.hasOutputAdditionItem(itemStack -> outputFluidContainers.stream().anyMatch(stack -> stack.is(itemStack.getItem())))) {
            return true;
        }

        // 容器内部没有符合烹饪的原材料&&仓库存在可以烹饪的原材料
        if (!innerCanCook && !recManager.getRecipesIngredients().isEmpty()) {
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
        Fluid fluid = kettleBlockEntity.getFluidTank().getFluid().getFluid();
        List<ItemStack> fluidContainers = FLUID_CONTAINERS.getOrDefault(fluid, Collections.emptyList());
        if (!innerCanCook && recManager.hasOutputAdditionItem(itemStack -> fluidContainers.stream().anyMatch(stack -> stack.is(itemStack.getItem())))) {
            return true;
        }

        return false;
    }

    @Override
    public void processCookMake(ServerLevel serverLevel, EntityMaid maid, KettleBlockEntity kettleBlockEntity, MaidRecipesManager<KettleRecipe> recManager) {
        kettleBlockEntity.setChanged();

        ItemStackHandler inventory = kettleBlockEntity.getInventory();
        IItemHandlerModifiable inputInv = recManager.getInputInv();
        IItemHandlerModifiable outputInv = recManager.getOutputInv();
        IItemHandlerModifiable outputAdditionInv = recManager.getOutputAdditionInv();

        // 输出槽有未取出的物品
        ItemStack output = inventory.getStackInSlot(KettleBlockEntity.OUTPUT_SLOT);
        if (!output.isEmpty()) {
            ItemStack outputCopy = output.copy();
            ItemStack leftItemStack = ItemHandlerHelper.insertItemStacked(outputInv, outputCopy, false);
            output.shrink(outputCopy.getCount() - leftItemStack.getCount());

            kettleBlockEntity.setChanged();
        }

        // 存在容器
        ItemStack container = inventory.getStackInSlot(KettleBlockEntity.CONTAINER_SLOT);
        if (!container.isEmpty()) {
            ItemStack containerCopy = container.copy();
            ItemStack leftItemStack = ItemHandlerHelper.insertItemStacked(outputAdditionInv, containerCopy, false);
            container.shrink(containerCopy.getCount() - leftItemStack.getCount());

            kettleBlockEntity.setChanged();
        }

        boolean innerCanCook = kettleBlockEntity.isHeated() && ((ICbeAccessor) kettleBlockEntity).tlmk$innerCanCook();

        // 存在输出流体，待容器取出
        Fluid outputFluid = kettleBlockEntity.getOutput().getFluid();
        List<ItemStack> outputFluidContainers = FLUID_CONTAINERS.getOrDefault(outputFluid, Collections.emptyList());
        ItemStack outputAdditionItem = recManager.findOutputAdditionItem(itemStack -> outputFluidContainers.stream().anyMatch(stack -> stack.is(itemStack.getItem())));
        if (!innerCanCook && !outputAdditionItem.isEmpty()) {
            inventory.insertItem(KettleBlockEntity.CONTAINER_SLOT, outputAdditionItem, false);

            // 存在容器
            ItemStack container2 = inventory.getStackInSlot(KettleBlockEntity.DRINK_DISPLAY_SLOT);
            if (!container2.isEmpty()) {
                ItemStack containerCopy = container2.copy();
                ItemStack leftItemStack = ItemHandlerHelper.insertItemStacked(outputAdditionInv, containerCopy, false);
                container2.shrink(containerCopy.getCount() - leftItemStack.getCount());
            }

            // 输出槽有未取出的物品
            ItemStack output2 = inventory.getStackInSlot(KettleBlockEntity.OUTPUT_SLOT);
            if (!output2.isEmpty()) {
                ItemStack outputCopy = output2.copy();
                ItemStack leftItemStack = ItemHandlerHelper.insertItemStacked(outputInv, outputCopy, false);
                output2.shrink(outputCopy.getCount() - leftItemStack.getCount());
            }

            kettleBlockEntity.setChanged();
        }

//         容器内部没有符合烹饪的原材料&&容器内部存在余下的材料
//         容器内部没有符合烹饪的原材料&&仓库存在可以烹饪的原材料
        boolean hasInput = false;
        if (!innerCanCook) {
            for (int i = 0; i < 2; i++) {
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

            kettleBlockEntity.setChanged();
        }

        if (!innerCanCook && !hasInput && kettleBlockEntity.getFluidTank().isEmpty() && !recManager.getRecipesIngredients().isEmpty()) {
            Pair<List<Integer>, List<List<ItemStack>>> recipeIngredient = recManager.getRecipeIngredient();
            if (hasEnoughIngredient(recipeIngredient.getFirst(), recipeIngredient.getSecond())) {

                int amount = recipeIngredient.getFirst().get(0);
                for (ItemStack itemStack : recipeIngredient.getSecond().get(0)) {
                    if (itemStack.isEmpty()) continue;
                    int count = itemStack.getCount();

                    if (count >= amount) {
                        for (int i = 0; i < amount; i++) {
                            ItemStack fluidExtract = kettleBlockEntity.fluidExtract(kettleBlockEntity, itemStack.copyWithCount(1), ItemStack.EMPTY);
                            ItemStack leftInsertedStack = ItemHandlerHelper.insertItemStacked(inputInv, fluidExtract, false);
                            itemStack.shrink(1 - leftInsertedStack.getCount());
                        }

                        ItemStack leftInsertedStack = inventory.insertItem(KettleBlockEntity.CONTAINER_SLOT, itemStack.copyWithCount(amount), false);
                        itemStack.shrink(amount - leftInsertedStack.getCount());
                        break;
                    } else {
                        for (int i = 0; i < count; i++) {
                            ItemStack fluidExtract = kettleBlockEntity.fluidExtract(kettleBlockEntity, itemStack.copyWithCount(1), ItemStack.EMPTY);
                            ItemStack leftInsertedStack = ItemHandlerHelper.insertItemStacked(inputInv, fluidExtract, false);
                            itemStack.shrink(1 - leftInsertedStack.getCount());
                        }
                        amount -= count;
                        if (amount <= 0) {
                            break;
                        }
                    }
                }

                this.insertInputsStack(inventory, inputInv, kettleBlockEntity, recipeIngredient);
                kettleBlockEntity.setChanged();
            }
        }
    }

    private boolean hasEnoughIngredient(List<Integer> amounts, List<List<ItemStack>> ingredients) {
        boolean canInsert = true;

        int i = 0;
        for (List<ItemStack> ingredient : ingredients) {
            int actualCount = amounts.get(i++);
            for (ItemStack itemStack : ingredient) {
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

    private void insertInputsStack(ItemStackHandler beInv, IItemHandlerModifiable ingreInputsInv, KettleBlockEntity be, Pair<List<Integer>, List<List<ItemStack>>> ingredientPair) {
        List<Integer> amounts = ingredientPair.getFirst();
        List<List<ItemStack>> ingredients = ingredientPair.getSecond();

        for (int i = 0, j = 0; i < ingredients.size() - 1; i++, j++) {
            insertAndShrink(beInv, amounts.get(j), ingredients, j + 1, i);
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
    public List<KettleRecipe> getRecipes(Level level) {
        KEY_RECIPE_INGREDIENTS.clear();

        if (KEY_RECIPE_INGREDIENTS.isEmpty()) {
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
            List<KettleRecipe> kettleRecipes = level.getRecipeManager().getAllRecipesFor(FRRecipeTypes.BREWING.get());
            List<KettlePouringRecipe> kettlePouringRecipes = level.getRecipeManager().getAllRecipesFor(FRRecipeTypes.KETTLE_POURING.get());

            for (KettleRecipe kettleRecipe : kettleRecipes) {
                // 输入的流体
                FluidStack fluidIn = kettleRecipe.getFluidIn();
                if (fluidIn != null && !fluidIn.isEmpty()) {
                    // 输入的流体容器
                    if (!FLUID_CONTAINERS.containsKey(fluidIn.getFluid())) {
                        List<KettlePouringRecipe> matchKegPouringRecipes = kettlePouringRecipes.stream()
                                .filter(pouringRecipe -> pouringRecipe.getFluid().isSame(fluidIn.getFluid()))
                                .toList();
                        List<ItemStack> list = matchKegPouringRecipes.stream()
                                .map(KettlePouringRecipe::getContainer)
                                .filter(itemStack -> !itemStack.isEmpty())
                                .toList();

                        if (!list.isEmpty()) {
                            FLUID_CONTAINERS.put(fluidIn.getFluid(), Lists.newArrayList(list));
                        }

                    }

                    // 输入的流体所对应的ItemStack
                    List<ItemStack> fluidItems = fluidItemStacks.computeIfAbsent(fluidIn.getFluid(), fluid -> {
                        return kettlePouringRecipes.stream()
                                .filter(pouringRecipe -> pouringRecipe.getFluid().isSame(fluid))
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


                    MaidKettleRecipe maidKettleRecipe = new MaidKettleRecipe(fluidItems, kettleRecipe.getIngredients());
                    KEY_RECIPE_INGREDIENTS.put(kettleRecipe, maidKettleRecipe);
                } else {
                    MaidKettleRecipe maidKettleRecipe = new MaidKettleRecipe(Collections.emptyList(), kettleRecipe.getIngredients());
                    KEY_RECIPE_INGREDIENTS.put(kettleRecipe, maidKettleRecipe);
                }

                // 输出的流体容器
                FluidStack fluidOut = kettleRecipe.getFluidOut();
                if (fluidOut != null && !fluidOut.isEmpty() && !FLUID_CONTAINERS.containsKey(fluidOut.getFluid())) {
                    List<KettlePouringRecipe> matchKegPouringRecipes = kettlePouringRecipes.stream()
                            .filter(pouringRecipe -> pouringRecipe.getFluid().isSame(fluidOut.getFluid()))
                            .toList();

                    List<ItemStack> list = matchKegPouringRecipes.stream()
                            .map(KettlePouringRecipe::getContainer)
                            .filter(container -> !container.isEmpty())
                            .toList();
                    FLUID_CONTAINERS.put(fluidOut.getFluid(), Lists.newArrayList(list));
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
        return KEY_RECIPE_INGREDIENTS.keySet().stream().toList();
    }

    @Override
    public MaidRecipesManager<KettleRecipe> getRecipesManager(EntityMaid maid) {
        return new KettleRecipesManager(maid, this);
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.FR_KETTLE.uid;
    }

    @Override
    public ItemStack getIcon() {
        return FRItems.KETTLE.get().getDefaultInstance();
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.FR_KETTLE;
    }

    public record MaidKettleRecipe(List<ItemStack> inFluids, List<Ingredient> inItems) {
    }
}
