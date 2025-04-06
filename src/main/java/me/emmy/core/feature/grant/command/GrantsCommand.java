package me.emmy.core.feature.grant.command;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.feature.grant.menu.GrantsMenu;
import me.emmy.core.profile.Profile;
import me.emmy.core.profile.ProfileService;
import me.emmy.core.util.CC;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 31/03/2025
 */
public class GrantsCommand extends BaseCommand {
    @CommandData(name = "grants", permission = "flash.command.grants", description = "View your grants.", usage = "/grants <player>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /grants <player>"));
            return;
        }

        OfflinePlayer target = this.flash.getPlayerIdentityCache().getOfflinePlayer(args[0]);
        ProfileService profileService = this.flash.getServiceRepository().getService(ProfileService.class);
        Profile targetProfile = profileService.getProfile(target.getUniqueId());
        if (targetProfile == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        if (targetProfile.getGrants().isEmpty()) {
            player.sendMessage(CC.translate("&cThat player has no grants."));
            return;
        }

        player.sendMessage(CC.translate("&7&oPlease wait..."));
        new GrantsMenu(target, false).openMenu(player);
    }
}
