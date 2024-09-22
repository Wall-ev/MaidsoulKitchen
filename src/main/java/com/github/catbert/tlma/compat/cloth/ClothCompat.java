package com.github.catbert.tlma.compat.cloth;

import com.github.catbert.tlma.compat.cloth.event.AddClothConfigEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class ClothCompat {
    public static void init() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            MenuIntegration.registerModsPage();
            MinecraftForge.EVENT_BUS.register(new AddClothConfigEvent());
        }
    }
}
