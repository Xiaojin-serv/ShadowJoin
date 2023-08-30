package top.shadowpixel.shadowjoin.format;

import lombok.Data;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;
import top.shadowpixel.shadowcore.api.function.components.ExecutableEvent;
import top.shadowpixel.shadowcore.util.collection.MapUtils;
import top.shadowpixel.shadowcore.util.text.ColorUtils;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
@Data
@SerializableAs("ShadowJoin-Format")
public class Format implements ConfigurationSerializable {
    private int priority;
    private String name, alias;
    private ExecutableEvent joinEvent = ExecutableEvent.emptyEvent(),
                            quitEvent = ExecutableEvent.emptyEvent();

    public Format(String name, String alias, int priority, ExecutableEvent joinEvent) {
        this(name, alias, priority, joinEvent, ExecutableEvent.emptyEvent());
    }

    public Format(String name, String alias, int priority, ExecutableEvent joinEvent, ExecutableEvent quitEvent) {
        this.name = name;
        this.alias = alias;
        this.priority = priority;
        this.joinEvent = joinEvent;
        this.quitEvent = quitEvent;
    }

    public Format(Map<String, Object> map) {
        this.name = map.get("Name").toString();
        this.priority = (int) map.get("Priority");
        this.alias = ColorUtils.colorize(map.getOrDefault("Alias", this.name).toString());

        //Events initialization
        if (map.containsKey("Join-events")) {
            this.joinEvent = ExecutableEvent.of((List<String>) map.get("Join-events"));
        }

        if (map.containsKey("Quit-events")) {
            this.quitEvent = ExecutableEvent.of((List<String>) map.get("Quit-events"));
        }
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        return MapUtils.of(
                "Name", this.name,
                "Alias", this.alias,
                "Priority", this.priority,
                "Join-events", this.joinEvent.getEventContent(),
                "Quit-events", this.quitEvent.getEventContent()
        );
    }

    public static class EmptyFormatContainer {
        private static Format format;

        public static Format getEmptyFormat() {
            if (format == null) {
                format = new Format("empty", "empty", Integer.MAX_VALUE, ExecutableEvent.emptyEvent());
            }

            return format;
        }
    }
}
