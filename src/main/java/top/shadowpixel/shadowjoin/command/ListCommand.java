package top.shadowpixel.shadowjoin.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import top.shadowpixel.shadowcore.api.command.subcommand.SubCommand;
import top.shadowpixel.shadowjoin.format.Format;
import top.shadowpixel.shadowjoin.format.FormatManager;
import top.shadowpixel.shadowjoin.util.LocaleUtils;

import java.util.stream.Collectors;

public class ListCommand extends SubCommand {
    public ListCommand() {
        super("List", "ShadowJoin.Commands.List");
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] arguments) {
        send(LocaleUtils.getCmdMessage(sender, "List",
                "{list}", FormatManager.getInstance().getLoadedFormats().values().stream().map(Format::getName).collect(Collectors.joining(", "))));
    }
}
