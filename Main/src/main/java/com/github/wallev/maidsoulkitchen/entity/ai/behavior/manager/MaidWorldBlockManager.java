package com.github.wallev.maidsoulkitchen.entity.ai.behavior.manager;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class MaidWorldBlockManager {

    private final EntityMaid maid;
    private BlockPos breakBlockPos;
    private BlockState blockState;
    private ItemStack heldItem;
    private float currentBreakTime;
    private float lastBreakProgress;
    private float maxBreakTime;

    public MaidWorldBlockManager(EntityMaid maid) {
        this.maid = maid;
    }

    public void reset() {
        breakBlockPos = null;
        blockState = null;
        currentBreakTime = 0;
        lastBreakProgress = 0;
        maxBreakTime = 0;
    }

    public void initial() {
        this.initial(this.breakBlockPos);
    }

    public void initial(BlockPos blockPos) {
        this.initial(blockPos, maid.getMainHandItem());
    }

    public void initial(BlockPos blockPos, ItemStack heldItem) {
        this.breakBlockPos = blockPos;
        this.blockState = maid.level.getBlockState(blockPos);
        this.heldItem = heldItem;
        float digSpeed = this.getDigSpeed(blockState, heldItem);
        this.maxBreakTime = (blockState.getBlock().defaultDestroyTime() / digSpeed) * 20;
    }

    @Nullable
    public BlockPos getBreakBlockPos() {
        return breakBlockPos;
    }

    public BlockState getBlockState() {
        return blockState;
    }

    public void setBlockState(BlockState blockState) {
        this.blockState = blockState;
    }

    public float getCurrentBreakTime() {
        return currentBreakTime;
    }

    public void incrementBreakTime() {
        currentBreakTime++;
    }

    public ItemStack getHeldItem() {
        return heldItem;
    }

    public void setHeldItem(ItemStack heldItem) {
        this.heldItem = heldItem;
    }

    public void setMaxBreakTime(float maxBreakTime) {
        this.maxBreakTime = maxBreakTime;
    }

    public float getMaxBreakTime() {
        return maxBreakTime;
    }

    public int getLastBreakProgress() {
        return (int) (lastBreakProgress = ((float) currentBreakTime / (float) maxBreakTime) * 10.0F);
    }

    public float getDigSpeed(BlockState pState, ItemStack destroyingItem) {
        float destroySpeed = destroyingItem.getDestroySpeed(pState);
        if (destroySpeed > 1.0F) {
            int i = EnchantmentHelper.getBlockEfficiency(this.maid);
            if (i > 0 && !destroyingItem.isEmpty()) {
                destroySpeed += (float) (i * i + 1);
            }
        }

        if (MobEffectUtil.hasDigSpeed(this.maid)) {
            destroySpeed *= 1.0F + (float) (MobEffectUtil.getDigSpeedAmplification(this.maid) + 1) * 0.2F;
        }

        if (this.maid.hasEffect(MobEffects.DIG_SLOWDOWN)) {
            float f1 = switch (Objects.requireNonNull(this.maid.getEffect(MobEffects.DIG_SLOWDOWN)).getAmplifier()) {
                case 0 -> 0.3F;
                case 1 -> 0.09F;
                case 2 -> 0.0027F;
                default -> 8.1E-4F;
            };

            destroySpeed *= f1;
        }

        if (this.maid.isEyeInFluid(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(this.maid)) {
            destroySpeed /= 5.0F;
        }

        if (!this.maid.onGround()) {
            destroySpeed /= 5.0F;
        }

        return destroySpeed;
    }

    public static Optional<MaidWorldBlockManager> get(EntityMaid maid) {
        return maid.getBrain().getMemory(ModEntities.WORLD_BLOCK_MANAGER.get());
    }

    public static void erase(EntityMaid maid) {
        maid.getBrain().eraseMemory(ModEntities.WORLD_BLOCK_MANAGER.get());
    }

    public static void destroyBlock(EntityMaid maid, BlockPos blockPos) {
        get(maid).ifPresentOrElse(worldBlockManager -> {
            worldBlockManager.initial(blockPos);
        }, () -> {
            MaidWorldBlockManager worldBlockManager = new MaidWorldBlockManager(maid);
            worldBlockManager.initial(blockPos);
            maid.getBrain().setMemory(ModEntities.WORLD_BLOCK_MANAGER.get(), worldBlockManager);
        });
    }

    public static void destroyBlock(EntityMaid maid, BlockPos blockPos, ItemStack heldItem) {
        get(maid).ifPresentOrElse(worldBlockManager -> {
            worldBlockManager.initial(blockPos, heldItem);
        }, () -> {
            MaidWorldBlockManager worldBlockManager = new MaidWorldBlockManager(maid);
            worldBlockManager.initial(blockPos, heldItem);
            maid.getBrain().setMemory(ModEntities.WORLD_BLOCK_MANAGER.get(), worldBlockManager);
        });
    }

    public static void walkAndDestroyBlock(EntityMaid maid, BlockPos blockPos) {
        BehaviorUtils.setWalkAndLookTargetMemories(maid, blockPos, 0.5F, 1);
        destroyBlock(maid, blockPos);
    }

    public static void walkAndDestroyBlock(EntityMaid maid, BlockPos blockPos, ItemStack heldItem) {
        BehaviorUtils.setWalkAndLookTargetMemories(maid, blockPos, 0.5F, 1);
        destroyBlock(maid, blockPos, heldItem);
    }

}