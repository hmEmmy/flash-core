package me.emmy.core.feature.world.command;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.util.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 30/03/2025
 */
public class WorldCommand extends BaseCommand {
    @CommandData(name = "world", permission = "flash.command.world", description = "World command", usage = "/world")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Arrays.asList(
                "",
                "&3&lWorld Commands &8- &7Help",
                "   &3● &f/world list",
                "   &3● &f/world create &b<name> &3<worldtype>",
                "   &3● &f/world createvoidworld &b<name>",
                "   &3● &f/world delete &b<name>",
                "   &3● &f/world tp &b<name>",
                "   &3● &f/world setrule &b<name> &3<gamerule> <value>",
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));
    }
}