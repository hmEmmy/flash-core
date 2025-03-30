package me.emmy.core.server.command;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.server.ServerProperty;
import me.emmy.core.util.ActionBarUtil;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 30/03/2025
 */
public class SetSpawnCommand extends BaseCommand {
    @CommandData(name = "setspawn", permission = "flash.command.setspawn", usage = "/setspawn", description = "Set the server spawn.")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        this.flash.getServiceRepository().getService(ServerProperty.class).updateSpawn(player.getLocation());
        ActionBarUtil.sendMessage(player, "&a&lUPDATED SPAWN: &b" + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ() + player.getLocation().getPitch() + ", " + player.getLocation().getYaw(), 5);
    }
}