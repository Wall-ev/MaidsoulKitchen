package com.github.wallev.maidsoulkitchen.handler.base.mkcontainer;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * 用于建立女仆烹饪厨具，便于管理
 * <br>应该在createBrain的时候就建立
 * <br>并传进MaidCookMoveTask和MaidCookMakeTask
 */
public abstract class AbstractMaidCookBe<B extends BlockEntity, R extends Recipe<? extends Container>> {
    protected final EntityMaid maid;
    protected final MaidRecipesManager<R> recipesManager;
    protected B cookBe;
    // @final
    protected int inputSlotSize;
    // @final
    protected int inputStartSlot;
    // @final
    protected int resultSlot;

    public AbstractMaidCookBe(EntityMaid maid, MaidRecipesManager<R> recipesManager, B cookBe) {
        this.maid = maid;
        this.recipesManager = recipesManager;
        this.cookBe = cookBe;
        this.initialSlots();
    }

    /**
     * 初始化厨具的格子信息（比如原料输入的格子，起始格子，输出的格子）
     */
    protected abstract void initialSlots();

    /**
     * 从厨具取出某个格子的物品
     *
     * @param slot     格子
     * @param amount   取出的数量
     * @param simulate 是否模拟取出
     * @return 取出后剩余的物品
     */
    public abstract ItemStack extractItem(int slot, int amount, boolean simulate);

    /**
     * 将物品插入到厨具的某个格子中
     *
     * @param slot     格子
     * @param stack    要插入的物品
     * @param simulate 是否模拟插入
     * @return 插入后剩余的物品
     */
    public abstract ItemStack insertItem(int slot, ItemStack stack, boolean simulate);

    /**
     * 获取厨具的某个格子里的ItemStack
     *
     * @param slot 格子
     * @return 格子里的ItemStack
     */
    public abstract ItemStack getStackInSlot(int slot);

    /**
     * 对于方块实体，确保包含方块实体的区块稍后保存到磁盘
     * <br>游戏不会认为它没有更改并跳过它。
     */
    public void setChanged() {
        this.cookBe.setChanged();
    }


    /**
     * 厨具内部的所有原料输入的格子是否有物资在里头
     */
    public boolean hasInputs() {
        for (int i = inputStartSlot; i < inputStartSlot + inputSlotSize; i++) {
            if (!this.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 厨具内部的输出（完全烹饪好的食物）的格子是否有物资在里头
     */
    public boolean hasResult() {
        return !this.getStackInSlot(resultSlot).isEmpty();
    }

    /**
     * 厨具内部的物资是否能够烹饪（即物资是否符合某个配方的原材料）
     * <br>不包括外部条件和内部条件（比如要加水，加燃料等；厨具的下面的方块提供温度）
     */
    public abstract void innerCanCook();


    /**
     * 获取原料的Inv
     */
    public IItemHandlerModifiable getIngredientInv() {
        return recipesManager.getIngredientInv();
    }

    /**
     * 获取输出的Inv
     */
    public IItemHandlerModifiable getOutputInv() {
        return recipesManager.getOutputInv();
    }



    /**
     * 获取maid
     */
    public EntityMaid getMaid() {
        return maid;
    }

    /**
     * 获取配方管理器
     */
    public MaidRecipesManager<R> getRecipesManager() {
        return recipesManager;
    }

    /**
     * 烹饪厨具
     */
    public B getCookBe() {
        return cookBe;
    }

    /**
     * 设置烹饪的厨具
     * <br>应该在每次执行逻辑的时候设置一次，以更新为该厨具
     */
    public void setCookBe(B cookBe) {
        this.cookBe = cookBe;
    }

    /**
     * 获取输入槽起始位置
     */
    public int getInputStartSlot() {
        return inputStartSlot;
    }

    /**
     * 获取输入槽大小
     */
    public int getInputSlotSize() {
        return inputSlotSize;
    }

    /**
     * 获取结果槽
     */
    public int getResultSlot() {
        return resultSlot;
    }
}
