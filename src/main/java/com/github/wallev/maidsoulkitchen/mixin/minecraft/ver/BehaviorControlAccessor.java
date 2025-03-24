package com.github.wallev.maidsoulkitchen.mixin.minecraft.ver;

import com.github.wallev.verhelper.server.ai.VBehaviorControl;
import net.minecraft.world.entity.ai.behavior.Behavior;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Behavior.class)
public interface BehaviorControlAccessor extends VBehaviorControl {
}
