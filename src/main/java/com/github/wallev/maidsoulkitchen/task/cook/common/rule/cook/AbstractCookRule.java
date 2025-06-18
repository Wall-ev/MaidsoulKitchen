package com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook;

import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidCookManager;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * 使用 {@link AbstractCookRule#getOrCreate()} 获取实例
 */
public abstract class AbstractCookRule<B extends BlockEntity, R extends Recipe<? extends Container>> {

    protected AbstractCookRule() {
    }

    public abstract boolean canMoveTo(CookBeBase<B> cookBeBase, MaidCookManager<R> cm);

    public abstract void cookMake(CookBeBase<B> cookBeBase, MaidCookManager<R> cm);

    public boolean tickCan(CookBeBase<B> cookBeBase, MaidCookManager<R> cm) {
        return false;
    }

    public void tickCookMake(CookBeBase<B> cookBeBase, MaidCookManager<R> cm) {
    }

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
