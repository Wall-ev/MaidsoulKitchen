package com.github.wallev.maidsoulkitchen.task.farm;

import com.github.wallev.maidsoulkitchen.api.IMaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.api.task.IAddonFarmTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskNormalFarm;
import com.github.wallev.maidsoulkitchen.event.MaidMkTaskEnableEvent;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import sereneseasons.api.season.SeasonHelper;

import java.util.List;
import java.util.Locale;


public class TaskSsFarm extends TaskNormalFarm implements IMaidsoulKitchenTask, IAddonFarmTask {
    @Override
    public boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        boolean plantB = super.canPlant(maid, basePos, baseState, seed);
        if (plantB) {
            String lowerCase = SeasonHelper.getSeasonState(maid.level()).getSeason().name().toLowerCase(Locale.ENGLISH);
            TagKey<Item> itemTagKey = ItemTags.create(new ResourceLocation("sereneseasons:" + lowerCase + "_crops"));
            return seed.getTags().anyMatch(itemTagKey::equals) ||
                    seed.getTags().noneMatch(tagKey -> tagKey.location().toString().matches("sereneseasons:.*_crops"));
        }

        return false;
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.SERENESEASONS_FARM.uid;
    }

    @Override
    public boolean isEnable(EntityMaid maid) {
        MaidMkTaskEnableEvent maidMkTaskEnableEvent = new MaidMkTaskEnableEvent(maid, this);
        MinecraftForge.EVENT_BUS.post(maidMkTaskEnableEvent);
        return maidMkTaskEnableEvent.isEnable();
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        return super.createBrainTasks(maid);
    }

    @Override
    public String getBookEntry() {
        return "sereneseasons_farm";
    }
}
