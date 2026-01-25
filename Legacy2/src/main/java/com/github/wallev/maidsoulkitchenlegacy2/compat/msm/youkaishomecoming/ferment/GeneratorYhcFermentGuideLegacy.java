package com.github.wallev.maidsoulkitchenlegacy2.compat.msm.youkaishomecoming.ferment;

import com.github.wallev.maidsoulkitchen.compat.msm.youkaishomecoming.ferment.GeneratorYhcFermentGuide;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.LegacyAutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import dev.xkmc.youkaishomecoming.content.pot.ferment.FermentationRecipe;

@LegacyAutoCraftGuideGeneratorRegister(TaskInfo.MSM_YHC_FERMENT_LEGACY)
public class GeneratorYhcFermentGuideLegacy extends GeneratorYhcFermentGuide {

    @Override
    public int getRecipeTime(FermentationRecipe<?> recipe) {
        return recipe.getFermentationTime();
    }
}
