package com.github.wallev.farmsoulkitchen.client.gui.entity.maid.cook;

import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import com.github.wallev.farmsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.wallev.farmsoulkitchen.client.gui.entity.maid.MaidTaskConfigGui;
import com.github.wallev.farmsoulkitchen.config.subconfig.TaskConfig;
import com.github.wallev.farmsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.farmsoulkitchen.inventory.container.maid.CookConfigContainer;
import com.github.wallev.farmsoulkitchen.network.NetworkHandler;
import com.github.wallev.farmsoulkitchen.network.message.ActionCookDataRecMessage;
import com.github.wallev.farmsoulkitchen.network.message.SetCookDataModeMessage;
import com.github.wallev.farmsoulkitchen.client.gui.widget.button.*;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.anti_ad.mc.ipn.api.IPNButton;
import org.anti_ad.mc.ipn.api.IPNGuiHint;
import org.anti_ad.mc.ipn.api.IPNPlayerSideOnly;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@IPNPlayerSideOnly
@IPNGuiHint(button = IPNButton.SORT, horizontalOffset = -36, bottom = -12)
@IPNGuiHint(button = IPNButton.SORT_COLUMNS, horizontalOffset = -24, bottom = -24)
@IPNGuiHint(button = IPNButton.SORT_ROWS, horizontalOffset = -12, bottom = -36)
@IPNGuiHint(button = IPNButton.SHOW_EDITOR, horizontalOffset = -5)
@IPNGuiHint(button = IPNButton.SETTINGS, horizontalOffset = -5)
public class CookConfigGui extends MaidTaskConfigGui<CookConfigContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(FarmsoulKitchen.MOD_ID, "textures/gui/cook_guide.png");
    protected final Zone taskDisplay = new Zone(6, 20, 70, 20);
    // 需要特殊处理
    protected final Zone typeDisplay = new Zone(-4, 22, 18, 18);
    protected final Zone searchBoxDisplay = new Zone(-25, 22, 18, 18);
    protected final Zone searchTextDisplay = new Zone(-25, 22, 41, 18);
    protected final Zone resultDisplay = new Zone(6, 44, 152, 86);
    protected final Zone scrollDisplay = new Zone(161, 44, 9, 86);
    protected final ResultInfo ref = new ResultInfo(4, 7, 20, 20, 2, 2);
    @SuppressWarnings("rawtypes")
    private final List<Recipe> recipeList = new ArrayList<>();
    private final List<RecButton> recButtons = new ArrayList<>();
    private EditBox searchBox;
    private CookData cookData;
    private ICookTask<?, ?> cookTask;

    public CookConfigGui(CookConfigContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, Component.translatable("gui.farmsoulkitchen.cook_setting_screen.title"));
    }

    @Override
    protected void initAdditionData() {
        super.initAdditionData();
        if (!(task instanceof ICookTask<?, ?>)) {
            return;
        }

        this.initCookData();
        this.initRecipeList();
    }

    private void initCookData() {
        this.cookTask = (ICookTask<?, ?>) task;
        this.cookData = cookTask.getTaskData(maid);
    }

    @SuppressWarnings("all")
    private void initRecipeList() {
        this.recipeList.clear();
        List<Recipe> recipes;
        Level level = maid.level;
        RegistryAccess registryAccess = level.registryAccess();
        if (searchBox != null && StringUtils.isNotBlank(searchBox.getValue())) {
            String search = this.searchBox.getValue().toLowerCase(Locale.US);
            recipes = (List<Recipe>) ((ICookTask<?, ?>) task).getRecipes(level)
                    .stream().filter(recipe -> {
                        return ((ICookTask<?, ?>) task).getResultItem((Recipe<?>)recipe, registryAccess).getDisplayName().getString().toLowerCase(Locale.US).contains(search);
                    }).toList();
        } else {
            recipes = (List<Recipe>) ((ICookTask<?, ?>) task).getRecipes(level); // all recipes
        }
        this.recipeList.addAll(recipes);
    }

    @Override
    protected void initAdditionWidgets() {
        super.initAdditionWidgets();
        this.addJeiButton();
        this.addTaskInfoButton();
        this.addSearchTextBox();
        this.addSearchBox();
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
        this.renderSearchSearchText(graphics, mouseX, mouseY, partialTicks);
        this.renderSearchBox(graphics);
        this.drawSplitZoneCard(graphics);
        this.drawScrollInfoBar(graphics);
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int x, int y) {
        super.renderTooltip(graphics, x, y);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
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

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height);
        String value = this.searchBox.getValue();
        this.searchBox.setValue(value);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.searchBox.tick();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.searchBox.mouseClicked(mouseX, mouseY, button)) {
            this.setFocused(this.searchBox);
            return true;
        } else if (this.searchBox.isFocused()) {
            this.searchBox.setFocused(false);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (searchBox == null) {
            return false;
        }
        String perText = this.searchBox.getValue();
        if (this.searchBox.charTyped(codePoint, modifiers)) {
            if (!Objects.equals(perText, this.searchBox.getValue())) {
                this.solIndex = 0;
                this.init();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean hasKeyCode = InputConstants.getKey(keyCode, scanCode).getNumericKeyValue().isPresent();
        String preText = this.searchBox.getValue();
        if (hasKeyCode) {
            return true;
        }
        if (this.searchBox.keyPressed(keyCode, scanCode, modifiers)) {
            if (!Objects.equals(preText, this.searchBox.getValue())) {
                this.solIndex = 0;
                this.init();
            }
            return true;
        } else {
            return this.searchBox.isFocused() && this.searchBox.isVisible() && keyCode != 256 || super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    protected void insertText(String text, boolean overwrite) {
        if (overwrite) {
            this.searchBox.setValue(text);
        } else {
            this.searchBox.insertText(text);
        }
    }

    private void addJeiButton() {
//        ImageButton jeiButton = new ImageButton(visualZone.startX() + visualZone.width() - 12, visualZone.startY() + 8, 9, 9, 247, 212, 10, TEXTURE, (b) -> {
//        }) {
//
//        };
//        this.addRenderableWidget(jeiButton);
    }

    private void addTaskInfoButton() {
        int startX = visualZone.startX() + taskDisplay.startX();
        int startY = visualZone.startY() + taskDisplay.startY();
        TaskInfoButton taskInfoButton = new TaskInfoButton(startX, startY, taskDisplay.width(), taskDisplay.height(), this.task);
        this.addRenderableWidget(taskInfoButton);
    }

    private void addSearchTextBox() {
        int startX = width - leftPos - (-searchTextDisplay.startX()) - searchTextDisplay.width() - 1;
        int startY = visualZone.startY() + searchTextDisplay.startY();

        String textCache = searchBox == null ? "" : searchBox.getValue();
        boolean visible = searchBox != null && searchBox.isVisible();
        boolean focus = searchBox != null && searchBox.isFocused();
        searchBox = new EditBox(getMinecraft().font, startX, startY, searchTextDisplay.width(), searchTextDisplay.height(), Component.empty()) {
            @Override
            public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
                if (this.isVisible()) {
                    pGuiGraphics.blit(TEXTURE, startX - searchBoxDisplay.width(), startY, 40, 232, 59, 18);
                    super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
                }
            }

            @Override
            public int getY() {
                return super.getY() + 5;
            }

            @Override
            public int getX() {
                return super.getX() + 3;
            }

            @Override
            public boolean isMouseOver(double pMouseX, double pMouseY) {
                return this.visible && pMouseX >= (double) startX && pMouseX < (double) (startX + this.width) && pMouseY >= (double) startY && pMouseY < (double) (startY + this.height);
            }
        };
        searchBox.setVisible(visible);
        searchBox.setFocused(focus);
        searchBox.setValue(textCache);
        searchBox.setBordered(false);
        searchBox.setTextColor(0xF3EFE0);
        this.addWidget(this.searchBox);
    }

    private void addSearchBox() {
        int startX = width - leftPos - (-searchBoxDisplay.startX()) - searchBoxDisplay.width() - 1;
        int startY = visualZone.startY() + searchBoxDisplay.startY();

        if (searchBox.isVisible()) {
            startX -= searchTextDisplay.width();
        }

        int finalStartX = startX;

        StateSwitchingButton typeButton = new StateSwitchingButton(finalStartX, startY, searchBoxDisplay.width(), searchBoxDisplay.height(), searchBox.isVisible()) {
            @Override
            public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
            }

            @Override
            public void onClick(double pMouseX, double pMouseY) {
                this.isStateTriggered = !isStateTriggered;

                if (isStateTriggered) {
                    this.setX(finalStartX - searchTextDisplay.width());
                    searchBox.setVisible(true);
                    searchBox.setFocused(true);
                    searchBox.moveCursorToEnd();
                    init();
                } else {
                    this.setX(finalStartX);
                    searchBox.setVisible(false);
                    searchBox.setFocused(false);
                    searchBox.setValue("");
                    init();
                }
            }
        };
        this.addRenderableWidget(typeButton);
    }

    private void addTypeButton() {
        int startX = width - leftPos - (-typeDisplay.startX()) - typeDisplay.width() - 1;
        int startY = visualZone.startY() + typeDisplay.startY();

        TypeButton typeButton = new TypeButton(startX, startY, typeDisplay.width(), typeDisplay.height(), cookData.mode().equals(CookData.Mode.SELECT.name)) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                setAndSyncMode(!isSelected);
                updateRecButtonsState(this::toggleState);
            }
        };
        this.addRenderableWidget(typeButton);
    }

    private void setAndSyncMode(String mode) {
        cookData.setMode(mode);
        NetworkHandler.sendToServer(new SetCookDataModeMessage(maid.getId(), cookTask.getCookDataKey().getKey(), mode));
    }

    private void setAndSyncMode(boolean isSelected) {
        setAndSyncMode(isSelected ? CookData.Mode.SELECT.name : CookData.Mode.RANDOM.name);
    }

    @SuppressWarnings("rawtypes")
    private void addResultInfo() {
        this.recButtons.clear();

        int startX = visualZone.startX() + resultDisplay.startX();
        int startY = visualZone.startY() + resultDisplay.startY();

        int index = solIndex * (ref.row() * ref.col());
        for (int row = 0; row < ref.row(); row++) {
            for (int col = 0; col < ref.col(); col++) {
                if (index >= this.recipeList.size()) {
                    return;
                }
                Recipe recipe = this.recipeList.get(index++);
                int x = startX + (ref.rowWidth() + ref.rowSpacing()) * col;
                int y = startY + (ref.colHeight() + ref.colSpacing()) * row;
                RecButton recButton = new RecButton(maid, (ICookTask<?, ?>) task, cookData, recipe, x, y) {
                    @Override
                    public void onClick(double pMouseX, double pMouseY) {
                        arAndSyncRec(getRecipe().getId().toString());
                        updateRecButtonsState(this::toggleState);
                    }
                };
                initRecButtonActive(recButton);
                this.addRenderableWidget(recButton);

                this.recButtons.add(recButton);
            }
        }
    }

    private void initRecButtonActive(RecButton recButton) {
        initRecButtonActive(recButton, cookData.mode(), cookData.recs());
    }

    // 适配鼠标类型显示
    private void initRecButtonActive(RecButton recButton, String cookTaskMode, List<String> cookTaskRecs) {
        if (!cookTaskMode.equals(CookData.Mode.SELECT.name)) {
            recButton.active = false;
            return;
        }
        if (cookTaskRecs.size() >= TaskConfig.COOK_SELECTED_RECIPES.get() && !cookTaskRecs.contains(recButton.getRecipe().getId().toString())) {
            recButton.active = false;
            return;
        }
        recButton.active = true;
    }

    private void updateRecButtonsState(Runnable selfRun) {
        boolean selectedType = cookData.mode().equals(CookData.Mode.SELECT.name);
        List<String> cookTaskRecs1 = cookData.recs();
        for (RecButton recButton : recButtons) {
            String id = recButton.getRecipe().getId().toString();
            // 不是选择模式，不可点击
            if (!selectedType) {
                recButton.active = false;
            }
            // 超出数量限制并且要继续添加配方，不可点击
            else if (cookTaskRecs1.size() >= (TaskConfig.COOK_SELECTED_RECIPES.get())
                    && !cookTaskRecs1.contains(id)) {
                recButton.active = false;
            }else {
                recButton.active = true;
            }
        }

        selfRun.run();
    }

    private void arAndSyncRec(String rec) {
        cookData.addOrRemoveRec(rec);
        NetworkHandler.sendToServer(new ActionCookDataRecMessage(maid.getId(), cookTask.getCookDataKey().getKey(), rec));
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

    private void renderSearchSearchText(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (searchBox == null) return;

        int startX = width - leftPos - (-searchTextDisplay.startX()) - searchTextDisplay.width() - 1;
        int startY = visualZone.startY() + searchTextDisplay.startY();
        searchBox.render(graphics, pMouseX, pMouseY, pPartialTick);
        if (searchBox.isVisible() && searchBox.getValue().isEmpty() && !searchBox.isFocused()) {
            graphics.drawString(font, Component.translatable("gui.farmsoulkitchen.search").withStyle(ChatFormatting.ITALIC), startX + 3, startY + 5, 0XF5F5F5);
        }
    }

    private void renderSearchBox(GuiGraphics graphics) {
        if (searchBox == null) return;

        int startX = width - leftPos - (-searchBoxDisplay.startX()) - searchBoxDisplay.width() - 1;
        int startY = visualZone.startY() + searchBoxDisplay.startY();

        if (searchBox.isVisible()) {
            startX -= searchTextDisplay.width();
        } else {
            graphics.blit(TEXTURE, startX, startY, 0, 232, 18, 18);
        }

        graphics.blit(TEXTURE, startX + 1, startY + 1, 0, 181, 16, 16);
    }

    private void drawSplitZoneCard(GuiGraphics graphics) {
        int startX = width - leftPos - (-typeDisplay.startX()) - typeDisplay.width() - 2;
        int startY = visualZone.startY() + typeDisplay.startY();
        graphics.fill(startX - 1, startY, startX, startY + typeDisplay.width(), Color.BLACK.getRGB());
    }

    private void drawScrollInfoBar(GuiGraphics graphics) {
        int startX = visualZone.startX() + scrollDisplay.startX();
        int startY = visualZone.startY() + scrollDisplay.startY();
        graphics.blit(TEXTURE, startX, startY + 8, 189, 64, 9, 70);
        drawScrollIndicator(graphics, startX + 1, startY + 8 + 1);
    }

    private void drawScrollIndicator(GuiGraphics graphics, int startX, int startY) {
        if ((this.recipeList.size() - 1) / (ref.col() * ref.row()) >= 1) {
            graphics.blit(TEXTURE, startX, startY + (int) ((70 - 2 - 9) * getCurrentScroll()), 199, 64, 7, 9);
        } else {
            graphics.blit(TEXTURE, startX, startY, 206, 64, 7, 9);
        }
    }

    private float getCurrentScroll() {
        return Mth.clamp((float) (solIndex * (1.0 / ((this.recipeList.size() - 1) / (ref.col() * ref.row())))), 0, 1);
    }
}
