package com.github.wallev.maidsoulkitchen.client.renderer.entity.layer.banner;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.ILocationModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public interface IBannerRenderer {
    ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/bedrock/entity/maid_banner.png");

    default boolean bedrockRender(ItemStack bannerItem, SimpleBedrockModel<EntityMaid> bannerModel, PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn, EntityMaid maid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ResourceLocation bannerLocation = getBannerLocation(bannerItem, bannerModel, matrixStack, bufferIn, packedLightIn, maid, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        if (bannerLocation != null) {
            matrixStack.pushPose();
            matrixStack.translate(0, 0.5, 0.025);
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            matrixStack.mulPose(Axis.XN.rotationDegrees(5));
            VertexConsumer buffer = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
            bannerModel.renderToBuffer(matrixStack, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            renderBanner(bannerModel.getPart("banner"), matrixStack, bufferIn, packedLightIn, bannerLocation);
            matrixStack.popPose();

            return true;
        }
        return false;
    }

    default boolean geckoRender(ItemStack bannerItem, SimpleBedrockModel<EntityMaid> bannerModel, ILocationModel geoModel, PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn, EntityMaid maid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ResourceLocation bannerLocation = getBannerLocation(bannerItem, bannerModel, matrixStack, bufferIn, packedLightIn, maid, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        if (bannerLocation != null) {
            matrixStack.pushPose();
            RenderUtils.prepMatrixForLocator(matrixStack, geoModel.backpackBones());
            matrixStack.translate(0, 0.75, 0.3);
            matrixStack.scale(0.65F, -0.65F, -0.65F);
            matrixStack.mulPose(Axis.YN.rotationDegrees(180));
            matrixStack.mulPose(Axis.XN.rotationDegrees(5));
            VertexConsumer buffer = bufferIn.getBuffer(RenderType.entitySolid(TEXTURE));
            bannerModel.renderToBuffer(matrixStack, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            renderBanner(bannerModel.getPart("banner"), matrixStack, bufferIn, packedLightIn, bannerLocation);
            matrixStack.popPose();

            return true;
        }
        return false;
    }

    @Nullable
    ResourceLocation getBannerLocation(ItemStack bannerItem, SimpleBedrockModel<EntityMaid> bannerModel, PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn, EntityMaid maid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);

    static void renderBanner(BedrockPart banner, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ResourceLocation location) {
        VertexConsumer vc = bufferSource.getBuffer(RenderType.entitySolid(location));
        banner.render(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY);
    }
}
