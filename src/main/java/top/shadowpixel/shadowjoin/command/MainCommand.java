package top.shadowpixel.shadowjoin.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import top.shadowpixel.shadowjoin.ShadowJoin;
import top.shadowpixel.shadowjoin.util.LocaleUtils;
import top.shadowpixel.shadowcore.api.command.PluginCommand;
import top.shadowpixel.shadowcore.object.enums.CommandCondition;
import top.shadowpixel.shadowcore.util.entity.SenderUtils;

public class MainCommand extends PluginCommand {

    public MainCommand() {
        super("ShadowJoin");

        //Set events
        setEvent(CommandCondition.COMMAND_NOT_FOUND, sender ->
                sender.sendMessage(LocaleUtils.getCmdMessage(sender, "Absent")));
        setEvent(CommandCondition.ONLY_PLAYER, sender ->
                sender.sendMessage(LocaleUtils.getCmdMessage(sender, "Only-for-player")));
        setEvent(CommandCondition.ONLY_CONSOLE, sender ->
                sender.sendMessage(LocaleUtils.getCmdMessage(sender, "Only-for-console")));
        setEvent(CommandCondition.REQUIRED_PERMISSIONS, sender ->
                sender.sendMessage(LocaleUtils.getCmdMessage(sender, "No-permissions")));
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, String[] arguments) {
        //Show info messages
        SenderUtils.sendMessage(sender, LocaleUtils.getMessage(sender, "Message.Info", "{cmd}", label,
                "{version}", ShadowJoin.getVersion()));
    }
}
