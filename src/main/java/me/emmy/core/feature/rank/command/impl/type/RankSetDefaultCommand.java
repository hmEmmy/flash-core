package me.emmy.core.feature.rank.command.impl.type;

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
public class RankSetDefaultCommand extends BaseCommand {
    @CommandData(name = "rank.setdefault", permission = "flash.rank.setdefault", description = "Set a rank as the default rank", usage = "/rank setdefault <name>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank setdefault <name> <true/false>"));
            return;
        }

        if (this.flash.getServer().getOnlinePlayers().size() > 1) {
            player.sendMessage(CC.translate("&cYou may only set the default rank when there are no other players online!"));
            return;
        }

        RankService rankService = this.flash.getServiceRepository().getService(RankService.class);
        Rank rank = rankService.getRank(args[0]);
        if (rank == null) {
            player.sendMessage(CC.translate("&cA rank with that name does not exist!"));
            return;
        }

        boolean setDefaultRank;
        try {
            setDefaultRank = Boolean.parseBoolean(args[1]);
        } catch (Exception e) {
            player.sendMessage(CC.translate("&cInvalid value for default rank! Use true or false."));
            return;
        }

        rank.setDefaultRank(setDefaultRank);
        rankService.saveRank(rank);
        rankService.sendUpdatePacket(rank);

        String message;
        if (setDefaultRank) {
            message = "&aYou have successfully set the rank &b" + rank.getName() + " &aas the default rank!";
        } else {
            message = "&cYou have successfully removed the default rank from &b" + rank.getName() + "&c!";
        }
        ActionBarUtil.sendMessage(player, message, 10);
    }
}