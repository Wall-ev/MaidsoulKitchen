package com.github.wallev.farmsoulkitchen.compat.jei;

import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import com.github.wallev.farmsoulkitchen.client.gui.item.CookBagAbstractContainerGui;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

@JeiPlugin
public class MaidJeiPlugin implements IModPlugin {
    private static final ResourceLocation UID = new ResourceLocation(FarmsoulKitchen.MOD_ID, "jei");

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registerTaskListArea(registration);
    }

    private void registerTaskListArea(IGuiHandlerRegistration registration) {
        registration.addGenericGuiContainerHandler(CookBagAbstractContainerGui.class, new IGuiContainerHandler<CookBagAbstractContainerGui<?>>() {
            @Override
            public List<Rect2i> getGuiExtraAreas(CookBagAbstractContainerGui<?> bagAbstractContainerGui) {
                return bagAbstractContainerGui.getExclusionArea();
            }
        });
    }

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }
}
