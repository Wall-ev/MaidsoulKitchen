package com.github.wallev.maidsoulkitchen.compat.msm.bakeries.blender;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.cookingpot.ILdCookingPotGuideGenerator;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.GuideTest;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.renyigesai.bakeries.block.blender.BlenderBlock;
import com.renyigesai.bakeries.block.blender.BlenderBlockEntity;
import com.renyigesai.bakeries.init.BakeriesBlocks;
import com.renyigesai.bakeries.init.BakeriesItems;
import com.renyigesai.bakeries.recipe.BlenderRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

//@GuideTest
@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_BAKERIES_BLENDER)
public class GeneratorBakeriesBlenderGuide implements ILdCookingPotGuideGenerator<BlenderRecipe, BlenderBlockEntity> {
//public class GeneratorBakeriesBlenderGuide implements ILdCookingPotGuideGenerator<BlenderRecipe, SimpleContainer, BlenderBlockEntity> {
    @Override
    public boolean isValidBlockEntity(BlockEntity be) {
        return be instanceof BlenderBlockEntity;
    }

    @Override
    public boolean isHeated(BlenderBlockEntity be) {
        return true;
    }

    @Override
    public RecipeType<BlenderRecipe> getRecipeType() {
        return BlenderRecipe.Type.INSTANCE;
    }

    @Override
    public @NotNull ResourceLocation getType() {
        return VResourceLocation.createTypeMod(BlenderRecipe.Serializer.ID);
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) simpleContainer(allInputs);
    }

    @Override
    public List<Ingredient> getContainers(BlenderRecipe recipe) {
        return toIngredients(recipe.getContainer());
    }

    @Override
    public int getRecipeTime(BlenderRecipe recipe) {
        return 100;
    }

    @Override
    public Item getBlockItemForTranslate() {
        return BakeriesItems.BLENDER.get();
    }
}
