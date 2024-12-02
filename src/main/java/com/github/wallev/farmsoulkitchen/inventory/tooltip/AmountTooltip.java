package com.github.wallev.farmsoulkitchen.inventory.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record AmountTooltip(List<Ingredient> ingredients, Boolean isRandom, boolean isOverSize) implements TooltipComponent {
}
