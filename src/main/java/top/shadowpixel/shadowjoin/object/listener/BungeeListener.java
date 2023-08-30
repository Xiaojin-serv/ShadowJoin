package top.shadowpixel.shadowjoin.object.listener;

import com.google.common.io.ByteStreams;
import lombok.var;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;
import top.shadowpixel.shadowjoin.ShadowJoin;
import top.shadowpixel.shadowjoin.api.ShadowJoinAPI;
import top.shadowpixel.shadowjoin.bungee.BungeeManager;

@SuppressWarnings({"UnstableApiUsage", "FieldCanBeLocal"})
public class BungeeListener implements PluginMessageListener {

    private final ShadowJoin plugin;

    public BungeeListener() {
        this.plugin = ShadowJoin.getInstance();
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message) {
        if (!channel.equalsIgnoreCase(BungeeManager.CHANNEL)) {
            return;
        }

        var in = ByteStreams.newDataInput(message);
        var action = in.readUTF();
        var uid = in.readUTF();
        if (action.equalsIgnoreCase("setformat")) {
            var format = in.readUTF();
            ShadowJoinAPI.setFormat(uid, format);
        }
    }
}
