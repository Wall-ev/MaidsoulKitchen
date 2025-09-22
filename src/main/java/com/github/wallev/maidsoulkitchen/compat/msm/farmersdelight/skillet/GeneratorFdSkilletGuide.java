package com.github.wallev.maidsoulkitchen.compat.msm.farmersdelight.skillet;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.cookingpot.IFdCookingPotGuideGenerator;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.GuideTest;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;
import vectorwing.farmersdelight.common.registry.ModItems;

//@GuideTest
@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_FD_SKILLET)
public class GeneratorFdSkilletGuide implements IFdCookingPotGuideGenerator<CampfireCookingRecipe, SkilletBlockEntity> {
//public class GeneratorFdSkilletGuide implements IFdCookingPotGuideGenerator<CampfireCookingRecipe, Container, SkilletBlockEntity> {
    @Override
    public boolean isValidBlockEntity(BlockEntity be) {
        return be instanceof SkilletBlockEntity;
    }

    @Override
    public boolean isHeated(SkilletBlockEntity be) {
        return be.isHeated();
    }

    @Override
    public @NotNull ResourceLocation getType() {
        return VResourceLocation.createTypeMod(Mods.FD, "skillet");
    }

    @Override
    public RecipeType<CampfireCookingRecipe> getRecipeType() {
        return RecipeType.CAMPFIRE_COOKING;
    }

    @Override
    public int getRecipeTime(CampfireCookingRecipe recipe) {
        return recipe.getCookingTime();
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ModItems.SKILLET.get();
    }
}
