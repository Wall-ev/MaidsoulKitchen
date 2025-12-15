package com.github.wallev.maidsoulkitchen.modclazzchecker.manager.taskinfo.condition.composite;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.taskinfo.condition.LoadCondition;

import java.util.List;

/**
 * 与组合条件：所有子条件都必须满足才能加载
 */
public class AndCompositeCondition extends LoadCondition.CompositeCondition {
    /**
     * 与组合条件的构造函数
     * @param conditions 子条件列表
     */
    public AndCompositeCondition(List<LoadCondition<?>> conditions) {
        super(conditions);
    }

    /**
     * 创建一个与组合条件，所有子条件都必须满足才能加载
     * @param conditions 子条件列表
     */
    public AndCompositeCondition(LoadCondition<?>... conditions) {
        this(List.of(conditions));
    }

    @Override
    public boolean canLoad() {
        return condition().stream().allMatch(LoadCondition::canLoad);
    }
}
