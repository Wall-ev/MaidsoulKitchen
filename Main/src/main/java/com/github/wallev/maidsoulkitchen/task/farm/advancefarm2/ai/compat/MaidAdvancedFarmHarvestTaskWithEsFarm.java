package com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.ai.compat;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.ai.MaidAdvancedFarmHarvestTask;
import com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.handler.ICropHarvest;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.api.constant.crop.CropSeasonInfo;
import com.teamtea.eclipticseasons.api.constant.solar.Season;
import com.teamtea.eclipticseasons.common.core.crop.CropInfoManager;
import com.teamtea.eclipticseasons.config.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class MaidAdvancedFarmHarvestTaskWithEsFarm extends MaidAdvancedFarmHarvestTask {

    @Override
    protected ICropHarvest.Result harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, ICropHarvest.HarvestData harvestData) {
        if (CommonConfig.Crop.enableCrop.get()) {
            CropSeasonInfo seasonInfo = CropInfoManager.getSeasonInfo(cropState.getBlock());
            if (seasonInfo != null) {
                Season season = EclipticSeasonsApi.getInstance().getSolarTerm(maid.level()).getSeason();
                if (!seasonInfo.isSuitable(season)) {
                    maid.destroyBlock(harvestData.getLookPos());
                    return ICropHarvest.Result.SUCCESS;
                }
            }
        }

       return super.harvest(maid, cropPos, cropState, harvestData);
    }

    @Override
    protected boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, ICropHarvest.HarvestData harvestData) {
        if (CommonConfig.Crop.enableCrop.get()) {
            CropSeasonInfo seasonInfo = CropInfoManager.getSeasonInfo(cropState.getBlock());
            if (seasonInfo != null) {
                Season season = EclipticSeasonsApi.getInstance().getSolarTerm(maid.level()).getSeason();
                return !seasonInfo.isSuitable(season);
            }
        }

        return super.canHarvest(maid, cropPos, cropState, harvestData);
    }
}
