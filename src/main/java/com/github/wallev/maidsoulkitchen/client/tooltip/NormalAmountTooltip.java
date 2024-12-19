package com.github.wallev.maidsoulkitchen.client.tooltip;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.inventory.tooltip.AmountTooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class NormalAmountTooltip implements ClientAmountTooltip {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MaidsoulKitchen.MOD_ID, "textures/gui/cook_guide.png");
    private final MutableComponent titleTip = Component.translatable("tooltips.maidsoulkitchen.amount.title");
    private final MutableComponent randomTip = Component.translatable("gui.maidsoulkitchen.btn.cook_guide.warn.not_select").withStyle(ChatFormatting.YELLOW);
//    private final MutableComponent overSizeTip = Component.translatable("gui.maidsoulkitchen.btn.cook_guide.warn.over_size", TaskConfig.COOK_SELECTED_RECIPES.get()).withStyle(ChatFormatting.YELLOW);
    private final List<Ingredient> ingres;
    private final boolean isRandom;
    private final boolean isOverSize;

    public NormalAmountTooltip(AmountTooltip containerTooltip) {
        this.ingres = containerTooltip.ingredients();
        this.isRandom = containerTooltip.isRandom();
        this.isOverSize = containerTooltip.isOverSize();
    }

    @Override
    public int getHeight() {
        return 30 + 10;
//        return 30;
    }

    @Override
    public int getWidth(Font font) {
        int tipMax = font.width(titleTip);
//        if (isRandom) {
        {
            MutableComponent tip = Component.translatable("gui.maidsoulkitchen.btn.cook_guide.warn.now_type")
                    .append(Component.translatable(String.format("gui.maidsoulkitchen.btn.cook_guide.type.%s", this.isRandom ? "blacklist" : "whitelist")));
            tipMax = Math.max(tipMax, font.width(tip));
        }
//        if (isOverSize) {
//            tipMax = Math.max(tipMax, font.width(overSizeTip));
//        }
        return Math.max(tipMax, ingres.size() * 20);
    }

    @SuppressWarnings("all")
    @Override
    public void renderImage(Font font, int pX, int pY, GuiGraphics guiGraphics) {
//        if (isRandom) {
        {
            MutableComponent tip = Component.translatable("gui.maidsoulkitchen.btn.cook_guide.warn.now_type")
                    .append(Component.translatable(String.format("gui.maidsoulkitchen.btn.cook_guide.type.%s", this.isRandom ? "blacklist" : "whitelist")));
            guiGraphics.drawString(font, tip, pX, pY, ChatFormatting.YELLOW.getColor());
            pY += 10;
        }
//        if (isOverSize) {
//            guiGraphics.drawString(font, overSizeTip, pX, pY, ChatFormatting.YELLOW.getColor());
//            pY += 10;
//        }
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
