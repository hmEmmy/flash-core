package me.emmy.core.feature.rank.command.impl.inheritance;

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
public class RankRemoveInheritanceCommand extends BaseCommand {
    @CommandData(name = "rank.removeinheritance", permission = "flash.rank.removeinheritance", description = "Remove inheritance from a rank", usage = "/rank removeinheritance <rank> <parent>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank removeinheritance <rank> <inheritance>"));
            return;
        }

        RankService rankService = this.flash.getServiceRepository().getService(RankService.class);
        Rank rank = rankService.getRank(args[0]);
        if (rank == null) {
            player.sendMessage(CC.translate("&cThat rank does not exist!"));
            return;
        }

        Rank inheritedRank = rankService.getRank(args[1]);
        if (inheritedRank == null) {
            player.sendMessage(CC.translate("&cThat inheritance does not exist!"));
            return;
        }

        if (!rank.getInheritance().contains(inheritedRank.getName())) {
            player.sendMessage(CC.translate("&cThat rank does not inherit from " + inheritedRank.getName() + "!"));
            return;
        }

        rank.getInheritance().remove(inheritedRank.getName());
        rankService.saveRank(rank);
        rankService.sendUpdatePacket(rank);

        ActionBarUtil.sendMessage(player, "&aYou have successfully removed inheritance from &b" + rank.getName() + " &afrom &b" + inheritedRank.getName() + "&a!", 10);
    }
}