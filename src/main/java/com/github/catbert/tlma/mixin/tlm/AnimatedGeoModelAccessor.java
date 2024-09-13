package com.github.catbert.tlma.mixin.tlm;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoBone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = AnimatedGeoModel.class, remap = false)
public interface AnimatedGeoModelAccessor {
    @Accessor
    List<AnimatedGeoBone> getBackpackBones();
}
