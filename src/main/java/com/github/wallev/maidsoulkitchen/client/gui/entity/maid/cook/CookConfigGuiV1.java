package com.github.wallev.maidsoulkitchen.client.gui.entity.maid.cook;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.client.gui.entity.maid.MaidTaskConfigGui;
import com.github.wallev.maidsoulkitchen.client.gui.widget.button.*;
import com.github.wallev.maidsoulkitchen.client.gui.widget.info.ResultInfo;
import com.github.wallev.maidsoulkitchen.client.gui.widget.info.Zone;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.cook.v1.CookDataV1;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.cook.v1.KitchenData;
import com.github.wallev.maidsoulkitchen.inventory.container.maid.CookConfigContainer;
import com.github.wallev.maidsoulkitchen.network.NetworkHandler;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemDefinition;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec.MKRecipe;
import com.github.wallev.maidsoulkitchen.task.cook.common.task.CookTaskManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.task.TaskCook;
import com.github.wallev.maidsoulkitchen.vhelper.client.chat.VComponent;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.anti_ad.mc.ipn.api.IPNButton;
import org.anti_ad.mc.ipn.api.IPNGuiHint;
import org.anti_ad.mc.ipn.api.IPNPlayerSideOnly;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@IPNPlayerSideOnly
@IPNGuiHint(button = IPNButton.SORT, horizontalOffset = -36, bottom = -12)
@IPNGuiHint(button = IPNButton.SORT_COLUMNS, horizontalOffset = -24, bottom = -24)
@IPNGuiHint(button = IPNButton.SORT_ROWS, horizontalOffset = -12, bottom = -36)
@IPNGuiHint(button = IPNButton.SHOW_EDITOR, horizontalOffset = -5)
@IPNGuiHint(button = IPNButton.SETTINGS, horizontalOffset = -5)
@OnlyIn(Dist.CLIENT)
public class CookConfigGuiV1 extends MaidTaskConfigGui<CookConfigContainer> {
    private static final ResourceLocation TEXTURE = VResourceLocation.create(MaidsoulKitchen.MOD_ID, "textures/gui/cook_guide.png");
    protected final Zone taskDisplay = new Zone(6, 20, 70, 20);
    // 需要特殊处理
    protected final Zone typeDisplay = new Zone(-4, 22, 18, 18);
    protected final Zone searchBoxDisplay = new Zone(-25, 22, 18, 18);
    protected final Zone searchTextDisplay = new Zone(-25, 22, 41, 18);
    protected final Zone resultDisplay = new Zone(6, 44, 152, 86);
    protected final Zone scrollDisplay = new Zone(161, 44, 9, 86);
    protected final ResultInfo recTypeInfo = new ResultInfo(4, 7, 20, 20, 2, 2);
    protected final ResultInfo taskTypeInfo = new ResultInfo(4, 2, 70, 20, 6, 2);
    private final List<MKRecipe<?>> detailRecs = new ArrayList<>();
    private final List<MKRecipe<?>> recipeList = new ArrayList<>();
    private final List<RecButton> parentButtons = new ArrayList<>();
    private final Map<ItemDefinition, List<MKRecipe<?>>> differentResult = new HashMap<>();
    private final Map<ItemDefinition, List<MKRecipe<?>>> flatRecsMap = new HashMap<>();
    private final List<List<MKRecipe<?>>> flatRecs = new ArrayList<>();
    private List<ICookTask<?, ?>> cookTaskList = new ArrayList<>();
    private RecsDetailButton detailButton;
    private EditBox searchBox;
    private KitchenData kitchenData;
    private ResourceLocation kitchenName;
    private CookDataV1 cookData;
    private TaskCook kitchenTask;
    private ICookTask<?, ?> cookTask;
    private boolean initCookData = false;
    private DisplayMode displayMode = DisplayMode.DEFAULT;
    private ResultType resultType = ResultType.COOK_DATA;

    public CookConfigGuiV1(CookConfigContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, VComponent.translatable("gui.maidsoulkitchen.cook_setting_screen.title"));
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void initAdditionData() {
        super.initAdditionData();

        this.initCookData();
        this.initCookTaskList();
        this.initRecipeList();
    }

    private void initCookData() {
        if (!initCookData) {
            this.kitchenTask = (TaskCook) task;
            this.kitchenData = kitchenTask.getTaskData(maid);
            this.initCookData = true;
        }

        this.cookTask = this.getCookTask();
        this.kitchenName = kitchenData.getCookName();
        this.cookData = kitchenData.getCookData(kitchenName);

        if (kitchenName.equals(CookTaskManager.getIdleTask().getUid())) {
            this.resultType = ResultType.TASK;
        }
    }

    private ICookTask<?, ?> getCookTask() {
        ResourceLocation kitchenName = kitchenData.getCookName();
        return CookTaskManager.findTask(kitchenName).orElseGet(CookTaskManager::getIdleTask);
    }

    private void initCookTaskList() {
        if (searchBox != null && StringUtils.isNotBlank(searchBox.getValue())) {
            this.cookTaskList = this.getKitchenTaskV1ListBySearch();
        } else {
            // all cookTasks
            this.cookTaskList = CookTaskManager.getTaskIndex();
        }
    }

    @SuppressWarnings("all")
    private List<ICookTask<?, ?>> getKitchenTaskV1ListBySearch() {
        String search = this.searchBox.getValue().toLowerCase(Locale.US);
        return CookTaskManager.getTaskIndex().stream().filter(iKitchenTaskV1 -> {
            return iKitchenTaskV1.getName().getString().toLowerCase(Locale.US).contains(search);
        }).toList();
    }

    @SuppressWarnings("all")
    private void initRecipeList() {
        this.recipeList.clear();
        this.recipeList.addAll(this.collectRecs());

        this.differentResult.clear();
        this.differentResult.putAll(this.createDifferentResult());
        this.flatRecsMap.clear();
        this.flatRecsMap.putAll(this.collectFlatRecs());
        this.flatRecs.clear();
        this.flatRecs.addAll(flatRecsMap.values());
    }

    private Map<ItemDefinition, List<MKRecipe<?>>> createDifferentResult() {
        return cookTask.getRecipes(maid).stream()
                .collect(Collectors.groupingBy((r) -> {
                    return ItemDefinition.of(r.output());
                }));
    }

    private Map<ItemDefinition, List<MKRecipe<?>>> collectFlatRecs() {
        return this.recipeList.stream().collect(Collectors.groupingBy(r -> {
            return ItemDefinition.of(r.output());
        }));
    }

    @SuppressWarnings("all")
    private List<MKRecipe<?>> collectRecs() {
        switch (displayMode) {
            case CAN_COOK -> {
                return this.getRecsByMode(recipe -> {
                    return cookData.canCook(recipe);
                });
            }
            case NOT_COOK -> {
                return this.getRecsByMode(recipe -> {
                    return !cookData.canCook(recipe);
                });
            }
        }
        return this.getDefaultRecs();
    }

    @SuppressWarnings("all")
    private List<MKRecipe<?>> getRecsByMode(Predicate<Recipe<?>> recipeTest) {
        List<? extends MKRecipe<?>> list = cookTask.getRecipes(maid).stream()
                .filter(recipe -> {
                    return recipeTest.test(recipe.rec());
                }).toList();
        return (List<MKRecipe<?>>) list;
    }

    private void setDisplayMode(DisplayMode mode) {
        this.displayMode = mode;
    }

    @SuppressWarnings("all")
    private List<MKRecipe<?>> getDefaultRecs() {
        List<? extends MKRecipe<?>> allRecipe = cookTask.getRecipes(maid);
        if (searchBox != null && StringUtils.isNotBlank(searchBox.getValue())) {
            String search = this.searchBox.getValue().toLowerCase(Locale.US);
            List<? extends MKRecipe<?>> list = allRecipe.stream()
                    .filter(recipe -> {
                        return recipe.output().getDisplayName().getString().toLowerCase(Locale.US).contains(search);
                    }).toList();

            return (List<MKRecipe<?>>) list;
        } else {
            return (List<MKRecipe<?>>) allRecipe;
        }
    }

    @Override
    protected void initAdditionWidgets() {
        super.initAdditionWidgets();
        this.addTaskInfoButton();
        this.addSearchTextBox();
        this.addSearchBox();
        this.addTypeButton();
        this.initResultButton();
        this.addScrollButton();

        this.addInfoButton();
        this.addJeiButton();

        if (!detailRecs.isEmpty()) {
            parentButtons.forEach(b -> {
                b.active = false;
            });
        }
    }

    private void initResultButton() {
        // @fixme： 烹饪任务的uid还没想好命名方式，先这样修正把
        boolean isValidTask = false;
        for (ResourceLocation recl : CookTaskManager.getTaskMap().keySet()) {
            if (recl.equals(this.kitchenName)) {
                isValidTask = true;
                break;
            }
        }

        if (!isValidTask || CookTaskManager.getIdleTask().getUid().equals(this.kitchenName)) {
            this.resultType = ResultType.TASK;
        }


        switch (resultType) {
            case TASK -> addTypeInfoButton();
            case COOK_DATA -> addResultInfo();
        }
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
    protected void renderAdditionTransTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        int startX = width - leftPos - (-searchBoxDisplay.startX()) - searchBoxDisplay.width() - 1;
        int startY = visualZone.startY() + searchBoxDisplay.startY();

        if (searchBox.isVisible()) {
            startX -= searchTextDisplay.width();
        }

        int finalStartX = startX;

        boolean isCookSettingMainZone = mouseX >= startX && mouseY >= startY && mouseX < startX + searchBoxDisplay.width() && mouseY < startY + searchBoxDisplay.height();
        if (isCookSettingMainZone) {
            graphics.renderComponentTooltip(this.font, this.getDisplayModeTooltips(), mouseX, mouseY);
        }

        this.setHoverSlot();
    }

    private void setHoverSlot() {
        parentButtons.forEach(b -> {
            if (b.isTooltipHovered()) {
                this.hoveredSlot = b.virtualSlot;
            }
        });

        if (detailButton != null && !detailRecs.isEmpty()) {
            detailButton.getRecsButtons().forEach(b -> {
                if (b.isTooltipHovered()) {
                    this.hoveredSlot = b.virtualSlot;
                }
            });
        }
    }

    private List<Component> getDisplayModeTooltips() {
        List<Component> components = Lists.newArrayList(VComponent.translatable("gui.maidsoulkitchen.btn.display.tooltip.1"),
                VComponent.translatable("gui.maidsoulkitchen.btn.display.tooltip.2"));
        for (DisplayMode value : DisplayMode.values()) {

            MutableComponent component = value.getComponent(displayMode);
            int size = 0;
            switch (value) {
                case CAN_COOK -> {
                    size = this.getRecsByMode2DisplayTooltip((r) -> cookData.canCook(r)).size();
                }
                case NOT_COOK -> {
                    size = this.getRecsByMode2DisplayTooltip((r) -> !cookData.canCook(r)).size();
                }
                case DEFAULT -> {
                    size = this.differentResult.size();
                }

            }
            components.add(component.append("(" + size + ")"));
        }
        return components;
    }

    private List<List<MKRecipe<?>>> getRecsByMode2DisplayTooltip(Predicate<Recipe<?>> recipeTest) {
        return this.differentResult.values().stream()
                .filter(recs -> {
                    for (MKRecipe<?> rec : recs) {
                        if (recipeTest.test(rec.rec())) {
                            return true;
                        }
                    }
                    return false;
                })
                .toList();
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (detailButton != null && detailButton.needActive()) {
            return false;
        }

        if (super.mouseScrolled(mouseX, mouseY, delta)) {
            return true;
        }

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
            if (delta < 0 && solIndex < this.getScrolledSize()) {
                solIndex++;
                this.init();
                return true;
            }
        }

        return false;
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
                this.displayMode = DisplayMode.DEFAULT;
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
                this.displayMode = DisplayMode.DEFAULT;
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

    private void addInfoButton() {
        if (cookTask.getWarnComponent().isEmpty()) return;
        TImageButton infoButton = new TImageButton(cookTask, visualZone.startX() + visualZone.width() - 15, visualZone.startY() + 5, 9, 9, 237, 212, 10, TEXTURE, (b) -> {
        });
        this.addRenderableWidget(infoButton);
    }

    private void addJeiButton() {
//        ImageButton jeiButton = new ImageButton(visualZone.startX() + visualZone.width() - 12, visualZone.startY() + 8, 9, 9, 247, 212, 10, TEXTURE, (b) -> {
//        }) {
//
//        };
//        this.addRenderableWidget(jeiButton);
    }

    private void addTypeInfoButton() {
        final int offsetX = 4;
        int startX = visualZone.startX() + resultDisplay.startX();
        int startY = visualZone.startY() + resultDisplay.startY();

        int index = solIndex * (taskTypeInfo.row() * taskTypeInfo.col());
        for (int row = 0; row < taskTypeInfo.row(); row++) {
            for (int col = 0; col < taskTypeInfo.col(); col++) {
                if (index >= cookTaskList.size()) {
                    return;
                }

                ICookTask<?, ?> kitchenTaskV2 = cookTaskList.get(index++);
                int x = startX + (taskTypeInfo.rowWidth() + taskTypeInfo.rowSpacing()) * col;
                int y = startY + (taskTypeInfo.colHeight() + taskTypeInfo.colSpacing()) * row;
                TypeTaskButton typeTaskButton = new TypeTaskButton(offsetX + x, y, taskTypeInfo.rowWidth(), taskTypeInfo.colHeight(), kitchenTaskV2, b -> {
                    kitchenData.setCookName(kitchenTaskV2.getUid());
                    this.solIndex = 0;
                    this.searchBox.setValue("");
                    this.switchResultType();
                    this.init();
                    this.sendToServer();
                });
                this.addRenderableWidget(typeTaskButton);
            }
        }
    }

    private void addTaskInfoButton() {
        int startX = visualZone.startX() + taskDisplay.startX();
        int startY = visualZone.startY() + taskDisplay.startY();
        TaskInfoButton taskInfoButton = new TaskInfoButton(startX, startY, taskDisplay.width(), taskDisplay.height(), this.cookTask, this.kitchenData, b -> {
            this.solIndex = 0;
            this.searchBox.setValue("");
            this.switchResultType();
            this.init();
        });
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

            @Override
            public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
                boolean isCookSettingMainZone = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.getWidth() && mouseY < this.getY() + this.getHeight();
                if (delta != 0 && isCookSettingMainZone) {
                    // 向上滚
                    if (delta > 0) {
                        setDisplayMode(displayMode.pre());
                        solIndex = 0;
                        init();
                        return true;
                    }
                    // 向下滚
                    if (delta < 0) {
                        setDisplayMode(displayMode.next());
                        solIndex = 0;
                        init();
                        return true;
                    }
                }
                return false;
            }
        };
        this.addRenderableWidget(typeButton);
    }

    private void addTypeButton() {
        int startX = width - leftPos - (-typeDisplay.startX()) - typeDisplay.width() - 1;
        int startY = visualZone.startY() + typeDisplay.startY();

        TypeButton typeButton = new TypeButton(startX, startY, typeDisplay.width(), typeDisplay.height(), cookData.mode().equals(CookDataV1.Mode.WHITELIST.name)) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                setAndSyncMode(!isSelected);
                this.toggleState();
                displayMode = DisplayMode.DEFAULT;
                init();
            }
        };
        this.addRenderableWidget(typeButton);
    }

    private void setAndSyncMode(String mode) {
        cookData.setMode(mode);
        this.sendToServer();
//        NetworkHandler.C2S.setCookDataMode(maid.getId(), kitchenTask.getCookDataKey().getKey(), mode);
    }

    private void setAndSyncMode(boolean isSelected) {
        setAndSyncMode(isSelected ? CookDataV1.Mode.WHITELIST.name : CookDataV1.Mode.BLACKLIST.name);
    }

    private void addResultInfo() {
        parentButtons.clear();

        int startX = visualZone.startX() + resultDisplay.startX();
        int startY = visualZone.startY() + resultDisplay.startY();

        {
            int w = recTypeInfo.col() * recTypeInfo.rowWidth() + recTypeInfo.rowSpacing() * (recTypeInfo.col() - 1);
            int h = recTypeInfo.row() * recTypeInfo.colHeight() + recTypeInfo.colSpacing() * (recTypeInfo.row() - 1);
            int x = startX;
            int y = startY;

            detailButton = new RecsDetailButton(x, y, w, h, maid, cookTask, cookData) {
                @Override
                public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
                    if (!(this.active && this.visible)) {
                        return false;
                    }

                    if (!this.superClicked(pMouseX, pMouseY)) {
                        detailRecs.clear();
                        this.setCanAction(false);

                        parentButtons.forEach(b -> {
                            b.active = true;
                        });
                        return true;
                    }

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
            };

            if (!detailRecs.isEmpty()) {
                detailButton.setRecs(detailRecs);
                detailButton.setCanAction(true);

                parentButtons.forEach(b -> {
                    b.active = false;
                });
            }
        }

        {
            int index = solIndex * (recTypeInfo.row() * recTypeInfo.col());
            breakLable:
            for (int row = 0; row < recTypeInfo.row(); row++) {
                for (int col = 0; col < recTypeInfo.col(); col++) {
                    if (index >= this.flatRecs.size()) {
                        break breakLable;
                    }

                    List<MKRecipe<?>> recipes = flatRecs.get(index++);
                    int x = startX + (recTypeInfo.rowWidth() + recTypeInfo.rowSpacing()) * col;
                    int y = startY + (recTypeInfo.colHeight() + recTypeInfo.colSpacing()) * row;

                    RecButton recButton = new RecButton(maid, cookTask, cookData, recipes, x, y) {
                        @Override
                        public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
                            if (!(this.active && this.visible)) {
                                return false;
                            }

                            if (pButton == 1) {
                                if (this.superClicked(pMouseX, pMouseY) && this.debugClicked()) {
                                    return false;
                                }

                                if (this.superClicked(pMouseX, pMouseY)) {
                                    this.playDownSound(Minecraft.getInstance().getSoundManager());

                                    detailRecs.clear();
                                    detailRecs.addAll(this.recipes);
                                    detailButton.setRecs(this.recipes);
                                    detailButton.setCanAction(true);

                                    parentButtons.forEach(b -> {
                                        b.active = false;
                                    });
                                    return true;
                                }

                                return false;
                            }

                            return super.mouseClicked(pMouseX, pMouseY, pButton);
                        }

                        @Override
                        protected void arAndSyncRec() {
                            super.arAndSyncRec();
                            sendToServer();
                        }
                    };
                    parentButtons.add(recButton);
                }
            }
            parentButtons.forEach(this::addRenderableWidget);
        }

        this.addRenderableWidget(detailButton);
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int x, int y) {
        super.renderTooltip(graphics, x, y);
    }

    // 161, 25 189, 74
    private void addScrollButton() {
        int startX = visualZone.startX() + scrollDisplay.startX();
        int startY = visualZone.startY() + scrollDisplay.startY();
        Button upButton = new ImageButton(startX, startY, 9, 7, 199, 74, 14, TEXTURE, b -> {
            if (this.solIndex > 0) {
                this.solIndex--;
                this.init();
            }
        });
        Button downButton = new ImageButton(startX, startY + 8 + 1 + 70, 9, 7, 208, 74, 14, TEXTURE, b -> {
            if (this.solIndex < (this.flatRecs.size() - 1) / (recTypeInfo.col() * recTypeInfo.row())) {
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
            graphics.drawString(font, VComponent.translatable("gui.maidsoulkitchen.search").withStyle(ChatFormatting.ITALIC), startX + 3, startY + 5, 0XF5F5F5);
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
        if (this.getScrolledSize() >= 1) {
            graphics.blit(TEXTURE, startX, startY + (int) ((70 - 2 - 9) * getCurrentScroll()), 199, 64, 7, 9);
        } else {
            graphics.blit(TEXTURE, startX, startY, 206, 64, 7, 9);
        }
    }

    private float getCurrentScroll() {
        return Mth.clamp((float) (solIndex * (1.0 / this.getScrolledSize())), 0, 1);
    }

    private int getScrolledSize() {
        switch (resultType) {
            case TASK -> {
                return (this.cookTaskList.size() - 1) / (taskTypeInfo.col() * taskTypeInfo.row());
            }
            case COOK_DATA -> {
                return (this.flatRecsMap.size() - 1) / (recTypeInfo.col() * recTypeInfo.row());
            }
            default -> {
                return 1;
            }
        }
    }

    private void switchResultType() {
        switch (resultType) {
            case TASK -> resultType = ResultType.COOK_DATA;
            case COOK_DATA -> resultType = ResultType.TASK;
        }
    }

    private void sendToServer() {
        NetworkHandler.C2S.syncKitchenData2(maid.getId(), kitchenData);
    }

    public enum DisplayMode {
        DEFAULT,
        CAN_COOK,
        NOT_COOK,
        ;

        DisplayMode() {
        }

        public MutableComponent getComponent() {
            return VComponent.translatable("gui.maidsoulkitchen.btn.display.mode." + this.name().toLowerCase(Locale.ROOT));
        }

        public MutableComponent getComponent(DisplayMode mode) {
            MutableComponent component1 = this.getComponent();
            if (mode == this) {
                return component1.withStyle(ChatFormatting.DARK_GREEN);
            } else {
                return component1.withStyle(ChatFormatting.GRAY);
            }
        }

        public DisplayMode next() {
            DisplayMode[] modes = values();
            return modes[(this.ordinal() + 1) % modes.length];
        }

        public DisplayMode pre() {
            DisplayMode[] modes = values();
            return modes[(this.ordinal() - 1 + modes.length) % modes.length];
        }
    }

    public enum ResultType {
        TASK,
        COOK_DATA,
        ;
    }
}
