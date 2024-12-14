package com.github.wallev.farmsoulkitchen.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.api.client.gui.ITooltipButton;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import com.github.wallev.farmsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.wallev.farmsoulkitchen.config.subconfig.TaskConfig;
import com.github.wallev.farmsoulkitchen.entity.data.inner.task.CookData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;
import java.util.Optional;

public class RecButton extends StateSwitchingButton implements ITooltipButton {
    private static final ResourceLocation TEXTURE = new ResourceLocation(FarmsoulKitchen.MOD_ID, "textures/gui/cook_guide.png");
    private final EntityMaid maid;
    private final ICookTask<?, ?> cookTask;
    private final CookData cookData;
    private final Recipe<?> recipe;
    private final ItemStack stack;

    @SuppressWarnings("all")
    public RecButton(EntityMaid maid, ICookTask<?, ?> cookTask, CookData cookData, Recipe<?> recipe, int pX, int pY) {
        super(pX, pY, 20, 20, cookData.getRecs().contains(recipe.getId().toString()));
        this.initTextureValues(179, 25, 22, 0, TEXTURE);
        this.maid = maid;
        this.cookTask = cookTask;
        this.recipe = recipe;
        this.cookData = cookData;
        this.stack = cookTask.getResultItem(recipe, Minecraft.getInstance().level.registryAccess());
    }

    public void toggleState() {
        this.isStateTriggered = !this.isStateTriggered;
        this.active = true;
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        pGuiGraphics.renderItem(stack, this.getX() + 2, this.getY() + 2);
        this.renderShadow(pGuiGraphics);
    }

    private void renderShadow(GuiGraphics graphics) {
        if (cookData.mode().equals(CookData.Mode.WHITELIST.name)) {
            graphics.fill(this.getX(), this.getY(), this.getX() + 20, this.getY() + 20, 0x50F9F9F9);
        } else {
            graphics.fill(this.getX(), this.getY(), this.getX() + 20, this.getY() + 20, 0x50000010);
        }
    }

    @Override
    public boolean isTooltipHovered() {
        return this.isHovered();
    }

    @Override
    public void renderTooltip(GuiGraphics guiGraphics, Minecraft minecraft, int pMouseX, int pMouseY) {
        this.renderItemStackTooltips(minecraft, guiGraphics, pMouseX, pMouseY);
    }

    @SuppressWarnings("all")
    private void renderItemStackTooltips(Minecraft mc, GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        renderTooltipWithImage(stack, mc, pGuiGraphics, pMouseX, pMouseY);
    }

    private void renderTooltipWithImage(ItemStack stack, Minecraft mc, GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        List<Component> stackTooltip = Screen.getTooltipFromItem(mc, stack);

        boolean modeRandom = !cookData.mode().equals(CookData.Mode.WHITELIST.name);
//        boolean overSize = cookData.getRecs().size() >= TaskConfig.COOK_SELECTED_RECIPES.get();

//        Optional<TooltipComponent> recClientAmountTooltip = cookTask.getRecClientAmountTooltip(recipe, modeRandom, overSize);
        Optional<TooltipComponent> recClientAmountTooltip = cookTask.getRecClientAmountTooltip(recipe, modeRandom, false);

        pGuiGraphics.renderTooltip(mc.font, stackTooltip, recClientAmountTooltip, stack, pMouseX, pMouseY);
    }

    public Recipe<?> getRecipe() {
        return recipe;
    }
}
