package com.github.wallev.maidsoulkitchen.client.renderer.entity.layer.bedrock;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.client.renderer.entity.layer.banner.IBannerRenderer;
import com.github.wallev.maidsoulkitchen.client.renderer.entity.layer.banner.LayerRendererManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader.MAID_BANNER;

@OnlyIn(Dist.CLIENT)
public class LayerMaidBanner extends RenderLayer<Mob, BedrockModel<Mob>> {
    public static final ResourceLocation TEXTURE = IBannerRenderer.TEXTURE;
    private final EntityMaidRenderer renderer;
    private final SimpleBedrockModel<EntityMaid> bannerModel;
    private final List<IBannerRenderer> renders;

    public LayerMaidBanner(EntityMaidRenderer renderer, EntityModelSet modelSet) {
        super(renderer);
        this.renderer = renderer;
        this.bannerModel = Objects.requireNonNull(BedrockModelLoader.getModel(MAID_BANNER));
        this.renders = LayerRendererManager.getBannerRenderers();
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn, Mob mob, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        EntityMaid maid = IMaid.convertToMaid(mob);
        if (maid == null || !maid.getConfigManager().isShowBackItem()) {
            return;
        }
        if (!renderer.getMainInfo().isShowBackpack() || mob.isSleeping() || mob.isInvisible()) {
            return;
        }

        ItemStack stack = maid.getBackpackShowItem();
        if (stack.isEmpty()) {
            return;
        }
        for (IBannerRenderer render : renders) {
            boolean renderResult = render.bedrockRender(stack, bannerModel, matrixStack, bufferIn, packedLightIn, maid, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            if (renderResult) {
                break;
            }
        }
    }
}