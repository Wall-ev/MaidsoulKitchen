package com.github.wallev.maidsoulkitchen.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.api.client.gui.ITooltipButton;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.handler.VComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
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
    private static final ResourceLocation TEXTURE = new ResourceLocation(MaidsoulKitchen.MOD_ID, "textures/gui/cook_guide.png");
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
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float pPartialTick) {
        super.renderButton(poseStack, mouseX, mouseY, pPartialTick);
        Minecraft.getInstance().getItemRenderer().renderGuiItem(stack, x + 2, y + 2);
        this.renderShadow(poseStack);
    }

    private void renderShadow(PoseStack poseStack) {
        if (cookData.mode().equals(CookData.Mode.WHITELIST.name)) {
            fill(poseStack, x, y, x + 20, y + 20, 0x50F9F9F9);
        } else {
            fill(poseStack, x, y, x + 20, y + 20, 0x50000010);
        }
    }

    @Override
    public boolean isTooltipHovered() {
        return this.isHovered;
    }

    @Override
    public void renderTooltip(PoseStack poseStack, Minecraft minecraft, int pMouseX, int pMouseY) {
        this.renderItemStackTooltips(minecraft, poseStack, pMouseX, pMouseY);
    }

    @SuppressWarnings("all")
    private void renderItemStackTooltips(Minecraft mc, PoseStack poseStack, int pMouseX, int pMouseY) {
        renderTooltipWithImage(stack, mc, poseStack, pMouseX, pMouseY);
    }

    private void renderTooltipWithImage(ItemStack stack, Minecraft mc, PoseStack poseStack, int pMouseX, int pMouseY) {
        Screen screen = mc.screen;
        if (screen == null) {
            return;
        }

        List<Component> stackTooltip = screen.getTooltipFromItem(stack);

        if (mc.options.advancedItemTooltips) {
            stackTooltip.add(VComponent.literal(" "));
            stackTooltip.add(VComponent.literal(String.format("RecipeId: %s", recipe.getId())).withStyle(ChatFormatting.DARK_GRAY));
        }

        boolean modeRandom = !cookData.mode().equals(CookData.Mode.WHITELIST.name);
        Optional<TooltipComponent> recClientAmountTooltip = cookTask.getRecClientAmountTooltip(recipe, modeRandom, false);

        screen.renderTooltip(poseStack, stackTooltip, recClientAmountTooltip, pMouseX, pMouseY, stack);
    }

    public Recipe<?> getRecipe() {
        return recipe;
    }
}
