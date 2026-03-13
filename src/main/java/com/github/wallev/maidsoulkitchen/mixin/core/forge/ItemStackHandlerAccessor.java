package com.github.wallev.maidsoulkitchen.mixin.core.forge;


import net.neoforged.neoforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = ItemStackHandler.class, remap = false)
public interface ItemStackHandlerAccessor {

    @Invoker("onContentsChanged")
    void tlmk$onContentsChanged(int slot);

}
