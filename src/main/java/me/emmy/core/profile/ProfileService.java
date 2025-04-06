package me.emmy.core.profile;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import me.emmy.core.Flash;
import me.emmy.core.api.service.IService;
import me.emmy.core.database.mongo.MongoService;
import me.emmy.core.feature.grant.Grant;
import me.emmy.core.feature.rank.Rank;
import me.emmy.core.feature.rank.RankService;
import me.emmy.core.profile.data.PermissionData;
import me.emmy.core.profile.listener.ProfileListener;
import me.emmy.core.profile.storage.IProfileStorage;
import me.emmy.core.profile.storage.impl.MongoProfileStorageImpl;
import me.emmy.core.util.Logger;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 29/03/2025
 */
@Getter
public class ProfileService implements IService {
    protected final Flash plugin;
    private final Map<UUID, Profile> profiles;
    private final IProfileStorage profileStorage;
    public final MongoCollection<Document> collection;

    /**
     * Constructor for the ProfileService class.
     *
     * @param plugin the plugin instance of Flash.
     */
    public ProfileService(Flash plugin) {
        this.plugin = plugin;
        this.profiles = new HashMap<>();
        this.collection = plugin.getServiceRepository().getService(MongoService.class).getDatabase().getCollection("profiles");
        this.profileStorage = new MongoProfileStorageImpl(this.plugin);
        this.initialize();
    }

    @Override
    public void initialize() {
        this.plugin.getServer().getPluginManager().registerEvents(new ProfileListener(this), this.plugin);
    }

    @Override
    public void closure() {
        this.profiles.forEach((uuid, profile) -> profile.saveProfile());
        Logger.logInfo("Saved all profiles to the database.");
    }

    /**
     * Retrieves a profile from the map of profiles or creates a new one if it doesn't exist.
     *
     * @param name  the name of the player
     * @param force whether to force create a new profile if not found
     * @return the profile
     */
    @SuppressWarnings("deprecation")
    public Profile getOfflineProfile(String name, boolean force) {
        Logger.log("Getting offline profile for " + name);
        Player onlinePlayer = Bukkit.getPlayer(name);
        if (onlinePlayer != null) {
            return this.getProfile(onlinePlayer.getUniqueId());
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        UUID uuid = offlinePlayer.getUniqueId();

        if (this.profiles.containsKey(uuid)) {
            return this.profiles.get(uuid);
        }

        Document document = this.collection.find(Filters.eq("uuid", uuid.toString())).first();
        if (document == null) {
            document = this.collection.find(Filters.eq("username", name)).first();
        }

        if (document == null) {
            if (!force) {
                return null;
            }

            Profile profile = new Profile(uuid);
            this.addProfile(profile);
            return profile;
        }

        Profile profile = new Profile(UUID.fromString(document.getString("uuid")));
        this.profileStorage.loadProfile(profile);
        this.addProfile(profile);
        return profile;
    }

    /**
     * Retrieves a profile from the map of profiles.
     *
     * @param uuid the UUID of the profile
     * @return the profile
     */
    public Profile getProfile(UUID uuid) {
        return this.profiles.get(uuid);
    }

    /**
     * Retrieves a profile from the map of profiles, or creates a new one if it doesn't exist.
     *
     * @param uuid the UUID of the profile
     * @return the profile
     */
    public Profile getProfileCreateIfAbsent(UUID uuid) {
        if (!this.profiles.containsKey(uuid)) {
            Profile profile = new Profile(uuid);
            profile.loadProfile();

            this.addProfile(profile);

            return profile;
        }

        return this.profiles.get(uuid);
    }

    /**
     * Adds a profile to the map of profiles.
     *
     * @param profile the profile
     */
    public void addProfile(Profile profile) {
        this.profiles.put(profile.getUuid(), profile);
    }

    public void loadProfiles() {
        for (Document document : this.collection.find()) {
            UUID uuid = UUID.fromString(document.getString("uuid"));
            Profile profile = new Profile(uuid);
            profile.loadProfile();

            this.profiles.put(uuid, profile);
        }
    }

    /**
     * Attaches permissions to a player based on their profile.
     *
     * @param player the player to attach permissions to
     */
    public void attachPermissions(Player player) {
        Profile profile = this.profiles.get(player.getUniqueId());
        if (profile == null) {
            return;
        }

        PermissionData permissionData = profile.getPermissionData();
        RankService rankService = this.plugin.getServiceRepository().getService(RankService.class);

        player.getEffectivePermissions().clear();
        PermissionAttachment attachment = player.addAttachment(this.plugin);

        permissionData.getPersonalPermissions().clear();
        permissionData.getRankPermissions().clear();

        for (String permission : profile.getPermissionData().getPersonalPermissions()) {
            permissionData.addPermission(permission);
            attachment.setPermission(permission, true);
        }

        for (Grant grant : profile.getGrants()) {
            if (grant.hasExpired() || !grant.isActive()) continue;

            Rank rank = rankService.getRank(grant.getRankName());
            if (rank == null) continue;

            for (String permission : rank.getPermissions()) {
                permissionData.addRankPermission(permission);
                attachment.setPermission(permission, true);
            }

            for (String inheritedRankName : rank.getInheritance()) {
                Rank inheritedRank = rankService.getRank(inheritedRankName);
                if (inheritedRank == null) continue;

                for (String inheritedPermission : inheritedRank.getPermissions()) {
                    permissionData.addRankPermission(inheritedPermission);
                    attachment.setPermission(inheritedPermission, true);
                }
            }
        }

        Rank defaultRank = rankService.getDefaultRank();
        if (defaultRank != null) {
            for (String permission : defaultRank.getPermissions()) {
                permissionData.addRankPermission(permission);
                attachment.setPermission(permission, true);
            }
        }
    }
}