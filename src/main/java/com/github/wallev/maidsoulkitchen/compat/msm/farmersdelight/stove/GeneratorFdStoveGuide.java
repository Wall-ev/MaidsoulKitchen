package com.github.wallev.maidsoulkitchen.compat.msm.farmersdelight.stove;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.cookingpot.IFdCookingPotGuideGenerator;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.GuideTest;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.block.entity.StoveBlockEntity;
import vectorwing.farmersdelight.common.registry.ModItems;

//@GuideTest
@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_FD_STOVE)
public class GeneratorFdStoveGuide implements IFdCookingPotGuideGenerator<CampfireCookingRecipe, StoveBlockEntity> {
//public class GeneratorFdStoveGuide implements IFdCookingPotGuideGenerator<CampfireCookingRecipe, Container, StoveBlockEntity> {
    @Override
    public boolean isValidBlockEntity(BlockEntity be) {
        return be instanceof StoveBlockEntity;
    }

    @Override
    public boolean isHeated(StoveBlockEntity be) {
        Level level = be.getLevel();
        BlockPos blockPos = be.getBlockPos();

        return level.getBlockState(blockPos).getValue(StoveBlock.LIT);
    }

    @Override
    public RecipeType<CampfireCookingRecipe> getRecipeType() {
        return RecipeType.CAMPFIRE_COOKING;
    }

    @Override
    public @NotNull ResourceLocation getType() {
        return VResourceLocation.createTypeMod(Mods.FD, this.getRecipeType().toString());
    }

    @Override
    public int getRecipeTime(CampfireCookingRecipe recipe) {
        return recipe.getCookingTime();
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ModItems.STOVE.get();
    }
}
