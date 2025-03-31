package me.emmy.core.feature.grant.listener;

import me.emmy.core.Flash;
import me.emmy.core.feature.grant.menu.GrantDurationMenu;
import me.emmy.core.profile.Profile;
import me.emmy.core.profile.ProfileService;
import me.emmy.core.util.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 31/03/2025
 */
public class GrantListener implements Listener {
    protected final Flash plugin;

    /**
     * Constructor for GrantListener.
     *
     * @param plugin The plugin instance of Flash.
     */
    public GrantListener(Flash plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onEnterReason(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = this.plugin.getServiceRepository().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.isGranting()) {
            if (profile.getGrantProcessData() == null) {
                return;
            }

            String message = event.getMessage();
            event.setCancelled(true);

            if (message.equalsIgnoreCase("cancel")) {
                profile.setGranting(false);
                profile.setGrantProcessData(null);
                player.sendMessage(CC.translate("&4[Grant] &cGrant process cancelled."));
                return;
            }

            profile.getGrantProcessData().setReason(event.getMessage());
            profile.setGranting(false);

            player.sendMessage(CC.translate("&4[Grant] &aEntered reason: &7'&r" + message + "&7'"));
            new GrantDurationMenu(profile.getGrantProcessData()).openMenu(player);
        }
    }
}