package com.github.wallev.maidsoulkitchen.modclazzchecker.manager.taskinfo.condition;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.taskinfo.TaskInfo0;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.taskinfo.condition.composite.AndCompositeCondition;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.taskinfo.condition.composite.OrCompositeCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * 条件接口：所有可参与组合的条件单元都需实现此接口
 */
public interface LoadCondition<C> {
    /**
     * 判断当前条件是否满足加载要求
     * @return 满足返回true，否则false
     */
    boolean canLoad();

    /**
     * 获取当前条件的具体配置信息
     * @return 条件配置对象
     */
    C condition();

    /**
     * 创建一个基于Mods的加载条件
     * @param mods 目标Mod
     * @return ModsCondition实例
     */
    static ModsCondition of(Mods mods) {
        return new ModsCondition(mods, false);
    }

    static ModsCondition of(Mods mods, boolean modMark) {
        return new ModsCondition(mods, modMark);
    }

    /**
     * 创建一个基于TaskInfo0的加载条件
     * @param taskInfo 目标任务信息
     * @return TaskInfoCondition实例
     */
    static TaskInfoCondition of(TaskInfo0 taskInfo) {
        return new TaskInfoCondition(taskInfo);
    }

    /**
     * 创建一个与组合条件，所有子条件都必须满足才能加载
     * @param mods 目标Mod列表
     * @return AndCompositeCondition实例
     */
    static AndCompositeCondition and(Mods... mods) {
        AndCompositeCondition andCompositeCondition = new AndCompositeCondition();
        for (Mods mod : mods) {
            andCompositeCondition.addCondition(new ModsCondition(mod, false));
        }
        return andCompositeCondition;
    }

    /**
     * 创建一个或组合条件，当任意一个子条件满足时，整体条件即满足
     * @param mods 目标Mod列表
     * @return OrCompositeCondition实例
     */
    static OrCompositeCondition or(Mods... mods) {
        OrCompositeCondition orCompositeCondition = new OrCompositeCondition();
        for (Mods mod : mods) {
            orCompositeCondition.addCondition(new ModsCondition(mod, false));
        }
        return orCompositeCondition;
    }

    /**
     * 创建一个或组合条件，当任意一个子条件满足时，整体条件即满足
     * @param conditions 子条件列表
     * @return OrCompositeCondition实例
     */
    static OrCompositeCondition or(LoadCondition<?>... conditions) {
        return new OrCompositeCondition(conditions);
    }

    static AndCompositeCondition and(LoadCondition<?>... conditions) {
        return new AndCompositeCondition(conditions);
    }


     /**
     * 组合条件接口：用于表示多个条件的组合关系
     */
    abstract class CompositeCondition implements LoadCondition<List<LoadCondition<?>>> {
        private final List<LoadCondition<?>> conditions = new ArrayList<>();

         /**
         * 组合条件的构造函数
         * @param conditions 子条件列表
         */
        public CompositeCondition(List<LoadCondition<?>> conditions) {
            this.conditions.addAll(conditions);
        }

        public void addCondition(LoadCondition<?> condition) {
            conditions.add(condition);
        }

        @Override
        public List<LoadCondition<?>> condition() {
            return conditions;
        }
    }
}
