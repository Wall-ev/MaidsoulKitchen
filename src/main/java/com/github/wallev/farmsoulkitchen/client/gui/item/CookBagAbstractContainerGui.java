package com.github.wallev.farmsoulkitchen.client.gui.item;

import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import com.github.wallev.farmsoulkitchen.client.gui.widget.button.CookBagGuiSideTabButton;
import com.github.wallev.farmsoulkitchen.inventory.container.item.CookBagAbstractContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public abstract class CookBagAbstractContainerGui<T extends CookBagAbstractContainer> extends AbstractContainerScreen<T> {
    protected static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(FarmsoulKitchen.MOD_ID, "textures/gui/cook_bag_container.png");
    protected static final ResourceLocation CONFIG_BACKGROUND = new ResourceLocation(FarmsoulKitchen.MOD_ID, "textures/gui/cook_bag_config.png");
    protected final Component titleComponent;

    public CookBagAbstractContainerGui(T container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);
        this.titleComponent = titleIn;
        this.imageHeight = 256;
        this.inventoryLabelY = 162;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - font.width(titleComponent)) / 2;
        this.titleLabelY = 4;

        this.addSideTab();
    }

    private void addSideTab() {
        CookBagSideTabs<T> tCookBagSideTabs = new CookBagSideTabs<>(menu.containerId, leftPos + 171, topPos + 12);
        for (CookBagGuiSideTabButton tab : tCookBagSideTabs.getTabs(this)) {
            this.addRenderableWidget(tab);
        }
    }

    public List<Rect2i> getExclusionArea() {
        List<Rect2i> excludedArea = new ArrayList<>();
        int tabsSize = CookBagSideTabs.getTabHeights();
        excludedArea.add(new Rect2i(leftPos + 171, topPos + 12, 26, tabsSize));
        return excludedArea;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        this.drawSideTabGui(graphics, partialTicks, x, y);
    }

    // 绘制侧边栏底部贴图
    private void drawSideTabGui(GuiGraphics graphics, float partialTicks, int x, int y) {
//        graphics.blit(SIDE, leftPos + 176, topPos + 12, 235, 107, 21, 47);
    }
}
