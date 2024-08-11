package com.github.catbert.tlma.client.event;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.init.InitItems;
import com.github.catbert.tlma.item.ItemWios;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = TLMAddon.MOD_ID, value = Dist.CLIENT)
public final class WiosRenderEvent {
    @SubscribeEvent
    public static void onRender(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) {
                return;
            }
            ItemStack mainStack = mc.player.getMainHandItem();
            if (mainStack.getItem() != InitItems.WIOS_BAUBLE.get()) {
                return;
            }
            BlockPos pos = ItemWios.getBindingPos(mainStack);
            if (pos == null) {
                return;
            }
            Vec3 position = event.getCamera().getPosition().reverse();
            AABB aabb = new AABB(pos).move(position);
            VertexConsumer buffer = mc.renderBuffers().bufferSource().getBuffer(RenderType.LINES);
            LevelRenderer.renderLineBox(event.getPoseStack(), buffer, aabb, 1.0F, 0, 0, 1.0F);
        }
    }
}
