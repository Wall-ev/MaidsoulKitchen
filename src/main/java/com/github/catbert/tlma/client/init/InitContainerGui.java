package com.github.catbert.tlma.client.init;

import com.github.catbert.tlma.client.gui.entity.maid.cook.CompatFarmConfigerGui;
import com.github.catbert.tlma.client.gui.entity.maid.cook.CookConfigerGui;
import com.github.catbert.tlma.client.gui.item.WiosContainerGui;
import com.github.catbert.tlma.inventory.container.CompatFarmConfigerContainer;
import com.github.catbert.tlma.inventory.container.CookConfigerContainer;
import com.github.catbert.tlma.inventory.container.WiosContainer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class InitContainerGui {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent evt) {
        evt.enqueueWork(() -> MenuScreens.register(CookConfigerContainer.TYPE, CookConfigerGui::new));
        evt.enqueueWork(() -> MenuScreens.register(CompatFarmConfigerContainer.TYPE, CompatFarmConfigerGui::new));
        evt.enqueueWork(() -> MenuScreens.register(WiosContainer.TYPE, WiosContainerGui::new));
    }
}
