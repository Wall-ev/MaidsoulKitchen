package com.github.catbert.tlma.mixin.tlm.client;

import com.github.catbert.tlma.client.gui.entity.maid.cook.CookSettingContainerGui;
import com.github.catbert.tlma.client.gui.entity.maid.cook.IAddonAbstractMaidContainerGui;
import com.github.catbert.tlma.entity.passive.SideTabIndex;
import com.github.catbert.tlma.network.NetworkHandler;
import com.github.catbert.tlma.network.message.ToggleSideTabMessage;
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
        if (Minecraft.getInstance().screen instanceof CookSettingContainerGui gui) {
            IMaidTask task = this.getTask();
            boolean enable = task.isEnable(gui.getMaid());
//            if (enable && task instanceof ICookTask<?,?>) {
            if (enable) {
                ((IAddonAbstractMaidContainerGui) gui).init(this.getTask());
//                NetworkHandler.CHANNEL.sendToServer(new ToggleSideTabMessage(gui.getMenu().containerId, gui.getMaid().getId(), SideTabIndex.SETTING.getIndex()));

//                NetworkHandler.CHANNEL.sendToServer(new OpeCookingSettingGuiMessage(cookingSettingContainerGui.getMaid().getId()));
            }
        }
    }
}
