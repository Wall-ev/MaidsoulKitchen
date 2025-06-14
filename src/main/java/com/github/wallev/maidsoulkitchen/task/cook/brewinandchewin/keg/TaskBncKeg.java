package com.github.wallev.maidsoulkitchen.task.cook.brewinandchewin.keg;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.DataRegister;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidRecipesManager2;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.FluidPotCookRule1;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;
import umpaz.brewinandchewin.common.crafting.KegFermentingRecipe;
import umpaz.brewinandchewin.common.registry.BnCBlocks;

public class TaskBncKeg extends ICookTask<KegBlockEntity, KegFermentingRecipe> {

    @Override
    protected CookBeBase<KegBlockEntity> createCookBe(EntityMaid maid) {
        return new KegCookBe(maid);
    }

    @Override
    protected AbstractCookRule<KegBlockEntity, KegFermentingRecipe> createCookRule() {
        return FluidPotCookRule1.getInstance();
    }

    @Override
    protected RecSerializerManager<KegFermentingRecipe> createRecSerializerManager() {
        return new KegRecSerializerManager();
    }

    @Override
    protected MaidRecipesManager2<KegFermentingRecipe> createRecipesManager(EntityMaid maid, CookBeBase<KegBlockEntity> cookBe) {
        return new MaidKegRecipesManager(recSerializerManager, maid, this, cookBe);
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return DataRegister.BNC_KEY;
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.BNC_KEY.uid;
    }

    @Override
    public ItemStack getIcon() {
        return BnCBlocks.KEG.get().asItem().getDefaultInstance();
    }
}
