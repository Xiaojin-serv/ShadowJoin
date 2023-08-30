package top.shadowpixel.shadowjoin.format;

import lombok.ToString;
import lombok.var;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.shadowpixel.shadowcore.api.config.ConfigurationProvider;
import top.shadowpixel.shadowcore.api.function.components.ExecutableEvent;
import top.shadowpixel.shadowcore.object.interfaces.Manager;
import top.shadowpixel.shadowcore.util.collection.ArrayUtils;
import top.shadowpixel.shadowcore.util.collection.MapUtils;
import top.shadowpixel.shadowcore.util.io.FileUtils;
import top.shadowpixel.shadowjoin.ShadowJoin;
import top.shadowpixel.shadowjoin.data.DataManager;
import top.shadowpixel.shadowjoin.util.Logger;
import top.shadowpixel.shadowjoin.util.MLogger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("ResultOfMethodCallIgnored")
@ToString
public class FormatManager implements Manager {
    private final ShadowJoin plugin;
    private final LinkedHashMap<String, Format> loadedFormats = new LinkedHashMap<>(0);
    private File formatFile;
    private Format defaultFormat;
    private ExecutableEvent firstJoin;

    public FormatManager(ShadowJoin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize() {
        this.formatFile = new File(plugin.getConfiguration().getString("Format.File")
                .replace("{default}", plugin.getDataFolder().toString()));
        this.formatFile.mkdirs();
        loadFormats();

        //Sort and set the default format
        MapUtils.sort(loadedFormats, Comparator.comparingInt(o -> o.getValue().getPriority()));
        var def = getFormat(plugin.getConfiguration().getString("Format.Default"));
        if (def == null) {
            Logger.error("The default format is absent, now using the format with the highest priority.");
            var tail = MapUtils.getTail(loadedFormats);
            if (tail == null) {
                defaultFormat = Format.EmptyFormatContainer.getEmptyFormat();
                Logger.error(
                        "Failed, now using empty format.",
                        "It seems that there are any formats,",
                        "please follow the tutorial and create any!"
                );
            } else {
                defaultFormat = tail.getValue();
            }
        } else {
            defaultFormat = def;
        }

        //Load first join
        var config = plugin.getConfiguration().getConfigurationSection("Format.First-join");
        assert config != null;
        if (config.getBoolean("Enable")) {
            firstJoin = ExecutableEvent.of(config.getStringList("Events"));
        } else {
            firstJoin = ExecutableEvent.emptyEvent();
        }
    }

    @Override
    public void unload() {
        this.loadedFormats.clear();
    }

    /**
     * Load a format from a file.
     *
     * @param file Format file
     * @return Whether loaded successfully
     */
    public boolean loadFormat(File file) {
        try {
            var config = ConfigurationProvider.getYamlConfigurationProvider().load(file);
            var format = config.get("Format");
            if (format instanceof Format) {
                this.loadedFormats.put(((Format) format).getName(), (Format) format);
            } else {
                MLogger.error("Format " + format + " is not an valid format");
                return false;
            }
        } catch (IOException e) {
            MLogger.error("Failed to load format " + file, e);
            return false;
        }

        return true;
    }

    /**
     * Load a format from the default path.
     *
     * @param name Name of format
     * @return Whether loaded successfully
     */
    public boolean loadFormat(String name) {
        return loadFormat(new File(this.formatFile, name + ".yml"))
                || loadFormat(new File(this.formatFile, name + ".yaml"));
    }

    /**
     * Load formats from the default path.
     */
    public void loadFormats() {
        loadFormats(this.formatFile);
    }

    /**
     * Load formats in this folder.
     *
     * @param file File consists of formats
     */
    public void loadFormats(File file) {
        var files = file.listFiles();
        if (ArrayUtils.isNull(files)) {
            return;
        }

        Arrays.stream(files)
                .filter(f -> f.getName().endsWith(".yml") || f.getName().endsWith(".yaml"))
                .forEach(this::loadFormat);
    }

    /**
     * Change the player's format.
     * If format is null, it will be changed to default.
     *
     * @param player Player
     * @param format Format
     */
    public void setFormat(@NotNull Player player, @Nullable Format format) {
        if (format == null) {
            setFormat(player, defaultFormat);
            return;
        }

        var data = DataManager.getInstance().getPlayerData(player);
        data.setFormat(format.getName());
        DataManager.getInstance().save(player);
    }

    /**
     * Change player's format.
     *
     * @param player Player
     * @param format Format
     */
    public void setFormat(@NotNull Player player, @NotNull String format) {
        setFormat(player, getFormat(format));
    }

    /**
     * Create a format but do not load it.
     *
     * @param name Name of the new format
     * @param alias Alias of the new format
     * @param priority Priority of the new format
     * @return Whether created successfully
     */
    @SuppressWarnings("DataFlowIssue")
    public boolean create(@NotNull String name, @NotNull String alias, int priority) {
        if (loadedFormats.containsKey(name)) {
            return false;
        }

        try {
            var lines = FileUtils.readAllLines(plugin.getResource("default.yml"), Charset.defaultCharset(),
                    "$name", name,
                    "$alias", alias,
                    "$priority", String.valueOf(priority));

            var file = new File(this.formatFile, name + ".yml");

            //Create files
            file.createNewFile();

            //Write files
            Files.write(file.toPath(), lines, Charset.defaultCharset());
            return true;
        } catch (IOException e) {
            MLogger.error("Failed to create a format", e);
            return false;
        }
    }

    @Nullable
    public Format getFormat(String name) {
        return MapUtils.smartMatch(name, this.loadedFormats);
    }

    @NotNull
    public Format getFormatOf(@NotNull Player player) {
        var data = DataManager.getInstance().getPlayerData(player);
        var tf = getFormat(data.getFormat());
        var format = tf == null ? defaultFormat : tf;
        var pri = format.getPriority();
        for (PermissionAttachmentInfo p : player.getEffectivePermissions()) {
            if (!p.getPermission().toLowerCase().startsWith("shadowjoin.format.")) {
                continue;
            }

            String f = p.getPermission().substring(18);
            var fmt = getFormat(f);
            if (fmt == null || fmt.getPriority() > format.getPriority()) {
                continue;
            }

            if (pri > fmt.getPriority()) {
                pri = fmt.getPriority();
                format = fmt;
            }
        }

        return format;
    }

    /**
     * Get the default format.
     *
     * @return The default format
     */
    @NotNull
    public Format getDefaultFormat() {
        return this.defaultFormat;
    }

    /**
     * Get the first join event.
     *
     * @return The first join event
     */
    @NotNull
    public ExecutableEvent getFirstJoin() {
        return this.firstJoin;
    }

    /**
     * @return Unmodifiable map of loaded formats.
     */
    public Map<String, Format> getLoadedFormats() {
        return Collections.unmodifiableMap(this.loadedFormats);
    }

    public static FormatManager getInstance() {
        return ShadowJoin.getInstance().getFormatManager();
    }
}
