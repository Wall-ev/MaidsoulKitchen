package com.github.wallev.maidsoulkitchen.task.cook.common.cbaccessor;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMskMixinInterface;

public interface ICbeAccessor extends IMskMixinInterface {

    /**
     * 烹饪的厨具内部是否可以烹饪（仅仅是与配方原料相关的判断，不包括外部条件，比如农夫乐事的需要Heated状态）
     * @return 是否可以烹饪
     */
    boolean tlmk$innerCanCook();

}
