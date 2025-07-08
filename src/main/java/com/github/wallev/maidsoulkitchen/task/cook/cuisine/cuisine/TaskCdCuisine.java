package com.github.wallev.maidsoulkitchen.task.cook.cuisine.cuisine;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.task.CookTask;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import dev.xkmc.cuisinedelight.content.recipe.BaseCuisineRecipe;
import dev.xkmc.cuisinedelight.init.registrate.CDBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@TaskClassAnalyzer(TaskInfo.CD_CUISINE_SKILLET)
public class TaskCdCuisine extends ICookTask<CuisineSkilletBlockEntity, BaseCuisineRecipe<?>> {
    @Override
    protected AbstractCookRule<CuisineSkilletBlockEntity, BaseCuisineRecipe<?>> createCookRule() {
        return CuisineCookRule.getInstance();
    }

    @Override
    protected RecSerializerManager<BaseCuisineRecipe<?>> createRecSerializerManager() {
        return CuisineRecSerializerManager.getInstance();
    }

    @Override
    protected CookBeBase<CuisineSkilletBlockEntity> createCookBe(EntityMaid maid) {
        return new CuisineBe(maid);
    }

    @Override
    public ResourceLocation getUid() {
        return CookTask.CD_CUISINE_SKILLET.uid;
    }

    @Override
    public ItemStack getIcon() {
        return CDBlocks.SKILLET.asStack();
    }
}
