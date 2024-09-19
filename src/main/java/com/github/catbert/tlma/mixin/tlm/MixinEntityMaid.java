package com.github.catbert.tlma.mixin.tlm;

import com.github.catbert.tlma.api.IAddonMaid;
import com.github.catbert.tlma.api.ILittleMaidTask;
import com.github.catbert.tlma.util.FakePlayerUtil;
import com.github.catbert.tlma.util.MaidTaskDataUtil;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.nbt.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.ref.WeakReference;

@Mixin(value = EntityMaid.class, remap = false)
public abstract class MixinEntityMaid extends TamableAnimal implements CrossbowAttackMob, IMaid, IAddonMaid {
//    @Unique
    @SuppressWarnings("all")
    private static final EntityDataAccessor<Integer> SEARCHY_OFFSET_DATA = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.INT);
//    @Unique
    @SuppressWarnings("all")
    private static final String SEARCHY_OFFSET_TAG = "SearchYOffset";
//    @Unique
    @SuppressWarnings("all")
    private WeakReference<FakePlayer> fakePlayer;
    @Shadow
    private IMaidTask task;

    protected MixinEntityMaid(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Shadow
    public abstract IMaidTask getTask();

    @Inject(at = @At("TAIL"), remap = true, method = "defineSynchedData()V")
    private void registerData$tlma(CallbackInfo ci) {
        entityData.define(MaidTaskDataUtil.TASK_DATA_INFO, new CompoundTag());
    }

    @SuppressWarnings("all")
    @Inject(at = @At("TAIL"), remap = true, method = "addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V")
    private void writeAdditional$tlma(CompoundTag compoundNBT, CallbackInfo ci) {
        compoundNBT.put(MaidTaskDataUtil.TASK_DATA_TAG, MaidTaskDataUtil.getTaskData(this.asStrictMaid()));
    }

    @SuppressWarnings("all")
    @Inject(at = @At("TAIL"), remap = true, method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V")
    private void readAdditional$tlma(CompoundTag compoundNBT, CallbackInfo ci) {
        if (compoundNBT.contains(MaidTaskDataUtil.TASK_DATA_TAG, Tag.TAG_COMPOUND)) {
            MaidTaskDataUtil.setTaskData(this.asStrictMaid(), compoundNBT.getCompound(MaidTaskDataUtil.TASK_DATA_TAG));
        }

        if (compoundNBT.contains(SEARCHY_OFFSET_TAG, Tag.TAG_INT)) {
            this.entityData.set(SEARCHY_OFFSET_DATA, compoundNBT.getInt(SEARCHY_OFFSET_TAG));
        }
    }

    @Override
    public Integer getStartYOffset$tlma() {
        return this.entityData.get(SEARCHY_OFFSET_DATA);
//        ??为啥在MaidOverlay不能实时获取呢？
//        return getAddonMaidData().getInt(SEARCHY_OFFSET_TAG);
    }

    @Override
    public void setStartYOffset$tlma(int offset) {
        this.entityData.set(SEARCHY_OFFSET_DATA, offset);
//        getAddonMaidData().putInt(SEARCHY_OFFSET_TAG, offset);
    }

    @Override
    public @NotNull WeakReference<FakePlayer> getFakePlayer$tlma() {
        return fakePlayer;
    }

    @Override
    public void initFakePlayer$tlma() {
        if (fakePlayer == null) {
            this.fakePlayer = FakePlayerUtil.setupBeforeTrigger((ServerLevel) level(), this.getName().getString(), this);
        }
    }

    public boolean openMaidGuiFromSideTab(Player player, int tabIndex, boolean taskListOpen, int taskPage) {
        if (player instanceof ServerPlayer && !this.isSleeping()) {
            this.navigation.stop();
            NetworkHooks.openScreen((ServerPlayer) player, getGuiProviderFromSideTab(tabIndex, taskListOpen, taskPage), (buffer) -> buffer.writeInt(getId()));
        }
        return true;
    }

    public MenuProvider getGuiProviderFromSideTab(int tabIndex, boolean taskListOpen, int taskPage) {
        if (tabIndex == 0 && task instanceof ILittleMaidTask littleMaidTask) {
            return littleMaidTask.getGuiProvider((EntityMaid) (Object) this, getId(), taskListOpen, taskPage);
        }
        switch (tabIndex) {
            case 0:
//                return this.getMaidBackpackType().getGuiProvider(getId());
//                return TaskConfigerContainer.create(getId());
            default:
                return this.getMaidBackpackType().getGuiProvider(getId());
        }
    }
}
