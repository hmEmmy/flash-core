package me.emmy.core.feature.rank.command.impl;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.feature.rank.Rank;
import me.emmy.core.feature.rank.RankService;
import me.emmy.core.util.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 29/03/2025
 */
public class RankInfoCommand extends BaseCommand {
    @CommandData(name = "rank.info", permission = "flash.rank.info", description = "View rank information", usage = "/rank info <rank>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /rank info <rank>"));
            return;
        }

        String rankName = args[0];
        RankService rankService = this.flash.getServiceRepository().getService(RankService.class);
        Rank rank = rankService.getRank(rankName);
        if (rank == null) {
            player.sendMessage(CC.translate("&cA rank with that name does not exist!"));
            return;
        }

        String yes = CC.translate("&aYes");
        String no = CC.translate("&cNo");
        String notSet = CC.translate("&cNot Set");
        String none = CC.translate("&cNone");

        Arrays.asList(
                "",
                "  &3&lRank Information &8- &7" + rank.getColor() + rank.getName(),
                "   &3● &fName: &b" + rank.getName(),
                "   &3● &fPrefix: &b" + (rank.getPrefix().isEmpty() ? notSet : rank.getPrefix()),
                "   &3● &fSuffix: &b" + (rank.getSuffix().isEmpty() ? notSet : rank.getSuffix()),
                "   &3● &fWeight: &b" + rank.getWeight(),
                "   &3● &fCost: &b" + rank.getCost(),
                "   &3● &fColor: &b" + rank.getColor() + rank.getColor().name(),
                "   &3● &fStaff: &b" + (rank.isStaffRank() ? yes : no),
                "   &3● &fDefault: &b" + (rank.isDefaultRank() ? yes : no),
                "   &3● &fPurchasable: &b" + (rank.isPurchasable() ? yes : no),
                "   &3● &fHidden: &b" + (rank.isHiddenRank() ? yes : no),
                "   &3● &fInheritance: &b" + (rank.getInheritance().isEmpty() ? none : rank.getInheritance().toString()),
                "   &3● &fPermissions: &b" + (rank.getPermissions().isEmpty() ? none : rank.getPermissions().toString()),
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));
    }
}