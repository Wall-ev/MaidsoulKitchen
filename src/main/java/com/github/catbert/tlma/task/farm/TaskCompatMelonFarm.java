package com.github.catbert.tlma.task.farm;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.api.ILittleMaidTask;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskMelon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import static com.github.catbert.tlma.event.TaskMelonEvent.MELON_STEM_MAP;

@LittleMaidExtension
public class TaskCompatMelonFarm extends TaskMelon implements ILittleMaidTask {
    public static final ResourceLocation UID = new ResourceLocation(TLMAddon.MOD_ID, "compat_melon");

    @Override
    public boolean canLoaded() {
        return true;
    }

    @Override
    public boolean isEnable(EntityMaid maid) {
        return true;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        boolean canHarvest = super.canHarvest(maid, cropPos, cropState);
        if (canHarvest) return true;

        Block block = cropState.getBlock();
//         STEM_MELON_MAP: melon, stem
        if (MELON_STEM_MAP.containsKey(block)) {
            Block stemBlock = MELON_STEM_MAP.get(block).getFirst();
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockState offsetState = maid.level().getBlockState(cropPos.relative(direction));
                if (offsetState.is(stemBlock)) {
                    return true;
                }
            }
        }
        return false;

//         STEM_MELON_MAP: stem, melon
//        if (STEM_MELON_MAP.containsKey(block)) {
//            Direction direction = cropState.getValue(HorizontalDirectionalBlock.FACING);
//            Block melonBlock = STEM_MELON_MAP.get(block);
//            return maid.level().getBlockState(cropPos.relative(direction)).is(melonBlock);
//        }
    }

    @Override
    public void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        Block block = cropState.getBlock();
        if (MELON_STEM_MAP.containsKey(block)) {
            ItemStack mainHandItem = maid.getMainHandItem();
            if (EnchantmentHelper.hasSilkTouch(mainHandItem)) {
                Item melonItem = MELON_STEM_MAP.get(block).getSecond();
                if (maid.destroyBlock(cropPos, false)) {
                    mainHandItem.hurtAndBreak(1, maid, (e) -> {
                        e.broadcastBreakEvent(InteractionHand.MAIN_HAND);
                    });
                    Block.popResource(maid.level(), cropPos, melonItem.getDefaultInstance());
                }
            } else {
                maid.destroyBlock(cropPos);
            }
        } else {
            super.harvest(maid, cropPos, cropState);
        }
    }
}
