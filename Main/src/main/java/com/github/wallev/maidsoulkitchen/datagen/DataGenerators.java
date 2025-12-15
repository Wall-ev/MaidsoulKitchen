package com.github.wallev.maidsoulkitchen.datagen;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.datagen.lang.ModLanguageProvider;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskModClazzManager;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.gen.TaskCompatExtractor;
import com.github.wallev.maidsoulkitchen.util.DevUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = MaidsoulKitchen.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) throws Exception {
        MaidsoulKitchen.LOGGER.debug("is gatherdata from DataGenerators MaidsoulKitchen");
        if (!DevUtil.isDevEnv())
            return;
        MaidsoulKitchen.LOGGER.debug("is gatherdata from DataGenerators MaidsoulKitchen");


        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        var vanillaPack = generator.getVanillaPack(true);
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();

        var blockTagsProvider = vanillaPack.addProvider(output -> new ModBlockTags(output, registries, MaidsoulKitchen.MOD_ID, existingFileHelper));
        vanillaPack.addProvider(output -> new ModItemTags(output, registries, blockTagsProvider.contentsGetter(), MaidsoulKitchen.MOD_ID, existingFileHelper));
        vanillaPack.addProvider(output -> new ModRecipeGenerator(output));
        generator.addProvider(event.includeServer(), new ModDamageTypeTags(packOutput, registries, existingFileHelper));

        if (event.includeClient()) {
            generator.addProvider(event.includeClient(), new ModLanguageProvider(packOutput, MaidsoulKitchen.MOD_ID, ModLanguageProvider.Local.EN_US));
            generator.addProvider(event.includeClient(), new ModLanguageProvider(packOutput, MaidsoulKitchen.MOD_ID, ModLanguageProvider.Local.ZH_CN));
        }

        Path rootOutputFolder = event.getGenerator().rootOutputFolder;
        TaskModClazzManager.writeModTaskClazzFile(rootOutputFolder);

        TaskCompatExtractor.solver(rootOutputFolder);
    }
}
