//package com.catbert.tlma.event;
//
//
//import com.catbert.tlma.MaidsoulKitchen;
//import net.minecraft.tags.TagKey;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//import sereneseasons.api.season.Season;
//import sereneseasons.api.season.SeasonChangedEvent;
//import sereneseasons.init.ModTags;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Mod.EventBusSubscriber
//public final class SeasonEvent {
//
//    public static Map<Level, List<TagKey<?>>> LevelSeasonData = new HashMap<>();
//
//    @SubscribeEvent
//    public static void seasonChange(SeasonChangedEvent<Season.SubSeason> event) {
//        Level level = event.getLevel();
//        Object newSeason = event.getNewSeason();
//        if (newSeason instanceof Season.SubSeason season) {
//
//            TagKey<Block> blockTagKey = switch (season.getSeason()) {
//                case WINTER -> ModTags.Blocks.WINTER_CROPS;
//                case SPRING -> ModTags.Blocks.SPRING_CROPS;
//                case SUMMER -> ModTags.Blocks.SUMMER_CROPS;
//                case AUTUMN -> ModTags.Blocks.AUTUMN_CROPS;
//            };
//
//            TagKey<Item> itemTagKey = switch (season.getSeason()) {
//                case WINTER -> ModTags.Items.WINTER_CROPS;
//                case SPRING -> ModTags.Items.SPRING_CROPS;
//                case SUMMER -> ModTags.Items.SUMMER_CROPS;
//                case AUTUMN -> ModTags.Items.AUTUMN_CROPS;
//            };
//
//            LevelSeasonData.put(level, List.of(blockTagKey, itemTagKey));
//
//            MaidsoulKitchen.LOGGER.info("Season changed to " + LevelSeasonData);
//        }
//    }
//}
