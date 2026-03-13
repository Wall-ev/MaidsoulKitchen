package com.github.wallev.maidsoulkitchen.mixin.compat.youkaishomecoming;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskMixin;
import com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming.KettleBlockAccessor;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleBlock;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@TaskMixin(value = TaskInfo.YHC_TEA_KETTLE)
@Mixin(value = KettleBlock.class)
public abstract class KettleBlockMixin implements KettleBlockAccessor {

    @Shadow
    @Final
    protected static Lazy<Map<Ingredient, Integer>> MAP;

    @Override
    public @NotNull Lazy<Map<Ingredient, Integer>> tlmk$getMap() {
        return MAP;
    }
}
