package com.github.wallev.maidsoulkitchen.mixin.core.minecraft.ver;

import com.github.wallev.maidsoulkitchen.vhelper.server.ai.VBehaviorControl;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BehaviorControl.class)
public interface BehaviorControlAccessor extends VBehaviorControl {
}
