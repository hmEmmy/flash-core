package me.emmy.core.database.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.emmy.core.Flash;
import me.emmy.core.api.service.IService;
import me.emmy.core.config.ConfigService;
import me.emmy.core.util.Logger;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 29/03/2025
 */
@Getter
public class MongoService implements IService {
    protected final Flash plugin;
    private MongoDatabase database;
    private MongoClient client;

    /**
     * Constructor for the MongoService class.
     *
     * @param plugin the plugin instance of Flash.
     */
    public MongoService(Flash plugin) {
        this.plugin = plugin;
        this.initialize();
    }

    @Override
    public void initialize() {
        try {
            FileConfiguration config = Flash.getInstance().getServiceRepository().getService(ConfigService.class).getDatabaseConfig();
            String uri = config.getString("database.mongo.uri");
            String databaseName = config.getString("database.mongo.database");

            ConnectionString connectionString = new ConnectionString(uri);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .retryWrites(true)
                    .build();

            this.client = MongoClients.create(settings);
            this.database = this.client.getDatabase(databaseName);
            Logger.logInfo("Successfully connected to MongoDB.");
        } catch (Exception e) {
            Logger.logError("Failed to connect to MongoDB. Please check your connection string and database name.");
            System.exit(2);
        }
    }

    @Override
    public void closure() {
        if (this.client == null) {
            return;
        }
        this.client.close();
        Logger.logInfo("Successfully closed MongoDB connection.");
    }
}