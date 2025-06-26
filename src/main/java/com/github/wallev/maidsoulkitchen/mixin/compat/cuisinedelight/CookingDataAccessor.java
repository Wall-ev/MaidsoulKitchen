package com.github.wallev.maidsoulkitchen.mixin.compat.cuisinedelight;

import com.github.wallev.maidsoulkitchen.api.mixin.IMaidsoulKitchenInterface;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.classana.TaskMixin;
import dev.xkmc.cuisinedelight.content.logic.CookingData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@TaskMixin(task = TaskInfo.CD_CUISINE_SKILLET)
@Mixin(value = CookingData.class, remap = false)
public interface CookingDataAccessor extends IMaidsoulKitchenInterface {

    @Accessor("speed")
    float tlmk$getSpeed();

}
