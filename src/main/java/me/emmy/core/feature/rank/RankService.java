package me.emmy.core.feature.rank;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import me.emmy.core.Flash;
import me.emmy.core.api.service.IService;
import me.emmy.core.database.mongo.MongoService;
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

    /**
     * Constructor for RankService.
     *
     * @param plugin the Flash plugin instance
     */
    public RankService(Flash plugin) {
        this.plugin = plugin;
        this.ranks = new ArrayList<>();
        this.collection = plugin.getServiceRepository().getService(MongoService.class).getDatabase().getCollection("ranks");
        this.initialize();
    }

    @Override
    public void initialize() {
        this.collection.find().forEach(document -> {
            Rank rank = new Rank(document.getString("name"));
            rank.setPrefix(document.getString("prefix"));
            rank.setSuffix(document.getString("suffix"));
            rank.setWeight(document.getInteger("weight"));
            rank.setCost(document.getInteger("cost"));
            rank.setColor(ChatColor.valueOf(document.getString("color")));
            rank.setDefaultRank(document.getBoolean("defaultRank"));
            rank.setStaffRank(document.getBoolean("staffRank"));
            rank.setPurchasable(document.getBoolean("purchasable"));
            rank.setHiddenRank(document.getBoolean("hidden"));
            rank.setInheritance((List<String>) document.get("inheritance"));
            rank.setPermissions((List<String>) document.get("permissions"));
            this.ranks.add(rank);
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
     * Retrieves a rank by its name.
     *
     * @param name the name of the rank
     * @return the rank, or null if not found
     */
    public Rank getRank(String name) {
        return this.ranks.stream().filter(rank -> rank.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    /**
     * Retrieves a rank by its object.
     *
     * @param rank the rank object
     * @return the rank, or null if not found
     */
    public Rank getRank(Rank rank) {
        return this.ranks.stream().filter(r -> r.getName().equalsIgnoreCase(rank.getName())).findFirst().orElse(null);
    }

    /**
     * Retrieves the default rank.
     *
     * @return the default rank, or null if not found
     */
    public Rank getDefaultRank() {
        return this.ranks.stream().filter(Rank::isDefaultRank).findFirst().orElse(null);
    }

    /**
     * Method to save a rank.
     *
     * @param rank the rank to save
     */
    public void saveRank(Rank rank) {
        Document document = this.toDocument(rank);
        this.collection.replaceOne(Filters.eq("name", rank.getName()), document, new ReplaceOptions().upsert(true));
    }

    /**
     * Converts a Rank object to a MongoDB Document.
     *
     * @param rank the rank to convert
     * @return the MongoDB Document
     */
    private Document toDocument(Rank rank) {
        Document document = new Document();
        document.put("name", rank.getName());
        document.put("prefix", rank.getPrefix());
        document.put("suffix", rank.getSuffix());
        document.put("weight", rank.getWeight());
        document.put("cost", rank.getCost());
        document.put("color", rank.getColor().name());
        document.put("defaultRank", rank.isDefaultRank());
        document.put("staffRank", rank.isStaffRank());
        document.put("purchasable", rank.isPurchasable());
        document.put("hidden", rank.isHiddenRank());
        document.put("inheritance", rank.getInheritance());
        document.put("permissions", rank.getPermissions());
        return document;
    }

    /**
     * Creates a rank and saves it to the database.
     *
     * @param rank the rank to create
     */
    public void createRank(Rank rank) {
        this.saveRank(rank);
        this.ranks.add(rank);
    }

    /**
     * Deletes a rank from the database and the list.
     *
     * @param rank the rank to delete
     */
    public void deleteRank(Rank rank) {
        this.collection.deleteOne(Filters.eq("name", rank.getName()));
        this.ranks.remove(rank);
    }

    public void createDefaultRank() {
        Rank rank = new Rank("Default");
        rank.setPrefix("");
        rank.setSuffix("");
        rank.setWeight(0);
        rank.setCost(0);
        rank.setColor(ChatColor.GREEN);
        rank.setDefaultRank(true);
        rank.setStaffRank(false);
        rank.setPurchasable(false);
        rank.setHiddenRank(false);
        rank.setInheritance(new ArrayList<>());
        rank.setPermissions(new ArrayList<>());
        this.createRank(rank);
    }

    /**
     * Reloads a rank by deleting and recreating it.
     *
     * @param rank the rank to reload
     */
    public void reloadRank(Rank rank) {
        Rank toBeUpdatedRank = this.getRank(rank.getName());
        if (toBeUpdatedRank == null) {
            return;
        }

        toBeUpdatedRank.setPrefix(rank.getPrefix());
        toBeUpdatedRank.setSuffix(rank.getSuffix());
        toBeUpdatedRank.setWeight(rank.getWeight());
        toBeUpdatedRank.setCost(rank.getCost());
        toBeUpdatedRank.setColor(rank.getColor());
        toBeUpdatedRank.setDefaultRank(rank.isDefaultRank());
        toBeUpdatedRank.setStaffRank(rank.isStaffRank());
        toBeUpdatedRank.setPurchasable(rank.isPurchasable());
        toBeUpdatedRank.setHiddenRank(rank.isHiddenRank());
        toBeUpdatedRank.setInheritance(rank.getInheritance());
        toBeUpdatedRank.setPermissions(rank.getPermissions());
        this.saveRank(toBeUpdatedRank);
    }
}