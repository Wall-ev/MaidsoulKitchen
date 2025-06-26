package com.github.wallev.maidsoulkitchen.mixin.compat.cuisinedelight;

import com.github.wallev.maidsoulkitchen.api.mixin.IMaidsoulKitchenInterface;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.classana.TaskMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@TaskMixin(task = TaskInfo.CD_CUISINE_SKILLET)
@Pseudo
@Mixin(targets = "dev.xkmc.cuisinedelight.content.logic.CookingData$CookingEntry", remap = false)
public interface CookingEntryAccessor extends IMaidsoulKitchenInterface {

    @Accessor("startTime")
    long tlmk$getStartTime();

}
