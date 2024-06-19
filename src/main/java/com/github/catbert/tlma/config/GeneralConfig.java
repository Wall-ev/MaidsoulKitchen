package com.github.catbert.tlma.config;

import com.github.catbert.tlma.config.subconfig.RenderConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class GeneralConfig {
    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
//        TaskConfig.init(builder);
        RenderConfig.init(builder);
        return builder.build();
    }
}
