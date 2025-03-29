package me.emmy.core.profile.storage.impl;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.emmy.core.Flash;
import me.emmy.core.profile.Profile;
import me.emmy.core.profile.ProfileService;
import me.emmy.core.profile.storage.IProfileStorage;
import org.bson.Document;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 29/03/2025
 */
public class MongoProfileStorageImpl implements IProfileStorage {
    protected final Flash plugin;

    /**
     * Constructor for the MongoProfileStorageImpl class.
     *
     * @param plugin the plugin instance of Flash.
     */
    public MongoProfileStorageImpl(Flash plugin) {
        this.plugin = plugin;
    }

    /**
     * Saves the given profile to the database/storage.
     *
     * @param profile the profile to save
     */
    @Override
    public void saveProfile(Profile profile) {
        Document document = new Document();
        document.put("uuid", profile.getUuid().toString());
        document.put("username", profile.getUsername());
        document.put("coins", profile.getCoins());
        document.put("tag", profile.getTag());

        this.plugin.getServiceRepository().getService(ProfileService.class).getCollection().replaceOne(Filters.eq("uuid", profile.getUuid().toString()), document, new ReplaceOptions().upsert(true));
    }

    /**
     * Loads the profile from the database/storage.
     *
     * @param profile the profile to load
     */
    @Override
    public void loadProfile(Profile profile) {
        Document document = this.plugin.getServiceRepository().getService(ProfileService.class).getCollection().find(Filters.eq("uuid", profile.getUuid().toString())).first();
        if (document == null) {
            this.saveProfile(profile);
            return;
        }

        profile.setUsername(document.getString("username"));
        profile.setCoins(document.containsKey("coins") ? document.getInteger("coins") : 0);
        profile.setTag(document.getString("tag") == null ? "" : document.getString("tag"));
    }
}