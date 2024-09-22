package com.github.catbert.tlma.task.farm;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.api.ILittleMaidTask;
import com.github.catbert.tlma.api.task.IAddonFarmTask;
import com.github.catbert.tlma.foundation.utility.Mods;
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
import java.util.Locale;


public class TaskSSFarm extends TaskNormalFarm implements ILittleMaidTask, IAddonFarmTask {
    public static final ResourceLocation NAME = new ResourceLocation(TLMAddon.MOD_ID, "sereneseasons_farm");

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
