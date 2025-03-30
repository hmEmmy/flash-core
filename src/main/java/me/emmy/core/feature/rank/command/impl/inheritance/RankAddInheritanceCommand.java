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
public class RankAddInheritanceCommand extends BaseCommand {
    @CommandData(name = "rank.addinheritance", permission = "flash.rank.addinheritance", description = "Add inheritance to a rank", usage = "/rank addinheritance <rank> <inheritance>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank addinheritance <rank> <inheritance>"));
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
            player.sendMessage(CC.translate("&cThat inherited rank does not exist!"));
            return;
        }

        if (rank.getInheritance().contains(inheritedRank.getName())) {
            player.sendMessage(CC.translate("&cThat rank is already inherited!"));
            return;
        }

        rank.getInheritance().add(inheritedRank.getName());
        rankService.saveRank(rank);
        rankService.sendUpdatePacket(rank);

        ActionBarUtil.sendMessage(player, "&aYou have successfully added &b" + inheritedRank.getName() + " &ato the inheritance of &b" + rank.getName() + "&a!", 10);
    }
}