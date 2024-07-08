package com.github.catbert.tlma.task;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.api.ILittleMaidTask;
import com.github.catbert.tlma.api.task.IDrinkTask;
import com.github.catbert.tlma.foundation.utility.Mods;
import com.github.catbert.tlma.task.ai.brain.MaidDrinkOwnerTask;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidFeedOwnerTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskFeedOwner;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import dev.ghen.thirst.api.ThirstHelper;
import dev.ghen.thirst.content.purity.WaterPurity;
import dev.ghen.thirst.foundation.common.capability.IThirst;
import dev.ghen.thirst.foundation.common.capability.ModCapabilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@LittleMaidExtension
public class TaskFeedAndDrinkOwner extends TaskFeedOwner implements ILittleMaidTask, IDrinkTask {

    public static final ResourceLocation UID = new ResourceLocation(TLMAddon.MOD_ID, "feedanddrink");

    @Override
    public boolean canLoaded() {
        return Mods.TWT.isLoaded;
    }

    @Override
    public ItemStack getIcon() {
        return PotionUtils.setPotion(Items.GLASS_BOTTLE.getDefaultInstance(), Potions.EMPTY);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
//        return Lists.newArrayList(Pair.of(5, new MaidFeedOwnerTask(this, 2, 0.6f)), Pair.of(5, new MaidDrinkOwnerTask(this, 2, 0.6f)));
        return Lists.newArrayList(Pair.of(5, new MaidDrinkOwnerTask(this, 2, 0.6f)));
    }

    /**
     * 对应的物品是否可以作为饮品
     *
     * @param stack 对应的物品
     * @param owner 喂食的对象
     * @return 是否可以作为饮品
     */
    @Override
    public boolean isDrink(ItemStack stack, Player owner) {
        return ThirstHelper.isDrink(stack);
    }

    /**
     * 获取对应食物的优先级
     *
     * @param stack 传入的物品
     * @param owner 喂食的对象
     * @return 对应食物的优先级
     */
    @Override
    public IDrinkTask.Priority getDrinkPriority(ItemStack stack, Player owner) {

        if (ThirstHelper.itemRestoresThirst(stack) && ThirstHelper.isDrink(stack)) {
            IThirst iThirst = owner.getCapability(ModCapabilities.PLAYER_THIRST).orElse(null);
            if (iThirst != null){
                int heal = ThirstHelper.getThirst(stack);

                int hunger = 20 - iThirst.getThirst();
                if (heal == hunger) {
                    return IDrinkTask.Priority.HIGH;
                } else if (heal > hunger) {
                    return IDrinkTask.Priority.LOWEST;
                } else {
                    return IDrinkTask.Priority.LOW;
                }
            }
        }

        return IDrinkTask.Priority.NONE;
    }

    /**
     * 具体喂食的执行逻辑
     *
     * @param stack 喂食的物品
     * @param owner 喂食的对象
     * @return 喂食后的物品
     */
    @Override
    public ItemStack drink(ItemStack stack, Player owner) {
        if (stack.getUseAnimation() == UseAnim.DRINK) {
            owner.level().playSound(null, owner, stack.getDrinkingSound(), SoundSource.NEUTRAL,
                    0.5f, owner.level().getRandom().nextFloat() * 0.1f + 0.9f);
        }

        AtomicBoolean canDrink = new AtomicBoolean(false);
        owner.getCapability(ModCapabilities.PLAYER_THIRST).ifPresent((cap) -> {
            if (WaterPurity.givePurityEffects(owner, stack)) {
                cap.drink(owner, ThirstHelper.getThirst(stack), ThirstHelper.getQuenched(stack));
                canDrink.set(true);
            }
        });

        if (canDrink.get()) {
            return stack.getItem().finishUsingItem(stack, owner.level(), owner);
        }
        return stack;
    }
}
