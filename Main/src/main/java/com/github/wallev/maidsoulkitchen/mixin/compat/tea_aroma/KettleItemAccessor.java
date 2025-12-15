package com.github.wallev.maidsoulkitchen.mixin.compat.tea_aroma;

import cn.foggyhillside.tea_aroma.items.KettleItem;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMskMixinInterface;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskMixin;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@TaskMixin(TaskInfo.MSM_TA_BOILING)
@Mixin(value = KettleItem.class, remap = false)
public interface KettleItemAccessor extends IMskMixinInterface {
    @Invoker("setStackBoilProgress")
    static void msk$setStackBoilProgress(ItemStack stack, int boilProgress) {
        throw new UnsupportedOperationException();
    }

    @Invoker("getStackBoilProgress")
    static int msk$getStackBoilProgress(ItemStack stack) {
        throw new UnsupportedOperationException();
    }

    @Accessor("BOIL")
    static int msk$getBoil() {
        throw new UnsupportedOperationException();
    }

    @Accessor("MAX_PROGRESS")
    static int msk$getMaxProgress() {
        throw new UnsupportedOperationException();
    }
}
