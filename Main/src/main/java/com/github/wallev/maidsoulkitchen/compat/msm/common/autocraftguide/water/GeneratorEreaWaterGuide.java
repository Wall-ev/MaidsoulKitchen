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
import org.jetbrains.annotations.NotNull;

import java.util.List;


@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_MULTI_GETTER_WATER)
public class GeneratorEreaWaterGuide extends BaseGetterWaterGuideGenerator {

    @TypeLang(
            en_us = "Multi water watering water",
            zh_cn = "从无限水中装水"
    )
    @Override
    public @NotNull ResourceLocation getType() {
        return VResourceLocation.createTypeMod(ModRecipes.CONSUME_WATER_RECIPE.toString() + "_from_multi");
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) recipeWrapperContainer(allInputs);
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return isValidWaterBlock(level, pos);
    }

    @Override
    public boolean isValidRecipe(ConsumeWaterRecipe recipe) {
        return recipe.condition == ConsumeWaterRecipe.Condition.MULTIPLE;
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ItemStack.EMPTY.getItem();
    }
}
