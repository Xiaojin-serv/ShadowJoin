package top.shadowpixel.shadowjoin.command;

import lombok.var;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;
import top.shadowpixel.shadowcore.api.command.subcommand.ConsoleCommand;
import top.shadowpixel.shadowjoin.format.FormatManager;
import top.shadowpixel.shadowjoin.util.CommandUtils;
import top.shadowpixel.shadowjoin.util.LocaleUtils;

public class CreateFormatCommand extends ConsoleCommand {

    public CreateFormatCommand() {
        super("Create");
    }

    @Override
    public void execute(@NotNull ConsoleCommandSender sender, @NotNull String label, @NotNull String[] arguments) {
        if (!CommandUtils.checkArgument(sender, arguments, 3)) {
            return;
        }

        var priority = CommandUtils.findInt(sender, arguments[2], 3);
        if (priority == null) {
            return;
        }

        if (FormatManager.getInstance().create(arguments[0], arguments[1], priority)) {
            send(LocaleUtils.getMessage(sender, "Message.Format.Created"));
        } else {
            send(LocaleUtils.getMessage(sender, "Message.Format.Failed-to-create"));
        }
    }
}
