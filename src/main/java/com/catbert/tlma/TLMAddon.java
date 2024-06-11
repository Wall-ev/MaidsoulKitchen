package com.catbert.tlma;

import com.catbert.tlma.config.GeneralConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TLMAddon.MOD_ID)
public class TLMAddon {
    public static final String MOD_ID = "touhou_little_maid_addon";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public TLMAddon() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GeneralConfig.init());
    }
}
