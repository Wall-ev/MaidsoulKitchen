package com.github.wallev.farmsoulkitchen.client.gui.item;

import com.github.wallev.farmsoulkitchen.client.gui.widget.button.CookBagModeButton;
import com.github.wallev.farmsoulkitchen.inventory.container.item.BagType;
import com.github.wallev.farmsoulkitchen.inventory.container.item.CookBagConfigContainer;
import com.github.wallev.farmsoulkitchen.item.ItemCulinaryHub;
import com.github.wallev.farmsoulkitchen.network.NetworkHandler;
import com.github.wallev.farmsoulkitchen.network.message.ClearCookBagBindPosesMessage;
import com.github.wallev.farmsoulkitchen.network.message.SetCookBagBindModeMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;
import org.anti_ad.mc.ipn.api.IPNIgnore;

import java.awt.*;
import java.util.Objects;

@IPNIgnore
public class CookBagConfigContainerGui extends CookBagAbstractContainerGui<CookBagConfigContainer> {
    protected String bindMode;

    public CookBagConfigContainerGui(CookBagConfigContainer container, Inventory inv, Component titleIn) {
        super(container, inv, Component.translatable("gui.farmsoulkitchen.culinary_hub.config.title"));
        this.bindMode = ItemCulinaryHub.getBindMode(menu.cookBag);
    }

    @Override
    protected void init() {
        super.init();

        this.addBindModeButtons();
    }

    private void addBindModeButtons() {
        int x = leftPos + 6;
        int y = topPos + 6;
        for (BagType value : BagType.values()) {
            MutableComponent title = Component.translatable("gui.farmsoulkitchen.culinary_hub.config.bind_mode." + value.translateKey);

            if (value == BagType.INGREDIENT_ADDITION || value == BagType.START_ADDITION) {
                title.append(Component.translatable("gui.farmsoulkitchen.development")).withStyle(ChatFormatting.YELLOW);
            }

            CookBagModeButton cookBagModeButton = new CookBagModeButton(x, y += 22, 100, 20, title, b -> {
            }) {
                @Override
                public void onClick(double pMouseX, double pMouseY) {
                    super.onClick(pMouseX, pMouseY);
                    bindMode = value.name;
                    NetworkHandler.sendToServer(new SetCookBagBindModeMessage(bindMode));
                }

                @Override
                public void renderString(GuiGraphics pGuiGraphics, Font pFont, int pColor) {
                    if (Objects.equals(bindMode, value.name)) {
                        pColor = Color.GREEN.getRGB();
                    }
                    super.renderString(pGuiGraphics, pFont, pColor);
                }
            };

            if (value == BagType.INGREDIENT_ADDITION || value == BagType.START_ADDITION) {
                cookBagModeButton.active = false;
            }

            this.addRenderableWidget(cookBagModeButton);
        }

        Button clearButton = Button.builder(Component.translatable("gui.farmsoulkitchen.culinary_hub.config.clear_bind_poses").withStyle(ChatFormatting.YELLOW), b -> {
                    NetworkHandler.sendToServer(new ClearCookBagBindPosesMessage());
                    onClose();
                })
                .bounds(x, y += 22, 100, 20)
                .build();
        this.addRenderableWidget(clearButton);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int x = leftPos + 6;
        int y = topPos + 6;
        int width = 100;
        guiGraphics.renderItem(Items.RED_MUSHROOM.getDefaultInstance(), x + width + 2, y + 22);
        guiGraphics.renderItem(Items.BROWN_MUSHROOM.getDefaultInstance(), x + width + 16 + 2, y + 22);
        guiGraphics.renderItem(Items.COAL.getDefaultInstance(), x + width + 2, y + 44 + 2);
        guiGraphics.renderItem(Items.WATER_BUCKET.getDefaultInstance(), x + width + 16 + 2, y + 44 + 2);
        guiGraphics.renderItem(Items.BOWL.getDefaultInstance(), x + width + 2, y + 88 + 2);
        guiGraphics.renderItem(Items.GLASS_BOTTLE.getDefaultInstance(), x + width + 16 + 2, y + 88 + 2);
        guiGraphics.renderItem(Items.MUSHROOM_STEW.getDefaultInstance(), x + width + 2, y + 110 + 2);
    }

    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(this.font, Component.translatable("gui.farmsoulkitchen.culinary_hub.config.bind_mode"), this.titleLabelX, this.titleLabelY + 12, 4210752, false);
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
