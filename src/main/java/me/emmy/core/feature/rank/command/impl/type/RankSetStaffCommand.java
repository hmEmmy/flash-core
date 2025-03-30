package me.emmy.core.feature.rank.command.impl.type;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.database.redis.RedisService;
import me.emmy.core.database.redis.packet.impl.rank.RankUpdatePacketImpl;
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
public class RankSetStaffCommand extends BaseCommand {
    @CommandData(name = "rank.setstaff", permission = "flash.rank.setstaff", description = "Set a rank as staff", usage = "/rank setstaff <rank>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank setstaff <rank> <true/false>"));
            return;
        }

        RankService rankService = this.flash.getServiceRepository().getService(RankService.class);
        Rank rank = rankService.getRank(args[0]);
        if (rank == null) {
            player.sendMessage(CC.translate("&cA rank with that name does not exist!"));
            return;
        }

        boolean setStaffRank;
        try {
            setStaffRank = Boolean.parseBoolean(args[1]);
        } catch (Exception e) {
            player.sendMessage(CC.translate("&cInvalid value for staff status! Use true or false."));
            return;
        }

        rank.setStaffRank(setStaffRank);
        rankService.saveRank(rank);

        RankUpdatePacketImpl rankUpdatePacket = new RankUpdatePacketImpl(rank);
        this.flash.getServiceRepository().getService(RedisService.class).sendPacket(rankUpdatePacket);

        String message;
        if (setStaffRank) {
            message = "&aYou have successfully set the rank &b" + rank.getName() + " &ato staff to &a&ltrue&a.";
        } else {
            message = "&aYou have successfully set the rank &b" + rank.getName() + " &ato staff status: &c&lfalse&a.";
        }
        ActionBarUtil.sendMessage(player, message, 7);
    }
}