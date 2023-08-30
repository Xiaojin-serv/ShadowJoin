package top.shadowpixel.shadowjoin.util;

import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import top.shadowpixel.shadowcore.util.entity.SenderUtils;
import top.shadowpixel.shadowcore.util.text.StringUtils;
import top.shadowpixel.shadowjoin.ShadowJoin;
import top.shadowpixel.shadowjoin.format.Format;
import top.shadowpixel.shadowjoin.format.FormatManager;

public class CommandUtils {

    public static final ShadowJoin plugin = ShadowJoin.getInstance();

    private CommandUtils() {
        throw new UnsupportedOperationException();
    }

    public static boolean checkArgument(CommandSender sender, String[] arguments, int len) {
        if (arguments.length < len) {
            SenderUtils.sendMessage(sender, LocaleUtils.getCmdMessage(sender, "Parameters-error"));
            return false;
        }

        return true;
    }

    @Nullable
    public static Player findPlayer(CommandSender sender, String name) {
        Player player;
        if ((player = Bukkit.getPlayer(name)) == null) {
            sendCommandMessage(sender, "Player-not-found", "{name}", name);
        }

        return player;
    }

    public static @Nullable Integer findInt(CommandSender sender, String integer, int index) {
        if (!StringUtils.isInteger(integer)) {
            sendCommandMessage(sender, "Not-an-integer", "{index}", String.valueOf(index));
            return null;
        }

        return Integer.valueOf(integer);
    }

    public static @Nullable Double findDouble(CommandSender sender, String d, int index) {
        try {
            return Double.parseDouble(d);
        } catch (Throwable throwable) {
            sendCommandMessage(sender, "Not-an-number", "{index}", String.valueOf(index));
            return null;
        }
    }

    public static @Nullable Format findFormat(CommandSender sender, String name) {
        var format = FormatManager.getInstance().getFormat(name);
        if (format == null) {
            sendCommandMessage(sender, "Format-not-found", "{name}", name);
        }

        return format;
    }

    public static void sendCommandMessage(CommandSender sender, String path, String... replacements) {
        SenderUtils.sendMessage(sender,
                LocaleUtils.getCmdMessage(sender, path, replacements));
    }
}
