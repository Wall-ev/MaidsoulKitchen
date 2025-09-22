package com.github.wallev.maidsoulkitchen;

import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.block.multiblock.MultiBlockManager;
import com.github.tartaricacid.touhoulittlemaid.client.overlay.MaidTipsOverlay;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.debug.target.DebugTarget;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.ExtraMaidBrainManager;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.data.TaskDataRegister;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.entity.task.meal.MaidMealManager;
import com.github.tartaricacid.touhoulittlemaid.inventory.chest.ChestManager;
import com.github.tartaricacid.touhoulittlemaid.item.bauble.BaubleManager;
import com.github.wallev.maidsoulkitchen.chest.FarmDelightCabinet;
import com.github.wallev.maidsoulkitchen.client.renderer.entity.layer.banner.LayerRendererManager;
import com.github.wallev.maidsoulkitchen.client.renderer.entity.layer.bedrock.LayerMaidBanner;
import com.github.wallev.maidsoulkitchen.client.renderer.entity.layer.gecko.GeckoLayerMaidBanner;
import com.github.wallev.maidsoulkitchen.debug.target.DefaultTargets;
import com.github.wallev.maidsoulkitchen.entity.ai.brain.MaidBrain;
import com.github.wallev.maidsoulkitchen.init.ModItems;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.DataRegister;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.TaskRegister;
import com.github.wallev.maidsoulkitchen.item.bauble.BurnProtectBauble;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.task.MaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.util.DevUtil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@LittleMaidExtension
public final class MaidPlugin implements ILittleMaid {

    public MaidPlugin() {
        MaidsoulKitchenTask.init();
        if (FMLEnvironment.dist == Dist.CLIENT && DevUtil.isDevEnv()) {
            LayerRendererManager.init();
        }
    }

    @Override
    public void addMaidTask(TaskManager manager) {
        TaskRegister.init(manager);
    }

    @Override
    public void bindMaidBauble(BaubleManager manager) {
        if (Mods.MC.load()) {
            manager.bind(ModItems.BURN_PROTECT_BAUBLE, new BurnProtectBauble());
        }
    }

    @Override
    public void addMaidBackpack(BackpackManager manager) {

    }

    @Override
    public void addMultiBlock(MultiBlockManager manager) {

    }

    @Override
    public void addChestType(ChestManager manager) {
        if (Mods.FD.load()) {
            manager.add(new FarmDelightCabinet());
        }
    }

    @Override
    public void addMaidMeal(MaidMealManager manager) {

    }

    @Override
    public void registerTaskData(TaskDataRegister register) {
        DataRegister.init(register);
    }

    @OnlyIn(Dist.CLIENT)
    public void addAdditionMaidLayer(EntityMaidRenderer renderer, EntityRendererProvider.Context context) {
        if (!DevUtil.isDevEnv()) {
            return;
        }
        renderer.addLayer(new LayerMaidBanner(renderer, context.getModelSet()));
    }

    @OnlyIn(Dist.CLIENT)
    public void addAdditionGeckoMaidLayer(GeckoEntityMaidRenderer<? extends Mob> renderer, EntityRendererProvider.Context context) {
        if (!DevUtil.isDevEnv()) {
            return;
        }
        renderer.addGeoLayerRenderer(new GeckoLayerMaidBanner<>(renderer, context.getModelSet()));
    }

    @Override
    public void addMaidTips(MaidTipsOverlay maidTipsOverlay) {

    }

    @Override
    public void addExtraMaidBrain(ExtraMaidBrainManager manager) {
        manager.addExtraMaidBrain(new MaidBrain());
    }

    @Override
    public Collection<? extends Function<EntityMaid, List<DebugTarget>>> getMaidDebugTargets() {
        return DefaultTargets.getDefaultTargets();
    }
}
