package me.emmy.core.feature.world.command.impl;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.feature.world.WorldService;
import me.emmy.core.util.ActionBarUtil;
import me.emmy.core.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 30/03/2025
 */
public class WorldCreateVoidWorldCommand extends BaseCommand {
    @CommandData(name = "world.createvoidworld", permission = "flash.command.world.createvoidworld", description = "Create a void world", usage = "/world createvoidworld <name>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /world createvoidworld <name>"));
            return;
        }

        String worldName = args[0];
        WorldService worldService = this.flash.getServiceRepository().getService(WorldService.class);
        if (this.flash.getServer().getWorld(worldName) != null) {
            player.sendMessage(CC.translate("&cA world with this name already exists!"));
            return;
        }

        worldService.createVoidWorld(worldName);
        ActionBarUtil.sendMessage(player, "&aSuccessfully created a void world called &b" + worldName + "&a!", 10);
    }
}