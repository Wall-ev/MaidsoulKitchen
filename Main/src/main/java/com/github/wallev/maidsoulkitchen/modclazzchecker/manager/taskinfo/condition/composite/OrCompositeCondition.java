package com.github.wallev.maidsoulkitchen.modclazzchecker.manager.taskinfo.condition.composite;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.taskinfo.condition.LoadCondition;

import java.util.List;

/**
 * 或组合条件：任一子条件满足即可加载
 */
public class OrCompositeCondition extends LoadCondition.CompositeCondition {
    /**
     * 或组合条件的构造函数
     * @param conditions 子条件列表
     */
    public OrCompositeCondition(List<LoadCondition<?>> conditions) {
        super(conditions);
    }

    public OrCompositeCondition(LoadCondition<?>... conditions) {
        super(List.of(conditions));
    }

    @Override
    public boolean canLoad() {
        return condition().stream().anyMatch(LoadCondition::canLoad);
    }
}
