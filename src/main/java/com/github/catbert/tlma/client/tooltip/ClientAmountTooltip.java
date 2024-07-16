package com.github.catbert.tlma.client.tooltip;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.inventory.tooltip.AmountTooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class ClientAmountTooltip implements ClientTooltipComponent {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TLMAddon.MOD_ID, "textures/gui/cook_guide.png");
    private final MutableComponent titleTip = Component.translatable("tooltips.touhou_little_maid_addon.amount.title");
    private final List<Ingredient> ingres;

    public ClientAmountTooltip(AmountTooltip containerTooltip) {
        this.ingres = containerTooltip.ingredients();
    }

    @Override
    public int getHeight() {
        return 30;
    }

    @Override
    public int getWidth(Font font) {
        return Math.max(font.width(titleTip), ingres.size() * 20);
    }

    @Override
    public void renderImage(Font font, int pX, int pY, GuiGraphics guiGraphics) {
        guiGraphics.drawString(font, titleTip, pX, pY, ChatFormatting.GRAY.getColor());
        int i = 0;
        pY += 10;
        for (Ingredient ingre : this.ingres) {
            ItemStack[] stackItems = ingre.getItems();
            if (stackItems.length == 0) {
                continue;
            }

            int xOffset = pX + i++ * 20;

            guiGraphics.renderItem(stackItems[0], xOffset, pY);

            if (stackItems.length > 1) {
                guiGraphics.blit(TEXTURE, xOffset, pY + 13, 0, 253, 3, 3);
            }
        }
    }
}
