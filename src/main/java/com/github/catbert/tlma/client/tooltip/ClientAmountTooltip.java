package com.github.catbert.tlma.client.tooltip;

import com.github.catbert.tlma.inventory.tooltip.AmountTooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ClientAmountTooltip implements ClientTooltipComponent {
    private final NonNullList<ItemStack> items = NonNullList.create();
    private MutableComponent titleTip = Component.translatable("tooltips.touhou_little_maid_addon.amount.title");

    public ClientAmountTooltip(AmountTooltip containerTooltip) {
        IItemHandler handler = containerTooltip.handler();
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                this.items.add(stack);
            }
        }
    }

    @Override
    public int getHeight() {
        return 30;
    }

    @Override
    public int getWidth(Font font) {
        return Math.max(font.width(titleTip), items.size() * 20);
    }

    @Override
    public void renderImage(Font font, int pX, int pY, GuiGraphics guiGraphics) {
        guiGraphics.drawString(font, titleTip, pX, pY, ChatFormatting.GRAY.getColor());
        int i = 0;
        pY += 10;
        for (ItemStack stack : this.items) {
            int xOffset = pX + i * 20;
            guiGraphics.renderFakeItem(stack, xOffset, pY);
            guiGraphics.renderItemDecorations(font, stack, xOffset, pY);
            i++;
        }
    }
}
