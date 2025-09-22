package com.github.wallev.maidsoulkitchen.client.overlay;

import com.github.wallev.maidsoulkitchen.init.ModItems;
import com.github.wallev.maidsoulkitchen.inventory.container.item.BagType;
import com.github.wallev.maidsoulkitchen.item.ItemCulinaryHub;
import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CulinaryHubOverlay implements IGuiOverlay {
    // 写的什么玩意....
    private static String getTranslateKey(String bindMode) {
        switch (bindMode) {
            case "Ingredient" -> {
                return BagType.INGREDIENT.translateKey;
            }
            case "StartAddition" -> {
                return BagType.START_ADDITION.translateKey;
            }
            case "IngredientAddition" -> {
                return BagType.INGREDIENT_ADDITION.translateKey;
            }
            case "OutputAddition" -> {
                return BagType.OUTPUT_ADDITION.translateKey;
            }
            case "Output" -> {
                return BagType.OUTPUT.translateKey;
            }
            default -> {
                return "";
            }
        }
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft minecraft = gui.getMinecraft();
        Options options = minecraft.options;
        if (!options.getCameraType().isFirstPerson()) {
            return;
        }
        if (minecraft.gameMode == null || minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR) {
            return;
        }
        LocalPlayer player = minecraft.player;
        if (player == null) {
            return;
        }
        if (!(minecraft.hitResult instanceof BlockHitResult blockHitResult)) {
            return;
        }
        ItemStack mainHandItem = player.getMainHandItem();
        if (!mainHandItem.is(ModItems.CULINARY_HUB.get())) {
            return;
        }
        BlockPos hitResultBlockPos = blockHitResult.getBlockPos();
        BlockEntity blockEntity = player.level().getBlockEntity(hitResultBlockPos);
        if (blockEntity != null && ItemCulinaryHub.getBeInv(blockEntity) != null) {

            boolean binded = false;
            Map<BagType, List<BlockPos>> bindPoses = ItemCulinaryHub.getBindPoses(mainHandItem);

            List<Component> bindedComponents = new ArrayList<>();
            bindedComponents.add(Component.translatable("gui.maidsoulkitchen.culinary_hub.already_bind_types").withStyle(ChatFormatting.GRAY));
            MutableComponent component = Component.empty().withStyle(ChatFormatting.GRAY);
            int index = bindPoses.size();
            for (Map.Entry<BagType, List<BlockPos>> bagType : bindPoses.entrySet()) {
                BagType type = bagType.getKey();

                boolean canDisplay = true;
                for (BagType displayVal : BagType.DISPLAY_VALS) {
                    if (displayVal != type) {
                        canDisplay = false;
                        break;
                    }
                }
                if (!canDisplay) {
                    continue;
                }

                MutableComponent bindModeComponent = Component.translatable("gui.maidsoulkitchen.culinary_hub.config.bind_mode." + type.translateKey);
                if (!bagType.getValue().contains(hitResultBlockPos)) {
                    continue;
                }
                component.append(bindModeComponent);
                if (--index > 0) {
                    component.append(", ");
                }
                binded = true;
            }
            bindedComponents.add(component);

            List<Component> tips = Lists.newArrayList();
            if (!binded) {
                tips.add(Component.translatable("gui.maidsoulkitchen.culinary_hub.can_sneak_bind").withStyle(ChatFormatting.GRAY));

                String bindMode = ItemCulinaryHub.getBindMode(mainHandItem);
                if (bindMode.isEmpty()) {
                    tips.add(Component.translatable("gui.maidsoulkitchen.culinary_hub.right_click_to_bind").withStyle(ChatFormatting.GRAY));
                } else {
                    int size = ItemCulinaryHub.getBindModePoses(mainHandItem, bindMode).size();
                    tips.add(Component.translatable("gui.maidsoulkitchen.culinary_hub.current_binding_type")
                            .append(Component.translatable("gui.maidsoulkitchen.culinary_hub.config.bind_mode." + getTranslateKey(bindMode)))
                            .append(Component.literal(String.format("[%s/%s]", size, ItemCulinaryHub.BIND_SIZE)))
                            .withStyle(ChatFormatting.GRAY));
                }
            } else {
                tips.addAll(bindedComponents);
            }

            gui.setupOverlayRenderState(true, false);
            int offset = (screenHeight / 2) + 5;
            for (Component tip : tips) {
                int width = minecraft.font.width(tip);
                guiGraphics.drawString(minecraft.font, tip, (screenWidth - width) / 2, offset, 0xFFFFFF);
                offset += minecraft.font.lineHeight + 1;
            }
        }
    }
}
