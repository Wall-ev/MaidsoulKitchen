package com.github.wallev.farmsoulkitchen.task.cook.v1.yhc;

import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import com.github.wallev.farmsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.farmsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.farmsoulkitchen.task.cook.v1.common.TaskFdPot;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import dev.xkmc.youkaishomecoming.content.pot.moka.MokaMakerBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.moka.MokaRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;


public class TaskYhcMoka extends TaskFdPot<MokaMakerBlockEntity, MokaRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(FarmsoulKitchen.MOD_ID, "yhc_moka_pot");

    @Override
    public ItemStackHandler getItemStackHandler(MokaMakerBlockEntity be) {
        return be.getInventory();
    }

    @Override
    public int getOutputSlot() {
        return MokaMakerBlockEntity.OUTPUT_SLOT;
    }

    @Override
    public int getInputSize() {
        return 4;
    }

    @Override
    public int getMealStackSlot() {
        return MokaMakerBlockEntity.MEAL_DISPLAY_SLOT;
    }

    @Override
    public int getContainerStackSlot() {
        return MokaMakerBlockEntity.CONTAINER_SLOT;
    }

    @Override
    public ItemStack getFoodContainer(MokaMakerBlockEntity blockEntity) {
        return blockEntity.getContainer();
    }

    @Override
    public boolean isHeated(MokaMakerBlockEntity be) {
        return be.isHeated();
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof MokaMakerBlockEntity;
    }

    @Override
    public RecipeType<MokaRecipe> getRecipeType() {
        return YHBlocks.MOKA_RT.get();
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return YHBlocks.MOKA.asStack();
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.YHC_MOKA;
    }
}
