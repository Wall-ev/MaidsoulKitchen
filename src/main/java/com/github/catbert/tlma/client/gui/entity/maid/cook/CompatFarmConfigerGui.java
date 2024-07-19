package com.github.catbert.tlma.client.gui.entity.maid.cook;

import com.github.catbert.tlma.inventory.container.CompatFarmConfigerContainer;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import net.minecraft.network.chat.Component;
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
    public void init(IMaidTask task) {

    }
}
