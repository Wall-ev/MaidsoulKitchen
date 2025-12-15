package com.github.wallev.maidsoulkitchen.task.cook.beachparty.minifridge;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.NormalCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.task.AutoCookTaskRegister;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.satisfy.beachparty.core.block.entity.MiniFridgeBlockEntity;
import net.satisfy.beachparty.core.recipe.MiniFridgeRecipe;
import net.satisfy.beachparty.core.registry.ObjectRegistry;

import static com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask.Index.DBP_MINI_FRIDGE;

@AutoCookTaskRegister(TaskInfo.DBP_MINI_FRIDGE)
@TaskClassAnalyzer(TaskInfo.DBP_MINI_FRIDGE)
public class TaskDbpMiniFridge extends ICookTask<MiniFridgeBlockEntity, MiniFridgeRecipe> {
    @Override
    protected AbstractCookRule<MiniFridgeBlockEntity, MiniFridgeRecipe> createCookRule() {
        return NormalCookRule.getInstance();
    }

    @Override
    protected RecSerializerManager<MiniFridgeRecipe> createRecSerializerManager() {
        return MiniFridgeRecSerializerManager.getInstance();
    }

    @Override
    protected CookBeBase<MiniFridgeBlockEntity> createCookBe(EntityMaid maid) {
        return new MiniFridgeCookBe(maid);
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.DBP_MINI_FRIDGE.getUid();
    }

    @Override
    protected Index indexEnum() {
        return DBP_MINI_FRIDGE;
    }



    @Override
    public ItemStack getIcon() {
        return ObjectRegistry.MINI_FRIDGE.get().asItem().getDefaultInstance();
    }
}
