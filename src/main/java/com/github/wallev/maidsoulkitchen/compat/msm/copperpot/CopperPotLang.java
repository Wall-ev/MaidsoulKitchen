package com.github.wallev.maidsoulkitchen.compat.msm.copperpot;

import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.ModLang;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.MsmLangUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;

public class CopperPotLang {

    @ModLang(
            value = Mods.COPPER_POT,
            en_us = "Copper Pot",
            zh_cn = "Copper Pot",
            custom = true
    )
    public static final MsmLangUtil.LangProvider LANG_PROVIDER = () -> null;

}
