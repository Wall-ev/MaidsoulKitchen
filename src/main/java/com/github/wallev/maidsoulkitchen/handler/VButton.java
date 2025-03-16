package com.github.wallev.maidsoulkitchen.handler;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class VButton extends Button {

    public VButton(Builder builder) {
        super(builder.x, builder.y, builder.width, builder.height, builder.message, builder.onPress, builder.onTooltip);
    }

    public static Builder builder(Component pMessage, OnPress pOnPress) {
        return new Builder(pMessage, pOnPress);
    }

    public static Button builder(Builder builder) {
        return new Button(builder.x, builder.y, builder.width, builder.height, builder.message, builder.onPress);
    }

    public static Button builder(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress) {
        return new Button(pX, pY, pWidth, pHeight, pMessage, pOnPress);
    }

    public static Button builder(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress, OnTooltip pOnTooltip) {
        return new Button(pX, pY, pWidth, pHeight, pMessage, pOnPress, pOnTooltip);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Builder {
        private final Component message;
        private final OnPress onPress;
        protected OnTooltip onTooltip = Button.NO_TOOLTIP;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;

        public Builder(Component pMessage, OnPress pOnPress) {
            this.message = pMessage;
            this.onPress = pOnPress;
        }

        public Builder pos(int pX, int pY) {
            this.x = pX;
            this.y = pY;
            return this;
        }

        public Builder width(int pWidth) {
            this.width = pWidth;
            return this;
        }

        public Builder size(int pWidth, int pHeight) {
            this.width = pWidth;
            this.height = pHeight;
            return this;
        }

        public Builder bounds(int pX, int pY, int pWidth, int pHeight) {
            return this.pos(pX, pY).size(pWidth, pHeight);
        }

        public Builder tooltip(List<Component> components) {
            this.onTooltip = (pButton, pPoseStack, pMouseX, pMouseY) -> {
                Screen screen = Minecraft.getInstance().screen;
                if (screen != null) {
                    screen.renderComponentTooltip(pPoseStack, components, pMouseX, pMouseY);
                }
            };
            return this;
        }

        public Builder tooltip(Component component) {
            this.onTooltip = (pButton, pPoseStack, pMouseX, pMouseY) -> {
                Screen screen = Minecraft.getInstance().screen;
                if (screen != null) {
                    screen.renderComponentTooltip(pPoseStack, Lists.newArrayList(component), pMouseX, pMouseY);
                }
            };
            return this;
        }

        public Button build() {
            return build(builder -> new Button(x, y, width, height, message, onPress, onTooltip));
        }

        public Button build(java.util.function.Function<Builder, Button> builder) {
            return builder.apply(this);
        }
    }

}
