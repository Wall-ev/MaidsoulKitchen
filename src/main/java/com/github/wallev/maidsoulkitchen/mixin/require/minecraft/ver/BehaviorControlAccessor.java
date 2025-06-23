package com.github.wallev.maidsoulkitchen.mixin.require.minecraft.ver;

import com.github.wallev.verhelper.server.ai.VBehaviorControl;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BehaviorControl.class)
public interface BehaviorControlAccessor extends VBehaviorControl {
}
