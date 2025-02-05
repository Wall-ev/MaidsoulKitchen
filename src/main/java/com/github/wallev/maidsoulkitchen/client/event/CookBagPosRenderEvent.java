package com.github.wallev.maidsoulkitchen.client.event;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.init.MkItems;
import com.github.wallev.maidsoulkitchen.inventory.container.item.BagType;
import com.github.wallev.maidsoulkitchen.item.ItemCulinaryHub;
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
@Mod.EventBusSubscriber(modid = MaidsoulKitchen.MOD_ID, value = Dist.CLIENT)
public final class CookBagPosRenderEvent {
    @SubscribeEvent
    public static void onRender(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) {
                return;
            }
            ItemStack mainStack = mc.player.getMainHandItem();
            if (!mainStack.is(MkItems.CULINARY_HUB.get())) {
                return;
            }
            for (BagType value : BagType.values()) {
                BagType.ColorA color = value.color;
                for (BlockPos pos : ItemCulinaryHub.getBindModePoses(mainStack, value.name)) {
                    Vec3 position = event.getCamera().getPosition().reverse();
                    AABB aabb = new AABB(pos).move(position);
                    VertexConsumer buffer = mc.renderBuffers().bufferSource().getBuffer(RenderType.LINES);
                    LevelRenderer.renderLineBox(event.getPoseStack(), buffer, aabb, color.getRed(), color.getGreen(), color.getBlue(), 1.0F);
                }
            }
        }
    }
}
