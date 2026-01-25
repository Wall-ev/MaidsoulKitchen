package com.github.wallev.maidsoulkitchen;

import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.client.animation.HardcodedAnimationManger;
import com.github.tartaricacid.touhoulittlemaid.client.overlay.MaidTipsOverlay;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.wallev.maidsoulkitchen.client.renderer.entity.layer.banner.LayerRendererManager;
import com.github.wallev.maidsoulkitchen.client.renderer.entity.layer.bedrock.LayerMaidBanner;
import com.github.wallev.maidsoulkitchen.client.renderer.entity.layer.gecko.GeckoLayerMaidBanner;
import com.github.wallev.maidsoulkitchen.util.DevUtil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@LittleMaidExtension
public final class MaidClientPlugin implements ILittleMaid {

    public MaidClientPlugin() {
        if (DevUtil.isDevEnv()) {
            LayerRendererManager.init();
        }
    }

    @Override
    public void addAdditionMaidLayer(EntityMaidRenderer renderer, EntityRendererProvider.Context context) {
        if (!DevUtil.isDevEnv())
            return;

        renderer.addLayer(new LayerMaidBanner(renderer, context.getModelSet()));
    }

    @Override
    public void addAdditionGeckoMaidLayer(GeckoEntityMaidRenderer<? extends Mob> renderer, EntityRendererProvider.Context context) {
        if (!DevUtil.isDevEnv())
            return;

        renderer.addGeoLayerRenderer(new GeckoLayerMaidBanner<>(renderer, context.getModelSet()));
    }

    @Override
    public void addMaidTips(MaidTipsOverlay maidTipsOverlay) {

    }

    @Override
    public void addHardcodeAnimation(HardcodedAnimationManger manger) {
        ILittleMaid.super.addHardcodeAnimation(manger);
    }
}
