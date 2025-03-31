package me.emmy.core.feature.grant;

import me.emmy.core.Flash;
import me.emmy.core.api.service.IService;
import me.emmy.core.feature.grant.listener.GrantListener;
import me.emmy.core.profile.Profile;
import me.emmy.core.profile.ProfileService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 31/03/2025
 */
public class GrantService implements IService {
    protected final Flash plugin;

    /**
     * Constructor for GrantService.
     *
     * @param plugin The Flash plugin instance.
     */
    public GrantService(Flash plugin) {
        this.plugin = plugin;
        this.initialize();
    }

    @Override
    public void initialize() {
        this.plugin.getServer().getPluginManager().registerEvents(new GrantListener(this.plugin), this.plugin);
    }

    @Override
    public void closure() {

    }

    /**
     * Adds a grant to a player's profile.
     *
     * @param profile The profile of the player.
     * @param grant   The grant to be added.
     */
    public void addGrant(Profile profile, Grant grant) {
        List<Grant> grants = profile.getGrants();
        grants.add(grant);

        profile.setGrants(grants);
        Player player = Bukkit.getPlayer(profile.getUuid());
        if (player != null) {
            this.plugin.getServiceRepository().getService(ProfileService.class).attachPermissions(player);
        }

        profile.saveProfile();
    }

    /**
     * Deactivates a specific grant for a target profile.
     *
     * @param targetProfile The target profile.
     * @param rank          The rank to be deactivated.
     * @param reason        The reason for deactivation.
     * @param sender        The sender of the deactivation request.
     */
    public void deactivateGrant(Profile targetProfile, String rank, String reason, String sender) {
        List<Grant> grants = targetProfile.getGrants();
        grants.stream().filter(grant -> grant.getRankName().equalsIgnoreCase(rank)).forEach(grant -> {
            grant.setRemover(sender);
            grant.setRemovalReason(reason);
            grant.setRemovedAt(System.currentTimeMillis());
            grant.setActive(false);
        });

        Player player = Bukkit.getPlayer(targetProfile.getUuid());
        if (player != null) {
            this.plugin.getServiceRepository().getService(ProfileService.class).attachPermissions(player);
        }

        targetProfile.saveProfile();
    }

    /**
     * Removed a grant from a profile.
     *
     * @param profile the profile to delete the grant from.
     * @param grant   the grant to delete.
     */
    public void removeGrant(Profile profile, Grant grant) {
        List<Grant> grants = profile.getGrants();
        grants.remove(grant);
        profile.saveProfile();
    }
}