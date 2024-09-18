package com.github.catbert.tlma.client.gui.entity.maid.cook;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.api.task.v1.cook.ICookTask;
import com.github.catbert.tlma.client.gui.widget.button.*;
import com.github.catbert.tlma.entity.passive.CookTaskData;
import com.github.catbert.tlma.inventory.container.CookConfigureContainer;
import com.github.catbert.tlma.network.NetworkHandler;
import com.github.catbert.tlma.network.message.SetCookTaskModeMessage;
import com.github.catbert.tlma.util.MaidAddonTagUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.Recipe;
import org.anti_ad.mc.ipn.api.IPNButton;
import org.anti_ad.mc.ipn.api.IPNGuiHint;
import org.anti_ad.mc.ipn.api.IPNPlayerSideOnly;

import java.util.List;

@IPNPlayerSideOnly
@IPNGuiHint(button = IPNButton.SORT, horizontalOffset = -36, bottom = -12)
@IPNGuiHint(button = IPNButton.SORT_COLUMNS, horizontalOffset = -24, bottom = -24)
@IPNGuiHint(button = IPNButton.SORT_ROWS, horizontalOffset = -12, bottom = -36)
@IPNGuiHint(button = IPNButton.SHOW_EDITOR, horizontalOffset = -5)
@IPNGuiHint(button = IPNButton.SETTINGS, horizontalOffset = -5)
public class CookConfigureGui extends MaidTaskConfigureGui<CookConfigureContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TLMAddon.MOD_ID, "textures/gui/cook_guide.png");
    protected final Zone taskDisplay = new Zone(6, 20, 70, 20);
    // 需要特殊处理
    protected final Zone typeDisplay = new Zone(-4, 27, 8, 13);
    protected final Zone resultDisplay = new Zone(6, 44, 152, 86);
    protected final Zone scrollDisplay = new Zone(161, 44, 9, 86);
    protected final ResultInfo ref = new ResultInfo(4, 7, 20, 20, 2, 2);
    @SuppressWarnings("rawtypes")
    private List<Recipe> recipeList;
    private CompoundTag cookTaskInfo;

    public CookConfigureGui(CookConfigureContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, Component.translatable("gui.touhou_little_maid_addon.cook_setting_screen.title"));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected void initAdditionData() {
        super.initAdditionData();
        this.recipeList = (List<Recipe>) ((ICookTask<?, ?>) task).getRecipes(maid.level);
        this.cookTaskInfo = MaidAddonTagUtil.getCookTaskInfo(maid, task.getUid().toString());
    }

    @Override
    protected void initAdditionGuiInfo() {
        super.initAdditionGuiInfo();
        this.addTaskInfoButton();
        this.addTypeButton();
        this.addResultInfo();
        this.addScrollButton();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderAddition(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.drawScrollInfoBar(graphics);
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int x, int y) {
        super.renderTooltip(graphics, x, y);
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
            if (delta < 0 && solIndex < (this.recipeList.size() - 1) / (ref.col() * ref.row())) {
                solIndex++;
                this.init();
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    private void addTaskInfoButton() {
        int startX = visualZone.startX() + taskDisplay.startX();
        int startY = visualZone.startY() + taskDisplay.startY();
        TaskInfoButton taskInfoButton = new TaskInfoButton(startX, startY, taskDisplay.width(), taskDisplay.height(), this.task);
        this.addRenderableWidget(taskInfoButton);
    }

    private void addTypeButton() {
        int maxWidth = 0;
        for (CookTaskData.Mode value : CookTaskData.Mode.values()) {
            maxWidth = Math.max(maxWidth, font.width(Component.translatable("gui.touhou_little_maid_addon.btn.cook_guide.type." + value.getUid())) + 5);
        }
        int startX = width - leftPos - (-typeDisplay.startX()) - maxWidth - 1;
        int startY = visualZone.startY() + typeDisplay.startY();

        TypeButton2 typeButton = new TypeButton2(startX, startY, maxWidth, typeDisplay.height(), MaidAddonTagUtil.getCookTaskMode(cookTaskInfo).equals(CookTaskData.Mode.SELECT.getUid())) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                this.toggleSelected();
                NetworkHandler.sendToServer(new SetCookTaskModeMessage(maid.getId(), task.getUid().toString(), this.isSelected ? CookTaskData.Mode.SELECT.getUid() : CookTaskData.Mode.RANDOM.getUid()));
            }
        };
        this.addRenderableWidget(typeButton);
    }

    @SuppressWarnings("rawtypes")
    private void addResultInfo() {
        int startX = visualZone.startX() + resultDisplay.startX();
        int startY = visualZone.startY() + resultDisplay.startY();

        int index = solIndex * (ref.row() * ref.col());
        for (int row = 0; row < ref.row(); row++) {
            for (int col = 0; col < ref.col(); col++) {
                if (index >= this.recipeList.size()) {
                    return;
                }
                Recipe recipe = this.recipeList.get(index);
                int x = startX + (ref.rowWidth() + ref.rowSpacing()) * col;
                int y = startY + (ref.colHeight() + ref.colSpacing()) * row;
                RecButton recButton = new RecButton(maid, cookTaskInfo, recipe, x, y);
                this.addRenderableWidget(recButton);
                index++;
            }
        }
    }

    // 161, 25 189, 74
    private void addScrollButton() {
        int startX = visualZone.startX() + scrollDisplay.startX();
        int startY = visualZone.startY() + scrollDisplay.startY();
        ImageButton upButton = new ImageButton(startX, startY, 9, 7, 199, 74, 14, TEXTURE, b -> {
            if (this.solIndex > 0) {
                this.solIndex--;
                this.init();
            }
        });
        Button downButton = new ImageButton(startX, startY + 8 + 1 + 70, 9, 7, 208, 74, 14, TEXTURE, b -> {
            if (this.solIndex < (this.recipeList.size() - 1) / (ref.col() * ref.row())) {
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
        graphics.blit(TEXTURE, startX, startY + 8, 189, 64, 9, 70);
        drawScrollIndicator(graphics, startX + 1, startY + 8 + 1);
    }

    private void drawScrollIndicator(GuiGraphics graphics, int startX, int startY) {
        if ((this.recipeList.size() - 1) / (ref.col() * ref.row()) > 1) {
            graphics.blit(TEXTURE, startX, startY + (int) ((70 - 11) * getCurrentScroll()), 199, 64, 7, 9);
        } else {
            graphics.blit(TEXTURE, startX, startY, 206, 64, 7, 9);
        }
    }

    private float getCurrentScroll() {
        return Mth.clamp((float) (solIndex * (1.0 / ((this.recipeList.size() - 1) / (ref.col() * ref.row())))), 0, 1);
    }
}
