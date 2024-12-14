package com.github.wallev.farmsoulkitchen.client.tooltip;

import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import com.github.wallev.farmsoulkitchen.config.subconfig.TaskConfig;
import com.github.wallev.farmsoulkitchen.inventory.tooltip.CrockPotTooltip;
import com.github.wallev.farmsoulkitchen.task.cook.v1.crokckpot.TaskCrockPot;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.recipe.cooking.requirement.*;
import com.sihenzhang.crockpot.util.MathUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Map;

public class CrockPotAmountTooltip implements ClientAmountTooltip{
    private static final ResourceLocation TEXTURE = new ResourceLocation(FarmsoulKitchen.MOD_ID, "textures/gui/cook_guide.png");
    private final int rowSpacing = 2, colSpacing = 2;
    private final MutableComponent titleTip = Component.translatable("tooltips.farmsoulkitchen.amount.title");
//    private final MutableComponent randomTip = Component.translatable("gui.farmsoulkitchen.btn.cook_guide.warn.not_select").withStyle(ChatFormatting.YELLOW);
//    private final MutableComponent overSizeTip = Component.translatable("gui.farmsoulkitchen.btn.cook_guide.warn.over_size", TaskConfig.COOK_SELECTED_RECIPES.get()).withStyle(ChatFormatting.YELLOW);

    private final CrockPotTooltip crockPotTooltip;
    private final TaskCrockPot.RecInfo1 recInfo1;
    private final Map<IRequirement, List<Item>> requirementListMap;
    private final Boolean isRandom;
    private final boolean isOverSize;
    private final int rows, cols;

    public CrockPotAmountTooltip(CrockPotTooltip crockPotTooltip) {
        this.crockPotTooltip = crockPotTooltip;
        this.recInfo1 = crockPotTooltip.recInfo1();
        this.requirementListMap = crockPotTooltip.requirementListMap();
        this.isRandom = crockPotTooltip.isRandom();
        this.isOverSize = crockPotTooltip.isOverSize();

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
        return Minecraft.getInstance().font.lineHeight + 2 + 10 + rows * (22 + rowSpacing);
    }

    @Override
    public int getWidth(Font font) {
        int tipMax = font.width(titleTip);
//        if (isRandom) {
        {
            MutableComponent tip = Component.translatable("gui.farmsoulkitchen.btn.cook_guide.warn.now_type")
                    .append(Component.translatable(String.format("gui.farmsoulkitchen.btn.cook_guide.type.%s", this.isRandom ? "blacklist" : "whitelist")));
            tipMax = Math.max(tipMax, font.width(tip));
        }
//        if (isOverSize) {
//            tipMax = Math.max(tipMax, font.width(overSizeTip));
//        }
        return Math.max(tipMax, cols * (64 + colSpacing));
    }

    @Override
    public void renderImage(Font font, int pX, int pY, GuiGraphics guiGraphics) {

//        if (isRandom) {
        {
            MutableComponent tip = Component.translatable("gui.farmsoulkitchen.btn.cook_guide.warn.now_type")
                    .append(Component.translatable(String.format("gui.farmsoulkitchen.btn.cook_guide.type.%s", this.isRandom ? "blacklist" : "whitelist")));
            guiGraphics.drawString(font, tip, pX, pY, ChatFormatting.YELLOW.getColor());
            pY += 10;
        }
//        if (isOverSize) {
//            guiGraphics.drawString(font, overSizeTip, pX, pY, ChatFormatting.YELLOW.getColor());
//            pY += 10;
//        }
        guiGraphics.drawString(font, titleTip, pX, pY, ChatFormatting.GRAY.getColor());
        pY += 10;

        int i = 0;

        for (RequirementCategoryMax noRequire : recInfo1.getNoRequires()) {
            FoodCategory category = noRequire.getCategory();
            ItemStack itemStack = FoodCategory.getItemStack(category);
            int xOffset = pX + (i % cols) * (64 + colSpacing);
            int yOffset = pY + (i / cols) * (22 + rowSpacing);
            guiGraphics.blit(TEXTURE, xOffset, yOffset, 192, 234, 64, 22);
            guiGraphics.renderItem(itemStack, xOffset + 3, yOffset + 3);
            MutableComponent mutableComponent = MathUtils.fuzzyIsZero(noRequire.getMax()) ? Component.translatable("integration.crockpot.jei.crock_pot_cooking.requirement.no") : Component.translatable("integration.crockpot.jei.crock_pot_cooking.requirement.le", noRequire.getMax());
            guiGraphics.drawString(font, mutableComponent, xOffset + 3 + 16 + 2, yOffset + 8, ChatFormatting.BLACK.getColor(), false);
            i++;
        }
        for (RequirementCategoryMinExclusive anyRequire : recInfo1.getAnyRequires()) {
            FoodCategory category = anyRequire.getCategory();
            ItemStack itemStack = FoodCategory.getItemStack(category);
            int xOffset = pX + (i % cols) * (64 + colSpacing);
            int yOffset = pY + (i / cols) * (22 + rowSpacing);
            guiGraphics.blit(TEXTURE, xOffset, yOffset, 192, 234, 64, 22);
            guiGraphics.renderItem(itemStack, xOffset + 3, yOffset + 3);
            MutableComponent mutableComponent = MathUtils.fuzzyIsZero(anyRequire.getMin()) ? Component.translatable("integration.crockpot.jei.crock_pot_cooking.requirement.any") : Component.translatable("integration.crockpot.jei.crock_pot_cooking.requirement.gt", anyRequire.getMin());
            guiGraphics.drawString(font, mutableComponent, xOffset + 3 + 16 + 2, yOffset + 8, ChatFormatting.BLACK.getColor(), false);
            i++;
        }
        for (RequirementMustContainIngredient mustRequire : recInfo1.getMustRequires()) {
            Ingredient category = mustRequire.getIngredient();
            ItemStack[] items = category.getItems();
            ItemStack itemStack = items[0];
            int xOffset = pX + (i % cols) * (64 + colSpacing);
            int yOffset = pY + (i / cols) * (22 + rowSpacing);
            guiGraphics.blit(TEXTURE, xOffset, yOffset, 192, 234, 64, 22);
            guiGraphics.renderItem(itemStack, xOffset + 3, yOffset + 3);
            if (items.length > 1) {
                guiGraphics.blit(TEXTURE, xOffset + 3, yOffset + 3 + 13, 0, 253, 3, 3);
            }
            MutableComponent mutableComponent = Component.translatable(mustRequire.getQuantity() >= 4 ? "integration.crockpot.jei.crock_pot_cooking.requirement.eq" : "integration.crockpot.jei.crock_pot_cooking.requirement.ge", mustRequire.getQuantity());
            guiGraphics.drawString(font, mutableComponent, xOffset + 3 + 16 + 2, yOffset + 8, ChatFormatting.BLACK.getColor(), false);
            i++;
        }
        
        for (RequirementCategoryMin noRequire : recInfo1.getMinRequires()) {
            FoodCategory category = noRequire.getCategory();
            ItemStack itemStack = FoodCategory.getItemStack(category);
            int xOffset = pX + (i % cols) * (64 + colSpacing);
            int yOffset = pY + (i / cols) * (22 + rowSpacing);
            guiGraphics.blit(TEXTURE, xOffset, yOffset, 192, 234, 64, 22);
            guiGraphics.renderItem(itemStack, xOffset + 3, yOffset + 3);
            MutableComponent mutableComponent = Component.translatable("integration.crockpot.jei.crock_pot_cooking.requirement.ge", noRequire.getMin());
            guiGraphics.drawString(font, mutableComponent, xOffset + 3 + 16 + 2, yOffset + 8, ChatFormatting.BLACK.getColor(), false);
            i++;
        }
        for (RequirementCategoryMinExclusive minRequire : recInfo1.getMinERequires()) {
            FoodCategory category = minRequire.getCategory();
            ItemStack itemStack = FoodCategory.getItemStack(category);
            int xOffset = pX + (i % cols) * (64 + colSpacing);
            int yOffset = pY + (i / cols) * (22 + rowSpacing);
            guiGraphics.blit(TEXTURE, xOffset, yOffset, 192, 234, 64, 22);
            guiGraphics.renderItem(itemStack, xOffset + 3, yOffset + 3);
            MutableComponent mutableComponent = MathUtils.fuzzyIsZero(minRequire.getMin()) ? Component.translatable("integration.crockpot.jei.crock_pot_cooking.requirement.any") : Component.translatable("integration.crockpot.jei.crock_pot_cooking.requirement.gt", minRequire.getMin());
            guiGraphics.drawString(font, mutableComponent, xOffset + 3 + 16 + 2, yOffset + 8, ChatFormatting.BLACK.getColor(), false);
            i++;
        }
        
        for (RequirementCategoryMax maxRequire : recInfo1.getMaxRequires()) {
            FoodCategory category = maxRequire.getCategory();
            ItemStack itemStack = FoodCategory.getItemStack(category);
            int xOffset = pX + (i % cols) * (64 + colSpacing);
            int yOffset = pY + (i / cols) * (22 + rowSpacing);
            guiGraphics.blit(TEXTURE, xOffset, yOffset, 192, 234, 64, 22);
            guiGraphics.renderItem(itemStack, xOffset + 3, yOffset + 3);
            MutableComponent mutableComponent = MathUtils.fuzzyIsZero(maxRequire.getMax()) ? Component.translatable("integration.crockpot.jei.crock_pot_cooking.requirement.no") : Component.translatable("integration.crockpot.jei.crock_pot_cooking.requirement.le", maxRequire.getMax());
            guiGraphics.drawString(font, mutableComponent, xOffset + 3 + 16 + 2, yOffset + 8, ChatFormatting.BLACK.getColor(), false);
            i++;
        }
        for (RequirementCategoryMaxExclusive maxERequire : recInfo1.getMaxERequires()) {
            FoodCategory category = maxERequire.getCategory();
            ItemStack itemStack = FoodCategory.getItemStack(category);
            int xOffset = pX + (i % cols) * (64 + colSpacing);
            int yOffset = pY + (i / cols) * (22 + rowSpacing);
            guiGraphics.blit(TEXTURE, xOffset, yOffset, 192, 234, 64, 22);
            guiGraphics.renderItem(itemStack, xOffset + 3, yOffset + 3);
            MutableComponent mutableComponent = Component.translatable("integration.crockpot.jei.crock_pot_cooking.requirement.lt", maxERequire.getMax());
            guiGraphics.drawString(font, mutableComponent, xOffset + 3 + 16 + 2, yOffset + 8, ChatFormatting.BLACK.getColor(), false);
            i++;
        }
        
        for (RequirementMustContainIngredientLessThan mustLessTenRequire : recInfo1.getMustLessRequires()) {
            Ingredient category = mustLessTenRequire.getIngredient();
            ItemStack[] items = category.getItems();
            ItemStack itemStack = items[0];
            int xOffset = pX + (i % cols) * (64 + colSpacing);
            int yOffset = pY + (i / cols) * (22 + rowSpacing);
            guiGraphics.blit(TEXTURE, xOffset, yOffset, 192, 234, 64, 22);
            guiGraphics.renderItem(itemStack, xOffset + 3, yOffset + 3);
            if (items.length > 1) {
                guiGraphics.blit(TEXTURE, xOffset + 3, yOffset + 3 + 13, 0, 253, 3, 3);
            }
            guiGraphics.renderItem(itemStack, xOffset, yOffset);
            MutableComponent mutableComponent = Component.translatable(mustLessTenRequire.getQuantity() >= 4 ? "integration.crockpot.jei.crock_pot_cooking.requirement.eq" : "integration.crockpot.jei.crock_pot_cooking.requirement.le", mustLessTenRequire.getQuantity());
            guiGraphics.drawString(font, mutableComponent, xOffset + 3 + 16 + 2, yOffset + 8, ChatFormatting.BLACK.getColor(), false);
            i++;
        }
         

    }
}
