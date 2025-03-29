package me.emmy.core.feature.rank.command.impl.permission;

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
public class RankAddPermissionCommand extends BaseCommand {
    @CommandData(name = "rank.addpermission", permission = "flash.rank.addpermission", description = "Add a permission to a rank", usage = "/rank addpermission <rank> <permission>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank addpermission <rank> <permission>"));
            return;
        }

        RankService rankService = this.flash.getServiceRepository().getService(RankService.class);
        Rank rank = rankService.getRank(args[0]);
        if (rank == null) {
            player.sendMessage(CC.translate("&cA rank with that name does not exist!"));
            return;
        }

        String permission = args[1];
        if (rank.getPermissions().contains(permission)) {
            player.sendMessage(CC.translate("&cThat permission is already assigned to this rank!"));
            return;
        }

        rank.getPermissions().add(permission);
        rankService.saveRank(rank);
        ActionBarUtil.sendMessage(player, "&aYou have successfully added the permission &b" + permission + " &ato the rank &b" + rank.getName() + "&a!", 7);
    }
}