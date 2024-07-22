package com.github.catbert.tlma.client.gui.widget.button;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.task.farm.handler.v1.berry.BerryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CFRuleButton extends Button implements ITooltipBtn {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TLMAddon.MOD_ID, "textures/gui/farm_guide.png");
    private final BerryHandler handlerInfo;
    private final List<ItemStack> blockItems = new ArrayList<>();

    public CFRuleButton(BerryHandler handlerInfo, int pX, int pY, OnPress pOnPress) {
        super(pX, pY, 152, 24, Component.empty(), pOnPress, Supplier::get);
        this.handlerInfo = handlerInfo;

        int i = 0;
        for (Block block : ForgeRegistries.BLOCKS) {
            if (i > 9) break;
            if (handlerInfo.isFarmBlock(block)) {
                blockItems.add(new ItemStack(block));
                i++;
            }
        }
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft mc = Minecraft.getInstance();

        int pV0ffset = this.isHovered ? this.height : 0;
        pGuiGraphics.blit(TEXTURE, this.getX(), this.getY(), 0, pV0ffset, this.width, this.height);

        pGuiGraphics.blit(TEXTURE, this.getX() + 3, this.getY() + 3, 152 + 2, 3, 18, 18);
        pGuiGraphics.renderItem(handlerInfo.getIcon(), this.getX() + 4, this.getY() + 4);

        pGuiGraphics.blit(TEXTURE, this.getX() + 131, this.getY() + 3, 152 + 2, 3, 18, 18);
        pGuiGraphics.blit(TEXTURE, this.getX() + 131 + 1 + 1, this.getY() + 3 + 1 + 1, 152 + 2 + 18 + 2, 5, 14, 14);
//        pGuiGraphics.blit(TEXTURE, this.getX() + 131 + 1, this.getY() + 3 + 1, 152 + 2 + 18 + 2, 5 + 24, 14, 14);

        pGuiGraphics.drawString(mc.font, handlerInfo.getName(), this.getX() + 24, this.getY() + 3, 0x404040, false);

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(0.5f, 0.5f, 1);
        pGuiGraphics.renderItem(handlerInfo.getIcon(), (this.getX() + 24) * 2, (this.getY() + 13) * 2);
        int i = 0;
        for (ItemStack itemStack : blockItems) {
            pGuiGraphics.renderItem(itemStack, (this.getX() + 24 + (i++ * 10)) * 2, (this.getY() + 13) * 2);
        }
        pGuiGraphics.pose().popPose();
    }

    @Override
    public void renderTooltip(GuiGraphics graphics, Minecraft mc, int mouseX, int mouseY) {

    }
}
