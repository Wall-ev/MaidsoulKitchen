package com.github.wallev.maidsoulkitchen.compat.msm.tea_aroma;

import cn.foggyhillside.tea_aroma.TeaAroma;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.ModLang;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.MsmLangUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;

public class Lang {

    @ModLang(Mods.TA)
    public static final MsmLangUtil.LangProvider LANG_PROVIDER = () -> {
        return TeaAroma.TEA_AROMA_TAB.get().getDisplayName();
    };

}
