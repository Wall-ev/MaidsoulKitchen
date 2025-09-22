package com.github.wallev.maidsoulkitchen.compat.msm.bakeries.dough_craft_table;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.cutter.ICutterGuideGenerator;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.renyigesai.bakeries.init.BakeriesBlocks;
import com.renyigesai.bakeries.recipe.DoughCraftingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_BAKERIES_DOUGH)
public class GeneratorDoughGuide implements ICutterGuideGenerator<DoughCraftingRecipe> {
    @Override
    public ResourceLocation cutterRecipeLoc() {
        return DoughCraftingRecipe.Serializer.ID;
    }

    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return true;
    }

    @Override
    public Item getBlockItemForTranslate() {
        return BakeriesBlocks.DOUGH_CRAFTING_TABLE.get().asItem();
    }

    @Override
    public RecipeType<DoughCraftingRecipe> getRecipeType() {
        return DoughCraftingRecipe.Type.INSTANCE;
    }

    @Override
    public @NotNull ResourceLocation getType() {
        return VResourceLocation.createTypeMod(Mods.BAKERIES, DoughCraftingRecipe.Type.ID);
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) recipeWrapperContainer(allInputs);
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return level.getBlockState(pos).is(BakeriesBlocks.DOUGH_CRAFTING_TABLE.get());
    }
}
