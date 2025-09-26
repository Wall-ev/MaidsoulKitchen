package com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.water;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.TypeLang;
import com.github.wallev.maidsoulkitchen.init.ModRecipes;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.recipe.water.ConsumeWaterRecipe;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.List;


@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_SINGLE_GETTER_WATER)
public class GeneratorSingleWaterGuide extends BaseGetterWaterGuideGenerator {

    @TypeLang(
            en_us = "Watering Water From Single",
            zh_cn = "从单格水源中打水"
    )
    @Override
    public @NotNull ResourceLocation getType() {
        return VResourceLocation.createTypeMod(ModRecipes.CONSUME_WATER_RECIPE.toString() + "_from_single");
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) simpleContainer(allInputs);
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return level.getBlockState(pos).is(Blocks.WATER);
    }

    @Override
    public boolean isValidRecipe(ConsumeWaterRecipe recipe) {
        return recipe.condition == ConsumeWaterRecipe.Condition.SINGLE;
    }

    @Override
    public Item getBlockItemForTranslate() {
        return DEFAULT_ITEM;
    }
}
