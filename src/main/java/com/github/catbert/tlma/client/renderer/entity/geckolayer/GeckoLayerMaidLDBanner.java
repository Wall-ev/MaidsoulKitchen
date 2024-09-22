package com.github.catbert.tlma.client.renderer.entity.geckolayer;

import com.github.catbert.tlma.config.subconfig.RenderConfig;
import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBannerModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import de.cristelknight.doapi.common.item.StandardItem;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;

import static com.github.catbert.tlma.TLMAddon.LOGGER;

public class GeckoLayerMaidLDBanner<T extends Mob> extends GeoLayerRenderer<T, GeckoEntityMaidRenderer<T>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_banner.png");
    private final MaidBannerModel bannerModel;

    public GeckoLayerMaidLDBanner(GeckoEntityMaidRenderer renderer, EntityModelSet modelSet) {
        super(renderer);
        this.bannerModel = new MaidBannerModel(modelSet.bakeLayer(MaidBannerModel.LAYER));
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        IMaid maid = IMaid.convert(entity);
        if (maid == null) {
            return;
        }

        if (!RenderConfig.LD_BANNER_RENDER_ENABLED.get()) return;

        Item item = maid.getBackpackShowItem().getItem();
        if (item instanceof StandardItem) {
            if (!this.entityRenderer.getAnimatableEntity(entity).getMaidInfo().isShowBackpack() || !InGameMaidConfig.INSTANCE.isShowBackItem() || entity.isSleeping() || entity.isInvisible()) {
                return;
            }

            AnimatedGeoModel geoModel = this.entityRenderer.getAnimatableEntity(entity).getCurrentModel();
            if (geoModel != null && !geoModel.backpackBones().isEmpty()) {
                matrixStack.pushPose();
                RenderUtils.prepMatrixForLocator(matrixStack, geoModel.backpackBones());
                matrixStack.translate(0, 0.75, 0.3);
                matrixStack.scale(0.65F, -0.65F, -0.65F);
                matrixStack.mulPose(Axis.YN.rotationDegrees(180));
                matrixStack.mulPose(Axis.XN.rotationDegrees(5));
                VertexConsumer buffer = bufferIn.getBuffer(RenderType.entitySolid(TEXTURE));
                // 渲染木棍
                this.bannerModel.renderToBuffer(matrixStack, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                // 渲染旗帜
                ResourceLocation location = StandardItem.getLocationOrNull(item);
                if (location == null) {
                    LOGGER.error("ResourceLocation for StandardBlock texture is null! At: " + item);
                } else {
                    VertexConsumer vc = bufferIn.getBuffer(RenderType.entitySolid(location));
                    this.bannerModel.getBanner().render(matrixStack, vc, packedLightIn, OverlayTexture.NO_OVERLAY);
                }
                matrixStack.popPose();
            }
        }
    }
}
