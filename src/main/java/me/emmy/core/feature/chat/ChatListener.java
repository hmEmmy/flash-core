package me.emmy.core.feature.chat;

import me.emmy.core.Flash;
import me.emmy.core.config.ConfigService;
import me.emmy.core.feature.tag.Tag;
import me.emmy.core.feature.tag.TagService;
import me.emmy.core.profile.Profile;
import me.emmy.core.profile.ProfileService;
import me.emmy.core.util.CC;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 31/03/2025
 */
public class ChatListener implements Listener {
    protected final Flash plugin;

    /**
     * Constructor for ChatListener.
     *
     * @param plugin The plugin instance of Flash.
     */
    public ChatListener(Flash plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.plugin.getServiceRepository().getService(ProfileService.class).getProfile(player.getUniqueId());
        if (profile == null) return;

        //TODO: punishment handling, chat mute handling, chat filter handling

        boolean translate = player.hasPermission("flash.chat.translate");
        String message = event.getMessage();

        FileConfiguration config = this.plugin.getServiceRepository().getService(ConfigService.class).getSettingsConfig();
        if (config.getBoolean("chat.formatting.enabled")) {
            String format = config.getString("chat.formatting.format");

            format = CC.translate(format);

            format = format.replace("%prefix%", CC.translate(profile.getHighestRank().getPrefix()));
            format = format.replace("%suffix%", CC.translate(profile.getHighestRank().getSuffix()));
            format = format.replace("%tag%", CC.translate(profile.getTagAppearance(true)));
            format = format.replace("%color%", String.valueOf(profile.getHighestRank().getColor()));
            format = format.replace("%player%", player.getName());

            String finalMessage = translate ? CC.translate(message) : message;
            format = format.replace("%message%", finalMessage);

            event.setFormat(format);
        }
    }
}