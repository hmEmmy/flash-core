package me.emmy.core.util;

import lombok.experimental.UtilityClass;
import me.emmy.core.Flash;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Emmy
 * @project Alley
 * @date 17/10/2024 - 07:26
 */
@UtilityClass
public class ActionBarUtil {
    /**
     * Method to send an action bar message to a player in an specific interval.
     *
     * @param player The player.
     * @param message The message.
     * @param durationMillis The duration in milliseconds.
     */
    public void sendMessage(Player player, String message, int durationMillis) {
        try {
            IChatBaseComponent chatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + CC.translate(message) + "\"}");
            PacketPlayOutChat packet = new PacketPlayOutChat(chatBaseComponent, (byte) 2);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

            if (durationMillis > 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        IChatBaseComponent clearChatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"\"}");
                        PacketPlayOutChat clearPacket = new PacketPlayOutChat(clearChatBaseComponent, (byte) 2);
                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(clearPacket);
                    }
                }.runTaskLater(Flash.getInstance(), durationMillis * 20L);
            }
        } catch (Exception exception) {
            Logger.logError("Failed to send action bar message to " + player.getName() + ": " + exception.getMessage());
        }
    }
}