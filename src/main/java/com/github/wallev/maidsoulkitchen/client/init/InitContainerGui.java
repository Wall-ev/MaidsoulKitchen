package com.github.wallev.maidsoulkitchen.client.init;

import com.github.wallev.maidsoulkitchen.client.gui.entity.maid.cook.CookConfigGui;
import com.github.wallev.maidsoulkitchen.client.gui.entity.maid.farm.BerryFarmConfigGui;
import com.github.wallev.maidsoulkitchen.client.gui.entity.maid.farm.CompatMelonConfigGui;
import com.github.wallev.maidsoulkitchen.client.gui.entity.maid.farm.FruitFarmConfigGui;
import com.github.wallev.maidsoulkitchen.client.gui.item.CookBagConfigContainerGui;
import com.github.wallev.maidsoulkitchen.client.gui.item.CookBagGui;
import com.github.wallev.maidsoulkitchen.inventory.container.item.CookBagConfigContainer;
import com.github.wallev.maidsoulkitchen.inventory.container.item.CookBagContainer;
import com.github.wallev.maidsoulkitchen.inventory.container.maid.BerryFarmConfigContainer;
import com.github.wallev.maidsoulkitchen.inventory.container.maid.CompatMelonConfigContainer;
import com.github.wallev.maidsoulkitchen.inventory.container.maid.CookConfigContainer;
import com.github.wallev.maidsoulkitchen.inventory.container.maid.FruitFarmConfigContainer;
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
