package com.github.wallev.maidsoulkitchen.compat.msm.bakeries.fermentation_tank;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.ICookingGuideGenerator;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.renyigesai.bakeries.init.BakeriesBlocks;
import com.renyigesai.bakeries.init.BakeriesItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_BAKERIES_FERMENTATION_TANK)
public class GeneratorBakeriesFermentationTankGuide implements ICookingGuideGenerator<Void> {

    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return true;
    }

    @Override
    public @NotNull ResourceLocation getType() {
        return VResourceLocation.createTypeMod(Mods.BAKERIES, "fermentation_tank_cheese");
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return level.getBlockState(pos).is(BakeriesBlocks.FERMENTATION_TANK.get());
    }

    @Override
    public void consumeRecipes(RecipeManager manager, Consumer<Void> recipeConsumer) {
        recipeConsumer.accept(null);
    }

    @Override
    public ResourceLocation getRecipeId(Void recipe) {
        return VResourceLocation.create(Mods.BAKERIES.getModId(), "fermentation_tank_cheese");
    }

    @Override
    public List<Ingredient> getInputs(Void recipe) {
        return toIngredients(Items.MILK_BUCKET.getDefaultInstance(), BakeriesItems.SALT.get().getDefaultInstance());
    }

    @Override
    public List<ItemStack> getOutputs(Void recipe, RegistryAccess registryAccess) {
        return List.of(new ItemStack(BakeriesItems.CHEESE_CUBE.get(), 4));
    }

    @Override
    public int getRecipeTime(Void recipe) {
        return 38000;
    }

    @Override
    public Item getBlockItemForTranslate() {
        return BakeriesBlocks.FERMENTATION_TANK.get().asItem();
    }
}
