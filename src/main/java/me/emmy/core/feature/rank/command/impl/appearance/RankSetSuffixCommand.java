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
public class RankSetSuffixCommand extends BaseCommand {
    @CommandData(name = "rank.setsuffix", permission = "flash.rank.setsuffix", description = "Set the suffix of a rank", usage = "/rank setsuffix <rank> <suffix>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank setsuffix <rank> <suffix>"));
            return;
        }

        RankService rankService = this.flash.getServiceRepository().getService(RankService.class);
        Rank rank = rankService.getRank(args[0]);
        if (rank == null) {
            player.sendMessage(CC.translate("&cThat rank does not exist!"));
            return;
        }

        String suffix = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        if (suffix.length() > 16) {
            player.sendMessage(CC.translate("&cThe suffix cannot be longer than 16 characters!"));
            return;
        }

        rank.setSuffix(suffix);
        rankService.saveRank(rank);
        rankService.sendUpdatePacket(rank);

        ActionBarUtil.sendMessage(player, "&aYou have successfully set the suffix of &b" + rank.getName() + " &ato &b" + suffix + "&a!", 10);
    }
}