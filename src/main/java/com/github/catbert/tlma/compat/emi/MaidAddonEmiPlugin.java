package com.github.catbert.tlma.compat.emi;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.widget.Bounds;

@EmiEntrypoint
public class MaidAddonEmiPlugin implements EmiPlugin {

    private void registerExclusionArea(EmiRegistry registry) {
        registry.addGenericExclusionArea(((screen, consumer) -> {
            if (!(screen instanceof AbstractMaidContainerGui<?> maidContainerGui)) {
                return;
            }
            consumer.accept(new Bounds(maidContainerGui.getGuiLeft() + 251 + 5, maidContainerGui.getGuiTop() + 26 + 7, 21, 99));
        }));
    }

    @Override
    public void register(EmiRegistry registry) {
        registerExclusionArea(registry);
    }
}