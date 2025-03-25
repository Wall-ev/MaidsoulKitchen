package com.github.wallev.maidsoulkitchen.inventory.tooltip;

import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record AmountTooltip(String recipeId, List<Ingredient> ingredients, Boolean isBlacklist, boolean isOverSize, CookData cookData) implements TooltipComponent {
}
