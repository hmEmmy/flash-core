package me.emmy.core.server;

import me.emmy.core.Flash;
import me.emmy.core.util.CC;
import org.bukkit.Bukkit;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 06/04/2025
 */
public class ServerMonitor {
    protected final Flash plugin;
    public final String prefix;

    /**
     * Constructor for the ServerMonitor class.
     *
     * @param plugin The plugin instance of Flash.
     */
    public ServerMonitor(Flash plugin) {
        this.plugin = plugin;
        this.prefix = CC.translate("&7[&6ServerMonitor&7] &f");
    }

    /**
     * Broadcasts the online status of a server to the console.
     *
     * @param serverName The name of the server.
     * @param online     The online status of the server.
     */
    public void broadcastOnlineStatus(String serverName, boolean online) {
        if (online) {
            Bukkit.getConsoleSender().sendMessage(CC.translate(this.prefix + "&7The server &8" + serverName + " &7is now online and ready to join!"));
        } else {
            Bukkit.getConsoleSender().sendMessage(CC.translate(this.prefix + "&7The server &8" + serverName + " &7is now offline!"));
        }
    }
}