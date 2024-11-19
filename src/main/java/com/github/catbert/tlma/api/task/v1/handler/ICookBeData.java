package com.github.catbert.tlma.api.task.v1.handler;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public record ICookBeData(BlockEntity be, NonNullList<ItemStack> inputStacks, ItemStack outputStack) {

    public static class Rule {

    }

}
