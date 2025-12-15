package com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook;

import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.GatherResult;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.MaidCookManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.FluidRecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.ItemStackUtil;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;

import java.util.List;

/**
 * 烹饪规则的抽象类
 * <p>
 * 使用 {@link AbstractCookRule#getOrCreate()} 获取实例
 *
 * @param <B> 方块实体
 * @param <R> 配方
 */
public abstract class AbstractCookRule<B extends BlockEntity, R extends Recipe<? extends Container>> {

    protected boolean hasFluidContainers(Fluid fluid, MaidCookManager<R> rm) {
        FluidRecSerializerManager<R> frm = rm.getRecSerializerManager().toFluid();
        List<ItemStack> containers = frm.fluidContainer(fluid);

        return rm.hasItem(itemStack -> ItemStackUtil.isItem(containers, itemStack));
    }

    protected GatherResult getFluidContainers(Fluid fluid, MaidCookManager<R> rm) {
        FluidRecSerializerManager<R> frm = rm.getRecSerializerManager().toFluid();
        List<ItemStack> containers = frm.fluidContainer(fluid);

        return rm.getItem(itemStack -> ItemStackUtil.isItem(containers, itemStack));
    }

    /**
     * 受保护的构造函数，确保该类只能被其子类实例化
     * <p>
     * 只能够使用 {@link AbstractCookRule#getOrCreate()} 获取实例
     */
    protected AbstractCookRule() {
    }

    /**
     * 判断是否可以移动到指定的烹饪状态
     *
     * @param cookBeBase 烹饪方块实体的基础类实例
     * @param cm 女仆烹饪管理器实例
     * @return 如果可以移动到厨具则返回 true，否则返回 false
     */
    public abstract boolean canMoveTo(CookBeBase<B> cookBeBase, MaidCookManager<R> cm);

    /**
     * 执行烹饪操作
     *
     * @param cookBeBase 烹饪方块实体的基础类实例
     * @param cm 女仆烹饪管理器实例
     */
    public abstract void cookMake(CookBeBase<B> cookBeBase, MaidCookManager<R> cm);

    /**
     * 判断是否可以在 tick 时执行操作
    /**
     * 在 tick 时停止烹饪操作
     *
     * @param cookBeBase 烹饪方块实体的基础类实例
     * @param cm 女仆烹饪管理器实例
     */
    public boolean tickCan(CookBeBase<B> cookBeBase, MaidCookManager<R> cm) {
        return false;
    }

    /**
     * 在 tick 时执行烹饪操作
     *
     * @param cookBeBase 烹饪方块实体的基础类实例
     * @param cm 女仆烹饪管理器实例
     */
    public void tickCookMake(CookBeBase<B> cookBeBase, MaidCookManager<R> cm) {
    }

    /**
     * 在 tick 时停止烹饪操作
     *
     * @param cookBeBase 烹饪方块实体的基础类实例
     * @param cm 女仆烹饪管理器实例
     */
    public void tickStop(CookBeBase<B> cookBeBase, MaidCookManager<R> cm) {
    }

    /**
     * 获取 <strong>ICookRule</strong> 的实例
     *
     * @return 如果需要 <strong>tick</strong> 执行，那就返回新的实例；反之返回单例。
     */
    public AbstractCookRule<B, R> getOrCreate() {
        return this;
    }
}
