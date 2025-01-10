package com.github.wallev.maidsoulkitchen.handler.rule;

import com.github.wallev.maidsoulkitchen.handler.rec.CookRec;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

public class DefaultCookRecRuleSerializer<R extends Recipe<? extends Container>> extends AbstractCookRecRuleSerializer<R, CookRec<R>> {
}
