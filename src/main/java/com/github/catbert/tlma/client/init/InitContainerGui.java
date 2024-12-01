package com.github.catbert.tlma.client.init;

import com.github.catbert.tlma.client.gui.entity.maid.cook.*;
import com.github.catbert.tlma.client.gui.entity.maid.farm.*;
import com.github.catbert.tlma.client.gui.item.CookBagConfigContainerGui;
import com.github.catbert.tlma.client.gui.item.CookBagGui;
import com.github.catbert.tlma.inventory.container.item.CookBagConfigContainer;
import com.github.catbert.tlma.inventory.container.item.CookBagContainer;
import com.github.catbert.tlma.inventory.container.maid.*;
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
        evt.enqueueWork(() -> MenuScreens.register(BerryFarmConfigContainer.TYPE, BerryFarmConfigGui::new));
        evt.enqueueWork(() -> MenuScreens.register(FruitFarmConfigContainer.TYPE, FruitFarmConfigGui::new));
        evt.enqueueWork(() -> MenuScreens.register(CompatMelonConfigContainer.TYPE, CompatMelonConfigGui::new));
        evt.enqueueWork(() -> MenuScreens.register(CookBagContainer.TYPE, CookBagGui::new));
        evt.enqueueWork(() -> MenuScreens.register(CookBagConfigContainer.TYPE, CookBagConfigContainerGui::new));
    }
}
