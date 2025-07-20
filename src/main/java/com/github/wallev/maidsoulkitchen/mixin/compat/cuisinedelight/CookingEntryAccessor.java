package com.github.wallev.maidsoulkitchen.mixin.compat.cuisinedelight;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMccMixinInterface;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@TaskMixin(value = TaskInfo.CD_CUISINE_SKILLET)
@Pseudo
@Mixin(targets = "dev.xkmc.cuisinedelight.content.logic.CookingData$CookingEntry", remap = false)
public interface CookingEntryAccessor extends IMccMixinInterface {

    @Accessor("startTime")
    long tlmk$getStartTime();

}
