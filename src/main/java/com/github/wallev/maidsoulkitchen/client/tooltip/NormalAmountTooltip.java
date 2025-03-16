package com.github.wallev.maidsoulkitchen.client.tooltip;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.handler.VComponent;
import com.github.wallev.maidsoulkitchen.inventory.tooltip.AmountTooltip;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

import static net.minecraft.client.gui.GuiComponent.blit;

public class NormalAmountTooltip implements ClientAmountTooltip {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MaidsoulKitchen.MOD_ID, "textures/gui/cook_guide.png");
    private final MutableComponent titleTip = VComponent.translatable("tooltips.maidsoulkitchen.amount.title");
    private final MutableComponent randomTip = VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.warn.not_select").withStyle(ChatFormatting.YELLOW);
    //    private final MutableComponent overSizeTip = VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.warn.over_size", TaskConfig.COOK_SELECTED_RECIPES.get()).withStyle(ChatFormatting.YELLOW);
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
    }

    @Override
    public int getWidth(Font font) {
        int tipMax = font.width(titleTip);
        {
            MutableComponent tip = VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.warn.now_type")
                    .append(VComponent.translatable(String.format("gui.maidsoulkitchen.btn.cook_guide.type.%s", this.isRandom ? "blacklist" : "whitelist")));
            tipMax = Math.max(tipMax, font.width(tip));
        }
        return Math.max(tipMax, ingres.size() * 20);
    }

    @Override
    public void renderText(Font pFont, int pX, int pY, Matrix4f pMatrix4f, MultiBufferSource.BufferSource pBufferSource) {
        MutableComponent tip = VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.warn.now_type")
                .append(VComponent.translatable(String.format("gui.maidsoulkitchen.btn.cook_guide.type.%s", this.isRandom ? "blacklist" : "whitelist")));
        pFont.drawInBatch(tip, (float) pX, (float) pY, ChatFormatting.YELLOW.getColor(), true, pMatrix4f, pBufferSource, false, 0, 15728880);
        pY += 10;

        pFont.drawInBatch(titleTip, (float) pX, (float) pY, ChatFormatting.GRAY.getColor(), true, pMatrix4f, pBufferSource, false, 0, 15728880);
    }

    @Override
    public void renderImage(Font font, int pX, int pY, PoseStack poseStack, ItemRenderer itemRenderer, int blitOffset) {
        int i = 0;
        pY += 20;
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
                blit(poseStack, xOffset, pY + 13, 0, 253, 3, 3, 256, 256, 256, 256);
            }
        }
    }
}
