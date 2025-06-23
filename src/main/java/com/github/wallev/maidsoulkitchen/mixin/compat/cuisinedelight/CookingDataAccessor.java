package com.github.wallev.maidsoulkitchen.mixin.compat.cuisinedelight;

import com.github.wallev.maidsoulkitchen.api.mixin.IMaidsoulKitchenInterface;
import dev.xkmc.cuisinedelight.content.logic.CookingData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = CookingData.class, remap = false)
public interface CookingDataAccessor extends IMaidsoulKitchenInterface {

    @Accessor("speed")
    float tlmk$getSpeed();

}
