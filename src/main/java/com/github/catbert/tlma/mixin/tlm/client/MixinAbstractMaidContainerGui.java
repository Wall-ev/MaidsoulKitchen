package com.github.catbert.tlma.mixin.tlm.client;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.client.gui.entity.maid.MaidSideTabs;
import com.github.catbert.tlma.client.gui.widget.button.ITooltipBtn;
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
    private static final ResourceLocation RIGHT_SIDE = new ResourceLocation(TLMAddon.MOD_ID, "textures/gui/maid_gui_right_side.png");
    @Shadow(remap = false)
    @Final
    @Nullable
    private EntityMaid maid;

    public MixinAbstractMaidContainerGui(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Shadow
    protected abstract void init();

    @Inject(at = @At("TAIL"), method = "init")
    private void init$tlma(CallbackInfo ci) {
        if (maid != null) {
            this.addSideTabsButton();
        }
    }

    @Inject(at = @At("TAIL"), method = "renderBg")
    private void renderBg$tlma(GuiGraphics graphics, float partialTicks, int x, int y, CallbackInfo ci) {
        this.drawSideTabGui(graphics, partialTicks, x, y);
    }

    @Inject(at = @At("TAIL"), method = "renderTooltip")
    private void renderTooltip$tlma(GuiGraphics graphics, int x, int y, CallbackInfo ci) {
        renderSideTabButtonInfo(graphics, x, y);
    }

    private void renderSideTabButtonInfo(GuiGraphics graphics, int x, int y) {
        this.renderables.stream().filter(b -> b instanceof ITooltipBtn).forEach(b -> {
            if (((ITooltipBtn) b).isHovered()) {
                ((ITooltipBtn) b).renderTooltip(graphics, getMinecraft(), x, y);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void addSideTabsButton() {
        MaidSideTabs<T> maidTabs = new MaidSideTabs<>(maid.getId(), leftPos, topPos);
        MaidSideTabButton[] tabs = maidTabs.getTabs((AbstractMaidContainerGui<T>) (Object) this);
        for (MaidSideTabButton button : tabs) {
            this.addRenderableWidget(button);
        }
    }

    private void drawSideTabGui(GuiGraphics graphics, float partialTicks, int x, int y) {
        graphics.blit(RIGHT_SIDE, leftPos + 251 + 5, topPos + 28 + 9, 235, 107, 21, 74);
    }
}
