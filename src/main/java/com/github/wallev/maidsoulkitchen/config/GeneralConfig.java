package com.github.wallev.maidsoulkitchen.config;

import com.github.wallev.maidsoulkitchen.config.subconfig.RegisterConfig;
import com.github.wallev.maidsoulkitchen.config.subconfig.TaskConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class GeneralConfig {
    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        TaskConfig.init(builder);
        RegisterConfig.init(builder);
        return builder.build();
    }
}
