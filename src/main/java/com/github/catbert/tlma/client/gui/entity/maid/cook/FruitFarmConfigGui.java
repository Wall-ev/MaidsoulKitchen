package com.github.catbert.tlma.client.gui.entity.maid.cook;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.api.task.v1.farm.ICompatFarm;
import com.github.catbert.tlma.api.task.v1.farm.ICompatFarmHandler;
import com.github.catbert.tlma.api.task.v1.farm.IHandlerInfo;
import com.github.catbert.tlma.client.gui.widget.button.CFRuleButton;
import com.github.catbert.tlma.client.gui.widget.button.ResultInfo;
import com.github.catbert.tlma.client.gui.widget.button.Zone;
import com.github.catbert.tlma.inventory.container.FruitFarmConfigContainer;
import com.github.catbert.tlma.network.NetworkHandler;
import com.github.catbert.tlma.network.message.FarmTaskRuleActionMessage;
import com.github.catbert.tlma.network.message.SetFruitFarmSearchYOffsetMessage;
import com.github.catbert.tlma.task.farm.handler.v1.IFarmHandlerManager;
import com.github.catbert.tlma.util.MaidTaskDataUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.anti_ad.mc.ipn.api.IPNButton;
import org.anti_ad.mc.ipn.api.IPNGuiHint;
import org.anti_ad.mc.ipn.api.IPNPlayerSideOnly;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

@IPNPlayerSideOnly
@IPNGuiHint(button = IPNButton.SORT, horizontalOffset = -36, bottom = -12)
@IPNGuiHint(button = IPNButton.SORT_COLUMNS, horizontalOffset = -24, bottom = -24)
@IPNGuiHint(button = IPNButton.SORT_ROWS, horizontalOffset = -12, bottom = -36)
@IPNGuiHint(button = IPNButton.SHOW_EDITOR, horizontalOffset = -5)
@IPNGuiHint(button = IPNButton.SETTINGS, horizontalOffset = -5)
public class FruitFarmConfigGui extends MaidTaskConfigGui<FruitFarmConfigContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TLMAddon.MOD_ID, "textures/gui/farm_guide.png");
    protected final Zone scrollDisplay = new Zone(161, 47, 9, 110);
    protected final Zone ruleDisplay = new Zone(6, 47, 152, 110);
    protected final ResultInfo ref = new ResultInfo(3, 1, 152, 24, 0, 5);
    private final int limitSize = ref.row() * ref.col();
    private List<ICompatFarmHandler> handlers;
    private CompoundTag farmTaskInfo;
    private int retrieval;

    public FruitFarmConfigGui(FruitFarmConfigContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, screenContainer.getMaid().getTask().getName().append(Component.translatable("gui.touhou_little_maid_addon.farm_config_screen.title")));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initAdditionData() {
        super.initAdditionData();
        this.handlers = (List<ICompatFarmHandler>) Arrays.stream(((ICompatFarm<?>) task).getManagerHandlerValues()).map(IFarmHandlerManager::getFarmHandler).toList();
        this.farmTaskInfo = MaidTaskDataUtil.getFarmTaskInfo(maid, task.getUid().toString());
    }

    @Override
    protected void initAdditionWidgets() {
        super.initAdditionWidgets();
        this.addRetrievalButton();
        this.addRuleButton();
        this.addScrollButton();
    }

    @Override
    protected void initBaseData() {
        super.initBaseData();
        this.retrieval = MaidTaskDataUtil.getFruitFarmSearchYOffset(maid, task.getUid().toString());
    }

    private void addRetrievalButton() {
        int x = font.width(Component.literal("检索高度: " + "--"));
        int startX = visualZone.startX() + 6 + 26 + x;
        int startY = visualZone.startY() + 22 + 2;
        ImageButton addButton = new ImageButton(startX, startY, 17, 18, 80, 238, 0, TEXTURE, b -> {
            if (this.retrieval >= 5) {
                return;
            }
            this.retrieval++;
            NetworkHandler.sendToServer(new SetFruitFarmSearchYOffsetMessage(maid.getId(), task.getUid().toString(), this.retrieval));
        });
        Button downButton = new ImageButton(startX + 17, startY, 17, 18, 80 + 17, 238, 0, TEXTURE, b -> {
            if (this.retrieval <= -5) {
                return;
            }
            this.retrieval--;
            NetworkHandler.sendToServer(new SetFruitFarmSearchYOffsetMessage(maid.getId(), task.getUid().toString(), this.retrieval));
        });
        this.addRenderableWidget(addButton);
        this.addRenderableWidget(downButton);
    }

    @Override
    protected void renderAddition(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderAddition(graphics, mouseX, mouseY, partialTicks);
        this.renderRetrieval(graphics);
        this.drawScrollInfoBar(graphics);
    }

    private void renderRetrieval(GuiGraphics graphics) {
//        MutableComponent literal = Component.literal("Retrieval Height: " + retrieval);
        MutableComponent literal = Component.literal("检索高度: " + retrieval);
        // 暂时先这样... todo
        int width = font.width(literal);
        int x = visualZone.startX() + 6;
        int y = visualZone.startY() + 22;
        graphics.blit(TEXTURE, x, y, 0 ,236, 22, 20);
        // 暂时先这样... todo
        if (retrieval >= 0) {
            int width1 = font.width(Component.literal("-"));
            width += width1;
            for (int i = 0; i < width; i++) {
                graphics.blit(TEXTURE, x + 22 + i, y, 22 ,236, 1, 20);
            }
            graphics.blit(TEXTURE, x + 22 + width, y, 76,236, 8, 20);
        }else {
            for (int i = 0; i < width; i++) {
                graphics.blit(TEXTURE, x + 22 + i, y, 22 ,236, 1, 20);
            }
            graphics.blit(TEXTURE, x + 22 + width, y, 76 ,236, 8, 20);
        }
        graphics.renderItem(task.getIcon(), x + 2, y + 2);
        graphics.drawString(font, literal, x + 22, y + 7, Color.WHITE.getRGB(), false);
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
            boolean contains = MaidTaskDataUtil.getFarmTaskRules(farmTaskInfo, this.task.getUid().toString()).contains(handlerUid);
            CFRuleButton cfRuleButton = new CFRuleButton((IHandlerInfo) handler, handler, contains, startX, startY) {
                @Override
                public void onClick(double pMouseX, double pMouseY) {
                    this.isSelected = !this.isSelected;
                    NetworkHandler.sendToServer(new FarmTaskRuleActionMessage(maid.getId(), task.getUid().toString(), this.handlerInfo.getUid().toString(), this.isSelected));
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
        Button downButton = new ImageButton(startX, startY + 8 + 1 + 66, 9, 7, 237, 10, 14, TEXTURE, b -> {
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

    // 95 - 29 = 66;
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
