package me.emmy.core.feature.rank;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import me.emmy.core.Flash;
import me.emmy.core.api.service.IService;
import me.emmy.core.database.mongo.MongoService;
import me.emmy.core.util.Logger;
import org.bson.Document;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 29/03/2025
 */
@Getter
public class RankService implements IService {
    protected final Flash plugin;
    private final List<Rank> ranks;
    private final MongoCollection<Document> collection;
    private final Gson gson;

    /**
     * Constructor for RankService.
     *
     * @param plugin the Flash plugin instance
     */
    public RankService(Flash plugin) {
        this.plugin = plugin;
        this.ranks = new ArrayList<>();
        this.collection = plugin.getServiceRepository().getService(MongoService.class).getDatabase().getCollection("ranks");
        this.gson = new Gson();
        this.initialize();
    }

    @Override
    public void initialize() {
        this.collection.find().forEach(document -> {
            try {
                String json = document.toJson();
                Rank rank = this.gson.fromJson(json, Rank.class);
                this.ranks.add(rank);
            } catch (Exception exception) {
                Logger.logError("Failed to deserialize rank document: " + document.toJson());
            }
        });

        if (this.getDefaultRank() == null) {
            this.createDefaultRank();
        }
    }

    @Override
    public void closure() {
        this.ranks.forEach(this::saveRank);
    }

    /**
     * Saves a rank to the MongoDB collection using Gson.
     *
     * @param rank the rank to save
     */
    public void saveRank(Rank rank) {
        String json = this.gson.toJson(rank);
        Document document = Document.parse(json);
        this.collection.replaceOne(Filters.eq("name", rank.getName()), document, new ReplaceOptions().upsert(true));
    }

    /**
     * Allows to retrieve a rank.
     *
     * @param rank the rank to retrieve
     * @return the rank if found, null otherwise
     */
    public Rank getRank(Rank rank) {
        return this.ranks.stream().filter(r -> r.getName().equalsIgnoreCase(rank.getName())).findFirst().orElse(null);
    }

    /**
     * Allows to retrieve a rank by its name.
     *
     * @param name the name of the rank to retrieve
     * @return the rank if found, null otherwise
     */
    public Rank getRank(String name) {
        return this.ranks.stream().filter(r -> r.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    /**
     * Allows to retrieve the default rank.
     *
     * @return the default rank if found, null otherwise
     */
    public Rank getDefaultRank() {
        return this.ranks.stream().filter(Rank::isDefaultRank).findFirst().orElse(null);
    }

    private void createDefaultRank() {
        Rank rank = new Rank("Default");
        rank.setDescription("&7Default rank");
        rank.setPrefix("");
        rank.setSuffix("");
        rank.setColor(ChatColor.GREEN);
        rank.setWeight(0);
        rank.setCost(0);
        rank.setDefaultRank(true);
        rank.setStaffRank(false);
        rank.setHiddenRank(false);
        rank.setPurchasable(false);
        rank.setPermissions(new ArrayList<>());
        rank.setInheritance(new ArrayList<>());
        this.ranks.add(rank);
        this.saveRank(rank);
    }

    /**
     * Creates a new rank with default values.
     *
     * @param rank the rank to create
     */
    public void createRank(Rank rank) {
        rank.setDescription("");
        rank.setPrefix("");
        rank.setSuffix("");
        rank.setColor(ChatColor.WHITE);
        rank.setWeight(0);
        rank.setCost(0);
        rank.setDefaultRank(false);
        rank.setStaffRank(false);
        rank.setHiddenRank(false);
        rank.setPurchasable(false);
        rank.setPermissions(new ArrayList<>());
        rank.setInheritance(new ArrayList<>());
        this.ranks.add(rank);
        this.saveRank(rank);
    }

    /**
     * Deletes a rank from the list and the MongoDB collection.
     *
     * @param rank the rank to delete
     */
    public void deleteRank(Rank rank) {
        this.ranks.remove(rank);
        this.collection.deleteOne(new Document("name", rank.getName()));
    }
}