package me.emmy.core.feature.tag;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import me.emmy.core.Flash;
import me.emmy.core.api.service.IService;
import me.emmy.core.database.mongo.MongoService;
import me.emmy.core.feature.tag.enums.EnumTagCategory;
import me.emmy.core.util.Logger;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 30/03/2025
 */
@Getter
public class TagService implements IService {
    protected final Flash plugin;
    private final List<Tag> tags;
    private final MongoCollection<Document> collection;

    /**
     * Constructor for TagService.
     *
     * @param plugin the Flash plugin instance
     */
    public TagService(Flash plugin) {
        this.plugin = plugin;
        this.tags = new ArrayList<>();
        this.collection = plugin.getServiceRepository().getService(MongoService.class).getDatabase().getCollection("tags");
        this.initialize();
    }

    @Override
    public void initialize() {
        this.collection.find().forEach(document -> {
            Tag tag = new Tag(document.getString("name"), document.getString("appearance"));
            tag.setDescription(document.getString("description"));
            tag.setCategory(EnumTagCategory.valueOf(document.getString("category")));
            tag.setColor(ChatColor.valueOf(document.getString("color")));
            tag.setIcon(Material.valueOf(document.getString("icon")));
            tag.setDurability(document.getInteger("durability"));
            tag.setPurchasable(document.getBoolean("purchasable"));
            tag.setCost(document.getInteger("cost"));
            this.tags.add(tag);
        });

        if (this.tags.isEmpty()) {
            this.createDefaults();
        }
    }

    @Override
    public void closure() {
        this.tags.forEach(this::saveTag);
    }

    /**
     * Retrieves a tag by its name.
     *
     * @param name the name of the tag
     * @return the tag, or null if not found
     */
    public Tag getTag(String name) {
        return this.tags.stream().filter(tag -> tag.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    /**
     * Method to save a tag.
     *
     * @param tag the tag to save
     */
    public void saveTag(Tag tag) {
        Document document = this.toDocument(tag);
        this.collection.replaceOne(Filters.eq("name", tag.getName()), document, new ReplaceOptions().upsert(true));
    }

    /**
     * Converts a Tag object to a MongoDB Document.
     *
     * @param tag the tag to convert
     * @return the MongoDB Document
     */
    private Document toDocument(Tag tag) {
        Document document = new Document();
        document.put("name", tag.getName());
        document.put("appearance", tag.getAppearance());
        document.put("description", tag.getDescription());
        document.put("category", tag.getCategory().name());
        document.put("color", tag.getColor().name());
        document.put("icon", tag.getIcon().name());
        document.put("durability", tag.getDurability());
        document.put("purchasable", tag.isPurchasable());
        document.put("cost", tag.getCost());
        return document;
    }

    /**
     * Creates a tag and saves it to the database.
     *
     * @param tag the tag to create
     */
    public void createTag(Tag tag) {
        this.saveTag(tag);
        this.tags.add(tag);
    }

    /**
     * Helper method to create a new tag.
     *
     * @param name        The tag's name
     * @param appearance  The tag's appearance (e.g., "❤")
     * @param description The tag's description
     * @param category    The tag's category
     * @param color       The tag's color
     * @param icon        The material icon
     * @param durability  The durability of the icon
     * @return A newly created Tag object
     */
    public Tag createTag(String name, String appearance, String description, EnumTagCategory category, ChatColor color, Material icon, int durability) {
        Tag tag = new Tag(name, appearance);
        tag.setDescription(description);
        tag.setCategory(category);
        tag.setColor(color);
        tag.setIcon(icon);
        tag.setDurability(durability);
        tag.setPurchasable(false);
        tag.setCost(0);
        return tag;
    }

    /**
     * Deletes a tag from the database and the list.
     *
     * @param tag the tag to delete
     */
    public void deleteTag(Tag tag) {
        this.collection.deleteOne(Filters.eq("name", tag.getName()));
        this.tags.remove(tag);
    }

    /**
     * Reloads a tag by deleting and recreating it.
     *
     * @param tag the tag to reload
     */
    public void reloadTag(Tag tag) {
        Tag toBeUpdatedTag = this.getTag(tag.getName());
        if (toBeUpdatedTag == null) {
            return;
        }

        toBeUpdatedTag.setAppearance(tag.getAppearance());
        toBeUpdatedTag.setDescription(tag.getDescription());
        toBeUpdatedTag.setCategory(tag.getCategory());
        toBeUpdatedTag.setColor(tag.getColor());
        toBeUpdatedTag.setIcon(tag.getIcon());
        toBeUpdatedTag.setDurability(tag.getDurability());
        toBeUpdatedTag.setPurchasable(tag.isPurchasable());
        toBeUpdatedTag.setCost(tag.getCost());
        this.saveTag(toBeUpdatedTag);
    }

    /**
     * Helper method to get the amount of tags in a specific category.
     *
     * @param category The tag's category
     * @return The number of tags in the specified category
     */
    public int getAmountInCategory(EnumTagCategory category) {
        return (int) this.tags.stream().filter(tag -> tag.getCategory() == category).count();
    }

    public void createDefaults() {
        List<Tag> defaultTags = Arrays.asList(
                this.createTag("Emmy", "&lEmmy", "Emmy's unique Tag", EnumTagCategory.SPECIAL, ChatColor.LIGHT_PURPLE, Material.REDSTONE, 0),
                this.createTag("Heart", "❤", "", EnumTagCategory.EXCLUSIVE, ChatColor.DARK_RED, Material.REDSTONE, 0),
                this.createTag("BetaTester", "&lBetaTester", "Exclusive to Beta Testers", EnumTagCategory.EXCLUSIVE, ChatColor.YELLOW, Material.NAME_TAG, 0),
                this.createTag("Diamond", "&7[&b&l♦&7]", "", EnumTagCategory.EMOJI, ChatColor.AQUA, Material.DIAMOND, 0),
                this.createTag("Star", "★", "Star Tag", EnumTagCategory.EMOJI, ChatColor.YELLOW, Material.GOLD_NUGGET, 0),
                this.createTag("BestWW", "&lBestWW", "", EnumTagCategory.TEXT, ChatColor.DARK_RED, Material.NAME_TAG, 0),
                this.createTag("Crown", "&7[&6&l♛&7]", "", EnumTagCategory.EMOJI, ChatColor.GOLD, Material.GOLDEN_APPLE, 0),
                this.createTag("King", "King ♚", "", EnumTagCategory.TEXT, ChatColor.RED, Material.NAME_TAG, 0),
                this.createTag("Queen", "Queen ♛", "", EnumTagCategory.TEXT, ChatColor.LIGHT_PURPLE, Material.NAME_TAG, 0),
                this.createTag("Tick", "✔", "", EnumTagCategory.EMOJI, ChatColor.GREEN, Material.WOOL, 5),
                this.createTag("Flower", "&7[&d&l❖&7]", "", EnumTagCategory.EMOJI, ChatColor.LIGHT_PURPLE, Material.RED_ROSE, 0),
                this.createTag("Cross", "✖", "", EnumTagCategory.EMOJI, ChatColor.RED, Material.REDSTONE, 0),
                this.createTag("Blood", "&lBLOOD", "", EnumTagCategory.TEXT, ChatColor.RED, Material.REDSTONE, 0),
                this.createTag("Goat", "&lGOAT", "", EnumTagCategory.TEXT, ChatColor.AQUA, Material.EMERALD, 0),
                this.createTag("Banana", "&lBanana", "", EnumTagCategory.TEXT, ChatColor.YELLOW, Material.GOLDEN_APPLE, 0),
                this.createTag("Love", "&lLove", "", EnumTagCategory.TEXT, ChatColor.RED, Material.RED_ROSE, 0),
                this.createTag("Yurrrrrrr", "&lYurrrrrrr", "", EnumTagCategory.TEXT, ChatColor.GREEN, Material.EMERALD, 0),
                this.createTag("Legend", "&r&k|&r&9Legend&r&k|&r", "", EnumTagCategory.TEXT, ChatColor.GOLD, Material.EMERALD, 0),
                this.createTag("First", "&l#1", "", EnumTagCategory.TEXT, ChatColor.RED, Material.EMERALD, 0),
                this.createTag("Godly", "&lGodly", "", EnumTagCategory.TEXT, ChatColor.DARK_RED, Material.EMERALD, 0),
                this.createTag("Prince", "&lPrince", "", EnumTagCategory.TEXT, ChatColor.LIGHT_PURPLE, Material.RED_ROSE, 1),
                this.createTag("Princess", "&lPrincess", "", EnumTagCategory.TEXT, ChatColor.LIGHT_PURPLE, Material.RED_ROSE, 7),
                this.createTag("Demon", "&lDemon", "", EnumTagCategory.TEXT, ChatColor.DARK_RED, Material.REDSTONE, 0)
        );

        for (Tag tag : defaultTags) {
            if (this.getTag(tag.getName()) == null) {
                this.createTag(tag);
                Logger.logInfo("Created tag: " + tag.getName());
            } else {
                Logger.logInfo("Tag already exists: " + tag.getName());
            }
        }

        this.tags.forEach(this::saveTag);
    }
}