package top.shadowpixel.shadowjoin.locale;

import lombok.var;
import org.jetbrains.annotations.NotNull;
import top.shadowpixel.shadowjoin.ShadowJoin;
import top.shadowpixel.shadowjoin.util.Logger;
import top.shadowpixel.shadowcore.api.config.ConfigurationProvider;
import top.shadowpixel.shadowcore.api.locale.AbstractLocaleManager;
import top.shadowpixel.shadowcore.api.locale.Locale;
import top.shadowpixel.shadowcore.util.collection.ListUtils;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class LocaleManager extends AbstractLocaleManager<ShadowJoin> {

    public static final List<Locale.PresetLocaleInfo> PRESET_LOCALE_INFOS = ListUtils.immutableList(
            Locale.PresetLocaleInfo.of("zh_CN", "Locale/zh_CN")
    );

    public static final List<String> CONTENTS = ListUtils.immutableList(
            "Message.yml"
    );

    public LocaleManager(ShadowJoin plugin, File directory) {
        super(plugin, directory);
    }

    @Override
    public void initialize() {
        super.initialize();
        setDefaultLocale(plugin.getConfiguration().getString("Locale.Default-locale"));
    }

    @Override
    public @NotNull Collection<Locale.PresetLocaleInfo> getPresetLocales() {
        return PRESET_LOCALE_INFOS;
    }

    @Override
    public @NotNull Collection<String> getContents() {
        return CONTENTS;
    }

    @NotNull
    public static LocaleManager getInstance() {
        return ShadowJoin.getInstance().getLocaleManager();
    }
    private static Locale internal;

    @NotNull
    public static Locale getInternal() {
        if (internal == null) {
            var plugin = ShadowJoin.getInstance();
            var locale = new Locale(plugin, null, CONTENTS);
            locale.setName("Internal");

            try {
                //noinspection DataFlowIssue
                locale.addConfig("Message", ConfigurationProvider.getProvider("Yaml")
                        .load(plugin.getResource("Locale/zh_CN/Message.yml")));
            } catch (Exception e) {
                Logger.warn("Failed to load internal locale!");
                Logger.error("It may not affect a lot, you can ignore it.");
            }

            internal = locale;
        }

        return internal;
    }
}
