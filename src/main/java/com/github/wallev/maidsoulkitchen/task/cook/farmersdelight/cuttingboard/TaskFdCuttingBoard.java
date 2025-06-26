package com.github.wallev.maidsoulkitchen.task.cook.farmersdelight.cuttingboard;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.DataRegister;
import com.github.wallev.maidsoulkitchen.task.MaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModBlocks;

@TaskClassAnalyzer(TaskInfo.FD_CUTTING_BOARD)
public class TaskFdCuttingBoard extends ICookTask<CuttingBoardBlockEntity, CuttingBoardRecipe> {
    @Override
    protected AbstractCookRule<CuttingBoardBlockEntity, CuttingBoardRecipe> createCookRule() {
        return CuttingBoardCookRule.getInstance();
    }

    @Override
    protected RecSerializerManager<CuttingBoardRecipe> createRecSerializerManager() {
        return CuttingBoardRecSerializerManager.getInstance();
    }

    @Override
    protected CookBeBase<CuttingBoardBlockEntity> createCookBe(EntityMaid maid) {
        return new CuttingBoardBe(maid);
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return DataRegister.FD_CUTTING_BOARD;
    }

    @Override
    public ResourceLocation getUid() {
        return MaidsoulKitchenTask.FD_CUTTING_BOARD.uid;
    }

    @Override
    public ItemStack getIcon() {
        return ModBlocks.CUTTING_BOARD.get().asItem().getDefaultInstance();
    }
}
