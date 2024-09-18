package com.github.catbert.tlma.client.init;

import com.github.catbert.tlma.client.gui.entity.maid.cook.CompatFarmConfigureGui;
import com.github.catbert.tlma.client.gui.entity.maid.cook.CookConfigureGui;
import com.github.catbert.tlma.inventory.container.CompatFarmConfigureContainer;
import com.github.catbert.tlma.inventory.container.CookConfigureContainer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class InitContainerGui {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent evt) {
        evt.enqueueWork(() -> MenuScreens.register(CookConfigureContainer.TYPE, CookConfigureGui::new));
        evt.enqueueWork(() -> MenuScreens.register(CompatFarmConfigureContainer.TYPE, CompatFarmConfigureGui::new));
    }
}
