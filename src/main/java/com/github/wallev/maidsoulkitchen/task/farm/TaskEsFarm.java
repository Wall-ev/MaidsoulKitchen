package com.github.wallev.maidsoulkitchen.task.farm;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskNormalFarm;
import com.github.wallev.maidsoulkitchen.api.IMaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.api.task.IAddonFarmTask;
import com.github.wallev.maidsoulkitchen.api.event.MaidMkTaskEnableEvent;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.mojang.datafixers.util.Pair;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.api.constant.crop.CropSeasonInfo;
import com.teamtea.eclipticseasons.api.constant.solar.Season;
import com.teamtea.eclipticseasons.common.core.crop.CropInfoManager;
import com.teamtea.eclipticseasons.common.handler.SolarUtil;
import com.teamtea.eclipticseasons.config.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;

import java.util.List;

@TaskClassAnalyzer(com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo.ECLIPTICSSEASONS_FARM)
public class TaskEsFarm extends TaskNormalFarm implements IMaidsoulKitchenTask, IAddonFarmTask {
    @Override
    public boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        boolean plantB = super.canPlant(maid, basePos, baseState, seed);
        if (plantB && CommonConfig.Crop.enableCrop.get() && seed.getItem() instanceof BlockItem blockItem) {
            CropSeasonInfo seasonInfo = CropInfoManager.getSeasonInfo(blockItem.getBlock());
            Season season = EclipticSeasonsApi.getInstance().getSolarTerm(maid.level).getSeason();
            return seasonInfo != null && seasonInfo.isSuitable(season);
        }

        return plantB;
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.ECLIPTICSSEASONS_FARM.uid;
    }

    @Override
    public boolean isEnable(EntityMaid maid) {
        MaidMkTaskEnableEvent maidMkTaskEnableEvent = new MaidMkTaskEnableEvent(maid, this);
        NeoForge.EVENT_BUS.post(maidMkTaskEnableEvent);
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
