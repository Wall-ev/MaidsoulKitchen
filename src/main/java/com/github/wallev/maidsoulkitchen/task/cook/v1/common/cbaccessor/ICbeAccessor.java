package com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

public interface ICbeAccessor {

    /**
     * 烹饪的厨具内部是否可以烹饪（仅仅是与配方原料相关的判断，不包括外部条件，比如农夫乐事的需要Heated状态）
     * @return 是否可以烹饪
     */
    boolean innerCanCook$tlma();

}
