package com.github.wallev.maidsoulkitchen.handler.task;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.handler.initializer.brewinandchewin.mkcontainer.BncKeyContainer;
import com.github.wallev.maidsoulkitchen.handler.task.handler.MaidRecipesManager;
import com.github.wallev.maidsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;
import umpaz.brewinandchewin.common.crafting.KegRecipe;
import umpaz.brewinandchewin.common.registry.BCBlocks;
import umpaz.brewinandchewin.common.registry.BCRecipeTypes;

public class TaskBncKeyV2 extends AbstractTaskCook<BncKeyContainer, KegBlockEntity, KegRecipe>{
    /**
     * 获取当前任务所对应的配方类型
     *
     * @return 配方类型
     */
    @Override
    public RecipeType<KegRecipe> getRecipeType() {
        return BCRecipeTypes.FERMENTING.get();
    }

    /**
     * 判断当前方块实体是否是当前任务所对应的厨具方块
     *
     * @param blockEntity 方块实体
     * @return 是否是当前任务所对应的方块实体
     */
    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof KegBlockEntity;
    }

    /**
     * 创建烹饪相关信息，应当同#createBrainTasks的时候一起创建
     *
     * @param maid           女仆对象
     * @param recipesManager 配方管理器
     * @return 返回烹饪相关的信息的创建
     */
    @Override
    protected BncKeyContainer createMaidCookBe(EntityMaid maid, MaidRecipesManager<BncKeyContainer, KegBlockEntity, KegRecipe> recipesManager) {
        return new BncKeyContainer(maid, recipesManager);
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.BNC_KEY.uid;
    }

    @Override
    public ItemStack getIcon() {
        return BCBlocks.KEG.get().asItem().getDefaultInstance();
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound(EntityMaid entityMaid) {
        return null;
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.BNC_KEY;
    }
}
