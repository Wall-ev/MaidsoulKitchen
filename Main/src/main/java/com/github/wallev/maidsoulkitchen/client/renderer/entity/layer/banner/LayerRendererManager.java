package com.github.wallev.maidsoulkitchen.client.renderer.entity.layer.banner;

import com.github.wallev.maidsoulkitchen.client.renderer.entity.layer.banner.bakery.LayerBakeryBannerRender;
import com.github.wallev.maidsoulkitchen.client.renderer.entity.layer.banner.beachparty.LayerBeachpartyBannerRender;
import com.github.wallev.maidsoulkitchen.client.renderer.entity.layer.banner.bloomingnature.LayerBloomingnatureBannerRender;
import com.github.wallev.maidsoulkitchen.client.renderer.entity.layer.banner.brewery.LayerBreweryBannerRender;
import com.github.wallev.maidsoulkitchen.client.renderer.entity.layer.banner.candlelight.LayerCandlelightBannerRender;
import com.github.wallev.maidsoulkitchen.client.renderer.entity.layer.banner.herbalbrews.LayerHerbalbrewsBannerRender;
import com.github.wallev.maidsoulkitchen.client.renderer.entity.layer.banner.meadow.LayerMeadowBannerRender;
import com.github.wallev.maidsoulkitchen.client.renderer.entity.layer.banner.vinery.LayerVineryBannerRender;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.google.common.collect.ImmutableList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class LayerRendererManager {
    private static LayerRendererManager INSTANCE = new LayerRendererManager();

    private List<IBannerRenderer> bannerRenderers;

    private LayerRendererManager() {
        bannerRenderers = new ArrayList<>();
        addBannerRenderer(TaskInfo.LAYER_BAKERY, LayerBakeryBannerRender::new);
        addBannerRenderer(TaskInfo.LAYER_BEACHPARTY, LayerBeachpartyBannerRender::new);
        addBannerRenderer(TaskInfo.LAYER_BLOOMINGNATURE, LayerBloomingnatureBannerRender::new);
        addBannerRenderer(TaskInfo.LAYER_BREWERY, LayerBreweryBannerRender::new);
        addBannerRenderer(TaskInfo.LAYER_CANDLELIGHT, LayerCandlelightBannerRender::new);
        addBannerRenderer(TaskInfo.LAYER_HERBALBREWS, LayerHerbalbrewsBannerRender::new);
        addBannerRenderer(TaskInfo.LAYER_MEADOW, LayerMeadowBannerRender::new);
        addBannerRenderer(TaskInfo.LAYER_VINERY, LayerVineryBannerRender::new);
        bannerRenderers = ImmutableList.copyOf(bannerRenderers);
    }

    public static void init() {
        INSTANCE = new LayerRendererManager();
    }

    private void addBannerRenderer(TaskInfo taskInfo, Supplier<IBannerRenderer> renderer) {
        if (taskInfo.canLoad()) {
            bannerRenderers.add(renderer.get());
        }
    }

    public static List<IBannerRenderer> getBannerRenderers() {
        return INSTANCE.bannerRenderers;
    }
}
