package com.github.wallev.maidsoulkitchen.mixin.forge;

import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = FluidTank.class, remap = false)
public interface FluidTankAccessor {

    @Invoker("onContentsChanged")
    void tlmk$onContentChanged();

}
