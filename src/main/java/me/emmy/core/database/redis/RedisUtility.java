package me.emmy.core.database.redis;

import lombok.experimental.UtilityClass;
import me.emmy.core.Flash;
import me.emmy.core.server.ServerProperty;
import me.emmy.core.util.CC;
import me.emmy.core.util.ClickableUtil;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 30/03/2025
 */
@UtilityClass
public class RedisUtility {
    /**
     * Sends an alert message to all players with the specified permission.
     *
     * @param message    The message to send.
     * @param permission The permission required to receive the message.
     */
    public void alert(String message, String permission) {
        String serverName = Flash.getInstance().getServiceRepository().getService(ServerProperty.class).getName();

        for (Player player : Flash.getInstance().getServer().getOnlinePlayers()) {
            if (player.hasPermission(permission)) {
                player.sendMessage(CC.translate("&3(" + serverName + ") &r" + message));
            }
        }
    }

    /**
     * Sends an alert message to all players with the specified permission without a prefix.
     *
     * @param message    The message to send.
     */
    public void alertPubliclyWithoutPrefix(String message) {
        for (Player player : Flash.getInstance().getServer().getOnlinePlayers()) {
            player.sendMessage(CC.translate(message));
        }
    }

    /**
     * Sends a clickable list of messages to all players with the specified permission.
     *
     * @param messages   The list of messages to send.
     * @param permission The permission required to receive the messages.
     * @param hover      The hover text for the clickable messages.
     */
    public void alertClickableList(List<String> messages, String permission, String hover) {
        for (Player player : Flash.getInstance().getServer().getOnlinePlayers()) {
            if (player.hasPermission(permission)) {
                ClickableUtil.sendList(player, messages, null, hover);
            }
        }
    }
}