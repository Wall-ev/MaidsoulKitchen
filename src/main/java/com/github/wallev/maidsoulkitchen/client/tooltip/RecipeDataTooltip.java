package com.github.wallev.maidsoulkitchen.client.tooltip;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.util.MathUtil;
import com.github.wallev.maidsoulkitchen.util.TextContactUtil;
import com.github.wallev.verhelper.client.chat.VComponent;
import com.github.wallev.verhelper.client.resources.VResourceLocation;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("all")
public class RecipeDataTooltip implements ClientAmountTooltip {
    private static final ResourceLocation TEXTURE = VResourceLocation.create(MaidsoulKitchen.MOD_ID, "textures/gui/cook_guide.png");

    private final CookData cookData;
    private final String recipeId;
    private final List<RecIngredient> ingres;
    private final RecIngredient result;
    private final boolean isBlacklist;

    public RecipeDataTooltip(TooltipRecipeData tooltipRecipeData) {
        this.cookData = tooltipRecipeData.cookData();
        this.recipeId = tooltipRecipeData.recipeId();
        this.ingres = RecIngredient.warp(tooltipRecipeData.ingres());
        this.isBlacklist = tooltipRecipeData.isBlacklist();
        this.result = RecIngredient.warpResult(tooltipRecipeData.result);
    }

    private MutableComponent getAmountComponent() {
        return VComponent.translatable("tooltips.maidsoulkitchen.amount.title");
    }

    private MutableComponent getCancookComponent() {
        return VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.can_cook")
                .append(VComponent.translatable(String.format("gui.maidsoulkitchen.btn.cook_guide.can_cook.%s", this.canCook() ? "true" : "false")));
    }

    private MutableComponent getModeComponent() {
        return VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.warn.now_type")
                .append(VComponent.translatable(String.format("gui.maidsoulkitchen.btn.cook_guide.type.%s", this.isBlacklist ? "blacklist" : "whitelist")));
    }

    private boolean canCook() {
        if (isBlacklist) {
            return !cookData.blacklistRecs().contains(recipeId);
        } else {
            return cookData.whitelistRecs().contains(recipeId);
        }
    }

    private int getTooltipHeight(Font font) {
        return 30 + ingres.stream().mapToInt(rec -> rec.getHeight(font)).sum() + 2 * (ingres.size() - 1) + 1 + 10 + result.getHeight(font);
    }

    private int getTooltipWidth(Font font) {
        return MathUtil.max(font.width(this.getAmountComponent()),
                font.width(this.getModeComponent()),
                font.width(this.getCancookComponent()),
                ingres.stream().mapToInt(rec -> rec.getWidth(font)).max().orElse(0),
                result.getWidth(font));
    }

    private ItemStack getItem(Ingredient ingredient) {
        int item = (int) (System.currentTimeMillis() / 1000L % ingredient.getItems().length);
        return ingredient.getItems()[item];
    }

    @Override
    public int getHeight() {
        return this.getTooltipHeight(Minecraft.getInstance().font);
    }

    @Override
    public int getWidth(Font font) {
        return this.getTooltipWidth(font);
    }

    @Override
    public void renderText(Font font, int mouseX, int pMouseY, Matrix4f matrix, MultiBufferSource.BufferSource bufferSource) {

    }

    @Override
    public void renderImage(Font font, int pX, int pY, GuiGraphics guiGraphics) {
        {
            guiGraphics.drawString(font, this.getModeComponent(), pX, pY, ChatFormatting.GOLD.getColor());
            pY += 10;
            guiGraphics.drawString(font, this.getCancookComponent(), pX, pY, this.canCook() ? ChatFormatting.DARK_GREEN.getColor() : ChatFormatting.DARK_RED.getColor());
            pY += 10;
        }

        pY = drawLine(font, pX, pY, guiGraphics);

        guiGraphics.drawString(font, this.getAmountComponent(), pX, pY, ChatFormatting.GRAY.getColor());
        pY += 10;

        for (RecIngredient recIngredient : this.ingres) {
            pY = renderRecipeIngredientData(font, pX, pY, guiGraphics, recIngredient);
        }

        pY = drawLine(font, pX, pY, guiGraphics);

        {
            pY = renderRecipeIngredientData(font, pX, pY, guiGraphics, result);
        }

        pY = drawLine(font, pX, pY, guiGraphics);
    }

    private int drawLine(Font font, int pX, int pY, GuiGraphics guiGraphics) {
        for (int i = 0; i < this.getWidth(font) / 2; i += 2) {
            guiGraphics.fillGradient(pX + i * 2, pY, pX + i * 2 + 2, pY + 1, Color.WHITE.getRGB(), Color.DARK_GRAY.getRGB());
        }
        pY += 2;
        return pY;
    }

    private int renderRecipeIngredientData(Font font, int pX, int pY, GuiGraphics guiGraphics, RecIngredient recIngredient) {
        int height = recIngredient.getHeight(font);
        guiGraphics.fillGradient(pX, pY, pX + 2, pY + height, Color.GRAY.getRGB(), Color.DARK_GRAY.getRGB());

        int i = 0;
        int widgetOffsetX = pX + 4;

        List<Ingredient> ingredients = recIngredient.ingredients();
        for (Ingredient ingre : ingredients) {
            ItemStack[] stackItems = ingre.getItems();
            if (stackItems.length == 0) {
                continue;
            }

            int xOffset = widgetOffsetX + i++ * 20;

            ItemStack itemStack = getItem(ingre);
            guiGraphics.renderItem(itemStack, xOffset, pY);
            if (itemStack.getCount() > 1) {
                guiGraphics.renderItemDecorations(font, itemStack, xOffset, pY);
            }

            if (stackItems.length > 1) {
                guiGraphics.blit(TEXTURE, xOffset, pY + 13, 0, 253, 3, 3);
            }
        }

        {
            Component typeComponent = recIngredient.type().getComponent();
            guiGraphics.drawString(font, typeComponent, widgetOffsetX + ingredients.size() * 20 + 2, pY + 7, recIngredient.type().getColorFormatting().getColor());
            guiGraphics.fillGradient(widgetOffsetX, pY + 16, widgetOffsetX + ingredients.size() * 20 + 2 + font.width(typeComponent), pY + 16 + 1, Color.GRAY.getRGB(), Color.DARK_GRAY.getRGB());
        }

        pY += 18;

        for (Component sourceTip : recIngredient.sourceTips) {
            guiGraphics.drawString(font, sourceTip, widgetOffsetX, pY, ChatFormatting.GRAY.getColor());
            pY += (font.lineHeight + 1);
        }
        pY += 2;

        return pY;
    }

    public enum IngredientType {
        MANDATORY(ChatFormatting.GOLD),
        MAYBE(ChatFormatting.GRAY),
        OUTPUT(ChatFormatting.DARK_GREEN),
        ;

        private final ChatFormatting colorFormatting;

        IngredientType(ChatFormatting colorFormatting) {
            this.colorFormatting = colorFormatting;
        }

        public Component getComponent() {
            return VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.ingredient_type." + name().toLowerCase(Locale.ENGLISH));
        }

        public ChatFormatting getColorFormatting() {
            return colorFormatting;
        }
    }

    public enum IngredientSourceType {
        MAIN_HAND,
        OFF_HAND,
        MAID_BACKPACK,
        HUB_START_ADDITION,
        HUB_INGREDIENT,
        HUB_INGREDIENT_ADDITION,
        HUB_OUTPUT_ADDITION,
        HUB_OUTPUT,
        PICKUP,
        ;

        public MutableComponent getComponent() {
            return VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.ingredient_source_type." + name().toLowerCase(Locale.ENGLISH));
        }
    }

    public record TooltipRecipeData(CookData cookData, String recipeId, List<TooltipRecIngredient> ingres, TooltipRecIngredient result,
                                    boolean isBlacklist, boolean isOverSize) implements TooltipComponent {
    }

    public record TooltipRecIngredient(List<Ingredient> ingredients, List<List<IngredientSourceType>> sourceTypes,
                                       IngredientType type, int ruleMatchIndex) {
    }

    public record RecIngredient(List<Ingredient> ingredients, List<Component> sourceTips, IngredientType type) {

        public static RecIngredient warpResult(TooltipRecIngredient tooltipRecIngredient) {
            return warp(tooltipRecIngredient, "gui.maidsoulkitchen.btn.cook_guide.output_to");
        }

        public static RecIngredient warp(TooltipRecIngredient tooltipRecIngredient) {
            return warp(tooltipRecIngredient, "gui.maidsoulkitchen.btn.cook_guide.ingredient_source_type");
        }

        private static RecIngredient warp(TooltipRecIngredient tooltipRecIngredient, String key) {
            List<List<IngredientSourceType>> sourceTypes = tooltipRecIngredient.sourceTypes;

            int i = 0;
            List<Component> sourceTips = new ArrayList<>();

            for (List<IngredientSourceType> sourceType : sourceTypes) {
                MutableComponent component = TextContactUtil.contact(sourceType, VComponent.translatable(key, i), IngredientSourceType::getComponent);

                if (i == tooltipRecIngredient.ruleMatchIndex()) {
                    component.withStyle(ChatFormatting.DARK_GREEN);
                }
                sourceTips.add(component);

                i++;
            }

            return new RecIngredient(tooltipRecIngredient.ingredients(), sourceTips, tooltipRecIngredient.type());
        }

        public static List<RecIngredient> warp(List<TooltipRecIngredient> tooltipRecIngredients) {
            return tooltipRecIngredients.stream().map(RecIngredient::warp).toList();
        }

        public int getWidth(Font font) {
            return 4 + Math.max(ingredients.size() * 20 + font.width(type.getComponent()), sourceTips.stream().mapToInt(font::width).max().orElse(0));
        }

        public int getHeight(Font font) {
            return 18 + (font.lineHeight + 1) * (sourceTips.size());
        }
    }
}
