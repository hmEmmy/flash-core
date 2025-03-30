package me.emmy.core.feature.rank.command.impl.manage;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.database.redis.RedisService;
import me.emmy.core.database.redis.packet.impl.rank.RankDeletionPacketImpl;
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
public class RankDeleteCommand extends BaseCommand {
    @CommandData(name = "rank.delete", permission = "flash.rank.delete", description = "Delete an existing rank", usage = "/rank delete <name>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /rank delete <name>"));
            return;
        }

        String rankName = args[0];
        RankService rankService = this.flash.getServiceRepository().getService(RankService.class);
        Rank rank = rankService.getRank(rankName);
        if (rank == null) {
            player.sendMessage(CC.translate("&cA rank with that name does not exist!"));
            return;
        }

        if (rank.isDefaultRank()) {
            player.sendMessage(CC.translate("&cYou cannot delete the default rank!"));
            return;
        }

        RankDeletionPacketImpl rankDeletionPacket = new RankDeletionPacketImpl(rank);
        this.flash.getServiceRepository().getService(RedisService.class).sendPacket(rankDeletionPacket);

        ActionBarUtil.sendMessage(player, "&cYou have successfully deleted the rank &b" + rankName + "&c!", 7);
    }
}