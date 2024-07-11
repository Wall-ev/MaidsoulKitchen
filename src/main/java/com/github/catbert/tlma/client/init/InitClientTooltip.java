package com.github.catbert.tlma.client.init;

import com.github.catbert.tlma.client.tooltip.ClientAmountTooltip;
import com.github.catbert.tlma.inventory.tooltip.AmountTooltip;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class InitClientTooltip {
    @SubscribeEvent
    public static void onRegisterClientTooltip(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(AmountTooltip.class, ClientAmountTooltip::new);
    }
}
