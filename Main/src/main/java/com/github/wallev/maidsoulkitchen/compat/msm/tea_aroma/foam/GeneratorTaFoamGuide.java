package com.github.wallev.maidsoulkitchen.compat.msm.tea_aroma.foam;

import cn.foggyhillside.tea_aroma.recipe.FoamRecipe;
import cn.foggyhillside.tea_aroma.registry.ModItems;
import cn.foggyhillside.tea_aroma.registry.ModRecipeTypes;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.tea.ITeaGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.EnchantCommonUseAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.custom.SneakCommonUseAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.util.FailCraftGuideStepData;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.compat.msm.tea_aroma.util.TeaBrewingFoamHelper;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOption;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOptionSet;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;

import java.util.List;

import static com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.TargetUtil.makeTargetVirtualNoSide;

@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_TA_FOAM)
public class GeneratorTaFoamGuide implements ITeaGuideGenerator<FoamRecipe> {
//public class GeneratorTaFoamGuide implements ITeaGuideGenerator<FoamRecipe, SimpleContainer> {

    @Override
    public void generateSteps(BlockPos pos, Level level, FoamRecipe recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        ItemStack cup = realItems.remove(0);
        ItemStack liquid = realItems.remove(realItems.size() - 1);

        // 放茶杯
        craftGuide.addStep(new CraftGuideStepData(makeTargetVirtualNoSide(pos), List.of(cup), List.of(),
//                CommonUseAction.TYPE
                SneakCommonUseAction.TYPE));

        // 放茶叶
        CraftGuideOperator2.forEachSingleItem(realItems, itemStack -> {
            craftGuide.addItemUse(pos.above(), itemStack);
        });

        // 放水
        ItemStack leftFluidTeaBase = leftFluidTeaBase(liquid.copyWithCount(1));
//        craftGuide.addItemUse(pos.above(), liquid, leftFluidTeaBase);

        CraftGuideStepData data = new CraftGuideStepData(makeTargetVirtualNoSide(pos.above()), List.of(), List.of(liquid), EnchantCommonUseAction.TYPE, ActionOptionSet.with(ActionOption.OPTIONAL, true));
        CompoundTag compoundTag = FailCraftGuideStepData.toCompoundTag(data);

        CraftGuideStepData resultStepData = new CraftGuideStepData(makeTargetVirtualNoSide(pos.above()), List.of(liquid), List.of(leftFluidTeaBase), EnchantCommonUseAction.TYPE, compoundTag);
        craftGuide.addStep(resultStepData);


        // 右键拾取成品
        craftGuide.addEmptyUse(pos.above(), outputs);
//        craftGuide.addStep(new CraftGuideStepData(
//                TargetUtil.makeTargetVirtualNoSide(pos.above()),
//                List.of(),
//                outputs,
//                EnchantCommonUseAction.TYPE
//        ));
    }

    @Override
    public ItemStack leftFluidTeaBase(ItemStack itemStack) {
        return TeaBrewingFoamHelper.leftTeaFluidBase(itemStack);
    }

    @Override
    public List<Ingredient> getCups(FoamRecipe recipe) {
        return List.of(Ingredient.of(recipe.getTea()));
    }

    @Override
    public List<Ingredient> getTeaLeaves(FoamRecipe recipe) {
        return List.of();
    }

    @Override
    public List<Ingredient> getFluidTeaBase(FoamRecipe recipe) {
        return List.of(TeaBrewingFoamHelper.milkLiquidIngredient());
    }

    @Override
    public List<ItemStack> getTea(FoamRecipe recipe, RegistryAccess registryAccess) {
        return List.of(recipe.getResultItem(registryAccess));
    }

    @Override
    @NotNull
    public ResourceLocation getType() {
        assert ModRecipeTypes.FOAM_RECIPE.getId() != null;
        return VResourceLocation.createTypeMod(ModRecipeTypes.FOAM_RECIPE.getId());
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) simpleContainer(allInputs);
    }

    @Override
    public String getRecipeTranslateKeyFromJei() {


//        Minecraft mc = Minecraft.getInstance();
//        PackRepository resourcePackRepository = mc.getResourcePackRepository();
//        List<PackResources> list = resourcePackRepository.openAllSelected();
//        CompletableFuture<Unit> RESOURCE_RELOAD_INITIAL_TASK =  CompletableFuture.completedFuture(Unit.INSTANCE);
//        ReloadInstance reloadinstance = new ReloadableResourceManager(PackType.CLIENT_RESOURCES)
//                .createReload(Util.backgroundExecutor(), mc, RESOURCE_RELOAD_INITIAL_TASK, list);
//
////        ReloadInstance reloadinstance = this.resourceManager.createReload(Util.backgroundExecutor(), this, RESOURCE_RELOAD_INITIAL_TASK, list);


        return ITeaGuideGenerator.super.getRecipeTranslateKeyFromJei();
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ModItems.CUP.get().asItem();
    }

    @Override
    public RecipeType<FoamRecipe> getRecipeType() {
        return FoamRecipe.Type.INSTANCE;
    }
}
