package com.github.wallev.maidsoulkitchen.mixin.compat.maidstoragemanager;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMskMixinInterface;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.work.CraftLayer;

import java.util.List;

@TaskMixin(TaskInfo.MSM_CORE)
@Mixin(value = CraftLayer.class, remap = false)
public interface CraftLayerAccessor extends IMskMixinInterface {
    @Accessor("steps")
    List<CraftGuideStepData> msk$getSteps();
}
