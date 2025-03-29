package me.emmy.core.feature.rank.command;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.util.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 29/03/2025
 */
public class RankCommand extends BaseCommand {
    @CommandData(name = "rank", permission = "flash.rank", description = "Rank command", usage = "/rank")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Arrays.asList(
                "",
                "  &3&lRank Commands &8- &7Help",
                "   &3● &f/rank list &8- &7List all ranks",
                "   &3● &f/rank info <rank> &8- &7View rank information",
                "   &3● &f/rank create <name> &8- &7Create a new rank",
                "   &3● &f/rank delete <name> &8- &7Delete a rank",
                "   &3● &f/rank setcolor <rank> <color> &8- &7Set the color of a rank",
                "   &3● &f/rank setcost <rank> <cost> &8- &7Set the cost of a rank",
                "   &3● &f/rank setdescription <rank> <description> &8- &7Set the description of a rank",
                "   &3● &f/rank setprefix <rank> <prefix> &8- &7Set the prefix of a rank",
                "   &3● &f/rank setsuffix <rank> <suffix> &8- &7Set the suffix of a rank",
                "   &3● &f/rank setweight <rank> <weight> &8- &7Set the weight of a rank",
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));
    }
}