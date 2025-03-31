package me.emmy.core.profile;

import lombok.Getter;
import lombok.Setter;
import me.emmy.core.Flash;
import me.emmy.core.feature.grant.Grant;
import me.emmy.core.feature.rank.Rank;
import me.emmy.core.feature.rank.RankService;
import me.emmy.core.feature.tag.Tag;
import me.emmy.core.feature.tag.TagService;
import me.emmy.core.profile.data.GrantProcessData;
import me.emmy.core.profile.data.PermissionData;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 29/03/2025
 */
@Getter
@Setter
public class Profile {
    private UUID uuid;

    private String username;
    private String tag;

    private boolean online;
    private boolean isGranting;
    private int coins;

    private List<Grant> grants;

    private PermissionData permissionData;
    private GrantProcessData grantProcessData;

    /**
     * Constructor for the Profile class.
     *
     * @param uuid The UUID of the player linked to this profile.
     */
    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.username = Bukkit.getOfflinePlayer(this.uuid).getName();
        this.tag = "";
        this.online = false;
        this.isGranting = false;
        this.coins = 0;
        this.grants = new ArrayList<>();
        this.permissionData = new PermissionData();
        this.grantProcessData = null;
    }

    public void loadProfile() {
        Flash.getInstance().getServiceRepository().getService(ProfileService.class).getProfileStorage().loadProfile(this);
    }

    public void saveProfile() {
        Flash.getInstance().getServiceRepository().getService(ProfileService.class).getProfileStorage().saveProfile(this);
    }

    /**
     * Checks if the player already has a specific rank granted and if it has expired.
     *
     * @param rank The rank to check.
     * @return true if the rank is already granted and not expired, false otherwise.
     */
    public boolean rankAlreadyGranted(Rank rank) {
        return this.grants.stream().anyMatch(g -> g.getRank().equalsIgnoreCase(rank.getName()) && !g.hasExpired());
    }

    /**
     * Get the highest rank that a player has
     *
     * @return the highest rank
     */
    public Rank getHighestRank() {
        RankService rankService = Flash.getInstance().getServiceRepository().getService(RankService.class);
        return this.getActiveGrants().stream()
                .map(Grant::getRank)
                .filter(Objects::nonNull)
                .max(Comparator.comparingInt(rank -> rankService.getRank(rank).getWeight()))
                .map(rankService::getRank)
                .orElse(rankService.getDefaultRank());
    }

    /**
     * Get all active grants for this profile.
     *
     * @return a list of active grants
     */
    public List<Grant> getActiveGrants() {
        return this.grants.stream()
                .filter(grant -> !grant.hasExpired() && grant.isActive())
                .collect(Collectors.toList());
    }

    /**
     * Get all inactive grants for this profile.
     *
     * @return a list of inactive grants
     */
    public List<Grant> getInactiveGrants() {
        return this.grants.stream()
                .filter(grant -> grant.hasExpired() && grant.isActive())
                .collect(Collectors.toList());
    }

    /**
     * Get all active grants for this profile.
     *
     * @return a list of active grants
     */
    public Grant getActiveGrant(String rank) {
        return this.grants.stream()
                .filter(grant -> grant.getRank().equalsIgnoreCase(rank) && !grant.hasExpired() && grant.isActive())
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all inactive grants for this profile.
     *
     * @return a list of inactive grants
     */
    public Grant getInactiveGrant(String rank) {
        return this.grants.stream()
                .filter(grant -> grant.getRank().equalsIgnoreCase(rank) && grant.hasExpired() && grant.isActive())
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves the tag for a given profile.
     *
     * @param spacing Whether to include spacing in the tag or not. (Mainly for the in-game chat)
     * @return The formatted tag string.
     */
    public String getTagAppearance(boolean spacing) {
        TagService tagService = Flash.getInstance().getServiceRepository().getService(TagService.class);
        Tag tag = tagService.getTag(this.tag);
        String appearance = tag.getColor() + tag.getAppearance();
        return this.tag.isEmpty() ? "" : spacing ? " &r" + appearance : "&r" + appearance;
    }
}