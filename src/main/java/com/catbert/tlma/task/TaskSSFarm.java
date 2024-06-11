package com.catbert.tlma.task;

import com.catbert.tlma.TLMAddon;
import com.catbert.tlma.api.ILittleMaidTask;
import com.catbert.tlma.foundation.utility.Mods;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskNormalFarm;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import sereneseasons.api.season.SeasonHelper;

import java.util.List;

@LittleMaidExtension
public class TaskSSFarm extends TaskNormalFarm implements ILittleMaidTask {
    public static final ResourceLocation NAME = new ResourceLocation(TLMAddon.MOD_ID, "sereneseasons_farm");

    @Override
    public boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        boolean plantB = super.canPlant(maid, basePos, baseState, seed);
        if (plantB) {
            String lowerCase = SeasonHelper.getSeasonState(maid.level()).getSeason().name().toLowerCase();
            TagKey<Item> itemTagKey = ItemTags.create(new ResourceLocation("sereneseasons:" + lowerCase + "_crops"));
            boolean b = seed.getTags().anyMatch(itemTagKey::equals);
            return b || seed.getTags().noneMatch(tagKey -> tagKey.location().toString().matches("sereneseasons:.*_crops"));
//            if (!b) {
//                TLMCompat.LOGGER.info("TaskSSFarm canPlant false " + lowerCase + " " + b);
//                boolean b1 = seed.getTags().noneMatch(tagKey -> tagKey.location().toString().matches("sereneseasons:.*_crops"));
//                TLMCompat.LOGGER.info(seed.getTags().map(tagKey -> tagKey.location().toString()) + " " + b1);
//            }else {
//                return true;
//            }
        }

        return false;
    }

    @Override
    public boolean modLoaded() {
        return Mods.SS.isLoaded;
    }

//    @Override
//    public boolean configLoaded() {
//        return TaskConfig.SERENESEASONS_FARM_TASK_ENABLED.get();
//    }

    @Override
    public ResourceLocation getUid() {
        return NAME;
    }

    @Override
    public boolean isEnable(EntityMaid maid) {
        return true;
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        return super.createBrainTasks(maid);
    }
}
