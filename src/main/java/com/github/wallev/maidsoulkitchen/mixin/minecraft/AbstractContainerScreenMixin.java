package com.github.wallev.maidsoulkitchen.mixin.minecraft;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack.IBackpackContainerScreen;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.RefreshMaidBrainMessage;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;


@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {

    @Inject(method = "onClose", at = @At("HEAD"))
    private void tlmk$refreshEntityMaidBrain(CallbackInfo ci) {
        AbstractContainerScreen<?> containerScreen = (AbstractContainerScreen<?>) (Object) this;
        if (containerScreen instanceof AbstractMaidContainerGui<?> maidContainerGui && maidContainerGui instanceof IBackpackContainerScreen backpackContainerScreen) {
            Optional.ofNullable(maidContainerGui.getMaid()).ifPresent(maid -> {
                NetworkHandler.CHANNEL.sendToServer(new RefreshMaidBrainMessage(maid.getId()));
            });
        }
    }
}
