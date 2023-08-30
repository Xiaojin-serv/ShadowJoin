package top.shadowpixel.shadowjoin.api;

import lombok.NonNull;
import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.shadowpixel.shadowjoin.ShadowJoin;
import top.shadowpixel.shadowjoin.data.DataHandler;
import top.shadowpixel.shadowjoin.format.Format;
import top.shadowpixel.shadowjoin.format.FormatManager;

import java.util.UUID;

public class ShadowJoinAPI {
    private static boolean initialized;
    private static ShadowJoin plugin;

    public static void initialize(ShadowJoin plugin) {
        if (initialized) return;
        ShadowJoinAPI.plugin = plugin;
    }

    @Nullable
    public static Format getFormat(String name) {
        return plugin.getFormatManager().getFormat(name);
    }

    @NonNull
    public static Format getFormatOf(Player player) {
        return plugin.getFormatManager().getFormatOf(player);
    }

    @NotNull
    public static Format getDefaultFormat() {
        return plugin.getFormatManager().getDefaultFormat();
    }

    public static void setFormat(Player player, String format) {
        var fmt = getFormat(format);
        if (fmt == null) {
            return;
        }

        FormatManager.getInstance().setFormat(player, fmt);
    }

    public static void setFormat(String playerName, String format) {
        if (getFormat(format) == null) {
            return;
        }

        var player = Bukkit.getPlayer(playerName);
        if (player == null) {
            DataHandler.setFormat(playerName, format);
            return;
        }

        setFormat(player, format);
    }
}
