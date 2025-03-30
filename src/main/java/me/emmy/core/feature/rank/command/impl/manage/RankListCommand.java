package me.emmy.core.feature.rank.command.impl.manage;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.feature.rank.Rank;
import me.emmy.core.feature.rank.RankService;
import me.emmy.core.util.CC;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.entity.Player;

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

        String yes = CC.translate("&aYes");
        String no = CC.translate("&cNo");
        String none = CC.translate("&cNone");
        String notSet = CC.translate("&cNot Set");

        player.sendMessage(CC.translate(""));
        player.sendMessage(CC.translate("  &3&lRank List &8- &7Available Ranks"));

        List<Rank> sortedRanks = rankService.getRanks().stream()
                .sorted(Comparator.comparingInt(Rank::getWeight).reversed())
                .collect(Collectors.toList());

        sortedRanks.forEach(rank -> {
            BaseComponent[] hoverText = new ComponentBuilder("")
                    .append(CC.translate("  &3&lRank Information &8- &7" + rank.getColor() + rank.getName())).append("\n")
                    .append(CC.translate("   &3● &fPrefix: ")).append(CC.translate(rank.getPrefix().isEmpty() ? notSet : rank.getPrefix())).append("\n")
                    .append(CC.translate("   &3● &fSuffix: ")).append(CC.translate(rank.getSuffix().isEmpty() ? notSet : rank.getSuffix())).append("\n")
                    .append(CC.translate("   &3● &fWeight: ")).append(CC.translate("&b" + rank.getWeight())).append("\n")
                    .append(CC.translate("   &3● &fCost: ")).append(CC.translate("&b" + rank.getCost())).append("\n")
                    .append(CC.translate("   &3● &fColor: ")).append(CC.translate(rank.getColor() + rank.getColor().name())).append("\n")
                    .append(CC.translate("   &3● &fStaff: ")).append(CC.translate("&b" + (rank.isStaffRank() ? yes : no))).append("\n")
                    .append(CC.translate("   &3● &fDefault: ")).append(CC.translate("&b" + (rank.isDefaultRank() ? yes : no))).append("\n")
                    .append(CC.translate("   &3● &fPurchasable: ")).append(CC.translate("&b" + (rank.isPurchasable() ? yes : no))).append("\n")
                    .append(CC.translate("   &3● &fHidden: ")).append(CC.translate("&b" + (rank.isHiddenRank() ? yes : no))).append("\n")
                    .append(CC.translate("   &3● &fPermissions: ")).append(CC.translate("&b" + (rank.getPermissions().isEmpty() ? none : String.join(", ", rank.getPermissions())))).append("\n")
                    .append(CC.translate("   &3● &fInheritance: ")).append(CC.translate("&b" + (rank.getInheritance().isEmpty() ? none : String.join(", ", rank.getInheritance()))))
                    .create();

            ComponentBuilder message = new ComponentBuilder("   ");
            message.append("● ").color(ChatColor.DARK_AQUA);
            message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
            message.append(rank.getName()).color(ChatColor.WHITE);
            message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
            message.append(" - ").color(ChatColor.GRAY);
            message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
            message.append(CC.translate(rank.getPrefix().isEmpty() ? notSet : rank.getPrefix()));
            message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
            message.append(" ").color(ChatColor.GRAY);
            message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
            message.append("[" + rank.getWeight() + "]").color(ChatColor.GRAY);
            if (rank.isDefaultRank()) {
                message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
                message.append(" (Default)").color(ChatColor.GRAY);
            }
            message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));

            player.spigot().sendMessage(message.create());
        });

        player.sendMessage(CC.translate(""));
    }
}