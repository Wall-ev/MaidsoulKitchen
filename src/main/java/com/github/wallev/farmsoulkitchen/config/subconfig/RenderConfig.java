package com.github.wallev.farmsoulkitchen.config.subconfig;

import net.minecraftforge.common.ForgeConfigSpec;

public class RenderConfig {
    public static ForgeConfigSpec.BooleanValue LD_BANNER_RENDER_ENABLED;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("Render");

        builder.comment("Maid can render LdBanner.");
        LD_BANNER_RENDER_ENABLED = builder.define("LdBannerRenderEnabled", true);

        builder.pop();
    }
}
