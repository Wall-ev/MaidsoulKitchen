package com.github.wallev.maidsoulkitchen.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.client.gui.mod.ClothConfigScreen;
import com.github.tartaricacid.touhoulittlemaid.compat.cloth.ClothConfigCompat;
import com.github.tartaricacid.touhoulittlemaid.init.registry.CompatRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.ModList;

import java.awt.*;

public class NavCompatMelonConfigButton extends Button {
    public NavCompatMelonConfigButton(int pX, int pY, int pWidth, int pHeight, Component pMessage) {
        super(pX, pY, pWidth, pHeight, pMessage, (b) -> {
            if (ModList.get().isLoaded(CompatRegistry.CLOTH_CONFIG)) {
                ClothConfigCompat.openConfigScreen();
            } else {
                ClothConfigScreen.open();
            }
        });
    }

    @Override
    protected void renderBg(PoseStack poseStack, Minecraft minecraft, int mouseX, int mouseY) {
        super.renderBg(poseStack, minecraft, mouseX, mouseY);

        Font font = Minecraft.getInstance().font;
        int color = isHovered ? Color.BLUE.getRGB() : Color.YELLOW.getRGB();
        font.draw(poseStack, getMessage(), x, y, color);
        fill(poseStack, x, y + font.lineHeight + 1, x + font.width(getMessage()), y + font.lineHeight + 2, color);
    }
}
