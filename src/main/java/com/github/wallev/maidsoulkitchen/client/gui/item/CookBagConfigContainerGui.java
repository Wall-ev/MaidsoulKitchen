package com.github.wallev.maidsoulkitchen.client.gui.item;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.client.gui.widget.button.CookBagModeButton;
import com.github.wallev.maidsoulkitchen.handler.VButton;
import com.github.wallev.maidsoulkitchen.handler.VComponent;
import com.github.wallev.maidsoulkitchen.handler.util.VGuiRendererHelper;
import com.github.wallev.maidsoulkitchen.inventory.container.item.BagType;
import com.github.wallev.maidsoulkitchen.inventory.container.item.CookBagConfigContainer;
import com.github.wallev.maidsoulkitchen.item.ItemCulinaryHub;
import com.github.wallev.maidsoulkitchen.network.NetworkHandler;
import com.github.wallev.maidsoulkitchen.network.message.ClearCookBagBindPosesMessage;
import com.github.wallev.maidsoulkitchen.network.message.SetCookBagBindModeMessage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;
import org.anti_ad.mc.ipn.api.IPNIgnore;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@IPNIgnore
public class CookBagConfigContainerGui extends CookBagAbstractContainerGui<CookBagConfigContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MaidsoulKitchen.MOD_ID, "textures/gui/cook_guide.png");

    protected String bindMode;

    public CookBagConfigContainerGui(CookBagConfigContainer container, Inventory inv, Component titleIn) {
        super(container, inv, VComponent.translatable("gui.maidsoulkitchen.culinary_hub.config.title"));
        this.bindMode = ItemCulinaryHub.getBindMode(menu.cookBag);
    }

    @Override
    protected void init() {
        super.init();

        this.addBindModeButtons();
        this.addInfoButton();
    }

    private void addBindModeButtons() {
        int x = leftPos + 6;
        int y = topPos + 6;
        for (BagType value : BagType.values()) {
            MutableComponent title = VComponent.translatable("gui.maidsoulkitchen.culinary_hub.config.bind_mode." + value.translateKey);

            if (value == BagType.INGREDIENT_ADDITION || value == BagType.START_ADDITION) {
                title.append(VComponent.translatable("gui.maidsoulkitchen.development")).withStyle(ChatFormatting.YELLOW);
            }

            CookBagModeButton cookBagModeButton = new CookBagModeButton(x, y += 22, 100, 20, title, b -> {
            }, VComponent.translatable("gui.maidsoulkitchen.culinary_hub.config.bind_mode." + value.translateKey + ".tooltip")) {
                @Override
                public void onClick(double pMouseX, double pMouseY) {
                    super.onClick(pMouseX, pMouseY);
                    bindMode = value.name;
                    NetworkHandler.sendToServer(new SetCookBagBindModeMessage(bindMode));
                }

                @Override
                public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
                    if (this.visible) {
                        this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                        Minecraft minecraft = Minecraft.getInstance();
                        RenderSystem.setShader(GameRenderer::getPositionTexShader);
                        RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
                        int yImageOffset = this.getYImage(this.isHoveredOrFocused());
                        RenderSystem.enableBlend();
                        RenderSystem.defaultBlendFunc();
                        RenderSystem.enableDepthTest();
                        this.blit(poseStack, this.x, this.y, 0, 46 + yImageOffset * 20, this.width / 2, this.height);
                        this.blit(poseStack, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + yImageOffset * 20, this.width / 2, this.height);
                        this.renderBg(poseStack, minecraft, mouseX, mouseY);
                    }

                    if (this.visible) {
                        int color = getFGColor();
                        if (Objects.equals(bindMode, value.name)) {
                            color = Color.GREEN.getRGB();
                        }
                        VGuiRendererHelper.renderScrollingString(poseStack, font, this.getMessage(), this, color | Mth.ceil(this.alpha * 255.0F) << 24);
                    }

                    if (this.visible && this.isHovered) {
                        this.renderToolTip(poseStack, mouseX, mouseY);
                    }

                }
            };

            if (value == BagType.INGREDIENT_ADDITION || value == BagType.START_ADDITION) {
                cookBagModeButton.active = false;
            }

            this.addRenderableWidget(cookBagModeButton);
        }

        Button clearButton = VButton.builder(VComponent.translatable("gui.maidsoulkitchen.culinary_hub.config.clear_bind_poses").withStyle(ChatFormatting.YELLOW), b -> {
                    NetworkHandler.sendToServer(new ClearCookBagBindPosesMessage());
                    onClose();
                })
                .bounds(x, y += 22, 100, 20)
                .tooltip(VComponent.translatable("gui.maidsoulkitchen.culinary_hub.config.clear_bind_poses.tooltip"))
                .build();
        this.addRenderableWidget(clearButton);
    }

    private void addInfoButton() {
        int x = leftPos + imageWidth;
        int y = topPos + 5;
        ImageButton infoButton = new ImageButton(x - 15, y, 9, 9, 237 - 10, 212, 10, TEXTURE, (b) -> {
        });
        MutableComponent mutableComponent = VComponent.translatable("tooltips.maidsoulkitchen.culinary_hub.desc.usage").withStyle(ChatFormatting.GREEN);
        mutableComponent.append(CommonComponents.NEW_LINE);
        mutableComponent.append(VComponent.translatable("tooltips.maidsoulkitchen.culinary_hub.desc.usage.1").withStyle(ChatFormatting.GRAY));
        mutableComponent.append(CommonComponents.NEW_LINE);
        mutableComponent.append(VComponent.translatable("tooltips.maidsoulkitchen.culinary_hub.desc.usage.2").withStyle(ChatFormatting.GRAY));
        mutableComponent.append(CommonComponents.NEW_LINE);
        mutableComponent.append(VComponent.translatable("tooltips.maidsoulkitchen.culinary_hub.desc.usage.3").withStyle(ChatFormatting.GRAY));
        infoButton.setMessage(mutableComponent);
        this.addRenderableWidget(infoButton);

        Map<BagType, java.util.List<BlockPos>> bindPoses = ItemCulinaryHub.getBindPoses(this.menu.cookBag);
        List<BagType> leftBindBagTypes = new ArrayList<>();
        bindPoses.forEach((type, poses) -> {
            if (poses.isEmpty() && !(type == BagType.INGREDIENT_ADDITION || type == BagType.START_ADDITION)) {
                leftBindBagTypes.add(type);
            }
        });
        if (bindPoses.isEmpty() || leftBindBagTypes.size() == BagType.values().length - 2) {
            MutableComponent mutableComponent1 = VComponent.translatable("tooltips.maidsoulkitchen.culinary_hub.desc.warn").withStyle(ChatFormatting.YELLOW);
            mutableComponent1.append(CommonComponents.NEW_LINE);
            mutableComponent1.append(VComponent.translatable("tooltips.maidsoulkitchen.culinary_hub.desc.warn.empty").withStyle(ChatFormatting.GRAY));

            ImageButton warnButton = new ImageButton(x - 15 - 10, y, 9, 9, 237, 212, 10, TEXTURE, (b) -> {
            });
            warnButton.setMessage(mutableComponent1);
            this.addRenderableWidget(warnButton);
        } else if (!leftBindBagTypes.isEmpty()) {
            MutableComponent leftComponent1 = VComponent.empty();
            boolean first = true;
            for (BagType value : leftBindBagTypes) {
                if (first) {
                    leftComponent1.append(VComponent.translatable("gui.maidsoulkitchen.culinary_hub.config.bind_mode." + value.translateKey).withStyle(ChatFormatting.GRAY));
                    first = false;
                } else {
                    leftComponent1.append(VComponent.literal("、").append(VComponent.translatable("gui.maidsoulkitchen.culinary_hub.config.bind_mode." + value.translateKey).withStyle(ChatFormatting.GRAY)));
                }
            }
            MutableComponent leftComponent = VComponent.literal("[").append(leftComponent1).append(VComponent.literal("]").withStyle(ChatFormatting.GRAY));

            MutableComponent mutableComponent1 = VComponent.translatable("tooltips.maidsoulkitchen.culinary_hub.desc.warn").withStyle(ChatFormatting.YELLOW);
            mutableComponent1.append(CommonComponents.NEW_LINE);
            mutableComponent1.append(VComponent.translatable("tooltips.maidsoulkitchen.culinary_hub.desc.warn.left", leftComponent).withStyle(ChatFormatting.GRAY));

            ImageButton warnButton = new ImageButton(x - 15 - 10, y, 9, 9, 237, 212, 10, TEXTURE, (b) -> {
            });
            warnButton.setMessage(mutableComponent1);
            this.addRenderableWidget(warnButton);
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.render(poseStack, mouseX, mouseY, partialTick);

        int x = leftPos + 6;
        int y = topPos + 6;
        int width = 100;
        getMinecraft().getItemRenderer().renderGuiItem(Items.RED_MUSHROOM.getDefaultInstance(), x + width + 2, y + 22);
        getMinecraft().getItemRenderer().renderGuiItem(Items.BROWN_MUSHROOM.getDefaultInstance(), x + width + 16 + 2, y + 22);
        getMinecraft().getItemRenderer().renderGuiItem(Items.COAL.getDefaultInstance(), x + width + 2, y + 44 + 2);
        getMinecraft().getItemRenderer().renderGuiItem(Items.WATER_BUCKET.getDefaultInstance(), x + width + 16 + 2, y + 44 + 2);
        getMinecraft().getItemRenderer().renderGuiItem(Items.BOWL.getDefaultInstance(), x + width + 2, y + 88 + 2);
        getMinecraft().getItemRenderer().renderGuiItem(Items.GLASS_BOTTLE.getDefaultInstance(), x + width + 16 + 2, y + 88 + 2);
        getMinecraft().getItemRenderer().renderGuiItem(Items.MUSHROOM_STEW.getDefaultInstance(), x + width + 2, y + 110 + 2);
    }

    protected void renderLabels(PoseStack poseStack, int pMouseX, int pMouseY) {
        font.draw(poseStack, VComponent.translatable("gui.maidsoulkitchen.culinary_hub.config.bind_mode"), this.titleLabelX, this.titleLabelY + 12, 4210752);
        font.draw(poseStack, this.titleComponent, this.titleLabelX, this.titleLabelY, 4210752);
        font.draw(poseStack, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, CONFIG_BACKGROUND);

        int middleX = (this.width - this.imageWidth) / 2;
        int middleY = (this.height - this.imageHeight) / 2;
        blit(poseStack, middleX, middleY, 0, 0, this.imageWidth, this.imageHeight);
        super.renderBg(poseStack, partialTick, mouseX, mouseY);
    }
}
