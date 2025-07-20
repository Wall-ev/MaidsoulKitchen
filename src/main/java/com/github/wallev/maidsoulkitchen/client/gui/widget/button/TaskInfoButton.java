package com.github.wallev.maidsoulkitchen.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.util.TipsHelper;
import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.cook.v1.KitchenData;
import com.github.wallev.maidsoulkitchen.task.cook.common.task.CookTaskManager;
import com.github.wallev.maidsoulkitchen.vhelper.client.chat.VComponent;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

// 防止覆盖tooltip的部分区域
@OnlyIn(Dist.CLIENT)
public class TaskInfoButton extends NormalTooltipButton {
    private static final ResourceLocation TEXTURE = VResourceLocation.create(MaidsoulKitchen.MOD_ID, "textures/gui/cook_guide.png");

    private final ICookTask<?, ?> cookTask;
    private final boolean isIdle;

    public TaskInfoButton(int pX, int pY, int pWidth, int pHeight, ICookTask<?, ?> cookTask, KitchenData kitchenData, OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, cookTask.getName(), getDesc(cookTask), pOnPress);
        this.cookTask = cookTask;
        this.isIdle = kitchenData.getCookName().equals(CookTaskManager.getIdleTask().getUid());
    }

    public static List<Component> getDesc(ICookTask<?, ?> task) {
        List<Component> components = new ArrayList<>();
        components.add(VComponent.translatable("gui.maidsoulkitchen.widget.cook_guide.task.desc", task.getName()));
        String typeString = task.getRecipeTypeId();

        components.add(CommonComponents.SPACE);
        components.add(VComponent.translatable("gui.maidsoulkitchen.widget.cook_guide.task.recipe_type", typeString).withStyle(ChatFormatting.DARK_GRAY));

        return components;
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft mc = Minecraft.getInstance();
        pGuiGraphics.blit(TEXTURE, this.getX(), this.getY(), 179, 2, this.width, this.height);

        pGuiGraphics.renderItem(cookTask.getIcon(), this.getX() + 2, this.getY() + 2);
        List<FormattedCharSequence> splitTexts = mc.font.split(cookTask.getName(), 42);
        if (!splitTexts.isEmpty()) {
            pGuiGraphics.drawString(mc.font, splitTexts.get(0), this.getX() + 22, this.getY() + 5, 0xffffff, false);
        }

        if (isIdle) {
            TipsHelper.renderTips(pGuiGraphics, this, Component.translatable("gui.maidsoulkitchen.widget.cook_guide.task.choose_cook_type"));
        }
    }

    protected void renderScrollingTaskString(GuiGraphics pGuiGraphics, Font pFont, int x, int y, int pWidth, int pColor) {
        renderScrollingString(pGuiGraphics, pFont, this.getMessage(), x, y, x + pWidth, y + pFont.lineHeight, pColor);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
}
