package com.github.wallev.maidsoulkitchen.mixin.compat.brewinandchewin;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskMixin;
import com.github.wallev.maidsoulkitchen.task.cook.common.cbaccessor.ICbeAccessor;
import com.github.wallev.maidsoulkitchen.task.cook.common.cbaccessor.IRecipeExperinceAward;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;
import umpaz.brewinandchewin.common.container.AbstractedItemHandler;
import umpaz.brewinandchewin.common.crafting.KegFermentingRecipe;
import umpaz.brewinandchewin.common.utility.KegRecipeWrapper;

import java.util.List;
import java.util.Optional;

@TaskMixin(TaskInfo.BNC_KEY)
@Mixin(value = KegBlockEntity.class)
public abstract class KegBlockEntityMixin implements ICbeAccessor, IRecipeExperinceAward {

    @Shadow
    @Final
    private KegRecipeWrapper recipeWrapper;


    @Shadow
    public abstract AbstractedItemHandler getInventory();

    @Shadow
    protected abstract boolean canFerment(KegFermentingRecipe recipe, KegBlockEntity keg);

    @Shadow public abstract List<Recipe<?>> getUsedRecipesAndPopExperience(Level level, Vec3 pos);

    @Shadow @Final private Object2IntOpenHashMap<ResourceLocation> usedRecipeTracker;

    @Shadow protected abstract Optional<RecipeHolder<KegFermentingRecipe>> getMatchingRecipe(KegRecipeWrapper inventoryWrapper);

    @Override
    public boolean tlmk$innerCanCook() {
        Optional<RecipeHolder<KegFermentingRecipe>> matchingRecipe = this.getMatchingRecipe(recipeWrapper);
        return matchingRecipe.isPresent() && this.canFerment(matchingRecipe.get().value(), (KegBlockEntity) (Object) this);
    }

    @Override
    public void tlmk$awardExperience(Entity entity) {
        this.getUsedRecipesAndPopExperience(entity.level, entity.position());
        this.usedRecipeTracker.clear();
    }
}
