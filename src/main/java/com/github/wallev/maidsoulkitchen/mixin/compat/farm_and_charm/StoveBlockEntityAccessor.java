package com.github.wallev.maidsoulkitchen.mixin.compat.farm_and_charm;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMskMixinInterface;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskMixin;
import net.satisfy.farm_and_charm.core.block.entity.StoveBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@TaskMixin(TaskInfo.MSM_FARM_AND_CHARM_STOVE)
@Mixin(value = StoveBlockEntity.class, remap = false)
public interface StoveBlockEntityAccessor extends IMskMixinInterface {
    @Invoker
    boolean callIsBurning();
}
