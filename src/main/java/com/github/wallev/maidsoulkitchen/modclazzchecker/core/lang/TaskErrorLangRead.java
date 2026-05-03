package com.github.wallev.maidsoulkitchen.modclazzchecker.core.lang;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.ITaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.manager.BaseClazzCheckManager;
import com.github.wallev.maidsoulkitchen.util.AnnotationHelper;
import org.objectweb.asm.Type;

import java.lang.reflect.Field;

public class TaskErrorLangRead {

    public static void init(BaseClazzCheckManager<?, ?> clazzCheckManager) {
        Type annotationType = clazzCheckManager.getErrorTaskLangAnnotationType();

        AnnotationHelper.read(annotationType, (data) -> {
            String className = data.clazz().getClassName();
            String fieldName = data.memberName();

            // 解析注解属性值
            String enUs = AnnotationHelper.getHolderValue(data, "en_us");
            String zhCn = AnnotationHelper.getHolderValue(data, "zh_cn");
            String enUsDesc = AnnotationHelper.getHolderValue(data, "en_us_desc");
            String zhCnDesc = AnnotationHelper.getHolderValue(data, "zh_cn_desc");

            TaskErrorLang taskErrorLang = new TaskErrorLang(
                    enUs == null ? "" : enUs,
                    zhCn == null ? "" : zhCn,
                    enUsDesc == null ? "" : enUsDesc,
                    zhCnDesc == null ? "" : zhCnDesc
            );

            // 通过反射获取注解绑定的字段对象
            Field annotatedField = getAnnotatedField(className, fieldName);
            try {
                // 对于static字段，直接传入null作为所有者
                Object fieldValue = annotatedField.get(null);

                if (fieldValue instanceof ITaskInfo<?> taskInfo) {
                    String uidStr = taskInfo.getUidStr();
                    clazzCheckManager.addErrorTaskLang(uidStr, taskErrorLang);
                }
            } catch (Exception e) {
                throw new RuntimeException("[TaskErrorLang]Exception: " + className + "#" + fieldName, e);
            }

        });
    }

    /**
     * 通过类名和字段名反射获取字段对象
     */
    private static Field getAnnotatedField(String className, String fieldName) {
        try {
            Class<?> clazz = Class.forName(className);
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("[TaskErrorLang]ClassNotFoundException: " + className, e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("[TaskErrorLang]NoSuchFieldException: " + className + "#" + fieldName, e);
        } catch (Exception e) {
            throw new RuntimeException("[TaskErrorLang]Exception: " + className + "#" + fieldName, e);
        }
    }

}
