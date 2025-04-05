package me.emmy.core.feature.punishment.command;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.feature.punishment.Punishment;
import me.emmy.core.feature.punishment.PunishmentService;
import me.emmy.core.feature.punishment.enums.EnumPunishmentType;
import me.emmy.core.profile.Profile;
import me.emmy.core.profile.ProfileService;
import me.emmy.core.server.ServerProperty;
import me.emmy.core.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 05/04/2025
 */
public class BanCommand extends BaseCommand {
    @CommandData(name = "ban", permission = "flash.command.ban", description = "Bans a player.", usage = "/ban <player> <duration> &7[reason] [silent: -s]", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cUsage: /ban <player> <duration> &7[reason] [silent: -s]"));
            return;
        }

        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        ProfileService profileService = this.flash.getServiceRepository().getService(ProfileService.class);
        Profile targetProfile = profileService.getProfile(targetPlayer.getUniqueId());
        if (targetProfile == null) {
            sender.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        String duration;
        if (args.length > 2 && args[1].equalsIgnoreCase("permanent") || args[1].equalsIgnoreCase("perm")) {
            duration = "permanent";
        } else {
            duration = args[1];
        }

        String reason = args.length > 2 ? String.join(" ", Arrays.copyOfRange(args, 1, args.length)) : "";
        boolean silent = args.length > 3 && args[args.length - 1].equalsIgnoreCase("-s");
        String issuer = sender instanceof Player ? (sender).getName() : "Console";
        String server = this.flash.getServiceRepository().getService(ServerProperty.class).getName();

        PunishmentService punishmentService = this.flash.getServiceRepository().getService(PunishmentService.class);
        Punishment punishment = punishmentService.createPunishment(issuer, duration, reason, EnumPunishmentType.BAN, server, silent);
        punishmentService.addActivePunishment(punishment, targetProfile);

        if (sender instanceof Player) {
            Profile profile = profileService.getProfile(((Player) sender).getPlayer().getUniqueId());
            profile.getPunishmentData().addIssuedPunishment(punishment);
        }

        //TODO: do something for console bans i also wanna store them somewhere
    }
}