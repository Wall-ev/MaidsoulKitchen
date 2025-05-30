package com.github.wallev.maidsoulkitchen.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.api.client.gui.ITooltipButton;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.verhelper.client.chat.VComponent;
import com.github.wallev.verhelper.client.resources.VResourceLocation;
import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class RecButton extends StateSwitchingButton implements ITooltipButton {
    private static final ResourceLocation TEXTURE = VResourceLocation.create(MaidsoulKitchen.MOD_ID, "textures/gui/cook_guide.png");
    private final EntityMaid maid;
    private final ICookTask<?, ?> cookTask;
    private final CookData cookData;
    private final Recipe<?> recipe;
    private final ItemStack stack;
    // 构建虚拟 Slot, 用于支持 jei、rei、emi 等配方管理器查询成品相关信息.
    protected final VirtualSlot virtualSlot;

    @SuppressWarnings("all")
    public RecButton(EntityMaid maid, ICookTask<?, ?> cookTask, CookData cookData, Recipe<?> recipe, int pX, int pY) {
        super(pX, pY, 20, 20, cookData.getRecs().contains(recipe.getId().toString()));
        this.initTextureValues(179, 25, 22, 0, TEXTURE);
        this.maid = maid;
        this.cookTask = cookTask;
        this.recipe = recipe;
        this.cookData = cookData;
        this.stack = cookTask.getResultItem(recipe, maid.level.registryAccess());
        this.virtualSlot = new VirtualSlot(stack);
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
/**
 *         if (!shiftDown) {
 *             MutableComponent name = VComponent.empty().append(stack.getHoverName()).withStyle(stack.getRarity().getStyleModifier());
 *             textTooltips.add(name);
 *             textTooltips.add(VComponent.literal("Hold [Shift] to read more!").withStyle(ChatFormatting.DARK_GRAY));
 *             if (advancedItemTooltips) {
 *                 stackTooltip.add(CommonComponents.SPACE);
 *                 textTooltips.add(VComponent.literal("ResultItemId: " + ForgeRegistries.ITEMS.getKey(stack.getItem()).toString()).withStyle(ChatFormatting.DARK_GRAY));
 *                 textTooltips.add(VComponent.literal(String.format("RecipeId: %s", recipe.getId())).withStyle(ChatFormatting.DARK_GRAY));
 *             }
 *
 *             String modId = stack.getItem().getCreatorModId(stack);
 *             String modContainer = ModList.get().getModContainerById(modId).get().getModInfo().getDisplayName();
 *             textTooltips.add(VComponent.literal(modContainer).withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.ITALIC));
 *         } else {
 *             textTooltips.addAll(stackTooltip);
 *             if (advancedItemTooltips) {
 *                 textTooltips.add(Component.literal(String.format("RecipeId: %s", recipe.getId())).withStyle(ChatFormatting.DARK_GRAY));
 *             }
 *         }
 */
        if (mc.options.advancedItemTooltips) {
            stackTooltip.add(stackTooltip.size() - 1, VComponent.literal(String.format("RecipeId: %s", recipe.getId())).withStyle(ChatFormatting.DARK_GRAY));
        }

        boolean modeRandom = !cookData.mode().equals(CookData.Mode.WHITELIST.name);
        Optional<TooltipComponent> recClientAmountTooltip = cookTask.getRecClientAmountTooltip(recipe, modeRandom, false, cookData, maid);

        pGuiGraphics.renderTooltip(mc.font, stackTooltip, recClientAmountTooltip, stack, pMouseX, pMouseY);
    }

    public Recipe<?> getRecipe() {
        return recipe;
    }

    public static class VirtualSlot extends Slot {
        private static final Container EMPTY_INV = new SimpleContainer(0);
        private final ItemStack result;
        public VirtualSlot(ItemStack result) {
            super(EMPTY_INV, 0, 0, 0);
            this.result = result;
        }

        @Override
        public ItemStack getItem() {
            return result;
        }

        @Override
        public boolean isActive() {
            return false;
        }

        @Override
        public boolean mayPickup(@NotNull Player playerIn) {
            return false;
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return false;
        }

        @Override
        public boolean isHighlightable() {
            return false;
        }

        @Override
        public boolean hasItem() {
            return false;
        }

        @Override
        public boolean allowModification(@NotNull Player pPlayer) {
            return false;
        }
    }
}
