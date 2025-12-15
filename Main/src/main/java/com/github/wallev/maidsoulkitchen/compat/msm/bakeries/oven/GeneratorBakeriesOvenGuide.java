package com.github.wallev.maidsoulkitchen.compat.msm.bakeries.oven;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.cookingpot.IFdCookingPotGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.TargetUtil;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.TypeLang;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.GuideTest;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.renyigesai.bakeries.block.oven.OvenBlockEntity;
import com.renyigesai.bakeries.init.BakeriesItems;
import com.renyigesai.bakeries.recipe.oven.OvenRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import studio.fantasyit.maid_storage_manager.craft.CollectCraftEvent;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOptionSet;
import studio.fantasyit.maid_storage_manager.craft.action.CraftAction;
import studio.fantasyit.maid_storage_manager.craft.action.PathTargetLocator;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.generator.config.ConfigTypes;

import java.util.List;

//@GuideTest
@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_BAKERIES_OVEN)
public class GeneratorBakeriesOvenGuide implements IFdCookingPotGuideGenerator<OvenRecipe, OvenBlockEntity> {
//public class GeneratorBakeriesOvenGuide implements IFdCookingPotGuideGenerator<OvenRecipe, Container, OvenBlockEntity> {

    @TypeLang(
            en_us = "Number of each bake",
            zh_cn = "每次烘烤的数量"
    )
    protected ConfigTypes.ConfigType<Integer> COUNT = new ConfigTypes.ConfigType<>(
            "count",
            4,
            Component.translatable("config.maid_storage_manager.crafting.generating.maid_storage_manager." + this.toTypeStr() + ".count"),
            ConfigTypes.ConfigTypeEnum.Integer
    );

    public GeneratorBakeriesOvenGuide(CollectCraftEvent event) {
        event.addAutoCraftGuideGenerator(this);
        event.addAction(
                SetTimeAction.TYPE,
                SetTimeAction::new,
                PathTargetLocator::nearByNoLimitation,
                CraftAction.PathEnoughLevel.CLOSER.value,
                false,
                4,
                4,
                List.of(SetTimeAction.OPTION_WAIT)
        );
    }

    @Override
    public int getRecipeTime(OvenRecipe recipe) {
        return recipe.getTime();
    }

    @Override
    public void inputsStep(BlockPos pos, CraftGuideOperator2 craftGuide, List<ItemStack> realItems) {
        for (int i = 0; i < Math.max(COUNT.getValue(), 4); i++) {
            craftGuide.addItemInsert(this.getInputStorageType(), Direction.UP, realItems.get(0).copyWithCount(1));
        }
    }

    @Override
    public void waitToOutputStep(BlockPos pos, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, OvenRecipe recipe) {
        int minTemperature = recipe.getMin_temperature();
        craftGuide.addStep(new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                List.of(),
                List.of(),
                SetTimeAction.TYPE,
                ActionOptionSet.with(SetTimeAction.OPTION_WAIT, true, String.valueOf(minTemperature))
        ));

        IFdCookingPotGuideGenerator.super.waitToOutputStep(pos, craftGuide, realItems, recipe);
    }

    @Override
    public void outputsStep(BlockPos pos, CraftGuideOperator2 craftGuide, List<ItemStack> outputs) {
        for (int i = 0; i < Math.max(COUNT.getValue(), 4); i++) {
            craftGuide.addItemTake(this.getOutputStorageType(), Direction.DOWN, outputs.get(0).copyWithCount(1));
        }
    }

    @Override
    public boolean isValidBlockEntity(BlockEntity be) {
        return be instanceof OvenBlockEntity;
    }

    @Override
    public boolean isHeated(OvenBlockEntity be) {
        return true;
    }

    @Override
    public RecipeType<OvenRecipe> getRecipeType() {
        return OvenRecipe.Type.INSTANCE;
    }

    @Override
    public @NotNull ResourceLocation getType() {
        return VResourceLocation.createTypeMod(OvenRecipe.Serializer.ID);
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) recipeWrapperContainer(allInputs);
    }

    @Override
    public Item getBlockItemForTranslate() {
        return BakeriesItems.OVEN.get();
    }

    @Override
    public List<ConfigTypes.ConfigType<?>> getConfigurations() {
        return List.of(COUNT);
    }
}
