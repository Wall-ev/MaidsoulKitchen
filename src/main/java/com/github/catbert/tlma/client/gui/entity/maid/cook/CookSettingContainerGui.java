package com.github.catbert.tlma.client.gui.entity.maid.cook;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.api.task.v1.cook.ICookTask;
import com.github.catbert.tlma.inventory.container.CookSettingContainer;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.anti_ad.mc.ipn.api.IPNButton;
import org.anti_ad.mc.ipn.api.IPNGuiHint;
import org.anti_ad.mc.ipn.api.IPNPlayerSideOnly;

import java.util.Collections;
import java.util.List;


@IPNPlayerSideOnly
@IPNGuiHint(button = IPNButton.SORT, horizontalOffset = -36, bottom = -12)
@IPNGuiHint(button = IPNButton.SORT_COLUMNS, horizontalOffset = -24, bottom = -24)
@IPNGuiHint(button = IPNButton.SORT_ROWS, horizontalOffset = -12, bottom = -36)
@IPNGuiHint(button = IPNButton.SHOW_EDITOR, horizontalOffset = -5)
@IPNGuiHint(button = IPNButton.SETTINGS, horizontalOffset = -5)
public class CookSettingContainerGui extends AbstractMaidContainerGui<CookSettingContainer> implements ICookContainerScreen{

    private static final ResourceLocation TEXTURE = new ResourceLocation(TLMAddon.MOD_ID, "textures/gui/cook_guide.png");
    private int left;
    private int top;
    private int solIndex = 0;
    private List<ItemStack> list = Collections.emptyList();

    private int rowSpacing;
    private int colSpacing;
    private int numRows;
    private int numCols;
    private int rowWidth;
    private int colHeight;

    public CookSettingContainerGui(CookSettingContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        initInfo(5, 7, 20, 20, 2, 2);
    }

    private void initInfo(int row, int col, int rowWidth, int colHeight, int colSpacing, int rowSpacing) {
        this.rowWidth = rowWidth;
        this.colHeight = colHeight;
        this.rowSpacing = rowSpacing;
        this.colSpacing = colSpacing;
        this.numRows = row;
        this.numCols = col;
    }

    @Override
    @SuppressWarnings("all")
    protected void init() {
        super.init();
        left = leftPos + 81;
        top = topPos + 28;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && this.getMaid().getTask() instanceof ICookTask<?, ?> task) {
            List<Recipe> allRecipesFor = (List<Recipe>) task.getRecipes(mc.level);
            list = allRecipesFor.stream().map(recipe -> recipe.getResultItem(mc.level.registryAccess())).toList();
        }

        if (list.isEmpty()) return;

        addResultStackButtons();
        addScrollButton();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        if (getMaid() != null) {
            drawTaskTitleBar(graphics, mouseX, mouseY, partialTicks);

            // 绘制 滚动条 背景
            // 161, 25
            {
                int startX = left + 161;
                int startY = top + 25;
                graphics.blit(TEXTURE, startX, startY + 8, 179, 64, 9, 92);
                drawScrollSide(graphics, startX + 1, startY + 8 + 1);
            }
        }
    }

    private void drawScrollSide(GuiGraphics graphics, int startX, int startY) {
        if ((this.list.size() - 1) / (numCols * numRows) > 1) {
            graphics.blit(TEXTURE, startX, startY + (int) ((92 - 11) * getCurrentScroll()), 189, 64, 7, 9);
        } else {
            graphics.blit(TEXTURE, startX, startY,196, 64, 7, 9);
        }
    }

    public float getCurrentScroll() {
        return Mth.clamp((float) (solIndex * (1.0 / ((this.list.size() - 1) / (numCols * numRows)))), 0, 1);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
//        if (delta != 0) {
//            // 向上滚
//            if (delta > 0 && solIndex > 0) {
//                solIndex--;
//                this.init();
//                return true;
//            }
//            // 向下滚
//            if (delta < 0 && solIndex < (this.list.size() - 1) / (numCols * numRows)){
//                solIndex++;
//                this.init();
//                return true;
//            }
//        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    // 161, 25 189, 74
    private void addScrollButton() {
        int startX = left + 161;
        int startY = top + 25;
        ImageButton upButton = new ImageButton(startX, startY, 9, 7, 189, 74, 14, TEXTURE, b -> {
            if (this.solIndex > 0) {
                this.solIndex--;
                this.init();
            }
        });
        Button downButton = new ImageButton(startX, startY + 101, 9, 7, 198, 74, 14, TEXTURE, b -> {
            if (this.solIndex < (this.list.size() - 1) / (numCols * numRows)) {
                this.solIndex++;
                this.init();
            }
        });
        this.addRenderableWidget(upButton);
        this.addRenderableWidget(downButton);
    }

    //6, 4
    private void drawTaskTitleBar(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        int startX = left + 6;
        int startY = top + 4;
        IMaidTask task = getMaid().getTask();

        graphics.blit(TEXTURE, startX, startY, 179, 2, 70, 20);
        graphics.renderItem(task.getIcon(), startX + 2, startY + 2);
        List<FormattedCharSequence> splitTexts = font.split(task.getName(), 42);
        if (!splitTexts.isEmpty()) {
            graphics.drawString(font, splitTexts.get(0), startX + 22, startY + 5, 0x333333, false);
        }
    }

    //6, 25
    protected void addResultStackButtons() {

        int startX = left + 6;
        int startY = top + 25;

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int x = startX + col * (rowWidth + rowSpacing); // 计算 x 坐标
                int y = startY + row * (colHeight + colSpacing); // 计算 y 坐标
                int index = row * numCols + col; // 计算元素在列表中的索引
                if (index < 35) {
                    ItemStack stack = this.getItemStack(index);
                    if (stack.isEmpty()) continue;

                    renderChildren(stack, false, x, y);
                }
            }
        }
    }

    private void renderChildren(ItemStack stack, boolean b, int x, int y) {

        StateSwitchingButton showChatBubble = new StateSwitchingButton(x, y, rowWidth, colHeight, b) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                this.isStateTriggered = !this.isStateTriggered;
            }

            @Override
            public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
                int i = this.xTexStart;
                int j = this.yTexStart;
                if (this.isStateTriggered) {
                    i += this.xDiffTex;
                }

                if (this.isHoveredOrFocused()) {
                    j += this.yDiffTex;
                }

                if (this.isHovered()){
                    pGuiGraphics.renderTooltip(font, stack, pMouseX, pMouseY);
                }

                pGuiGraphics.blit(this.resourceLocation, this.getX(), this.getY(), i, j, this.width, this.height);
                pGuiGraphics.renderItem(stack, x + 2, y + 2);
            }
        };
        showChatBubble.initTextureValues(179, 25, 22, 0, TEXTURE);

        this.addRenderableWidget(showChatBubble);

    }

    private ItemStack getItemStack(int index) {
        int i = index + (numRows * numCols) * solIndex;
        if (i < list.size()) {
            return list.get(i);
        }
        return ItemStack.EMPTY;
    }
}
