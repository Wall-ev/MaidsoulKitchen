package com.github.wallev.farmsoulkitchen.client.init;

import com.github.wallev.farmsoulkitchen.client.tooltip.CrockPotAmountTooltip;
import com.github.wallev.farmsoulkitchen.client.tooltip.NormalAmountTooltip;
import com.github.wallev.farmsoulkitchen.config.subconfig.RegisterConfig;
import com.github.wallev.farmsoulkitchen.foundation.utility.Mods;
import com.github.wallev.farmsoulkitchen.inventory.tooltip.AmountTooltip;
import com.github.wallev.farmsoulkitchen.inventory.tooltip.CrockPotTooltip;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class InitClientTooltip {
    @SubscribeEvent
    public static void onRegisterClientTooltip(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(AmountTooltip.class, NormalAmountTooltip::new);
        if (Mods.CP.isLoaded()) {
            event.register(CrockPotTooltip.class, CrockPotAmountTooltip::new);
        }
    }
}
