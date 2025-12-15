package com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.tea;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.click.IOnlyUseGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.EnchantCommonUseAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.util.FailCraftGuideStepData;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOption;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOptionSet;
import studio.fantasyit.maid_storage_manager.craft.context.common.CommonUseAction;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;

import java.util.ArrayList;
import java.util.List;

import static com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.TargetUtil.makeTargetVirtualNoSide;

public interface ITeaGuideGenerator<R extends Recipe<? extends Container>> extends IOnlyUseGuideGenerator<R> {
//public interface ITeaGuideGenerator<R extends Recipe<C>, C extends Container> extends IOnlyUseGuideGenerator<R, C> {

    @Override
    default void generateSteps(BlockPos pos, Level level, R recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        ItemStack cup = realItems.remove(0);
        ItemStack liquid = realItems.remove(realItems.size() - 1);

        // 放茶杯
        craftGuide.addItemUse(cup);

        // 放茶叶
        CraftGuideOperator2.forEachSingleItem(realItems, itemStack -> {
            craftGuide.addItemUse(pos.above(), itemStack);
        });

        // 放水
        ItemStack leftFluidTeaBase = leftFluidTeaBase(liquid.copyWithCount(1));
//        craftGuide.addItemUse(pos.above(), liquid, leftFluidTeaBase);
        // 取回茶杯
        List<CraftGuideStepData> failSteps = new ArrayList<>();
        CraftGuideStepData failCupStep = new CraftGuideStepData(
                makeTargetVirtualNoSide(pos.above()),
                List.of(),
                List.of(liquid),
                CommonUseAction.TYPE,
                ActionOptionSet.with(ActionOption.OPTIONAL, true)
        );
        failSteps.add(failCupStep);
        // 取回茶叶
        CraftGuideOperator2.forEachSingleItem(realItems, itemStack -> {
//            craftGuide.addItemUse(pos.above(), itemStack);
            CraftGuideStepData failLeaveStep = new CraftGuideStepData(
                    makeTargetVirtualNoSide(pos.above()),
                    List.of(),
                    List.of(itemStack),
                    CommonUseAction.TYPE
//                    ActionOptionSet.with(ActionOption.OPTIONAL, true)
            );
            failSteps.add(failLeaveStep);
        });
        CompoundTag compoundTag = FailCraftGuideStepData.toCompoundTag(failSteps);

        CraftGuideStepData resultStepData = new CraftGuideStepData(
                makeTargetVirtualNoSide(pos.above()),
                List.of(liquid),
                List.of(leftFluidTeaBase),
                EnchantCommonUseAction.TYPE,
                compoundTag
        );
        craftGuide.addStep(resultStepData);

        // 右键拾取成品
        craftGuide.addEmptyUse(pos.above(), outputs);
//        craftGuide.addStep(new CraftGuideStepData(
//                TargetUtil.makeTargetVirtualNoSide(pos.above()),
//                List.of(),
//                outputs,
//                SneakCommonUseAction.TYPE,
//                ActionOptionSet.with(SneakCommonUseAction.SNEAK, false)
//        ));
    }


    @Override
    default List<Ingredient> getInputs(R recipe) {
        List<Ingredient> inputs = Lists.newArrayList();
        // 杯子
        inputs.addAll(this.getCups(recipe));
        // 原料
        inputs.addAll(this.getTeaLeaves(recipe));
        // 原料：流体
        inputs.addAll(this.getFluidTeaBase(recipe));

        return inputs;
    }

    @Override
    default List<ItemStack> getOutputs(R recipe, RegistryAccess registryAccess) {
        return this.getTea(recipe, registryAccess);
    }

    ItemStack leftFluidTeaBase(ItemStack fluidTeaBase);

    List<Ingredient> getCups(R recipe);

    List<Ingredient> getTeaLeaves(R recipe);

    List<Ingredient> getFluidTeaBase(R recipe);

    List<ItemStack> getTea(R recipe, RegistryAccess registryAccess);

    @Override
    default int getRecipeTime(R recipe) {
        return 0;
    }

    @Override
    default boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return true;
    }

    @Override
    default boolean isBlockValid(Level level, BlockPos pos) {
        return this.isValidGroundBlock(level, pos);
    }
}
