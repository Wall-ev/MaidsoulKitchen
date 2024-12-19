package com.github.wallev.maidsoulkitchen.client.gui.widget.button;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;

// 防止覆盖tooltip的部分区域
public class TaskInfoButton extends NormalTooltipButton {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MaidsoulKitchen.MOD_ID, "textures/gui/cook_guide.png");

    private IMaidTask task;

    public TaskInfoButton(int pX, int pY, int pWidth, int pHeight, IMaidTask task) {
        super(pX, pY, pWidth, pHeight, task.getName(), getDesc(task), (b) -> {
        });
        this.task = task;
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft mc = Minecraft.getInstance();
        pGuiGraphics.blit(TEXTURE, this.getX(), this.getY(), 179, 2, this.width, this.height);
        pGuiGraphics.renderItem(task.getIcon(), this.getX() + 2, this.getY() + 2);
        List<FormattedCharSequence> splitTexts = mc.font.split(task.getName(), 42);
        if (!splitTexts.isEmpty()) {
            pGuiGraphics.drawString(mc.font, splitTexts.get(0), this.getX() + 22, this.getY() + 5, 0xffffff, false);
        }
    }

    protected void renderScrollingTaskString(GuiGraphics pGuiGraphics, Font pFont, int x, int y, int pWidth, int pColor) {
        renderScrollingString(pGuiGraphics, pFont, this.getMessage(), x, y, x + pWidth, y + pFont.lineHeight, pColor);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        return false;
    }

    public static List<Component> getDesc(IMaidTask task) {
        List<Component> components = new ArrayList<>();
        components.add(Component.translatable("gui.maidsoulkitchen.widget.cook_guide.task.desc", task.getName()));
        if (task instanceof ICookTask<?, ?> maidTask) {
            RecipeType<?> recipeType = maidTask.getRecipeType();
            String typeString = recipeType.toString();

            components.add(CommonComponents.SPACE);
            components.add(Component.translatable("gui.maidsoulkitchen.widget.cook_guide.task.recipe_type", typeString).withStyle(ChatFormatting.DARK_GRAY));
        }
        return components;
    }
}
