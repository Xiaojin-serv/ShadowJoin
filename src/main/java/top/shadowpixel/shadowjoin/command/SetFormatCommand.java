package top.shadowpixel.shadowjoin.command;

import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.shadowpixel.shadowcore.api.command.subcommand.SubCommand;
import top.shadowpixel.shadowcore.util.entity.PlayerUtils;
import top.shadowpixel.shadowjoin.data.DataHandler;
import top.shadowpixel.shadowjoin.format.FormatManager;
import top.shadowpixel.shadowjoin.util.LocaleUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static top.shadowpixel.shadowjoin.util.CommandUtils.*;

public class SetFormatCommand extends SubCommand {

    public SetFormatCommand() {
        super("SetFormat", "ShadowJoin.Commands.SetFormat");
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] arguments) {
        if (!checkArgument(sender, arguments, 2)) {
            return;
        }

//        var player = findPlayer(sender, arguments[0]);
        var player = Bukkit.getPlayer(arguments[0]);
        var format = findFormat(sender, arguments[1]);
        if (format == null) {
            return;
        }

        if (player == null) {
            DataHandler.setFormat(sender, arguments[0], arguments[1]);
            return;
        }

        FormatManager.getInstance().setFormat(player, format);
        send(LocaleUtils.getCmdMessage(sender, "Set-format",
                "{player}", player.getName(),
                "{format}", format.getName()));
    }

    @Override
    public @Nullable List<String> complete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] arguments) {
        switch (arguments.length) {
            case 1:
                return PlayerUtils.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            case 2:
                return new ArrayList<>(FormatManager.getInstance().getLoadedFormats().keySet());
        }

        return Collections.emptyList();
    }
}
