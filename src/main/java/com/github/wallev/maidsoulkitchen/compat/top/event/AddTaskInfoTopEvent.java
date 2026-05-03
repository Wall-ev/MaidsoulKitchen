package com.github.wallev.maidsoulkitchen.compat.top.event;

import com.github.tartaricacid.touhoulittlemaid.api.event.AddTopInfoEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AddTaskInfoTopEvent {

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void addTaskInfo(AddTopInfoEvent event) {
        IProbeInfo probeInfo = event.getProbeInfo();
        IProbeHitEntityData hitEntityData = event.getHitEntityData();
        ProbeMode probeMode = event.getProbeMode();
        EntityMaid maid = event.getMaid();
//
//        if (!(maid.getTask() instanceof ICompatFarmTask<?> farmTask)) return;
//        if (farmTask.getUid().equals(MaidsoulKitchenTask.FRUIT_FARM.uid)) {
//            // todo: sync
//            int fruitFarmSearchYOffset = farmTask.getTaskData(maid).searchYOffset();
//            probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
//                    .text(VComponent.translatable("top.maidsoulkitchen.entity_maid.farm.fruit.search_y_offset")
//                            .append(Component.literal("" + fruitFarmSearchYOffset)));
//        }
//
//        boolean first = true;
//        BerryFruitData farmData = farmTask.getTaskData(maid);
//
//        for (IFarmHandlerManager<?> handler : farmTask.getHandlerManagers()) {
//            ICompatHandlerInfo farmHandler = handler.getFarmHandler();
//            ResourceLocation uid = farmHandler.getUid();
//            if (!farmData.containRule(uid.toString())) continue;
//            MutableComponent translatable = VComponent.translatable("top.maidsoulkitchen.entity_maid.farm.rule");
//            if (first) {
//                first = false;
//                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
//                        .text(translatable.append(farmHandler.getName()));
//            } else {
//                Font font = Minecraft.getInstance().font;
//                int time = font.width(translatable) / font.width(" ");
//                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
//                        .text(Component.literal(" ".repeat(time)).append(farmHandler.getName()));
//            }
//        }
    }

}
