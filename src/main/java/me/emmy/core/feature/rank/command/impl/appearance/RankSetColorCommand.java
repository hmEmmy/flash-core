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
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 29/03/2025
 */
public class RankSetColorCommand extends BaseCommand {
    @CommandData(name = "rank.setcolor", permission = "flash.rank.setcolor", description = "Set the color of a rank", usage = "/rank setcolor <rank> <color>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank setcolor <rank> <color>"));
            return;
        }

        RankService rankService = this.flash.getServiceRepository().getService(RankService.class);
        Rank rank = rankService.getRank(args[0]);
        if (rank == null) {
            player.sendMessage(CC.translate("&cThat rank does not exist!"));
            return;
        }

        ChatColor color;
        try {
            color = ChatColor.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(CC.translate("&cThat color does not exist! Example: &4DARK_RED&c."));
            return;
        }

        rank.setColor(color);
        rankService.saveRank(rank);

        RankUpdatePacketImpl rankUpdatePacket = new RankUpdatePacketImpl(rank);
        this.flash.getServiceRepository().getService(RedisService.class).sendPacket(rankUpdatePacket);

        ActionBarUtil.sendMessage(player, "&aYou have successfully set the color of &b" + rank.getName() + " &ato &b" + color + color.name() + "&a!", 10);
    }
}