package com.github.catbert.tlma.client.gui.widget.button;

import com.github.catbert.tlma.TLMAddon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public abstract class ResultButton extends Button {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TLMAddon.MOD_ID, "textures/gui/cook_guide.png");
    private final ResultInfo ref;
    public ResultButton(Zone bounds, ResultInfo ref) {
        super(bounds.startX(), bounds.startY(), bounds.width(), bounds.height(), Component.empty(), (b) -> {}, Supplier::get);
        this.ref = ref;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (this.active && this.visible) {
            if (this.isValidClickButton(pButton)) {
                boolean flag = this.clicked(pMouseX, pMouseY);
                int i = checkCoordinate2(pMouseX, pMouseY, this.getX(), this.getY());
                if (flag && i != -1 && !getItemStack(i).isEmpty()) {
                    this.playDownSound(Minecraft.getInstance().getSoundManager());
                    this.onClick(pMouseX, pMouseY);
                    return true;
                }
            }

        }
        return false;
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

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderItemSlot(guiGraphics);
    }

    protected void renderItemSlot(GuiGraphics pGuiGraphics) {
        for (int row = 0; row < ref.row(); row++) {
            for (int col = 0; col < ref.col(); col++) {
                int x = this.getX() + col * (ref.rowWidth() + ref.rowSpacing()); // 计算 x 坐标
                int y = this.getY() + row * (ref.colHeight() + ref.colSpacing()); // 计算 y 坐标
                int index = row * ref.col() + col; // 计算元素在列表中的索引
                if (index < ref.row() * ref.col()) {
                    ItemStack stack = this.getItemStack(index);
                    if (stack.isEmpty()) continue;

                    renderChildren(stack, false, pGuiGraphics, x, y);
                }
            }
        }
    }

    protected abstract ItemStack getItemStack(int index);
    public void renderChildren(ItemStack stack, boolean has, GuiGraphics pGuiGraphics, int x, int y) {
//        int pUOffset = getUOffset(stack);
//        int pVOffset = getVOffset(stack);
        pGuiGraphics.blit(TEXTURE, x, y, 179, 25, 20, 20);
        pGuiGraphics.renderItem(stack, x+2, y+2);
    }
}
