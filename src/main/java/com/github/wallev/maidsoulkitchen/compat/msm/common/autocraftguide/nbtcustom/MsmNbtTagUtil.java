package com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.nbtcustom;

import com.github.wallev.maidsoulkitchen.datagen.ModItemTags;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.AnnotationHelper;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import studio.fantasyit.maid_storage_manager.util.ItemStackUtil;

import java.lang.reflect.Field;
import java.util.Locale;

import static com.github.wallev.maidsoulkitchen.util.AnnotationHelper.getAnnotatedField;

public class MsmNbtTagUtil {


    public static void autonGenMatchNbtItem(ModItemTags modItemTags) {
        IntrinsicHolderTagsProvider.IntrinsicTagAppender<Item> tag = modItemTags.tag(ItemStackUtil.matchNbt);

        AnnotationHelper.read(NbtItemTagGen.class, data -> {
            String value = AnnotationHelper.getEnumHolderValue(data, "value");
            if (!TaskInfo.by(value.toUpperCase(Locale.ENGLISH)).canLoad())
                return;

            String className = data.clazz().getClassName();
            String fieldName = data.memberName();

            // 通过反射获取注解绑定的字段对象
            Field annotatedField = getAnnotatedField(className, fieldName);
            try {
                // 对于static字段，直接传入null作为所有者
                Object fieldValue = annotatedField.get(null);

                if (fieldValue instanceof Item item) {
                    ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
                    if (key == null)
                        return;
                    tag.addOptional(key);
                }
            } catch (Exception e) {
                throw new RuntimeException("[AutoGenMatchNbtUseTag]Exception: " + className + "#" + fieldName, e);
            }
        });

    }

}
