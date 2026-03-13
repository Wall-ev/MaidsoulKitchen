package com.github.wallev.maidsoulkitchen;

import com.github.wallev.maidsoulkitchen.config.GeneralConfig;
import com.github.wallev.maidsoulkitchen.init.MkContainer;
import com.github.wallev.maidsoulkitchen.init.MkEffects;
import com.github.wallev.maidsoulkitchen.init.MkItems;
import com.github.wallev.maidsoulkitchen.init.MkMemories;
import com.github.wallev.maidsoulkitchen.item.ItemCulinaryHub;
import com.github.wallev.maidsoulkitchen.network.NetworkHandler;
import com.github.wallev.maidsoulkitchen.vhelper.IModInfo;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(MaidsoulKitchen.MOD_ID)
public final class MaidsoulKitchen implements IModInfo {

    public MaidsoulKitchen(IEventBus modEventBus, ModContainer modContainer) {
        initRegister(modEventBus, modContainer);
        initConfigureRegister(modEventBus, modContainer);
    }

    private static void initRegister(IEventBus modEventBus, ModContainer modContainer) {
        MkItems.ITEMS.register(modEventBus);
        MkEffects.EFFECTS.register(modEventBus);
        MkContainer.CONTAINER_TYPE.register(modEventBus);
        MkMemories.MEMORY_MODULE_TYPES.register(modEventBus);
        ItemCulinaryHub.DATA_COMPONENTS.register(modEventBus);
        modEventBus.addListener(NetworkHandler::registerPacket);
    }

    private static void initConfigureRegister(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, GeneralConfig.init());
    }
}
