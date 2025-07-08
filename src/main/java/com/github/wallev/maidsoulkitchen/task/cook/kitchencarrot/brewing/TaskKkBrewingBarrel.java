package com.github.wallev.maidsoulkitchen.task.cook.kitchencarrot.brewing;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.task.CookTask;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.WaterCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import io.github.tt432.kitchenkarrot.blockentity.BrewingBarrelBlockEntity;
import io.github.tt432.kitchenkarrot.recipes.recipe.BrewingBarrelRecipe;
import io.github.tt432.kitchenkarrot.registries.ModBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@TaskClassAnalyzer(TaskInfo.KK_BREW_BARREL)
public class TaskKkBrewingBarrel extends ICookTask<BrewingBarrelBlockEntity, BrewingBarrelRecipe> {
    @Override
    protected AbstractCookRule<BrewingBarrelBlockEntity, BrewingBarrelRecipe> createCookRule() {
        return WaterCookRule.getInstance();
    }

    @Override
    protected RecSerializerManager<BrewingBarrelRecipe> createRecSerializerManager() {
        return KkBrewingBarrelRecSerializerManager.getInstance();
    }

    @Override
    protected CookBeBase<BrewingBarrelBlockEntity> createCookBe(EntityMaid maid) {
        return new BrewingBarrelBe(maid);
    }

    @Override
    public ResourceLocation getUid() {
        return CookTask.KK_BREW_BARREL.uid;
    }

    @Override
    public ItemStack getIcon() {
        return ModBlocks.BREWING_BARREL.get().asItem().getDefaultInstance();
    }
}
