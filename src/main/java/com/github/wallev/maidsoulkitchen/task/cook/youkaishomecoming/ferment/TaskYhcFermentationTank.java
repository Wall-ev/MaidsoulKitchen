package com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming.ferment;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.task.CookTask;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.FluidPotCookRule2;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import dev.xkmc.youkaishomecoming.content.pot.ferment.FermentationRecipe;
import dev.xkmc.youkaishomecoming.content.pot.ferment.FermentationTankBlockEntity;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@TaskClassAnalyzer(TaskInfo.YHC_FERMENTATION_TANK)
public class TaskYhcFermentationTank extends ICookTask<FermentationTankBlockEntity, FermentationRecipe<?>> {

    @Override
    protected CookBeBase<FermentationTankBlockEntity> createCookBe(EntityMaid maid) {
        return new FermentationCookBe(maid);
    }

    @Override
    protected AbstractCookRule<FermentationTankBlockEntity, FermentationRecipe<?>> createCookRule() {
        return FluidPotCookRule2.getInstance();
    }

    @Override
    protected RecSerializerManager<FermentationRecipe<?>> createRecSerializerManager() {
        return FermentationRecSerializerManager.getInstance();
    }

    @Override
    public ResourceLocation getUid() {
        return CookTask.YHC_FERMENTATION_TANK.uid;
    }

    @Override
    public ItemStack getIcon() {
        return YHBlocks.FERMENT.asStack();
    }
}
