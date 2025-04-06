package me.emmy.core.feature.punishment.command.issue;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.database.redis.RedisService;
import me.emmy.core.database.redis.packet.impl.punishment.PunishmentActionPacket;
import me.emmy.core.feature.punishment.Punishment;
import me.emmy.core.feature.punishment.PunishmentService;
import me.emmy.core.feature.punishment.enums.EnumPunishmentType;
import me.emmy.core.profile.Profile;
import me.emmy.core.profile.ProfileService;
import me.emmy.core.server.ServerProperty;
import me.emmy.core.util.ActionBarUtil;
import me.emmy.core.util.CC;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 05/04/2025
 */
public class BanCommand extends BaseCommand {

    @CommandData(name = "ban", permission = "flash.command.ban", description = "Bans a player.", usage = "/ban <player> <duration> [reason] [-s]", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cUsage: /ban <player> <duration> [reason] [-s]"));
            return;
        }

        OfflinePlayer targetPlayer = this.flash.getPlayerIdentityCache().getOfflinePlayer(args[0]);
        UUID targetUUID = targetPlayer.getUniqueId();

        ProfileService profileService = this.flash.getServiceRepository().getService(ProfileService.class);
        Profile targetProfile = profileService.getProfile(targetUUID);
        if (targetProfile == null) {
            sender.sendMessage(CC.translate("&cThat player has never joined."));
            return;
        }

        if (targetProfile.getPunishments().stream().anyMatch(punishment -> punishment.getType() == EnumPunishmentType.BAN && punishment.isActive())) {
            sender.sendMessage(CC.translate("&cThat player is already banned."));
            return;
        }

        String duration = "perm";
        if (!args[1].equalsIgnoreCase("perm") && !args[1].equalsIgnoreCase("permanent")) {
            duration = args[1];
        }

        String reason = args.length > 2 ? String.join(" ", Arrays.copyOfRange(args, 2, args.length)) : EnumPunishmentType.BAN.getDefaultPunishmentReason();
        String server = this.flash.getServiceRepository().getService(ServerProperty.class).getName();
        String issuer = sender instanceof Player ? sender.getName() : "Console";

        boolean silent = false;
        if (args[args.length - 1].equalsIgnoreCase("-s")) {
            silent = true;
            reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length - 1));
        }

        PunishmentService punishmentService = this.flash.getServiceRepository().getService(PunishmentService.class);
        Punishment punishment = punishmentService.createPunishment(
                issuer,
                duration,
                reason,
                EnumPunishmentType.BAN,
                server,
                silent
        );
        punishmentService.addActivePunishment(punishment, targetProfile);

        PunishmentActionPacket packet = new PunishmentActionPacket(punishment, targetPlayer.getName());
        this.flash.getServiceRepository().getService(RedisService.class).sendPacket(packet);

        if (sender instanceof Player) {
            Profile senderProfile = profileService.getProfile(((Player) sender).getUniqueId());
            if (senderProfile != null) {
                senderProfile.getPunishmentData().addIssuedPunishment(punishment);
            }
            ActionBarUtil.sendMessage((Player) sender, "&7&oYou have issued a ban to &e" + targetProfile.getUsername() + "&7&o for &e" + duration + "&7&o.", 20);
        } else {
            sender.sendMessage(CC.translate("&7&oYou have issued a ban to &e" + targetProfile.getUsername() + "&7&o for &e" + duration + "&7&o."));
        }

        // TODO: Implement console-issued punishment tracking
    }
}
