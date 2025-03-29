package me.emmy.core.profile;

import lombok.Getter;
import me.emmy.core.Flash;
import me.emmy.core.api.service.IService;

import java.util.HashMap;
import java.util.List;
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

    /**
     * Constructor for the ProfileService class.
     *
     * @param plugin the plugin instance of Flash.
     */
    public ProfileService(Flash plugin) {
        this.plugin = plugin;
        this.profiles = new HashMap<>();
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

    }

    public Profile getProfile(UUID uuid) {
        return this.profiles.get(uuid);
    }

    public void addProfile(UUID uuid, Profile profile) {
        this.profiles.put(uuid, profile);
    }
}
