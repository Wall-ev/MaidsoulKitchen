//package com.github.wallev.maidsoulkitchen.util;
//
//import com.google.common.collect.Lists;
//import com.mojang.datafixers.util.Either;
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.DataResult;
//import com.mojang.serialization.MapCodec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import net.minecraft.core.Holder;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.core.registries.Registries;
//import net.minecraft.network.chat.Component;
//import net.minecraft.tags.TagKey;
//import net.minecraft.util.ExtraCodecs;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraft.world.level.block.Blocks;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//
//public class IngredientUtil {
//
//    public static final Codec<Holder<Item>> ITEM_NON_AIR_CODEC = BuiltInRegistries.ITEM.holderByNameCodec().validate((p_330100_) -> p_330100_.is(Items.AIR.builtInRegistryHolder()) ? DataResult.error(() -> "Item must not be minecraft:air") : DataResult.success(p_330100_));
//
//
//    /** @deprecated */
//    @Deprecated
//    private static Codec<Ingredient> codec(boolean allowEmpty) {
//        Codec<IngredientUtil.Value[]> codec = Codec.list(IngredientUtil.Value.CODEC).comapFlatMap((p_300810_) -> !allowEmpty && p_300810_.size() < 1 ? DataResult.error(() -> "Item array cannot be empty, at least one item must be defined") : DataResult.success((IngredientUtil.Value[])p_300810_.toArray(new IngredientUtil.Value[0])), List::of);
//        return Codec.either(codec, IngredientUtil.Value.CODEC).flatComapMap((p_300805_) -> (Ingredient)p_300805_.map(Ingredient::new, (p_300806_) -> new Ingredient(new IngredientUtil.Value[]{p_300806_})), (p_300808_) -> {
//            if (p_300808_.values.length == 1) {
//                return DataResult.success(Either.right(p_300808_.values[0]));
//            } else {
//                return p_300808_.values.length == 0 && !allowEmpty ? DataResult.error(() -> "Item array cannot be empty, at least one item must be defined") : DataResult.success(Either.left(p_300808_.values));
//            }
//        });
//    }
//
////    static {
////        LIST_CODEC = MAP_CODEC_NONEMPTY.codec().listOf();
////        LIST_CODEC_NONEMPTY = LIST_CODEC.validate((list) -> list.isEmpty() ? DataResult.error(() -> "Item array cannot be empty, at least one item must be defined") : DataResult.success(list));
////    }
//
//    public static record ItemValue(ItemStack item) implements IngredientUtil.Value {
//        static final MapCodec<IngredientUtil.ItemValue> MAP_CODEC = RecordCodecBuilder.mapCodec((p_330109_) -> p_330109_.group(ItemStack.SIMPLE_ITEM_CODEC.fieldOf("item").forGetter((p_300919_) -> p_300919_.item)).apply(p_330109_, IngredientUtil.ItemValue::new));
//        static final Codec<IngredientUtil.ItemValue> CODEC;
//
//        public boolean equals(Object other) {
//            boolean var10000;
//            if (other instanceof IngredientUtil.ItemValue ingredient$itemvalue) {
//                var10000 = ingredient$itemvalue.item.getItem().equals(this.item.getItem()) && ingredient$itemvalue.item.getCount() == this.item.getCount();
//            } else {
//                var10000 = false;
//            }
//
//            return var10000;
//        }
//
//        public int hashCode() {
//            return 31 * this.item.getItem().hashCode() + this.item.getCount();
//        }
//
//        public Collection<ItemStack> getItems() {
//            return Collections.singleton(this.item);
//        }
//
//        static {
//            CODEC = MAP_CODEC.codec();
//        }
//    }
//
//    public static record TagValue(TagKey<Item> tag) implements IngredientUtil.Value {
//        static final MapCodec<IngredientUtil.TagValue> MAP_CODEC = RecordCodecBuilder.mapCodec((p_301118_) -> p_301118_.group(TagKey.codec(Registries.ITEM).fieldOf("tag").forGetter((p_301154_) -> p_301154_.tag)).apply(p_301118_, IngredientUtil.TagValue::new));
//        static final Codec<IngredientUtil.TagValue> CODEC;
//
//        public boolean equals(Object other) {
//            boolean var10000;
//            if (other instanceof IngredientUtil.TagValue ingredient$tagvalue) {
//                var10000 = ingredient$tagvalue.tag.location().equals(this.tag.location());
//            } else {
//                var10000 = false;
//            }
//
//            return var10000;
//        }
//
//        public Collection<ItemStack> getItems() {
//            List<ItemStack> list = Lists.newArrayList();
//
//            for(Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(this.tag)) {
//                list.add(new ItemStack(holder));
//            }
//
//            if (list.isEmpty()) {
//                ItemStack itemStack = new ItemStack(Blocks.BARRIER);
////                itemStack.set(DataComponents.CUSTOM_NAME, Component.literal("Empty Tag: " + String.valueOf(this.tag.location())));
//                list.add(itemStack);
//            }
//
//            return list;
//        }
//
//        static {
//            CODEC = MAP_CODEC.codec();
//        }
//    }
//
//    public interface Value {
//        MapCodec<IngredientUtil.Value> MAP_CODEC = ExtraCodecs.xor(IngredientUtil.ItemValue.MAP_CODEC, IngredientUtil.TagValue.MAP_CODEC).xmap((p_300956_) -> (IngredientUtil.Value)p_300956_.map((p_300932_) -> p_300932_, (p_301313_) -> p_301313_), (p_301304_) -> {
//            if (p_301304_ instanceof IngredientUtil.TagValue ingredient$tagvalue) {
//                return Either.right(ingredient$tagvalue);
//            } else if (p_301304_ instanceof IngredientUtil.ItemValue ingredient$itemvalue) {
//                return Either.left(ingredient$itemvalue);
//            } else {
//                throw new UnsupportedOperationException("This is neither an item value nor a tag value.");
//            }
//        });
//        Codec<IngredientUtil.Value> CODEC = MAP_CODEC.codec();
//
//        Collection<ItemStack> getItems();
//    }
//
//}
