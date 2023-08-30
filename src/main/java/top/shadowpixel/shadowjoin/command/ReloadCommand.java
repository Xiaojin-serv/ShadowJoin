package top.shadowpixel.shadowjoin.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.shadowpixel.shadowjoin.ShadowJoin;
import top.shadowpixel.shadowjoin.bungee.BungeeManager;
import top.shadowpixel.shadowjoin.config.ConfigManager;
import top.shadowpixel.shadowjoin.data.DataManager;
import top.shadowpixel.shadowjoin.format.FormatManager;
import top.shadowpixel.shadowjoin.locale.LocaleManager;
import top.shadowpixel.shadowjoin.util.LocaleUtils;
import top.shadowpixel.shadowcore.api.command.subcommand.SubCommand;
import top.shadowpixel.shadowcore.util.collection.ArrayUtils;
import top.shadowpixel.shadowcore.util.collection.ListUtils;
import top.shadowpixel.shadowcore.util.entity.SenderUtils;
import top.shadowpixel.shadowcore.util.plugin.PluginUtils;

import java.util.List;

public class ReloadCommand extends SubCommand {

    private final ShadowJoin plugin = ShadowJoin.getInstance();

    public ReloadCommand() {
        super(ListUtils.immutableList("Reload", "Rl"), "ShadowJoin.Commands.Reload");
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, String[] arguments) {
        if (ArrayUtils.isNull(arguments)) {
            if (plugin.getConfiguration().getBoolean("Data.Save-on-reloading")) {
                DataManager.getInstance().saveAll();
            }

            PluginUtils.reloadManager(
                    ConfigManager.getInstance(),
                    LocaleManager.getInstance(),
                    FormatManager.getInstance(),
                    BungeeManager.getInstance(),
                    DataManager.getInstance());
        } else {
            switch (arguments[0].toLowerCase()) {
                case "config":
                    ConfigManager.getInstance().reload();
                    break;
                case "locale":
                    LocaleManager.getInstance().reload();
                    break;
                case "datum":
                case "data":
                    if (plugin.getConfiguration().getBoolean("Data.Save-on-reloading")) {
                        DataManager.getInstance().saveAll();
                    }

                    DataManager.getInstance().reload();
                    break;
                case "format":
                    FormatManager.getInstance().reload();
                    break;
                case "bungee":
                    BungeeManager.getInstance().reload();
                    break;
            }
        }

        SenderUtils.sendMessage(sender, LocaleUtils.getMessage(sender, "Message.Commands.Reloaded"));
    }

    @Override
    public @Nullable List<String> complete(@NotNull CommandSender sender, @NotNull String label, String[] arguments) {
        return ListUtils.asList("Config", "Locale", "Format");
    }
}
