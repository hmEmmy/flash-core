package me.emmy.core.server;

import lombok.Getter;
import me.emmy.core.Flash;
import me.emmy.core.api.service.IService;
import me.emmy.core.config.ConfigService;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 29/03/2025
 */
@Getter
public class ServerProperty implements IService {
    protected final Flash plugin;
    private final FileConfiguration config;

    private String name;
    private String region;
    private String motd;

    /**
     * Constructor for ServerProperty.
     *
     * @param plugin the plugin instance of Flash
     */
    public ServerProperty(Flash plugin) {
        this.plugin = plugin;
        this.config = plugin.getServiceRepository().getService(ConfigService.class).getSettingsConfig();
        this.initialize();
    }

    @Override
    public void initialize() {
        this.name = this.config.getString("server.name");
        this.region = this.config.getString("server.region");
        this.motd = ""; //TODO: Add proper motd handling service or whatever, cba rn
    }

    @Override
    public void closure() {

    }
}