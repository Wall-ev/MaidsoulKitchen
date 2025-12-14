package com.github.wallev.maidsoulkitchen.compat.msm.youkaishomecoming.mokapot;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.ICookingRecipeGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.storage.ContainerStorage;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import dev.xkmc.youkaishomecoming.content.pot.moka.MokaRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_YHC_MOKA_POT)
public class GeneratorYhcMokaPotGuide implements ICookingRecipeGuideGenerator<MokaRecipe> {

    public GeneratorYhcMokaPotGuide() {
        new MokaPotInvFactory();
    }

    @Override
    public int getRecipeTime(MokaRecipe recipe) {
        return recipe.getCookTime();
    }

    @Override
    public List<Ingredient> getContainers(MokaRecipe recipe) {
        return List.of(Ingredient.of(recipe.getOutputContainer()));
    }

    @Override
    public Item getBlockItemForTranslate() {
        return YHBlocks.MOKA.asItem();
    }

    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return true;
    }

    @Override
    public RecipeType<MokaRecipe> getRecipeType() {
        return YHBlocks.MOKA_RT.get();
    }

    @Override
    @NotNull
    public ResourceLocation getType() {
        return VResourceLocation.createTypeMod(YHBlocks.MOKA_RT.getId());
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) recipeWrapperContainer(allInputs);
    }

    @Override
    public ResourceLocation getOutputContainerStorageType() {
        return ContainerStorage.TYPE;
    }

    @Override
    public ResourceLocation getOutputStorageType() {
        return ContainerStorage.TYPE;
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return level.getBlockState(pos).is(YHBlocks.MOKA.get());
    }

    @Override
    public boolean matchResultCount() {
        return true;
    }
}
