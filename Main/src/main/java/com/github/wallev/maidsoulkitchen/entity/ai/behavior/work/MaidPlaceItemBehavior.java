package com.github.wallev.maidsoulkitchen.entity.ai.behavior.work;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.entity.ai.behavior.manager.BehaviorType;
import com.github.wallev.maidsoulkitchen.entity.ai.behavior.manager.MaidBehaviorManager;
import com.github.wallev.maidsoulkitchen.init.ModEntities;
import com.github.wallev.maidsoulkitchen.util.InvUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.util.Objects;
import java.util.Optional;

@MaidBehaviorManager.BehaviorAutoRegister
public class MaidPlaceItemBehavior implements IMaidBehavior {
    public record PlaceData(BlockPos pos, ItemStack stack, int slot) {
        public PlaceData(BlockPos pos, ItemStack stack) {
            this(pos, stack, -1);
        }

        @Nullable
        public static PlaceData get(EntityMaid maid) {
            return maid.getBrain().getMemory(ModEntities.PLACE_DATA.get()).orElse(null);
        }

        public static void set(EntityMaid maid, PlaceData placeData) {
            maid.getBrain().setMemory(ModEntities.PLACE_DATA.get(), placeData);
        }

        public static void erase(EntityMaid maid) {
            maid.getBrain().eraseMemory(ModEntities.PLACE_DATA.get());
        }

    }
    @Override
    public ResourceLocation getUid() {
        return BehaviorType.PLACE_ITEM.getUid();
    }

    @Override
    public boolean canStillUse(ServerLevel serverLevel, EntityMaid maid, long gameTime) {
        return this.checkExtraStartConditions(serverLevel, maid) && PlaceData.get(maid) != null;
    }

    @Override
    public boolean checkExtraStartConditions(ServerLevel serverLevel, EntityMaid maid) {

        return Optional.ofNullable(MaidPlaceItemBehavior.PlaceData.get(maid)).map(placeData -> {
            BlockPos blockPos = placeData.pos();
            if (blockPos != null && canReachBlockByJump(maid, blockPos)) {
                return true;
            }
            return false;
        }).orElse(false);
    }

    @Override
    public void start(ServerLevel serverLevel, EntityMaid maid, long gameTime) {
        PlaceData placeData = Objects.requireNonNull(PlaceData.get(maid));
        Pair<ItemStack, Integer> pair = findItemInInventory(maid, placeData);
        if (pair == null) {
            return;
        }
        boolean placed = maid.placeItemBlock(placeData.pos(), pair.getA());
        if (placed) {
            maid.getAvailableInv(true).setStackInSlot(pair.getB(), pair.getA());
        }
        PlaceData.erase(maid);
    }

    Pair<ItemStack, Integer> findItemInInventory(EntityMaid maid, PlaceData placeData) {
        ItemStack stack = placeData.stack();
        CombinedInvWrapper availableInv = maid.getAvailableInv(true);
        if (placeData.slot() >= 0) {
            ItemStack stackInSlot = availableInv.getStackInSlot(placeData.slot());
            if (ItemStack.matches(stack, stackInSlot)) {
                return new Pair<>(stackInSlot, placeData.slot());
            }
        }
        int stackSlot = InvUtil.findStackSlot(availableInv, stackInSlot -> ItemStack.matches(stack, stackInSlot));
        return stackSlot >= 0 ? new Pair<>(availableInv.getStackInSlot(stackSlot), stackSlot) : null;
    }

    @Override
    public void tick(ServerLevel serverLevel, EntityMaid maid, long gameTime) {

    }

    @Override
    public void stop(ServerLevel serverLevel, EntityMaid maid, long gameTime) {
        PlaceData.erase(maid);
    }

    private boolean canReachBlockByJump(EntityMaid maid, BlockPos blockPos) {
        return maid.blockPosition().closerThan(blockPos, 2.0);
//        return maid.blockPosition().closerThan(blockPos, maid.getMaxBlockReach() + 4);
    }

    public static void set(EntityMaid maid) {
        MaidBehaviorManager.set(maid, BehaviorType.PLACE_ITEM);
    }
}
