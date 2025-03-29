package me.emmy.core.feature.rank.command.impl;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.feature.rank.Rank;
import me.emmy.core.feature.rank.RankService;
import me.emmy.core.util.ActionBarUtil;
import me.emmy.core.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 29/03/2025
 */
public class RankSetCostCommand extends BaseCommand {
    @CommandData(name = "rank.setcost", permission = "flash.rank.setcost", description = "Set the cost of a rank", usage = "/rank setcost <rank> <cost>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank setcost <rank> <cost>"));
            return;
        }

        RankService rankService = this.flash.getServiceRepository().getService(RankService.class);
        Rank rank = rankService.getRank(args[0]);
        if (rank == null) {
            player.sendMessage(CC.translate("&cThat rank does not exist!"));
            return;
        }

        int cost;
        try {
            cost = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cThe cost must be a number!"));
            return;
        }

        rank.setCost(cost);
        rankService.saveRank(rank);
        ActionBarUtil.sendMessage(player, "&aYou have successfully set the cost of &b" + rank.getName() + " &ato &b" + cost + "&a!", 7);
    }
}