package com.github.catbert.tlma.mixin.tlm.client;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.client.gui.entity.maid.MaidSideTabs;
import com.github.catbert.tlma.client.gui.widget.button.MaidSideTabButton;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(AbstractMaidContainerGui.class)
public abstract class MixinAbstractMaidContainerGui<T extends AbstractMaidContainer> extends AbstractContainerScreen<T> {

    @Shadow @Final @Nullable private EntityMaid maid;
    private static final ResourceLocation RIGHT_SIDE = new ResourceLocation(TLMAddon.MOD_ID, "textures/gui/maid_gui_right_side.png");


    public MixinAbstractMaidContainerGui(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Inject(at = @At("TAIL"), method = "init")
    private void init$tlma(CallbackInfo ci) {
        if (maid != null) {
            this.addSideTabsButton();
        }
    }

    @SuppressWarnings("unchecked")
    private void addSideTabsButton() {
        MaidSideTabs<T> maidTabs = new MaidSideTabs<>(maid.getId(), leftPos, topPos);
        MaidSideTabButton[] tabs = maidTabs.getTabs((AbstractMaidContainerGui<T>)(Object)this);
        for (MaidSideTabButton button : tabs) {
            this.addRenderableWidget(button);
        }
    }

    @Inject(at = @At("TAIL"), method = "renderBg")
    private void renderBg$tlma(GuiGraphics graphics, float partialTicks, int x, int y, CallbackInfo ci) {
        this.drawSideTabGui(graphics, partialTicks, x, y);
    }

    private void drawSideTabGui(GuiGraphics graphics, float partialTicks, int x, int y){
//        graphics.pose().translate(0, 0, 200);
        graphics.blit(RIGHT_SIDE, leftPos + 251 + 5, topPos + 26 + 7, 235, 107, 21, 74);
    }

}
