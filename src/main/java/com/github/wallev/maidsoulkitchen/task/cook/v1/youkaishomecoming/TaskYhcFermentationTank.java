package com.github.wallev.maidsoulkitchen.task.cook.v1.youkaishomecoming;

import com.github.wallev.maidsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.RegisterData;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.action.IMaidAction;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.base.Preconditions;
import com.mojang.datafixers.util.Pair;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeFluid;
import dev.xkmc.youkaishomecoming.content.pot.ferment.FermentationDummyContainer;
import dev.xkmc.youkaishomecoming.content.pot.ferment.FermentationRecipe;
import dev.xkmc.youkaishomecoming.content.pot.ferment.FermentationTankBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.ferment.SimpleFermentationRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static dev.xkmc.youkaishomecoming.content.pot.ferment.FermentationTankBlock.OPEN;
import static net.minecraftforge.fluids.FluidUtil.*;

public class TaskYhcFermentationTank implements ICookTask<FermentationTankBlockEntity, FermentationRecipe<?>>, IMaidAction {
    public static boolean interactWithFluidHandler(@NotNull EntityMaid maid, ItemStack fluidStack, @NotNull Level level, @NotNull BlockPos pos, @Nullable Direction side) {
        Preconditions.checkNotNull(level);
        Preconditions.checkNotNull(pos);

        return getFluidHandler(level, pos, side).map(handler -> interactWithFluidHandler(maid, fluidStack, handler)).orElse(false);
    }

    public static boolean interactWithFluidHandler(@NotNull EntityMaid maid, ItemStack fluidStack, @NotNull IFluidHandler handler) {
        ItemStack heldItem = fluidStack;
        if (!heldItem.isEmpty()) {
            CombinedInvWrapper availableInv = maid.getAvailableInv(true);

            FluidActionResult fluidActionResult = tryFillContainerAndStow(heldItem, handler, availableInv, Integer.MAX_VALUE, null, true);
            if (!fluidActionResult.isSuccess()) {
                fluidActionResult = tryEmptyContainerAndStow(heldItem, handler, availableInv, Integer.MAX_VALUE, null, true);
            }

            if (fluidActionResult.isSuccess()) {

                heldItem.shrink(1);

                ItemStack result = fluidActionResult.getResult();
                ItemHandlerHelper.insertItemStacked(availableInv, result, false);
                return true;
            }
            return false;

        }
        return false;
    }

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

    @SuppressWarnings("all")
    @Override
    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid maid, FermentationTankBlockEntity blockEntity, MaidRecipesManager<FermentationRecipe<?>> recManager) {
        CombinedInvWrapper maidInv = maid.getAvailableInv(true);
        Fluid fluid = blockEntity.fluids.getFluidInTank(0).getFluid();
        if (fluid instanceof SakeFluid sakeFluid) {
            Item container = sakeFluid.type.getContainer();
            int outputAdditionItemCount = recManager.getOutputAdditionItemCount(container.getDefaultInstance());
            if (outputAdditionItemCount > 0) {
                return true;
            }
        }

        if (!blockEntity.items.isEmpty()) {
            FermentationDummyContainer cont = new FermentationDummyContainer(blockEntity.items, blockEntity.fluids);
            Optional<FermentationRecipe<?>> opt = maid.level.getRecipeManager().getRecipeFor((RecipeType) YHBlocks.FERMENT_RT.get(), cont, maid.level);
            if (opt.isEmpty()) {
                return true;
            }
        }

        boolean hasEnoughWater = !blockEntity.fluids.isEmpty();
        if (!hasEnoughWater) {
            for (int i = 0; i < maidInv.getSlots(); i++) {
                ItemStack stackInSlot = maidInv.getStackInSlot(i);
                if (stackInSlot.isEmpty() || stackInSlot.is(Items.BUCKET)) continue;
                LazyOptional<IFluidHandlerItem> opt = stackInSlot.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
                if (opt.resolve().isPresent()) {
                    return true;
                }
            }
        }

        if (hasEnoughWater && blockEntity.inProgress() == 0) {
            if (blockEntity.items.isEmpty() && !recManager.getRecipesIngredients().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("all")
    @Override
    public void processCookMake(ServerLevel serverLevel, EntityMaid maid, FermentationTankBlockEntity blockEntity, MaidRecipesManager<FermentationRecipe<?>> recManager) {
        CombinedInvWrapper maidInv = maid.getAvailableInv(true);

        IItemHandlerModifiable outputInv = recManager.getOutputInv();

        FluidStack fluidInTank = blockEntity.fluids.getFluidInTank(0);
        Fluid fluid = fluidInTank.getFluid();
        if (fluid instanceof SakeFluid sakeFluid) {
            Item container = sakeFluid.type.getContainer();
            int outputAdditionItemCount = recManager.getOutputAdditionItemCount(container.getDefaultInstance());
            if (outputAdditionItemCount > 0) {
                int takeAmount = Math.min(fluidInTank.getAmount() / sakeFluid.type.amount(), outputAdditionItemCount);

                ItemStack leftStack = ItemHandlerHelper.insertItemStacked(outputInv, sakeFluid.type.asStack(takeAmount), false);
                recManager.shrinkOutputAdditionItem(container.getDefaultInstance(), takeAmount - leftStack.getCount());
                blockEntity.fluids.drain(sakeFluid.type.amount() * takeAmount, IFluidHandler.FluidAction.EXECUTE);

                blockEntity.notifyTile();

                pickupAction(maid);
            }
        }

        if (blockEntity.fluids.isEmpty()) {
            for (int i = 0; i < maidInv.getSlots(); i++) {
                ItemStack stackInSlot = maidInv.getStackInSlot(i);
                LazyOptional<IFluidHandlerItem> opt = stackInSlot.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
                if (opt.resolve().isPresent()) {
                    if (interactWithFluidHandler(maid, stackInSlot, serverLevel, blockEntity.getBlockPos(), null)) {
                        blockEntity.notifyTile();
                    }
                }
            }
        }

        if (!blockEntity.items.isEmpty()) {
            FermentationDummyContainer cont = new FermentationDummyContainer(blockEntity.items, blockEntity.fluids);
            Optional<FermentationRecipe<?>> opt = maid.level.getRecipeManager().getRecipeFor((RecipeType) YHBlocks.FERMENT_RT.get(), cont, maid.level);
            if (opt.isEmpty()) {
                blockEntity.dumpInventory();
            }
        }

        if (!blockEntity.fluids.isEmpty() && blockEntity.inProgress() == 0) {
            if (!recManager.getRecipesIngredients().isEmpty()) {
                Pair<List<Integer>, List<List<ItemStack>>> recipeIngredient = recManager.getRecipeIngredient();
                if (recipeIngredient.getFirst().isEmpty()) return;

                for (List<ItemStack> itemStacks : recipeIngredient.getSecond()) {
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
    public NonNullList<Ingredient> getIngredients(Recipe<?> recipe) {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.addAll(((SimpleFermentationRecipe) recipe).ingredients);
        return ingredients;
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
}
