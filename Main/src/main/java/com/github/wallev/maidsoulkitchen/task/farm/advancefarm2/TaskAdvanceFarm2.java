package com.github.wallev.maidsoulkitchen.task.farm.advancefarm2;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.IMaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.init.ModEntities;
import com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.ai.MaidAdvancedFarmHarvestTask;
import com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.ai.MaidAdvancedFarmMoveTask;
import com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.ai.core.MaidDestroyTask;
import com.github.wallev.maidsoulkitchen.vhelper.server.ai.VBehaviorControl;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TaskAdvanceFarm2 implements IMaidsoulKitchenTask {

    public static final ResourceLocation UID = new ResourceLocation("maidsoulkitchen", "advance_farm_2");

    @Override
    public List<Pair<Integer, VBehaviorControl>> vCreateBrainTasks(EntityMaid maid) {
        MaidAdvancedFarmMoveTask maidAdvancedFarmMoveTask = new MaidAdvancedFarmMoveTask();
        MaidAdvancedFarmHarvestTask maidAdvancedFarmHarvestTask = new MaidAdvancedFarmHarvestTask();

//        if (false) {
//            maidAdvancedFarmMoveTask = new MaidAdvancedFarmMoveTaskWithEsFarm();
//            maidAdvancedFarmHarvestTask = new MaidAdvancedFarmHarvestTaskWithEsFarm();
//        }

        return Lists.newArrayList(
                new Pair<>(5, maidAdvancedFarmMoveTask),
                new Pair<>(5, maidAdvancedFarmHarvestTask),
                new Pair<>(5, new MaidDestroyTask())
        );
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return Items.NETHERITE_HOE.getDefaultInstance();
    }

    @Override
    public @Nullable SoundEvent getAmbientSound(EntityMaid maid) {
        return null;
    }

    @Override
    public boolean enableLookAndRandomWalk(EntityMaid maid) {
        return !maid.getBrain().hasMemoryValue(ModEntities.HARVEST_DATA.get());
    }
}
