package me.emmy.core.feature.rank.command.impl;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.feature.rank.Rank;
import me.emmy.core.feature.rank.RankService;
import me.emmy.core.util.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 29/03/2025
 */
public class RankListCommand extends BaseCommand {
    @CommandData(name = "rank.list", permission = "flash.rank.list", description = "List all ranks", usage = "/rank list", aliases = "ranks")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        RankService rankService = this.flash.getServiceRepository().getService(RankService.class);

        if (rankService.getRanks().isEmpty()) {
            player.sendMessage(CC.translate("&cThere are no ranks available."));
            return;
        }

        Arrays.asList(
                "",
                "  &3&lRank List &8- &7Available Ranks"
        ).forEach(line -> player.sendMessage(CC.translate(line)));

        List<Rank> sortedRanks = rankService.getRanks().stream()
                .sorted(Comparator.comparingInt(Rank::getWeight).reversed())
                .collect(Collectors.toList());

        sortedRanks.forEach(
                rank -> player.sendMessage(CC.translate("   &3● &f" + rank.getColor() + rank.getName() + " &7- &f" + rank.getPrefix() + "&8[&7" + rank.getWeight() + "&8]" + (rank.isDefaultRank() ? " &7&o(Default Rank)" : "")))
        );

        player.sendMessage("");
    }
}