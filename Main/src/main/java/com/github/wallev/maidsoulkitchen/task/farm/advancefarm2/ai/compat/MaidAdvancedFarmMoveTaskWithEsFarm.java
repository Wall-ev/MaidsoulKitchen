package com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.ai.compat;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.ai.MaidAdvancedFarmMoveTask;
import com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.handler.ICropHarvest;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.api.constant.crop.CropSeasonInfo;
import com.teamtea.eclipticseasons.api.constant.solar.Season;
import com.teamtea.eclipticseasons.common.core.crop.CropInfoManager;
import com.teamtea.eclipticseasons.config.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public class MaidAdvancedFarmMoveTaskWithEsFarm extends MaidAdvancedFarmMoveTask {

    @Override
    protected boolean ruleCanMoveTo(ICropHarvest rule, BlockState cropState, ServerLevel worldIn, EntityMaid maid, BlockPos cropPos, MaidPathFindingBFS pathFinding, ICropHarvest.HarvestData data) {
        if (CommonConfig.Crop.enableCrop.get()) {
            CropSeasonInfo seasonInfo = CropInfoManager.getSeasonInfo(cropState.getBlock());
            if (seasonInfo != null) {
                Season season = EclipticSeasonsApi.getInstance().getSolarTerm(worldIn).getSeason();
                return !seasonInfo.isSuitable(season);
            }
        }
        return super.ruleCanMoveTo(rule, cropState, worldIn, maid, cropPos, pathFinding, data);
    }
}
