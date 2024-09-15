package com.github.catbert.tlma.compat.rei;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZones;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZonesProvider;
import me.shedaniel.rei.forge.REIPluginClient;

import java.util.Collections;

@REIPluginClient
public class MaidAddonREIClientPlugin implements REIClientPlugin {

    @Override
    public void registerExclusionZones(ExclusionZones zones) {
        zones.register(AbstractMaidContainerGui.class, (ExclusionZonesProvider<AbstractMaidContainerGui<?>>) screen -> {
            return Collections.singletonList(new Rectangle(screen.getGuiLeft() + 251 + 5, screen.getGuiTop() + 26 + 7, 21, 99));
        });
    }

}