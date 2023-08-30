package top.shadowpixel.shadowjoin.config;

import org.jetbrains.annotations.NotNull;
import top.shadowpixel.shadowjoin.ShadowJoin;
import top.shadowpixel.shadowcore.api.config.AbstractConfigManager;
import top.shadowpixel.shadowcore.util.collection.ListUtils;

import java.util.Collection;
import java.util.List;

public class ConfigManager extends AbstractConfigManager<ShadowJoin> {

    private final List<ConfigurationInfo> INFOS = ListUtils.immutableList(
            ConfigurationInfo.of(plugin, "Config", "Config.yml")
    );

    public ConfigManager(ShadowJoin plugin) {
        super(plugin);
    }

    @Override
    public Collection<ConfigurationInfo> getConfigs() {
        return INFOS;
    }

    @NotNull
    public static ConfigManager getInstance() {
        return ShadowJoin.getInstance().getConfigManager();
    }
}
