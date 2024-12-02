package com.github.wallev.farmsoulkitchen.compat.builder.task.ab;

import com.github.wallev.farmsoulkitchen.util.function.Predicate3;
import com.github.tartaricacid.touhoulittlemaid.api.task.IFeedTask;
import com.mojang.datafixers.util.Function3;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class CustomTaskABFeed<T extends CustomTaskABFeedBuilder> extends CustomTaskABBase<T> implements IFeedTask {
    // IFeedTask
    protected Predicate3<IFeedTask, ItemStack, Player> isFood;
    protected Function3<IFeedTask, ItemStack, Player, Priority> getPriority;
    protected Function3<IFeedTask, ItemStack, Player, ItemStack> feed;

    public CustomTaskABFeed(T customTaskFeedBuilder) {
        super(customTaskFeedBuilder);
    }

    @Override
    public boolean isFood(ItemStack stack, Player owner) {
        return this.isFood.test(this, stack, owner);
    }

    @Override
    public Priority getPriority(ItemStack stack, Player owner) {
        return this.getPriority.apply(this, stack, owner);
    }

    @Override
    public ItemStack feed(ItemStack stack, Player owner) {
        return this.feed.apply(this, stack, owner);
    }

    @Override
    public void initBuilderData(T builder) {
        super.initBuilderData(builder);
        this.isFood = builder.isFood;
        this.getPriority = builder.getPriority;
        this.feed = builder.feed;
    }
}
