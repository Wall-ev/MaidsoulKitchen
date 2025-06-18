package com.github.wallev.maidsoulkitchen.task.farm.handler.berry.farmersrespite;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.wallev.maidsoulkitchen.util.modutility.Mods;
import com.github.wallev.verhelper.client.chat.VComponent;
import com.github.wallev.maidsoulkitchen.task.farm.handler.berry.BerryHandler;
import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import umpaz.farmersrespite.common.block.TeaBushBlock;

import java.util.List;
import java.util.Locale;

public abstract class FarmersRespiteTeaBerryHandler extends BerryHandler {

    protected boolean hasTool(EntityMaid maid) {
        return ItemsUtil.findStackSlot(maid.getAvailableInv(true), itemStack -> itemStack.is(Tags.Items.SHEARS)) > -1;
    }

    @Override
    protected boolean processHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        return this.harvestWithTool(maid, cropPos, cropState, itemStack -> itemStack.is(Tags.Items.SHEARS));
    }

    @Override
    public boolean canLoad() {
        return Mods.FRD.isLoaded;
    }

    @Override
    public boolean isFarmBlock(Block block) {
        return block instanceof TeaBushBlock;
    }

    public List<Component> getConditionDescription(EntityMaid maid) {
        String key = String.format("rule.%s.%s.%s.condition.hastool", getFarmType().name().toLowerCase(Locale.ENGLISH), getUid().getNamespace(), getUid().getPath());
        return Lists.newArrayList(VComponent.translatable(key).withStyle(this.hasTool(maid) ? ChatFormatting.GREEN : ChatFormatting.RED));
    }
}
