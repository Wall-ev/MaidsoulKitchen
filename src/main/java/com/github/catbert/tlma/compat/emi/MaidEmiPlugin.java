package com.github.catbert.tlma.compat.emi;

import com.github.catbert.tlma.client.gui.item.CookBagAbstractContainerGui;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.widget.Bounds;
import net.minecraft.client.renderer.Rect2i;

@EmiEntrypoint
public class MaidEmiPlugin implements EmiPlugin {

    private void registerExclusionArea(EmiRegistry registry) {
        registry.addGenericExclusionArea(((screen, consumer) -> {
            if (!(screen instanceof CookBagAbstractContainerGui<?> bagAbstractContainerGui)) {
                return;
            }
            for (Rect2i rect2i : bagAbstractContainerGui.getExclusionArea()) {
                consumer.accept(new Bounds(rect2i.getX(), rect2i.getY(), rect2i.getWidth(), rect2i.getHeight()));
            }
        }));
    }

    @Override
    public void register(EmiRegistry registry) {
        registerExclusionArea(registry);
    }
}