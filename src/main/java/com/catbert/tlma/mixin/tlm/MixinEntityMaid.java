package com.catbert.tlma.mixin.tlm;

import com.catbert.tlma.api.IMaidAddon;
import com.catbert.tlma.util.FakePlayerUtil;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.ref.WeakReference;

@Mixin(value = EntityMaid.class, remap = false)
public abstract class MixinEntityMaid extends TamableAnimal implements CrossbowAttackMob, IMaid, IMaidAddon {
    private static final EntityDataAccessor<Integer> START_Y_OFFSET = SynchedEntityData.defineId(MixinEntityMaid.class, EntityDataSerializers.INT);

    private static final String START_Y_OFFSET_TAG = "StartYOffset";

    private WeakReference<FakePlayer> fakePlayer;

    protected MixinEntityMaid(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(at = @At("RETURN"), method = "defineSynchedData")
    protected void defineSynchedData(CallbackInfo info){
        this.entityData.define(START_Y_OFFSET, 4);
    }
    @Inject(at = @At("RETURN"), method = "addAdditionalSaveData")
    protected void addAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        compound.putInt(START_Y_OFFSET_TAG, this.entityData.get(START_Y_OFFSET));
    }

    @Inject(at = @At("RETURN"), method = "readAdditionalSaveData")
    protected void readAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        if (compound.contains(START_Y_OFFSET_TAG, Tag.TAG_INT)) {
            this.entityData.set(START_Y_OFFSET, compound.getInt(START_Y_OFFSET_TAG));
        }
    }

    @Override
    public void setStartYOffset$tlma(int offset) {
        this.getEntityData().set(START_Y_OFFSET, offset);
    }

    @Override
    public Integer getStartYOffset$tlma() {
        return this.getEntityData().get(START_Y_OFFSET);
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
}
