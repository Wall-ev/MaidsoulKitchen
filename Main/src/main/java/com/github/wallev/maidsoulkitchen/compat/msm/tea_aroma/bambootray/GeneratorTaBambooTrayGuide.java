package com.github.wallev.maidsoulkitchen.compat.msm.tea_aroma.bambootray;

import cn.foggyhillside.tea_aroma.blocks.BambooTrayBlock;
import cn.foggyhillside.tea_aroma.config.CommonConfigs;
import cn.foggyhillside.tea_aroma.recipe.BambooTrayRecipe;
import cn.foggyhillside.tea_aroma.registry.ModBlocks;
import cn.foggyhillside.tea_aroma.registry.ModRecipeTypes;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.plate.IPlateGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.TypeLang;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import studio.fantasyit.maid_storage_manager.craft.generator.config.ConfigTypes;

import java.util.List;

@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_TA_BAMBOO_TRAY)
public class GeneratorTaBambooTrayGuide extends IPlateGuideGenerator<BambooTrayRecipe> {

    @TypeLang(
            en_us = "The number of each drying",
            zh_cn = "每次晾晒的数量"
    )
    protected ConfigTypes.ConfigType<Integer> COUNT_VIRTUAL = COUNT;

    @Override
    public int waitTime(BambooTrayRecipe recipe) {
        return CommonConfigs.BAMBOO_TRAY_MAX_PROGRESS.get();
    }

    @Override
    public int getMaxSingleCount() {
        return 8;
    }

    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return level.getBlockState(pos).getValue(BambooTrayBlock.PROCESS_TYPE).equals(0);
    }

    @Override
    public void generateSteps(BlockPos pos, Level level, BambooTrayRecipe recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        // 1 -> 凋零
        // 2 -> 揉捏
        // 3 -> 发酵
        switch (recipe.getProcessType()) {
            case 1, 3 ->
                    this.generateFermentationOrWithering(pos, level, recipe, craftGuide, realItems, needContainer, containers, outputs, remains);
            case 2 ->
                    this.generateRolling(pos, level, recipe, craftGuide, realItems, needContainer, containers, outputs, remains);
        }

    }

    @Override
    public Item getBlockItemForTranslate() {
        return ModBlocks.BAMBOO_TRAY.get().asItem();
    }

    public void generateFermentationOrWithering(BlockPos pos, Level level, BambooTrayRecipe recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        // 物品使用
        List<ItemStack> withCountRealItems = realItems.stream()
                .map(itemStack -> itemStack.copyWithCount(COUNT.getValue()))
                .toList();
        craftGuide.addItemUse(withCountRealItems);

        // 物品合成
        int waitTime = this.waitTime(recipe);
        craftGuide.addIdle(waitTime + Math.min(0, COUNT.getValue() - 1));

        // 物品拾取
        List<ItemStack> withCountOutputs = outputs.stream()
                .map(itemStack -> itemStack.copyWithCount(COUNT.getValue()))
                .toList();
        craftGuide.addItemPickup(withCountOutputs);
    }

    public void generateRolling(BlockPos pos, Level level, BambooTrayRecipe recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        // 物品使用
        List<ItemStack> withCountRealItems = realItems.stream()
                .map(itemStack -> itemStack.copyWithCount(COUNT.getValue()))
                .toList();
        craftGuide.addItemUse(withCountRealItems);

        // 物品合成
        craftGuide.addEmptyUse(4);

        // 物品拾取
        List<ItemStack> withCountOutputs = outputs.stream()
                .map(itemStack -> itemStack.copyWithCount(COUNT.getValue()))
                .toList();
        craftGuide.addItemPickup(withCountOutputs);
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return level.getBlockState(pos).is(ModBlocks.BAMBOO_TRAY.get());
    }

    @Override
    @NotNull
    public ResourceLocation getType() {
        assert ModRecipeTypes.BAMBOO_TRAY_RECIPE.getId() != null;
        return VResourceLocation.createTypeMod(ModRecipeTypes.BAMBOO_TRAY_RECIPE.getId());
    }

    @Override
    public RecipeType<BambooTrayRecipe> getRecipeType() {
        return BambooTrayRecipe.Type.INSTANCE;
    }

}
