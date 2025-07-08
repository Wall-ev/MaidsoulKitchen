package com.github.wallev.maidsoulkitchen.task.cook.drinkbeer.beerbarrel;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.task.CookTask;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.NormalCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import lekavar.lma.drinkbeer.blockentities.BeerBarrelBlockEntity;
import lekavar.lma.drinkbeer.recipes.BrewingRecipe;
import lekavar.lma.drinkbeer.registries.BlockRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@TaskClassAnalyzer(TaskInfo.DB_BEER)
public class TaskDbBeerBarrel extends ICookTask<BeerBarrelBlockEntity, BrewingRecipe> {
    @Override
    protected AbstractCookRule<BeerBarrelBlockEntity, BrewingRecipe> createCookRule() {
        return NormalCookRule.getInstance();
    }

    @Override
    protected RecSerializerManager<BrewingRecipe> createRecSerializerManager() {
        return BeerBarrelRecSerializerManager.getInstance();
    }

    @Override
    protected CookBeBase<BeerBarrelBlockEntity> createCookBe(EntityMaid maid) {
        return new BeerBarrelBe(maid);
    }

    @Override
    public ResourceLocation getUid() {
        return CookTask.DB_BEER.uid;
    }

    @Override
    public ItemStack getIcon() {
        return BlockRegistry.BEER_BARREL.get().asItem().getDefaultInstance();
    }
}
