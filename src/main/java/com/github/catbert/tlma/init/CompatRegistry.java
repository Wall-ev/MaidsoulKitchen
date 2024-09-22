package com.github.catbert.tlma.init;

import com.github.catbert.tlma.compat.cloth.ClothCompat;
import com.github.catbert.tlma.compat.patchouli.PatchouliCompat;
import com.github.tartaricacid.touhoulittlemaid.client.gui.mod.ClothConfigScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CompatRegistry {
    public static final String CLOTH_CONFIG = "cloth_config";
    public static final String PATCHOULI = "patchouli";
    @SubscribeEvent
    public static void onEnqueue(final InterModEnqueueEvent event) {
        event.enqueueWork(() -> checkModLoad(CLOTH_CONFIG, () -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                ClothCompat.init();
            }
        }));
        event.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                ClothConfigScreen.registerNoClothConfigPage();
            }
        });
        event.enqueueWork(() -> checkModLoad(PATCHOULI, PatchouliCompat::init));
    }

    private static void checkModLoad(String modId, Runnable runnable) {
        if (ModList.get().isLoaded(modId)) {
            runnable.run();
        }
    }
}
