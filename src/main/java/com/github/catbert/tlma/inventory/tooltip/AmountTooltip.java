package com.github.catbert.tlma.inventory.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraftforge.items.IItemHandler;
public record AmountTooltip(IItemHandler handler) implements TooltipComponent {
}
