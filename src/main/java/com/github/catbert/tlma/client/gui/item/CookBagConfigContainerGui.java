package com.github.catbert.tlma.client.gui.item;

import com.github.catbert.tlma.client.gui.widget.button.CookBagModeButton;
import com.github.catbert.tlma.inventory.container.item.BagType;
import com.github.catbert.tlma.inventory.container.item.CookBagConfigContainer;
import com.github.catbert.tlma.item.bauble.ItemCookBag;
import com.github.catbert.tlma.network.NetworkHandler;
import com.github.catbert.tlma.network.message.SetCookBagBindModeMessage;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import org.anti_ad.mc.ipn.api.IPNIgnore;

import java.awt.*;

@IPNIgnore
public class CookBagConfigContainerGui extends CookBagAbstractContainerGui<CookBagConfigContainer> {
    protected String bindMode;

    public CookBagConfigContainerGui(CookBagConfigContainer container, Inventory inv, Component titleIn) {
        super(container, inv, Component.literal("Cook Bag Config"));
        this.bindMode = ItemCookBag.getBindMode(menu.cookBag);
    }

    @Override
    protected void init() {
        super.init();

        this.addBindModeButtons();
    }

    private void addBindModeButtons() {
        int x = leftPos + 6;
        int y = topPos + 20;
        for (BagType value : BagType.values()) {
            MutableComponent title = Component.literal(value.name);
            CookBagModeButton cookBagModeButton = new CookBagModeButton(x, y += 22, Math.max(font.width(title) + 8, 100), 20, title, b -> {
            }) {
                @Override
                public void onClick(double pMouseX, double pMouseY) {
                    super.onClick(pMouseX, pMouseY);
                    bindMode = value.name;
                    NetworkHandler.sendToServer(new SetCookBagBindModeMessage(bindMode));
                }

                @Override
                public void renderString(GuiGraphics pGuiGraphics, Font pFont, int pColor) {
                    if (bindMode.equals(getMessage().getString())) {
                        pColor = Color.GREEN.getRGB();
                    }
                    super.renderString(pGuiGraphics, pFont, pColor);
                }
            };

            this.addRenderableWidget(cookBagModeButton);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(this.font, this.titleComponent, this.titleLabelX, this.titleLabelY, 4210752, false);
        pGuiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int middleX = (this.width - this.imageWidth) / 2;
        int middleY = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(CONFIG_BACKGROUND, middleX, middleY, 0, 0, this.imageWidth, this.imageHeight);
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
    }
}
