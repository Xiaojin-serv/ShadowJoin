package top.shadowpixel.shadowjoin.util;

import org.bukkit.command.CommandSender;
import top.shadowpixel.shadowjoin.ShadowJoin;
import top.shadowpixel.shadowcore.api.config.Configuration;
import top.shadowpixel.shadowcore.api.locale.Locale;
import top.shadowpixel.shadowcore.util.text.ReplaceUtils;
import top.shadowpixel.shadowjoin.object.hook.LangSwitcherHook;

public class LocaleUtils {

    private static final ShadowJoin plugin = ShadowJoin.getInstance();

    private LocaleUtils() {
        throw new UnsupportedOperationException();
    }

    public static Locale getLocale(CommandSender sender) {
        return LangSwitcherHook.getLocale(sender);
    }

    public static Configuration getMessage(CommandSender sender) {
        return getLocale(sender).getConfig("Message");
    }

    public static String getMessage(String path, String... replacements) {
        return ReplaceUtils.coloredReplace(plugin.getDefaultMessage().getString(path), replacements);
    }

    public static String getMessage(CommandSender sender, String path, String... replacements) {
        return ReplaceUtils.coloredReplace(getMessage(sender).getString(path).replace("{prefix}", ShadowJoin.getPrefix()), sender, replacements);
    }

    public static String getCmdMessage(CommandSender sender, String path, String... replacements) {
        return getMessage(sender, "Message.Commands." + path, replacements);
    }
}
