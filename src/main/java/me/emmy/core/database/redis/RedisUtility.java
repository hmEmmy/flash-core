package me.emmy.core.database.redis;

import lombok.experimental.UtilityClass;
import me.emmy.core.Flash;
import me.emmy.core.util.CC;
import me.emmy.core.server.ServerProperty;
import org.bukkit.entity.Player;

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
     * @param message   The message to send.
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
}