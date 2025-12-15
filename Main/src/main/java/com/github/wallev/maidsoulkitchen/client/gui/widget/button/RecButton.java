package com.github.wallev.maidsoulkitchen.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.api.client.gui.ITooltipButton;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.cook.v0.CookData;
import com.github.wallev.maidsoulkitchen.network.NetworkHandler;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec.MKRecipe;
import com.github.wallev.maidsoulkitchen.util.DevUtil;
import com.github.wallev.maidsoulkitchen.vhelper.client.chat.VComponent;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class RecButton extends StateSwitchingButton implements ITooltipButton {
    private static final ResourceLocation TEXTURE = VResourceLocation.of(MaidsoulKitchen.MOD_ID, "textures/gui/cook_guide.png");
    // 构建虚拟 Slot, 用于支持 jei、rei、emi 等配方管理器查询成品相关信息.
    public final VirtualSlot virtualSlot;
    protected final EntityMaid maid;
    protected final ICookTask<?, ?> cookTask;
    protected final CookData cookData;
    protected final List<MKRecipe<?>> recipes;
    protected final ItemStack stack;

    public RecButton(EntityMaid maid, ICookTask<?, ?> cookTask, CookData cookData, List<MKRecipe<?>> recipes, int pX, int pY) {
        super(pX, pY, 20, 20, containersRecs(cookData, recipes));
        this.initTextureValues(179, 25, 22, 0, TEXTURE);
        this.maid = maid;
        this.cookTask = cookTask;
        this.recipes = recipes;
        this.cookData = cookData;
        this.stack = recipes.get(0).output();
        this.virtualSlot = new VirtualSlot(stack);
    }

    private static boolean containersRecs(CookData cookData, List<MKRecipe<?>> recipes) {
        for (MKRecipe<?> recipe : recipes) {
            if (cookData.getRecs().contains(recipe.idStr())) {
                return true;
            }
        }

        return false;
    }

    public void toggleState() {
        this.isStateTriggered = !this.isStateTriggered;
        this.active = true;
    }

    @Override
    protected boolean clicked(double pMouseX, double pMouseY) {
        return super.clicked(pMouseX, pMouseY);
    }

    public boolean superClicked(double pMouseX, double pMouseY) {
        return super.clicked(pMouseX, pMouseY);
    }

    public boolean debugClicked() {
        if (DevUtil.isDevEnv() && Screen.hasControlDown()) {
            this.debugGiveItem();
            return true;
        }
        return false;
    }

    /**
     * 仅用于开发者调试，便捷获取配方所需的原材料
     */
    private void debugGiveItem() {
        ArrayList<ItemStack> stacks = new ArrayList<>();
        MKRecipe<?> recipe = recipes.get(0);

        if (!recipe.inFluids().isEmpty()) {
            stacks.add(recipe.inFluids().get(0));
        }

        List<ItemStack> list = recipe.inItems().stream()
                .map(item -> {
                    ItemStack[] items = item.ingredient.getItems();
                    return items.length > 0 ? items[0] : ItemStack.EMPTY;
                })
                .toList();
        stacks.addAll(list);

        NetworkHandler.C2S.giveRecipeIngredient(stacks);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.isStateTriggered = containersRecs(cookData, recipes);

        super.renderWidget(graphics, mouseX, mouseY, partialTick);
        RenderSystem.enableDepthTest();
        graphics.renderItem(stack, this.getX() + 2, this.getY() + 2);
        RenderSystem.disableDepthTest();
        this.renderShadow(graphics);
    }

    private void renderShadow(GuiGraphics graphics) {
        if (cookData.mode().equals(CookData.Mode.WHITELIST.name)) {
            graphics.fill(this.getX(), this.getY(), this.getX() + 20, this.getY() + 20, 0x50F9F9F9);
        } else {
            graphics.fill(this.getX(), this.getY(), this.getX() + 20, this.getY() + 20, 0x50000010);
        }
    }

    @Override
    public boolean isValidClickButton(int pButton) {
        return pButton == 0 || pButton == 1;
    }

    @Override
    public boolean isTooltipHovered() {
        return this.active && this.isHovered();
    }

    @Override
    public void renderTooltip(GuiGraphics guiGraphics, Minecraft minecraft, int pMouseX, int pMouseY) {
        this.renderItemStackTooltips(minecraft, guiGraphics, pMouseX, pMouseY);
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        this.arAndSyncRec();
    }

    protected void arAndSyncRec() {
        List<String> strRecs = recipes.stream().map(MKRecipe::idStr).toList();

        // isStateTriggered: 配方数据存在
        if (isStateTriggered) {
            cookData.removeRecs(strRecs);
        } else {
            cookData.addRecs(strRecs);
        }

//        NetworkHandler.C2S.syncKitchenData2();
//        NetworkHandler.C2S.actionCookDataRecs(maid.getId(), cookTask.getCookDataKey().getKey(), strRecs, !isStateTriggered);
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

        if (recipes.size() > 1) {
            boolean whiteMode = cookData.mode().equals("whitelist");
            List<String> list = whiteMode ? cookData.whitelistRecs() : cookData.blacklistRecs();

            int has = 0;
            for (MKRecipe<?> recipe : recipes) {
                boolean canCook = list.contains(recipe.idStr());
                if (canCook) {
                    has++;
                }
            }

            boolean canCook = whiteMode ? has > 0 : has == 0;

            Component cookModeMge = VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.warn.now_type")
                    .append(VComponent.translatable(String.format("gui.maidsoulkitchen.btn.cook_guide.type.%s", !whiteMode ? "blacklist" : "whitelist")))
                    .withStyle(ChatFormatting.GOLD);
            stackTooltip.add(cookModeMge);

            Component canCookMge = VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.can_cook")
                    .append(VComponent.translatable(String.format("gui.maidsoulkitchen.btn.cook_guide.can_cook.%s", canCook ? "true" : "false")))
                    .withStyle(canCook ? ChatFormatting.DARK_GREEN : ChatFormatting.DARK_RED);
            stackTooltip.add(canCookMge);

            Component recipeSelectedMage = VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.selected_recs", has, recipes.size())
                    .withStyle(ChatFormatting.GRAY);
            stackTooltip.add(recipeSelectedMage);

            pGuiGraphics.renderComponentTooltip(mc.font, stackTooltip, pMouseX, pMouseY, stack);
        } else {
            MKRecipe<?> recipe = recipes.get(0);
            if (mc.options.advancedItemTooltips) {
                stackTooltip.add(stackTooltip.size() - 1, VComponent.literal(String.format("RecipeId: %s", recipe.id())).withStyle(ChatFormatting.DARK_GRAY));
            }

            boolean modeRandom = !cookData.mode().equals(CookData.Mode.WHITELIST.name);
            Optional<TooltipComponent> recClientAmountTooltip = this.getRecClientAmountTooltip(recipe, modeRandom, false, cookData, maid);

            pGuiGraphics.renderTooltip(mc.font, stackTooltip, recClientAmountTooltip, stack, pMouseX, pMouseY);
        }
    }

    public Optional<TooltipComponent> getRecClientAmountTooltip(MKRecipe<?> recipe, boolean modeIsBlacklist, boolean overSize, CookData cookData, EntityMaid maid) {
        return cookTask.recSerializerManager.getRecClientAmountTooltip(recipe, modeIsBlacklist, overSize, cookData, maid);
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
