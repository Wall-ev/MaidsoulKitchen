package com.github.catbert.tlma.client.gui.widget.button;

import com.github.catbert.tlma.entity.passive.CookTaskData;
import net.minecraft.network.chat.Component;

import java.util.Collections;
import java.util.List;

public class TypeButton extends NormalTooltipButton {
    private final CookTaskData.TaskRule taskRule;

    public TypeButton(int pX, int pY, int pWidth, int pHeight, CookTaskData.TaskRule taskRule, OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, Component.empty(), Collections.emptyList(), pOnPress);
        this.taskRule = taskRule;
    }

    @Override
    public Component getMessage() {
        return Component.translatable(String.format("gui.touhou_little_maid_addon.btn.cook_guide.type.%s", this.taskRule.getMode().getUid()));
    }

    @Override
    public List<Component> getTooltips() {
        return List.of(Component.translatable(String.format("gui.touhou_little_maid_addon.btn.cook_guide.type.%s.desc", this.taskRule.getMode().getUid())));
    }
}
