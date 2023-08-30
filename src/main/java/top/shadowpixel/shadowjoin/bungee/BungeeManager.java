package top.shadowpixel.shadowjoin.bungee;

import lombok.var;
import org.bukkit.Bukkit;
import top.shadowpixel.shadowjoin.ShadowJoin;
import top.shadowpixel.shadowjoin.object.listener.BungeeListener;
import top.shadowpixel.shadowcore.object.interfaces.Manager;

public class BungeeManager implements Manager {
    public static final String CHANNEL = "ShadowJoin:Sync".toLowerCase();

    private final ShadowJoin plugin;

    public BungeeManager(ShadowJoin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize() {
        if (!isBungeeMode()) {
            return;
        }

        if (!plugin.getServer().getPluginManager().isPluginEnabled("ShadowMessenger")) {
            plugin.logger.warn("BungeeMode is enabled, but plugin ShadowMessenger is missing!",
                    "Please follow the tutorial and install it");
            return;
        }

        var messenger = Bukkit.getMessenger();
        messenger.registerIncomingPluginChannel(this.plugin, CHANNEL, new BungeeListener());
        messenger.registerOutgoingPluginChannel(this.plugin, CHANNEL);

        plugin.logger.info("&aBungee initialized!");
    }

    @Override
    public void unload() {
        if (!isBungeeMode()) {
            return;
        }

        var messenger = Bukkit.getMessenger();
        messenger.unregisterIncomingPluginChannel(this.plugin, CHANNEL);
        messenger.unregisterOutgoingPluginChannel(this.plugin, CHANNEL);
    }

    public boolean isBungeeMode() {
        return this.plugin.getConfiguration().getBoolean("Data.Bungee-mode");
    }

    public static BungeeManager getInstance() {
        return ShadowJoin.getInstance().getBungeeManager();
    }
}
