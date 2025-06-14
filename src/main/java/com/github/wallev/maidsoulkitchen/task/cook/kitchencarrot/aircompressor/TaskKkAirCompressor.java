package com.github.wallev.maidsoulkitchen.task.cook.kitchencarrot.aircompressor;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.DataRegister;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.FuelCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import io.github.tt432.kitchenkarrot.blockentity.AirCompressorBlockEntity;
import io.github.tt432.kitchenkarrot.recipes.recipe.AirCompressorRecipe;
import io.github.tt432.kitchenkarrot.registries.ModBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

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
    public TaskDataKey<CookData> getCookDataKey() {
        return DataRegister.KC_AIR_COMPRESSOR;
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.KK_AIR_COMPRESSOR.uid;
    }

    @Override
    public ItemStack getIcon() {
        return ModBlocks.AIR_COMPRESSOR.get().asItem().getDefaultInstance();
    }
}
