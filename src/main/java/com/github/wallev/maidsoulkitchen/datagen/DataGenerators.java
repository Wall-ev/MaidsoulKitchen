package com.github.wallev.maidsoulkitchen.datagen;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MaidsoulKitchen.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
//        DataGenerator generator = event.getGenerator();
//        PackOutput output = generator.getPackOutput();
//        ExistingFileHelper helper = event.getExistingFileHelper();
//        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
//
//        generator.addProvider(event.includeServer(), new ModDamageTypeTags(output, lookupProvider, helper));
    }
}
