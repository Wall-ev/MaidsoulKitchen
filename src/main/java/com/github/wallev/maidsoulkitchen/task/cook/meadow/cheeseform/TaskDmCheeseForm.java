package com.github.wallev.maidsoulkitchen.task.cook.meadow.cheeseform;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.task.CookTask;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.NormalCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.satisfy.meadow.core.block.entity.CheeseFormBlockEntity;
import net.satisfy.meadow.core.recipes.CheeseFormRecipe;
import net.satisfy.meadow.core.registry.ObjectRegistry;

@TaskClassAnalyzer(TaskInfo.DM_CHEESE_FORM)
public class TaskDmCheeseForm extends ICookTask<CheeseFormBlockEntity, CheeseFormRecipe> {
    @Override
    protected AbstractCookRule<CheeseFormBlockEntity, CheeseFormRecipe> createCookRule() {
        return NormalCookRule.getInstance();
    }

    @Override
    protected RecSerializerManager<CheeseFormRecipe> createRecSerializerManager() {
        return CheeseFormRecSerializerManager.getInstance();
    }

    @Override
    protected CookBeBase<CheeseFormBlockEntity> createCookBe(EntityMaid maid) {
        return new CheeseFormCookBe(maid);
    }

    @Override
    public ResourceLocation getUid() {
        return CookTask.DM_CHEESE_FORM.uid;
    }

    @Override
    public ItemStack getIcon() {
        return ObjectRegistry.CHEESE_FORM.get().asItem().getDefaultInstance();
    }
}
