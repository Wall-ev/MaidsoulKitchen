package com.github.wallev.maidsoulkitchen.task.cook.common.inv;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec.MKRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.*;

import static com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask.getSearchPos;

public abstract class MaidConditionRecipesManager2<R extends Recipe<? extends Container>, C> extends MaidRecipesManager2<R> {
    protected final int extraTryTimeMax = 20;
    protected Map<C, LinkedList<Integer>> tempIngredients = new HashMap<>();
    // 额外尝试标志位，因为有些烹饪条件是实时变化的。
    protected int extraTryTime = 0;
    protected Set<C> conditions;
    public MaidConditionRecipesManager2(RecSerializerManager<R> recSerializerManager, EntityMaid maid, ICookTask<?, R> task, CookBeBase<?> cookBeBase) {
        super(recSerializerManager, maid, task, cookBeBase);
    }

    @Override
    protected void createIngres() {
        this.clear();
        conditions = collectValidConditions();
        super.createIngres();
    }

    @Override
    public void clear() {
        tempIngredients.clear();
        maidRecs.clear();
        conditions = null;
    }

    protected Set<C> collectValidConditions() {
        return this.collectConditions(maid.level, maid, cookBeBase);
    }

    @Override
    protected void recAdd(MKRecipe<R> r, IndexRange indexRange) {
        C c = this.getRecipeCondition(r.rec());
        LinkedList<Integer> list = tempIngredients.computeIfAbsent(c, (k) -> new LinkedList<>());

        for (int i = indexRange.start(); i < indexRange.end(); i++) {
            list.add(i);
        }
    }

    @Override
    public boolean hasMaidRecs(CookBeBase<?> cookBeBase) {
        C beCondition = getBeCondition(cookBeBase);

        for (Map.Entry<C, LinkedList<Integer>> entry : this.tempIngredients.entrySet()) {
            if (isValid(beCondition, entry.getKey())) {
                LinkedList<Integer> value = entry.getValue();
                return !value.isEmpty();
            }
        }

        if (extraTryTime++ > extraTryTimeMax) {
            extraTryTime = 0;
            this.tempIngredients.clear();
            this.maidRecs.clear();
        }
        return false;
    }

    @Override
    public MaidRec pollMaidRec(CookBeBase<?> cookBeBase) {
        C beCondition = getBeCondition(cookBeBase);

        for (Map.Entry<C, LinkedList<Integer>> entry : this.tempIngredients.entrySet()) {
            C key = entry.getKey();
            if (isValid(beCondition, key)) {
                LinkedList<Integer> value = entry.getValue();
                Integer poll = value.poll();
                if (value.isEmpty()) {
                    this.tempIngredients.remove(key);
                }
                assert poll != null;
                return maidRecs.get(poll);
            }
        }

        return MaidRec.EMPTY;
    }

    @Override
    protected boolean recIsValid(MKRecipe<R> r) {
        return conditions.contains(this.getRecipeCondition(r.rec()));
    }

    protected abstract C getRecipeCondition(R r);

    protected abstract C getBeCondition(CookBeBase<?> cookBeBase);

    protected abstract boolean isValid(C beCondition, C rCondition);

    protected final Set<C> collectConditions(Level worldIn, EntityMaid maid, CookBeBase<?> cookBeBase) {
        Set<C> conditions = new HashSet<>();

        BlockPos centrePos = getSearchPos(maid);
        int searchRange = (int) maid.getRestrictRadius();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int y = 0; y <= ICookTask.VERTICAL_SEARCH_RANGE; y = y > 0 ? -y : 1 - y) {
            for (int i = 0; i < searchRange; ++i) {
                for (int x = 0; x <= i; x = x > 0 ? -x : 1 - x) {
                    for (int z = x < i && x > -i ? i : 0; z <= i; z = z > 0 ? -z : 1 - z) {
                        mutableBlockPos.setWithOffset(centrePos, x, y + 1, z);
                        if (maid.isWithinRestriction(mutableBlockPos) && this.shouldMoveTo(worldIn, maid, mutableBlockPos, cookBeBase)) {
                            conditions.add(this.getBeCondition(cookBeBase));
                        }
                    }
                }
            }
        }
        cookBeBase.clear();

        return conditions;
    }

    protected boolean shouldMoveTo(Level worldIn, EntityMaid maid, BlockPos blockPos, CookBeBase<?> cookBeBase) {
        BlockEntity blockEntity = worldIn.getBlockEntity(blockPos);
        if (blockEntity == null) {
            return false;
        }
        if (cookBeBase.isCookBe(blockEntity)) {
            cookBeBase.setBlockEntity(blockEntity);
            return true;
        }
        return false;
    }

    public static class IndexRange {
        private int start;
        private int end;

        public IndexRange() {
        }

        public void set(int start, int size) {
            this.start = start;
            this.end = start + size;
        }

        public int start() {
            return start;
        }

        public int end() {
            return end;
        }

        public void reset() {
            this.start = 0;
            this.end = 0;
        }
    }
}
