package com.github.wallev.maidsoulkitchen.compat.msm.common.util.action;

import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.EnchantCommonIdleAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.custom.LimitIdleAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import net.minecraft.core.BlockPos;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOptionSet;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;

import java.util.List;

import static com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.TargetUtil.*;

@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public class IdleStepUtil {

    public static void addStep(CraftGuideOperator2 craftGuide) {
        addStep(craftGuide.steps(), craftGuide.pos());
    }

    public static void addStep(List<CraftGuideStepData> steps, BlockPos pos) {
        steps.add(makeStep(pos));
    }

    /**
     * 创建一个制作指南步骤数据对象
     *
     * @param pos 方块位置，用于创建目标虚拟方块数据
     * @return 返回一个新的CraftGuideStepData对象，包含目标位置、空的列表数据和默认的附魔闲置动作类型
     */
    public static CraftGuideStepData makeStep(BlockPos pos) {
        return new CraftGuideStepData(
                makeTargetVirtualNoSide(pos),
                List.of(),
                List.of(),
                EnchantCommonIdleAction.TYPE
        );
    }

    public static void addStepWithLimit(CraftGuideOperator2 craftGuide) {
        addStepWithLimit(craftGuide.steps(), craftGuide.pos());
    }

    public static void addStepWithLimit(List<CraftGuideStepData> steps, BlockPos pos) {
        steps.add(makeStepWithLimit(pos));
    }

    /**
     * 创建一个制作指南步骤数据对象
     *
     * @param pos 方块位置，用于创建目标虚拟方块数据
     * @return 返回一个新的CraftGuideStepData对象，包含目标位置、空的列表数据和默认的附魔闲置动作类型
     */
    public static CraftGuideStepData makeStepWithLimit(BlockPos pos) {
        return new CraftGuideStepData(
                makeTargetVirtualNoSide(pos),
                List.of(),
                List.of(),
                LimitIdleAction.TYPE
        );
    }


    public static void addStep(CraftGuideOperator2 craftGuide, int tickTime) {
        addStep(craftGuide.steps(), craftGuide.pos(), tickTime);
    }

    public static void addStep(List<CraftGuideStepData> steps, BlockPos pos, int tickTime) {
        steps.add(makeStep(pos, tickTime));
    }

    /**
     * 创建一个制作指南步骤数据对象
     *
     * @param pos 方块位置，用于确定目标位置
     * @param tickTime 等待时间，以游戏刻为单位
     * @return 返回包含指定位置和等待时间的制作指南步骤数据
     */
    public static CraftGuideStepData makeStep(BlockPos pos, int tickTime) {
        // 创建一个新的制作指南步骤数据对象，包含目标位置、空的输入输出列表、空闲动作类型和等待选项
        return new CraftGuideStepData(
                makeTargetVirtualNoSide(pos),
                List.of(),
                List.of(),
                EnchantCommonIdleAction.TYPE,
                ActionOptionSet.with(EnchantCommonIdleAction.OPTION_WAIT, true, String.valueOf(tickTime))
        );
    }


    public static void addStepWithLimit(CraftGuideOperator2 craftGuide, int tickTime) {
        addStepWithLimit(craftGuide.steps(), craftGuide.pos(), tickTime);
    }

    public static void addStepWithLimit(List<CraftGuideStepData> steps, BlockPos pos, int tickTime) {
        steps.add(makeStepWithLimit(pos, tickTime));
    }

    /**
     * 创建一个制作指南步骤数据对象
     *
     * @param pos 方块位置，用于确定目标位置
     * @param tickTime 等待时间，以游戏刻为单位
     * @return 返回包含指定位置和等待时间的制作指南步骤数据
     */
    public static CraftGuideStepData makeStepWithLimit(BlockPos pos, int tickTime) {
        // 创建一个新的制作指南步骤数据对象，包含目标位置、空的输入输出列表、空闲动作类型和等待选项
        return new CraftGuideStepData(
                makeTargetVirtualNoSide(pos),
                List.of(),
                List.of(),
                LimitIdleAction.TYPE,
                ActionOptionSet.with(LimitIdleAction.OPTION_WAIT, true, String.valueOf(tickTime))
        );
    }

}
