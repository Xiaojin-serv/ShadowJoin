package top.shadowpixel.shadowjoin.command;

import lombok.var;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import top.shadowpixel.shadowcore.api.command.subcommand.PlayerCommand;
import top.shadowpixel.shadowjoin.api.ShadowJoinAPI;
import top.shadowpixel.shadowjoin.util.LocaleUtils;

public class FormatCommand extends PlayerCommand {
    public FormatCommand() {
        super("Format");
    }

    @Override
    public void execute(@NotNull Player player, @NotNull String label, @NotNull String[] arguments) {
        var format = ShadowJoinAPI.getFormatOf(player);
        var msg = LocaleUtils.getCmdMessage(player, "Format-info",
                "{format}", format.getAlias());
        send(msg);
    }
}
