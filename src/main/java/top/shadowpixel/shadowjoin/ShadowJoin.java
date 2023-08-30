package top.shadowpixel.shadowjoin;

import lombok.Getter;
import lombok.var;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.shadowpixel.shadowcore.api.util.Metrics;
import top.shadowpixel.shadowjoin.api.ShadowJoinAPI;
import top.shadowpixel.shadowjoin.bungee.BungeeManager;
import top.shadowpixel.shadowjoin.command.*;
import top.shadowpixel.shadowjoin.config.ConfigManager;
import top.shadowpixel.shadowjoin.data.DataManager;
import top.shadowpixel.shadowjoin.data.PlayerData;
import top.shadowpixel.shadowjoin.format.Format;
import top.shadowpixel.shadowjoin.format.FormatManager;
import top.shadowpixel.shadowjoin.locale.LocaleManager;
import top.shadowpixel.shadowjoin.object.hook.PlaceholderHook;
import top.shadowpixel.shadowjoin.object.listener.DataListener;
import top.shadowpixel.shadowjoin.object.listener.PlayerListener;
import top.shadowpixel.shadowjoin.util.Logger;
import top.shadowpixel.shadowjoin.util.MLogger;
import top.shadowpixel.shadowcore.api.config.Configuration;
import top.shadowpixel.shadowcore.api.locale.Locale;
import top.shadowpixel.shadowcore.api.plugin.AbstractPlugin;
import top.shadowpixel.shadowcore.api.util.time.MSTimer;
import top.shadowpixel.shadowcore.object.interfaces.Manager;
import top.shadowpixel.shadowcore.util.plugin.DescriptionChecker;
import top.shadowpixel.shadowcore.util.plugin.PluginUtils;

import java.io.File;
import java.util.Objects;

public final class ShadowJoin extends AbstractPlugin {
    public static final Manager.Property
            CONFIG_MANAGER = Manager.Property.of(ConfigManager.class, "configManager"),
            LOCALE_MANAGER = Manager.Property.of(LocaleManager.class, "localeManager"),
            DATA_MANAGER = Manager.Property.of(DataManager.class, "dataManager"),
            BUNGEE_MANAGER = Manager.Property.of(BungeeManager.class, "bungeeManager"),
            FORMAT_MANAGER = Manager.Property.of(FormatManager.class, "formatManager");

    @Getter
    private static ShadowJoin instance;
    @Getter
    private ConfigManager configManager;
    @Getter
    private LocaleManager localeManager;
    @Getter
    private DataManager dataManager;
    @Getter
    private BungeeManager bungeeManager;
    @Getter
    private FormatManager formatManager;

    private boolean isEnabled = false;

    @Override
    public void enable() {
        var timer = new MSTimer();
        instance = this;

        //Config manager initialization
        PluginUtils.initManager(this, CONFIG_MANAGER);
        logger.addReplacement("{prefix}", getPrefix());

        //Locale manager initialization
        var localeFile = new File(getConfiguration().getString("Locale.File")
                .replace("{default}", getDataFolder().toString()));
        PluginUtils.initManager(this, LOCALE_MANAGER, this, localeFile);
        if (getDefaultLocale() == LocaleManager.getInternal()) {
            this.logger.warn("Internal locale is in use!");
        }

        var lang = getDefaultMessage();
        this.logger.info(
                "",
                "",
                "&7&lShadow&a&lJoin &7>> &a" + lang.getString("Message.Welcome") + "!",
                "",
                "&f" + lang.getString("Message.Version") + ": &av" + getVersion(),
                "&f" + lang.getString("Message.Author") + ": &aXiaoJin_awa_",
                "",
                ""
        );

        //Check plugin.yml
        if (!new DescriptionChecker(
                this,
                "ShadowJoin",
                "XiaoJin_awa_",
                "1.2").check()) {
            MLogger.error("Message.OnEnable.Error-Plugin_yml");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        //Commands initialization
        try {
            MLogger.info("Message.OnEnable.Register-command");
            initCommands();
            MLogger.info("Message.OnEnable.Succeed");
        } catch (Exception e) {
            MLogger.info("Message.OnEnable.Failed");
            Logger.error(e);
        }

        //Listeners registration
        try {
            MLogger.info("Message.OnEnable.Register-listener");
            registerListener("Data", new DataListener(this));
            registerListener("Player", new PlayerListener(this));
            MLogger.info("Message.OnEnable.Succeed");
        } catch (Exception e) {
            MLogger.error("Message.OnEnable.Failed", e);
        }

        registerSerializations(Format.class, PlayerData.class);

        //Data manager initialization
        PluginUtils.initManager(this, DATA_MANAGER);

        //Format manager initialization
        PluginUtils.initManager(this, FORMAT_MANAGER);

        //Bungee manager initialization
        PluginUtils.initManager(this, BUNGEE_MANAGER);

        //PAPI Extension registration
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            try {
                MLogger.info("Message.OnEnable.Register-listener");
                new PlaceholderHook(this).register();
                MLogger.info("Message.OnEnable.Succeed");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        //Show event-loaded message
        var cnt = FormatManager.getInstance().getLoadedFormats().size();
        if (cnt > 0) {
            MLogger.infoReplaced("Message.Format.Total-loads", "{amount}", String.valueOf(cnt));
        } else {
            MLogger.info("Message.Format.No-loads");
        }

        //Final step
        Logger.info("");
        MLogger.infoReplaced("Message.OnEnable.Enabled",
                "{time}", String.valueOf(timer.getTimePassed()));
        ShadowJoinAPI.initialize(this);
        new Metrics(this, 12200);
        isEnabled = true;
    }

    @Override
    public void disable() {
        if (!isEnabled) return;

        PluginUtils.unloadManager(
                this.localeManager,
                this.configManager
        );
        MLogger.infoReplaced("Message.OnDisable.Disabled");
    }

    @NotNull
    @Deprecated
    public FileConfiguration getConfig() {
        return super.getConfig();
    }

    public Configuration getConfiguration() {
        return getConfiguration("Config");
    }

    @Nullable
    public Configuration getConfiguration(String name) {
        return this.configManager.getConfiguration(name);
    }
    
    public Locale getDefaultLocale() {
        return this.localeManager == null || this.localeManager.getDefaultLocale() == null ?
                LocaleManager.getInternal() : this.localeManager.getDefaultLocale();
    }

    public @NotNull Configuration getDefaultMessage() {
        return Objects.requireNonNull(getDefaultLocale().getConfig("Message"));
    }

    public void initCommands() {
        //Register commands
        registerCommandHandler();
        var command = this.commandHandler.registerCommand("ShadowJoin", new MainCommand());

        //Add subcommands
        assert command != null;
        command.addCommand(
                new AdminCommand(),
                new CreateFormatCommand(),
                new FormatCommand(),
                new HelpCommand(),
                new ReloadCommand(),
                new SetFormatCommand(),
                new ListCommand()
        );
    }

    public static String getPrefix() {
        return getInstance().getConfiguration().getString("Prefix");
    }

    public static String getVersion() {
        return getInstance().getDescription().getVersion();
    }
}
