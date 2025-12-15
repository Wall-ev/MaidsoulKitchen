package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.clazz;

public class LogEntry {
    private final LogLevel level;
    private final String message;
    private final Throwable throwable;

    public LogEntry(LogLevel level, String message) {
        this(level, message, null);
    }

    public LogEntry(LogLevel level, String message, Throwable throwable) {
        this.level = level;
        this.message = message;
        this.throwable = throwable;
    }

    public LogLevel getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    @Override
    public String toString() {
        return "[" + level + "] " + message + (throwable != null ? " - " + throwable.getMessage() : "");
    }
}
