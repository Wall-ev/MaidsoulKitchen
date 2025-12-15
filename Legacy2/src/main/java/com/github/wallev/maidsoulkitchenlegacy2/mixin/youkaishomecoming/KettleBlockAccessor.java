package com.github.wallev.maidsoulkitchenlegacy2.mixin.youkaishomecoming;

import com.github.wallev.maidsoulkitchen.legacy.mixin.LegacyTaskMixin;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMskMixinInterface;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleBlock;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.util.Lazy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@LegacyTaskMixin(mod = Mods.YHCD_223_250, task = TaskInfo.YHC_TEA_KETTLE_LEGACY)
@Mixin(value = KettleBlock.class, remap = false)
public interface KettleBlockAccessor extends IMskMixinInterface {

    @Accessor("MAP")
    static Lazy<Map<Ingredient, Integer>> waters() {
        throw new AssertionError();
    }

}
