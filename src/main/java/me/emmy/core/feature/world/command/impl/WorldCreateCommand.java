package me.emmy.core.feature.world.command.impl;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.feature.world.WorldService;
import me.emmy.core.util.ActionBarUtil;
import me.emmy.core.util.CC;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 30/03/2025
 */
public class WorldCreateCommand extends BaseCommand {
    @CommandData(name = "world.create", permission = "flash.command.world.create", description = "Create a new world", usage = "/worldcreate <name> <environment> <type>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 3) {
            player.sendMessage(CC.translate("&cUsage: /world create <name> <environment> <type>"));
            return;
        }

        String worldName = args[0];
        WorldService worldService = this.flash.getServiceRepository().getService(WorldService.class);
        if (this.flash.getServer().getWorld(worldName) != null) {
            player.sendMessage(CC.translate("&cA world with this name already exists!"));
            return;
        }

        Environment environment;
        try {
            environment = Environment.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            String validEnvironments = Arrays.stream(Environment.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            player.sendMessage(CC.translate("&cInvalid environment! Valid environments: &b" + validEnvironments));
            return;
        }

        WorldType worldType;
        try {
            worldType = WorldType.valueOf(args[2].toUpperCase());
        } catch (IllegalArgumentException e) {
            String validTypes = Arrays.stream(WorldType.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            player.sendMessage(CC.translate("&cInvalid world type! Valid types: &b" + validTypes));
            return;
        }

        worldService.createWorld(worldName, environment, worldType);
        ActionBarUtil.sendMessage(player, "&aSuccessfully created a new world called " +
                "&b" + worldName + "&a!", 10);
    }
}