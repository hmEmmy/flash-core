package me.emmy.core.util;

import lombok.experimental.UtilityClass;
import me.emmy.core.Flash;
import me.emmy.core.feature.rank.RankService;
import me.emmy.core.feature.tag.TagService;
import me.emmy.core.profile.ProfileService;
import org.bukkit.Bukkit;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Flash-Core
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
        Bukkit.getConsoleSender().sendMessage(CC.translate("&f[&b" + Flash.getInstance().getDescription().getName() + "&f] &fInfo &8&l| &f" + message));
    }

    /**
     * Log a message to the console.
     *
     * @param message the message to log
     */
    public void log(String message) {
        Bukkit.getConsoleSender().sendMessage(CC.translate("&f[&b" + Flash.getInstance().getDescription().getName() + "&f] &fLog &8&l| &7" + message));
    }

    /**
     * Log the startup information to the console.
     *
     * @param plugin the plugin instance of Flash
     */
    public void logStartupInfo(Flash plugin, long startupTime) {
        Arrays.asList(
                "",
                " &3&l" + plugin.getDescription().getName().toUpperCase() + " CORE",
                "",
                " &7 * &bAuthor: &f" + plugin.getDescription().getAuthors().toString().replace("[", "").replace("]", ""),
                " &7 * &bVersion: &f" + plugin.getDescription().getVersion(),
                " &7 * &bDescription: &f" + plugin.getDescription().getDescription(),
                "",
                " &7 * &bLoaded Profiles: &f" + plugin.getServiceRepository().getService(ProfileService.class).getProfiles().size(),
                " &7 * &bRanks: &f" + plugin.getServiceRepository().getService(RankService.class).getRanks().size(),
                " &7 * &bTags: &f" + plugin.getServiceRepository().getService(TagService.class).getTags().size(),
                "",
                " &7 * &bSpigot: &f" + plugin.getServer().getName(),
                "",
                " &f -> &bLoad Time: &f" + (System.currentTimeMillis() - startupTime) + "ms",
                ""
        ).forEach(message -> Bukkit.getConsoleSender().sendMessage(CC.translate(message)));
    }
}