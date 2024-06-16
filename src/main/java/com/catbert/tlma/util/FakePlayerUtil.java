package com.catbert.tlma.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.lang.ref.WeakReference;
import java.util.UUID;

import static com.catbert.tlma.TLMAddon.LOGGER;

public class FakePlayerUtil {
    private static final UUID ID = UUID.randomUUID();

    public static boolean isFakePlayer(Entity attacker) {
        return attacker instanceof FakePlayer;
    }

    public static WeakReference<FakePlayer> initFakePlayer(ServerLevel ws, String blockName) {
        final String name = "fake_player." + blockName;
        final GameProfile breakerProfile = new GameProfile(ID, name);
        WeakReference<FakePlayer> fakePlayer = new WeakReference<FakePlayer>(FakePlayerFactory.get(ws, breakerProfile));
        if (fakePlayer == null || fakePlayer.get() == null) {
            fakePlayer = null;
            return null; // trying to get around https://github.com/PrinceOfAmber/Cyclic/issues/113
        }
        fakePlayer.get().setOnGround(true);
        //    fakePlayer.get().onGround = true;
        fakePlayer.get().connection = new ServerGamePacketListenerImpl(ws.getServer(), new Connection(PacketFlow.SERVERBOUND), fakePlayer.get()) {

            @Override
            public void send(Packet<?> packetIn) {}
        };
        fakePlayer.get().setSilent(true);
        return fakePlayer;
    }

    public static WeakReference<FakePlayer> setupBeforeTrigger(ServerLevel sw, String name, Entity entity) {
        WeakReference<FakePlayer> fakePlayer = FakePlayerUtil.initFakePlayer(sw, name);
        if (fakePlayer == null) {
            LOGGER.error("Fake player failed to init " + name);
            return null;
        }
        //fake player facing the same direction as tile. for throwables
//        fakePlayer.get().setPos(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ()); //seems to help interact() mob drops like milk
        fakePlayer.get().setPos(entity.getX(), entity.getY(), entity.getZ()); //seems to help interact() mob drops like milk
//        fakePlayer.get().setYRot(EntityUtil.getYawFromFacing(this.getCurrentFacing()));
        fakePlayer.get().setYRot(EntityUtil.getYawFromFacing(Direction.UP));
        return fakePlayer;
    }

    public static InteractionResult interactUseOnBlock(WeakReference<FakePlayer> fakePlayer,
                                                       Level world, BlockPos targetPos, InteractionHand hand, Direction facing){
        if (fakePlayer == null) {
            return InteractionResult.FAIL;
        }
        Direction placementOn = (facing == null) ? fakePlayer.get().getMotionDirection() : facing;
        BlockHitResult blockraytraceresult = new BlockHitResult(
                fakePlayer.get().getLookAngle(), placementOn,
                targetPos, true);
        //processRightClick
        ItemStack itemInHand = fakePlayer.get().getItemInHand(hand);
        InteractionResult result = fakePlayer.get().gameMode.useItemOn(fakePlayer.get(), world, itemInHand, hand, blockraytraceresult);
//        InteractionResult result = fakePlayer.get().gameMode.useItemOn(fakePlayer.get(), world, ItemStack.EMPTY, hand, blockraytraceresult);
        // ModCyclic.LOGGER.info(targetPos + " gameMode.useItemOn() result = " + result + "  itemInHand = " + itemInHand);
        //it becomes CONSUME result 1 bucket. then later i guess it doesnt save, and then its water_bucket again
        return result;
    }


    public static InteractionResult interactUseOnBlockByDiscrete(WeakReference<FakePlayer> fakePlayer,
                                                       Level world, BlockPos targetPos, ItemStack itemStack, Direction facing){
        if (fakePlayer == null) {
            return InteractionResult.FAIL;
        }
        FakePlayer fakePlayer1 = fakePlayer.get();
        Direction placementOn = (facing == null) ? fakePlayer1.getMotionDirection() : facing;
        BlockHitResult blockraytraceresult = new BlockHitResult(fakePlayer1.getLookAngle(), placementOn, targetPos, true);
        fakePlayer1.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        fakePlayer1.setShiftKeyDown(true);
        fakePlayer1.setPose(Pose.CROUCHING);
        InteractionResult result = fakePlayer1.gameMode.useItemOn(fakePlayer1, world, itemStack, InteractionHand.MAIN_HAND, blockraytraceresult);

        fakePlayer1.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        fakePlayer1.setShiftKeyDown(false);
        /*
            todo
            恢复姿势(?怎么做)
         */
//        fakePlayer1.setPose(Pose.CROUCHING);

        return result;
    }
}
