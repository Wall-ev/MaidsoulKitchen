package com.github.wallev.farmsoulkitchen.event;

import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import com.github.wallev.farmsoulkitchen.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.init.InitCreativeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = FarmsoulKitchen.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ItemTabEvent {
    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == InitCreativeTabs.MAIN_TAB.getKey()){
            InitItems.ITEMS.getEntries().stream()
                    .filter(RegistryObject::isPresent)
                    .forEach(entry -> event.accept(entry.get()));
        }
    }
}
