package me.emmy.core.profile;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import me.emmy.core.Flash;
import me.emmy.core.api.service.IService;
import me.emmy.core.database.mongo.MongoService;
import me.emmy.core.profile.storage.IProfileStorage;
import me.emmy.core.profile.storage.impl.MongoProfileStorageImpl;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Emmy
 * @project Flash
 * @since 29/03/2025
 */
@Getter
public class ProfileService implements IService {
    protected final Flash plugin;
    public MongoCollection<Document> collection;
    private final Map<UUID, Profile> profiles;
    private final IProfileStorage profileStorage;

    /**
     * Constructor for the ProfileService class.
     *
     * @param plugin the plugin instance of Flash.
     */
    public ProfileService(Flash plugin) {
        this.plugin = plugin;
        this.collection = plugin.getServiceRepository().getService(MongoService.class).getDatabase().getCollection("profiles");
        this.profiles = new HashMap<>();
        this.profileStorage = new MongoProfileStorageImpl(this.plugin);
        this.initialize();
    }

    @Override
    public void initialize() {
        this.loadProfiles();
    }

    @Override
    public void save() {

    }

    public void loadProfiles() {
        for (Document document : this.collection.find()) {
            UUID uuid = UUID.fromString(document.getString("uuid"));
            Profile profile = new Profile(uuid);
            profile.loadProfile();

            this.profiles.put(uuid, profile);
        }
    }

    /*
     * Retrieves a profile from the map of profiles.
     *
     * @param uuid the UUID of the profile
     * @return the profile
     */
    public Profile getProfile(UUID uuid) {
        return this.profiles.get(uuid);
    }

    /**
     * Adds a profile to the map of profiles.
     *
     * @param uuid    the UUID of the profile
     * @param profile the profile
     */
    public void addProfile(UUID uuid, Profile profile) {
        this.profiles.put(uuid, profile);
    }
}