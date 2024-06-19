package com.github.catbert.tlma.task.cook.v1.beachparty;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.foundation.utility.Mods;
import com.github.catbert.tlma.task.cook.v1.common.TaskLdBaseContainerCook;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import satisfy.beachparty.block.entity.MiniFridgeBlockEntity;
import satisfy.beachparty.recipe.MiniFridgeRecipe;
import satisfy.beachparty.registry.ObjectRegistry;
import satisfy.beachparty.registry.RecipeRegistry;

@LittleMaidExtension
public class TaskDbpMiniFridge extends TaskLdBaseContainerCook<MiniFridgeBlockEntity, MiniFridgeRecipe> {
    public static final ResourceLocation NAME = new ResourceLocation(TLMAddon.MOD_ID, "dbp_mini_fridge");

    @Override
    public boolean canLoaded() {
        return Mods.DBP.isLoaded;
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
        return 2;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof MiniFridgeBlockEntity;
    }

    @Override
    public RecipeType<MiniFridgeRecipe> getRecipeType() {
        return RecipeRegistry.MINI_FRIDGE_RECIPE_TYPE.get();
    }

    @Override
    public ResourceLocation getUid() {
        return NAME;
    }

    @Override
    public ItemStack getIcon() {
        return ObjectRegistry.MINI_FRIDGE.get().asItem().getDefaultInstance();
    }

    @Override
    public boolean isHeated(MiniFridgeBlockEntity be) {
        return true;
    }

    @Override
    public Container getContainer(MiniFridgeBlockEntity be) {
        return be;
    }
}
