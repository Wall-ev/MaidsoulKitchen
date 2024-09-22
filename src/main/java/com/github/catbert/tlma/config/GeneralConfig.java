package com.github.catbert.tlma.config;

import com.github.catbert.tlma.config.subconfig.RegisterConfig;
import com.github.catbert.tlma.config.subconfig.RenderConfig;
import com.github.catbert.tlma.config.subconfig.TaskConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class GeneralConfig {
    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        RegisterConfig.init(builder);
        TaskConfig.init(builder);
        RenderConfig.init(builder);
        return builder.build();
    }
}
