package me.emmy.core.config;

import lombok.Getter;
import me.emmy.core.Flash;
import me.emmy.core.api.service.IService;
import me.emmy.core.util.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
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
            "settings.yml"
    };

    private final FileConfiguration settingsConfig;

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
    }

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void initialize() {
        if (!this.dataFolder.exists()) {
            this.dataFolder.mkdirs();
        }

        for (String fileName : this.configFileNames) {
            File file = new File(this.dataFolder, fileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    Logger.logError("Failed to create " + fileName + " configuration file.");
                }
            }
            this.configs.put(fileName, YamlConfiguration.loadConfiguration(file));
            this.files.put(fileName, file);
        }
    }

    @Override
    public void save() {
        for (Map.Entry<String, FileConfiguration> entry : this.configs.entrySet()) {
            File file = new File(this.dataFolder, entry.getKey());
            try {
                entry.getValue().save(file);
            } catch (IOException e) {
                Logger.logError("Failed to save " + entry.getKey() + " configuration file.");
            }
        }
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
     * Save a configuration file by its name.
     *
     * @param fileName the name of the configuration file
     */
    public void saveConfig(String fileName) {
        FileConfiguration config = this.getConfig(fileName);
        File file = this.getConfigFile(fileName);
        try {
            config.save(file);
        } catch (IOException e) {
            Logger.logError("Failed to save " + fileName + " configuration file.");
        }
    }
}