package me.emmy.core.cache.listener;

import me.emmy.core.cache.PlayerIdentityCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 06/04/2025
 */
public class PlayerIdentityListener implements Listener {
    private final PlayerIdentityCache playerIdentityCache;

    /**
     * Constructor for the PlayerIdentityListener class.
     *
     * @param playerIdentityCache The player identity cache.
     */
    public PlayerIdentityListener(PlayerIdentityCache playerIdentityCache) {
        this.playerIdentityCache = playerIdentityCache;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        this.playerIdentityCache.addPlayerToCache(player.getUniqueId(), player.getName());
    }
}
