package com.github.wallev.maidsoulkitchen.client.gui.widget.button;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.wallev.maidsoulkitchen.handler.VComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
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
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float pPartialTick) {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        blit(poseStack, x, y, 179, 2, this.width, this.height);
        mc.getItemRenderer().renderGuiItem(task.getIcon(), x + 2, y + 2);
        List<FormattedCharSequence> splitTexts = mc.font.split(task.getName(), 42);
        if (!splitTexts.isEmpty()) {
            mc.font.draw(poseStack, splitTexts.get(0), x + 22, y + 5, 0xffffff);
        }

        if (this.isHoveredOrFocused()) {
            this.renderToolTip(poseStack, mouseX, mouseY);
        }
    }

    protected void renderScrollingTaskString(PoseStack poseStack, Font pFont, int x, int y, int pWidth, int pColor) {
        pFont.draw(poseStack, this.getMessage(), x, y, pColor);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        return false;
    }

    public static List<Component> getDesc(IMaidTask task) {
        List<Component> components = new ArrayList<>();
        components.add(VComponent.translatable("gui.maidsoulkitchen.widget.cook_guide.task.desc", task.getName()));
        if (task instanceof ICookTask<?, ?> maidTask) {
            RecipeType<?> recipeType = maidTask.getRecipeType();
            String typeString = recipeType.toString();

            components.add(VComponent.SPACE);
            components.add(VComponent.translatable("gui.maidsoulkitchen.widget.cook_guide.task.recipe_type", typeString).withStyle(ChatFormatting.DARK_GRAY));
        }
        return components;
    }
}
