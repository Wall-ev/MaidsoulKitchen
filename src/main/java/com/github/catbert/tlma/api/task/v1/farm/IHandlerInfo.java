package com.github.catbert.tlma.api.task.v1.farm;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public interface IHandlerInfo {

    ItemStack getIcon();

    ResourceLocation getUid();

    default Optional<TooltipComponent> getCustomTooltip() {
        return Optional.empty();
    }

    //todo
    //默认读取模组tab的翻译名,其次为本模组的翻译名
    default MutableComponent getName() {
        return Component.translatable(String.format("rule.berry.%s.%s", getUid().getNamespace(), getUid().getPath()));
    }

    default List<Component> getDesc() {
        String key = String.format("rule.berry.%s.%s.desc", getUid().getNamespace(), getUid().getPath());
        return Lists.newArrayList(Component.translatable(key));
    }

}
