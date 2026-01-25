//package com.github.wallev.maidsoulkitchen.task.farm.advancefarm;
//
//import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
//import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
//import com.github.wallev.maidsoulkitchen.api.task.IMaidsoulKitchenTask;
//import com.github.wallev.maidsoulkitchen.task.farm.advancefarm.ai.CropResult;
//import com.github.wallev.maidsoulkitchen.task.farm.advancefarm.ai.MaidAdvancedFarmMoveTask;
//import com.github.wallev.maidsoulkitchen.task.farm.advancefarm.ai.MaidAdvancedFarmPlantTask;
//import com.github.wallev.maidsoulkitchen.task.farm.advancefarm.handler.IHarvestCrop;
//import com.github.wallev.maidsoulkitchen.vhelper.server.ai.VBehaviorControl;
//import com.google.common.collect.Lists;
//import com.mojang.datafixers.util.Pair;
//import net.minecraft.core.BlockPos;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.sounds.SoundEvent;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.block.state.BlockState;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.List;
//
//public class TaskAdvanceFarm implements IMaidsoulKitchenTask {
//    public static final ResourceLocation UID = new ResourceLocation("maidsoulkitchen", "advance_normal_farm");
//
//    public TaskAdvanceFarm() {
//        IHarvestCrop.Handler.init();
//    }
//
//    public boolean isSeed(ItemStack stack) {
//        return false;
//    }
//
//    public boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, CropResult cropResult, MaidPathFindingBFS pathFinding) {
//        for (IHarvestCrop rule : IHarvestCrop.Handler.getRules(cropState.getBlock())) {
//            if (rule.checkAndGetPos(maid, cropPos, cropState, cropResult, pathFinding)) {
//                cropResult.setUid(rule.getUid());
//                cropResult.setCloseEnoughDist(rule.getCloseEnoughDist());
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed, CropResult cropResult) {
//        return false;
//    }
//
//    public ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed, CropResult cropResult) {
//        return ItemStack.EMPTY;
//    }
//
//    @Override
//    public ResourceLocation getUid() {
//        return UID;
//    }
//
//    @Override
//    public ItemStack getIcon() {
//        return Items.DIAMOND_HOE.getDefaultInstance();
//    }
//
//    @Override
//    public @Nullable SoundEvent getAmbientSound(EntityMaid maid) {
//        return null;
//    }
//
//    @Override
//    public List<Pair<Integer, VBehaviorControl>> vCreateBrainTasks(EntityMaid maid) {
//        MaidAdvancedFarmMoveTask maidFarmMoveTask = new MaidAdvancedFarmMoveTask(this, 0.6f);
//        MaidAdvancedFarmPlantTask maidFarmPlantTask = new MaidAdvancedFarmPlantTask(this);
//        return Lists.newArrayList(Pair.of(5, maidFarmMoveTask), Pair.of(6, maidFarmPlantTask));
//    }
//}
