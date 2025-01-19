package com.github.wallev.maidsoulkitchen.task.other;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.api.ILittleMaidTask;
import com.github.wallev.maidsoulkitchen.task.ai.MaidDestroyBlockTask;
import com.github.wallev.maidsoulkitchen.task.ai.MaidMoveToTreeTask;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import me.pandamods.fallingtrees.api.Tree;
import me.pandamods.fallingtrees.api.TreeDataBuilder;
import me.pandamods.fallingtrees.api.TreeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class TaskPdfFallingTree implements ILittleMaidTask {
    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(MaidsoulKitchen.MOD_ID, "falling_tree");
    }

    @Override
    public ItemStack getIcon() {
        return Items.SPRUCE_SAPLING.getDefaultInstance();
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound(EntityMaid entityMaid) {
        return null;
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid entityMaid) {
        MaidMoveToTreeTask maidMoveToBlockTask = new MaidMoveToTreeTask(0.6f) {
            @Override
            protected boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid entityMaid, BlockPos blockPos) {
                BlockState blockState = serverLevel.getBlockState(blockPos);
                boolean isLog = blockState.is(BlockTags.LOGS);
                boolean belowIsNotLog = !serverLevel.getBlockState(blockPos.below()).is(BlockTags.LOGS);
                if (isLog && belowIsNotLog) {
                    Optional<Tree<?>> tree = TreeRegistry.getTree(blockState);
                    return tree.isPresent() && tree.get().getTreeData(new TreeDataBuilder(), blockPos, serverLevel).shouldFall();
                }
                return false;
            }
        };
        MaidDestroyBlockTask maidDestroyBlockTask = new MaidDestroyBlockTask();
        return Lists.newArrayList(Pair.of(5, maidMoveToBlockTask), Pair.of(5, maidDestroyBlockTask));
    }
}
