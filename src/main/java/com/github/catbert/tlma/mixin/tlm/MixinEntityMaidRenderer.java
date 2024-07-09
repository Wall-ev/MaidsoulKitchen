package com.github.catbert.tlma.mixin.tlm;

import com.github.catbert.tlma.api.IAddonMaidRenderer;
import com.github.catbert.tlma.client.renderer.entity.layer.LayerMaidLDBanner;
import com.github.catbert.tlma.foundation.utility.Mods;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityMaidRenderer.class, remap = false)
public abstract class MixinEntityMaidRenderer extends MobRenderer<Mob, BedrockModel<Mob>> implements IAddonMaidRenderer {
    public MixinEntityMaidRenderer(EntityRendererProvider.Context pContext, BedrockModel<Mob> pModel, float pShadowRadius) {
        super(pContext, pModel, pShadowRadius);
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    public void init(EntityRendererProvider.Context manager, CallbackInfo ci) {
        initRenderer(manager);
    }

    public void addDoApiBannerRenderer(EntityRendererProvider.Context manager) {
        if (!Mods.DAPI.isLoaded) return;
        this.addLayer(new LayerMaidLDBanner((EntityMaidRenderer) (Object) this, manager.getModelSet()));
    }
}
