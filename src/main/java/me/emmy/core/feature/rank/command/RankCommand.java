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
                "   &3● &f/rank list",
                "   &3● &f/rank info &b<rank>",
                "   &3● &f/rank create &b<rank>",
                "   &3● &f/rank delete &b<rank>",
                "   &3● &f/rank setcolor &b<rank> &3<color>",
                "   &3● &f/rank setcost &b<rank> &3<cost>",
                "   &3● &f/rank setdefault &b<rank> &3<true/false>",
                "   &3● &f/rank setdescription &b<rank> &3<description>",
                "   &3● &f/rank sethidden &b<rank> &3<true/false>",
                "   &3● &f/rank setprefix &b<rank> &3<prefix>",
                "   &3● &f/rank setpurchasable &b<rank> &3<true/false>",
                "   &3● &f/rank setstaff &b<rank> &3<true/false>",
                "   &3● &f/rank setsuffix &b<rank> &3<suffix>",
                "   &3● &f/rank setweight &b<rank> &3<weight>",
                "   &3● &f/rank addpermission &b<rank> &3<permission>",
                "   &3● &f/rank removepermission &b<rank> &3<permission>",
                "   &3● &f/rank addinheritance &b<rank> &3<inheritance>",
                "   &3● &f/rank removeinheritance &b<rank> &3<inheritance>",
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));
    }
}