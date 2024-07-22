package com.github.catbert.tlma.client.gui.entity.maid.cook;

import com.github.catbert.tlma.client.gui.widget.button.CFRuleButton;
import com.github.catbert.tlma.client.gui.widget.button.TitleInfoButton;
import com.github.catbert.tlma.inventory.container.CompatFarmConfigerContainer;
import com.github.catbert.tlma.task.farm.handler.v1.berry.BerryHandler;
import com.github.catbert.tlma.task.farm.handler.v1.berry.BerryHandlerManager;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import org.anti_ad.mc.ipn.api.IPNButton;
import org.anti_ad.mc.ipn.api.IPNGuiHint;
import org.anti_ad.mc.ipn.api.IPNPlayerSideOnly;

@IPNPlayerSideOnly
@IPNGuiHint(button = IPNButton.SORT, horizontalOffset = -36, bottom = -12)
@IPNGuiHint(button = IPNButton.SORT_COLUMNS, horizontalOffset = -24, bottom = -24)
@IPNGuiHint(button = IPNButton.SORT_ROWS, horizontalOffset = -12, bottom = -36)
@IPNGuiHint(button = IPNButton.SHOW_EDITOR, horizontalOffset = -5)
@IPNGuiHint(button = IPNButton.SETTINGS, horizontalOffset = -5)
public class CompatFarmConfigerGui extends MaidTaskConfigerGui<CompatFarmConfigerContainer> {
    public CompatFarmConfigerGui(CompatFarmConfigerContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void init() {
        super.init();
        if (getMaid() != null) {
            this.addTitleButton();
            this.addRuleButton();
        }
    }

    @Override
    public void init(IMaidTask task) {
        this.init();
    }

    private void addTitleButton() {
        MutableComponent titleComponent = Component.translatable("gui.touhou_little_maid_addon.berry_farm_configer_screen.title");
        int titleStartX = visualZone.startX() + (visualZone.width() - font.width(titleComponent)) / 2;
        int titleStartY = visualZone.startY() + 7;
        TitleInfoButton titleInfoButton = new TitleInfoButton(titleStartX, titleStartY, font.width(titleComponent), 9, titleComponent);
        this.addRenderableWidget(titleInfoButton);
    }

    private void addRuleButton() {
        int startX = visualZone.startX() + 12;
        int startY = visualZone.startY() + 20;
        for (BerryHandlerManager berryHandler : BerryHandlerManager.values()) {
            BerryHandler berryHandler1 = berryHandler.getVineryBerryHandler();
            if (!berryHandler1.canLoad()) continue;
            CFRuleButton cfRuleButton = new CFRuleButton(berryHandler1, startX, startY, (b) -> {

            });
            this.addRenderableWidget(cfRuleButton);
            startY += 24 + 5;
        }
    }
}
