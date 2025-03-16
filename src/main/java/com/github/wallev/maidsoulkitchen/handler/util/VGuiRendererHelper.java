package com.github.wallev.maidsoulkitchen.handler.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class VGuiRendererHelper {

    public static void renderScrollingString(PoseStack poseStack, Font pFont, Component pText, Button button, int pColor) {
        int i = button.x + 2;
        int j = button.x + button.getWidth() - 2;
        renderScrollingString(poseStack, pFont, pText, i, button.y, j, button.y + button.getHeight(), pColor);
    }

    public static void renderScrollingString(PoseStack poseStack, Font pFont, Component pText, int pMinX, int pMinY, int pMaxX, int pMaxY, int pColor) {
        int i = pFont.width(pText);
        int j = (pMinY + pMaxY - 9) / 2 + 1;
        int k = pMaxX - pMinX;
        if (i > k) {
            // @todo 绘制滚动信息
            GuiComponent.drawString(poseStack, pFont, pText, pMinX, j, pColor);

//            int l = i - k;
//            double d0 = (double) Util.getMillis() / 1000.0D;
//            double d1 = Math.max((double) l * 0.5D, 3.0D);
//            double d2 = Math.sin((Math.PI / 2D) * Math.cos((Math.PI * 2D) * d0 / d1)) / 2.0D + 0.5D;
//            double d3 = Mth.lerp(d2, 0.0D, l);
//
//            RenderSystem.enableScissor(pMinX, pMinY, pMaxX, pMaxY);
//            GuiComponent.drawString(poseStack, pFont, pText, pMinX - (int) d3, j, pColor);
//            RenderSystem.disableScissor();
        } else {
            GuiComponent.drawCenteredString(poseStack, pFont, pText, (pMinX + pMaxX) / 2, j, pColor);
        }

    }

}
