package top.shadowpixel.shadowjoin.object.hook;

import lombok.var;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.shadowpixel.shadowcore.util.collection.ArrayUtils;
import top.shadowpixel.shadowjoin.ShadowJoin;
import top.shadowpixel.shadowjoin.format.FormatManager;

public class PlaceholderHook extends PlaceholderExpansion {
    private final ShadowJoin plugin;

    public PlaceholderHook(ShadowJoin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "ShadowJoin";
    }

    @Override
    public @NotNull String getAuthor() {
        return "XiaoJin_awa_";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        var parameters = params.split("_");
        if (ArrayUtils.isNull(parameters)) {
            return null;
        }

        switch (parameters[0].toLowerCase()) {
            case "format":
                return FormatManager.getInstance().getFormatOf(player).getName();
            case "alias":
                return FormatManager.getInstance().getFormatOf(player).getAlias();
        }
        return null;
    }
}
