package com.github.wallev.maidsoulkitchen.compat.msm.youkaishomecoming.kettle;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.fluidinsert.IFluidInsertRecipeGuideGenerator;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_YHC_KETTLE)
public class GeneratorYhcKettleGuide implements IFluidInsertRecipeGuideGenerator<KettleRecipe> {

    @Override
    public int getRecipeTime(KettleRecipe recipe) {
        return recipe.getCookTime();
    }

    @Override
    public List<Ingredient> getContainers(KettleRecipe recipe) {
        return List.of(Ingredient.of(recipe.getOutputContainer()));
    }

    @Override
    public Item getBlockItemForTranslate() {
        return YHBlocks.KETTLE.asItem();
    }

    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return true;
    }

    @Override
    public RecipeType<KettleRecipe> getRecipeType() {
        return YHBlocks.KETTLE_RT.get();
    }

    @Override
    @NotNull
    public ResourceLocation getType() {
        return VResourceLocation.createTypeMod(YHBlocks.KETTLE_RT.getId());
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) recipeWrapperContainer(allInputs);
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return level.getBlockState(pos).is(YHBlocks.KETTLE.get());
    }

    @Override
    public List<Ingredient> getFluids(KettleRecipe recipe) {
        return List.of(Ingredient.of(Items.WATER_BUCKET));
    }
}
