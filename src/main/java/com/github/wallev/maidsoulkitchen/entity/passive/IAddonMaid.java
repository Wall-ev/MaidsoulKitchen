package com.github.wallev.maidsoulkitchen.entity.passive;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.util.FakePlayerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.ItemHandlerHelper;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

import static com.github.wallev.maidsoulkitchen.MaidsoulKitchen.LOGGER;

public interface IAddonMaid {
    Set<Block> BLACK_LIST = new HashSet<>();

    static ItemStack interactUseOnBlockWithItem(EntityMaid maid, BlockPos blockPos, ItemStack itemStack) {
        IAddonMaid addonMaid = (IAddonMaid) maid;
        WeakReference<FakePlayer> fakePlayer$tlma = addonMaid.tlmk$getFakePlayer();
        FakePlayer fakePlayer = fakePlayer$tlma.get();
        if (fakePlayer != null) {
            try {
                fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
                InteractionResult interactionResult = FakePlayerUtil.interactUseOnBlock(fakePlayer$tlma, maid.level(), blockPos, InteractionHand.MAIN_HAND, null);

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
        IAddonMaid addonMaid = (IAddonMaid) maid;
        WeakReference<FakePlayer> fakePlayer$tlma = addonMaid.tlmk$getFakePlayer();
        FakePlayer fakePlayer = fakePlayer$tlma.get();
        if (fakePlayer != null) {
            try {
                InteractionResult interactionResult = FakePlayerUtil.interactUseOnBlock(fakePlayer$tlma, maid.level(), blockPos, InteractionHand.MAIN_HAND, null);

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

    WeakReference<FakePlayer> tlmk$getFakePlayer();

    void tlmk$initFakePlayer();

}
