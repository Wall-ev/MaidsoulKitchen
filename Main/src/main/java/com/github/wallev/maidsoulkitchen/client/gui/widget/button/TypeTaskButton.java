package com.github.wallev.maidsoulkitchen.client.gui.widget.button;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TypeTaskButton extends NormalTooltipButton {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MaidsoulKitchen.MOD_ID, "textures/gui/cook_guide.png");

    private final ICookTask<?, ?> cookTask;
    private final ItemStack icon;

    public TypeTaskButton(int pX, int pY, int pWidth, int pHeight, ICookTask<?, ?> cookTask, OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, cookTask.getName(), getDesc(cookTask), pOnPress);
        this.cookTask = cookTask;
        this.icon = cookTask.getIcon();
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft mc = Minecraft.getInstance();
        pGuiGraphics.blit(TEXTURE, this.getX(), this.getY(), 179, 2, this.width, this.height);
        pGuiGraphics.renderItem(icon, this.getX() + 2, this.getY() + 2);
        List<FormattedCharSequence> splitTexts = mc.font.split(this.getMessage(), 42);
        if (!splitTexts.isEmpty()) {
            pGuiGraphics.drawString(mc.font, splitTexts.get(0), this.getX() + 22, this.getY() + 5, 0xffffff, false);
        }
    }

    protected void renderScrollingTaskString(GuiGraphics pGuiGraphics, Font pFont, int x, int y, int pWidth, int pColor) {
        renderScrollingString(pGuiGraphics, pFont, this.getMessage(), x, y, x + pWidth, y + pFont.lineHeight, pColor);
    }

    private static List<Component> getDesc(ICookTask<?, ?> task) {
        List<Component> components = new ArrayList<>();
        components.add(task.getIcon().getHoverName());
        components.addAll(task.getDescription());

        String typeString = task.getRecipeTypeId();
        components.add(CommonComponents.SPACE);
        components.add(Component.translatable("gui.maidsoulkitchen.widget.cook_guide.task.recipe_type", typeString).withStyle(ChatFormatting.DARK_GRAY));

        return components;
    }
}
