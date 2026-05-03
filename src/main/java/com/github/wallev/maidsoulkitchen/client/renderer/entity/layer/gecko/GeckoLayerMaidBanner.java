package com.github.wallev.maidsoulkitchen.client.renderer.entity.layer.gecko;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntityRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.ILocationModel;
import com.github.wallev.maidsoulkitchen.client.renderer.entity.layer.banner.IBannerRenderer;
import com.github.wallev.maidsoulkitchen.client.renderer.entity.layer.banner.LayerRendererManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader.MAID_BANNER;

@OnlyIn(Dist.CLIENT)
public class GeckoLayerMaidBanner<T extends Mob, R extends IGeoEntityRenderer<T>> extends GeoLayerRenderer<T, R> {
    private static final ResourceLocation TEXTURE = IBannerRenderer.TEXTURE;
    private final SimpleBedrockModel<EntityMaid> bannerModel;
    private final EntityModelSet modelSet;
    private final List<IBannerRenderer> renders;

    public GeckoLayerMaidBanner(R renderer, EntityModelSet modelSet) {
        super(renderer);
        this.modelSet = modelSet;
        this.bannerModel = Objects.requireNonNull(BedrockModelLoader.getModel(MAID_BANNER));
        this.renders = LayerRendererManager.getBannerRenderers();
    }

    @Override
    public GeoLayerRenderer<T, R> copy(R entityRendererIn) {
        return new com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer.GeckoLayerMaidBanner<>(entityRendererIn, modelSet);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        EntityMaid maid = IMaid.convertToMaid(entity);
        if (maid == null) {
            return;
        }
        if (!maid.getConfigManager().isShowBackItem() || entity.isSleeping() || entity.isInvisible()) {
            return;
        }
        if (!getGeoEntity(entity).getMaidInfo().isShowBackpack()) {
            return;
        }
        ILocationModel geoModel = getLocationModel(entity);
        if (geoModel == null || geoModel.backpackBones().isEmpty()) {
            return;
        }

        ItemStack stack = maid.getBackpackShowItem();
        if (stack.isEmpty()) {
            return;
        }
        for (IBannerRenderer render : renders) {
            boolean renderResult = render.geckoRender(stack, bannerModel, geoModel, poseStack, bufferIn, packedLight, maid, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            if (renderResult) {
                break;
            }
        }
    }
}