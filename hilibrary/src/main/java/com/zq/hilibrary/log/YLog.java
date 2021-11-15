package com.zq.hilibrary.log;


import androidx.annotation.NonNull;

import java.util.List;


/**
 * Tips:
 * 1. 打印堆栈信息
 * 2. File输出
 * 3.
 */
public class YLog {
    public static void v(Object... contents) {
        log(LogType.V, contents);
    }

    public static void vt(String tag, Object... contents) {
        log(LogType.V, tag, contents);
    }

    public static void d(Object... contents) {
        log(LogType.D, contents);
    }

    public static void dt(String tag, Object... contents) {
        log(LogType.D, tag, contents);
    }

    public static void i(Object... contents) {
        log(LogType.I, contents);
    }

    public static void it(String tag, Object... contents) {
        log(LogType.I, tag, contents);
    }

    public static void w(Object... contents) {
        log(LogType.W, contents);
    }

    public static void wt(String tag, Object... contents) {
        log(LogType.W, tag, contents);
    }

    public static void e(Object... contents) {
        log(LogType.E, contents);
    }

    public static void et(String tag, Object... contents) {
        log(LogType.E, tag, contents);
    }

    public static void a(Object... contents) {
        log(LogType.A, contents);
    }

    public static void at(String tag, Object... contents) {
        log(LogType.A, tag, contents);
    }

    public static void log(@LogType.TYPE int type, Object... contents) {
        log(type, LogManager.getConfig().getGlobalTag(), contents);
    }

    public static void log(@LogType.TYPE int type, @NonNull String tag, Object... contents) {
        log(LogManager.getConfig(), type, tag, contents);
    }

    public static void log(@NonNull LogConfig config, @LogType.TYPE int type, @NonNull String tag, Object... contents) {
        if (!config.enable()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (config.includeTread()) {
            String threadInfo = LogConfig.Companion.getTHREAD_FORMATTER().format(Thread.currentThread());
            sb.append(threadInfo).append("\n");
        }
        if (config.stackTraceDepth() > 0) {
            String stackTrace = LogConfig.Companion.getSTACK_TRACE_FORMATTER().format(
                    HiStackTraceUtil.getCroppedRealStackTrack(
                            new Throwable().getStackTrace(), "com.zq.hilibrary.log", config.stackTraceDepth()
                    ));
            sb.append(stackTrace).append("\n");
        }
        String body = parseBody(contents, config);
        if (body != null) {//替换转义字符\
            body = body.replace("\\\"", "\"");
        }
        sb.append(body);
        List<LogPrinter> logPrinters = !config.printers().isEmpty() ? config.printers() : LogManager.getPrinter();
        for (LogPrinter logPrinter : logPrinters) {
            logPrinter.print(config, type, tag, sb.toString());
        }
    }


    private static String parseBody(@NonNull Object[] contents, @NonNull LogConfig config) {
        if (config.injectJsonParser() != null) {
            return config.injectJsonParser().toJson(contents);
        }
        StringBuilder sb = new StringBuilder();
        for (Object o : contents) {
            sb.append(o.toString()).append(";");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
