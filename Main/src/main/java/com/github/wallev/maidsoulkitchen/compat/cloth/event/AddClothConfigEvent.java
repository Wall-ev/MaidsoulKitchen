package com.github.wallev.maidsoulkitchen.compat.cloth.event;

import com.github.wallev.maidsoulkitchen.compat.cloth.MenuIntegration;
import com.github.wallev.maidsoulkitchen.lib.auto.event.AutoEventSubscriber;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@AutoEventSubscriber(mods = Mods.CLOTH_CONFIG, value = Dist.CLIENT)
public class AddClothConfigEvent {

    @SubscribeEvent
    public static void addConfig(com.github.tartaricacid.touhoulittlemaid.api.event.client.AddClothConfigEvent event) {
        ConfigBuilder root = event.getRoot();
        ConfigEntryBuilder entryBuilder = event.getEntryBuilder();
        MenuIntegration.addConfig(root, entryBuilder, true);
    }

}
