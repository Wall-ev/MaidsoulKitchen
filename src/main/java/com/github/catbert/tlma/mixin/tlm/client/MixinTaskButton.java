package com.github.catbert.tlma.mixin.tlm.client;

import com.github.catbert.tlma.client.gui.entity.maid.cook.CookSettingContainerGui;
import com.github.catbert.tlma.client.gui.widget.button.TaskInfoButton;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.TaskButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = TaskButton.class, remap = false)
public abstract class MixinTaskButton extends Button {
    protected MixinTaskButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress, CreateNarration pCreateNarration) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress, pCreateNarration);
    }

    @Shadow
    public abstract IMaidTask getTask();

    @Override
    public void onPress() {
        super.onPress();
        if (Minecraft.getInstance().screen instanceof CookSettingContainerGui cookingSettingContainerGui) {
            IMaidTask task = this.getTask();
            boolean enable = task.isEnable(cookingSettingContainerGui.getMaid());
//            if (enable && task instanceof ICookTask<?,?>) {
            if (enable) {
                cookingSettingContainerGui.renderables.forEach(renderable -> {
                    if (renderable instanceof TaskInfoButton button) {
                        button.setTask(task);
                    }
                });
                cookingSettingContainerGui.refreshInfo(task);
            }
        }
    }
}
