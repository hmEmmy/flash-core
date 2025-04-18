package me.emmy.core.feature.rank;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import me.emmy.core.Flash;
import me.emmy.core.api.service.IService;
import me.emmy.core.database.mongo.MongoService;
import me.emmy.core.database.redis.RedisService;
import me.emmy.core.database.redis.packet.impl.rank.RankUpdatePacketImpl;
import me.emmy.core.util.Logger;
import org.bson.Document;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
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
    @SuppressWarnings("unchecked")
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

        if (this.ranks.isEmpty()) {
            this.createDefaults();
        }

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
     * Helper method to create a new rank.
     *
     * @param name        The rank's name
     * @param prefix      The rank's prefix
     * @param weight      The rank's weight
     * @param color       The rank's color
     * @param staffRank   Whether the rank is a staff rank
     * @return A newly created Rank object
     */
    private Rank createRank(String name, String prefix, int weight, ChatColor color, boolean staffRank) {
        Rank rank = new Rank(name);
        rank.setPrefix(prefix);
        rank.setSuffix("");
        rank.setWeight(weight);
        rank.setCost(0);
        rank.setColor(color);
        rank.setDefaultRank(false);
        rank.setStaffRank(staffRank);
        rank.setPurchasable(false);
        rank.setHiddenRank(false);
        rank.setInheritance(new ArrayList<>());
        rank.setPermissions(new ArrayList<>());
        return rank;
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

    /**
     * Sends an update packet for a rank over redis.
     *
     * @param rank the rank to send the update for
     */
    public void sendUpdatePacket(Rank rank) {
        RankUpdatePacketImpl rankPacket = new RankUpdatePacketImpl(rank);
        this.plugin.getServiceRepository().getService(RedisService.class).sendPacket(rankPacket);
    }

    private void createDefaults() {
        List<Rank> defaultRanks = Arrays.asList(
                this.createRank("Owner", "&7[&4&oOwner&7] &r", 1000, ChatColor.DARK_RED, true),
                this.createRank("Manager", "&7[&4&oManager&7] &r", 750, ChatColor.DARK_RED, true),
                this.createRank("Admin", "&7[&cAdmin&7] &r", 500, ChatColor.RED, true),
                this.createRank("Mod", "&7[&3&oMod&7] &r", 200, ChatColor.DARK_AQUA, true),
                this.createRank("Helper", "&7[&e&oHelper&7] &r", 100, ChatColor.YELLOW, true),
                this.createRank("Builder", "&7[&2&oBuilder&7] &r", 75, ChatColor.DARK_GREEN, true),
                this.createRank("Famous", "&7[&6&oFamous&7] &r", 50, ChatColor.GOLD, false),
                this.createRank("Media", "&7[&5&oMedia&7] &r", 40, ChatColor.DARK_PURPLE, false),
                this.createRank("MVP", "&7[&bMVP&7] &r", 20, ChatColor.AQUA, false),
                this.createRank("VIP", "&7[&2VIP&7] &r", 10, ChatColor.DARK_GREEN, false)
        );

        for (Rank rank : defaultRanks) {
            if (this.getRank(rank.getName()) == null) {
                this.createRank(rank);
                Logger.logInfo("Created rank: " + rank.getName());
            } else {
                Logger.logInfo("Rank already exists: " + rank.getName());
            }
        }

        this.ranks.forEach(this::saveRank);
    }
}