package com.github.catbert.tlma.api;

import com.github.catbert.tlma.entity.passive.CookTaskData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.FakePlayer;

import java.lang.ref.WeakReference;
import java.util.List;

 public interface IAddonMaid {

    WeakReference<FakePlayer> getFakePlayer$tlma();

    void initFakePlayer$tlma();

    CompoundTag getAddonMaidData$tlma();

    void setAddonMaidData$tlma(CompoundTag nbt);

    Integer getStartYOffset$tlma();

    void setStartYOffset$tlma(int offset);

    boolean openMaidGuiFromSideTab(Player player, int tabIndex, boolean taskListOpen, int taskPage);

    MenuProvider getGuiProviderFromSideTab(int tabIndex, boolean taskListOpen, int taskPage);

    void addOrRemoveRecipe(String recipeId);

    CompoundTag getCookTaskTag();

    ListTag getRecipeListTag();

    CookTaskData getCookTaskData1();

    void addOrRemoveRecipe1(String recipeId);

    boolean containsRecipe1(String recipeId);

    void toggleTaskRuleMode1();

    void addOrRemoveRecipe2(String recipeId);

    boolean containsRecipe2(String recipeId);

    CompoundTag getCookInfo();

    void setCookInfo(CompoundTag cookInfo);

    void putCookTaskInfo(ResourceLocation taskRec, CookTaskData.Mode mode, List<ResourceLocation> recs);

    String getCookTaskMode(ResourceLocation taskRec);

    void setCookTaskMode(String taskRec, String mode);

    void setTaskRule(String taskUid, CookTaskData.TaskRule taskRule);
}
