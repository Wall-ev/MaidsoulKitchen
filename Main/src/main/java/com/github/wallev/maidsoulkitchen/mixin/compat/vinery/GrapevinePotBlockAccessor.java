package com.github.wallev.maidsoulkitchen.mixin.compat.vinery;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMskMixinInterface;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskMixin;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.satisfy.vinery.core.block.GrapevinePotBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@TaskMixin(TaskInfo.MSM_VINERY_GRAPE_POT)
@Mixin(value = GrapevinePotBlock.class, remap = false)
public interface GrapevinePotBlockAccessor extends IMskMixinInterface {
    @Accessor("STAGE")
    static IntegerProperty msk$getState() {
        throw new UnsupportedOperationException();
    }

    @Accessor("STORAGE")
    static IntegerProperty msk$getStorage() {
        throw new UnsupportedOperationException();
    }
}
