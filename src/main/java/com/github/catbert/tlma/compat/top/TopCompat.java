package com.github.catbert.tlma.compat.top;

import com.github.catbert.tlma.compat.top.event.AddTaskInfoTopEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class TopCompat {
    public static void init() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            MinecraftForge.EVENT_BUS.register(new AddTaskInfoTopEvent());
        }
    }
}
