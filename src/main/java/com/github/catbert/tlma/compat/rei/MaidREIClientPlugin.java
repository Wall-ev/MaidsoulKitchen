package com.github.catbert.tlma.compat.rei;

import com.github.catbert.tlma.client.gui.item.CookBagAbstractContainerGui;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZones;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZonesProvider;
import me.shedaniel.rei.forge.REIPluginClient;
import net.minecraft.client.renderer.Rect2i;

import java.util.ArrayList;
import java.util.List;

@REIPluginClient
public class MaidREIClientPlugin implements REIClientPlugin {

    @Override
    public void registerExclusionZones(ExclusionZones zones) {
        zones.register(CookBagAbstractContainerGui.class, (ExclusionZonesProvider<CookBagAbstractContainerGui<?>>) screen -> {
            List<Rectangle> rectangles = new ArrayList<>();
            for (Rect2i rect2i : screen.getExclusionArea()) {
                rectangles.add(new Rectangle(rect2i.getX(), rect2i.getY(), rect2i.getWidth(), rect2i.getHeight()));
            }
            return rectangles;
        });
    }
}