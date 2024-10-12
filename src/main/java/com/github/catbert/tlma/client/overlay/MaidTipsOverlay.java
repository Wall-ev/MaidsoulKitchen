package com.github.catbert.tlma.client.overlay;

import com.github.catbert.tlma.task.farm.TaskFruitFarm;
import com.github.catbert.tlma.util.MaidTaskDataUtil;
import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.List;
import java.util.Map;

public class MaidTipsOverlay implements IGuiOverlay {
    private static final ResourceLocation ICON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_tips_icon.png");
    private static final Map<Item, MutableComponent> TIPS = Maps.newHashMap();

    public static void init() {
//        addTips("overlay.touhou_little_maid.book.tips", Items.BOOK);
    }

    private static MutableComponent checkSpecialTips(ItemStack mainhandItem, EntityMaid maid, LocalPlayer player) {
//        if (maid.isOwnedBy(player) && mainhandItem.is(Items.BOOK) && maid.getTask().getUid() == TaskFruitFarm.UID) {
//            return Component.translatable("overlay.touhou_little_maid_addon.book.tips")
//                    .append(Component.translatable("overlay.touhou_little_maid_addon.book.tips.search_yoffset")
//                            .append(MaidTaskDataUtil.getFruitFarmSearchYOffset(maid, maid.getTask().getUid().toString()) + ""));
//
//        }
        return null;
    }

    private static void addTips(String key, Item... items) {
        for (Item item : items) {
            TIPS.put(item, Component.translatable(key));
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
        if (!(minecraft.hitResult instanceof EntityHitResult result)) {
            return;
        }
        if (!(result.getEntity() instanceof EntityMaid maid)) {
            return;
        }
        LocalPlayer player = minecraft.player;
        if (player == null) {
            return;
        }
        if (!maid.isAlive()) {
            return;
        }
        MutableComponent tip = null;
        if (maid.isOwnedBy(player)) {
            tip = TIPS.get(player.getMainHandItem().getItem());
        }
        if (tip == null) {
            tip = checkSpecialTips(player.getMainHandItem(), maid, player);
        }
        if (tip != null) {
            gui.setupOverlayRenderState(true, false);
            List<FormattedCharSequence> split = minecraft.font.split(tip, 150);
            int offset = (screenHeight / 2 - 5) - split.size() * 10;
            guiGraphics.renderItem(player.getMainHandItem(), screenWidth / 2 - 8, offset);
            guiGraphics.blit(ICON, screenWidth / 2 + 2, offset - 4, 16, 16, 16, 16, 16, 16);
            offset += 18;
            for (FormattedCharSequence sequence : split) {
                int width = minecraft.font.width(sequence);
                guiGraphics.drawString(minecraft.font, sequence, (screenWidth - width) / 2, offset, 0xFFFFFF);
                offset += 10;
            }
        }
    }
}
