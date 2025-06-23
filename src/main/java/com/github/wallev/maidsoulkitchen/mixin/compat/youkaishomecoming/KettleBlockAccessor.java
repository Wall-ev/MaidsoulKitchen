package com.github.wallev.maidsoulkitchen.mixin.compat.youkaishomecoming;

import com.github.wallev.maidsoulkitchen.api.mixin.IMaidsoulKitchenInterface;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleBlock;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.util.Lazy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(value = KettleBlock.class, remap = false)
public interface KettleBlockAccessor extends IMaidsoulKitchenInterface {

    @Accessor("MAP")
    static Lazy<Map<Ingredient, Integer>> waters() {
        throw new AssertionError();
    }

}
