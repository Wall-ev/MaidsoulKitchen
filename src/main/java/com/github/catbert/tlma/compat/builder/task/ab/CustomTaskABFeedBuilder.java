package com.github.catbert.tlma.compat.builder.task.ab;

import com.github.catbert.tlma.util.function.Predicate3;
import com.github.tartaricacid.touhoulittlemaid.api.task.IFeedTask;
import com.mojang.datafixers.util.Function3;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class CustomTaskABFeedBuilder<T extends CustomTaskABFeed> extends CustomTaskABBaseBuilder<T> {
    // IFeedTask
    protected Predicate3<IFeedTask, ItemStack, Player> isFood;
    protected Function3<IFeedTask, ItemStack, Player, IFeedTask.Priority> getPriority;
    protected Function3<IFeedTask, ItemStack, Player, ItemStack> feed;


    protected CustomTaskABFeedBuilder(ResourceLocation id) {
        super(id);
    }

    public CustomTaskABFeedBuilder(ResourceLocation id, boolean debug) {
        super(id, debug);
    }

    // IFeedTask
    public CustomTaskABFeedBuilder<T> isFood(Predicate3<IFeedTask, ItemStack, Player> isFood) {
        this.isFood = isFood;
        return this;
    }

    public CustomTaskABFeedBuilder<T>  getPriority(Function3<IFeedTask, ItemStack, Player, IFeedTask.Priority> getPriority) {
        this.getPriority = getPriority;
        return this;
    }

    public CustomTaskABFeedBuilder<T>  feed(Function3<IFeedTask, ItemStack, Player, ItemStack> feed) {
        this.feed = feed;
        return this;
    }
}
