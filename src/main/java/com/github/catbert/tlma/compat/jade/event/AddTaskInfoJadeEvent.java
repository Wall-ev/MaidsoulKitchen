package com.github.catbert.tlma.compat.jade.event;

import com.github.catbert.tlma.api.task.v1.farm.ICompatFarm;
import com.github.catbert.tlma.api.task.v1.farm.IHandlerInfo;
import com.github.catbert.tlma.task.farm.TaskFruitFarm;
import com.github.catbert.tlma.task.farm.handler.v1.IFarmHandlerManager;
import com.github.catbert.tlma.util.MaidTaskDataUtil;
import com.github.tartaricacid.touhoulittlemaid.api.event.AddJadeInfoEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.List;

public class AddTaskInfoJadeEvent {

    @SubscribeEvent
    public void addTaskInfo(AddJadeInfoEvent event) {
        IPluginConfig pluginConfig = event.getPluginConfig();
        EntityMaid maid = event.getMaid();
        ITooltip tooltip = event.getTooltip();

        if (!(maid.getTask() instanceof ICompatFarm<?> farmTask)) return;
        if (farmTask.getUid().equals(TaskFruitFarm.NAME)) {
            // todo: sync
            int fruitFarmSearchYOffset = MaidTaskDataUtil.getFruitFarmSearchYOffset(maid, farmTask.getUid().toString());
            tooltip.add(Component.translatable("top.touhou_little_maid_addon.entity_maid.farm.fruit.search_y_offset").append(Component.literal("" + fruitFarmSearchYOffset)));
        }

        boolean first = true;
        List<String> farmTaskRulesList = MaidTaskDataUtil.getFarmTaskRulesList(maid, farmTask.getUid().toString());
        for (IFarmHandlerManager<?> handler : farmTask.getManagerHandlerValues()) {
            IHandlerInfo farmHandler = handler.getFarmHandler();
            ResourceLocation uid = farmHandler.getUid();
            if (!farmTaskRulesList.contains(uid.toString())) continue;
            MutableComponent translatable = Component.translatable("top.touhou_little_maid_addon.entity_maid.farm.rule");
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
