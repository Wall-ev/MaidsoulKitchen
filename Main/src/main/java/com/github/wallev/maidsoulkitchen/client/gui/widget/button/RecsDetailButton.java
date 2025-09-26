package com.github.wallev.maidsoulkitchen.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.api.client.gui.ITooltipButton;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.client.gui.widget.info.ResultInfo;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.cook.v1.CookDataV1;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec.MKRecipe;
import com.github.wallev.maidsoulkitchen.vhelper.client.chat.VComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;

import java.util.ArrayList;
import java.util.List;

public class RecsDetailButton extends AbstractButton implements ITooltipButton {
    public static final ResultInfo REF = new ResultInfo(4, 6, 20, 20, 2, 2);
    protected final int startX;
    protected final int startY;
    protected final int wWidth;
    protected final int wHeight;
    protected boolean needActive = false;
    protected List<MKRecipe<?>> recs = new ArrayList<>();
    protected List<RecButton> recsButtons = new ArrayList<>();

    protected final EntityMaid maid;
    protected final ICookTask<?, ?> cookTask;
    protected final CookDataV1 cookData;

    public RecsDetailButton(int pX, int pY, int pWidth, int pHeight, EntityMaid maid, ICookTask<?, ?> cookTask, CookDataV1 cookData) {
        super(pX, pY, pWidth, pHeight, VComponent.empty());
        this.startX = pX;
        this.startY = pY;
        this.wWidth = pWidth;
        this.wHeight = pHeight;

        this.maid = maid;
        this.cookTask = cookTask;
        this.cookData = cookData;

        this.setCanAction(false);
    }

    public void setCanAction(boolean can) {
        this.needActive = can;
        this.active = can;
        this.visible = can;
    }

    public boolean needActive() {
        return needActive;
    }

    protected void createRecsButton() {
        recsButtons.clear();

        int pSize = recs.size();
        int pCol = REF.col();
        int rows = (pSize + pCol - 1) / pCol;
        int columns = Math.min(pSize, pCol);

        int w = columns * REF.rowWidth() + REF.rowSpacing() * (columns - 1);
        int h = rows * REF.colHeight() + REF.colSpacing() * (rows - 1);

        this.setWidth(w + 6);
        this.setHeight(h + 6);

        this.setX(this.startX + (this.wWidth - this.getWidth()) / 2);
        this.setY(this.startY + (this.wHeight - this.getHeight()) / 2);

        int index = 0;
        for (int row = 0; row < REF.row(); row++) {
            for (int col = 0; col < REF.col(); col++) {
                if (index >= recs.size()) {
                    return;
                }

                List<MKRecipe<?>> recipes = List.of(recs.get(index++));
                int x = this.getX() + (REF.rowWidth() + REF.rowSpacing()) * col;
                int y = this.getY() + (REF.colHeight() + REF.colSpacing()) * row;

                RecButton recButton = new RecButton(maid, cookTask, cookData, recipes, x + 3, y + 3) {

                };
                recsButtons.add(recButton);
            }
        }
    }

    public List<MKRecipe<?>> getRecs() {
        return recs;
    }

    public void setRecs(List<MKRecipe<?>> recs) {
        this.recs = recs;
        this.createRecsButton();
    }

    public List<RecButton> getRecsButtons() {
        return recsButtons;
    }

    public void setRecsButtons(List<RecButton> recsButtons) {
        this.recsButtons = recsButtons;
    }

    public boolean isNeedActive() {
        return needActive;
    }

    public void setNeedActive(boolean needActive) {
        this.needActive = needActive;
    }

    @Override
    public boolean isHoveredOrFocused() {
        return false;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(0, 0, 199);

        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        recsButtons.forEach(b -> {
            b.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        });

        pGuiGraphics.pose().popPose();
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        super.onClick(pMouseX, pMouseY);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pButton == 1) {
            for (RecButton b : recsButtons) {
                if (b.superClicked(pMouseX, pMouseY)) {
                    if (b.debugClicked()) {
                        return false;
                    }
                }
            }
            return false;
        }


        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    protected boolean clicked(double pMouseX, double pMouseY) {
        for (RecButton b : recsButtons) {
            if (b.clicked(pMouseX, pMouseY)) {
                b.onClick(pMouseX, pMouseY);
                return true;
            }
        }

        return false;
    }

    public boolean superClicked(double pMouseX, double pMouseY) {
        return super.clicked(pMouseX, pMouseY);
    }

    @Override
    public boolean isTooltipHovered() {
        for (RecButton b : recsButtons) {
            if (b.isTooltipHovered()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void renderTooltip(GuiGraphics graphics, Minecraft mc, int mouseX, int mouseY) {
        recsButtons.forEach(b -> {
            if (b.isTooltipHovered()) {
                b.renderTooltip(graphics, mc, mouseX, mouseY);
            }
        });
    }

    @Override
    public void onPress() {

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        Tooltip tooltip = this.getTooltip();
        if (tooltip != null) {
            tooltip.updateNarration(narrationElementOutput);
        }
    }

    @Override
    protected boolean isValidClickButton(int pButton) {
        return pButton == 0 || pButton == 1;
    }
}
