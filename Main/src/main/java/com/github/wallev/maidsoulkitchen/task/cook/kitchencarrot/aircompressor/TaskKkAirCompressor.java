package com.github.wallev.maidsoulkitchen.task.cook.kitchencarrot.aircompressor;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.FuelCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.task.AutoCookTaskRegister;
import io.github.tt432.kitchenkarrot.blockentity.AirCompressorBlockEntity;
import io.github.tt432.kitchenkarrot.recipes.recipe.AirCompressorRecipe;
import io.github.tt432.kitchenkarrot.registries.ModBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@AutoCookTaskRegister(TaskInfo.KK_AIR_COMPRESSOR)
@TaskClassAnalyzer(TaskInfo.KK_AIR_COMPRESSOR)
public class TaskKkAirCompressor extends ICookTask<AirCompressorBlockEntity, AirCompressorRecipe> {
    @Override
    protected AbstractCookRule<AirCompressorBlockEntity, AirCompressorRecipe> createCookRule() {
        return FuelCookRule.getInstance();
    }

    @Override
    protected RecSerializerManager<AirCompressorRecipe> createRecSerializerManager() {
        return AirCompressorRecSerializerManager.getInstance();
    }

    @Override
    protected CookBeBase<AirCompressorBlockEntity> createCookBe(EntityMaid maid) {
        return new AirCompressorBe(maid);
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.KK_AIR_COMPRESSOR.getUid();
    }

    @Override
    protected Index indexEnum() {
        return Index.KK_AIR_COMPRESSOR;
    }

    @Override
    public ItemStack getIcon() {
        return ModBlocks.AIR_COMPRESSOR.get().asItem().getDefaultInstance();
    }
}
