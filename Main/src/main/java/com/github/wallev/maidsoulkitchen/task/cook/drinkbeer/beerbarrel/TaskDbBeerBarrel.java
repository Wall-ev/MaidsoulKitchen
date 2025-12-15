package com.github.wallev.maidsoulkitchen.task.cook.drinkbeer.beerbarrel;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.NormalCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.task.AutoCookTaskRegister;
import lekavar.lma.drinkbeer.blockentities.BeerBarrelBlockEntity;
import lekavar.lma.drinkbeer.recipes.BrewingRecipe;
import lekavar.lma.drinkbeer.registries.BlockRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask.Index.DB_BEER;

@AutoCookTaskRegister(TaskInfo.DB_BEER)
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
        return TaskInfo.DB_BEER.getUid();
    }

    @Override
    protected Index indexEnum() {
        return DB_BEER;
    }



    @Override
    public ItemStack getIcon() {
        return BlockRegistry.BEER_BARREL.get().asItem().getDefaultInstance();
    }
}
