package com.github.wallev.maidsoulkitchen.compat.msm.youkaishomecoming.basin;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.grape.IGrapeJumpRecipeGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.youkaishomecoming.base.IYhcRecipe;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.google.common.collect.Lists;
import dev.xkmc.youkaishomecoming.content.item.fluid.YHFluid;
import dev.xkmc.youkaishomecoming.content.pot.basin.BasinBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.basin.BasinRecipe;
import dev.xkmc.youkaishomecoming.content.pot.basin.SimpleBasinRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_YHC_BASIN)
public class GeneratorYhcBasinGuide implements IGrapeJumpRecipeGuideGenerator<BasinRecipe<?>>, IYhcRecipe<SimpleBasinRecipe> {
    @Override
    public int jumpTime() {
        return 6;
    }

    @Override
    public RecipeType<BasinRecipe<?>> getRecipeType() {
        return YHBlocks.BASIN_RT.get();
    }

    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return level.getBlockEntity(pos) instanceof BasinBlockEntity basin && isEmpty(basin);
    }

    public boolean isEmpty(BasinBlockEntity basin) {
        return basin.getFluidHandler().isEmpty() && basin.getItemHandler().isEmpty();
    }

    @Override
    @NotNull
    public ResourceLocation getType() {
        return VResourceLocation.createTypeMod(YHBlocks.BASIN_RT.getId());
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return level.getBlockState(pos).is(YHBlocks.BASIN.get());
    }

    @Override
    public List<Ingredient> getInputs(BasinRecipe<?> recipe) {
        SimpleBasinRecipe oRecipe = castRecipe(recipe) ;
        Ingredient input = oRecipe.input;

        List<Ingredient> convertInputs = Arrays.stream(input.getItems())
                .map(itemStack -> itemStack.copyWithCount(5))
                .map(Ingredient::of)
                .toList();
        return convertInputs;
    }

    @Override
    public List<Ingredient> getContainers(BasinRecipe<?> recipe) {
        ItemStack bottle = Items.GLASS_BOTTLE.getDefaultInstance();
        return Lists.newArrayList(Ingredient.of(bottle));
    }

    @Override
    public List<ItemStack> getOutputs(BasinRecipe<?> recipe, RegistryAccess registryAccess) {
        SimpleBasinRecipe oRecipe = castRecipe(recipe) ;

        Fluid fluid = oRecipe.output.getFluid();
        if (fluid instanceof YHFluid sake) {
            return List.of(sake.type.asStack(sake.type.count()));
        } else {
            return List.of();
        }
    }

    @Override
    public Item getBlockItemForTranslate() {
        return YHBlocks.BASIN.asItem();
    }
}
