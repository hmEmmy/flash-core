package me.emmy.core.feature.grant.command;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.feature.grant.menu.GrantMenu;
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
public class GrantCommand extends BaseCommand {
    @CommandData(name = "grant", permission = "flash.command.grant", description = "Grants a player a rank.", usage = "/grant <player> [force]")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /grant <player> &7[force]"));
            return;
        }

        OfflinePlayer target = this.flash.getPlayerIdentityCache().getOfflinePlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        ProfileService profileService = this.flash.getServiceRepository().getService(ProfileService.class);

        if (args.length == 2 && args[1].equalsIgnoreCase("force")) {
            Profile profile = profileService.getProfileCreateIfAbsent(target.getUniqueId());
            player.sendMessage(CC.translate("&7&oForcing grant for &e" + target.getName() + "..."));
            if (profile == null) {
                player.sendMessage(CC.translate("&cThat is not a valid player."));
                return;
            }

            new GrantMenu(target).openMenu(player);
        } else {
            Profile profile = profileService.getProfile(target.getUniqueId());
            if (profile == null) {
                player.sendMessage(CC.translate("&cPlayer not found."));
                return;
            }
        }

        player.sendMessage(CC.translate("&7&oPlease wait..."));
        new GrantMenu(target).openMenu(player);
    }
}
