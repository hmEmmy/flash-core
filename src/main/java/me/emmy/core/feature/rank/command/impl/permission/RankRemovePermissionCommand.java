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
public class RankRemovePermissionCommand extends BaseCommand {
    @CommandData(name = "rank.removepermission", permission = "flash.rank.removepermission", description = "Remove a permission from a rank", usage = "/rank removepermission <rank> <permission>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank removepermission <rank> <permission>"));
            return;
        }

        RankService rankService = this.flash.getServiceRepository().getService(RankService.class);
        Rank rank = rankService.getRank(args[0]);
        if (rank == null) {
            player.sendMessage(CC.translate("&cA rank with that name does not exist!"));
            return;
        }

        String permission = args[1];
        if (!rank.getPermissions().contains(permission)) {
            player.sendMessage(CC.translate("&cThis rank does not have that permission!"));
            return;
        }

        rank.getPermissions().remove(permission);
        rankService.saveRank(rank);
        rankService.sendUpdatePacket(rank);

        ActionBarUtil.sendMessage(player, "&aYou have successfully removed the permission &b" + permission + " &afrom the rank &b" + rank.getName() + "&a!", 10);
    }
}