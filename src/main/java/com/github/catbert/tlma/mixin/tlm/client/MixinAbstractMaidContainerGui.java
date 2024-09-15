package com.github.catbert.tlma.mixin.tlm.client;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.client.gui.entity.maid.IAbstractMaidContainer;
import com.github.catbert.tlma.client.gui.entity.maid.IAbstractMaidContainerGui;
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

@Mixin(value = AbstractMaidContainerGui.class, remap = false)
public abstract class MixinAbstractMaidContainerGui<T extends AbstractMaidContainer> extends AbstractContainerScreen<T> implements IAbstractMaidContainerGui {
    private static final ResourceLocation RIGHT_SIDE = new ResourceLocation(TLMAddon.MOD_ID, "textures/gui/maid_gui_right_side.png");
    @Shadow(remap = false)
    @Final
    @Nullable
    private EntityMaid maid;

    public MixinAbstractMaidContainerGui(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.taskListOpen = ((IAbstractMaidContainer)pMenu).getTaskListOpen();
        TASK_PAGE = ((IAbstractMaidContainer)pMenu).getTaskPage();
    }

    // todo
    // priority: low
    // 将gui的TASK_PAGE、taskListOpen改为使用container的属性，用来在打开Gui的时候设置gui的部分属性
    @Inject(at = @At("TAIL"), method = "<init>")
    private void init1$tlma(T screenContainer, Inventory inv, Component titleIn, CallbackInfo ci) {
        this.taskListOpen = ((IAbstractMaidContainer)screenContainer).getTaskListOpen();
        TASK_PAGE = ((IAbstractMaidContainer)screenContainer).getTaskPage();
    }

    @Shadow
    protected abstract void init();

    @Shadow private static int TASK_PAGE ;

    @Shadow private boolean taskListOpen;

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

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        return super.mouseScrolled(pMouseX, pMouseY, pDelta);
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
        graphics.blit(RIGHT_SIDE, leftPos + 251 + 5, topPos + 28 + 9, 235, 107, 21, 99);
    }

    @Override
    public void setTaskListOpen(boolean taskListOpen) {
        this.taskListOpen = taskListOpen;
    }

    @Override
    public void setTaskPage(int taskPage) {
        TASK_PAGE = taskPage;
    }

    @Override
    public int getTaskPage() {
        return TASK_PAGE;
    }
}
