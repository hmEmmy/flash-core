package me.emmy.core.feature.world.command.impl;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.feature.world.menu.WorldDeleteConfirmMenu;
import me.emmy.core.util.CC;
import org.bukkit.entity.Player;

/**
 * Command to delete an existing world.
 * 
 * @author Emmy
 * @project Flash-Core
 * @since 30/03/2025
 */
public class WorldDeleteCommand extends BaseCommand {
    @CommandData(name = "world.delete", permission = "flash.command.world.delete", description = "Delete an existing world", usage = "/worlddelete <name> [deleteFiles]")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /world delete <name> <deleteFiles>"));
            return;
        }

        String worldName = args[0];
        if (this.flash.getServer().getWorld(worldName) == null) {
            player.sendMessage(CC.translate("&cThe world &b" + worldName + " &cdoes not exist!"));
            return;
        }

        boolean deleteFiles;
        try {
            deleteFiles = Boolean.parseBoolean(args[1]);
        } catch (Exception e) {
            player.sendMessage(CC.translate("&cInvalid argument for deleteFiles. Use true or false."));
            return;
        }

        new WorldDeleteConfirmMenu(worldName, deleteFiles).openMenu(player);
    }
}