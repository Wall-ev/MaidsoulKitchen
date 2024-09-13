package com.github.catbert.tlma.task.cook.v1.herbal;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.foundation.utility.Mods;
import com.github.catbert.tlma.task.cook.v1.common.TaskLdBaseContainerCook;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import satisfy.herbalbrews.blocks.entity.TeaKettleBlockEntity;
import satisfy.herbalbrews.recipe.TeaKettleRecipe;
import satisfy.herbalbrews.registry.ObjectRegistry;
import satisfy.herbalbrews.registry.RecipeTypeRegistry;


public class TaskDhbTeaKettle extends TaskLdBaseContainerCook<TeaKettleBlockEntity, TeaKettleRecipe> {
    public static final ResourceLocation NAME = new ResourceLocation(TLMAddon.MOD_ID, "dhb_tea_kettle");

    @Override
    public boolean isHeated(TeaKettleBlockEntity be) {
        return be.isBeingBurned();
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
        return 0;
    }

    @Override
    public Container getContainer(TeaKettleBlockEntity be) {
        return be;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof TeaKettleBlockEntity;
    }

    @Override
    public RecipeType<TeaKettleRecipe> getRecipeType() {
        return RecipeTypeRegistry.TEA_KETTLE_RECIPE_TYPE.get();
    }

    @Override
    public ResourceLocation getUid() {
        return NAME;
    }

    @Override
    public ItemStack getIcon() {
        return ObjectRegistry.TEA_KETTLE.get().asItem().getDefaultInstance();
    }
}
