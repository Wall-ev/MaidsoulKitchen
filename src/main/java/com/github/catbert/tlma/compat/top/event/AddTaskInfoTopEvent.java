package com.github.catbert.tlma.compat.top.event;

import com.github.catbert.tlma.api.task.v1.farm.ICompatFarm;
import com.github.catbert.tlma.api.task.v1.farm.IHandlerInfo;
import com.github.catbert.tlma.entity.data.inner.task.FarmData;
import com.github.catbert.tlma.entity.data.inner.task.FruitData;
import com.github.catbert.tlma.task.farm.TaskFruitFarm;
import com.github.catbert.tlma.task.farm.handler.v1.IFarmHandlerManager;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.api.event.AddTopInfoEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class AddTaskInfoTopEvent {

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void addTaskInfo(AddTopInfoEvent event) {
        IProbeInfo probeInfo = event.getProbeInfo();
        IProbeHitEntityData hitEntityData = event.getHitEntityData();
        ProbeMode probeMode = event.getProbeMode();
        EntityMaid maid = event.getMaid();

        if (!(maid.getTask() instanceof ICompatFarm<?, ?> farmTask)) return;
        if (farmTask.getUid().equals(TaskFruitFarm.UID)) {
            // todo: sync
            FruitData fruitData = maid.getOrCreateData(((TaskFruitFarm) farmTask).getCookDataKey(), new FruitData());
            int fruitFarmSearchYOffset = fruitData.searchYOffset();
            probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                    .text(Component.translatable("top.touhou_little_maid_addon.entity_maid.farm.fruit.search_y_offset")
                            .append(Component.literal("" + fruitFarmSearchYOffset)));
        }

        boolean first = true;
        FarmData farmData = farmTask.getTaskData(maid);
        List<String> farmTaskRulesList = farmData.rules();

        for (IFarmHandlerManager<?> handler : farmTask.getManagerHandlerValues()) {
            IHandlerInfo farmHandler = handler.getFarmHandler();
            ResourceLocation uid = farmHandler.getUid();
            if (!farmTaskRulesList.contains(uid.toString())) continue;
            MutableComponent translatable = Component.translatable("top.touhou_little_maid_addon.entity_maid.farm.rule");
            if (first) {
                first = false;
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                        .text(translatable.append(farmHandler.getName()));
            } else {
                Font font = Minecraft.getInstance().font;
                int time = font.width(translatable) / font.width(" ");
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                        .text(Component.literal(" ".repeat(time)).append(farmHandler.getName()));
            }
        }
    }

}
