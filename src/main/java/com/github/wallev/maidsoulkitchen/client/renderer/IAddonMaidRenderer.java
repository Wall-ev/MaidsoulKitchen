package com.github.wallev.maidsoulkitchen.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;

public interface IAddonMaidRenderer {

    default void initRenderer(EntityRendererProvider.Context manager){
//        addArmorRenderer(manager);
//        addCuriosRenderer(manager);
        addDoApiBannerRenderer(manager);
//        addSupplementariesBannerRenderer(manager);
    }

    void addArmorRenderer(EntityRendererProvider.Context manager);
    // just renderer,not apply effect.
    void addCuriosRenderer(EntityRendererProvider.Context manager);
    void addDoApiBannerRenderer(EntityRendererProvider.Context manager);
    void addSupplementariesBannerRenderer(EntityRendererProvider.Context manager);

}
