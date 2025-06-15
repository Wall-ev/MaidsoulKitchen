package com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook;

import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.implement.TextChatBubbleData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidRecipesManager2;
import com.github.wallev.maidsoulkitchen.util.TextContactUtil;
import com.github.wallev.verhelper.client.chat.VComponent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

/**
 * 使用 {@link AbstractCookRule#getOrCreate()} 获取实例
 */
public abstract class AbstractCookRule<B extends BlockEntity, R extends Recipe<? extends Container>> {

    protected AbstractCookRule() {
    }

    public abstract boolean canMoveTo(CookBeBase<B> cookBeBase, MaidRecipesManager2<R> rm);

    public abstract void cookMake(CookBeBase<B> cookBeBase, MaidRecipesManager2<R> rm);

    public boolean tickCan(CookBeBase<B> cookBeBase, MaidRecipesManager2<R> rm) {
        return false;
    }

    public void tickCookMake(CookBeBase<B> cookBeBase, MaidRecipesManager2<R> rm) {
    }

    public void tickStop(CookBeBase<B> cookBeBase, MaidRecipesManager2<R> rm) {
    }

    /**
     * 获取 <strong>ICookRule</strong> 的实例
     *
     * @return 如果需要 <strong>tick</strong> 执行，那就返回新的实例；反之返回单例。
     */
    public abstract AbstractCookRule<B, R> getOrCreate();
}
