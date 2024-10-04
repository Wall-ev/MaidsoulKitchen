package com.github.catbert.tlma.compat.jade;

import com.github.catbert.tlma.compat.jade.event.AddTaskInfoJadeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class JadeCompat {
    public static void init() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            MinecraftForge.EVENT_BUS.register(new AddTaskInfoJadeEvent());
        }
    }
}
