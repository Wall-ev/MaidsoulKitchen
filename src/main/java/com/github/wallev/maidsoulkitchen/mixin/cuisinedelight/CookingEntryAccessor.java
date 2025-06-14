package com.github.wallev.maidsoulkitchen.mixin.cuisinedelight;

import dev.xkmc.cuisinedelight.content.logic.CookingData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = CookingData.CookingEntry.class, remap = false)
public interface CookingEntryAccessor {

    @Accessor("startTime")
    long tlmk$getStartTime();

}
