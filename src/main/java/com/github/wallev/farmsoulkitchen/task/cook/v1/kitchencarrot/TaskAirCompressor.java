package com.github.wallev.farmsoulkitchen.task.cook.v1.kitchencarrot;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import com.github.wallev.farmsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.wallev.farmsoulkitchen.api.task.v1.cook.IHandlerCookBe;
import com.github.wallev.farmsoulkitchen.api.task.v1.cook.IItemHandlerCook;
import com.github.wallev.farmsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.farmsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.farmsoulkitchen.mixin.kitchkarrot.AirCompressorBlockEntityAccessor;
import com.github.wallev.farmsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.mojang.datafixers.util.Pair;
import io.github.tt432.kitchenkarrot.blockentity.AirCompressorBlockEntity;
import io.github.tt432.kitchenkarrot.recipes.recipe.AirCompressorRecipe;
import io.github.tt432.kitchenkarrot.registries.ModBlocks;
import io.github.tt432.kitchenkarrot.registries.RecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.List;

public class TaskAirCompressor implements ICookTask<AirCompressorBlockEntity, AirCompressorRecipe>, IHandlerCookBe<AirCompressorBlockEntity>, IItemHandlerCook<AirCompressorBlockEntity, AirCompressorRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(FarmsoulKitchen.MOD_ID, "kk_air_compressor");

    private static void replenishEnergy(EntityMaid maid, AirCompressorBlockEntity brewBe, CombinedInvWrapper maidInv) {
        int energyItemSlot = ItemsUtil.findStackSlot(maidInv, stack -> stack.is(Items.REDSTONE));
        if (energyItemSlot > -1) {
            ItemStack stackInSlot = maidInv.getStackInSlot(energyItemSlot);
            ItemHandlerHelper.insertItemStacked(brewBe.getInput2(), stackInSlot.split(1), false);
        }
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.KC_AIR_COMPRESSOR;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof AirCompressorBlockEntity;
    }

    @Override
    public RecipeType<AirCompressorRecipe> getRecipeType() {
        return RecipeTypes.AIR_COMPRESSOR.get();
    }

    @Override
    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid maid, AirCompressorBlockEntity blockEntity, MaidRecipesManager<AirCompressorRecipe> recManager) {
        CombinedInvWrapper maidInv = maid.getAvailableInv(true);
        AirCompressorBlockEntityAccessor accessor = (AirCompressorBlockEntityAccessor) blockEntity;

        if (!blockEntity.getOutput().getStackInSlot(0).isEmpty()) {
            return true;
        }

        boolean findChargeItem = ItemsUtil.findStackSlot(maidInv, stack -> stack.is(Items.REDSTONE)) > -1;
        if (!accessor.callIsStarted() && !recManager.getRecipesIngredients().isEmpty()) {
            return accessor.callHasEnergy() || findChargeItem;
        }

        if (accessor.callGetRecipeFromItems() != null && !accessor.callHasEnergy() && findChargeItem) {
            return true;
        }

        if (!accessor.callIsStarted() && hasInput(blockEntity.getInput1())) {
            return true;
        }

        return false;
    }

    @Override
    public void processCookMake(ServerLevel serverLevel, EntityMaid maid, AirCompressorBlockEntity blockEntity, MaidRecipesManager<AirCompressorRecipe> recManager) {
        CombinedInvWrapper maidInv = maid.getAvailableInv(true);
        AirCompressorBlockEntityAccessor accessor = (AirCompressorBlockEntityAccessor) blockEntity;

        if (!accessor.callHasEnergy()) {
            replenishEnergy(maid, blockEntity, maidInv);
        }

        if (!blockEntity.getOutput().getStackInSlot(0).isEmpty()) {
            extractOutputStack(blockEntity.getOutput(), recManager.getOutputInv(), blockEntity);
        }
        pickupAction(maid);

        if (!accessor.callIsStarted() && hasInput(blockEntity.getInput1())) {
            extractInputsStack(blockEntity.getInput1(), recManager.getInputInv(), blockEntity);
        }

        if (!accessor.callIsStarted() && accessor.callHasEnergy() && !recManager.getRecipesIngredients().isEmpty()) {
            Pair<List<Integer>, List<List<ItemStack>>> recipeIngredient = recManager.getRecipeIngredient();
            if (recipeIngredient.getFirst().isEmpty()) return;
            insertInputsStack(blockEntity.getInput1(), maidInv, blockEntity, recipeIngredient);
        }

        pickupAction(maid);
        recManager.getCookInv().syncInv();
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return ModBlocks.AIR_COMPRESSOR.get().asItem().getDefaultInstance();
    }

    @Override
    public ItemStackHandler getItemStackHandler(AirCompressorBlockEntity airCompressorBlockEntity) {
        return airCompressorBlockEntity.getInput1();
    }

    @Override
    public int getOutputSlot() {
        return 0;
    }

    @Override
    public int getInputSize() {
        return 5;
    }

    @Override
    public ItemStackHandler getBeInv(AirCompressorBlockEntity airCompressorBlockEntity) {
        return airCompressorBlockEntity.getInput1();
    }

    @Override
    public NonNullList<Ingredient> getIngredients(Recipe<?> recipe) {
        AirCompressorRecipe airCompressorRecipe = (AirCompressorRecipe) recipe;
        NonNullList<Ingredient> ingredient = airCompressorRecipe.getIngredient();
        Ingredient container = airCompressorRecipe.getContainer();
        if (container != null) {
            NonNullList<Ingredient> ingredients = NonNullList.create();
            ingredients.addAll(ingredient);
            ingredients.add(container);
            return ingredients;
        } else {
            return ingredient;
        }
    }
}
