package com.github.wallev.farmsoulkitchen.entity.backpack;

import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import com.github.wallev.farmsoulkitchen.api.ILittleMaidBackpack;
import com.github.wallev.farmsoulkitchen.client.model.backpack.OldBigBackpackModel;
import com.github.wallev.farmsoulkitchen.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BigBackpack;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;


public class OldBigBackpack extends BigBackpack implements ILittleMaidBackpack {
    public static final ResourceLocation ID = new ResourceLocation(FarmsoulKitchen.MOD_ID, "old_big_backpack");

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public EntityModel<EntityMaid> getBackpackModel(EntityModelSet modelSet) {
        return new OldBigBackpackModel(modelSet.bakeLayer(OldBigBackpackModel.LAYER));
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getBackpackTexture() {
        return new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/old_maid_backpack_big.png");
    }

    @OnlyIn(Dist.CLIENT)
    public void offsetBackpackItem(PoseStack poseStack) {
        poseStack.translate(0, 0, -0.4);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Item getItem() {
        return InitItems.OLD_MAID_BACKPACK_BIG.get();
    }
}