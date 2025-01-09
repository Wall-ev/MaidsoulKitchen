package com.github.wallev.maidsoulkitchen.task.cook.handler.v2;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class ICookRecSerializer<R extends Recipe<? extends Container>> {
    private final List<R> cookRecs = Lists.newArrayList();
    private final Set<Item> validIngres = Sets.newHashSet();
    private final Map<R, ? extends CookRec<R>> cookRecData = Maps.newHashMap();

    /**
     * 获取对应的配方类型
     */
    public abstract RecipeType<R> getRecipeType();

    /**
     * 初始化配方信息，应该建立缓存，以便减少运行时的压力
     */
    public abstract void initialize(Level level);

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

    public abstract List<R> getRecipes();

    public void clear() {
        cookRecs.clear();
        validIngres.clear();
        cookRecData.clear();
    }

    public record CookRec<R extends Recipe<? extends Container>>(R recipe, List<List<Item>> ingredients, List<Item> result) {
    }

    public record CookRecInfo<R extends Recipe<? extends Container>>(R recipe, List<Item> ingredients, Item result) {

    }
}
