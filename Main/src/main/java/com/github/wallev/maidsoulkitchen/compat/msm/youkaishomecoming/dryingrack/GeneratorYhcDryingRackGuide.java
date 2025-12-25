package com.github.wallev.maidsoulkitchen.compat.msm.youkaishomecoming.dryingrack;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.plate.IPlateGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.TypeLang;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackBlock;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import studio.fantasyit.maid_storage_manager.craft.generator.config.ConfigTypes;

@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_YHC_DRYING_RACK)
public class GeneratorYhcDryingRackGuide extends IPlateGuideGenerator<DryingRackRecipe> {

    @TypeLang(
            en_us = "The number of each drying",
            zh_cn = "每次晾晒的数量"
    )
    protected ConfigTypes.ConfigType<Integer> COUNT_VIRTUAL = COUNT;

    @Override
    public int waitTime(DryingRackRecipe recipe) {
        return recipe.getCookingTime();
    }

    @Override
    public int getMaxSingleCount() {
        return 4;
    }

    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return level.canSeeSky(pos) && level.isDay() && !level.isRainingAt(pos);
    }

    @Override
    @NotNull
    public ResourceLocation getType() {
        return VResourceLocation.ofTypeMod(YHBlocks.RACK_RT.getId());
    }

    @Override
    public int getRecipeTime(DryingRackRecipe recipe) {
        return recipe.getCookingTime();
    }

    @Override
    public Item getBlockItemForTranslate() {
        return YHBlocks.RACK.asItem();
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return level.getBlockState(pos).getBlock() instanceof DryingRackBlock;
    }

    @Override
    public RecipeType<DryingRackRecipe> getRecipeType() {
        return YHBlocks.RACK_RT.get();
    }
}
