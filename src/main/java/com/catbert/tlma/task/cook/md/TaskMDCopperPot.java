package com.catbert.tlma.task.cook.md;

import com.catbert.tlma.TLMAddon;
import com.catbert.tlma.api.task.cook.IMDCook;
import com.catbert.tlma.api.task.cook.ITaskCook;
import com.catbert.tlma.foundation.utility.Mods;
import com.catbert.tlma.task.ai.brain.MaidCookMakeTask;
import com.catbert.tlma.task.ai.brain.MaidCookMoveTask;
import com.catbert.tlma.task.cook.handler.MaidRecipesManager;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.sammy.minersdelight.content.block.copper_pot.CopperPotBlockEntity;
import com.sammy.minersdelight.setup.MDBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import java.util.ArrayList;
import java.util.List;

import static com.catbert.tlma.TLMAddon.LOGGER;

@LittleMaidExtension
public class TaskMDCopperPot implements ITaskCook<CookingPotRecipe, CopperPotBlockEntity>, IMDCook<CookingPotRecipe> {
    public static final ResourceLocation NAME = new ResourceLocation(TLMAddon.MOD_ID, "md_copper_pot");

    @Override
    public boolean modLoaded() {
        return Mods.MD.isLoaded;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof CopperPotBlockEntity;
    }

    @Override
    public RecipeType<CookingPotRecipe> getRecipeType() {
        return ModRecipeTypes.COOKING.get();
    }

    @Override
    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid entityMaid, CopperPotBlockEntity blockEntity, MaidRecipesManager<CookingPotRecipe> maidRecipesManager) {
        return maidShouldMoveTo(serverLevel, entityMaid, blockEntity, maidRecipesManager);
    }

    @Override
    public void processCookMake(ServerLevel serverLevel, EntityMaid entityMaid, CopperPotBlockEntity blockEntity, MaidRecipesManager<CookingPotRecipe> maidRecipesManager) {
        maidCookMake(serverLevel, entityMaid, blockEntity, maidRecipesManager);
    }

    @Override
    public ResourceLocation getUid() {
        return NAME;
    }

    @Override
    public ItemStack getIcon() {
        return MDBlocks.COPPER_POT.asStack();
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        if (maid.level().isClientSide) return new ArrayList<>();
        LOGGER.info("create brain tasks: " + maid.level() + " " + maid + " " + maid.level().isClientSide);

        MaidRecipesManager<CookingPotRecipe> cookingPotRecipeMaidRecipesManager = new MaidRecipesManager<>(maid, getRecipeType(), false) {
            @Override
            protected List<CookingPotRecipe> filterRecipes(List<CookingPotRecipe> recipes) {
                return recipes.stream().filter(cookingPotRecipe -> {
                    return cookingPotRecipe.getIngredients().size() <= 4;
                }).toList();
            }
        };
        MaidCookMoveTask<CookingPotRecipe, CopperPotBlockEntity> maidCookMoveTask = new MaidCookMoveTask<>(maid, this, cookingPotRecipeMaidRecipesManager);
        MaidCookMakeTask<CookingPotRecipe, CopperPotBlockEntity> maidCookMakeTask = new MaidCookMakeTask<>(this, cookingPotRecipeMaidRecipesManager);
        return Lists.newArrayList(Pair.of(5, maidCookMoveTask), Pair.of(6, maidCookMakeTask));
    }

    @Override
    public int getOutputSlot() {
        return CopperPotBlockEntity.OUTPUT_SLOT;
    }

    @Override
    public int getInputEndSlot() {
        return 3;
    }

    @Override
    public int getMealStackSlot() {
        return CopperPotBlockEntity.MEAL_DISPLAY_SLOT;
    }

    @Override
    public int getContainerStackSlot() {
        return CopperPotBlockEntity.CONTAINER_SLOT;
    }
}
