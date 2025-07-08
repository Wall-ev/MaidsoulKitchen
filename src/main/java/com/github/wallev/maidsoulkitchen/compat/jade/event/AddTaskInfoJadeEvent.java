package com.github.wallev.maidsoulkitchen.compat.jade.event;

import com.github.tartaricacid.touhoulittlemaid.api.event.AddJadeInfoEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.farm.ICompatFarmTask;
import com.github.wallev.maidsoulkitchen.api.task.farm.ICompatHandlerInfo;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.berryfruit.v1.BerryFruitData;
import com.github.wallev.maidsoulkitchen.task.MaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.task.farm.handler.IFarmHandlerManager;
import com.github.wallev.verhelper.client.chat.VComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class AddTaskInfoJadeEvent {

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void addTaskInfo(AddJadeInfoEvent event) {
        IPluginConfig pluginConfig = event.getPluginConfig();
        EntityMaid maid = event.getMaid();
        ITooltip tooltip = event.getTooltip();

        if (!(maid.getTask() instanceof ICompatFarmTask<?> farmTask)) return;
        if (farmTask.getUid().equals(MaidsoulKitchenTask.FRUIT_FARM.uid)) {
            // todo: sync
            int fruitFarmSearchYOffset = farmTask.getTaskData(maid).searchYOffset();
            tooltip.add(VComponent.translatable("top.maidsoulkitchen.entity_maid.farm.fruit.search_y_offset").append(Component.literal("" + fruitFarmSearchYOffset)));
        }

        boolean first = true;
        BerryFruitData farmData = farmTask.getTaskData(maid);
        for (IFarmHandlerManager<?> handler : farmTask.getHandlerManagers()) {
            ICompatHandlerInfo farmHandler = handler.getFarmHandler();
            ResourceLocation uid = farmHandler.getUid();
            if (!farmData.containRule(uid.toString())) continue;
            MutableComponent translatable = VComponent.translatable("top.maidsoulkitchen.entity_maid.farm.rule");
            if (first) {
                first = false;
                tooltip.add(translatable.append(farmHandler.getName()));
            } else {
                Font font = Minecraft.getInstance().font;
                int time = font.width(translatable) / font.width(" ");
                tooltip.add(Component.literal(" ".repeat(time)).append(farmHandler.getName()));
            }
        }
    }

}
