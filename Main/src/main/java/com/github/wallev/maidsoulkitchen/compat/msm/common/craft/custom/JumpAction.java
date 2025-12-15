package com.github.wallev.maidsoulkitchen.compat.msm.common.craft.custom;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.util.IFailGuideUseActionContext;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.StringUtils;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOption;
import studio.fantasyit.maid_storage_manager.craft.context.AbstractCraftActionContext;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideData;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.work.CraftLayer;


@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public class JumpAction extends AbstractCraftActionContext implements IFailGuideUseActionContext {
    public static final ResourceLocation TYPE = VResourceLocation.ofMod("jump");

    public static final ActionOption<Boolean> JUMP_COUNT = new ActionOption<>(
            VResourceLocation.ofMod("jump_mode"),
            new Component[]{
                    Component.translatable("gui.maid_storage_manager.craft_guide.common.idle_second"),
                    Component.translatable("gui.maid_storage_manager.craft_guide.common.idle_tick")
            },
            new ResourceLocation[]{
                    new ResourceLocation("maid_storage_manager:textures/gui/craft/option/wait_second.png"),
                    new ResourceLocation("maid_storage_manager:textures/gui/craft/option/wait_tick.png")
            },
            "",
            new ActionOption.BiConverter<>(
                    i -> i != 0, b -> b ? 1 : 0
            ),
            ActionOption.ValuePredicatorOrGetter.predicator(t -> (t.isBlank() || (StringUtils.isNumeric(t) && Integer.parseInt(t) <= 999)))
    );

    private Vec3 center;
    private int leftJumpCount = 0;

    public JumpAction(EntityMaid maid, CraftGuideData craftGuideData, CraftGuideStepData craftGuideStepData, CraftLayer layer) {
        super(maid, craftGuideData, craftGuideStepData, layer);
    }

    @Override
    public void loadEnv(CompoundTag env) {
        if (env.contains("leftJumpCount"))
            leftJumpCount = env.getInt("leftJumpCount");
        else
            leftJumpCount = 0;
    }

    @Override
    public CompoundTag saveEnv(CompoundTag env) {
        env.putInt("leftJumpCount", leftJumpCount);
        return super.saveEnv(env);
    }

    @Override
    public Result start() {
        BlockPos target = craftGuideStepData.getStorage().getPos();
        this.center = target.getCenter();

        String jumpCountStr = craftGuideStepData.getOptionValue(JUMP_COUNT);
        if (jumpCountStr.isBlank())
            jumpCountStr = "0";
        int time = Integer.parseInt(jumpCountStr);

        this.leftJumpCount = time;

        return Result.NOT_DONE;
    }

    @Override
    public Result tick() {
        if (!maid.onGround()) {
            return Result.NOT_DONE;
        } else if (leftJumpCount <= 0) {
            return Result.SUCCESS;
        } else {
            this.dump();
            return Result.NOT_DONE;
        }
    }

    @Override
    public void stop() {
        this.center = null;
        this.leftJumpCount = 0;
    }

    public void dump() {
        leftJumpCount--;

        Vec3 deltaMovement = maid.getDeltaMovement();
        maid.setDeltaMovement(0, deltaMovement.y(), 0);
        maid.setDeltaMovement((center.x - maid.position().x) * 0.2f,
                0.3, (center.z - maid.position().z) * 0.2f);
        Vec3 deltaMovement1 = maid.getDeltaMovement();
        maid.setDeltaMovement(deltaMovement1.x * 0.3f, maid.getDeltaMovement().y(), deltaMovement1.z * 0.3f);
    }

}
