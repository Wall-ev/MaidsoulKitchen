package com.github.wallev.maidsoulkitchen.compat.msm.dungeonsdelight.cooking;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.cookingpot.IFdCookingPotGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.storage.ContainerStorage;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.GuideTest;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.yirmiri.dungeonsdelight.common.block.entity.MonsterPotBlockEntity;
import net.yirmiri.dungeonsdelight.common.block.entity.container.MonsterPotRecipe;
import net.yirmiri.dungeonsdelight.core.registry.DDItems;
import net.yirmiri.dungeonsdelight.core.registry.DDRecipeRegistries;

import java.util.List;

//@GuideTest
@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_DD_MONSTER_POT)
public class GeneratorDdCookingGuide implements IFdCookingPotGuideGenerator<MonsterPotRecipe, MonsterPotBlockEntity> {
//public class GeneratorDdCookingGuide implements IFdCookingPotGuideGenerator<MonsterPotRecipe, RecipeWrapper, MonsterPotBlockEntity> {

    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return level.getBlockEntity(pos) instanceof MonsterPotBlockEntity;
    }

    @Override
    public RecipeType<MonsterPotRecipe> getRecipeType() {
        return DDRecipeRegistries.MONSTER_COOKING_RECIPE_TYPE.get();
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof MonsterPotBlockEntity;
    }

    @Override
    public boolean isValidBlockEntity(BlockEntity be) {
        return be instanceof MonsterPotBlockEntity;
    }

    @Override
    public boolean isHeated(MonsterPotBlockEntity be) {
        return be.isHeated();
    }

    @Override
    public int getRecipeTime(MonsterPotRecipe recipe) {
        return recipe.getCookTime();
    }

    @Override
    public List<Ingredient> getContainers(MonsterPotRecipe recipe) {
        return toIngredients(recipe.getOutputContainer());
    }

    @Override
    public Item getBlockItemForTranslate() {
        return DDItems.MONSTER_POT.get();
    }

    @Override
    public ResourceLocation getOutputContainerStorageType() {
        return ContainerStorage.TYPE;
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) recipeWrapperContainer(allInputs);
    }
}
