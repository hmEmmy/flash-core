package me.emmy.core.server.property.command;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.server.property.ServerProperty;
import me.emmy.core.util.ActionBarUtil;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 30/03/2025
 */
public class SpawnCommand extends BaseCommand {
    @CommandData(name = "spawn", permission = "flash.command.spawn", usage = "/spawn", description = "Teleport to the server spawn.")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        this.flash.getServiceRepository().getService(ServerProperty.class).teleportToSpawn(player);
        ActionBarUtil.sendMessage(player, "&a&lTELEPORTED TO: &bSpawn", 5);
    }
}