package me.emmy.core.util;

import lombok.experimental.UtilityClass;
import me.emmy.core.Flash;
import org.bukkit.Bukkit;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Flash
 * @since 26/03/2025
 */
@UtilityClass
public class Logger {
    /**
     * Log an error message to the console.
     *
     * @param message the message to log
     */
    public void logError(String message) {
        Bukkit.getConsoleSender().sendMessage(CC.translate("&c[" + Flash.getInstance().getDescription().getName() + "] Error &8&l| &c" + message));
    }

    /**
     * Log an info message to the console.
     *
     * @param message the message to log
     */
    public void logInfo(String message) {
        Bukkit.getConsoleSender().sendMessage(CC.translate("&f[&3" + Flash.getInstance().getDescription().getName() + "&f] &fInfo &8&l| &f" + message));
    }

    /**
     * Log a message to the console.
     *
     * @param message the message to log
     */
    public void log(String message) {
        Bukkit.getConsoleSender().sendMessage(CC.translate("&f[&3" + Flash.getInstance().getDescription().getName() + "&f] &8&l| &7" + message));
    }

    /**
     * Log the startup information to the console.
     *
     * @param plugin the plugin instance of Flash
     */
    public void logStartupInfo(Flash plugin, long startupTime) {
        Arrays.asList(
                "",
                " &3&l" + plugin.getDescription().getName().toUpperCase() + " PRACTICE CORE",
                "",
                " &7 * &bAuthor: &f" + plugin.getDescription().getAuthors().toString().replace("[", "").replace("]", ""),
                " &7 * &bVersion: &f" + plugin.getDescription().getVersion(),
                "",
                " &7 * &bRanks: &f" + "0",
                " &7 * &bTags: &f" + "0",
                "",
                " &7 * &bSpigot: &f" + plugin.getServer().getName(),
                "",
                " &f -> &bLoad Time: &f" + (System.currentTimeMillis() - startupTime) + "ms",
                ""
        ).forEach(message -> Bukkit.getConsoleSender().sendMessage(CC.translate(message)));
    }
}