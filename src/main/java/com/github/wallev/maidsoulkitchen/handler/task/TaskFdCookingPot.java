package com.github.wallev.maidsoulkitchen.handler.task;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.handler.base.mkcontainer.OutputContainerIMbe;
import com.github.wallev.maidsoulkitchen.handler.initializer.farmersdelight.mkcontainer.FdCookingPotContainer;
import com.github.wallev.maidsoulkitchen.handler.task.handler.MaidRecipesManager;
import com.github.wallev.maidsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public class TaskFdCookingPot extends AbstractTaskCook<OutputContainerIMbe<CookingPotBlockEntity, CookingPotRecipe>, CookingPotBlockEntity, CookingPotRecipe> {
    /**
     * 获取当前任务所对应的配方类型
     *
     * @return 配方类型
     */
    @Override
    public RecipeType<CookingPotRecipe> getRecipeType() {
        return ModRecipeTypes.COOKING.get();
    }

    /**
     * 判断当前方块实体是否是当前任务所对应的厨具方块
     *
     * @param cookBe 方块实体
     * @return 是否是当前任务所对应的方块实体
     */
    @Override
    public boolean isCookBE(BlockEntity cookBe) {
        return cookBe instanceof CookingPotBlockEntity;
    }

    /**
     * 创建烹饪相关信息，应当同#createBrainTasks的时候一起创建
     *
     * @param maid           女仆对象
     * @param recipesManager 配方管理器
     * @return 返回烹饪相关的信息的创建
     */
    @Override
    protected OutputContainerIMbe<CookingPotBlockEntity, CookingPotRecipe> createMaidCookBe(EntityMaid maid, MaidRecipesManager<OutputContainerIMbe<CookingPotBlockEntity, CookingPotRecipe>, CookingPotBlockEntity, CookingPotRecipe> recipesManager) {
        return new FdCookingPotContainer(maid, recipesManager);
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.FD_COOK_POT.uid;
    }

    @Override
    public ItemStack getIcon() {
        return ModItems.COOKING_POT.get().getDefaultInstance();
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound(EntityMaid entityMaid) {
        return null;
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.FD_COOK_POT;
    }
}
