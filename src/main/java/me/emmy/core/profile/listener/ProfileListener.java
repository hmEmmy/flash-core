package me.emmy.core.profile.listener;

import me.emmy.core.Flash;
import me.emmy.core.feature.punishment.Punishment;
import me.emmy.core.feature.punishment.enums.EnumPunishmentType;
import me.emmy.core.profile.Profile;
import me.emmy.core.profile.ProfileService;
import me.emmy.core.server.property.ServerProperty;
import me.emmy.core.util.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 29/03/2025
 */
public class ProfileListener implements Listener {
    private final ProfileService profileService;

    /**
     * Constructor for the ProfileListener class.
     *
     * @param profileService The profile repository.
     */
    public ProfileListener(ProfileService profileService) {
        this.profileService = profileService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }

        Profile profile = new Profile(event.getPlayer().getUniqueId());
        profile.loadProfile();

        this.profileService.addProfile(profile);

        this.handlePunishments(event, profile);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.profileService.getProfile(player.getUniqueId());
        profile.setUsername(player.getName());
        profile.setOnline(true);
        event.setJoinMessage(null);

        Flash.getInstance().getServiceRepository().getService(ServerProperty.class).teleportToSpawn(player);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.profileService.getProfile(player.getUniqueId());
        event.setQuitMessage(null);
        profile.setOnline(false);
        profile.saveProfile();
    }

    @EventHandler
    public void onPlayerKickEvent(PlayerKickEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.profileService.getProfile(player.getUniqueId());
        profile.setOnline(false);
        profile.saveProfile();
    }

    /**
     * Handles the punishments of a player during login.
     *
     * @param event   The PlayerLoginEvent.
     * @param profile The profile of the player.
     */
    private void handlePunishments(PlayerLoginEvent event, Profile profile) {
        for (Punishment punishment : profile.getPunishments()) {
            if (punishment.isActive()) {
                if (punishment.getType() == EnumPunishmentType.BAN || punishment.getType() == EnumPunishmentType.BLACKLIST) {
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, CC.translate("&cYou are &4" + punishment.getType().getAction().toUpperCase() + " &cfrom this server.\n&7Reason: " + punishment.getReason()));
                    return;
                }
            }
        }
    }
}