package com.github.catbert.tlma.client.init;

import com.github.catbert.tlma.client.gui.entity.maid.cook.*;
import com.github.catbert.tlma.inventory.container.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class InitContainerGui {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent evt) {
        evt.enqueueWork(() -> MenuScreens.register(CookConfigContainer.TYPE, CookConfigGui::new));
        evt.enqueueWork(() -> MenuScreens.register(CompatFarmConfigContainer.TYPE, CompatFarmConfigGui::new));
        evt.enqueueWork(() -> MenuScreens.register(FruitFarmConfigContainer.TYPE, FruitFarmConfigGui::new));
        evt.enqueueWork(() -> MenuScreens.register(NoConfigContainer.TYPE, NoConfigGui::new));
        evt.enqueueWork(() -> MenuScreens.register(CompatMelonConfigContainer.TYPE, CompatMelonConfigGui::new));
    }
}
