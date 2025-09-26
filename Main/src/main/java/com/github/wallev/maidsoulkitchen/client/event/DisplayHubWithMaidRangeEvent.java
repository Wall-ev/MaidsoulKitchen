package com.github.wallev.maidsoulkitchen.client.event;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.RenderHelper;
import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.item.ItemCulinaryHub;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.TimeUnit;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = MaidsoulKitchen.MOD_ID, value = Dist.CLIENT)
public class DisplayHubWithMaidRangeEvent {
    private static final Cache<Integer, Integer> CACHE = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build();

    @SubscribeEvent
    public static void onRender(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || mc.level == null) {
                return;
            }
            Vec3 camera = event.getCamera().getPosition().reverse();
            PoseStack poseStack = event.getPoseStack();
            for (int id : CACHE.asMap().keySet()) {
                Entity entity = mc.level.getEntity(id);
                if (!(entity instanceof EntityMaid maid)) {
                    return;
                }

                renderPos(camera, poseStack, mc, maid);
            }
        }
    }

    private static void renderPos(Vec3 camera, PoseStack poseStack, Minecraft mc, EntityMaid maid) {
        poseStack.pushPose();
        poseStack.translate(0, 1, 0);

        BlockPos restrictPos = ICookTask.getSearchPos(maid);
        Vec3 restrictCam = camera.add(restrictPos.getX() + 0.5, restrictPos.getY() + 0.5, restrictPos.getZ() + 0.5);

        Vec3 maidPos = camera.add(maid.position());
        RenderHelper.renderLine(poseStack, mc.renderBuffers().bufferSource().getBuffer(RenderType.LINES), restrictCam, maidPos, 1.0f, 0.2f, 0.2f);

        AABB aabb = maid.getBoundingBox().move(0, -1, 0).move(camera);
        DebugRenderer.renderFilledBox(poseStack, mc.renderBuffers().bufferSource(), aabb, 0.8F, 0.8F, 0.2F, 0.75F);

        {
            Vec3 restrictCenter = restrictPos.getCenter();
            Vec3 centerPos = camera.add(restrictCenter.x() + 0.5, restrictCenter.y() + 0.5, restrictCenter.z() + 0.5);
            double radius = MaidConfig.MAID_WORK_RANGE.get() * ItemCulinaryHub.WORK_RANGE + 0.1;
            VertexConsumer buffer = mc.renderBuffers().bufferSource().getBuffer(RenderType.LINES);
            RenderHelper.renderCylinder(poseStack, buffer, centerPos, radius, 16, 1.0F, 0, 0);

            Vec3 textPos = new Vec3(restrictCenter.x() + 0.5, restrictCenter.y() + 2, restrictCenter.z() + 0.5);
            String text = I18n.get("message.touhou_little_maid.kappa_compass.work_area");
            RenderHelper.renderFloatingText(poseStack, text, textPos.x, textPos.y, textPos.z, 0xff1111, 0.15f, true, -5, false);
            RenderHelper.renderFloatingText(poseStack, "▼", textPos.x, textPos.y, textPos.z, 0xff1111, 0.15f, true, 5, false);
        }

        poseStack.popPose();
    }

    public static void add(int id) {
        CACHE.put(id, 0);
    }
}
