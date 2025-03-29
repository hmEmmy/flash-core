package me.emmy.core.feature.rank.command.impl.manage;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.database.redis.RedisService;
import me.emmy.core.database.redis.packet.enums.EnumRankPacketType;
import me.emmy.core.database.redis.packet.impl.RankPacketImpl;
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
public class RankCreateCommand extends BaseCommand {
    @CommandData(name = "rank.create", permission = "flash.rank.create", description = "Create a new rank", usage = "/rank create <name>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /rank create <name>"));
            return;
        }

        String rankName = args[0];
        RankService rankService = this.flash.getServiceRepository().getService(RankService.class);
        if (rankService.getRank(rankName) != null) {
            player.sendMessage(CC.translate("&cA rank with that name already exists!"));
            return;
        }

        Rank rank = new Rank(rankName);
        rankService.createRank(rank);

        RankPacketImpl rankPacket = RankPacketImpl.builder()
                .rankName(rankName)
                .packetType(EnumRankPacketType.CREATE)
                .build();
        this.flash.getServiceRepository().getService(RedisService.class).sendPacket(rankPacket);

        ActionBarUtil.sendMessage(player, "&aYou have successfully created a new rank called &b" + rankName + "&a!", 7);
    }
}