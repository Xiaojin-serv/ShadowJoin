package top.shadowpixel.shadowjoin.data;

import lombok.ToString;
import lombok.var;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;
import top.shadowpixel.shadowjoin.format.FormatManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SerializableAs("ShadowJoin-PlayerData")
@ToString
public class PlayerData implements ConfigurationSerializable {

    private UUID owner;
    private String format;

    public PlayerData(UUID owner) {
        this.owner = owner;
    }

    public PlayerData(UUID owner, String format) {
        this.owner = owner;
        this.format = format;
    }

    public PlayerData(Map<String, Object> map) {
        var fmt = map.get("Format");
        this.format = fmt == null ? null : fmt.toString();
    }

    @NotNull
    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        if (this.owner == null) {
            this.owner = owner;
        }
    }

    @NotNull
    public String getFormat() {
        return format;
    }

    public void setFormat(@NotNull String format) {
        this.format = format;
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        var dataMap = new HashMap<String, Object>();
        dataMap.put("Format", format);
        return dataMap;
    }
}
