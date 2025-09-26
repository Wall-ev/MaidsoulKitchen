package com.github.wallev.maidsoulkitchen.compat.patchouli;

import com.github.wallev.maidsoulkitchen.compat.patchouli.event.OpenPatchouliBookEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class PatchouliCompat {
    public static void init() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            MinecraftForge.EVENT_BUS.register(new OpenPatchouliBookEvent());
        }
    }
}
