package com.github.catbert.tlma.client.gui.entity.maid.farm;

import com.github.catbert.tlma.client.gui.entity.maid.MaidTaskConfigGui;
import com.github.catbert.tlma.client.gui.widget.button.NavCompatMelonConfigButton;
import com.github.catbert.tlma.inventory.container.maid.CompatMelonConfigContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import org.anti_ad.mc.ipn.api.IPNButton;
import org.anti_ad.mc.ipn.api.IPNGuiHint;
import org.anti_ad.mc.ipn.api.IPNPlayerSideOnly;

// todo
// 直接将全局配置集成在MaidConfigContainerGui里,并以可视化方式呈现出来
@IPNPlayerSideOnly
@IPNGuiHint(button = IPNButton.SORT, horizontalOffset = -36, bottom = -12)
@IPNGuiHint(button = IPNButton.SORT_COLUMNS, horizontalOffset = -24, bottom = -24)
@IPNGuiHint(button = IPNButton.SORT_ROWS, horizontalOffset = -12, bottom = -36)
@IPNGuiHint(button = IPNButton.SHOW_EDITOR, horizontalOffset = -5)
@IPNGuiHint(button = IPNButton.SETTINGS, horizontalOffset = -5)
public class CompatMelonConfigGui extends MaidTaskConfigGui<CompatMelonConfigContainer> {

    public CompatMelonConfigGui(CompatMelonConfigContainer compatMelonConfigContainer, Inventory inv, Component titleIn) {
        super(compatMelonConfigContainer, inv, Component.empty());
    }

    @Override
    protected void initAdditionWidgets() {
        super.initAdditionWidgets();
        this.addNavCompatMelonConfigButton();
    }

    private void addNavCompatMelonConfigButton() {

        MutableComponent translatable = Component.translatable("gui.touhou_little_maid_addon.config.global_config").withStyle(ChatFormatting.ITALIC);
        int startX = ((visualZone.width() - font.width(translatable)) / 2) + visualZone.startX();
        int startY = ((visualZone.height() - font.lineHeight) / 2) + visualZone.startY();
        int width = font.width(translatable);
        int height = font.lineHeight + 1;

        NavCompatMelonConfigButton navCompatMelonConfigButton = new NavCompatMelonConfigButton(startX, startY, width, height, translatable);
        this.addRenderableWidget(navCompatMelonConfigButton);
    }
}
