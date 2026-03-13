package com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMskMixinInterface;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.util.Lazy;

import java.util.Map;

public interface KettleBlockAccessor extends IMskMixinInterface {
    Lazy<Map<Ingredient, Integer>> tlmk$getMap();
}
