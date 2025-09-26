package com.github.wallev.maidsoulkitchen.modclazzchecker.core.manager;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.ITaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.clazz.MultiClassAnalysisResult;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class TaskLoadError {

    public static void reportError(Consumer<Component> consumer, BaseClazzCheckManager<?, ?> checkManager) {
        if (!checkManager.needReportErrorTasks()) {
            return;
        }

        List<String> errorList = checkManager.getErrorTasks().stream().filter(task -> {
            ITaskInfo<?> taskInfo = checkManager.taskInfoByUid(task);
            return taskInfo != null && taskInfo.canLoadWithoutCheckClazz();
        }).toList();
        if (errorList.isEmpty()) {
            return;
        }

        String modId = checkManager.getModId();
        String issueUrl = checkManager.getIssueUrl();
        String fileAbsPath = MultiClassAnalysisResult.getExportFileAbsPath(checkManager);

        MutableComponent parent = Component.translatable(String.format("message.%s.warning.title", modId)).withStyle(ChatFormatting.DARK_RED);
        parent.append(CommonComponents.NEW_LINE);
        parent.append(Component.translatable(String.format("message.%s.warning.compat_failed", modId)));
        parent.append(CommonComponents.NEW_LINE);
        MutableComponent issueUrlComponent = Component.translatable(String.format("message.%s.warning.clicked_to_report", modId))
                .withStyle(ChatFormatting.GOLD)
                .withStyle(ChatFormatting.UNDERLINE)
                .withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, issueUrl)))
                .withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(issueUrl))));
        parent.append(issueUrlComponent);
        parent.append(CommonComponents.NEW_LINE);
        MutableComponent fileUrlComponent = Component.translatable(String.format("message.%s.warning.clicked_to_export", modId), fileAbsPath)
                .withStyle(ChatFormatting.GOLD)
                .withStyle(ChatFormatting.UNDERLINE)
                .withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, MultiClassAnalysisResult.ROOT_FOLDER.toString())))
                .withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(fileAbsPath))));
        parent.append(fileUrlComponent);
        parent.append(CommonComponents.NEW_LINE);
        parent.append(Component.translatable(String.format("message.%s.warning.feedbacked", modId)).withStyle(ChatFormatting.GRAY));
        parent.append(CommonComponents.NEW_LINE);

        MutableComponent component1 = Component.translatable(String.format("message.%s.warning.failed_task", modId))
                .withStyle(ChatFormatting.DARK_RED);
        parent.append(component1);
        parent.append(CommonComponents.NEW_LINE);

        for (String errorTask : errorList) {
            ITaskInfo<?> task = checkManager.taskInfoByUid(errorTask);
            if (task == null || !task.canLoadWithoutCheckClazz()) {
                continue;
            }
            if (task.getUidStr().contains("msm") && !Mods.MAID_STORAGE_MANAGER.versionLoad()) {
                continue;
            }
            MutableComponent mutableComponent = Component.translatable(String.format("message.%s.warning.failed_task", modId))
                    .append(getName(errorTask, checkManager))
                    .append(Component.translatable(String.format("message.%s.warning.failed_modid", modId)))
                    .append(task.getBindMod().modId());
            parent.append(mutableComponent);
            parent.append(CommonComponents.NEW_LINE);
        }
        parent.append(Component.translatable("message.maidsoulkitchen.warning.end"));

        consumer.accept(parent);
        checkManager.markReported();
    }
    
    private static MutableComponent getName(String taskUid, BaseClazzCheckManager<?, ?> checkManager) {
        ResourceLocation resourceLocation = ResourceLocation.tryParse(taskUid);
        if (resourceLocation == null) {
            return Component.empty();
        }
        return getName(resourceLocation, checkManager);
    }

    private static MutableComponent getName(ResourceLocation taskUid, BaseClazzCheckManager<?, ?> checkManager) {
        LanguageManager languageManager = Minecraft.getInstance().getLanguageManager();
        Locale javaLocale = languageManager.getJavaLocale();
        ITaskInfo<?> taskInfo = checkManager.defaultTaskInf();
        ITaskInfo<?> taskInfoByUid = checkManager.taskInfoByUid(taskUid.toString());
        if (taskInfoByUid != null) {
            taskInfo = taskInfoByUid;
        } else {
            int a=  1;
        }

        String errorLang;
        if (javaLocale.equals(Locale.CHINA)) {
            errorLang = checkManager.getErrorTaskLang(taskInfo).zh_cn();
        }  else {
            errorLang = checkManager.getErrorTaskLang(taskInfo).en_us();
        }

        return Component.literal(errorLang);
    }
}
