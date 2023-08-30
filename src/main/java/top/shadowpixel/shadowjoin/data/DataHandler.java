package top.shadowpixel.shadowjoin.data;

import lombok.var;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.shadowpixel.shadowcore.api.uid.UUIDStorage;
import top.shadowpixel.shadowcore.util.entity.SenderUtils;
import top.shadowpixel.shadowjoin.ShadowJoin;
import top.shadowpixel.shadowjoin.util.LocaleUtils;
import top.shadowpixel.shadowmessenger.ShadowMessenger;

import java.util.function.Consumer;

public class DataHandler {

    private static ShadowJoin plugin;

    public static void initialize(ShadowJoin pl) {
        plugin = pl;
    }

    public static void setFormat(@NotNull String playerName, @NotNull String format) {
        setFormat(null, playerName, format);
    }

    public static void setFormat(@Nullable CommandSender sender, @NotNull String playerName, @NotNull String format) {
        if (isBungeeMode()) {
            ShadowMessenger.query("ShadowJoin SetFormat " + playerName + " " + format, result -> {
                showMessage(sender, "through-bungee");
                if (result.getReturnValue().equalsIgnoreCase("OK")) {
                    SenderUtils.sendMessage(sender, LocaleUtils.getCmdMessage(sender, "Set-format",
                            "{format}", format,
                            "{player}", playerName));
                } else {
                    OfflineHandler.setFormatOffline(sender, playerName, format);
                }
            });
            return;
        }

        OfflineHandler.setFormatOffline(sender, playerName, format);
    }

    public static boolean isBungeeMode() {
        return plugin.getConfiguration().getBoolean("Data.Bungee-mode") && plugin.getServer().getPluginManager().isPluginEnabled("ShadowMessenger");
    }

    private static void showMessage(@Nullable CommandSender sender, @NotNull String type, @Nullable String... replacement) {
        if (sender != null) {
            switch (type.toLowerCase()) {
                case "through-bungee":
                    SenderUtils.sendMessage(sender, LocaleUtils.getMessage(sender, "Message.Data.Through-bungee"));
                    break;
                case "player-not-found":
                    SenderUtils.sendMessage(sender, LocaleUtils.getCmdMessage(sender, "Player-not-found"), replacement);
                    break;
                case "offline":
                    SenderUtils.sendMessage(sender, LocaleUtils.getMessage(sender, "Message.Data.Offline"));
                    break;
            }
        }
    }

    private static class OfflineHandler {

        private static void setFormatOffline(@Nullable CommandSender sender, @NotNull String playerName, @NotNull String format) {
            var mod = handleOffline(sender, playerName, c -> c.setFormat(format));
            if (sender != null && mod) {
                SenderUtils.sendMessage(sender, LocaleUtils.getCmdMessage(sender, "Set-format",
                        "{player}", playerName,
                        "{format}", format));
                showMessage(sender, "offline");
            }
        }

        private static boolean handleOffline(@Nullable CommandSender sender, @NotNull String playerName, @NotNull Consumer<PlayerData> consumer) {
            var uuid = UUIDStorage.getUUID(playerName);
            if (uuid == null) {
                showMessage(sender, "player-not-found", "{name}", playerName);
                return false;
            }

            var dataManager = plugin.getDataManager();
            var data = dataManager.dataModifier.load(uuid);
            if (data == null) {
                showMessage(sender, "player-not-found", "{name}", playerName);
                return false;
            }

            consumer.accept(data);
            dataManager.dataModifier.save(data);
            return true;
        }
    }
}
