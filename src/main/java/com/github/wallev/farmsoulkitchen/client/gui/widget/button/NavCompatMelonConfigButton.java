package com.github.wallev.farmsoulkitchen.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.client.gui.mod.ClothConfigScreen;
import com.github.tartaricacid.touhoulittlemaid.compat.cloth.ClothConfigCompat;
import com.github.tartaricacid.touhoulittlemaid.init.registry.CompatRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.ModList;

import java.awt.*;
import java.util.function.Supplier;

public class NavCompatMelonConfigButton extends Button {
    public NavCompatMelonConfigButton(int pX, int pY, int pWidth, int pHeight, Component pMessage) {
        super(pX, pY, pWidth, pHeight, pMessage, (b) -> {
            if (ModList.get().isLoaded(CompatRegistry.CLOTH_CONFIG)) {
                ClothConfigCompat.openConfigScreen();
            } else {
                ClothConfigScreen.open();
            }
        }, Supplier::get);
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Font font = Minecraft.getInstance().font;
        int color = isHovered ? Color.BLUE.getRGB() : Color.YELLOW.getRGB();
        pGuiGraphics.drawString(font, getMessage(), getX(), getY(), color, false);
        pGuiGraphics.fill(getX(), getY() + font.lineHeight + 1, getX() + font.width(getMessage()), getY() + font.lineHeight + 2, color);
    }
}
