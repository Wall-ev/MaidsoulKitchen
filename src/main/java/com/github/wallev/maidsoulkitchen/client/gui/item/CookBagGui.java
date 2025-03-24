package com.github.wallev.maidsoulkitchen.client.gui.item;

import com.github.wallev.verhelper.client.chat.VComponent;
import com.github.wallev.maidsoulkitchen.inventory.container.item.CookBagContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.anti_ad.mc.ipn.api.IPNIgnore;

@IPNIgnore
public class CookBagGui extends CookBagAbstractContainerGui<CookBagContainer> {
    public CookBagGui(CookBagContainer container, Inventory inv, Component titleIn) {
        super(container, inv, VComponent.translatable("gui.maidsoulkitchen.culinary_hub.bag.title"));
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.render(poseStack, mouseX, mouseY, partialTick);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int pMouseX, int pMouseY) {
        font.draw(poseStack, this.titleComponent, this.titleLabelX, this.titleLabelY, 4210752);
        font.draw(poseStack, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752);
        font.draw(poseStack, VComponent.translatable("gui.maidsoulkitchen.culinary_hub.bag.ingredient"), inventoryLabelX, 12, 4210752);
        font.draw(poseStack, VComponent.translatable("gui.maidsoulkitchen.culinary_hub.bag.other"), inventoryLabelX, 78, 4210752);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);

        int middleX = (this.width - this.imageWidth) / 2;
        int middleY = (this.height - this.imageHeight) / 2;
        blit(poseStack, middleX, middleY, 0, 0, this.imageWidth, this.imageHeight);
        super.renderBg(poseStack, partialTick, mouseX, mouseY);
    }
}
