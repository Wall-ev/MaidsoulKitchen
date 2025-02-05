package com.github.wallev.maidsoulkitchen.mixin.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack.IBackpackContainerScreen;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.RefreshMaidBrainMessage;
import com.github.wallev.maidsoulkitchen.api.task.v1.cook.ICookTask;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = AbstractMaidContainerGui.class, remap = false)
public abstract class AbstractMaidContainerGuiMixin<T extends AbstractMaidContainer> extends AbstractContainerScreen<T> {

    @Shadow @Final protected EntityMaid maid;

    public AbstractMaidContainerGuiMixin(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void onClose() {
        if (((AbstractMaidContainerGui<?>)(Object)(this)) instanceof IBackpackContainerScreen && this.maid.getTask() instanceof ICookTask<?,?>) {
            if (this.maid != null) {
                NetworkHandler.CHANNEL.sendToServer(new RefreshMaidBrainMessage(maid.getId()));
            }
        }
        super.onClose();
    }
}
