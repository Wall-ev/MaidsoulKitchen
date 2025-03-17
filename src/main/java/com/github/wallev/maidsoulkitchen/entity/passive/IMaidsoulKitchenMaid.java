package com.github.wallev.maidsoulkitchen.entity.passive;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.util.FakePlayerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

import static com.github.wallev.maidsoulkitchen.MaidsoulKitchen.LOGGER;

public interface IMaidsoulKitchenMaid {
    Set<Block> BLACK_LIST = new HashSet<>();

    static ItemStack interactUseOnBlockWithItem(EntityMaid maid, BlockPos blockPos, ItemStack itemStack) {
        IMaidsoulKitchenMaid addonMaid = (IMaidsoulKitchenMaid) maid;
        WeakReference<FakePlayer> fakePlayer$tlma = addonMaid.tlmk$getFakePlayer();
        FakePlayer fakePlayer = fakePlayer$tlma.get();
        if (fakePlayer != null) {
            try {
                fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
                InteractionResult interactionResult = FakePlayerUtil.interactUseOnBlock(fakePlayer$tlma, maid.level, blockPos, InteractionHand.MAIN_HAND, null);

                if (interactionResult == InteractionResult.PASS) {
                    BlockState blockState = maid.level.getBlockState(blockPos);
                    Block block = blockState.getBlock();
                    LOGGER.warn("FakePlayerUtil.interactUseOnBlock PASS: blockState:{} block: {}", blockState, block);
                    BLACK_LIST.add(block);
                    LOGGER.warn(BLACK_LIST.toString());
                }

                if (interactionResult != InteractionResult.PASS) {
                    ItemStack itemInHandCopy = fakePlayer.getItemInHand(InteractionHand.MAIN_HAND).copy();
                    ItemHandlerHelper.insertItemStacked(maid.getAvailableInv(true), itemInHandCopy, false);
                    fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                    return itemInHandCopy;
                } else {
                    fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                    return ItemStack.EMPTY;
                }

            } catch (Exception e) {
                return ItemStack.EMPTY;
            }
        }

        return ItemStack.EMPTY;
    }

    static ItemStack interactUseOnBlockWithoutItem(EntityMaid maid, BlockPos blockPos) {
        IMaidsoulKitchenMaid addonMaid = (IMaidsoulKitchenMaid) maid;
        WeakReference<FakePlayer> fakePlayer$tlma = addonMaid.tlmk$getFakePlayer();
        FakePlayer fakePlayer = fakePlayer$tlma.get();
        if (fakePlayer != null) {
            try {
                InteractionResult interactionResult = FakePlayerUtil.interactUseOnBlock(fakePlayer$tlma, maid.level, blockPos, InteractionHand.MAIN_HAND, null);

                if (interactionResult == InteractionResult.PASS) {
                    BlockState blockState = maid.level.getBlockState(blockPos);
                    Block block = blockState.getBlock();
                    LOGGER.warn("FakePlayerUtil.interactUseOnBlock PASS: items:{} blockstate: {}", blockState, block);
                    BLACK_LIST.add(block);
                    LOGGER.warn(BLACK_LIST.toString());
                }

                if (interactionResult != InteractionResult.PASS) {
                    ItemStack itemInHandCopy = fakePlayer.getItemInHand(InteractionHand.MAIN_HAND).copy();
                    ItemHandlerHelper.insertItemStacked(maid.getAvailableInv(true), itemInHandCopy, false);
                    fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                    return itemInHandCopy;
                } else {
                    fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                    return ItemStack.EMPTY;
                }

            } catch (Exception e) {
                return ItemStack.EMPTY;
            }
        }

        return ItemStack.EMPTY;
    }

    static InteractionResult interactUseOnBlock(EntityMaid maid, BlockPos targetPos, InteractionHand hand, @Nullable Direction facing) {
        FakePlayer fakePlayer = ((IMaidsoulKitchenMaid) maid).tlmk$getFakePlayer().get();
        Direction placementOn = (facing == null) ? fakePlayer.getMotionDirection() : facing;
        BlockHitResult blockraytraceresult = new BlockHitResult(
                fakePlayer.getLookAngle(), placementOn,
                targetPos, true);
        //processRightClick
        ItemStack itemInHand = fakePlayer.getItemInHand(hand);
        return fakePlayer.gameMode.useItemOn(fakePlayer, maid.level, itemInHand, hand, blockraytraceresult);
    }

    static void maidRightClick(EntityMaid maid, BlockPos targetPos, InteractionHand hand) {
        try {
            InteractionResult interactionResult = interactUseOnBlock(maid, targetPos, hand, null);
            if (interactionResult == InteractionResult.PASS) {
                BlockState blockState = maid.level.getBlockState(targetPos);
                Block block = blockState.getBlock();
                LOGGER.warn("FakePlayerUtil.interactUseOnBlock PASS: items:{} blockstate: {}", blockState, block);
                BLACK_LIST.add(block);
                LOGGER.warn(BLACK_LIST.toString());
            }
        }catch (Exception e) {
            LOGGER.error("FakePlayerUtil.interactUseOnBlock error: " + e);
        }
    }

    static void maidRightClick(EntityMaid maid, BlockPos targetPos) {
        maidRightClick(maid, targetPos, InteractionHand.MAIN_HAND);
    }

    static void pickupAction(EntityMaid maid) {
        maid.swing(InteractionHand.MAIN_HAND);
        maid.playSound(SoundEvents.ITEM_PICKUP, 1.0F, maid.getRandom().nextFloat() * 0.1F + 1.0F);
    }

    WeakReference<FakePlayer> tlmk$getFakePlayer();

    void tlmk$initFakePlayer();

}
