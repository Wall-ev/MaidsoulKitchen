package com.github.wallev.maidsoulkitchen.datagen.lang;

import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.MsmLangUtil;
import com.github.wallev.maidsoulkitchen.init.ModItems;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ModLanguageProvider extends LanguageProvider {
    public enum Local {
        EN_US,
        ZH_CN
    }

    public record LangVal(Local local, String val) {}

    private final Map<String, Pair<LangVal, LangVal>> map = new HashMap<>();
    private final Local currentLocal;

    public ModLanguageProvider(PackOutput output, String modid, Local locale) {
        super(output, modid, locale.name().toLowerCase(Locale.ENGLISH));
        this.currentLocal = locale;
    }

    @Override
    protected void addTranslations() {
        this.addLocalLang();
        this.addModCompatLang();
    }

    protected void addModCompatLang() {
        try {
            MsmLangUtil.autonGenModLang(this, currentLocal.name().toLowerCase(Locale.ENGLISH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void addLocalLang() {
        initLang();
        // 根据当前语言环境添加对应的翻译
        for (Map.Entry<String, Pair<LangVal, LangVal>> entry : map.entrySet()) {
            String key = entry.getKey();
            Pair<LangVal, LangVal> pair = entry.getValue();

            String value = switch (currentLocal) {
                case ZH_CN -> pair.getSecond().val();
                default -> pair.getFirst().val();
            };

            add(key, value);
        }
    }

    protected void initLang() {
        addItemLang();
        addTooltipsLang();
        addEffectLang();
        addChatBubbleLang();
        addConfigLang();
        addTaskLang();
        addOverlayLang();
        addTopLang();
        addMessageLang();
        addRuleLang();
        addGuiLang();
        addPatchouliLang();
    }

    protected void addItemLang() {
//        addItemLang(MkItems.OLD_MAID_BACKPACK_BIG.get(), "Old Maid Backpack", "旧版的大背包");
        addItemLang(ModItems.BURN_PROTECT_BAUBLE.get(), "§6Burn Protect Bauble", "§6燃着保护饰品");
        addItemLang(ModItems.CULINARY_HUB.get(), "Culinary hub", "烹饪中枢");
    }

    protected void addTooltipsLang() {
        addLang("tooltips.maidsoulkitchen.culinary_hub.desc.usage", "[Usage]", "[使用方法]");
        addLang("tooltips.maidsoulkitchen.culinary_hub.desc.usage.0", "Press and hold [SHIFT] to view details", "按住[SHIFT]查看详情");
        addLang("tooltips.maidsoulkitchen.culinary_hub.desc.usage.1", "- Right-click to open the Gui and select the mode of binding", "- 右键打开Gui选择绑定的模式");
        addLang("tooltips.maidsoulkitchen.culinary_hub.desc.usage.2", "- Shift-right-click the box to bind the box, and Shift again-right-click the box to unbind", "- Shift右击箱子绑定箱子，再次Shift右击箱子取消绑定");
        addLang("tooltips.maidsoulkitchen.culinary_hub.desc.usage.3", "- Place it in the fifth slot of the first column of the maid's backpack to take effect", "- 放在女仆背包第一栏第五个格子生效");
        addLang("tooltips.maidsoulkitchen.culinary_hub.desc.function", "[Function]", "[功能]");
        addLang("tooltips.maidsoulkitchen.culinary_hub.desc.function.1", "Allows maids to interact directly with other chests while cooking", "允许女仆在烹饪时直接与其他箱子互动");
        addLang("tooltips.maidsoulkitchen.culinary_hub.desc.function.2", "There are three types of chests that can be bound, with a maximum of 3 of each", "可绑定三种类型的箱子，每种最多可绑定3个");
        addLang("tooltips.maidsoulkitchen.culinary_hub.desc.warn", "[Warning]", "[使用警告]");
        addLang("tooltips.maidsoulkitchen.culinary_hub.desc.warn.empty", "There are currently no chests bound to it, which will render the [Culinary Hub] useless", "当前还未绑定任何箱子，这将使[烹饪中枢]发挥不了作用");
        addLang("tooltips.maidsoulkitchen.culinary_hub.desc.warn.left", "There are currently %s chests left unbound, please continue to bind them, otherwise [Culinary Hub] will not be fully effective", "当前还剩%s类型的箱子未绑定，请继续绑定它们，否则[烹饪中枢]发挥不了全部作用");
        addLang("tooltips.maidsoulkitchen.burn_protect_bauble.desc.function", "[Function]", "[功能]");
        addLang("tooltips.maidsoulkitchen.burn_protect_bauble.desc.function.1", "Prevents the maid from burning when cooking", "防止女仆在烹饪时被烫伤");
        addLang("tooltips.maidsoulkitchen.amount.title", "Craft One Amount Ingredient:", "涉及到的原材料:");
    }

    protected void addEffectLang() {
        addLang("effect.maidsoulkitchen.burn_protect", "Burn Protect", "抗燃");
    }

    protected void addChatBubbleLang() {
        addLang("chat_bubble.maidsoulkitchen.inner.feed_animal.type.none_food.0", "No food for %s... Can't raise them!", "没有%s的食物了... 养不了它们了！");
        addLang("chat_bubble.maidsoulkitchen.inner.feed_animal.type.none_food.1", "%s, there's no food for you... I have to wrong you, go hungry for a while!", "%s,没有你们食物了... 只能委屈你们,先饿一段时间了！");
        addLang("chat_bubble.maidsoulkitchen.inner.feed_animal.type.none_food.2", "This %s is too picky... I won't raise them anymore, there's no food!", "这%s也太挑食了... 不养了不养了,都没有食物,养啥啊！");
        addLang("chat_bubble.maidsoulkitchen.inner.feed_animal.type.max_number.0", "There are too many %s... I won't feed them anymore!", "这%s太多了... 我不再喂养它们了！");
        addLang("chat_bubble.maidsoulkitchen.inner.feed_animal.type.max_number.1", "There are too many %s... Can't feed them anymore!", "这%s太多了吧... 不能再喂养它们了啦！");
        addLang("chat_bubble.maidsoulkitchen.inner.feed_animal.type.max_number.2", "This %s eats too much... I won't raise them anymore, they eat too much!", "这%s也太能吃了把... 不养了不养了,太能吃了！");
        addLang("chat_bubble.maidsoulkitchen.inner.feed_animal.feed_end", "Little %s, grow up quickly~", "小%s君，快快长大哦~");
        addLang("chat_bubble.maidsoulkitchen.inner.feed_animal.no_animals", "There seem to be no animals that can be raised~", "这似乎没有可以养殖的动物诶~");
        addLang("chat_bubble.maidsoulkitchen.cook.collect_ingredients", "Let me see what food can be made~", "让我来看看都可以制作那些食物~");
        addLang("chat_bubble.maidsoulkitchen.cook.no_ingredient_cook", "┭┮﹏┭┮ There's no raw material in the warehouse, can't cook!", "┭┮﹏┭┮仓库怎么什么原材料都没有啊，都不能炒菜了！");
        addLang("chat_bubble.maidsoulkitchen.cook.can_cook_these_food", "Wow, it can make so much food:", "哇，可以制作这么多食物诶: ");
        addLang("chat_bubble.maidsoulkitchen.cook.food_amount", "portion", "份");
        addLang("chat_bubble.maidsoulkitchen.cook.master", "Master", "主人");
        addLang("chat_bubble.maidsoulkitchen.cook.make_food.0", "It's time to fry %d portions of %s love bento for master %s~", "是时候给主人%s炒%d份%s爱心便当了~");
        addLang("chat_bubble.maidsoulkitchen.cook.make_food.1", "It's time to fry %d portions of %s love bento~", "是时候给炒%d份%s爱心便当了~");
        addLang("chat_bubble.maidsoulkitchen.cook.make_food.2", "It's time for %d portions of %s!", "是时候来%d份%s了！");
        addLang("chat_bubble.maidsoulkitchen.cook.make_food.3", "Is %s delicious? Let's have %d portions~", "%s好吃么，来%d份看看~");
        addLang("chat_bubble.maidsoulkitchen.cook.make_food.4", "%d portions of %s, let's start!", "%d份%s，启动！");
    }

    protected void addConfigLang() {
        addLang("config.maidsoulkitchen.title", "Touhou Little Maid Addon: Farm And Cook", "车万女仆拓展：农耕与烹饪");
        addLang("config.maidsoulkitchen.title.tip", "[Addon: Farm And Cook]", "[拓展：农耕与烹饪]");
        addLang("config.maidsoulkitchen.task", "Task", "任务");
        addLang("config.maidsoulkitchen.task.melon_stem_list", "Melon Stem List", "瓜苗配置");
        addLang("config.maidsoulkitchen.task.melon_stem_list.tooltip", "These entries configure the melon stem list\nrule: §eattached_melon_stem_block_id\nEg: §eminecraft:attached_pumpkin_stem §eminecraft:attached_pumpkin_stem ...", "这些条目配置了要采集的瓜的瓜苗\n规则: §e瓜苗的方块id\n例如: §eminecraft:attached_pumpkin_stem §eminecraft:attached_pumpkin_stem ...");
        addLang("config.maidsoulkitchen.task.melon_and_stem_list", "Melon And Stem List", "瓜类瓜苗配置");
        addLang("config.maidsoulkitchen.task.melon_and_stem_list.tooltip", "These entries configure the melon stem and melon_block list\nrule: §emelon_item_id(silk touch)§c,§eattached_melon_stem_block_id\nEg: §eminecraft:melon§c,§eminecraft:attached_melon_stem", "这些条目配置了要采集的瓜\n规则: §e瓜的物品id(精准采集)§c,§e瓜苗的方块id\n例如: §eminecraft:melon§c,§eminecraft:attached_melon_stem");
        addLang("config.maidsoulkitchen.task.cook_selected_recipes", "Cook Selected Recipe Numbs", "烹饪定向配方最大数量值");
        addLang("config.maidsoulkitchen.task.cook_selected_recipes.tooltip", "Cook Selected Recipe Numbs", "烹饪定向配方最大数量值");
        addLang("config.maidsoulkitchen.task.fruit_search_yoffset", "Fruit Task Search YOffset Numbs", "水果农场检索高度值");
        addLang("config.maidsoulkitchen.task.fruit_search_yoffset.tooltip", "Change the working mode to the retrieval start height value of the maid of the fruit task", "改变工作模式为水果的女仆的检索起始高度值");
        addLang("config.maidsoulkitchen.task.feed_animal_t", "Max Number of the same type Animal", "繁殖同种动物的最大数量");
        addLang("config.maidsoulkitchen.task.feed_animal_t.tooltip", "The max number of the same animal around when the maid breeds animals", "女仆处于繁殖动物模式时，周围最大可以繁殖的同种动物数量");
        addLang("config.maidsoulkitchen.render", "Render", "渲染");
        addLang("config.maidsoulkitchen.render.ld_banner_render", "let's do banner", "let's do旗帜");
        addLang("config.maidsoulkitchen.render.ld_banner_render.tooltip", "Maid can render let's do Banner.", "女仆会渲染Let's do系列的旗帜");
        addLang("config.maidsoulkitchen.register", "Register", "注册");
        addLang("config.maidsoulkitchen.register.restart_warn.tooltip", "§cWarning： This requires a reboot for this to take effect", "§c注意： 这需要重启才生效");
        addLang("config.maidsoulkitchen.register.berry_farm_task", "Berry Farm Task", "浆果农场任务");
        addLang("config.maidsoulkitchen.register.berry_farm_task.tooltip", "This can make the berry farm task enabled or not.", "这会启用或禁用浆果农场任务的注册");
        addLang("config.maidsoulkitchen.register.fruit_farm_task", "Fruit Farm Task", "水果农场任务");
        addLang("config.maidsoulkitchen.register.fruit_farm_task.tooltip", "This can make the fruit farm task enabled or not.", "这会启用或禁用水果农场任务的注册");
        addLang("config.maidsoulkitchen.register.feed_animal_t", "BreedT Task", "繁殖2任务");
        addLang("config.maidsoulkitchen.register.feed_animal_t.tooltip", "This can make the breed_t task enabled or not.", "这会启用或禁用繁殖2任务的注册");
        addLang("config.maidsoulkitchen.register.compat_melon_farm_task", "Compat Melon Farm Task", "兼容瓜类农场任务");
        addLang("config.maidsoulkitchen.register.compat_melon_farm_task.tooltip", "This can make the compat melon farm task enabled or not.", "这会启用或禁用兼容瓜类农场任务的注册");
        addLang("config.maidsoulkitchen.register.serene_seasons_farm_task", "SereneSeasons Farm Task", "四季农场任务");
        addLang("config.maidsoulkitchen.register.serene_seasons_farm_task.tooltip", "This can make the sereneseasons farm task enabled or not.", "这会启用或禁用四季农场任务的注册");
        addLang("config.maidsoulkitchen.register.eclipticseasons_farm", "Ecliptic Seasons Farm Task", "节气农场任务");
        addLang("config.maidsoulkitchen.register.eclipticseasons_farm.tooltip", "This can make the ecliptic seasons farm task enabled or not.", "这会启用或禁用节气农场任务的注册");
        addLang("config.maidsoulkitchen.register.feed_and_drink_task", "Feed And Drink Task", "喂食与饮水任务");
        addLang("config.maidsoulkitchen.register.feed_and_drink_task.tooltip", "This can make the feed and drink task enabled or not.", "这会启用或禁用喂食与饮水任务的注册");
        addLang("config.maidsoulkitchen.register.furnace_task", "Furnace Task", "熔炉任务");
        addLang("config.maidsoulkitchen.register.furnace_task.tooltip", "This can make the furnace task enabled or not.", "这会启用或禁用熔炉任务的注册");
        addLang("config.maidsoulkitchen.register.fd_cook_pot", "FD Cook Pot Task", "农夫乐事厨锅任务");
        addLang("config.maidsoulkitchen.register.fd_cook_pot.tooltip", "This can make the fd cook pot task enabled or not.", "这会启用或禁用农夫乐事厨锅任务的注册");
        addLang("config.maidsoulkitchen.register.fd_cutting_board", "FD Cutting Board Task", "农夫乐事砧板任务");
        addLang("config.maidsoulkitchen.register.fd_cutting_board.tooltip", "This can make the fd cutting board task enabled or not.", "这会启用或禁用农夫乐事砧板任务的注册");
        addLang("config.maidsoulkitchen.register.cd_cuisine_skillet", "Cd Cuisine Skillet Task", "料理乐事炒锅任务");
        addLang("config.maidsoulkitchen.register.cd_cuisine_skillet.tooltip", "This can make the cd cuisine skillet task enabled or not.", "这会启用或禁用料理乐事炒锅任务的注册");
        addLang("config.maidsoulkitchen.register.bd_grill", "Bd Grill Task", "烧烤乐事烧烤架任务");
        addLang("config.maidsoulkitchen.register.bd_grill.tooltip", "This can make the bd grill skillet task enabled or not.", "这会启用或禁用烧烤乐事烧烤架任务的注册");
        addLang("config.maidsoulkitchen.register.bd_basin", "Bd Basin Task", "烧烤乐事配料盆任务");
        addLang("config.maidsoulkitchen.register.bd_basin.tooltip", "This can make the bd basin skillet task enabled or not.", "这会启用或禁用烧烤乐事配料盆任务的注册");
        addLang("config.maidsoulkitchen.register.md_cook_pot", "MD Cook Pot Task", "旷工乐事铜锅任务");
        addLang("config.maidsoulkitchen.register.md_cook_pot.tooltip", "This can make the md cook pot task enabled or not.", "这会启用或禁用农夫旷工乐事铜锅任务的注册");
        addLang("config.maidsoulkitchen.register.fr_kettle", "Fr Kettle", "农夫暇事茶壶任务");
        addLang("config.maidsoulkitchen.register.fr_kettle.tooltip", "This can make the fr kettle task enabled or not.", "这会启用或禁用农夫暇事茶壶任务的注册");
        addLang("config.maidsoulkitchen.register.bnc_key", "Bnc Key", "饮酒作乐发酵桶任务");
        addLang("config.maidsoulkitchen.register.bnc_key.tooltip", "This can make the bnc key task enabled or not.", "这会启用或禁用饮酒作乐发酵桶任务的注册");
        addLang("config.maidsoulkitchen.register.yhc_tea_kettle", "Yhc Tea Kettle Task", "妖怪归家茶壶任务");
        addLang("config.maidsoulkitchen.register.yhc_tea_kettle.tooltip", "This can make the yhc tea kettle task enabled or not.", "这会启用或禁用妖怪归家茶壶任务的注册");
        addLang("config.maidsoulkitchen.register.yhc_fermentation_tank", "Yhc Fermentation Tank Task", "妖怪归家发酵桶任务");
        addLang("config.maidsoulkitchen.register.yhc_fermentation_tank.tooltip", "This can make the yhc fermentation tank task enabled or not.", "这会启用或禁用妖怪归家发酵桶任务的注册");
        addLang("config.maidsoulkitchen.register.yhc_drying_rack", "Yhc Drying Rack Task", "妖怪归家晒干架任务");
        addLang("config.maidsoulkitchen.register.yhc_drying_rack.tooltip", "This can make the yhc drying rack task enabled or not.", "这会启用或禁用妖怪归家晒干架任务的注册");
        addLang("config.maidsoulkitchen.register.yhc_moka", "Yhc Moka Task", "妖怪归家摩卡壶任务");
        addLang("config.maidsoulkitchen.register.yhc_moka.tooltip", "This can make the yhc moka task enabled or not.", "这会启用或禁用妖怪归家摩卡壶任务的注册");
        addLang("config.maidsoulkitchen.register.cp_crock_pot", "Cp Crock Pot Task", "烹饪锅烹饪锅任务");
        addLang("config.maidsoulkitchen.register.cp_crock_pot.tooltip", "This can make the cp crock pot task enabled or not.", "这会启用或禁用烹饪锅烹饪锅任务的注册");
        addLang("config.maidsoulkitchen.register.db_beer", "Db Beer Task", "喝啤酒啦啤酒桶任务");
        addLang("config.maidsoulkitchen.register.db_beer.tooltip", "This can make the db beer task enabled or not.", "这会启用或禁用喝啤酒啦啤酒桶任务的注册");
        addLang("config.maidsoulkitchen.register.kk_brew_barrel", "Kk Brew Barrel Task", "胡萝卜厨房酿造桶任务");
        addLang("config.maidsoulkitchen.register.kk_brew_barrel.tooltip", "This can make the kk brew barrel task enabled or not.", "这会启用或禁用胡萝卜厨房酿造桶任务的注册");
        addLang("config.maidsoulkitchen.register.kk_air_compressor", "Kk Air Compressor Task", "胡萝卜厨房空气压缩机任务");
        addLang("config.maidsoulkitchen.register.kk_air_compressor.tooltip", "This can make the kk air compressor task enabled or not.", "这会启用或禁用胡萝卜厨房空气压缩机任务的注册");
        addLang("config.maidsoulkitchen.register.dbk_cooking_pot", "Dbk Cooking Pot", "馥郁烘焙小烹饪锅任务");
        addLang("config.maidsoulkitchen.register.dbk_cooking_pot.tooltip", "This can make the dbk cooking pot task enabled or not.", "这会启用或禁用馥郁烘焙小烹饪锅任务的注册");
        addLang("config.maidsoulkitchen.register.dbp_mini_fridge", "Dbp Mini Fridge Task", "沙滩派对小冰箱任务");
        addLang("config.maidsoulkitchen.register.dbp_mini_fridge.tooltip", "This can make the dbp mini fridge task enabled or not.", "这会启用或禁用沙滩派对小冰箱任务的注册");
        addLang("config.maidsoulkitchen.register.dbp_tiki_bar", "Dbp Tiki Bar Task", "沙滩派对提基吧台任务");
        addLang("config.maidsoulkitchen.register.dbp_tiki_bar.tooltip", "This can make the dbp tiki bar task enabled or not.", "这会启用或禁用沙滩派对提基吧台任务的注册");
        addLang("config.maidsoulkitchen.register.dcl_cooking_pan", "Dcl Cooking Pan Task", "烛火晚宴汤锅任务");
        addLang("config.maidsoulkitchen.register.dcl_cooking_pan.tooltip", "This can make the dcl cooking pan task enabled or not.", "这会启用或禁用烛火晚宴汤锅任务的注册");
        addLang("config.maidsoulkitchen.register.dcl_cooking_pot", "Dcl Cooking Pot Task", "烛火晚宴平底锅任务");
        addLang("config.maidsoulkitchen.register.dcl_cooking_pot.tooltip", "This can make the dcl cooking pot task enabled or not.", "这会启用或禁用烛火晚宴平底锅任务的注册");
        addLang("config.maidsoulkitchen.register.dcl_stove", "Dcl Stove", "烛火晚宴烤炉任务");
        addLang("config.maidsoulkitchen.register.dcl_stove.tooltip", "This can make the dcl cooking stove task enabled or not.", "这会启用或禁用馥郁烘焙烤炉任务的注册");
        addLang("config.maidsoulkitchen.register.dfc_roast", "Dfc Roast", "沉浸农艺烘焙机任务");
        addLang("config.maidsoulkitchen.register.dfc_roast.tooltip", "This can make the dfc roast task enabled or not.", "这会启用或禁用沉浸农艺烘焙机任务的注册");
        addLang("config.maidsoulkitchen.register.dfc_cooking_pot", "Dfc Cooking Pot Task", "沉浸农艺烹饪锅任务");
        addLang("config.maidsoulkitchen.register.dfc_cooking_pot.tooltip", "This can make the dfc cooking pot task enabled or not.", "这会启用或禁用沉浸农艺烹饪锅任务的注册");
        addLang("config.maidsoulkitchen.register.dfc_stove", "Dfc Stove", "沉浸农艺烤炉任务");
        addLang("config.maidsoulkitchen.register.dfc_stove.tooltip", "This can make the dfc stove task enabled or not.", "这会启用或禁用沉浸农艺烤炉任务的注册");
        addLang("config.maidsoulkitchen.register.dhb_cauldron", "Dhb Cauldron Task", "煨茶酝露酿煮锅任务");
        addLang("config.maidsoulkitchen.register.dhb_cauldron.tooltip", "This can make the dhb cauldron task enabled or not.", "这会启用或禁用煨茶酝露酿煮锅任务的注册");
        addLang("config.maidsoulkitchen.register.dhb_tea_kettle", "Dhb Tea Kettle Task", "煨茶酝露茶壶任务");
        addLang("config.maidsoulkitchen.register.dhb_tea_kettle.tooltip", "This can make the dhb tea kettle task enabled or not.", "这会启用或禁用煨茶酝露茶壶任务的注册");
        addLang("config.maidsoulkitchen.register.fermentation_barrel", "Fermentation Barrel Task", "葡园酒香陈酿桶任务");
        addLang("config.maidsoulkitchen.register.fermentation_barrel.tooltip", "This can make the fermentation barrel task enabled or not.", "这会启用或禁用葡园酒香陈酿桶任务的注册");
    }

    protected void addTaskLang() {
        addLang("task.maidsoulkitchen.dkb_cooking_pot", "Cooking Pot", "小烹饪锅");
        addLang("task.maidsoulkitchen.dkb_cooking_pot.desc", "Maid can cook food by use the let's do bakery cooking pot", "女仆会使用馥郁烘焙的小烹饪锅烹饪食物");
        addLang("task.maidsoulkitchen.dkb_cooking_pot.enable_condition.has_enough_favor", "Maid favor level >= 1", "女仆好感度等级大于等于1");
        addLang("task.maidsoulkitchen.dbp_mini_fridge", "Mini Fridge", "小冰箱");
        addLang("task.maidsoulkitchen.dbp_mini_fridge.desc", "Maid can cook food by use the let's do beachparty mini fridge", "女仆会使用沙滩派对的小冰箱制作冰淇淋等");
        addLang("task.maidsoulkitchen.dbp_mini_fridge.enable_condition.has_enough_favor", "Maid favor level >= 1", "女仆好感度等级大于等于1");
        addLang("task.maidsoulkitchen.dbp_tiki_bar", "Tiki Bar", "提基吧台");
        addLang("task.maidsoulkitchen.dbp_tiki_bar.desc", "Maid can cook food by use the let's do beachparty tiki bar", "女仆会使用沙滩派对的提基吧台冲制饮品");
        addLang("task.maidsoulkitchen.dbp_tiki_bar.enable_condition.has_enough_favor", "Maid favor level >= 1", "女仆好感度等级大于等于1");
        addLang("task.maidsoulkitchen.bnc_key", "Fermentation Barrel", "发酵桶");
        addLang("task.maidsoulkitchen.bnc_key.desc", "Maid can brew wine using the Brewin' and Chewin' fermentation barrel", "女仆会使用饮酒作乐的发酵桶酿酒");
        addLang("task.maidsoulkitchen.bnc_key.enable_condition.has_enough_favor", "Maid favor level >= 1", "女仆好感度等级大于等于1");
        addLang("task.maidsoulkitchen.fr_kettle", "Teapot", "茶壶");
        addLang("task.maidsoulkitchen.fr_kettle.desc", "Maid can make tea using the Farmer's Respite teapot", "女仆会使用农夫暇事的茶壶煮茶");
        addLang("task.maidsoulkitchen.fr_kettle.enable_condition.has_enough_favor", "Maid favor level >= 1", "女仆好感度等级大于等于1");
        addLang("task.maidsoulkitchen.dcl_cooking_pot", "Stock Pot", "汤锅");
        addLang("task.maidsoulkitchen.dcl_cooking_pot.desc", "Maid can cook food using the Let's Do Candlelight stock pot", "女仆会使用烛火晚宴的汤锅烹饪食物");
        addLang("task.maidsoulkitchen.dcl_cooking_pot.enable_condition.has_enough_favor", "Maid favor level >= 1", "女仆好感度等级大于等于1");
        addLang("task.maidsoulkitchen.dcl_cooking_pan", "Frying Pan", "平底锅");
        addLang("task.maidsoulkitchen.dcl_cooking_pan.desc", "Maid can cook food using the Let's Do Candlelight frying pan", "女仆会使用烛火晚宴的平底锅烹饪食物");
        addLang("task.maidsoulkitchen.dcl_cooking_pan.enable_condition.has_enough_favor", "Maid favor level >= 1", "女仆好感度等级大于等于1");
        addLang("task.maidsoulkitchen.dcl_stove", "Oven", "烤炉");
        addLang("task.maidsoulkitchen.dcl_stove.desc", "Maid can bake pastries using the Let's Do Candlelight oven", "女仆会使用烛火晚宴的烤炉烘焙糕点");
        addLang("task.maidsoulkitchen.dcl_stove.enable_condition.has_enough_favor", "Maid favor level >= 1", "女仆好感度等级大于等于1");
        addLang("task.maidsoulkitchen.dfc_cooking_pot", "Stock Pot", "汤锅");
        addLang("task.maidsoulkitchen.dfc_cooking_pot.desc", "Maid can cook food using the Let's Do Farm and Charm stock pot", "女仆会使用沉浸农艺的汤锅烹饪食物");
        addLang("task.maidsoulkitchen.dfc_cooking_pot.enable_condition.has_enough_favor", "Maid favor level >= 1", "女仆好感度等级大于等于1");
        addLang("task.maidsoulkitchen.dfc_roast", "Roaster", "烘焙机");
        addLang("task.maidsoulkitchen.dfc_roast.desc", "Maid can cook food using the Let's Do Farm and Charm roaster", "女仆会使用沉浸农艺的烘焙机烹饪食物");
        addLang("task.maidsoulkitchen.dfc_roast.enable_condition.has_enough_favor", "Maid favor level >= 1", "女仆好感度等级大于等于1");
        addLang("task.maidsoulkitchen.dfc_stove", "Oven", "烤炉");
        addLang("task.maidsoulkitchen.dfc_stove.desc", "Maid can bake pastries using the Let's Do Farm and Charm oven", "女仆会使用沉浸农艺的烤炉烘焙糕点");
        addLang("task.maidsoulkitchen.dfc_stove.enable_condition.has_enough_favor", "Maid favor level >= 1", "女仆好感度等级大于等于1");
        addLang("task.maidsoulkitchen.farmersdelight_cooking_pot_cooking", "Cooking Pot", "厨锅");
        addLang("task.maidsoulkitchen.farmersdelight_cooking_pot_cooking.desc", "Maid can cook food using the Farmer's Delight cooking pot", "女仆会使用农夫乐事厨锅烹饪食物");
        addLang("task.maidsoulkitchen.farmersdelight_cooking_pot_cooking.enable_condition.has_enough_favor", "Maid favor level >= 1", "女仆好感度等级大于等于1");
        addLang("task.maidsoulkitchen.farmersdelight_cutting_board_cutting", "Cutting Board", "砧板");
        addLang("task.maidsoulkitchen.farmersdelight_cutting_board_cutting.desc", "Maid can process ingredients using the Farmer's Delight cutting board", "女仆会使用农夫乐事砧板处理食材");
        addLang("task.maidsoulkitchen.farmersdelight_cutting_board_cutting.enable_condition.has_enough_favor", "Maid favor level >= 1", "女仆好感度等级大于等于1");
        addLang("task.maidsoulkitchen.cuisinedelight_cuisine_skillet_cuisine", "Wok", "炒锅");
        addLang("task.maidsoulkitchen.cuisinedelight_cuisine_skillet_cuisine.desc", "Maid can stir-fry using the Cuisine Delight wok", "女仆会使用料理乐事炒锅炒菜");
        addLang("task.maidsoulkitchen.cuisinedelight_cuisine_skillet_cuisine.enable_condition.has_enough_favor", "Maid favor level >= 1", "女仆好感度等级大于等于1");
        addLang("task.maidsoulkitchen.barbequesdelight_grill_grilling", "Grill", "烧烤架");
        addLang("task.maidsoulkitchen.barbequesdelight_grill_grilling.desc", "Maid can grill using the Barbeque Delight grill", "女仆会使用烧烤乐事烧烤架烧烤");
        addLang("task.maidsoulkitchen.barbequesdelight_grill_grilling.enable_condition.has_enough_favor", "Maid favor level >= 1", "女仆好感度等级大于等于1");
        addLang("task.maidsoulkitchen.barbequesdelight_basin_skewering", "Ingredient Basin", "配料盆");
        addLang("task.maidsoulkitchen.barbequesdelight_basin_skewering.desc", "Maid can skewer ingredients using the Barbeque Delight basin", "女仆会使用烧烤乐事配料盆串烧食材");
        addLang("task.maidsoulkitchen.barbequesdelight_basin_skewering.enable_condition.has_enough_favor", "Maid favor level >= 1", "女仆好感度等级大于等于1");
        addLang("task.maidsoulkitchen.miners_delight_copper_pot_cooking", "Copper Pot", "铜锅");
        addLang("task.maidsoulkitchen.miners_delight_copper_pot_cooking.desc", "Maid can cook food using the Miner's Delight copper pot", "女仆会使用矿工乐事的铜锅烹饪食物");
        addLang("task.maidsoulkitchen.miners_delight_copper_pot_cooking.enable_condition.has_enough_favor", "Maid favor level >= 1", "女仆好感度等级大于等于1");
        addLang("task.maidsoulkitchen.youkaishomecoming_kettle_kettle", "Teapot", "茶壶");
        addLang("task.maidsoulkitchen.youkaishomecoming_kettle_kettle.desc", "Maid can make tea using the Youkai's Homecoming teapot", "女仆会使用妖怪归家的茶壶泡茶");
        addLang("task.maidsoulkitchen.youkaishomecoming_kettle_kettle.enable_condition.has_enough_favor", "Maid favor level >= 1", "女仆好感度等级大于等于1");
        addLang("task.maidsoulkitchen.youkaishomecoming_fermentation_tank_fermentation", "Fermentation Tank", "发酵桶");
        addLang("task.maidsoulkitchen.youkaishomecoming_fermentation_tank_fermentation.desc", "Maid can brew wine using the Youkai's Homecoming fermentation tank", "女仆会使用妖怪归家的发酵桶酿酒");
        addLang("task.maidsoulkitchen.youkaishomecoming_fermentation_tank_fermentation.enable_condition.has_enough_favor", "Maid favor level >= 1", "女仆好感度等级大于等于1");
        addLang("task.maidsoulkitchen.youkaishomecoming_drying_rack_drying_rack", "Drying Rack", "晒干架");
        addLang("task.maidsoulkitchen.youkaishomecoming_drying_rack_drying_rack.desc", "Maid can dry tea leaves using the Youkai's Homecoming drying rack", "女仆会使用妖怪归家的晒干架晾置茶叶");
        addLang("task.maidsoulkitchen.youkaishomecoming_drying_rack_drying_rack.enable_condition.has_enough_favor", "Maid favor level >= 1", "女仆好感度等级大于等于1");
        addLang("task.maidsoulkitchen.youkaishomecoming_moka_pot_moka_pot", "Moka Pot", "摩卡壶");
        addLang("task.maidsoulkitchen.youkaishomecoming_moka_pot_moka_pot.desc", "Maid can make coffee using the Youkai's Homecoming moka pot", "女仆会使用妖怪归家的摩卡壶冲咖啡");
        addLang("task.maidsoulkitchen.youkaishomecoming_moka_pot_moka_pot.enable_condition.has_enough_favor", "Maid favor level >= 1", "女仆好感度等级大于等于1");
        addLang("task.maidsoulkitchen.berries_farm", "Berries", "浆果");
        addLang("task.maidsoulkitchen.berries_farm.desc", "Maid can harvest berries", "女仆会采集浆果");
        addLang("task.maidsoulkitchen.fruit_farm", "Fruit", "水果");
        addLang("task.maidsoulkitchen.fruit_farm.desc", "Maid can harvest fruit", "女仆会采摘水果");
        addLang("task.maidsoulkitchen.sereneseasons_farm", "Serene Seasons Farm", "四季农场");
        addLang("task.maidsoulkitchen.sereneseasons_farm.desc", "Maid can properly harvest and plant seasonal crops", "女仆会正确收获和种植四季作物");
        addLang("task.maidsoulkitchen.eclipticseasons_farm", "Ecliptic Seasons Farm", "节气农场");
        addLang("task.maidsoulkitchen.eclipticseasons_farm.desc", "Maid can properly harvest and plant solar term crops", "女仆会正确收获和种植节气作物");
        addLang("task.maidsoulkitchen.compat_melon", "Melon (Compat)", "瓜类(兼容)");
        addLang("task.maidsoulkitchen.compat_melon.desc", "Maid will harvest vanilla and other mods' melons, need to configure", "女仆会收获原版以及其他模组的瓜类,需要自行配置");
        addLang("task.maidsoulkitchen.compat_melon.condition.has_silk_touch", "Holding a tool with Silk Touch enchantment in main hand allows precise harvesting of melons", "主手持有带有精准采集附魔的工具时，能够精准采集瓜");
        addLang("task.maidsoulkitchen.feed_animal_t", "BreedT", "繁殖2");
        addLang("task.maidsoulkitchen.feed_animal_t.desc", "Maid will attempt to breed different types of surrounding animals", "女仆尝试繁殖周围不同种类的动物");
        addLang("task.maidsoulkitchen.feed_animal_t.condition.can_feed", "Have items that can breed animals in the maid's inventory", "女仆背包中有可以繁殖动物的物品");
        addLang("task.maidsoulkitchen.feed_animal_t.condition.assault_weapon", "Main hand holds an assault weapon (to kill excess animals)", "主手持有攻击性武器（用于杀死超过上限的动物）");
        addLang("task.maidsoulkitchen.minecraft_furnace_smelting", "Furnace", "熔炉");
        addLang("task.maidsoulkitchen.minecraft_furnace_smelting.desc", "Maid will cook food or smelt ores, etc.", "女仆会烹饪食物或者熔炼矿物等");
        addLang("task.maidsoulkitchen.feedanddrink", "Feed and Drink", "喂食与饮水");
        addLang("task.maidsoulkitchen.feedanddrink.desc", "Maid will actively feed hungry or thirsty masters nearby", "女仆会主动投喂周围饥渴的主人");
        addLang("task.maidsoulkitchen.crockpot_crock_pot_crock_pot_cooking", "Crock Pot", "烹饪锅");
        addLang("task.maidsoulkitchen.crockpot_crock_pot_crock_pot_cooking.desc", "Maid can cook food using the Crock Pot", "女仆会使用烹饪锅的烹饪锅烹饪食物");
        addLang("task.maidsoulkitchen.crockpot_crock_pot_crock_pot_cooking.enable_condition.has_enough_favor", "Maid favor level >= 2", "女仆好感度等级大于等于2");
        addLang("task.maidsoulkitchen.drinkbeer_beer_barrel_brewing", "Beer Barrel", "啤酒桶");
        addLang("task.maidsoulkitchen.drinkbeer_beer_barrel_brewing.desc", "Maid can brew beer using the Drink Beer beer barrel", "女仆会使用和啤酒啦的啤酒桶酿酒");
        addLang("task.maidsoulkitchen.drinkbeer_beer_barrel_brewing.enable_condition.has_enough_favor", "Maid favor level >= 1", "女仆好感度等级大于等于1");
        addLang("task.maidsoulkitchen.kitchenkarrot_brewing_barrel_brewing_barrel", "Brewing Barrel", "酿造桶");
        addLang("task.maidsoulkitchen.kitchenkarrot_brewing_barrel_brewing_barrel.desc", "Maid can brew wine using the Kitchen Karrot brewing barrel", "女仆会使用胡萝卜厨房的酿造桶酿酒");
        addLang("task.maidsoulkitchen.kitchenkarrot_brewing_barrel_brewing_barrel.enable_condition.has_enough_favor", "Maid favor level >= 1", "女仆好感度等级大于等于1");
        addLang("task.maidsoulkitchen.kitchenkarrot_air_compressor_air_compressing", "Air Compressor", "空气压缩机");
        addLang("task.maidsoulkitchen.kitchenkarrot_air_compressor_air_compressing.desc", "Maid can make food using the Kitchen Karrot air compressor", "女仆会使用胡萝卜厨房的空气压缩机制作食物");
        addLang("task.maidsoulkitchen.kitchenkarrot_air_compressor_air_compressing.enable_condition.has_enough_favor", "Maid favor level >= 1", "女仆好感度等级大于等于1");
        addLang("task.maidsoulkitchen.cook", "Cook", "烹饪");
        addLang("task.maidsoulkitchen.cook.desc", "Maid will cook using cooking utensils", "女仆会烹饪厨具进行烹饪");
        addLang("task.maidsoulkitchen.idle", "Idle", "空闲");
    }

    protected void addOverlayLang() {
        addLang("overlay.maidsoulkitchen.book.tips", "The book will change the working mode to the retrieval start height value of the maid of the fruit\nRight-click +1, stealth right-click -1\nThe search range is 2 blocks up from the starting height", "书本会改变工作模式为水果的女仆的检索起始高度值\n右击+1,潜行右击-1\n检索范围为起始高度向上延伸2格");
        addLang("overlay.maidsoulkitchen.book.tips.search_yoffset", "SearchYOffest: ", "检索起始高度: ");
    }

    protected void addTopLang() {
        addLang("top.maidsoulkitchen.entity_maid.farm.rule", "Rule: ", "规则: ");
        addLang("top.maidsoulkitchen.entity_maid.farm.fruit.search_y_offset", "Y_Retrieval: ", "检索高度: ");
        addLang("top.maidsoulkitchen.entity_maid.task.cook.type", "Type: ", "烹饪类型: ");
    }

    protected void addMessageLang() {
        addLang("message.maidsoulkitchen.book.max_yoffset", "The value of YOffset must be within the range of -5 to 5", "检索起始高度的取值范围为-5到5");
        addLang("message.maidsoulkitchen.culinary_hub.bine_type_max", "The maximum number of chests of the type currently bound is 3", "当前绑定的类型的箱子最多为3个");
        addLang("message.maidsoulkitchen.warning.title", "---------------- Maidsoul Kitchen warning---------------", "----------------女仆厨房警告---------------");
        addLang("message.maidsoulkitchen.warning.compat_failed", "Some mods have failed to be compatible, and they have been automatically blocked for you!", "当前有部分mod兼容失败，已自动为您拦截！");
        addLang("message.maidsoulkitchen.warning.clicked_to_report", "Please click here to give feedback to the author, otherwise it will never be resolved! ", "请点击此处，向作者反馈，不然永远不会得到解决！");
        addLang("message.maidsoulkitchen.warning.clicked_to_export", "Click here to open the folder, find %s, attach this file!", "点击此处打开文件夹，找到%s，附上此文件即可！");
        addLang("message.maidsoulkitchen.warning.feedbacked", "If you've already given feedback, or if the author already knows, you can ignore this information!", "如果您已经反馈了，或者作者已经知晓，可忽略此信息！");
        addLang("message.maidsoulkitchen.warning.failed_task", ">- Task: ", ">- 任务：");
        addLang("message.maidsoulkitchen.warning.failed_modid", ", modid; ", "，模组ID：");
        addLang("message.maidsoulkitchen.warning.end", "-----------------------------------------", "-----------------------------------------");
    }

    protected void addRuleLang() {
        addLang("rule.berry.maidsoulkitchen.berry_minecraft", "Minecraft", "原版");
        addLang("rule.berry.maidsoulkitchen.berry_minecraft.desc", "The maid will actively collect the minecraft sweet berries around here", "女仆会主动采集周围原版的甜浆果");
        addLang("rule.berry.maidsoulkitchen.berry_l2_harvester", "LightLand", "莱特兰作物");
        addLang("rule.berry.maidsoulkitchen.berry_l2_harvester.desc", "The maid will actively collect the lightland series crops around here", "女仆会主动采集周围莱特兰系列的作物");
        addLang("rule.berry.maidsoulkitchen.berry_farmersrespite_greentea", "Farmer's Respite (Green Tea Leaves)", "农夫暇事（绿茶叶）");
        addLang("rule.berry.maidsoulkitchen.berry_farmersrespite_greentea.desc", "The maid will take the initiative to collect the green tea leaves of the surrounding farmers respite", "女仆会主动采集周围农夫暇事的绿茶叶");
        addLang("rule.berry.maidsoulkitchen.berry_farmersrespite_greentea.condition.hastool", "Maid backpack with shears required", "需要女仆背包里有剪刀");
        addLang("rule.berry.maidsoulkitchen.berry_farmersrespite_yellowtea", "Farmer's Respite (Yellow Tea Leaves)", "农夫暇事（黄茶叶）");
        addLang("rule.berry.maidsoulkitchen.berry_farmersrespite_yellowtea.desc", "The maid will take the initiative to collect the yellow tea leaves of the surrounding farmers respite", "女仆会主动采集周围农夫暇事的黄茶叶");
        addLang("rule.berry.maidsoulkitchen.berry_farmersrespite_yellowtea.condition.hastool", "Maid backpack with shears required", "需要女仆背包里有剪刀");
        addLang("rule.berry.maidsoulkitchen.berry_farmersrespite_blacktea", "Farmer's Respite (Black Tea Leaves)", "农夫暇事（红茶叶）");
        addLang("rule.berry.maidsoulkitchen.berry_farmersrespite_blacktea.desc", "The maid will take the initiative to collect the black tea leaves of the surrounding farmers respite", "女仆会主动采集周围农夫暇事的红茶叶");
        addLang("rule.berry.maidsoulkitchen.berry_farmersrespite_blacktea.condition.hastool", "Maid backpack with shears required", "需要女仆背包里有剪刀");
        addLang("rule.berry.maidsoulkitchen.berry_simple_farming", "Simple Farming", "简单农业");
        addLang("rule.berry.maidsoulkitchen.berry_simple_farming.desc", "The maid will take the initiative to collect all kinds of shrubs from the simple farming around them", "女仆会主动采集周围简单农业的各种灌木");
        addLang("rule.berry.maidsoulkitchen.berry_vinery", "Vinery", "葡园酒香");
        addLang("rule.berry.maidsoulkitchen.berry_vinery.desc", "The maid will take the initiative to collect the various grapes that are fragrant from the surrounding viney", "女仆会主动采集周围葡园酒香的各种葡萄");
        addLang("rule.berry.maidsoulkitchen.berry_compat", "Compat", "兼容模式");
        addLang("rule.berry.maidsoulkitchen.berry_compat.desc", "The maid will try to gather various berries around here", "女仆会尝试采集周围的各种浆果");
        addLang("rule.fruit.maidsoulkitchen.fruit_fruit_stack", "Fruit Stack", "果栈丰盈");
        addLang("rule.fruit.maidsoulkitchen.fruit_fruit_stack.desc", "The maid will take the initiative to pick a variety of fruits from the surrounding fruit stack", "女仆会采摘周围果栈丰盈的各种水果");
        addLang("rule.fruit.maidsoulkitchen.fruit_simple_farming", "Simple Farming", "简单农业");
        addLang("rule.fruit.maidsoulkitchen.fruit_simple_farming.desc", "The maid will take the initiative to pick a variety of fruits from the surrounding simple farming", "女仆会主动采摘周围简单农业的各种水果");
        addLang("rule.fruit.maidsoulkitchen.fruit_vinery", "Vinery", "葡园酒香");
        addLang("rule.fruit.maidsoulkitchen.fruit_vinery.desc", "The maid will take the initiative to pick a variety of fruits from the surrounding vinery", "女仆会主动采摘周围葡园酒香的各种水果");
        addLang("rule.fruit.maidsoulkitchen.fruit_compat", "Compat", "兼容模式");
        addLang("rule.fruit.maidsoulkitchen.fruit_compat.desc", "The maid will try to pick various fruits around here", "女仆会尝试采摘周围的各种水果");
    }

    protected void addGuiLang() {
        addLang("gui.maidsoulkitchen.btn.display.tooltip.1", "Click to search, click to take back again!", "点击可搜索，再次点击收回！");
        addLang("gui.maidsoulkitchen.btn.display.tooltip.2", "Scroll to switch display modes!", "滚动切换显示模式！");
        addLang("gui.maidsoulkitchen.btn.display.mode.default", "Default", "默认");
        addLang("gui.maidsoulkitchen.btn.display.mode.can_cook", "Can cook", "可烹饪");
        addLang("gui.maidsoulkitchen.btn.display.mode.not_cook", "Not cook", "不可烹饪");
        addLang("gui.maidsoulkitchen.btn.task_setting", "Task Setting", "任务设置");
        addLang("gui.maidsoulkitchen.btn.task_setting.desc", " -§a used to configer task, for example various of cooking task", " -§a 用于一些任务的设置,比如各种模式的烹饪");
        addLang("gui.maidsoulkitchen.btn.task_book", "Task Book", "任务手册");
        addLang("gui.maidsoulkitchen.btn.task_book.desc", " -§a used to open the task book entry from memorizable gensokyo", " -§a 用于跳转到手册对应的章节");
        addLang("gui.maidsoulkitchen.btn.task_info", "Task Info", "任务信息");
        addLang("gui.maidsoulkitchen.btn.task_info.desc", " -§a used to display simple info", " -§a 用于简单显示该任务的信息");
        addLang("gui.maidsoulkitchen.btn.task_warn_text", " -§c is dev, hope, wish", " -§c 还在开发中,敬请期待");
        addLang("gui.maidsoulkitchen.btn.cook_guide.type.blacklist", "Blacklist", "黑名单");
        addLang("gui.maidsoulkitchen.btn.cook_guide.type.whitelist", "Whitelist", "白名单");
        addLang("gui.maidsoulkitchen.btn.cook_guide.type.whitelist.desc.0", "Change maid cook mode: Blacklist", "切换女仆烹饪模式: 黑名单");
        addLang("gui.maidsoulkitchen.btn.cook_guide.type.whitelist.desc.1", "- The maid will randomly cook from all recipes removed from the blacklist", "- 女仆会从除去黑名单里所有的配方里随机烹饪");
        addLang("gui.maidsoulkitchen.btn.cook_guide.type.blacklist.desc.0", "Change maid cook mode: Whitelist", "切换女仆烹饪模式: 白名单");
        addLang("gui.maidsoulkitchen.btn.cook_guide.type.blacklist.desc.1", "- The maid will cook randomly from the selected recipe", "- 女仆会从选定的配方里随机烹饪");
        addLang("gui.maidsoulkitchen.btn.cook_guide.can_cook", "The food is cookable: ", "当前食物是否可烹饪：");
        addLang("gui.maidsoulkitchen.btn.cook_guide.can_cook.true", "Yes", "是");
        addLang("gui.maidsoulkitchen.btn.cook_guide.can_cook.false", "No", "否");
        addLang("gui.maidsoulkitchen.btn.cook_guide.warn.now_type", "Now cook mode: ", "当前烹饪模式: ");
        addLang("gui.maidsoulkitchen.btn.cook_guide.warn.not_select", "This cook mode type isn't selected, so can't select", "当前烹饪模式不是定向模式，无法选择");
        addLang("gui.maidsoulkitchen.btn.cook_guide.warn.over_size", "This cook selected recipes is over size : %d, so can't add", "当前烹饪定向配方数已经超额：%d, 无法添加");
        addLang("gui.maidsoulkitchen.btn.cook_guide.selected_recs", "The recipe in %d/%d is currently selected", "当前已选择%d/%d配方");
        addLang("gui.maidsoulkitchen.btn.cook_guide.info.warn", "Task Warning：", "任务警告：");
        addLang("gui.maidsoulkitchen.btn.cook_guide.info.warn.crockpot", "- There are some problems with the recipe analysis of the crockpot task, which will be updated and optimized in the future!", "- 由于烹饪锅的特殊性，配方解析有一些问题，会在后续的更新优化！");
        addLang("gui.maidsoulkitchen.btn.cook_guide.info.warn.furnace", "- Due to the peculiarity of the furnace, it is not currently possible to interact with the culinary hub, but it will be updated and optimized in the future!", "- 由于熔炉的特殊性，所以当前还不能与烹饪中枢交互，会在后续的更新优化！");
        addLang("gui.maidsoulkitchen.btn.cook_guide.info.warn.furnace.1", "- Due to the special nature of the furnace's recipe, it is not currently controlled by the black and white list, and will be updated and optimized in the future!", "- 由于熔炉的配方特殊性，所以当前还没受到黑白名单的控制，会在后续的更新优化！");
        addLang("gui.maidsoulkitchen.btn.cook_guide.info.warn.cuttingboard", "- Due to the particularity of the cutting board task, it is not possible to interact with the culinary hub at present, and it will be updated and optimized in the future!", "- 由于砧板任务的特殊性，所以当前还不能与烹饪中枢交互，会在后续的更新优化！");
        addLang("gui.maidsoulkitchen.widget.cook_guide.task.desc", "Now Task: %s", "当前任务: %s");
        addLang("gui.maidsoulkitchen.widget.cook_guide.task.recipe_type", "RecipeType: %s", "配方类型: %s");
        addLang("gui.maidsoulkitchen.widget.cook_guide.task.choose_cook_type", "Click to select a cooking mode.", "点击选择一个烹饪模式把！");
        addLang("gui.maidsoulkitchen.patchouli_warning.download", "Download Patchouli", "下载 Patchouli");
        addLang("gui.maidsoulkitchen.patchouli_warning.tips", "You'll need to install the Patchouli mod to open book entry feature", "你需要下载Patchouli Mod才可以启用跳转到手册对应章节功能");
        addLang("gui.maidsoulkitchen.cook_setting_screen.title", "Cook Guide", "烹饪指南");
        addLang("gui.maidsoulkitchen.farm_config_screen.title", "Farm Guide", "农场指南");
        addLang("gui.maidsoulkitchen.compat_farm_configer_screen.title", "CompatFarm Guide", "农耕指南");
        addLang("gui.maidsoulkitchen.berry_farm_configure_screen.title", "Berry Farm Guide", "浆果农场指南");
        addLang("gui.maidsoulkitchen.fruit_farm_configer_screen.title", "Fruit Farm Guide", "水果农场指南");
        addLang("gui.maidsoulkitchen.fruit_farm_configer_screen.farm.fruit.search_y_offset", "Y_Retrieval: %s", "检索高度: %s");
        addLang("gui.maidsoulkitchen.config.no_config", "This task has no configuration.", "这个任务没有配置.");
        addLang("gui.maidsoulkitchen.config.global_config", "Configure this task globally.", "全局配置该任务.");
        addLang("gui.maidsoulkitchen.search", "Search...", "搜索...");
        addLang("gui.maidsoulkitchen.to_setting_cook_task", "Click to enter the settings screen and select a cooking mode handle!", "点击进入设置界面选择一个烹饪模式把！");
        addLang("gui.maidsoulkitchen.culinary_hub.bag.ingredient", "Ingredient", "输入原料");
        addLang("gui.maidsoulkitchen.culinary_hub.bag.other", "SA | IA | OA | Output", "启动条件 | 输入原料条件 | 输出条件 | 输出");
        addLang("gui.maidsoulkitchen.culinary_hub.bag.title", "Cook Bag", "烹饪袋");
        addLang("gui.maidsoulkitchen.culinary_hub.config.title", "Culinary hub config", "烹饪中枢配置");
        addLang("gui.maidsoulkitchen.culinary_hub.config.bind_mode", "Bind Mode", "模式绑定");
        addLang("gui.maidsoulkitchen.culinary_hub.config.bind_mode.ingredient", "Ingredient", "输入原料");
        addLang("gui.maidsoulkitchen.culinary_hub.config.bind_mode.start_addition", "Start Addition", "启动条件");
        addLang("gui.maidsoulkitchen.culinary_hub.config.bind_mode.ingredient_addition", "Ingredient Addition", "输入原料条件");
        addLang("gui.maidsoulkitchen.culinary_hub.config.bind_mode.output_addition", "Output Addition", "输出条件");
        addLang("gui.maidsoulkitchen.culinary_hub.config.bind_mode.output", "Output", "输出");
        addLang("gui.maidsoulkitchen.culinary_hub.config.clear_bind_poses", "Clear", "清除");
        addLang("gui.maidsoulkitchen.culinary_hub.config.bind_mode.ingredient.tooltip", "Here is the button to bind the box that stores the ingredients needed for cooking\n\nSuch as red mushrooms and white mushrooms in the mushroom pot, rice and tomatoes for Farmer's Delight's rice, tomato sauce", "这里是用来绑定存放烹饪所需要的原材料的箱子的按钮\n\n比如蘑菇煲的红色蘑菇、白色蘑菇，农夫乐事的米饭、番茄酱所需要的稻米、番茄");
        addLang("gui.maidsoulkitchen.culinary_hub.config.bind_mode.start_addition.tooltip", "Here is the button to bind the box that stores the items needed to start the prerequisites for cooking utensils\n\nSuch as the water (bucket) needed for the teakettle for the youkai's home coming, and the fuel (coal) needed for the crockpot for the crockpot\n\nBut at present, the source of these resources can only be the maid's backpack, so it is better to use it with the gap\n\nRemember to disable the effective slot (the fifth block in the first column).", "这里是用来绑定存放启动厨具烹饪的前置条件的所需要的物品的箱子的按钮\n\n比如妖怪归家的茶壶需要的水资源（水桶）、烹饪锅的烹饪锅需要的燃料（煤炭）\n\n但目前这些资源的来源还只能是女仆背包，所以和隙间搭配使用效果更佳哦\n\n但记的把生效栏位（第一栏第五格）禁用哦");
        addLang("gui.maidsoulkitchen.culinary_hub.config.bind_mode.ingredient_addition.tooltip", "...", "...");
        addLang("gui.maidsoulkitchen.culinary_hub.config.bind_mode.output_addition.tooltip", "Here is the button to bind the box that stores the containers needed to take out the food that has already been cooked\n\nFor example, the bowl needed to take out the mushroom pot in the Farmer's Delight's cooking pot, and the glass bottle for apple cider", "这里是用来绑定存放取出厨具已经烹饪好的食物所需要的容器的箱子的按钮\n\n比如农夫乐事的厨锅取出蘑菇煲所需要的碗，苹果酒所需要的玻璃瓶");
        addLang("gui.maidsoulkitchen.culinary_hub.config.bind_mode.output.tooltip", "Here is the button to bind the box for storing cooked food\n\nsuch as the cooked mushroom pot, rice, and cider", "这里是用来绑定存放烹饪好的食物的箱子的按钮\n\n比如烹饪好的蘑菇煲、米饭、苹果酒");
        addLang("gui.maidsoulkitchen.culinary_hub.config.clear_bind_poses.tooltip", "Clear the coordinates of bound boxes with one click", "一键清除已绑定过的箱子的坐标");
        addLang("gui.maidsoulkitchen.culinary_hub.already_bind_types", "The current block is bound to the following types: ", "当前方块已绑定以下类型：");
        addLang("gui.maidsoulkitchen.culinary_hub.can_sneak_bind", "The current block can be sneak bound!", "当前方块可潜行绑定！");
        addLang("gui.maidsoulkitchen.culinary_hub.right_click_to_bind", "Right-click on the air and choose one to bind!", "还没选择绑定类型呢，面对空气右键选择一个吧！");
        addLang("gui.maidsoulkitchen.culinary_hub.current_binding_type", "Current binding type:", "当前绑定类型：");
        addLang("gui.maidsoulkitchen.development", "(Development)", "(开发中)");
        addLang("gui.maidsoulkitchen.btn.cook_guide.ingredient_source_type", "Source%d: ", "来源%d: ");
        addLang("gui.maidsoulkitchen.btn.cook_guide.ingredient_source_type.main_hand", "Mainhand", "主手");
        addLang("gui.maidsoulkitchen.btn.cook_guide.ingredient_source_type.off_hand", "Offhand", "副手");
        addLang("gui.maidsoulkitchen.btn.cook_guide.ingredient_source_type.maid_backpack", "Backpack", "背包");
        addLang("gui.maidsoulkitchen.btn.cook_guide.ingredient_source_type.hub_start_addition", "Culinary hub(Start Addition)", "烹饪中枢(启动条件)");
        addLang("gui.maidsoulkitchen.btn.cook_guide.ingredient_source_type.hub_ingredient", "Culinary hub(Ingredinet)", "烹饪中枢(输入原料)");
        addLang("gui.maidsoulkitchen.btn.cook_guide.ingredient_source_type.hub_ingredient_addition", "Culinary hub(Ingredient Addition)", "烹饪中枢(输入原料条件)");
        addLang("gui.maidsoulkitchen.btn.cook_guide.ingredient_source_type.hub_output_addition", "Culinary hub(Output Addition)", "烹饪中枢(输出条件)");
        addLang("gui.maidsoulkitchen.btn.cook_guide.ingredient_source_type.hub_output", "Culinary hub(Output)", "烹饪中枢(输出)");
        addLang("gui.maidsoulkitchen.btn.cook_guide.ingredient_source_type.pickup", "Pickup => Mainhand、Offhand、Backpack", "拾取 => 主手、副手、背包");
        addLang("gui.maidsoulkitchen.btn.cook_guide.ingredient_type.mandatory", "Mandatory", "必须");
        addLang("gui.maidsoulkitchen.btn.cook_guide.ingredient_type.maybe", "Maybe", "可能");
        addLang("gui.maidsoulkitchen.btn.cook_guide.ingredient_type.output", "Result", "成品");
        addLang("gui.maidsoulkitchen.btn.cook_guide.output_to", "Output to: ", "输出至%d: ");
    }

    protected void addPatchouliLang() {
        addLang("patchouli.touhou_little_maid.book.entries.maidsoulkitchen.cook", "Cook", "烹饪");
        addLang("patchouli.touhou_little_maid.book.entries.maidsoulkitchen.cook.pages.0.1.text", "Maid can cook by using mod blocks.$(br) You just need to put recipe ingredient on the maid inventory.", "女仆会使用模组厨具方块进行烹饪.$(br)你只需要把配方原料放入到女仆背包.$(br)下面将会介绍女仆烹饪任务的一些信息,请详细阅读");
        addLang("patchouli.touhou_little_maid.book.entries.maidsoulkitchen.cook.pages.1.1.text", "You can use the Cooking Hub to set the location of the cooking ingredients and the food chests$(br) It needs to be placed in the fifth slot in the first column of the maid's backpack$(br) If present, it interacts with the Cooking Hub - the bound crate interacts$(br) Ps:It is even more effective when used with gaps$(br)- Right-click to open the GUI and select the type you want to bind$(br) - Stealth, right-click the chest to bind, and right-click again to unbind", "你可以使用烹饪中枢设置烹饪的原料与食物的箱子的位置$(br) 需要放到女仆背包第一栏第五个格子$(br) 如果存在则与烹饪中枢交互——绑定的箱子交互$(br) Ps:与隙间搭配使用效果更加哦$(br)- 右键打开GUI选择要绑定的类型$(br) - 潜行右击箱子以绑定，再次右击取消绑定");
        addLang("patchouli.touhou_little_maid.book.entries.maidsoulkitchen.cook.pages.4.1.text", "This is the settings screen for the maid's cooking task, and you can click on the sidebar to jump to this screen firstn$(br)- 1.This is a display of the current maid's cooking tasks$(br)- 2. Toggle Cooking Mode: Blacklist vs. Whitelist Mode$(br) - Blacklist Mode: Randomly select the matching recipe from the remaining recipes after removing the selected recipe for cooking$(br) - Whitelist Mode: Randomly select the matching recipe from the selected recipe for cooking$(br)- 3.Select the food you want to cook$(br)- 4.You can click the up and down buttons to switch between the upper and lower page recipes, or the mouse wheel can also be used", "这是女仆烹饪任务的设置界面，你可以点击侧边栏第一个跳转到这界面$(br)- 1.这是显示当前女仆的烹饪任务$(br)- 2.切换烹饪模式：黑名单与白名单模式$(br)  - 黑名单模式：从除去选择的配方后的剩下配方中随机抽取符合的配方进行烹饪$(br)  - 白名单模式：从选定的配方中随机抽取符合的配方进行烹饪$(br)- 3.选择要烹饪的食物$(br)- 4.你可以点击上下按钮来切换上下页配方,或者鼠标滚轮也可以");
        addLang("patchouli.touhou_little_maid.book.entries.maidsoulkitchen.cook.pages.5.1.text", "Use this bauble to prevent kitchen homicides!", "使用这个饰品可以预防厨房杀人案哦！");
        addLang("patchouli.touhou_little_maid.book.entries.maidsoulkitchen.berry.pages.0.1.text", "The maid collects berries, which can be configured on the maid task configuration screen", "女仆会采集浆果，可以女仆任务配置界面配置");
        addLang("patchouli.touhou_little_maid.book.entries.maidsoulkitchen.fruit", "Fruit", "水果");
        addLang("patchouli.touhou_little_maid.book.entries.maidsoulkitchen.fruit.pages.0.1.text", "Maid can harvest fruits, which can be configured on the maid task configuration screen.", "女仆可以采摘水果，可以女仆任务配置界面配置.");
        addLang("patchouli.touhou_little_maid.book.entries.maidsoulkitchen.berry", "Berry", "浆果");
        addLang("patchouli.touhou_little_maid.book.entries.maidsoulkitchen.other", "Other", "其他");
        addLang("patchouli.touhou_little_maid.book.entries.maidsoulkitchen.other.pages.0.1.text", "Welcome to the Farming & Cooking mod", "欢迎下载使用农耕与烹饪模组");
        addLang("patchouli.touhou_little_maid.book.entries.maidsoulkitchen.other.pages.0.text.link_text", "Feedback link", "反馈链接");
        addLang("patchouli.touhou_little_maid.book.entries.maidsoulkitchen.melon", "Melon", "瓜类");
        addLang("patchouli.touhou_little_maid.book.entries.maidsoulkitchen.melon.pages.0.1.text", "The maid will harvest the melons around it, the same as the ontology melon quest, but the compatibility has been made, you can open the maid global configuration page or open the configuration file to configure", "女仆会收获周围的瓜，同本体瓜类任务一样，但做出了兼容性，你可以打开女仆全局配置页面或者打开配置文件进行配置");
        addLang("patchouli.touhou_little_maid.book.entries.maidsoulkitchen.sereneseasons_farm", "Serene Seasons Farm", "四季农场");
        addLang("patchouli.touhou_little_maid.book.entries.maidsoulkitchen.sereneseasons_farm.pages.0.1.text", "If you have installed Serene Seasons, the maid will plant according to the growing season", "如果你安装了静谧四季，那么女仆会按照作物生长季节来种植");
        addLang("patchouli.touhou_little_maid.book.entries.maidsoulkitchen.feed_animal_t", "BreedT", "繁殖");
        addLang("patchouli.touhou_little_maid.book.entries.maidsoulkitchen.feed_animal_t.pages.0.1.text", "The number of breeds in this mode is divided by animal species, with a maximum of 20 per animal", "此模式下繁殖的数量会按照动物种类来区分，每种动物最大繁殖数量为20");
        addLang("patchouli.touhou_little_maid.book.categories.maidsoulkitchen", "Addon[Farm And Cook]", "拓展[农耕与烹饪]");
        addLang("patchouli.touhou_little_maid.book.categories.maidsoulkitchen.description", "A addon that make maid lear more skill about farm and cook.", "一个附属模组,让女仆学会农耕与烹饪技巧.");
    }

    protected void addLang(String key, String en_us, String zh_cn) {
        LangVal enLangVal = new LangVal(Local.EN_US, en_us);
        LangVal zhLangVal = new LangVal(Local.ZH_CN, zh_cn);
        this.map.put(key, Pair.of(enLangVal, zhLangVal));
    }

    protected void addItemLang(Item item, String en_us, String zh_cn) {
        this.addLang(item.getDescriptionId(), en_us, zh_cn);
    }
}
