package com.github.wallev.maidsoulkitchen.client.renderer.entity.layer.banner.candlelight;

import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.client.renderer.entity.layer.banner.IBannerRenderer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.satisfy.candlelight.core.block.CompletionistBannerBlock;
import org.jetbrains.annotations.Nullable;

@TaskClassAnalyzer(TaskInfo.LAYER_CANDLELIGHT)
@OnlyIn(Dist.CLIENT)
public class LayerCandlelightBannerRender implements IBannerRenderer {

    @Override
    @Nullable
    public ResourceLocation getBannerLocation(ItemStack bannerItem, SimpleBedrockModel<EntityMaid> bannerModel, PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn, EntityMaid maid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        return bannerItem.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof CompletionistBannerBlock bannerBlockItem ? bannerBlockItem.getRenderTexture() : null;
    }
}
