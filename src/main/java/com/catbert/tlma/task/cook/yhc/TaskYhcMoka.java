package com.catbert.tlma.task.cook.yhc;

import com.catbert.tlma.TLMAddon;
import com.catbert.tlma.foundation.utility.Mods;
import com.catbert.tlma.task.cook.common.TaskFdPot;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import dev.xkmc.youkaishomecoming.content.pot.moka.MokaMakerBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.moka.MokaRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

@LittleMaidExtension
public class TaskYhcMoka extends TaskFdPot<MokaMakerBlockEntity, MokaRecipe> {
    public static final ResourceLocation NAME = new ResourceLocation(TLMAddon.MOD_ID, "yhc_moka_pot");

    @Override
    public boolean canLoaded() {
        return Mods.YHC.isLoaded;
    }

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
        return NAME;
    }

    @Override
    public ItemStack getIcon() {
        return YHBlocks.MOKA.asStack();
    }
}
