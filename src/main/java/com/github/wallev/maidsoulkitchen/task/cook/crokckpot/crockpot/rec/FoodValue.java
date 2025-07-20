package com.github.wallev.maidsoulkitchen.task.cook.crokckpot.crockpot.rec;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.sihenzhang.crockpot.base.FoodCategory;
import net.minecraft.world.item.Item;

import java.util.Map;

@TaskClassAnalyzer(TaskInfo.CP_CROCK_POT)
public record FoodValue(FoodCategory foodCategory, Map<Item, Float> itemValues) {
}
