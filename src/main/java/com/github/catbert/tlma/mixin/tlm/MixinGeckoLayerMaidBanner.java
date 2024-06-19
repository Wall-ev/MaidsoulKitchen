package com.github.catbert.tlma.mixin.tlm;

import com.github.catbert.tlma.config.subconfig.RenderConfig;
import com.github.catbert.tlma.foundation.utility.Mods;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBannerModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer.GeckoLayerMaidBanner;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import de.cristelknight.doapi.common.item.StandardItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.catbert.tlma.TLMAddon.LOGGER;

@Mixin(value = GeckoLayerMaidBanner.class, remap = false)
public abstract class MixinGeckoLayerMaidBanner<T extends Mob & IAnimatable> extends GeoLayerRenderer<T> {
    @Shadow
    @Final
    private static ResourceLocation TEXTURE;
    @Shadow
    @Final
    private GeckoEntityMaidRenderer renderer;
    @Shadow
    @Final
    private MaidBannerModel bannerModel;

    public MixinGeckoLayerMaidBanner(IGeoRenderer<T> entityRendererIn) {
        super(entityRendererIn);
    }

    @Shadow
    protected abstract void translateToBackpack(PoseStack poseStack, GeoModel geoModel);

    @Inject(at = @At("TAIL"), method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/Mob;FFFFFF)V")
    public void render(PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        IMaid maid = IMaid.convert(entity);
        if (maid != null) {
            Item item = maid.getBackpackShowItem().getItem();

            if (Mods.DAPI.isLoaded && RenderConfig.LD_BANNER_RENDER_ENABLED.get() && item instanceof StandardItem) {
                if (!this.renderer.getMainInfo().isShowBackpack() || !InGameMaidConfig.INSTANCE.isShowBackItem() || entity.isSleeping() || entity.isInvisible()) {
                    return;
                }

                GeoModel geoModel = this.entityRenderer.getGeoModel();
                if (geoModel != null && !geoModel.backpackBones.isEmpty()) {
                    matrixStack.pushPose();
                    this.translateToBackpack(matrixStack, geoModel);
                    matrixStack.translate(0.0, -1.5, 0.02);
                    matrixStack.scale(0.65F, 0.65F, 0.65F);
                    matrixStack.mulPose(Axis.XN.rotationDegrees(5.0F));
                    VertexConsumer buffer = bufferIn.getBuffer(RenderType.entityTranslucent(TEXTURE));
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
}
