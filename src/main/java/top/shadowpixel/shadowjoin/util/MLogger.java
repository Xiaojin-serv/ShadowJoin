package top.shadowpixel.shadowjoin.util;

import org.jetbrains.annotations.Nullable;
import top.shadowpixel.shadowjoin.ShadowJoin;
import top.shadowpixel.shadowcore.util.text.ReplaceUtils;

public class MLogger {

    private static final ShadowJoin plugin = ShadowJoin.getInstance();

    private MLogger() {
        throw new UnsupportedOperationException();
    }

    public static void info(String path) {
        Logger.info(plugin.getDefaultMessage().getString(path));
    }

    public static void infoReplaced(String path, String... replacements) {
        Logger.info(ReplaceUtils.coloredReplace(get(path), replacements));
    }

    public static void warn(String path) {
        Logger.warn(plugin.getDefaultMessage().getString(path));
    }

    public static void warn(String path, Throwable throwable) {
        Logger.warn(get(path), throwable);
    }

    public static void error(String path) {
        Logger.error(plugin.getDefaultMessage().getString(path));
    }

    public static void error(String path, Throwable throwable) {
        Logger.error(get(path), throwable);
    }
    
    @Nullable
    public static String get(String path) {
        return plugin.getDefaultMessage().getString(path);
    }
}
