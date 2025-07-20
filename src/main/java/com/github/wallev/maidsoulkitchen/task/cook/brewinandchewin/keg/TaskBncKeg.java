package com.github.wallev.maidsoulkitchen.task.cook.brewinandchewin.keg;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.task.CookTask;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.MaidCookManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.FluidPotCookRule1;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;
import umpaz.brewinandchewin.common.crafting.KegFermentingRecipe;
import umpaz.brewinandchewin.common.registry.BnCBlocks;

@TaskClassAnalyzer(TaskInfo.BNC_KEY)
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
        return KegRecSerializerManager.getInstance();
    }

    @Override
    protected MaidCookManager<KegFermentingRecipe> createRecipesManager(EntityMaid maid, CookBeBase<KegBlockEntity> cookBe) {
        return new MaidKegCookManager(recSerializerManager, maid, this, cookBe);
    }

    @Override
    public ResourceLocation getUid() {
        return CookTask.BNC_KEY.uid;
    }

    @Override
    public ItemStack getIcon() {
        return BnCBlocks.KEG.get().asItem().getDefaultInstance();
    }
}
