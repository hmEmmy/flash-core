package me.emmy.core.cache;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import me.emmy.core.Flash;
import me.emmy.core.database.mongo.MongoService;
import me.emmy.core.profile.ProfileService;
import me.emmy.core.util.Logger;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 06/04/2025
 */
@Getter
public class PlayerIdentityCache {
    protected final Flash plugin;
    private final Map<UUID, String> cachedPlayers;
    private final int cacheSizeLimit = 10000;
    public final MongoCollection<Document> collection;

    /**
     * Constructor for PlayerIdentityCache.
     *
     * @param plugin The plugin instance of Flash.
     */
    public PlayerIdentityCache(Flash plugin) {
        this.plugin = plugin;
        this.cachedPlayers = new LinkedHashMap<UUID, String>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<UUID, String> eldest) {
                return size() > cacheSizeLimit;
            }
        };

        this.collection = plugin.getServiceRepository().getService(MongoService.class).getDatabase().getCollection("player_identity_cache");

        this.getServerData();
        Logger.log("Cached " + this.cachedPlayers.size() + " players.");
    }

    public void getServerData() {
        this.loadDataFromMongo();

        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            if (!this.cachedPlayers.containsKey(player.getUniqueId())) {
                String playerName = player.getName();
                if (playerName != null) {
                    this.cachedPlayers.put(player.getUniqueId(), playerName);
                    this.savePlayerToMongo(player.getUniqueId(), playerName);
                }
            }
        }
    }

    public void reloadData() {
        this.cachedPlayers.clear();
        this.getServerData();
    }

    /**
     * Gets an online player by their name from the cache or server.
     *
     * @param name The player's name.
     * @return The player, or null if not found.
     */
    public Player getPlayer(String name) {
        return this.cachedPlayers.entrySet().stream()
                .filter(entry -> entry.getValue().equalsIgnoreCase(name))
                .map(Map.Entry::getKey)
                .findFirst()
                .map(uuid -> this.plugin.getServer().getPlayer(uuid))
                .orElse(null);
    }

    /**
     * Gets an offline player by their name from the cache or server.
     *
     * @param name The player's name.
     * @return The offline player, or null if not found.
     */
    public OfflinePlayer getOfflinePlayer(String name) {
        return this.cachedPlayers.entrySet().stream()
                .filter(entry -> entry.getValue().equalsIgnoreCase(name))
                .map(Map.Entry::getKey)
                .findFirst()
                .map(uuid -> this.plugin.getServer().getOfflinePlayer(uuid))
                .orElse(Bukkit.getOfflinePlayer(this.plugin.getServiceRepository().getService(ProfileService.class).getOfflineProfile(name, true).getUuid()));
    }

    /**
     * Removes a player from the cache and MongoDB.
     *
     * @param uuid The player's UUID.
     */
    public void removePlayerFromCache(UUID uuid) {
        this.cachedPlayers.remove(uuid);
        this.removePlayerFromMongo(uuid);
    }

    /**
     * Adds or updates a player in the cache and MongoDB.
     *
     * @param uuid       The player's UUID.
     * @param playerName The player's name.
     */
    public void addPlayerToCache(UUID uuid, String playerName) {
        this.cachedPlayers.put(uuid, playerName);
        this.savePlayerToMongo(uuid, playerName);
    }

    private void loadDataFromMongo() {
        for (Document doc : this.collection.find()) {
            UUID uuid = UUID.fromString(doc.getString("uuid"));
            String playerName = doc.getString("playerName");
            this.cachedPlayers.put(uuid, playerName);
        }
    }

    /**
     * Saves a player to MongoDB.
     *
     * @param uuid       The player's UUID.
     * @param playerName The player's name.
     */
    private void savePlayerToMongo(UUID uuid, String playerName) {
        Document document = new Document("uuid", uuid.toString()).append("playerName", playerName);
        this.collection.replaceOne(new Document("uuid", uuid.toString()), document, new ReplaceOptions().upsert(true));
    }

    /**
     * Removes a player from MongoDB.
     *
     * @param uuid The player's UUID.
     */
    private void removePlayerFromMongo(UUID uuid) {
        this.collection.deleteOne(new Document("uuid", uuid.toString()));
    }
}
