package com.catbert.tlma.task.cook.bnc;

import com.catbert.tlma.TLMAddon;
import com.catbert.tlma.foundation.utility.Mods;
import com.catbert.tlma.mixin.bnc.KegBlockEntityAccessor;
import com.catbert.tlma.task.ai.brain.MaidCookMakeTask;
import com.catbert.tlma.task.ai.brain.MaidCookMoveTask;
import com.catbert.tlma.task.cook.common.TaskFdCiCook;
import com.catbert.tlma.task.cook.handler.MaidRecipesManager;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;
import umpaz.brewinandchewin.common.crafting.KegRecipe;
import umpaz.brewinandchewin.common.registry.BCBlocks;
import umpaz.brewinandchewin.common.registry.BCRecipeTypes;

import java.util.*;

import static com.catbert.tlma.TLMAddon.LOGGER;

@LittleMaidExtension
public class TaskBncKey extends TaskFdCiCook<KegBlockEntity, KegRecipe> {

    public static final ResourceLocation NAME = new ResourceLocation(TLMAddon.MOD_ID, "bnc_key");

    @Override
    public boolean canLoaded() {
        return Mods.BNC.isLoaded;
    }

    @Override
    public ItemStackHandler getItemStackHandler(KegBlockEntity be) {
        return be.getInventory();
    }

    @Override
    public Optional<KegRecipe> getMatchingRecipe(KegBlockEntity be, RecipeWrapper recipeWrapper) {
        return ((KegBlockEntityAccessor) be).getMatchingRecipe$tlma(recipeWrapper);
    }

    @Override
    public boolean canCook(KegBlockEntity be, KegRecipe recipe) {
        return ((KegBlockEntityAccessor) be).canCook$tlma(recipe, be.getLevel());
    }

    @Override
    public int getOutputSlot() {
        return KegBlockEntity.OUTPUT_SLOT;
    }

    @Override
    public int getInputSize() {
        return 5;
    }

    @Override
    public int getMealStackSlot() {
        return KegBlockEntity.DRINK_DISPLAY_SLOT;
    }

    @Override
    public int getContainerStackSlot() {
        return KegBlockEntity.CONTAINER_SLOT;
    }

    @Override
    public ItemStack getFoodContainer(KegBlockEntity blockEntity) {
        return blockEntity.getContainer();
    }

    @Override
    public boolean isHeated(KegBlockEntity be) {
        return true;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof KegBlockEntity;
    }

    @Override
    public RecipeType<KegRecipe> getRecipeType() {
        return BCRecipeTypes.FERMENTING.get();
    }

    @Override
    public ResourceLocation getUid() {
        return NAME;
    }

    @Override
    public ItemStack getIcon() {
        return BCBlocks.KEG.get().asItem().getDefaultInstance();
    }
}
