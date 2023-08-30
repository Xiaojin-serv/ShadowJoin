package top.shadowpixel.shadowjoin.util;

import top.shadowpixel.shadowjoin.ShadowJoin;

/**
 * A simple logger wrapper of ShadowJoin
 */
public class Logger {

    private static final top.shadowpixel.shadowcore.util.logging.Logger logger = ShadowJoin.getInstance().logger;

    private Logger() {
        throw new UnsupportedOperationException();
    }

    public static void info(String... messages) {
        logger.info(messages);
    }

    public static void warn(String... messages) {
        logger.warn(messages);
    }

    public static void warn(String message, Throwable throwable) {
        logger.warn(message, throwable);
    }

    public static void warn(Throwable... throwables) {
        logger.warn(throwables);
    }

    public static void error(String... messages) {
        logger.error(messages);
    }

    public static void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    public static void error(Throwable... throwables) {
        logger.error(throwables);
    }
}
