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
public class RankSetPurchasableCommand extends BaseCommand {
    @CommandData(name = "rank.setpurchasable", permission = "flash.rank.setpurchasable", description = "Set a rank as purchasable", usage = "/rank setpurchasable <rank> <true/false>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank setpurchasable <rank> <true/false>"));
            return;
        }

        RankService rankService = this.flash.getServiceRepository().getService(RankService.class);
        Rank rank = rankService.getRank(args[0]);
        if (rank == null) {
            player.sendMessage(CC.translate("&cThat rank does not exist!"));
            return;
        }

        boolean purchasable;
        try {
            purchasable = Boolean.parseBoolean(args[1]);
        } catch (IllegalArgumentException e) {
            player.sendMessage(CC.translate("&cInvalid value! Use true or false."));
            return;
        }

        rank.setPurchasable(purchasable);
        rankService.saveRank(rank);

        RankUpdatePacketImpl rankUpdatePacket = new RankUpdatePacketImpl(rank);
        this.flash.getServiceRepository().getService(RedisService.class).sendPacket(rankUpdatePacket);

        String message = purchasable ? "&aYou have successfully set the rank &b" + rank.getName() + " &ato be purchasable!" : "&aYou have successfully set the rank &b" + rank.getName() + " &ato not be purchasable!";
        ActionBarUtil.sendMessage(player, message, 7);
    }
}