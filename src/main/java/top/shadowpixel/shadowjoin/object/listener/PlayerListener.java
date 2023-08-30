package top.shadowpixel.shadowjoin.object.listener;

import lombok.var;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import top.shadowpixel.shadowcore.api.function.EventExecutor;
import top.shadowpixel.shadowjoin.ShadowJoin;
import top.shadowpixel.shadowjoin.format.FormatManager;

public class PlayerListener implements Listener {
    private final ShadowJoin plugin;

    public PlayerListener(ShadowJoin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        //Cancel message
        event.setJoinMessage(null);

        //Check whether first join
        if (!player.hasPlayedBefore() && plugin.getConfiguration().getBoolean("Format.First-join.Enable")) {
            EventExecutor.execute(plugin, player, FormatManager.getInstance().getFirstJoin());
            return;
        }

        var format = FormatManager.getInstance().getFormatOf(player);
        EventExecutor.execute(plugin, player, format.getJoinEvent());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        var player = event.getPlayer();
        var format = FormatManager.getInstance().getFormatOf(player);
        EventExecutor.execute(plugin, player, format.getQuitEvent());
        //Cancel message
        event.setQuitMessage(null);
    }
}
