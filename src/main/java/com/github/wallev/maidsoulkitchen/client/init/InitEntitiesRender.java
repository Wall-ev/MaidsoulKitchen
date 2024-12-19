package com.github.wallev.maidsoulkitchen.client.init;

import com.github.wallev.maidsoulkitchen.client.model.backpack.OldBigBackpackModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class InitEntitiesRender {
    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(OldBigBackpackModel.LAYER, OldBigBackpackModel::createBodyLayer);
    }
}
