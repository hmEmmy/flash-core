package me.emmy.core.feature.world.command.impl;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.feature.world.WorldService;
import me.emmy.core.util.ActionBarUtil;
import me.emmy.core.util.CC;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 30/03/2025
 */
public class WorldTeleportCommand extends BaseCommand {
    @CommandData(name = "world.teleport", permission = "flash.command.world", aliases = "world.tp", description = "Teleport to a world", usage = "/world teleport <world>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /world teleport <world>"));
            return;
        }

        WorldService worldService = this.flash.getServiceRepository().getService(WorldService.class);
        World world = this.flash.getServer().getWorld(args[0]);
        if (world == null) {
            player.sendMessage(CC.translate("&cWorld " + args[0] + " does not exist!"));
            return;
        }

        worldService.teleportPlayer(player, world.getName());
        ActionBarUtil.sendMessage(player, "&a&lTELEPORTED TO: &b" + world.getName(), 5);
    }
}