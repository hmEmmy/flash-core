package me.emmy.core.server.listener;

import me.emmy.core.Flash;
import me.emmy.core.server.ServerProperty;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 30/03/2025
 */
public class MOTDListener implements Listener {
    protected final Flash plugin;

    /**
     * Constructor for the MOTDListener class.
     *
     * @param plugin the plugin instance of Flash.
     */
    public MOTDListener(Flash plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the MOTD display when the server is pinged.
     *
     * @param event the server ping event
     */
    @EventHandler
    private void onServerListPing(ServerListPingEvent event) {
        List<String> motd = this.plugin.getServiceRepository().getService(ServerProperty.class).getMotd();
        if (motd == null || motd.isEmpty()) {
            return;
        }

        event.setMotd(motd.stream().map(line -> line.replace("&", "§")).collect(Collectors.joining("\n")));
    }
}