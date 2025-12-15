package com.github.wallev.maidsoulkitchen.client.tooltip;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.cook.v0.CookData;
import com.github.wallev.maidsoulkitchen.vhelper.client.chat.VComponent;
import com.github.wallev.maidsoulkitchen.inventory.tooltip.AmountTooltip;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class NormalAmountTooltip implements ClientAmountTooltip {
    private static final ResourceLocation TEXTURE = VResourceLocation.of(MaidsoulKitchen.MOD_ID, "textures/gui/cook_guide.png");
    private final MutableComponent titleTip = VComponent.translatable("tooltips.maidsoulkitchen.amount.title");
    private final MutableComponent randomTip = VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.warn.not_select").withStyle(ChatFormatting.YELLOW);
//    private final MutableComponent overSizeTip = VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.warn.over_size", TaskConfig.COOK_SELECTED_RECIPES.get()).withStyle(ChatFormatting.YELLOW);
    private final String recipeId;
    private final List<Ingredient> ingres;
    private final boolean isBlacklist;
    private final boolean isOverSize;
    private final CookData cookData;

    public NormalAmountTooltip(AmountTooltip containerTooltip) {
        this.recipeId = containerTooltip.recipeId();
        this.ingres = containerTooltip.ingredients();
        this.isBlacklist = containerTooltip.isBlacklist();
        this.isOverSize = containerTooltip.isOverSize();
        this.cookData = containerTooltip.cookData();
    }

    @Override
    public int getHeight() {
        return 30 + 10 + 10;
    }

    @Override
    public int getWidth(Font font) {
        int tipMax = font.width(titleTip);
        {
            MutableComponent type = VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.warn.now_type")
                    .append(VComponent.translatable(String.format("gui.maidsoulkitchen.btn.cook_guide.type.%s", this.isBlacklist ? "blacklist" : "whitelist")));
            tipMax = Math.max(tipMax, font.width(type));
            MutableComponent canCook = VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.can_cook")
                    .append(VComponent.translatable(String.format("gui.maidsoulkitchen.btn.cook_guide.can_cook.%s", this.canCook() ? "true" : "false")));
            tipMax = Math.max(tipMax, font.width(canCook));
        }
        return Math.max(tipMax, ingres.size() * 20);
    }

    @SuppressWarnings("all")
    @Override
    public void renderImage(Font font, int pX, int pY, GuiGraphics guiGraphics) {
        {
            MutableComponent tip = VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.warn.now_type")
                    .append(VComponent.translatable(String.format("gui.maidsoulkitchen.btn.cook_guide.type.%s", this.isBlacklist ? "blacklist" : "whitelist")));
            guiGraphics.drawString(font, tip, pX, pY, ChatFormatting.YELLOW.getColor());
            pY += 10;
            MutableComponent canCook = VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.can_cook")
                    .append(VComponent.translatable(String.format("gui.maidsoulkitchen.btn.cook_guide.can_cook.%s", this.canCook() ? "true" : "false")))
                    .withStyle(this.canCook() ? ChatFormatting.GREEN : ChatFormatting.RED);
            guiGraphics.drawString(font, canCook, pX, pY, ChatFormatting.YELLOW.getColor());
            pY += 10;
        }

        guiGraphics.drawString(font, titleTip, pX, pY, ChatFormatting.GRAY.getColor());
        int i = 0;
        pY += 10;
        for (Ingredient ingre : this.ingres) {
            ItemStack[] stackItems = ingre.getItems();
            if (stackItems.length == 0) {
                continue;
            }

            int xOffset = pX + i++ * 20;

            ItemStack itemStack = stackItems[0];
            guiGraphics.renderItem(itemStack, xOffset, pY);
            if (itemStack.getCount() > 1) {
                guiGraphics.renderItemDecorations(font, itemStack, xOffset, pY);
            }

            if (stackItems.length > 1) {
                guiGraphics.blit(TEXTURE, xOffset, pY + 13, 0, 253, 3, 3);
            }
        }
    }

    private boolean canCook() {
        if (isBlacklist) {
            return !cookData.blacklistRecs().contains(recipeId);
        } else {
            return cookData.whitelistRecs().contains(recipeId);
        }
    }
}
