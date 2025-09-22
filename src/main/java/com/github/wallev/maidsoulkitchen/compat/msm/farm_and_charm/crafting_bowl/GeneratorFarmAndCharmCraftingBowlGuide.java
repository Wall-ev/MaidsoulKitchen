package com.github.wallev.maidsoulkitchen.compat.msm.farm_and_charm.crafting_bowl;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.click.IOnlyUseGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.GuideTest;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.satisfy.farm_and_charm.core.block.entity.CraftingBowlBlockEntity;
import net.satisfy.farm_and_charm.core.recipe.CraftingBowlRecipe;
import net.satisfy.farm_and_charm.core.registry.ObjectRegistry;
import net.satisfy.farm_and_charm.core.registry.RecipeTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@GuideTest
@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_FARM_AND_CHARM_CRAFTING_BOWL)
public class GeneratorFarmAndCharmCraftingBowlGuide implements IOnlyUseGuideGenerator<CraftingBowlRecipe> {
//public class GeneratorFarmAndCharmCraftingBowlGuide implements IOnlyUseGuideGenerator<CraftingBowlRecipe, Container> {

    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return true;
    }

    @Override
    public RecipeType<CraftingBowlRecipe> getRecipeType() {
        return RecipeTypeRegistry.CRAFTING_BOWL_RECIPE_TYPE.get();
    }

    @Override
    public @NotNull ResourceLocation getType() {
        return VResourceLocation.createTypeMod("farm_and_charm", "crafting_bowl");
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) recipeWrapperContainer(allInputs);
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof CraftingBowlBlockEntity;
    }



    @Override
    public void generateSteps(BlockPos pos, Level level, CraftingBowlRecipe recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        // 放入食材
        CraftGuideOperator2.forEachSingleItem(realItems, craftGuide::addItemUse);

        // 空手转动
        for (int i = 0; i < 15; i++) {
            craftGuide.addEmptyUse();
        }

        craftGuide.addIdle(10);
        craftGuide.addEmptyUse(outputs);
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ObjectRegistry.CRAFTING_BOWL.get().asItem();
    }
}
