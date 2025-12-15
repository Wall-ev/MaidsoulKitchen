package com.github.wallev.maidsoulkitchen.task.cook.brewinandchewin.keg;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.MaidCookManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.FluidPotCookRule1;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;
import umpaz.brewinandchewin.common.crafting.KegFermentingRecipe;
import umpaz.brewinandchewin.common.registry.BnCBlocks;

import com.github.wallev.maidsoulkitchen.task.cook.common.task.AutoCookTaskRegister;

@AutoCookTaskRegister(TaskInfo.BNC_KEY)
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
        return TaskInfo.BNC_KEY.getUid();
    }

    @Override
    public ItemStack getIcon() {
        return BnCBlocks.KEG.get().asItem().getDefaultInstance();
    }

    @Override
    protected @Nullable Index indexEnum() {
        return Index.BNC_KEY;
    }
}
