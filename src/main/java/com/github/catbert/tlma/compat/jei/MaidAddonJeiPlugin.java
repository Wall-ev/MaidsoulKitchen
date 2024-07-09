package com.github.catbert.tlma.compat.jei;

import com.github.catbert.tlma.TLMAddon;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.List;

@JeiPlugin
public class MaidAddonJeiPlugin implements IModPlugin {
    private static final ResourceLocation UID = new ResourceLocation(TLMAddon.MOD_ID, "jei");

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registerExtraAreas(registration);
    }

    private void registerExtraAreas(IGuiHandlerRegistration registration) {
        registration.addGenericGuiContainerHandler(AbstractMaidContainerGui.class, new IGuiContainerHandler<AbstractMaidContainerGui<?>>() {
            @Override
            public List<Rect2i> getGuiExtraAreas(AbstractMaidContainerGui containerScreen) {
                return Collections.singletonList(new Rect2i(containerScreen.getGuiLeft() + 251 + 5, containerScreen.getGuiTop() + 26 + 7, 21, 74));
            }
        });
    }

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }
}
