package top.shadowpixel.shadowjoin.object.listener;

import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import top.shadowpixel.shadowcore.util.entity.PlayerUtils;
import top.shadowpixel.shadowcore.util.entity.SenderUtils;
import top.shadowpixel.shadowcore.util.text.ReplaceUtils;
import top.shadowpixel.shadowjoin.ShadowJoin;
import top.shadowpixel.shadowjoin.data.DataManager;
import top.shadowpixel.shadowjoin.util.LocaleUtils;
import top.shadowpixel.shadowjoin.util.Logger;

public class DataListener implements Listener {
    private final ShadowJoin plugin;

    public DataListener(ShadowJoin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        //Check db connection
        var uuid = event.getUniqueId();
        var dataManager = DataManager.getInstance();
        if (dataManager.getStorageMethod().isDatabase() && !dataManager.getDatabase().isInitialized()) {
            var msg = LocaleUtils.getMessage("Message.Data.Player.No-db-connection");
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(msg);
            return;
        }

        var loaded = DataManager.getInstance().load(uuid, true);
        if (!loaded) {
            var msg = LocaleUtils.getMessage("Message.Data.Player.Failed-to-load");
            var adminMsg = LocaleUtils.getMessage("Message.Data.Admin.Failed-to-load",
                    "{player}", uuid.toString());
            Logger.error(adminMsg);
            PlayerUtils.getOnlineOperators()
                    .forEach(p -> p.sendMessage(adminMsg));

            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(msg);
        }
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        var msg = ReplaceUtils.coloredReplace(LocaleUtils.getMessage(player, "Message.Data.Player.Failed-to-load"), player);
        SenderUtils.sendMessage(player, msg,
                "{player}", player.getName());
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        var player = event.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            var unloaded = DataManager.getInstance().unload(player.getUniqueId());
            if (!unloaded) {
                var msg = LocaleUtils.getMessage(player, "Message.Data.Admin.Failed-to-unload",
                        "{player}", player.getName());
                Bukkit.getScheduler().runTask(plugin, () ->  {
                    Logger.error(msg);
                    PlayerUtils.getOnlineOperators()
                            .forEach(p -> p.sendMessage(msg));
                });
            }
        });
    }
}
