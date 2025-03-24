package com.github.wallev.maidsoulkitchen.client.tooltip;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.verhelper.client.chat.VComponent;
import com.github.wallev.maidsoulkitchen.inventory.tooltip.AmountTooltip;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.awt.*;
import java.util.List;

import static net.minecraft.client.gui.GuiComponent.blit;

public class NormalAmountTooltip implements ClientAmountTooltip {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MaidsoulKitchen.MOD_ID, "textures/gui/cook_guide.png");
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
            MutableComponent tip = VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.warn.now_type")
                    .append(VComponent.translatable(String.format("gui.maidsoulkitchen.btn.cook_guide.type.%s", this.isBlacklist ? "blacklist" : "whitelist")));
            tipMax = Math.max(tipMax, font.width(tip));
            MutableComponent canCook = VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.can_cook")
                    .append(VComponent.translatable(String.format("gui.maidsoulkitchen.btn.cook_guide.can_cook.%s", this.canCook() ? "true" : "false")));
            tipMax = Math.max(tipMax, font.width(canCook));
        }
        return Math.max(tipMax, ingres.size() * 20);
    }

    @Override
    public void renderImage(Font font, int pX, int pY, PoseStack poseStack, ItemRenderer itemRenderer, int blitOffset) {
        poseStack.pushPose();
        poseStack.translate(0, 0, blitOffset);
        {
            MutableComponent tip = VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.warn.now_type")
                    .append(VComponent.translatable(String.format("gui.maidsoulkitchen.btn.cook_guide.type.%s", this.isBlacklist ? "blacklist" : "whitelist")));
            font.draw(poseStack, tip, pX, pY, Color.YELLOW.getRGB());
            pY += 10;
            MutableComponent canCook = VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.can_cook")
                    .append(VComponent.translatable(String.format("gui.maidsoulkitchen.btn.cook_guide.can_cook.%s", this.canCook() ? "true" : "false")))
                    .withStyle(this.canCook() ? ChatFormatting.GREEN : ChatFormatting.RED);
            font.draw(poseStack, canCook, pX, pY, ChatFormatting.YELLOW.getColor());
            pY += 10;

        }

        font.draw(poseStack, titleTip, pX, pY, Color.GRAY.getRGB());
        int i = 0;
        pY += 10;
        for (Ingredient ingre : this.ingres) {
            ItemStack[] stackItems = ingre.getItems();
            if (stackItems.length == 0) {
                continue;
            }

            int xOffset = pX + i++ * 20;

            ItemStack itemStack = stackItems[0];

            itemRenderer.renderGuiItem(itemStack, xOffset, pY);
            if (itemStack.getCount() > 1) {
                itemRenderer.renderGuiItemDecorations(font, itemStack, xOffset, pY);
            }

            if (stackItems.length > 1) {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, TEXTURE);
                blit(poseStack, xOffset, pY + 13, 0, 253, 3, 3, 256, 256);
            }
        }

        poseStack.popPose();
    }
    private boolean canCook() {
        if (isBlacklist) {
            return !cookData.blacklistRecs().contains(recipeId);
        } else {
            return cookData.whitelistRecs().contains(recipeId);
        }
    }

}
