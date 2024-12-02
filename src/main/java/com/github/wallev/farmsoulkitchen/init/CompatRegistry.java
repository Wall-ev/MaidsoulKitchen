package com.github.wallev.farmsoulkitchen.init;

import com.github.wallev.farmsoulkitchen.compat.cloth.ClothCompat;
import com.github.wallev.farmsoulkitchen.compat.jade.JadeCompat;
import com.github.wallev.farmsoulkitchen.compat.patchouli.PatchouliCompat;
import com.github.wallev.farmsoulkitchen.compat.top.TopCompat;
import com.github.wallev.farmsoulkitchen.foundation.utility.Mods;
import com.github.tartaricacid.touhoulittlemaid.client.gui.mod.ClothConfigScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CompatRegistry {
    @SubscribeEvent
    public static void onEnqueue(final InterModEnqueueEvent event) {
        event.enqueueWork(() -> checkModLoad(Mods.CLOTH_CONFIG, ClothCompat::init));
        event.enqueueWork(() -> checkModLoad(Mods.PATCHOULI, PatchouliCompat::init));
        event.enqueueWork(() -> checkModLoad(Mods.JADE, JadeCompat::init));
        event.enqueueWork(() -> checkModLoad(Mods.TOP, TopCompat::init));
        event.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                ClothConfigScreen.registerNoClothConfigPage();
            }
        });
    }

    private static void checkModLoad(Mods mod, Runnable runnable) {
        if (mod.isLoaded()) {
            runnable.run();
        }
    }
}
