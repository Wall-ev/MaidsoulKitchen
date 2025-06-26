package com.github.wallev.maidsoulkitchen.task.cook.farmersrespite.kettle;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.DataRegister;
import com.github.wallev.maidsoulkitchen.task.MaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.FluidPotCookRule1;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import umpaz.farmersrespite.common.block.entity.KettleBlockEntity;
import umpaz.farmersrespite.common.crafting.KettleRecipe;
import umpaz.farmersrespite.common.registry.FRItems;

@TaskClassAnalyzer(TaskInfo.FR_KETTLE)
public class TaskFrKettle extends ICookTask<KettleBlockEntity, KettleRecipe> {

    @Override
    protected CookBeBase<KettleBlockEntity> createCookBe(EntityMaid maid) {
        return new KettleCookBe(maid);
    }

    @Override
    protected AbstractCookRule<KettleBlockEntity, KettleRecipe> createCookRule() {
        return FluidPotCookRule1.getInstance();
    }

    @Override
    protected RecSerializerManager<KettleRecipe> createRecSerializerManager() {
        return FrKettleRecSerializerManager.getInstance();
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return DataRegister.FR_KETTLE;
    }

    @Override
    public ResourceLocation getUid() {
        return MaidsoulKitchenTask.FR_KETTLE.uid;
    }

    @Override
    public ItemStack getIcon() {
        return FRItems.KETTLE.get().getDefaultInstance();
    }
}
