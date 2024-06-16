package com.catbert.tlma.config;

import com.catbert.tlma.config.subconfig.RenderConfig;
import com.catbert.tlma.config.subconfig.TaskConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class GeneralConfig {
    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
//        TaskConfig.init(builder);
        RenderConfig.init(builder);
        return builder.build();
    }
}
