package com.github.catbert.tlma.client.gui.entity.maid.cook;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.api.task.v1.cook.ICookTask;
import com.github.catbert.tlma.client.gui.widget.button.NormalTooltipButton;
import com.github.catbert.tlma.inventory.container.CookSettingContainer;
import com.github.catbert.tlma.inventory.tooltip.AmountTooltip;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.items.ItemStackHandler;
import org.anti_ad.mc.ipn.api.IPNButton;
import org.anti_ad.mc.ipn.api.IPNGuiHint;
import org.anti_ad.mc.ipn.api.IPNPlayerSideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@IPNPlayerSideOnly
@IPNGuiHint(button = IPNButton.SORT, horizontalOffset = -36, bottom = -12)
@IPNGuiHint(button = IPNButton.SORT_COLUMNS, horizontalOffset = -24, bottom = -24)
@IPNGuiHint(button = IPNButton.SORT_ROWS, horizontalOffset = -12, bottom = -36)
@IPNGuiHint(button = IPNButton.SHOW_EDITOR, horizontalOffset = -5)
@IPNGuiHint(button = IPNButton.SETTINGS, horizontalOffset = -5)
public class CookSettingContainerGui extends AbstractMaidContainerGui<CookSettingContainer> implements ICookContainerScreen {

    private static final ResourceLocation TEXTURE = new ResourceLocation(TLMAddon.MOD_ID, "textures/gui/cook_guide.png");
    private int left;
    private int top;
    private int solIndex = 0;
    private List<ItemStack> resultStackList = Collections.emptyList();
    private List<Recipe> recipeList = Collections.emptyList();
    private ItemStack lastResultTooltipStack = ItemStack.EMPTY;
    private List<ItemStack> lastIngreTooltipList = Collections.emptyList();
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
            this.recipeList = allRecipesFor;
            resultStackList = allRecipesFor.stream().map(recipe -> recipe.getResultItem(mc.level.registryAccess())).toList();
        }

        if (resultStackList.isEmpty()) return;

        addResultStackButtons();
        addScrollButton();
        addTypeButton();
    }

    private void addTypeButton() {
        MutableComponent randomComponent = Component.translatable("gui.touhou_little_maid_addon.btn.cook_guide.type.random");
        List<Component> randomTooltip = List.of(Component.translatable("gui.touhou_little_maid_addon.btn.cook_guide.type.random.desc"));
        int randomButtonWidth = font.width(randomComponent) + 4;
        MutableComponent selectedComponent = Component.translatable("gui.touhou_little_maid_addon.btn.cook_guide.type.selected");
        List<Component> selectedTooltip = List.of(Component.translatable("gui.touhou_little_maid_addon.btn.cook_guide.type.selected.desc"));
        int selectedButtonWidth = font.width(selectedComponent) + 4;

        int startX = width - leftPos - 5 - randomButtonWidth - selectedButtonWidth;
        int startY = top + 8;

        NormalTooltipButton randomBtn = new NormalTooltipButton(startX, startY, randomButtonWidth, 15, randomComponent, randomTooltip, b -> {
        });
        NormalTooltipButton selectedBtn = new NormalTooltipButton(startX + selectedButtonWidth, startY, selectedButtonWidth, 15, selectedComponent, selectedTooltip, b -> {
        });

        this.addRenderableWidget(randomBtn);
        this.addRenderableWidget(selectedBtn);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (getMaid() != null) {
            drawTaskTitleBar(graphics, mouseX, mouseY, partialTicks);
            // 绘制 滚动条 背景
            // 161, 25
            drawScrollBackground(graphics);
        }
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderTooltip(graphics, mouseX, mouseY);
        this.renderItemStackTooltips(getMinecraft(), graphics, mouseX, mouseY);
        this.renderTaskBarTooltip(graphics, mouseX, mouseY);
    }

    private void renderTaskBarTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        int startX = left + 6;
        int startY = top + 4;
        IMaidTask task = getMaid().getTask();
        boolean taskWidgetHovered = mouseX >= startX && mouseY >= startY && mouseX < startX + 70 && mouseY < startY + 20;
        if (taskWidgetHovered) {
            List<Component> components = new ArrayList<>();
            components.add(Component.translatable("gui.touhou_little_maid_addon.widget.cook_guide.task.desc", task.getName()));
            if (task instanceof ICookTask<?, ?> maidTask) {
                RecipeType<?> recipeType = maidTask.getRecipeType();
                String typeString = recipeType.toString();

                components.add(CommonComponents.SPACE);
                components.add(Component.translatable("gui.touhou_little_maid_addon.widget.cook_guide.task.recipe_type", typeString).withStyle(ChatFormatting.DARK_GRAY));
            }
            graphics.renderComponentTooltip(getMinecraft().font, components, mouseX, mouseY);
        }
    }

    private void drawScrollBackground(GuiGraphics graphics) {
        int startX = left + 161;
        int startY = top + 25;
        graphics.blit(TEXTURE, startX, startY + 8, 179, 64, 9, 92);
        drawScrollSide(graphics, startX + 1, startY + 8 + 1);
    }

    private void drawScrollSide(GuiGraphics graphics, int startX, int startY) {
        if ((this.resultStackList.size() - 1) / (numCols * numRows) > 1) {
            graphics.blit(TEXTURE, startX, startY + (int) ((92 - 11) * getCurrentScroll()), 189, 64, 7, 9);
        } else {
            graphics.blit(TEXTURE, startX, startY, 196, 64, 7, 9);
        }
    }

    public float getCurrentScroll() {
        return Mth.clamp((float) (solIndex * (1.0 / ((this.resultStackList.size() - 1) / (numCols * numRows)))), 0, 1);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        // 176, 137
        boolean isCookSettingMainZone = mouseX >= left && mouseY >= top && mouseX < left + 176 && mouseY < top + 137;
        if (delta != 0 && isCookSettingMainZone) {
            // 向上滚
            if (delta > 0 && solIndex > 0) {
                solIndex--;
                this.init();
                return true;
            }
            // 向下滚
            if (delta < 0 && solIndex < (this.resultStackList.size() - 1) / (numCols * numRows)) {
                solIndex++;
                this.init();
                return true;
            }
        }
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
            if (this.solIndex < (this.resultStackList.size() - 1) / (numCols * numRows)) {
                this.solIndex++;
                this.init();
            }
        });
        this.addRenderableWidget(upButton);
        this.addRenderableWidget(downButton);
    }

    //6, 4
    private void drawTaskTitleBar(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
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

        StateSwitchingButton resultStackBtn = new StateSwitchingButton(x, y, rowWidth, colHeight, b) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                this.isStateTriggered = !this.isStateTriggered;
            }

            @Override
            public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
                super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
                pGuiGraphics.renderItem(stack, x + 2, y + 2);
            }
        };
        resultStackBtn.initTextureValues(179, 25, 22, 0, TEXTURE);

        this.addRenderableWidget(resultStackBtn);

    }

    public void renderItemStackTooltips(Minecraft mc, GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        int startX = left + 6;
        int startY = top + 25;

        int index = checkCoordinate2(pMouseX, pMouseY, startX, startY);
        if (index != -1) {
            ItemStack itemStack = getItemStack(index);
            if (!itemStack.isEmpty()) {
                renderTooltipWithImage(itemStack, mc, pGuiGraphics, pMouseX, pMouseY);
            }
        }
    }

    private void renderTooltipWithImage(ItemStack stack, Minecraft mc, GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        List<Component> stackTooltip = Screen.getTooltipFromItem(mc, stack);

        ItemStackHandler container = getIngreContainer(stack);
        Optional<TooltipComponent> itemContainerTooltip = Optional.of(new AmountTooltip(container));

        pGuiGraphics.renderTooltip(mc.font, stackTooltip, itemContainerTooltip, pMouseX, pMouseY);
    }

    // todo
    @SuppressWarnings("all")
    private ItemStackHandler getIngreContainer(ItemStack stack){
        Item item = stack.getItem();

        if (this.lastResultTooltipStack.is(item)) {
            return getContainer(this.lastIngreTooltipList);
        }

        Optional<Recipe> first = this.recipeList.stream()
                .filter(r -> r.getResultItem(getMinecraft().level.registryAccess()).is(item))
                .findFirst();

        if (first.isEmpty()) {
            return getContainer(Collections.emptyList());
        }

        this.lastResultTooltipStack = stack;
        this.lastIngreTooltipList = first.get().getIngredients().stream()
                .map(ingre -> ((Ingredient)ingre).getItems()[0])
                .toList();
        return getContainer(this.lastIngreTooltipList);
    }

    public ItemStackHandler getContainer(List<ItemStack> stacks) {
        ItemStackHandler handler = new ItemStackHandler(stacks.size());
        for (int i = 0; i < stacks.size(); i++) {
            handler.setStackInSlot(i, stacks.get(i));
        }
        return handler;
    }

    public int checkCoordinate2(double pMouseX, double pMouseY, int startX, int startY) {
        if (pMouseX < startX || pMouseY < startY) return -1;

        int offsetRow = (int) (pMouseX - startX);
        int offsetCol = (int) (pMouseY - startY);

        if (offsetRow % (rowWidth + rowSpacing) < rowWidth && offsetCol % (colHeight + colSpacing) < colHeight) {
            int blockCol = offsetRow / (rowWidth + rowSpacing);
            int blockRow = offsetCol / (colHeight + colSpacing);

            if (blockRow >= 0 && blockRow < numRows && blockCol >= 0 && blockCol < numCols) {
                int blockIndex = blockRow * numCols + blockCol;

                if (blockIndex >= 0 && blockIndex < numCols * numRows) {
                    return blockIndex;
                }
            }
        }
        return -1;

    }

    private ItemStack getItemStack(int index) {
        int i = index + (numRows * numCols) * solIndex;
        if (i < resultStackList.size()) {
            return resultStackList.get(i);
        }
        return ItemStack.EMPTY;
    }
}
