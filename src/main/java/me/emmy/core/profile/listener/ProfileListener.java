package me.emmy.core.profile.listener;

import me.emmy.core.profile.Profile;
import me.emmy.core.profile.ProfileService;
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
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }

        Profile profile = new Profile(event.getPlayer().getUniqueId());
        profile.loadProfile();

        this.profileService.addProfile(profile);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.profileService.getProfile(player.getUniqueId());
        profile.setUsername(player.getName());
        profile.setOnline(true);
        event.setJoinMessage(null);
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
}
