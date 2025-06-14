package com.github.wallev.maidsoulkitchen.task.cook.crokckpot.crockpot.rec;

import com.sihenzhang.crockpot.base.FoodCategory;
import net.minecraft.world.item.Item;

import java.util.Map;

public record FoodValue(FoodCategory foodCategory, Map<Item, Float> itemValues) {
}
