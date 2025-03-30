package me.emmy.core.feature.rank.command.impl.appearance;

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

import java.util.Arrays;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 29/03/2025
 */
public class RankSetDescriptionCommand extends BaseCommand {
    @CommandData(name = "rank.setdescription", permission = "flash.rank.setdescription", description = "Set the description of a rank", usage = "/rank setdescription <rank> <description>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank setdescription <rank> <description>"));
            return;
        }

        RankService rankService = this.flash.getServiceRepository().getService(RankService.class);
        Rank rank = rankService.getRank(args[0]);
        if (rank == null) {
            player.sendMessage(CC.translate("&cA rank with that name does not exist!"));
            return;
        }

        String description = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        rank.setDescription(description);
        rankService.saveRank(rank);

        RankUpdatePacketImpl rankUpdatePacket = new RankUpdatePacketImpl(rank);
        this.flash.getServiceRepository().getService(RedisService.class).sendPacket(rankUpdatePacket);

        ActionBarUtil.sendMessage(player, "&aYou have successfully set the description of &b" + rank.getName() + " &ato &b" + description + "&a!", 10);
    }
}