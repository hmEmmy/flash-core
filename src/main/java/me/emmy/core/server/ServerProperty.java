package me.emmy.core.server;

import lombok.Getter;
import me.emmy.core.Flash;
import me.emmy.core.api.service.IService;
import me.emmy.core.config.ConfigService;
import me.emmy.core.util.LocationUtil;
import me.emmy.core.util.Logger;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

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
    private Location spawn;

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
        this.spawn = LocationUtil.deserialize(this.config.getString("server.spawn"));
    }

    @Override
    public void closure() {

    }

    /**
     * Teleports the player to the server spawn location.
     *
     * @param player the player to teleport
     */
    public void teleportToSpawn(Player player) {
        if (this.spawn == null) {
            Logger.logError("Spawn location is null. Please check your configuration or set the spawn location right away using /setspawn.");
            return;
        }
        player.teleport(this.spawn);
    }

    /**
     * Updates the server spawn location and saves it to the configuration file.
     *
     * @param location the new spawn location
     */
    public void updateSpawn(Location location) {
        this.spawn = location;
        this.config.set("server.spawn", LocationUtil.serialize(location));

        ConfigService configService = this.plugin.getServiceRepository().getService(ConfigService.class);
        configService.saveConfig(configService.getConfigFile("settings.yml"), this.config);
    }
}