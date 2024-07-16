package com.github.catbert.tlma.api;

import com.github.catbert.tlma.entity.passive.CookTaskData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.FakePlayer;

import java.lang.ref.WeakReference;

public interface IAddonMaid {

    WeakReference<FakePlayer> getFakePlayer$tlma();

    void initFakePlayer$tlma();

    CompoundTag getAddonMaidData$tlma();

    void setAddonMaidData$tlma(CompoundTag nbt);

    Integer getStartYOffset$tlma();

    void setStartYOffset$tlma(int offset);

    boolean openMaidGuiFromSideTab(Player player, int tabIndex);

    MenuProvider getGuiProviderFromSideTab(int tabIndex);

    void addOrRemoveRecipe(String recipeId);

    CompoundTag getCookTaskTag();

    ListTag getRecipeListTag();

    CookTaskData getCookTaskData1();

    void addOrRemoveRecipe1(String recipeId);

    boolean containsRecipe1(String recipeId);

    void addOrRemoveRecipe2(String recipeId);

    boolean containsRecipe2(String recipeId);

}
