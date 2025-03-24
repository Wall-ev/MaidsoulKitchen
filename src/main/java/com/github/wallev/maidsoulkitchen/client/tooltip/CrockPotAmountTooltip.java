package com.github.wallev.maidsoulkitchen.client.tooltip;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.verhelper.client.chat.VComponent;
import com.github.wallev.maidsoulkitchen.inventory.tooltip.CrockPotTooltip;
import com.github.wallev.maidsoulkitchen.task.cook.crokckpot.TaskCpCrockPot;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.recipe.cooking.requirement.*;
import com.sihenzhang.crockpot.util.MathUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Map;

import static net.minecraft.client.gui.GuiComponent.blit;

public class CrockPotAmountTooltip implements ClientAmountTooltip {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MaidsoulKitchen.MOD_ID, "textures/gui/cook_guide.png");
    private final int rowSpacing = 2, colSpacing = 2;
    private final MutableComponent titleTip = VComponent.translatable("tooltips.maidsoulkitchen.amount.title");
//    private final MutableComponent randomTip = VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.warn.not_select").withStyle(ChatFormatting.YELLOW);
//    private final MutableComponent overSizeTip = VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.warn.over_size", TaskConfig.COOK_SELECTED_RECIPES.get()).withStyle(ChatFormatting.YELLOW);
private final CrockPotTooltip crockPotTooltip;
    private final String recipeId;
    private final TaskCpCrockPot.RecInfo1 recInfo1;
    private final Map<IRequirement, List<Item>> requirementListMap;
    private final Boolean isBlacklist;
    private final CookData cookData;
    private final boolean isOverSize;
    private final int rows, cols;

    public CrockPotAmountTooltip(CrockPotTooltip crockPotTooltip) {
        this.crockPotTooltip = crockPotTooltip;
        this.recipeId = crockPotTooltip.recipeId();
        this.recInfo1 = crockPotTooltip.recInfo1();
        this.requirementListMap = crockPotTooltip.requirementListMap();
        this.isBlacklist = crockPotTooltip.isRandom();
        this.isOverSize = crockPotTooltip.isOverSize();
        this.cookData = crockPotTooltip.cookData();

        int count = recInfo1.getNoRequires().size()
                + recInfo1.getAnyRequires().size()
                + recInfo1.getMustRequires().size()
                + recInfo1.getMinRequires().size()
                + recInfo1.getMinERequires().size()
                + recInfo1.getMaxRequires().size()
                + recInfo1.getMaxERequires().size()
                + recInfo1.getMustLessRequires().size();

        if (count <= 2) {
            cols = 2;
        } else if (count == 3) {
            cols = 3;
        }else if (count == 4) {
            cols = 2;
        } else {
            cols = 3;
        }

        rows = count % cols == 0 ? count / cols : (count / cols) + 1;
    }

    @Override
    public int getHeight() {
        return Minecraft.getInstance().font.lineHeight + 2 + 10 + 10 + rows * (22 + rowSpacing);
    }

    @Override
    public int getWidth(Font font) {
        int tipMax = font.width(titleTip);
        {
            MutableComponent tip = VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.warn.now_type")
                    .append(VComponent.translatable(String.format("gui.maidsoulkitchen.btn.cook_guide.type.%s", this.isBlacklist ? "blacklist" : "whitelist")));
            tipMax = Math.max(tipMax, font.width(tip));
            MutableComponent canCook = VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.can_cook")
                    .append(VComponent.translatable(String.format("gui.maidsoulkitchen.btn.cook_guide.can_cook.%s", this.canCook() ? "true" : "false")));
            tipMax = Math.max(tipMax, font.width(canCook));
        }
        return Math.max(tipMax, cols * (64 + colSpacing));
    }

    @Override
    public void renderImage(Font font, int pX, int pY, PoseStack poseStack, ItemRenderer itemRenderer, int blitOffset) {
        poseStack.pushPose();
        poseStack.translate(0, 0, blitOffset);

        {
            MutableComponent tip = VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.warn.now_type")
                    .append(VComponent.translatable(String.format("gui.maidsoulkitchen.btn.cook_guide.type.%s", this.isBlacklist ? "blacklist" : "whitelist")));
            font.draw(poseStack, tip, pX, pY, ChatFormatting.YELLOW.getColor());
            pY += 10;
            MutableComponent canCook = VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.can_cook")
                    .append(VComponent.translatable(String.format("gui.maidsoulkitchen.btn.cook_guide.can_cook.%s", this.canCook() ? "true" : "false")))
                    .withStyle(this.canCook() ? ChatFormatting.GREEN : ChatFormatting.RED);
            font.draw(poseStack, canCook, pX, pY, ChatFormatting.YELLOW.getColor());
            pY += 10;
        }
        {
            font.draw(poseStack, titleTip, pX, pY, ChatFormatting.GRAY.getColor());
            pY += 10;
        }
        int i = 0;

        for (RequirementCategoryMax noRequire : recInfo1.getNoRequires()) {
            FoodCategory category = noRequire.getCategory();
            ItemStack itemStack = FoodCategory.getItemStack(category);
            int xOffset = pX + (i % cols) * (64 + colSpacing);
            int yOffset = pY + (i / cols) * (22 + rowSpacing);

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
            blit(poseStack, xOffset, yOffset, 192, 234, 64, 22, 256, 256);

            itemRenderer.renderGuiItem(itemStack, xOffset + 3, yOffset + 3);

            MutableComponent mutableComponent = MathUtils.fuzzyIsZero(noRequire.getMax()) ? VComponent.translatable("integration.crockpot.jei.crock_pot_cooking.requirement.no") : VComponent.translatable("integration.crockpot.jei.crock_pot_cooking.requirement.le", noRequire.getMax());
            font.draw(poseStack, mutableComponent, xOffset + 3 + 16 + 2, yOffset + 8, ChatFormatting.BLACK.getColor());

            i++;
        }
        for (RequirementCategoryMinExclusive anyRequire : recInfo1.getAnyRequires()) {
            FoodCategory category = anyRequire.getCategory();
            ItemStack itemStack = FoodCategory.getItemStack(category);
            int xOffset = pX + (i % cols) * (64 + colSpacing);
            int yOffset = pY + (i / cols) * (22 + rowSpacing);

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
            blit(poseStack, xOffset, yOffset, 192, 234, 64, 22, 256, 256);

            itemRenderer.renderGuiItem(itemStack, xOffset + 3, yOffset + 3);

            MutableComponent mutableComponent = MathUtils.fuzzyIsZero(anyRequire.getMin()) ? VComponent.translatable("integration.crockpot.jei.crock_pot_cooking.requirement.any") : VComponent.translatable("integration.crockpot.jei.crock_pot_cooking.requirement.gt", anyRequire.getMin());
            font.draw(poseStack, mutableComponent, (float) xOffset + 3 + 16 + 2, (float) yOffset + 8, ChatFormatting.BLACK.getColor());

            i++;
        }
        for (RequirementMustContainIngredient mustRequire : recInfo1.getMustRequires()) {
            Ingredient category = mustRequire.getIngredient();
            ItemStack[] items = category.getItems();
            ItemStack itemStack = items[0];
            int xOffset = pX + (i % cols) * (64 + colSpacing);
            int yOffset = pY + (i / cols) * (22 + rowSpacing);

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
            blit(poseStack, xOffset, yOffset, 192, 234, 64, 22, 256, 256);

            itemRenderer.renderGuiItem(itemStack, xOffset + 3, yOffset + 3);
            if (items.length > 1) {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, TEXTURE);
                blit(poseStack, xOffset + 3, yOffset + 3 + 13, 0, 253, 3, 3, 256, 256);
            }

            MutableComponent mutableComponent = VComponent.translatable(mustRequire.getQuantity() >= 4 ? "integration.crockpot.jei.crock_pot_cooking.requirement.eq" : "integration.crockpot.jei.crock_pot_cooking.requirement.ge", mustRequire.getQuantity());
            font.draw(poseStack, mutableComponent, xOffset + 3 + 16 + 2, yOffset + 8, ChatFormatting.BLACK.getColor());

            i++;
        }

        for (RequirementCategoryMin noRequire : recInfo1.getMinRequires()) {
            FoodCategory category = noRequire.getCategory();
            ItemStack itemStack = FoodCategory.getItemStack(category);
            int xOffset = pX + (i % cols) * (64 + colSpacing);
            int yOffset = pY + (i / cols) * (22 + rowSpacing);

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
            blit(poseStack, xOffset, yOffset, 192, 234, 64, 22, 256, 256);

            itemRenderer.renderGuiItem(itemStack, xOffset + 3, yOffset + 3);

            MutableComponent mutableComponent = VComponent.translatable("integration.crockpot.jei.crock_pot_cooking.requirement.ge", noRequire.getMin());
            font.draw(poseStack, mutableComponent, xOffset + 3 + 16 + 2, yOffset + 8, ChatFormatting.BLACK.getColor());

            i++;
        }
        for (RequirementCategoryMinExclusive minRequire : recInfo1.getMinERequires()) {
            FoodCategory category = minRequire.getCategory();
            ItemStack itemStack = FoodCategory.getItemStack(category);
            int xOffset = pX + (i % cols) * (64 + colSpacing);
            int yOffset = pY + (i / cols) * (22 + rowSpacing);

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
            blit(poseStack, xOffset, yOffset, 192, 234, 64, 22, 256, 256);

            itemRenderer.renderGuiItem(itemStack, xOffset + 3, yOffset + 3);

            MutableComponent mutableComponent = MathUtils.fuzzyIsZero(minRequire.getMin()) ? VComponent.translatable("integration.crockpot.jei.crock_pot_cooking.requirement.any") : VComponent.translatable("integration.crockpot.jei.crock_pot_cooking.requirement.gt", minRequire.getMin());
            font.draw(poseStack, mutableComponent, xOffset + 3 + 16 + 2, yOffset + 8, ChatFormatting.BLACK.getColor());

            i++;
        }

        for (RequirementCategoryMax maxRequire : recInfo1.getMaxRequires()) {
            FoodCategory category = maxRequire.getCategory();
            ItemStack itemStack = FoodCategory.getItemStack(category);
            int xOffset = pX + (i % cols) * (64 + colSpacing);
            int yOffset = pY + (i / cols) * (22 + rowSpacing);

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
            blit(poseStack, xOffset, yOffset, 192, 234, 64, 22, 256, 256);

            itemRenderer.renderGuiItem(itemStack, xOffset + 3, yOffset + 3);

            MutableComponent mutableComponent = MathUtils.fuzzyIsZero(maxRequire.getMax()) ? VComponent.translatable("integration.crockpot.jei.crock_pot_cooking.requirement.no") : VComponent.translatable("integration.crockpot.jei.crock_pot_cooking.requirement.le", maxRequire.getMax());
            font.draw(poseStack, mutableComponent, xOffset + 3 + 16 + 2, yOffset + 8, ChatFormatting.BLACK.getColor());

            i++;
        }
        for (RequirementCategoryMaxExclusive maxERequire : recInfo1.getMaxERequires()) {
            FoodCategory category = maxERequire.getCategory();
            ItemStack itemStack = FoodCategory.getItemStack(category);
            int xOffset = pX + (i % cols) * (64 + colSpacing);
            int yOffset = pY + (i / cols) * (22 + rowSpacing);

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
            blit(poseStack, xOffset, yOffset, 192, 234, 64, 22, 256, 256);

            itemRenderer.renderGuiItem(itemStack, xOffset + 3, yOffset + 3);

            MutableComponent mutableComponent = VComponent.translatable("integration.crockpot.jei.crock_pot_cooking.requirement.lt", maxERequire.getMax());
            font.draw(poseStack, mutableComponent, xOffset + 3 + 16 + 2, yOffset + 8, ChatFormatting.BLACK.getColor());

            i++;
        }

        for (RequirementMustContainIngredientLessThan mustLessTenRequire : recInfo1.getMustLessRequires()) {
            Ingredient category = mustLessTenRequire.getIngredient();
            ItemStack[] items = category.getItems();
            ItemStack itemStack = items[0];
            int xOffset = pX + (i % cols) * (64 + colSpacing);
            int yOffset = pY + (i / cols) * (22 + rowSpacing);

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
            blit(poseStack, xOffset, yOffset, 192, 234, 64, 22, 256, 256);

            itemRenderer.renderGuiItem(itemStack, xOffset + 3, yOffset + 3);
            if (items.length > 1) {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, TEXTURE);
                blit(poseStack, xOffset + 3, yOffset + 3 + 13, 0, 253, 3, 3, 256, 256);
            }
            itemRenderer.renderGuiItem(itemStack, xOffset, yOffset);

            MutableComponent mutableComponent = VComponent.translatable(mustLessTenRequire.getQuantity() >= 4 ? "integration.crockpot.jei.crock_pot_cooking.requirement.eq" : "integration.crockpot.jei.crock_pot_cooking.requirement.le", mustLessTenRequire.getQuantity());
            font.draw(poseStack, mutableComponent, xOffset + 3 + 16 + 2, yOffset + 8, ChatFormatting.BLACK.getColor());

            i++;
        }
        poseStack.popPose();
    }

    private boolean canCook() {
        if (isBlacklist) {
            return !cookData.blacklistRecs().contains(recipeId);
        } else {
            return cookData.whitelistRecs().contains(recipeId);
        }
    }
}
