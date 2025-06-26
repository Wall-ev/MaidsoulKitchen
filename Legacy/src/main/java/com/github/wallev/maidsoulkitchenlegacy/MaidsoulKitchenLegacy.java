package com.github.wallev.maidsoulkitchenlegacy;

import com.github.wallev.maidsoulkitchenlegacy.task.cook.LegacyRegister;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.github.wallev.maidsoulkitchenlegacy.MaidsoulKitchenLegacy.MOD_ID;

@Mod(MOD_ID)
public final class MaidsoulKitchenLegacy {
    public static final String MOD_ID = "maidsoulkitchen_legacy";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public MaidsoulKitchenLegacy() {
        LegacyRegister.register();
    }


}
