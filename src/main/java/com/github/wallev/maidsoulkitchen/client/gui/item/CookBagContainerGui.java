package com.github.wallev.maidsoulkitchen.client.gui.item;

import com.github.wallev.maidsoulkitchen.vhelper.client.chat.VComponent;
import com.github.wallev.maidsoulkitchen.inventory.container.item.CookBagContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.anti_ad.mc.ipn.api.IPNIgnore;

@IPNIgnore
@OnlyIn(Dist.CLIENT)
public class CookBagContainerGui extends CookBagAbstractContainerGui<CookBagContainer> {
    public CookBagContainerGui(CookBagContainer container, Inventory inv, Component titleIn) {
        super(container, inv, VComponent.translatable("gui.maidsoulkitchen.culinary_hub.bag.title"));
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(this.font, this.titleComponent, this.titleLabelX, this.titleLabelY, 4210752, false);
        pGuiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
        pGuiGraphics.drawString(font, VComponent.translatable("gui.maidsoulkitchen.culinary_hub.bag.ingredient"), inventoryLabelX, 12, 4210752, false);
        pGuiGraphics.drawString(font, VComponent.translatable("gui.maidsoulkitchen.culinary_hub.bag.output"), inventoryLabelX, 132, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int middleX = (this.width - this.imageWidth) / 2;
        int middleY = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(CONTAINER_BACKGROUND, middleX, middleY, 0, 0, this.imageWidth, this.imageHeight);
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
    }
}
