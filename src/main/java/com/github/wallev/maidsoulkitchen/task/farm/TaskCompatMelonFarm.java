package com.github.wallev.maidsoulkitchen.task.farm;

import com.github.wallev.maidsoulkitchen.api.IMaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.api.task.IAddonFarmTask;
import com.github.wallev.maidsoulkitchen.event.MaidMkTaskEnableEvent;
import com.github.wallev.maidsoulkitchen.inventory.container.maid.CompatMelonConfigContainer;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskMelon;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import com.github.wallev.maidsoulkitchen.event.MelonConfigEvent;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.MinecraftForge;

import static com.github.wallev.maidsoulkitchen.util.BlockUtil.getId;

public class TaskCompatMelonFarm extends TaskMelon implements IMaidsoulKitchenTask, IAddonFarmTask {
    @Override
    public boolean isEnable(EntityMaid maid) {
        return !MinecraftForge.EVENT_BUS.post(new MaidMkTaskEnableEvent(maid, this));
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.COMPAT_MELON_FARM.uid;
    }

    @Override
    public boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        Block block = cropState.getBlock();
        if (MelonConfigEvent.MELON_STEM_MAP.containsKey(BlockUtil.getId(block))) {
            String stemBlockId = MelonConfigEvent.MELON_STEM_MAP.get(BlockUtil.getId(block));
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockState offsetState = maid.level.getBlockState(cropPos.relative(direction));
                if (BlockUtil.getId(offsetState).equals(stemBlockId)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        Block block = cropState.getBlock();
        if (MelonConfigEvent.MELON_STEM_MAP.containsKey(BlockUtil.getId(block))) {
            ItemStack mainHandItem = maid.getMainHandItem();
            if (EnchantmentHelper.hasSilkTouch(mainHandItem)) {
                if (this.destroyBlockByHandItem(maid, cropPos)) {
                    mainHandItem.hurtAndBreak(1, maid, (e) -> {
                        e.broadcastBreakEvent(InteractionHand.MAIN_HAND);
                    });
                }
            } else {
                maid.destroyBlock(cropPos);
            }
        } else {
            super.harvest(maid, cropPos, cropState);
        }
    }

    public boolean destroyBlockByHandItem(EntityMaid maid, BlockPos pos) {
        return this.destroyBlockByHandItem(maid, pos, true);
    }

    public boolean destroyBlockByHandItem(EntityMaid maid, BlockPos pos, boolean dropBlock) {
        return maid.canDestroyBlock(pos) && this.destroyBlockByHandItem(maid, maid.level, pos, dropBlock);
    }

    private boolean destroyBlockByHandItem(EntityMaid maid, Level level, BlockPos blockPos, boolean dropBlock) {
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.isAir()) {
            return false;
        } else {
            FluidState fluidState = level.getFluidState(blockPos);
            if (!(blockState.getBlock() instanceof BaseFireBlock)) {
                level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, blockPos, Block.getId(blockState));
            }
            if (dropBlock) {
                BlockEntity blockEntity = blockState.hasBlockEntity() ? level.getBlockEntity(blockPos) : null;
                maid.dropResourcesToMaidInv(blockState, level, blockPos, blockEntity, maid, maid.getMainHandItem());
            }
            boolean setResult = level.setBlock(blockPos, fluidState.createLegacyBlock(), Block.UPDATE_ALL);
            if (setResult) {
                level.gameEvent(GameEvent.BLOCK_DESTROY, blockPos, GameEvent.Context.of(maid, blockState));
            }
            return setResult;
        }
    }

    @Override
    public MenuProvider getTaskConfigGuiProvider(EntityMaid maid) {
        final int entityId = maid.getId();
        return new MenuProvider() {
            public Component getDisplayName() {
                return Component.literal("Maid Task Config Container");
            }

            public AbstractMaidContainer createMenu(int index, Inventory playerInventory, Player player) {
                return new CompatMelonConfigContainer(index, playerInventory, entityId);
            }
        };
    }

    @Override
    public String getBookEntry() {
        return "melon";
    }
}
