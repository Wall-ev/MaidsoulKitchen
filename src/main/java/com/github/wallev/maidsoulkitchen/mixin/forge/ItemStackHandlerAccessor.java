package com.github.wallev.maidsoulkitchen.mixin.forge;

import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = ItemStackHandler.class, remap = false)
public interface ItemStackHandlerAccessor {

    @Invoker("onContentsChanged")
    void onContentsChanged$tlma(int slot);

}
