package com.github.wallev.maidsoulkitchen.client.gui.widget.button;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.api.task.v1.farm.ICompatFarmHandler;
import com.github.wallev.maidsoulkitchen.api.task.v1.farm.IHandlerInfo;
import com.github.tartaricacid.touhoulittlemaid.api.client.gui.ITooltipButton;
import com.github.wallev.maidsoulkitchen.handler.VComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class CFRuleButton extends Button implements ITooltipButton {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MaidsoulKitchen.MOD_ID, "textures/gui/farm_guide.png");
    protected final IHandlerInfo handlerInfo;
    protected final ICompatFarmHandler handler;
    private final List<ItemStack> blockItems = new ArrayList<>();
    private final ResultInfo ref = new ResultInfo(1, 9, 8, 8, 2, 2);
    protected boolean isSelected;
    private final List<Component> tooltips;

    public CFRuleButton(IHandlerInfo handlerInfo, ICompatFarmHandler handler, boolean isSelected, int pX, int pY, List<Component> tooltips) {
        super(pX, pY, 152, 24, VComponent.empty(), b -> {});
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
    protected void renderBg(PoseStack poseStack, Minecraft minecraft, int mouseX, int mouseY) {
        super.renderBg(poseStack, minecraft, mouseX, mouseY);

        Minecraft mc = Minecraft.getInstance();
        {
            int pV0ffset = this.isHovered ? this.height : 0;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
            this.blit(poseStack, this.x, this.y, 0, pV0ffset, this.width, this.height);
        }

        {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
            this.blit(poseStack, this.x + 3, this.y + 3, 152 + 2, 3, 18, 18);
            mc.getItemRenderer().renderGuiItem(handlerInfo.getIcon(), this.x + 4, this.y + 4);
        }

        {

            int pV0ffset = this.isSelected ? 0 : 24; // 0 : 24
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
            this.blit(poseStack, this.x + 131, this.y + 3, 152 + 2, 3, 18, 18);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
            this.blit(poseStack, this.x + 131 + 1 + 1, this.y + 3 + 1 + 1, 152 + 2 + 18 + 2, 5 + pV0ffset, 14, 14);
        }

        {
            mc.font.draw(poseStack, handlerInfo.getName(), this.x + 24, this.y + 3, 0x404040);

            poseStack.pushPose();
            poseStack.scale(0.5f, 0.5f, 1);
            int i = 0;
            for (ItemStack itemStack : blockItems) {
                mc.getItemRenderer().renderGuiItem(itemStack, (this.x + 24 + (i++ * 10)) * 2, (this.y + 13) * 2);
            }
            poseStack.popPose();
        }
    }

    @Override
    public boolean isTooltipHovered() {
        return this.isHovered;
    }

    @Override
    public void renderTooltip(PoseStack poseStack, Minecraft minecraft, int mouseX, int mouseY) {
        if (isHovered) {
            this.renderToolTip(poseStack, mouseX, mouseY);
        }
    }

    private void renderResStackTooltip(PoseStack poseStack, Minecraft mc, int mouseX, int mouseY) {
        Screen screen = Minecraft.getInstance().screen;
        if (screen == null) {
            return;
        }

        int index = checkCoordinate2(mouseX, mouseY, this.x + 24, this.y + 13);
        if (index != -1 && index < blockItems.size()) {
            screen.renderComponentTooltip(poseStack, screen.getTooltipFromItem(blockItems.get(index)), mouseX, mouseY);
        } else {
            screen.renderComponentTooltip(poseStack, this.tooltips, mouseX, mouseY);
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
