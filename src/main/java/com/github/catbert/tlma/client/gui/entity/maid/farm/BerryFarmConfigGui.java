package com.github.catbert.tlma.client.gui.entity.maid.farm;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.api.task.v1.farm.ICompatFarm;
import com.github.catbert.tlma.api.task.v1.farm.ICompatFarmHandler;
import com.github.catbert.tlma.api.task.v1.farm.IHandlerInfo;
import com.github.catbert.tlma.client.gui.entity.maid.MaidTaskConfigGui;
import com.github.catbert.tlma.client.gui.widget.button.CFRuleButton;
import com.github.catbert.tlma.client.gui.widget.button.ResultInfo;
import com.github.catbert.tlma.client.gui.widget.button.Zone;
import com.github.catbert.tlma.entity.data.inner.task.BerryData;
import com.github.catbert.tlma.inventory.container.maid.BerryFarmConfigContainer;
import com.github.catbert.tlma.network.NetworkHandler;
import com.github.catbert.tlma.network.message.ActionBerryFarmRuleMessage;
import com.github.catbert.tlma.task.farm.TaskBerryFarm;
import com.github.catbert.tlma.task.farm.handler.v1.IFarmHandlerManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.anti_ad.mc.ipn.api.IPNButton;
import org.anti_ad.mc.ipn.api.IPNGuiHint;
import org.anti_ad.mc.ipn.api.IPNPlayerSideOnly;

import java.util.Arrays;
import java.util.List;

@IPNPlayerSideOnly
@IPNGuiHint(button = IPNButton.SORT, horizontalOffset = -36, bottom = -12)
@IPNGuiHint(button = IPNButton.SORT_COLUMNS, horizontalOffset = -24, bottom = -24)
@IPNGuiHint(button = IPNButton.SORT_ROWS, horizontalOffset = -12, bottom = -36)
@IPNGuiHint(button = IPNButton.SHOW_EDITOR, horizontalOffset = -5)
@IPNGuiHint(button = IPNButton.SETTINGS, horizontalOffset = -5)
public class BerryFarmConfigGui extends MaidTaskConfigGui<BerryFarmConfigContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TLMAddon.MOD_ID, "textures/gui/farm_guide.png");
    protected final Zone scrollDisplay = new Zone(161, 20, 9, 110);
    protected final Zone ruleDisplay = new Zone(6, 20, 152, 110);
    protected final ResultInfo ref = new ResultInfo(4, 1, 152, 24, 0, 5);
    private final int limitSize = ref.row() * ref.col();
    private List<ICompatFarmHandler> handlers;
    private BerryData farmTaskInfo;

    public BerryFarmConfigGui(BerryFarmConfigContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, screenContainer.getMaid().getTask().getName().append(Component.translatable("gui.touhou_little_maid_addon.farm_config_screen.title")));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initAdditionData() {
        super.initAdditionData();
        this.handlers = (List<ICompatFarmHandler>) Arrays.stream(((ICompatFarm<?, ?>) task).getManagerHandlerValues()).map(IFarmHandlerManager::getFarmHandler).toList();
        this.farmTaskInfo = ((TaskBerryFarm) task).getTaskData(maid);
    }

    @Override
    protected void initAdditionWidgets() {
        super.initAdditionWidgets();
        this.addRuleButton();
        this.addScrollButton();
    }

    @Override
    protected void renderAddition(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderAddition(graphics, mouseX, mouseY, partialTicks);
        this.drawScrollInfoBar(graphics);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        // 176, 137
        boolean isCookSettingMainZone = mouseX >= visualZone.startX() && mouseY >= visualZone.startY() && mouseX < visualZone.startX() + visualZone.width() && mouseY < visualZone.startY() + visualZone.height();
        if (delta != 0 && isCookSettingMainZone) {
            // 向上滚
            if (delta > 0 && solIndex > 0) {
                solIndex--;
                this.init();
                return true;
            }
            // 向下滚
            if (delta < 0 && solIndex < (this.handlers.size() - 1) / limitSize) {
                solIndex++;
                this.init();
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    private void addRuleButton() {
        int startX = visualZone.startX() + ruleDisplay.startX();
        int startY = visualZone.startY() + ruleDisplay.startY();
        int index = solIndex * limitSize;

        for (int i = index; i < Math.min(handlers.size(), index + limitSize); i++) {
            ICompatFarmHandler handler = handlers.get(i);
            if (!handler.canLoad()) continue;
            String handlerUid = ((IHandlerInfo) handler).getUid().toString();
            boolean contains = farmTaskInfo.rules().contains(handlerUid);
            CFRuleButton cfRuleButton = new CFRuleButton((IHandlerInfo) handler, handler, contains, startX, startY) {
                @Override
                public void onClick(double pMouseX, double pMouseY) {
                    this.isSelected = !this.isSelected;
                    farmTaskInfo.addOrRemoveRule(this.handlerInfo.getUid().toString());
                    NetworkHandler.sendToServer(new ActionBerryFarmRuleMessage(maid.getId(), ((TaskBerryFarm) task).getCookDataKey().getKey(), this.handlerInfo.getUid().toString()));
                }
            };
            this.addRenderableWidget(cfRuleButton);
            startY += ref.colHeight() + ref.colSpacing();
        }
    }

    // 161, 25 189, 74
    private void addScrollButton() {
        int startX = visualZone.startX() + scrollDisplay.startX();
        int startY = visualZone.startY() + scrollDisplay.startY();
        ImageButton upButton = new ImageButton(startX, startY, 9, 7, 228, 10, 14, TEXTURE, b -> {
            if (this.solIndex > 0) {
                this.solIndex--;
                this.init();
            }
        });
        Button downButton = new ImageButton(startX, startY + 8 + 1 + 95, 9, 7, 237, 10, 14, TEXTURE, b -> {
            if (this.solIndex < (this.handlers.size() - 1) / limitSize) {
                this.solIndex++;
                this.init();
            }
        });
        this.addRenderableWidget(upButton);
        this.addRenderableWidget(downButton);
    }

    private void drawScrollInfoBar(GuiGraphics graphics) {
        int startX = visualZone.startX() + scrollDisplay.startX();
        int startY = visualZone.startY() + scrollDisplay.startY();
        graphics.blit(TEXTURE, startX, startY + 8, 247, 8, 9, 95);
        drawScrollIndicator(graphics, startX + 1, startY + 8 + 1);
    }

    private void drawScrollIndicator(GuiGraphics graphics, int startX, int startY) {
        if ((this.handlers.size() - 1) / limitSize >= 1) {
            graphics.blit(TEXTURE, startX, startY + (int) ((95 - 12) * getCurrentScroll()), 228, 0, 7, 9);
        } else {
            graphics.blit(TEXTURE, startX, startY, 235, 0, 7, 9);
        }
    }

    private float getCurrentScroll() {
        return Mth.clamp((float) (solIndex * (1.0 / ((this.handlers.size() - 1) / limitSize))), 0, 1);
    }
}
