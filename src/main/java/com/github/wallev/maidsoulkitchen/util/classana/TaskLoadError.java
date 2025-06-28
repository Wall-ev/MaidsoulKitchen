package com.github.wallev.maidsoulkitchen.util.classana;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.MultiClassAnalysisResult;
import com.github.wallev.verhelper.client.chat.VComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class TaskLoadError {
    private static final Set<ResourceLocation> ERROR_TASKS = new HashSet<>();

    public static void putError(ResourceLocation taskUid) {
        ERROR_TASKS.add(taskUid);
    }

    public static void reportError(Consumer<Component> consumer) {
        if (ERROR_TASKS.isEmpty()) {
            return;
        }

        MutableComponent parent = VComponent.translatable("message.maidsoulkitchen.warning.title").withStyle(ChatFormatting.DARK_RED);
        parent.append(VComponent.NEW_LINE);
        parent.append(VComponent.translatable("message.maidsoulkitchen.warning.compat_failed"));
        parent.append(VComponent.NEW_LINE);
        MutableComponent issueUrlComponent = VComponent.translatable("message.maidsoulkitchen.warning.clicked_to_report")
                .withStyle(ChatFormatting.GOLD)
                .withStyle(ChatFormatting.UNDERLINE)
                .withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, MaidsoulKitchen.ISSUE_URL)))
                .withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, VComponent.literal(MaidsoulKitchen.ISSUE_URL))));
        parent.append(issueUrlComponent);
        parent.append(VComponent.NEW_LINE);
        MutableComponent fileUrlComponent = VComponent.translatable("message.maidsoulkitchen.warning.clicked_to_open_file")
                .withStyle(ChatFormatting.GOLD)
                .withStyle(ChatFormatting.UNDERLINE)
                .withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, MultiClassAnalysisResult.ROOT_FOLDER.toString())))
                .withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, VComponent.literal(MultiClassAnalysisResult.LOG_FILE_PATH.toString()))));
        parent.append(fileUrlComponent);
        parent.append(VComponent.NEW_LINE);
        MutableComponent component1 = VComponent.translatable("message.maidsoulkitchen.warning.feedbacked")
                .withStyle(ChatFormatting.GRAY);
        parent.append(component1);
        parent.append(VComponent.NEW_LINE);
        for (ResourceLocation errorTask : ERROR_TASKS) {
            TaskInfo task = TaskInfo.by(errorTask);
            assert task != null;
            MutableComponent mutableComponent = VComponent.translatable("message.maidsoulkitchen.warning.failed_task")
                    .append(getName(errorTask))
                    .append(VComponent.translatable("message.maidsoulkitchen.warning.failed_modid"))
                    .append(task.getBindMod().modId);
            parent.append(mutableComponent);
            parent.append(VComponent.NEW_LINE);
        }
        parent.append(VComponent.translatable("message.maidsoulkitchen.warning.end"));
        consumer.accept(parent);

        ERROR_TASKS.clear();
    }

    private static MutableComponent getName(ResourceLocation taskUid) {
        String key = String.format("task.%s.%s", taskUid.getNamespace(), taskUid.getPath());
        return Component.translatable(key);
    }
}
