package com.github.wallev.maidsoulkitchen.client.gui.widget.button;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.api.task.v1.farm.ICompatFarmHandler;
import com.github.wallev.maidsoulkitchen.api.task.v1.farm.IHandlerInfo;
import com.github.tartaricacid.touhoulittlemaid.api.client.gui.ITooltipButton;
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

public class CFRuleButton extends Button implements ITooltipButton {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MaidsoulKitchen.MOD_ID, "textures/gui/farm_guide.png");
    protected final IHandlerInfo handlerInfo;
    protected final ICompatFarmHandler handler;
    private final List<ItemStack> blockItems = new ArrayList<>();
    private final ResultInfo ref = new ResultInfo(1, 9, 8, 8, 2, 2);
    protected boolean isSelected;
    private final List<Component> tooltips;

    public CFRuleButton(IHandlerInfo handlerInfo, ICompatFarmHandler handler, boolean isSelected, int pX, int pY, List<Component> tooltips) {
        super(pX, pY, 152, 24, Component.empty(), b -> {}, Supplier::get);
        this.handlerInfo = handlerInfo;
        this.handler = handler;
        this.isSelected = isSelected;

        int i = 0;
        for (Block block : ForgeRegistries.BLOCKS) {
            if (i > 9) break;
            if (handler.isFarmBlock(block)) {
                blockItems.add(new ItemStack(block));
                i++;
            }
        }

        this.tooltips = tooltips;
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft mc = Minecraft.getInstance();

        {
            int pV0ffset = this.isHovered ? this.height : 0;
            pGuiGraphics.blit(TEXTURE, this.getX(), this.getY(), 0, pV0ffset, this.width, this.height);
        }

        {
            pGuiGraphics.blit(TEXTURE, this.getX() + 3, this.getY() + 3, 152 + 2, 3, 18, 18);
            pGuiGraphics.renderItem(handlerInfo.getIcon(), this.getX() + 4, this.getY() + 4);
        }

        {
            int pV0ffset = this.isSelected ? 0 : 24; // 0 : 24
            pGuiGraphics.blit(TEXTURE, this.getX() + 131, this.getY() + 3, 152 + 2, 3, 18, 18);
            pGuiGraphics.blit(TEXTURE, this.getX() + 131 + 1 + 1, this.getY() + 3 + 1 + 1, 152 + 2 + 18 + 2, 5 + pV0ffset, 14, 14);
        }

        {
            pGuiGraphics.drawString(mc.font, handlerInfo.getName(), this.getX() + 24, this.getY() + 3, 0x404040, false);

            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().scale(0.5f, 0.5f, 1);
            int i = 0;
            for (ItemStack itemStack : blockItems) {
                pGuiGraphics.renderItem(itemStack, (this.getX() + 24 + (i++ * 10)) * 2, (this.getY() + 13) * 2);
            }
            pGuiGraphics.pose().popPose();
        }
    }

    @Override
    public boolean isTooltipHovered() {
        return this.isHovered();
    }

    @Override
    public void renderTooltip(GuiGraphics graphics, Minecraft mc, int mouseX, int mouseY) {
        if (isHovered()) {
            this.renderResStackTooltip(graphics, mc, mouseX, mouseY);
        }
    }

    private void renderResStackTooltip(GuiGraphics graphics, Minecraft mc, int mouseX, int mouseY) {
        int index = checkCoordinate2(mouseX, mouseY, this.getX() + 24, this.getY() + 13);
        if (index != -1 && index < blockItems.size()) {
            graphics.renderTooltip(mc.font, blockItems.get(index), mouseX, mouseY);
        } else {
            graphics.renderComponentTooltip(mc.font, this.tooltips, mouseX, mouseY);
        }
    }

    private int checkCoordinate2(double pMouseX, double pMouseY, int startX, int startY) {
        if (pMouseX < startX || pMouseY < startY) return -1;

        int offsetRow = (int) (pMouseX - startX);
        int offsetCol = (int) (pMouseY - startY);

        if (offsetRow % (ref.rowWidth() + ref.rowSpacing()) < ref.rowWidth() && offsetCol % (ref.colHeight() + ref.colSpacing()) < ref.colHeight()) {
            int blockCol = offsetRow / (ref.rowWidth() + ref.rowSpacing());
            int blockRow = offsetCol / (ref.colHeight() + ref.colSpacing());

            if (blockRow >= 0 && blockRow < ref.row() && blockCol >= 0 && blockCol < ref.col()) {
                int blockIndex = blockRow * ref.col() + blockCol;

                if (blockIndex >= 0 && blockIndex < ref.col() * ref.row()) {
                    return blockIndex;
                }
            }
        }
        return -1;

    }
}
