package com.github.catbert.tlma.client.gui.entity.maid.cook;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.api.IAddonMaid;
import com.github.catbert.tlma.api.task.v1.cook.ICookTask;
import com.github.catbert.tlma.client.gui.widget.button.*;
import com.github.catbert.tlma.entity.passive.CookTaskData;
import com.github.catbert.tlma.inventory.container.ClientTaskSettingMenuManager;
import com.github.catbert.tlma.inventory.container.CookSettingContainer;
import com.github.catbert.tlma.inventory.tooltip.AmountTooltip;
import com.github.catbert.tlma.network.NetworkHandler;
import com.github.catbert.tlma.network.message.MaidTaskRecMessage;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
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
public class CookSettingContainerGui extends AbstractMaidContainerGui<CookSettingContainer> implements ICookContainerGui, IAddonAbstractMaidContainerGui {

    private static final ResourceLocation TEXTURE = new ResourceLocation(TLMAddon.MOD_ID, "textures/gui/cook_guide.png");
    private final Zone taskDisplay = new Zone(6, 20, 70, 20);
    // 需要特殊处理
    private final Zone actionDisplay = new Zone(-5, 27, 8, 13);
    private final Zone resultDisplay = new Zone(6, 44, 152, 86);
    private final Zone scrollDisplay = new Zone(161, 44, 9, 86);
    private final ResultInfo ref = new ResultInfo(4, 7, 20, 20, 2, 2);
    private final int titleStartY = 8;
    private final EntityMaid maid;
    private final CompoundTag cookCompound;
    private final CookTaskData cookTaskData;
    private final List<ItemStack> resultStackList = new ArrayList<>();
    @SuppressWarnings("all")
    private final List<Recipe> recipeList = new ArrayList<>();
    private final List<String> selectRecs = new ArrayList<>();
    private Zone visualZone;
    private int solIndex = 0;
    private IMaidTask currentTask;
    private ItemStack lastResultTooltipStack = ItemStack.EMPTY;
    private List<Ingredient> lastIngreTooltipList = new ArrayList<>();

    public CookSettingContainerGui(CookSettingContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.maid = getMenu().getMaid();
        this.cookCompound = maid.level().isClientSide ? ClientTaskSettingMenuManager.getMenuData() : maid.getPersistentData();
        this.cookTaskData = maid.level().isClientSide ? ClientTaskSettingMenuManager.getCookTaskData() : ((IAddonMaid) maid).getCookTaskData1();
    }

    @Override
    protected void init() {
        this.init(this.maid.getTask());
    }

    @Override
    public void init(IMaidTask task) {
        super.init();
        if (getMaid() != null) {
            this.refreshInfo(task);
            this.guiCoordInfoInit();

            this.addTitleInfoButton();
            this.addTaskInfoButton();
            this.addTypeButton();
            this.addScrollButton();
            this.addResultStackButton();
        }
    }

    public void refreshInfo(IMaidTask task) {
        if (this.currentTask != task) {
            this.currentTask = task;
            this.solIndex = 0;
            this.selectRecs.clear();
            this.selectRecs.addAll(((IAddonMaid) this.maid).getCookTaskData1()
                    .getTaskRule(this.currentTask.getUid().toString())
                    .getRecipeIds());
            this.recipeInfoInit();
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.maid != null) {
            this.drawScrollInfoBar(graphics);
        }
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderTooltip(graphics, mouseX, mouseY);
        this.renderItemStackTooltips(getMinecraft(), graphics, mouseX, mouseY);
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
            if (delta < 0 && solIndex < (this.resultStackList.size() - 1) / (ref.col() * ref.row())) {
                solIndex++;
                this.init();
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }


    private void guiCoordInfoInit() {
        visualZone = new Zone(leftPos + 81, topPos + 28, 176, 137);
    }

    @SuppressWarnings("all")
    private void recipeInfoInit() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && this.currentTask instanceof ICookTask<?, ?> task) {
            List<Recipe> allRecipesFor = (List<Recipe>) task.getRecipes(mc.level);
            this.recipeList.clear();
            this.recipeList.addAll(allRecipesFor);
            this.resultStackList.clear();
            this.resultStackList.addAll(allRecipesFor.stream().map(recipe -> recipe.getResultItem(mc.level.registryAccess())).toList());
        }
    }


    private void addTitleInfoButton() {
        MutableComponent titleComponent = Component.translatable("gui.touhou_little_maid_addon.cook_setting_screen.title");
        int titleStartX = visualZone.startX() + (visualZone.width() - font.width(titleComponent)) / 2;
        TitleInfoButton titleInfoButton = new TitleInfoButton(titleStartX, visualZone.startY() + titleStartY, font.width(titleComponent), 9, titleComponent);
        this.addRenderableWidget(titleInfoButton);
    }

    private void addTaskInfoButton() {
        int startX = visualZone.startX() + taskDisplay.startX();
        int startY = visualZone.startY() + taskDisplay.startY();
        TaskInfoButton taskInfoButton = new TaskInfoButton(startX, startY, taskDisplay.width(), taskDisplay.height(), this.currentTask);
        this.addRenderableWidget(taskInfoButton);
    }

    private void addTypeButton() {
        MutableComponent randomComponent = Component.translatable("gui.touhou_little_maid_addon.btn.cook_guide.type.random");
        List<Component> randomTooltip = List.of(Component.translatable("gui.touhou_little_maid_addon.btn.cook_guide.type.random.desc"));
        int randomButtonWidth = font.width(randomComponent) + (actionDisplay.width() / 2);
        MutableComponent selectedComponent = Component.translatable("gui.touhou_little_maid_addon.btn.cook_guide.type.selected");
        List<Component> selectedTooltip = List.of(Component.translatable("gui.touhou_little_maid_addon.btn.cook_guide.type.selected.desc"));
        int selectedButtonWidth = font.width(selectedComponent) + (actionDisplay.width() / 2);

        int startX = width - leftPos - (-actionDisplay.startX()) - randomButtonWidth - selectedButtonWidth;
        int startY = visualZone.startY() + actionDisplay.startY();

        NormalTooltipButton randomBtn = new NormalTooltipButton(startX, startY, randomButtonWidth, actionDisplay.height(), randomComponent, randomTooltip, b -> {
        });
        NormalTooltipButton selectedBtn = new NormalTooltipButton(startX + selectedButtonWidth, startY, selectedButtonWidth, actionDisplay.height(), selectedComponent, selectedTooltip, b -> {
        });

        this.addRenderableWidget(randomBtn);
        this.addRenderableWidget(selectedBtn);
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
            if (this.solIndex < (this.resultStackList.size() - 1) / (ref.col() * ref.row())) {
                this.solIndex++;
                this.init();
            }
        });
        this.addRenderableWidget(upButton);
        this.addRenderableWidget(downButton);
    }

    //6, 25
    protected void addResultStackButton() {

        int startX = visualZone.startX() + resultDisplay.startX();
        int startY = visualZone.startY() + resultDisplay.startY();

        ResultButton resultButton = new ResultButton(new Zone(startX, startY, resultDisplay.width(), resultDisplay.height()), ref) {
            @Override
            protected ItemStack getItemStack(int index) {
                return CookSettingContainerGui.this.getItemStack(index);
            }

            @Override
            protected boolean isSelectedRec(int index) {
                int actualIndex = getActualIndex(index);
                if (actualIndex < 999) {
                    String recipeId = recipeList.get(actualIndex).getId().toString();
//                return ((IAddonMaid) serverMaid).containsRecipe2(recipeId);
//                return CookSettingContainerGui.this.containsRecipe2(recipeId);
                    return CookSettingContainerGui.this.containsRecipe1(recipeId);
                }
                return false;
            }

            @Override
            protected void recAddOrRemove(int index) {
                int actualIndex = getActualIndex(index);
                if (actualIndex < 999) {
                    String recipeId = recipeList.get(actualIndex).getId().toString();
//                    CookSettingContainerGui.this.removeOrAddRec(recipeId);
                    NetworkHandler.CHANNEL.sendToServer(new MaidTaskRecMessage(maid.getId(), recipeId));
                }
            }
        };

        this.addRenderableWidget(resultButton);
    }

    private void drawScrollInfoBar(GuiGraphics graphics) {
        int startX = visualZone.startX() + scrollDisplay.startX();
        int startY = visualZone.startY() + scrollDisplay.startY();
        graphics.blit(TEXTURE, startX, startY + 8, 189, 64, 9, 70);
        drawScrollIndicator(graphics, startX + 1, startY + 8 + 1);
    }

    private void drawScrollIndicator(GuiGraphics graphics, int startX, int startY) {
        if ((this.resultStackList.size() - 1) / (ref.col() * ref.row()) > 1) {
            graphics.blit(TEXTURE, startX, startY + (int) ((70 - 11) * getCurrentScroll()), 199, 64, 7, 9);
        } else {
            graphics.blit(TEXTURE, startX, startY, 206, 64, 7, 9);
        }
    }


    private void renderItemStackTooltips(Minecraft mc, GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        int startX = visualZone.startX() + resultDisplay.startX();
        int startY = visualZone.startY() + resultDisplay.startY();

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

        List<Ingredient> ingres = getIngre(stack);
        Optional<TooltipComponent> itemContainerTooltip = ingres.isEmpty() ? Optional.empty() : Optional.of(new AmountTooltip(ingres));

        pGuiGraphics.renderTooltip(mc.font, stackTooltip, itemContainerTooltip, pMouseX, pMouseY);
    }

    // todo
    @SuppressWarnings("all")
    private List<Ingredient> getIngre(ItemStack stack) {
        Item item = stack.getItem();

        if (this.lastResultTooltipStack.is(item)) {
            return this.lastIngreTooltipList;
        }

        Optional<Recipe> first = this.recipeList.stream()
                .filter(r -> r.getResultItem(getMinecraft().level.registryAccess()).is(item))
                .findFirst();
        if (first.isEmpty()) {
            return Collections.emptyList();
        }

        this.lastResultTooltipStack = stack;
        this.lastIngreTooltipList = first.get().getIngredients();
        return this.lastIngreTooltipList;
    }

    private int checkCoordinate2(double pMouseX, double pMouseY, int startX, int startY) {
        if (pMouseX < startX || pMouseY < startY) return -1;

        int offsetRow = (int) (pMouseX - startX);
        int offsetCol = (int) (pMouseY - startY);

        if (offsetRow % (ref.rowWidth() + ref.rowSpacing()) < ref.rowWidth() && offsetCol % (ref.colHeight() + ref.colSpacing()) < ref.colHeight()) {
            int blockCol = offsetRow / (ref.rowWidth() + ref.rowSpacing());
            int blockRow = offsetCol / (ref.colHeight() + ref.colSpacing());

            if (blockRow >= 0 && blockRow < ref.row() && blockCol >= 0 && blockCol < ref.col()) {
                int blockIndex = blockRow * ref.col() + blockCol;

                if (blockIndex >= 0 && blockIndex < ref.col() * ref.row()) {
                    return blockIndex;
                }
            }
        }
        return -1;

    }

    private ItemStack getItemStack(int index) {
        int i = getActualIndex(index);
        if (i < resultStackList.size()) {
            return resultStackList.get(i);
        }
        return ItemStack.EMPTY;
    }

    private int getActualIndex(int virtuallyIndex) {
        int i = virtuallyIndex + (ref.row() * ref.col()) * solIndex;
        if (i < resultStackList.size()) {
            return i;
        }
        return 999;
    }

    private float getCurrentScroll() {
        return Mth.clamp((float) (solIndex * (1.0 / ((this.resultStackList.size() - 1) / (ref.col() * ref.row())))), 0, 1);
    }

    private void removeOrAddRec(String recipeId) {
        if (this.selectRecs.contains(recipeId)) {
            this.selectRecs.remove(recipeId);
        } else {
            this.selectRecs.add(recipeId);
        }
    }

    public boolean containsRecipe2(String recipeId) {
        CompoundTag compound = this.cookCompound.getCompound(this.currentTask.getUid().toString());
        ListTag list = compound.getList("recipe", Tag.TAG_STRING);
        return list.contains(StringTag.valueOf(recipeId));
    }

    public boolean containsRecipe1(String recipeId) {
        return this.cookTaskData.getTaskRule(this.currentTask.getUid().toString()).getRecipeIds().contains(recipeId);
    }
}
