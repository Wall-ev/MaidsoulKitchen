package com.github.wallev.maidsoulkitchen.task.cook.minecraft.furnace;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.task.CookTask;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.MaidCookManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.FuelCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

@TaskClassAnalyzer(TaskInfo.FURNACE)
public class TaskFurnace extends ICookTask<AbstractFurnaceBlockEntity, AbstractCookingRecipe> {

    @Override
    protected CookBeBase<AbstractFurnaceBlockEntity> createCookBe(EntityMaid maid) {
        return new FurnaceCookBe(maid);
    }

    @Override
    protected AbstractCookRule<AbstractFurnaceBlockEntity, AbstractCookingRecipe> createCookRule() {
        return FuelCookRule.getInstance();
    }

    @Override
    protected RecSerializerManager<AbstractCookingRecipe> createRecSerializerManager() {
        return AbstractCookingRecSerializerManager.getInstance();
    }

    @Override
    protected MaidCookManager<AbstractCookingRecipe> createRecipesManager(EntityMaid maid, CookBeBase<AbstractFurnaceBlockEntity> cookBe) {
        return new MaidFurnaceCookManager(recSerializerManager, maid, this, cookBe);
    }

    @Override
    public ResourceLocation getUid() {
        return CookTask.FURNACE.uid;
    }

    @Override
    public ItemStack getIcon() {
        return Items.FURNACE.getDefaultInstance();
    }
}
