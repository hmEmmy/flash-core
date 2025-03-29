package me.emmy.core.config;

import lombok.Getter;
import me.emmy.core.Flash;
import me.emmy.core.api.service.IService;
import me.emmy.core.util.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Flash
 * @since 26/03/2025
 */
@Getter
public class ConfigService implements IService {
    protected final Flash plugin;
    private final File dataFolder;

    private final Map<String, FileConfiguration> configs;
    private final Map<String, File> files;

    private final String[] configFileNames = {
            "settings.yml", "database.yml"
    };

    private final FileConfiguration settingsConfig, databaseConfig;

    /**
     * Constructor for the ConfigService class.
     *
     * @param plugin the main class of the plugin
     */
    public ConfigService(Flash plugin) {
        this.plugin = plugin;
        this.dataFolder = plugin.getDataFolder();
        this.configs = new HashMap<>();
        this.files = new HashMap<>();

        this.initialize();

        this.settingsConfig = this.getConfig("settings.yml");
        this.databaseConfig = this.getConfig("database.yml");
    }

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void initialize() {
        if (!this.dataFolder.exists()) {
            this.dataFolder.mkdirs();
        }

        for (String fileName : this.configFileNames) {
            File configFile = new File(this.plugin.getDataFolder(), fileName);
            this.files.put(fileName, configFile);
            if (!configFile.exists()) {
                configFile.getParentFile().mkdirs();
                this.plugin.saveResource(fileName, false);
            }

            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            this.configs.put(fileName, config);
        }
    }

    @Override
    public void closure() {
        this.configs.forEach((fileName, config) -> {
                this.saveConfig(this.getConfigFile(fileName), config);
        });
    }

    /**
     * Get the configuration file by its name.
     *
     * @param fileName the name of the configuration file
     * @return the configuration file
     */
    public FileConfiguration getConfig(String fileName) {
        return this.configs.get(fileName);
    }

    /**
     * Get the file by its name.
     *
     * @param fileName the name of the file
     * @return the file
     */
    public File getConfigFile(String fileName) {
        return this.files.get(fileName);
    }

    /**
     * Save a configuration file.
     *
     * @param configFile The file to save.
     * @param fileConfiguration The configuration to save.
     */
    public void saveConfig(File configFile, FileConfiguration fileConfiguration) {
        try {
            fileConfiguration.save(configFile);
            fileConfiguration.load(configFile);
        } catch (Exception e) {
            Logger.logError("Error occurred while saving config: " + configFile.getName());
        }
    }
}