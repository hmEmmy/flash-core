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
public class RankSetHiddenCommand extends BaseCommand {
    @CommandData(name = "rank.sethidden", permission = "flash.rank.sethidden", description = "Set the hidden status of a rank", usage = "/rank sethidden <rank> <true/false>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank sethidden <rank> <true/false>"));
            return;
        }

        RankService rankService = this.flash.getServiceRepository().getService(RankService.class);
        Rank rank = rankService.getRank(args[0]);
        if (rank == null) {
            player.sendMessage(CC.translate("&cThat rank does not exist!"));
            return;
        }

        boolean hidden;
        try {
            hidden = Boolean.parseBoolean(args[1]);
        } catch (IllegalArgumentException e) {
            player.sendMessage(CC.translate("&cInvalid value for hidden status! Use true or false."));
            return;
        }

        rank.setHiddenRank(hidden);
        rankService.saveRank(rank);
        rankService.sendUpdatePacket(rank);

        String message = hidden ? "&aYou have successfully set the rank &b" + rank.getName() + " &ato hidden!" : "&aYou have successfully set the rank &b" + rank.getName() + " &ato visible!";
        ActionBarUtil.sendMessage(player, message, 10);
    }
}