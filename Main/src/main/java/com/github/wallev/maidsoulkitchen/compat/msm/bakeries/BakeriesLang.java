package com.github.wallev.maidsoulkitchen.compat.msm.bakeries;

import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.ModLang;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.MsmLangUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.renyigesai.bakeries.init.BakeriesGroup;

public class BakeriesLang {

    @ModLang(Mods.BAKERIES)
    public static final MsmLangUtil.LangProvider LANG_PROVIDER = () -> {
        return BakeriesGroup.BAKERY_TAB.get().getDisplayName();
    };

}
