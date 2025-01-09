package com.github.wallev.maidsoulkitchen.handler.serializer.rule;

import com.github.wallev.maidsoulkitchen.handler.rec.CookRec;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractCookRecSerializer<R extends Recipe<? extends Container>> {
    protected final RecipeType<R> recipeType;
    protected final List<R> cookRecs = Lists.newArrayList();
    protected final Set<Item> validIngres = Sets.newHashSet();
    protected final Map<R, CookRec<R>> cookRecData = Maps.newHashMap();

    public AbstractCookRecSerializer(RecipeType<R> recipeType) {
        this.recipeType = recipeType;
    }

    /**
     * 获取对应的配方类型
     */
    public RecipeType<R> getRecipeType() {
        return recipeType;
    }

    /**
     * 初始化配方信息，应该建立缓存，以便减少运行时的压力
     */
    protected abstract void initialize(Level level);

    public void init(Level level) {
        this.clear();
        this.initialize(level);
    }

    protected void initRecipes(Level level) {
        List<R> recipes = this.getRecipes(level);
        this.cookRecs.addAll(recipes);
    }

    /**
     * 获取所有应该符合原料的物品，包括所有配方的原料
     * 这应该在link{ICookRecSerializer#initialize}的时候初始化
     * 同理也应该使用缓存
     */
    public Set<Item> getValidIngres() {
        return validIngres;
    }

    /**
     * 获取所有应该符合原料的物品，包括所有配方的原料
     * 这应该在link{ICookRecSerializer#initialize}的时候初始化
     * 同理也应该使用缓存
     */
    public Set<Item> convertValidIngres(Set<Item> oriIngres) {
        oriIngres.retainAll(this.validIngres);
        return oriIngres;
    }

    /**
     * 获取对应配方类型的所有配方
     * 这应该和initialize一起使用
     * @param level Level
     * @return 对应的配方类型的所有配方
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected List<R> getRecipes(Level level) {
        return level.getRecipeManager().getAllRecipesFor((RecipeType) getRecipeType());
    }

    public List<R> getRecipes() {
        return cookRecs;
    }

    protected List<Ingredient> getIngredients(R recipe) {
        return recipe.getIngredients();
    }

    protected ItemStack getResultItem(R recipe, Level level){
            return recipe.getResultItem(level.registryAccess());
    }

    public void clear() {
        cookRecs.clear();
        validIngres.clear();
        cookRecData.clear();
    }
}
