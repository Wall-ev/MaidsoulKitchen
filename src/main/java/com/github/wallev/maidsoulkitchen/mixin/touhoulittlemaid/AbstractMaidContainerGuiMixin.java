package com.github.wallev.maidsoulkitchen.mixin.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack.IBackpackContainerScreen;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.RefreshMaidBrainMessage;
import com.github.wallev.maidsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.init.MkItems;
import com.github.wallev.maidsoulkitchen.util.RenderHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(value = AbstractMaidContainerGui.class, remap = false)
public abstract class AbstractMaidContainerGuiMixin<T extends AbstractMaidContainer> extends AbstractContainerScreen<T> {

    @Shadow
    @Final
    protected EntityMaid maid;

    public AbstractMaidContainerGuiMixin(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void onClose() {
        if (((AbstractMaidContainerGui<?>) (Object) (this)) instanceof IBackpackContainerScreen && this.maid.getTask() instanceof ICookTask<?, ?>) {
            if (this.maid != null) {
                NetworkHandler.CHANNEL.sendToServer(new RefreshMaidBrainMessage(maid.getId()));
            }
        }
        super.onClose();
    }

    @Inject(at = @At("TAIL"), method = "renderLabels")
    private void tlmk$renderHubSlotHighlight(GuiGraphics graphics, int x, int y, CallbackInfo ci) {
        if (this.menu.getCarried().is(MkItems.CULINARY_HUB.get()) && this.menu.slots.size() >= 55) {
            final int hubSlotIndex = 55;
            final int color = new Color(44, 255, 44, 96).getRGB();
            Slot hubSlot = this.getMenu().getSlot(hubSlotIndex);
            renderSlotHighlight(graphics, hubSlot.x, hubSlot.y, 0, color);
        }
    }
}
