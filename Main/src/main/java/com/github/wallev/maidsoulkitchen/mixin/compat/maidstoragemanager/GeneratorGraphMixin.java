package com.github.wallev.maidsoulkitchen.mixin.compat.maidstoragemanager;

import com.github.wallev.maidsoulkitchen.compat.msm.common.util.DynamicAddRecipeHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideData;
import studio.fantasyit.maid_storage_manager.craft.generator.algo.GeneratorGraph;
import studio.fantasyit.maid_storage_manager.craft.generator.algo.node.ItemNode;
import studio.fantasyit.maid_storage_manager.craft.generator.algo.node.Node;

import java.util.List;
import java.util.Queue;
import java.util.function.Function;

@Mixin(value = GeneratorGraph.class, remap = false)
public abstract class GeneratorGraphMixin {

    @Shadow
    Queue<Node> reversedQueue;

    @Shadow
    public abstract void addRecipe(ResourceLocation id, List<Ingredient> ingredients, List<Integer> ingredientCounts, ItemStack output, Function<List<ItemStack>, @Nullable CraftGuideData> craftGuideSupplier);

    @Shadow
    protected abstract int _addRecipe(ResourceLocation id, List<Ingredient> ingredients, List<Integer> ingredientCounts, List<ItemStack> output, Function<List<ItemStack>, @Nullable CraftGuideData> craftGuideSupplier, ResourceLocation type, boolean isOneTime);

    @Shadow
    protected ResourceLocation currentType;

    @Shadow
    protected boolean oneTime;

    @Shadow
    public int processedSteps;

    @Inject(method = "processReversed",
            at = @At(value = "INVOKE", target = "Ljava/util/Queue;poll()Ljava/lang/Object;"))
    private void processReversed(CallbackInfoReturnable<Boolean> cir) {
        Node node = reversedQueue.peek();
        if (node instanceof ItemNode itemNode && itemNode.edgesRev.isEmpty()) {
            ItemStack itemStack = itemNode.itemStack;
            DynamicAddRecipeHelper.DynamicRecipeData dynamicRecipeData = DynamicAddRecipeHelper.create(itemStack);
            if (dynamicRecipeData == null) return;

            int processedStep = _addRecipe(
                    dynamicRecipeData.id(),
                    dynamicRecipeData.ingredients(),
                    dynamicRecipeData.ingredientCounts(),
                    dynamicRecipeData.output(),
                    dynamicRecipeData.craftGuideSupplier(),
                    currentType,
                    oneTime
            );
            processedSteps-=processedStep;
        }
    }

}
