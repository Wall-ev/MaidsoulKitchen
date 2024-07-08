package com.github.catbert.tlma.task.ai.brain;

import com.github.catbert.tlma.api.task.IDrinkTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidCheckRateTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import dev.ghen.thirst.foundation.common.capability.IThirst;
import dev.ghen.thirst.foundation.common.capability.ModCapabilities;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class MaidDrinkOwnerTask extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 20;
    private final IDrinkTask task;
    private final float walkSpeed;
    private final int closeEnoughDist;

    public MaidDrinkOwnerTask(IDrinkTask task, int closeEnoughDist, float walkSpeed) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
        this.task = task;
        this.walkSpeed = walkSpeed;
        this.closeEnoughDist = closeEnoughDist;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }
    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid maid) {
        if (super.checkExtraStartConditions(worldIn, maid)) {
            LivingEntity owner = maid.getOwner();
            if (owner instanceof Player && owner.isAlive() && maid.isWithinRestriction(owner.blockPosition())) {
                if (owner.closerThan(maid, closeEnoughDist)) {
                    return true;
                }
                BehaviorUtils.setWalkAndLookTargetMemories(maid, owner, walkSpeed, 1);
            }
            return false;
        }
        return false;
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        LivingEntity owner = maid.getOwner();
        if (owner instanceof Player && owner.isAlive()) {
            Player player = (Player) owner;

            IThirst iThirst = owner.getCapability(ModCapabilities.PLAYER_THIRST).orElse(null);
            if (iThirst == null) {
                return;
            }
//            iThirst.getExhaustion() /
//            boolean dying = player.getHealth() / player.getMaxHealth() < 0.5f;
            boolean dying = iThirst.getThirst() < 20;

            IntList lowestFoods = new IntArrayList();
            IntList lowFoods = new IntArrayList();
            IntList highFoods = new IntArrayList();

            CombinedInvWrapper inv = maid.getAvailableInv(true);
            for (int i = 0; i < inv.getSlots(); ++i) {
                ItemStack stack = inv.getStackInSlot(i);
                if (task.isDrink(stack, player)) {
                    IDrinkTask.Priority priority = task.getDrinkPriority(stack, player);
                    if (priority == IDrinkTask.Priority.HIGH) {
                        highFoods.add(i);
                        break;
                    }
                    if (priority == IDrinkTask.Priority.LOW) {
                        lowFoods.add(i);
                        break;
                    }
                    if (dying && priority == IDrinkTask.Priority.LOWEST) {
                        lowestFoods.add(i);
                        break;
                    }
                }
            }

            if (highFoods.isEmpty() && lowFoods.isEmpty() && lowestFoods.isEmpty()) {
                return;
            }

            IntList map = !highFoods.isEmpty() ? highFoods : !lowFoods.isEmpty() ? lowFoods : lowestFoods;
            map.stream().skip(maid.getRandom().nextInt(map.size())).findFirst().ifPresent(slot -> {
                inv.setStackInSlot(slot, task.drink(inv.getStackInSlot(slot), player));
                maid.swing(InteractionHand.MAIN_HAND);
                this.setNextCheckTickCount(5);
            });
        }
    }
}
