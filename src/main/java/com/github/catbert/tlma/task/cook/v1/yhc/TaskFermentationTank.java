package com.github.catbert.tlma.task.cook.v1.yhc;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.entity.data.inner.task.CookData;
import com.github.catbert.tlma.task.cook.v1.common.TaskFdPot;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import dev.xkmc.youkaishomecoming.content.pot.ferment.FermentationTankBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.ferment.SimpleFermentationRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

public class TaskFermentationTank extends TaskFdPot<FermentationTankBlockEntity, SimpleFermentationRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(TLMAddon.MOD_ID, "yhc_fermentation_tank");
    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return null;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return false;
    }

    @Override
    public RecipeType<SimpleFermentationRecipe> getRecipeType() {
        return null;
    }

    @Override
    public int getMealStackSlot() {
        return 0;
    }

    @Override
    public int getContainerStackSlot() {
        return 0;
    }

    @Override
    public ItemStack getFoodContainer(FermentationTankBlockEntity blockEntity) {
        return null;
    }

    @Override
    public ItemStackHandler getItemStackHandler(FermentationTankBlockEntity be) {
        return null;
    }

    @Override
    public int getOutputSlot() {
        return 0;
    }

    @Override
    public int getInputSize() {
        return 0;
    }

    @Override
    public boolean isHeated(FermentationTankBlockEntity be) {
        return true;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return YHBlocks.FERMENT.asStack();
    }
}
