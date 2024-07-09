package com.github.catbert.tlma.client.renderer.entity.layer;

import com.github.catbert.tlma.config.subconfig.RenderConfig;
import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBannerModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import de.cristelknight.doapi.common.item.StandardItem;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;

import static com.github.catbert.tlma.TLMAddon.LOGGER;

public class LayerMaidLDBanner extends RenderLayer<Mob, BedrockModel<Mob>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_banner.png");
    private final EntityMaidRenderer renderer;
    private final MaidBannerModel bannerModel;

    public LayerMaidLDBanner(EntityMaidRenderer renderer, EntityModelSet modelSet) {
        super(renderer);
        this.renderer = renderer;
        this.bannerModel = new MaidBannerModel(modelSet.bakeLayer(MaidBannerModel.LAYER));
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn, Mob mob, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        IMaid maid = IMaid.convert(mob);
        if (maid == null) {
            return;
        }

        if (!(RenderConfig.LD_BANNER_RENDER_ENABLED.get())) return;

        Item item = maid.getBackpackShowItem().getItem();
        if (item instanceof StandardItem) {
            if (!renderer.getMainInfo().isShowBackpack() || !InGameMaidConfig.INSTANCE.isShowBackItem() || mob.isSleeping() || mob.isInvisible()) {
                return;
            }
            matrixStack.pushPose();
            matrixStack.translate(0.0, 0.5, 0.025);
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            matrixStack.mulPose(Axis.XN.rotationDegrees(5.0F));
            VertexConsumer buffer = bufferIn.getBuffer(RenderType.entityTranslucent(TEXTURE));
            // 渲染木棍
            bannerModel.renderToBuffer(matrixStack, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            // 渲染旗帜
            ResourceLocation location = StandardItem.getLocationOrNull(item);
            if (location == null) {
                LOGGER.error("ResourceLocation for StandardBlock texture is null! At: " + item);
            } else {
                VertexConsumer vc = bufferIn.getBuffer(RenderType.entitySolid(location));
                bannerModel.getBanner().render(matrixStack, vc, packedLightIn, OverlayTexture.NO_OVERLAY);
            }
            matrixStack.popPose();
        }

    }
}
