package top.shadowpixel.shadowjoin.object.listener;

import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import top.shadowpixel.shadowjoin.ShadowJoin;
import top.shadowpixel.shadowjoin.data.DataManager;
import top.shadowpixel.shadowjoin.util.LocaleUtils;
import top.shadowpixel.shadowjoin.util.Logger;
import top.shadowpixel.shadowcore.util.entity.PlayerUtils;
import top.shadowpixel.shadowcore.util.entity.SenderUtils;
import top.shadowpixel.shadowcore.util.text.ReplaceUtils;

public class DataListener implements Listener {

    private final ShadowJoin plugin;

    public DataListener(ShadowJoin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PlayerLoginEvent event) {
        //Check db connection
        var player = event.getPlayer();
        var dm = DataManager.getInstance();
        if (dm.getStorageMethod().isDatabase() && !dm.getDatabase().isInitialized()) {
            var msg = ReplaceUtils.coloredReplace(LocaleUtils.getMessage(player, "Message.Data.Player.No-db-connection"), player);
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(msg);
            return;
        }

        //Check data's load
        var loaded = DataManager.getInstance().load(player.getUniqueId(), true);
        if (!loaded) {
            var msg = ReplaceUtils.coloredReplace(LocaleUtils.getMessage(player, "Message.Data.Player.Failed-to-Load"), player);
            if (plugin.getConfiguration().getString("Data.Action-on-fail", "kick").equalsIgnoreCase("kick")) {
                event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                event.setKickMessage(msg);
            } else {
                SenderUtils.sendMessage(player, msg, "{player}", player.getName());
            }

            var adminMsg = LocaleUtils.getMessage(player, "Message.Data.Admin.Failed-to-load",
                    "{player}", player.getName());
            Logger.error(adminMsg);
            PlayerUtils.getOnlineOperators()
                    .forEach(p -> p.sendMessage(adminMsg));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        var player = event.getPlayer();
        var unloaded = DataManager.getInstance().unload(player.getUniqueId());
        if (!unloaded) {
            var msg = LocaleUtils.getMessage(player, "Message.Data.Admin.Failed-to-unload",
                    "{player}", player.getName());
            Logger.error(msg);
            PlayerUtils.getOnlineOperators()
                    .forEach(p -> p.sendMessage(msg));
        }
    }
}
