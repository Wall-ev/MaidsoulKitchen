package com.github.catbert.tlma;

import com.github.catbert.tlma.client.renderer.entity.geckolayer.GeckoLayerMaidLDBanner;
import com.github.catbert.tlma.client.renderer.entity.layer.LayerMaidLDBanner;
import com.github.catbert.tlma.config.subconfig.RegisterConfig;
import com.github.catbert.tlma.entity.backpack.OldBigBackpack;
import com.github.catbert.tlma.foundation.utility.Mods;
import com.github.catbert.tlma.init.InitItems;
import com.github.catbert.tlma.item.bauble.BurnProtectBauble;
import com.github.catbert.tlma.task.cook.v1.bakery.TaskDbkCookingPot;
import com.github.catbert.tlma.task.cook.v1.bakery.TaskDbkStove;
import com.github.catbert.tlma.task.cook.v1.beachparty.TaskDbpMiniFridge;
import com.github.catbert.tlma.task.cook.v1.beachparty.TaskDbpTikiBar;
import com.github.catbert.tlma.task.cook.v1.bnc.TaskBncKey;
import com.github.catbert.tlma.task.cook.v1.candlelight.TaskDclCookingPan;
import com.github.catbert.tlma.task.cook.v1.candlelight.TaskDclCookingPot;
import com.github.catbert.tlma.task.cook.v1.drinkbeer.TaskDbBeerBarrel;
import com.github.catbert.tlma.task.cook.v1.fd.TaskFDCookPot;
import com.github.catbert.tlma.task.cook.v1.herbal.TaskDhbCauldron;
import com.github.catbert.tlma.task.cook.v1.herbal.TaskDhbTeaKettle;
import com.github.catbert.tlma.task.cook.v1.mc.TaskFurnace;
import com.github.catbert.tlma.task.cook.v1.md.TaskMDCopperPot;
import com.github.catbert.tlma.task.cook.v1.vinery.TaskFermentationBarrel;
import com.github.catbert.tlma.task.cook.v1.yhc.TaskYhcMoka;
import com.github.catbert.tlma.task.farm.TaskBerryFarm;
import com.github.catbert.tlma.task.farm.TaskCompatMelonFarm;
import com.github.catbert.tlma.task.farm.TaskFruitFarm;
import com.github.catbert.tlma.task.farm.TaskSSFarm;
import com.github.catbert.tlma.task.other.TaskFeedAndDrinkOwner;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.block.multiblock.MultiBlockManager;
import com.github.tartaricacid.touhoulittlemaid.client.overlay.MaidTipsOverlay;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
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
        if (Mods.MC.isLoaded && RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED.get()){
            manager.add(new TaskCompatMelonFarm());
        }
        if (Mods.MC.isLoaded && RegisterConfig.BERRY_FARM_TASK_ENABLED.get()) {
            manager.add(new TaskBerryFarm());
        }
        if (Mods.MC.isLoaded && RegisterConfig.FRUIT_FARM_TASK_ENABLED.get()) {
            manager.add(new TaskFruitFarm());
        }


        if (Mods.SS.isLoaded && RegisterConfig.SERENESEASONS_FARM_TASK_ENABLED.get()) {
            manager.add(new TaskSSFarm());
        }


        if (Mods.TWT.isLoaded && RegisterConfig.FEED_AND_DRINK_OWNER_TASK_ENABLED.get()) {
            manager.add(new TaskFeedAndDrinkOwner());
        }


        if (Mods.MC.isLoaded && RegisterConfig.FURNACE_TASK_ENABLED.get()) {
            manager.add(new TaskFurnace());
        }


        if (Mods.FD.isLoaded && RegisterConfig.FD_COOK_POT_TASK_ENABLED.get()) {
            manager.add(new TaskFDCookPot());
        }
        if (Mods.MD.isLoaded && RegisterConfig.MD_COOK_POT_TASK_ENABLED.get()) {
            manager.add(new TaskMDCopperPot());
        }
        if (Mods.BNCD.isLoaded && RegisterConfig.BNC_KEY_TASK_ENABLED.get()) {
            manager.add(new TaskBncKey());
        }
        if (Mods.YHCD.isLoaded && RegisterConfig.YHC_MOKA_TASK_ENABLED.get()) {
            manager.add(new TaskYhcMoka());
        }


        if (Mods.DB.isLoaded && RegisterConfig.DB_BEER_TASK_ENABLED.get()) {
            manager.add(new TaskDbBeerBarrel());
        }


        if (Mods.DBK.isLoaded && RegisterConfig.DBK_COOKING_POT_TASK_ENABLED.get()) {
            manager.add(new TaskDbkCookingPot());
        }
        if (Mods.DBK.isLoaded && RegisterConfig.DBK_STOVE_TASK_ENABLED.get()) {
            manager.add(new TaskDbkStove());
        }

        if (Mods.DBP.isLoaded && RegisterConfig.DBP_MINE_FRIDGE_TASK_ENABLED.get()) {
            manager.add(new TaskDbpMiniFridge());
        }
        if (Mods.DBP.isLoaded && RegisterConfig.DBP_TIKI_BAR_TASK_ENABLED.get()) {
            manager.add(new TaskDbpTikiBar());
        }

        if (Mods.DCL.isLoaded && RegisterConfig.DCL_COOKING_POT_TASK_ENABLED.get()) {
            manager.add(new TaskDclCookingPan());
        }
        if (Mods.DCL.isLoaded && RegisterConfig.DCL_COOKING_POT_TASK_ENABLED.get()) {
            manager.add(new TaskDclCookingPot());
        }

        if (Mods.DHB.isLoaded && RegisterConfig.DHB_CAULDRON_TASK_ENABLED.get()) {
            manager.add(new TaskDhbCauldron());
        }
        if (Mods.DHB.isLoaded && RegisterConfig.DHB_TEA_KETTLE_TASK_ENABLED.get()) {
            manager.add(new TaskDhbTeaKettle());
        }

        if (Mods.DV.isLoaded && RegisterConfig.FERMENTATION_BARREL_TASK_ENABLED.get()) {
            manager.add(new TaskFermentationBarrel());
        }
    }

    @Override
    public void bindMaidBauble(BaubleManager manager) {
        if (Mods.MC.isLoaded) {
            manager.bind(InitItems.BURN_PROTECT_BAUBLE, new BurnProtectBauble());
        }
    }

    @Override
    public void addMaidBackpack(BackpackManager manager) {
        if (Mods.MC.isLoaded) {
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

    @OnlyIn(Dist.CLIENT)
    public void addAdditionMaidLayer(EntityMaidRenderer renderer, EntityRendererProvider.Context context) {
        if (Mods.DAPI.isLoaded) {
            renderer.addLayer(new LayerMaidLDBanner(renderer, context.getModelSet()));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void addAdditionGeckoMaidLayer(GeckoEntityMaidRenderer<? extends Mob> renderer, EntityRendererProvider.Context context) {
        if (Mods.DAPI.isLoaded) {
            renderer.addLayer(new GeckoLayerMaidLDBanner<>(renderer, context.getModelSet()));
        }
    }

    @Override
    public void addMaidTips(MaidTipsOverlay maidTipsOverlay) {

    }
}
