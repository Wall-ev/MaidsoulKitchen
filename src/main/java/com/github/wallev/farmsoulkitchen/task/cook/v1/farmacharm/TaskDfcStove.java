package com.github.wallev.farmsoulkitchen.task.cook.v1.farmacharm;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.farmsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.farmsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.farmsoulkitchen.task.TaskInfo;
import com.github.wallev.farmsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.github.wallev.farmsoulkitchen.task.cook.v1.bakery.IStoveBe;
import com.github.wallev.farmsoulkitchen.task.cook.v1.common.TaskLdContainerCook;
import com.github.wallev.farmsoulkitchen.task.cook.v1.common.bestate.IFuelBe;
import com.github.wallev.farmsoulkitchen.util.MaidDataUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.satisfy.farm_and_charm.block.entity.StoveBlockEntity;
import net.satisfy.farm_and_charm.recipe.StoveRecipe;
import net.satisfy.farm_and_charm.registry.ObjectRegistry;
import net.satisfy.farm_and_charm.registry.RecipeTypeRegistry;

import java.util.List;

public class TaskDfcStove extends TaskLdContainerCook<StoveBlockEntity, StoveRecipe> {
    protected static final int FUEL_SLOT = 4;

    @Override
    public boolean maidShouldMoveTo(ServerLevel serverLevel, EntityMaid entityMaid, StoveBlockEntity blockEntity, MaidRecipesManager<StoveRecipe> maidRecipesManager) {
        CombinedInvWrapper availableInv = entityMaid.getAvailableInv(true);

        Container inventory = getContainer(blockEntity);
        ItemStack outputStack = inventory.getItem(getOutputSlot());
        // 有最终物品
//        LOGGER.info("outputStack: {} ", outputStack);
        if (!outputStack.isEmpty()) {
            return true;
        }

        // 现在是否可以做饭（厨锅有没有正在做饭）
        boolean b = beInnerCanCook(inventory, blockEntity);
        List<Pair<List<Integer>, List<List<ItemStack>>>> recipesIngredients = maidRecipesManager.getRecipesIngredients();
//        LOGGER.info("recipe: {} {}",  b, recipesIngredients);
        if (!b && !recipesIngredients.isEmpty() && (beHasFuel(blockEntity) || maidHasFuel(availableInv, blockEntity))) {
            return true;
        }

        // 能做饭现在和有输入（也就是厨锅现在有物品再里面但是不符合配方
//        LOGGER.info("hasInput: {} {}", b, hasInput(inventory));
        if (!b && hasInput(inventory)) {
            return true;
        }

        return false;
    }

    protected boolean beHasFuel(StoveBlockEntity blockEntity) {
        return ((IFuelBe)blockEntity).isBurning$tlma() || (( IStoveBe)blockEntity).getTotalBurnTime$tlma(blockEntity.getItem(FUEL_SLOT)) > 0;
    }

    protected boolean maidHasFuel(CombinedInvWrapper availableInv, StoveBlockEntity blockEntity) {
       return MaidDataUtil.findMaidInventoryItemStack(availableInv, itemStack -> ((IStoveBe)blockEntity).getTotalBurnTime$tlma(itemStack) > 0) > -1;
    }

    @Override
    public void maidCookMake(ServerLevel serverLevel, EntityMaid entityMaid, StoveBlockEntity blockEntity, MaidRecipesManager<StoveRecipe> maidRecipesManager) {
        tryExtractItem(serverLevel, entityMaid, blockEntity, maidRecipesManager);
        maidRecipesManager.getCookInv().syncInv();
        if (!fuelItemAction(entityMaid, blockEntity)) return;

        tryInsertItem(serverLevel, entityMaid, blockEntity, maidRecipesManager);
        maidRecipesManager.getCookInv().syncInv();
    }

    protected boolean fuelItemAction(EntityMaid entityMaid, StoveBlockEntity blockEntity) {
        ItemStack item = blockEntity.getItem(FUEL_SLOT);
        if (((IFuelBe)blockEntity).isBurning$tlma() || ((IStoveBe)blockEntity).getTotalBurnTime$tlma(item) > 0) return true;

        CombinedInvWrapper availableInv = entityMaid.getAvailableInv(true);
        if (!item.isEmpty()){
            ItemStack copy = item.copy();
            blockEntity.setItem(FUEL_SLOT, ItemStack.EMPTY);
            ItemHandlerHelper.insertItemStacked(availableInv, copy, false);
            blockEntity.setChanged();
        }
        ItemStack fuelStack = MaidDataUtil.findMaidInventoryStack(availableInv, itemStack -> ((IStoveBe)blockEntity).getTotalBurnTime$tlma(itemStack) > 0);
        if (fuelStack.isEmpty()) {
            return false;
        }else {
            blockEntity.setItem(FUEL_SLOT, fuelStack.copy());
            fuelStack.setCount(0);
            return true;
        }
    }

    @Override
    public boolean isHeated(StoveBlockEntity be) {
        return true;
    }

    @Override
    public int getOutputSlot() {
        return 0;
    }

    @Override
    public int getInputStartSlot() {
        return 1;
    }

    @Override
    public int getInputSize() {
        return 3;
    }

    @Override
    public Container getContainer(StoveBlockEntity be) {
        return be;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof StoveBlockEntity;
    }

    @Override
    public RecipeType<StoveRecipe> getRecipeType() {
        return RecipeTypeRegistry.STOVE_RECIPE_TYPE.get();
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.DFC_STOVE.uid;
    }

    @Override
    public ItemStack getIcon() {
        return ObjectRegistry.STOVE.get().asItem().getDefaultInstance();
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.DFC_STOVE;
    }

}
