package com.github.wallev.maidsoulkitchen.inventory.tooltip;

import com.github.wallev.maidsoulkitchen.task.cook.v1.crokckpot.TaskCrockPot;
import com.sihenzhang.crockpot.recipe.cooking.requirement.IRequirement;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Map;

public record CrockPotTooltip(TaskCrockPot.RecInfo1 recInfo1, Map<IRequirement, List<Item>> requirementListMap, Boolean isRandom, boolean isOverSize) implements TooltipComponent {
}
