package com.github.catbert.tlma.client.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber
public class PatchouliScreenEvent {
    private static final int SIZE = 6;
//    private static int solIndex = 0;

    @SubscribeEvent
    public static void screenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof GuiBook patchouliScreen) {
            int solIndex = 0;
            final int startX = -26, startY = 40;
            PatchouliAPI.IPatchouliAPI iPatchouliAPI = PatchouliAPI.get();
            Map<ResourceLocation, Book> books = BookRegistry.INSTANCE.books;

            Book guiBook = patchouliScreen.book;

            int leftPos = patchouliScreen.bookLeft;
            int topPos = patchouliScreen.bookTop;

            List<Book> values = books.values().stream().toList();
            List<GuiEventListener> guiEventListeners = new ArrayList<>();

            generateBookButton(values, leftPos, startX, topPos, startY, solIndex, iPatchouliAPI, guiBook, guiEventListeners);
            guiEventListeners.forEach(event::addListener);

////            if (values.size() > SIZE) {
//                Button preButton = Button.builder(Component.literal("^"), pButton -> {
//                    guiEventListeners.forEach(event::removeListener);
//                    guiEventListeners.clear();
////                    solIndex--;
//                    generateBookButton(values, leftPos, startX, topPos, startY, solIndex, iPatchouliAPI, guiBook, guiEventListeners);
//                    guiEventListeners.forEach(event::addListener);
//                }).bounds(leftPos - 13, topPos + GuiBook.FULL_HEIGHT - 22 - 5, 13, 10).build();
//                Button nextButton = Button.builder(Component.literal("*"), pButton -> {
//                    guiEventListeners.forEach(event::removeListener);
//                    guiEventListeners.clear();
////                    solIndex++;
//                    generateBookButton(values, leftPos, startX, topPos, startY, solIndex, iPatchouliAPI, guiBook, guiEventListeners);
//                    guiEventListeners.forEach(event::addListener);
//                }).bounds(leftPos - 13, topPos + GuiBook.FULL_HEIGHT - 10 - 5, 13, 10).build();
//                event.addListener(preButton);
//                event.addListener(nextButton);
////            }
        }
    }

    private static void generateBookButton(List<Book> values, int leftPos, int startX, int topPos, int startY, int solIndex, PatchouliAPI.IPatchouliAPI iPatchouliAPI, Book guiBook, List<GuiEventListener> guiEventListeners) {
        int index = 0;
        for (Book book : values.subList(solIndex * SIZE, values.size() > SIZE ? (solIndex + 1) * SIZE : values.size())) {
            ResourceLocation bookTexture = book.bookTexture;
            ImageButton imageButton = new ImageButton(leftPos + startX, topPos + startY + index++ * 22, 26, 20, 0, 0, bookTexture, (b) -> iPatchouliAPI.openBookGUI(book.id)) {
                @Override
                public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
                    int uOffset = 272, vOffset = 160, uWidth = 11, uHeight = 10;
                    int itemXOffset = 8;
                    if (guiBook == book || this.isHovered) {
                        uOffset = 285;
                        uWidth = 13;
                        itemXOffset = 4;
                    }

                    RenderSystem.enableDepthTest();
                    graphics.pose().pushPose();
                    graphics.pose().translate(getX() + width, getY() + height, 0);
                    graphics.pose().scale(2, 2, 1);
                    graphics.pose().mulPose(Axis.ZP.rotationDegrees(180));
                    graphics.blit(this.resourceLocation, 0, 0, uOffset, vOffset, uWidth, uHeight, 512, 256);
                    graphics.pose().popPose();

                    graphics.renderItem(book.getBookItem(), getX() + itemXOffset, getY() + 2);

                    if (this.isHovered) {
                        graphics.renderTooltip(Minecraft.getInstance().font, book.getBookItem(), mouseX, mouseY);
                    }
                }
            };
            guiEventListeners.add(imageButton);
        }
    }
}
