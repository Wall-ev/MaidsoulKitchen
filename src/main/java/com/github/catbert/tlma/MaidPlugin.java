package com.github.catbert.tlma;

import com.github.catbert.tlma.client.renderer.entity.geckolayer.GeckoLayerMaidLDBanner;
import com.github.catbert.tlma.client.renderer.entity.layer.LayerMaidLDBanner;
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
import com.github.catbert.tlma.util.ActionUtil;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.api.bauble.IChestType;
import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.api.block.IMultiBlock;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.api.task.meal.IMaidMeal;
import com.github.tartaricacid.touhoulittlemaid.api.task.meal.MaidMealType;
import com.github.tartaricacid.touhoulittlemaid.block.multiblock.MultiBlockManager;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.entity.task.meal.MaidMealManager;
import com.github.tartaricacid.touhoulittlemaid.inventory.chest.ChestManager;
import com.github.tartaricacid.touhoulittlemaid.item.bauble.BaubleManager;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.RegistryObject;

@LittleMaidExtension
public final class MaidPlugin implements ILittleMaid {

    @Override
    public void addMaidTask(TaskManager manager) {
        load(Mods.MC, new TaskBerryFarm(), manager);
        load(Mods.MC, new TaskFruitFarm(), manager);
        load(Mods.MC, new TaskCompatMelonFarm(), manager);

        load(Mods.SS, new TaskSSFarm(), manager);

        load(Mods.TWT, new TaskFeedAndDrinkOwner(), manager);

        load(Mods.MC, new TaskFurnace(), manager);

        load(Mods.FD, new TaskFDCookPot(), manager);
        load(Mods.MD, new TaskMDCopperPot(), manager);
        load(Mods.BNCD, new TaskBncKey(), manager);
        load(Mods.YHCD, new TaskYhcMoka(), manager);

        load(Mods.DB, new TaskDbBeerBarrel(), manager);

        load(Mods.DBK, new TaskDbkCookingPot(), manager);
        load(Mods.DBK, new TaskDbkStove(), manager);
        load(Mods.DBP, new TaskDbpMiniFridge(), manager);
        load(Mods.DBP, new TaskDbpTikiBar(), manager);
        load(Mods.DCL, new TaskDclCookingPan(), manager);
        load(Mods.DCL, new TaskDclCookingPot(), manager);
        load(Mods.DHB, new TaskDhbCauldron(), manager);
        load(Mods.DHB, new TaskDhbTeaKettle(), manager);
        load(Mods.DV, new TaskFermentationBarrel(), manager);
    }

    @Override
    public void bindMaidBauble(BaubleManager manager) {
        load(Mods.MC, InitItems.BURN_PROTECT_BAUBLE, new BurnProtectBauble(), manager);
    }

    @Override
    public void addMaidBackpack(BackpackManager manager) {
        load(Mods.MC, new OldBigBackpack(), manager);
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
        ActionUtil.modRun(Mods.DAPI, () -> {
            renderer.addLayer(new LayerMaidLDBanner(renderer, context.getModelSet()));
        });
    }

    @OnlyIn(Dist.CLIENT)
    public void addAdditionGeckoMaidLayer(GeckoEntityMaidRenderer<? extends Mob> renderer, EntityRendererProvider.Context context) {
        ActionUtil.modRun(Mods.DAPI, () -> {
            renderer.addLayer(new GeckoLayerMaidLDBanner<>(renderer, context.getModelSet()));
        });
    }

    private static void load(Mods mod, IMaidTask task, TaskManager manager) {
        if (mod.isLoaded) {
            manager.add(task);
        }
    }

    private static void load(Mods mod, RegistryObject<Item> item, IMaidBauble bauble, BaubleManager manager) {
        if (mod.isLoaded) {
            manager.bind(item, bauble);
        }
    }


    private static void load(Mods mod, Item item, IMaidBauble bauble, BaubleManager manager) {
        if (mod.isLoaded) {
            manager.bind(item, bauble);
        }
    }

    private static void load(Mods mod, IMaidBackpack backpack, BackpackManager manager) {
        if (mod.isLoaded) {
            manager.add(backpack);
        }
    }

    private static void load(Mods mod, IMultiBlock multiBlock, MultiBlockManager manager) {
        if (mod.isLoaded) {
            manager.add(multiBlock);
        }
    }

    private static void load(Mods mod, IChestType chestType, ChestManager manager) {
        if (mod.isLoaded) {
            manager.add(chestType);
        }
    }

    private static void load(Mods mod, MaidMealType type, IMaidMeal maidMeal, MaidMealManager manager) {
        if (mod.isLoaded) {
            manager.addMaidMeal(type, maidMeal);
        }
    }
}
