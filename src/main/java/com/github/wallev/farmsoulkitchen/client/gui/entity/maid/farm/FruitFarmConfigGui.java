package com.github.wallev.farmsoulkitchen.client.gui.entity.maid.farm;

import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import com.github.wallev.farmsoulkitchen.api.task.v1.farm.ICompatFarm;
import com.github.wallev.farmsoulkitchen.api.task.v1.farm.ICompatFarmHandler;
import com.github.wallev.farmsoulkitchen.api.task.v1.farm.IHandlerInfo;
import com.github.wallev.farmsoulkitchen.client.gui.entity.maid.MaidTaskConfigGui;
import com.github.wallev.farmsoulkitchen.client.gui.widget.button.CFRuleButton;
import com.github.wallev.farmsoulkitchen.client.gui.widget.button.ResultInfo;
import com.github.wallev.farmsoulkitchen.client.gui.widget.button.Zone;
import com.github.wallev.farmsoulkitchen.entity.data.inner.task.FruitData;
import com.github.wallev.farmsoulkitchen.inventory.container.maid.FruitFarmConfigContainer;
import com.github.wallev.farmsoulkitchen.network.NetworkHandler;
import com.github.wallev.farmsoulkitchen.network.message.ActionFruitFarmRuleMessage;
import com.github.wallev.farmsoulkitchen.network.message.SetFruitFarmSearchYOffsetMessage;
import com.github.wallev.farmsoulkitchen.task.farm.TaskFruitFarm;
import com.github.wallev.farmsoulkitchen.task.farm.handler.v1.IFarmHandlerManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
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
    private static final ResourceLocation TEXTURE = new ResourceLocation(FarmsoulKitchen.MOD_ID, "textures/gui/farm_guide.png");
    protected final Zone scrollDisplay = new Zone(161, 47, 9, 110);
    protected final Zone ruleDisplay = new Zone(6, 47, 152, 110);
    protected final ResultInfo ref = new ResultInfo(3, 1, 152, 24, 0, 5);
    private final int limitSize = ref.row() * ref.col();
    private TaskFruitFarm fruitFarm;
    private List<ICompatFarmHandler> handlers;
    private FruitData farmTaskInfo;

    public FruitFarmConfigGui(FruitFarmConfigContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, screenContainer.getMaid().getTask().getName().append(Component.translatable("gui.farmsoulkitchen.farm_config_screen.title")));
    }

    @SuppressWarnings({"unchecked"})
    @Override
    protected void initAdditionData() {
        super.initAdditionData();
        this.handlers = (List<ICompatFarmHandler>) Arrays.stream(((ICompatFarm<?, ?>) task).getManagerHandlerValues()).map(IFarmHandlerManager::getFarmHandler).toList();
        this.fruitFarm = (TaskFruitFarm) task;
        this.farmTaskInfo = fruitFarm.getTaskData(maid);
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
    }

    private void addRetrievalButton() {
        MutableComponent literal = Component.translatable("gui.farmsoulkitchen.fruit_farm_configer_screen.farm.fruit.search_y_offset", "--");
        int x = font.width(literal);
        int startX = visualZone.startX() + 6 + 26 + x;
        int startY = visualZone.startY() + 22 + 2;
        ImageButton addButton = new ImageButton(startX, startY, 17, 18, 80, 238, 0, TEXTURE, b -> {
            if (this.farmTaskInfo.searchYOffset() >= 5) {
                return;
            }
            this.farmTaskInfo.increaseYOffset();
            NetworkHandler.sendToServer(new SetFruitFarmSearchYOffsetMessage(maid.getId(), fruitFarm.getCookDataKey().getKey(), this.farmTaskInfo.searchYOffset()));
        });
        Button downButton = new ImageButton(startX + 17, startY, 17, 18, 80 + 17, 238, 0, TEXTURE, b -> {
            if (this.farmTaskInfo.searchYOffset() <= -5) {
                return;
            }
            this.farmTaskInfo.decreaseYOffset();
            NetworkHandler.sendToServer(new SetFruitFarmSearchYOffsetMessage(maid.getId(), fruitFarm.getCookDataKey().getKey(), this.farmTaskInfo.searchYOffset()));
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
        MutableComponent literal = Component.translatable("gui.farmsoulkitchen.fruit_farm_configer_screen.farm.fruit.search_y_offset", this.farmTaskInfo.searchYOffset());
        // 暂时先这样... todo
        int width = font.width(literal);
        int x = visualZone.startX() + 6;
        int y = visualZone.startY() + 22;
        graphics.blit(TEXTURE, x, y, 0 ,236, 22, 20);
        // 暂时先这样... todo
        if (this.farmTaskInfo.searchYOffset() >= 0) {
            width += font.width(Component.literal("-"));
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
            boolean contains = farmTaskInfo.rules().contains(handlerUid);
            CFRuleButton cfRuleButton = new CFRuleButton((IHandlerInfo) handler, handler, contains, startX, startY) {
                @Override
                public void onClick(double pMouseX, double pMouseY) {
                    this.isSelected = !this.isSelected;
                    farmTaskInfo.addOrRemoveRule(this.handlerInfo.getUid().toString());
                    NetworkHandler.sendToServer(new ActionFruitFarmRuleMessage(maid.getId(), fruitFarm.getCookDataKey().getKey(), this.handlerInfo.getUid().toString()));
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
        graphics.blit(TEXTURE, startX, startY + 8, 247, 8, 9, 2);
        graphics.blit(TEXTURE, startX, startY + 8 + 2, 247, 10, 9, 62);
        graphics.blit(TEXTURE, startX, startY + 8 + 2 + 62, 247, 100, 9, 2);
        drawScrollIndicator(graphics, startX + 1, startY + 8 + 1);
    }

    // 95 - 29 = 66;
    private void drawScrollIndicator(GuiGraphics graphics, int startX, int startY) {
        if ((this.handlers.size() - 1) / limitSize >= 1) {
            graphics.blit(TEXTURE, startX, startY + (int) ((67 - 12) * getCurrentScroll()), 228, 0, 7, 9);
        } else {
            graphics.blit(TEXTURE, startX, startY, 235, 0, 7, 9);
        }
    }

    private float getCurrentScroll() {
        return Mth.clamp((float) (solIndex * (1.0 / ((this.handlers.size() - 1) / limitSize))), 0, 1);
    }
}
