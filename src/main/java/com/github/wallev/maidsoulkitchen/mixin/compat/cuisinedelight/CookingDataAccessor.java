package com.github.wallev.maidsoulkitchen.mixin.compat.cuisinedelight;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMccMixinInterface;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskMixin;
import dev.xkmc.cuisinedelight.content.logic.CookingData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@TaskMixin(value = TaskInfo.CD_CUISINE_SKILLET)
@Mixin(value = CookingData.class, remap = false)
public interface CookingDataAccessor extends IMccMixinInterface {

    @Accessor("speed")
    float tlmk$getSpeed();

}
