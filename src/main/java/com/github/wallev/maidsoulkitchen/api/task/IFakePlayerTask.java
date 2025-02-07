package com.github.wallev.maidsoulkitchen.api.task;

import com.github.wallev.maidsoulkitchen.entity.passive.IAddonMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

import static com.github.wallev.maidsoulkitchen.MaidsoulKitchen.LOGGER;

// from:https://github.com/Lothrazar/Cyclic/blob/179e693db439a48822e2f47dfd0f86466f02063c/src/main/java/com/lothrazar/cyclic/block/user/TileUser.java#L84
// 暂时先这样...再改...
public interface IFakePlayerTask {
    Set<Block> blackList = new HashSet<>();

    static InteractionResult interactUseOnBlock(EntityMaid maid, BlockPos targetPos, InteractionHand hand, @Nullable Direction facing) {
        FakePlayer fakePlayer = ((IAddonMaid) maid).tlmk$getFakePlayer().get();
        Direction placementOn = (facing == null) ? fakePlayer.getMotionDirection() : facing;
        BlockHitResult blockraytraceresult = new BlockHitResult(
                fakePlayer.getLookAngle(), placementOn,
                targetPos, true);
        //processRightClick
        ItemStack itemInHand = fakePlayer.getItemInHand(hand);
        return fakePlayer.gameMode.useItemOn(fakePlayer, maid.level(), itemInHand, hand, blockraytraceresult);
    }

    static void maidRightClick(EntityMaid maid, BlockPos targetPos, InteractionHand hand) {
        try {
//            LOGGER.info("FakePlayerUtil.interactUseOnBlock ");
            InteractionResult interactionResult = interactUseOnBlock(maid, targetPos, hand, null);
            if (interactionResult == InteractionResult.PASS) {
                BlockState blockState = maid.level.getBlockState(targetPos);
                Block block = blockState.getBlock();
                LOGGER.warn("FakePlayerUtil.interactUseOnBlock PASS: items:{} blockstate: {}", blockState, block);
                blackList.add(block);
                LOGGER.warn(blackList.toString());
            }
//            LOGGER.info("FakePlayerUtil.interactUseOnBlock result: " + interactionResult);
        }catch (Exception e) {
            LOGGER.error("FakePlayerUtil.interactUseOnBlock error: " + e);
        }
    }

    static void maidRightClick(EntityMaid maid, BlockPos targetPos) {
        maidRightClick(maid, targetPos, InteractionHand.MAIN_HAND);
    }

}
