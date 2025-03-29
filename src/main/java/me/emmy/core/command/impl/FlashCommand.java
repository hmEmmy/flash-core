package me.emmy.core.command.impl;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.util.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Flash
 * @since 26/03/2025
 */
public class FlashCommand extends BaseCommand {
    @CommandData(name = "flash", permission = "flash.command.flash", description = "The main command for the Flash plugin.")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Arrays.asList(
                "",
                "  &3&l" + this.flash.getDescription().getName() + " Core &8- &7Information",
                "   &3● &fAuthor: &b" + this.flash.getDescription().getAuthors().toString().replace("[", "").replace("]", ""),
                "   &3● &fVersion: &b" + this.flash.getDescription().getVersion(),
                "   &3● &fDiscord: &b" + "https://discord.gg/ERgRdXPS9T",
                "   &3● &fDescription: &b" + this.flash.getDescription().getDescription(),
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));
    }
}