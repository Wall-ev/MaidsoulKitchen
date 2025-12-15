package com.github.wallev.maidsoulkitchen.event;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.config.subconfig.TaskConfig;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

@Mod.EventBusSubscriber(modid = MaidsoulKitchen.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlackHubChestDefineEvent {

    public static final Set<Block> BLACK_HUB_CHEST_LIST = new HashSet<>();
    private static final String CONFIG_NAME = MaidsoulKitchen.MOD_ID + "-common.toml";

    @SubscribeEvent
    public static void onEvent(ModConfigEvent.Loading event) {
        String fileName = event.getConfig().getFileName();
        if (CONFIG_NAME.equals(fileName)) {
            handleConfig();
        }
    }

    public static void handleConfig() {
        BLACK_HUB_CHEST_LIST.clear();
        handleMelonAndStemList(TaskConfig.BLACK_HUB_CHEST_LIST.get(), BLACK_HUB_CHEST_LIST);
    }

    private static void handleMelonAndStemList(List<String> config, Set<Block> output) {
        for (String blockId : config) {
            ResourceLocation location = VResourceLocation.tryParse(blockId);
            if (location == null) continue;
            Block block = ForgeRegistries.BLOCKS.getValue(location);
            if (block == null) continue;
            output.add(block);
        }
    }
}
