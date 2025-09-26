package com.github.wallev.maidsoulkitchen.compat.msm.init;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskModClazzManager;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.io.IOException;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MaidStorageManagerCompat {

    static void init() {
        if (!TaskInfo.MSM_CORE.canLoad()) {
            return;
        }
        new AddCraftAndStorageTypes();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void registerMaidStorageEventListener(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Mods.init();
            TaskInfo.init();
            try {
                TaskModClazzManager.init();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            init();
        });
    }
}
