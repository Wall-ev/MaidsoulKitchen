package com.github.wallev.farmsoulkitchen.compat.cloth.event;

import com.github.wallev.farmsoulkitchen.compat.cloth.MenuIntegration;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AddClothConfigEvent {

    @SubscribeEvent
    public void addConfig(com.github.tartaricacid.touhoulittlemaid.api.event.client.AddClothConfigEvent event) {
        ConfigBuilder root = event.getRoot();
        ConfigEntryBuilder entryBuilder = event.getEntryBuilder();
        MenuIntegration.addConfig(root, entryBuilder, true);
    }

}
