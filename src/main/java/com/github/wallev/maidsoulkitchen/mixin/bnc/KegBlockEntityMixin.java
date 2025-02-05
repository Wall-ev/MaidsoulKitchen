package com.github.wallev.maidsoulkitchen.mixin.bnc;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor.ICbeAccessor;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor.IRecipeExperinceAward;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;
import umpaz.brewinandchewin.common.crafting.KegFermentingRecipe;
import umpaz.brewinandchewin.common.utility.KegRecipeWrapper;

import java.util.List;
import java.util.Optional;

@Mixin(value = KegBlockEntity.class, remap = false)
public abstract class KegBlockEntityMixin implements ICbeAccessor, IRecipeExperinceAward {

    @Shadow
    @Final
    private KegRecipeWrapper recipeWrapper;

    @Shadow
    protected abstract Optional<KegFermentingRecipe> getMatchingRecipe(KegRecipeWrapper inventoryWrapper);

    @Shadow
    public abstract ItemStackHandler getInventory();

    @Shadow
    protected abstract boolean canFerment(KegFermentingRecipe recipe, KegBlockEntity keg);

    @Shadow public abstract List<Recipe<?>> getUsedRecipesAndPopExperience(Level level, Vec3 pos);

    @Shadow @Final private Object2IntOpenHashMap<ResourceLocation> usedRecipeTracker;

    /**
     * 烹饪的厨具内部是否可以烹饪（仅仅是与配方原料相关的判断，不包括外部条件，比如农夫乐事的需要Heated状态）
     *
     * @return 是否可以烹饪
     */
    @Override
    public boolean innerCanCook$tlma() {
        Optional<KegFermentingRecipe> matchingRecipe = this.getMatchingRecipe(recipeWrapper);
        return matchingRecipe.isPresent() && this.canFerment(matchingRecipe.get(), (KegBlockEntity) (Object) this);
    }

    @Override
    public void awardExperience$tlma(EntityMaid maid) {
        this.getUsedRecipesAndPopExperience(maid.level, maid.position());
        this.usedRecipeTracker.clear();
    }
}
