package com.github.catbert.tlma;

import com.github.catbert.tlma.client.renderer.entity.geckolayer.GeckoLayerMaidLDBanner;
import com.github.catbert.tlma.client.renderer.entity.layer.LayerMaidLDBanner;
import com.github.catbert.tlma.entity.backpack.OldBigBackpack;
import com.github.catbert.tlma.foundation.utility.Mods;
import com.github.catbert.tlma.init.InitItems;
import com.github.catbert.tlma.init.registry.tlm.RegisterData;
import com.github.catbert.tlma.init.registry.tlm.RegisterTask;
import com.github.catbert.tlma.item.bauble.BurnProtectBauble;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.block.multiblock.MultiBlockManager;
import com.github.tartaricacid.touhoulittlemaid.client.overlay.MaidTipsOverlay;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.data.TaskDataRegister;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.entity.task.meal.MaidMealManager;
import com.github.tartaricacid.touhoulittlemaid.inventory.chest.ChestManager;
import com.github.tartaricacid.touhoulittlemaid.item.bauble.BaubleManager;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@LittleMaidExtension
public final class MaidPlugin implements ILittleMaid {

    @Override
    public void addMaidTask(TaskManager manager) {
        RegisterTask.register(manager);
    }

    @Override
    public void bindMaidBauble(BaubleManager manager) {
        if (Mods.MC.isLoaded()) {
            manager.bind(InitItems.BURN_PROTECT_BAUBLE, new BurnProtectBauble());
        }
    }

    @Override
    public void addMaidBackpack(BackpackManager manager) {
        if (Mods.MC.isLoaded()) {
            manager.add(new OldBigBackpack());
        }
    }

    @Override
    public void addMultiBlock(MultiBlockManager manager) {

    }

    @Override
    public void addChestType(ChestManager manager) {

    }

    @Override
    public void addMaidMeal(MaidMealManager manager) {

    }

    @Override
    public void registerTaskData(TaskDataRegister register) {
        RegisterData.register(register);
    }

    @OnlyIn(Dist.CLIENT)
    public void addAdditionMaidLayer(EntityMaidRenderer renderer, EntityRendererProvider.Context context) {
        if (Mods.DAPI.isLoaded()) {
            renderer.addLayer(new LayerMaidLDBanner(renderer, context.getModelSet()));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void addAdditionGeckoMaidLayer(GeckoEntityMaidRenderer<? extends Mob> renderer, EntityRendererProvider.Context context) {
        if (Mods.DAPI.isLoaded()) {
            renderer.addLayer(new GeckoLayerMaidLDBanner<>(renderer, context.getModelSet()));
        }
    }

    @Override
    public void addMaidTips(MaidTipsOverlay maidTipsOverlay) {

    }
}
