package me.emmy.core.feature.rank.command.impl.appearance;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.feature.rank.Rank;
import me.emmy.core.feature.rank.RankService;
import me.emmy.core.util.ActionBarUtil;
import me.emmy.core.util.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 29/03/2025
 */
public class RankSetPrefixCommand extends BaseCommand {
    @CommandData(name = "rank.setprefix", permission = "flash.rank.setprefix", description = "Set the prefix of a rank", usage = "/rank setprefix <rank> <prefix>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank setprefix <rank> <prefix>"));
            return;
        }

        RankService rankService = this.flash.getServiceRepository().getService(RankService.class);
        Rank rank = rankService.getRank(args[0]);
        if (rank == null) {
            player.sendMessage(CC.translate("&cThat rank does not exist!"));
            return;
        }

        String prefix = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        rank.setPrefix(prefix);
        rankService.saveRank(rank);
        ActionBarUtil.sendMessage(player, "&aYou have successfully set the prefix of &b" + rank.getName() + " &ato &r" + prefix + "&a!", 7);
    }
}