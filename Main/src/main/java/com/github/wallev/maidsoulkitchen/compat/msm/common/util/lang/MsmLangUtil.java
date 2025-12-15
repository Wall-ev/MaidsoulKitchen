package com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.ICookingGuideGenerator;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.util.AnnotationHelper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.loading.LoadingModList;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import studio.fantasyit.maid_storage_manager.craft.CollectCraftEvent;
import studio.fantasyit.maid_storage_manager.craft.generator.config.ConfigTypes;

import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.wallev.maidsoulkitchen.util.AnnotationHelper.getAnnotatedField;

public class MsmLangUtil {
    private static final Marker MARKER = MarkerManager.getMarker("AutoGenLang");

    public interface LangProvider {
        Component provider();
    }

    public static Map<String, String> getLangValues(Path resource) throws IOException {
        String json = Files.readString(resource);
        JsonObject jsonData = JsonParser.parseString(json).getAsJsonObject();
        Map<String, String> langVals = jsonData.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().getAsString()
                ));
        return langVals;
    }

    public static Path getResource(String modId, String path) {
        Path resource = LoadingModList.get().getModFileById(modId)
                .getFile()
                .findResource(path);
        return resource;
    }

    private static CollectCraftEvent virtualEvent() {
        return new CollectCraftEvent(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new HashMap<>());
    }

    private static void genModRecipeCompatLang(LanguageProvider languageProvider, String local) throws IOException {
        AnnotationHelper.read(TypeLang.class, data -> {
            String lang = AnnotationHelper.getHolderValue(data, local);

            CollectCraftEvent virtualEvent = virtualEvent();

            String clazzName = data.clazz().getClassName();
            Class<?> asmClazz;
            try {
                asmClazz = Class.forName(clazzName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            ICookingGuideGenerator<?> eventConstructor = tryInstantiateWithEventConstructor(asmClazz, virtualEvent, languageProvider, lang);
            if (eventConstructor == null) {
                eventConstructor = tryInstantiateEmptyConstructor(asmClazz, virtualEvent, languageProvider, lang);
            }

            switch (data.targetType()) {
                case METHOD -> {
                    String key = eventConstructor.getTranslateKey();
                    MaidsoulKitchen.LOGGER.debug(MARKER, "auto gen method_lang: clazz: {}, key: {}",  eventConstructor, key);
                    languageProvider.add(key, lang);
                }
                case FIELD -> {
                    String memberName = data.memberName();
                    Field annotatedField = getAnnotatedField(asmClazz, memberName);
                    try {
                        Object o = annotatedField.get(eventConstructor);
                        if (o instanceof ConfigTypes.ConfigType<?> configType) {
                            String transKey = getTransKey(configType.getTranslatableName());
                            MaidsoulKitchen.LOGGER.debug(MARKER, "auto gen field_lang: clazz: {}, key: {}",  eventConstructor, transKey);
                            languageProvider.add(transKey, lang);
                        }

                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    @Nullable
    private static ICookingGuideGenerator<?> tryInstantiateWithEventConstructor(Class<?> clazz, CollectCraftEvent event, LanguageProvider languageProvider, String lang) {
        try {
            Constructor<?> constructor = clazz.getConstructor(CollectCraftEvent.class);
            ICookingGuideGenerator<?> instance = (ICookingGuideGenerator<?>) constructor.newInstance(event);

//            String type = instance.getTranslateKey();
//            languageProvider.add(type, lang);

//            MaidsoulKitchen.LOGGER.info(MARKER, "Successfully added auto_craft_guides: {}", clazz);
            return instance;
        } catch (NoSuchMethodException e) {
            return null;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            MaidsoulKitchen.LOGGER.error(MARKER, "Failed to add auto_craft_guides instance: {}", clazz, e);
            throw new RuntimeException(e);
        }
    }

    private static ICookingGuideGenerator<?> tryInstantiateEmptyConstructor(Class<?> clazz, CollectCraftEvent event, LanguageProvider languageProvider, String lang) {
        try {
            ICookingGuideGenerator<?> instance = (ICookingGuideGenerator<?>) clazz.getDeclaredConstructor().newInstance();
            event.addAutoCraftGuideGenerator(instance);

            return instance;
//            String type = instance.getTranslateKey();
//            languageProvider.add(type, lang);
//            MaidsoulKitchen.LOGGER.info(MARKER, "Successfully added auto_craft_guides: {}", clazz);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignored) {
            MaidsoulKitchen.LOGGER.error(MARKER, "Failed to add auto_craft_guides instance: {}", clazz, ignored);
            throw new RuntimeException(ignored);
        }
    }

    private static void genModCompatLang(LanguageProvider languageProvider, String local) {
        AnnotationHelper.read(ModLang.class, data -> {
            String value = AnnotationHelper.getEnumHolderValue(data, "value");
            String modId = Mods.by(value.toUpperCase(Locale.ENGLISH)).getModId();

            boolean custom = (boolean) Optional.ofNullable(AnnotationHelper.getHolderValue(data, "custom"))
                    .orElse(false);
            if (custom) {
                String key1 = String.format("config.maid_storage_manager.crafting.generating.%s_%s", MaidsoulKitchen.MOD_ID, modId);
                String val = (String) Optional.ofNullable(AnnotationHelper.getHolderValue(data, local))
                        .orElse(AnnotationHelper.getHolderValue(data, "en_us"));
                languageProvider.add(key1, val);
                return;
            }


            String className = data.clazz().getClassName();
            String fieldName = data.memberName();

            // 通过反射获取注解绑定的字段对象
            Field annotatedField = getAnnotatedField(className, fieldName);
            try {
                // 对于static字段，直接传入null作为所有者
                Object fieldValue = annotatedField.get(null);

                if (fieldValue instanceof LangProvider langSup) {

                    Component component = langSup.provider();
                    if (component instanceof MutableComponent mutableComponent
                            && mutableComponent.getContents() instanceof TranslatableContents translatableContents) {
                        Path resource = getResource(modId, String.format("assets/%s/lang/%s.json", modId, local));
                        if (resource == null) {
                            return;
                        }

                        Map<String, String> langValues = getLangValues(resource);

                        String key = translatableContents.getKey();
                        String val = langValues.get(key);
                        if (val != null) {
                            String key1 = String.format("config.maid_storage_manager.crafting.generating.%s_%s", MaidsoulKitchen.MOD_ID,modId);
                            languageProvider.add(key1, val);
                        }
                    }
                } else if (fieldValue instanceof MutableComponent mutableComponent) {

                }
            } catch (Exception e) {
                throw new RuntimeException("[AutoGenMatchNbtUseTag]Exception: " + className + "#" + fieldName, e);
            }
        });
    }

    public static String getTransKey(Component component) {
        if (component instanceof MutableComponent mutableComponent
                && mutableComponent.getContents() instanceof TranslatableContents translatableContents) {
            return translatableContents.getKey();
        } else {
            return "";
        }
    }

    public static void autonGenModLang(LanguageProvider languageProvider, String local) throws IOException {
        genModCompatLang(languageProvider, local);
        genModRecipeCompatLang(languageProvider, local);
    }

}
