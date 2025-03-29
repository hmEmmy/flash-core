package me.emmy.core.util;

import lombok.experimental.UtilityClass;
import me.emmy.core.Flash;
import me.emmy.core.config.ConfigService;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 29/03/2025
 */
@UtilityClass
public class ServerProperty {
    private final FileConfiguration config = Flash.getInstance().getServiceRepository().getService(ConfigService.class).getSettingsConfig();

    public final String serverName = config.getString("server.name");
    public final String serverRegion = config.getString("server.region");
}