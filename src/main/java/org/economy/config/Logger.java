package org.economy.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    public enum LogType {
        INFO,WARNING,ERROR
    }

    public static void log(String message, LogType logType) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedMessage = String.format("{%s} {%s} %s", now.format(formatter), logType, message);
        System.out.println(formattedMessage);
    }

}
