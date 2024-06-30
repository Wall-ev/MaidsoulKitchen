package com.github.catbert.tlma.mixin.tlm;

import com.github.catbert.tlma.client.renderer.entity.geckolayer.GeckoLayerMaidLDBanner;
import com.github.catbert.tlma.foundation.utility.Mods;
import com.github.tartaricacid.touhoulittlemaid.client.entity.GeckoMaidEntity;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoReplacedEntityRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.model.AnimatedGeoModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GeckoEntityMaidRenderer.class, remap = false)
public abstract class MixinGeckoEntityMaidRenderer extends GeoReplacedEntityRenderer<GeckoMaidEntity> {

    public MixinGeckoEntityMaidRenderer(EntityRendererProvider.Context renderManager, AnimatedGeoModel<IAnimatable> modelProvider, GeckoMaidEntity animatable) {
        super(renderManager, modelProvider, animatable);
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    public void init(EntityRendererProvider.Context manager, CallbackInfo ci) {
        if (!Mods.DAPI.isLoaded) return;
        this.addLayer(new GeckoLayerMaidLDBanner<>((GeckoEntityMaidRenderer) (Object) this, manager.getModelSet()));
    }
}
