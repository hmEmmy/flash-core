package me.emmy.core.feature.rank.command.impl.manage;

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
public class RankSetWeightCommand extends BaseCommand {
    @CommandData(name = "rank.setweight", permission = "flash.rank.setweight", description = "Set the weight of a rank", usage = "/rank setweight <rank> <weight>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank setweight <rank> <weight>"));
            return;
        }

        RankService rankService = this.flash.getServiceRepository().getService(RankService.class);
        Rank rank = rankService.getRank(args[0]);
        if (rank == null) {
            player.sendMessage(CC.translate("&cThat rank does not exist!"));
            return;
        }

        int weight;
        try {
            weight = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cThat is not a valid number!"));
            return;
        }

        if (weight < 0) {
            player.sendMessage(CC.translate("&cThe weight must be a positive number!"));
            return;
        }

        rank.setWeight(weight);
        rankService.saveRank(rank);
        rankService.sendUpdatePacket(rank);

        ActionBarUtil.sendMessage(player, "&aYou have successfully set the weight of &b" + rank.getName() + " &ato &b" + weight + "&a!", 10);
    }
}