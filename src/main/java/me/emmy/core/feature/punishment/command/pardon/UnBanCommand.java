package me.emmy.core.feature.punishment.command.pardon;

import me.emmy.core.api.command.BaseCommand;
import me.emmy.core.api.command.CommandArgs;
import me.emmy.core.api.command.annotation.CommandData;
import me.emmy.core.database.redis.RedisService;
import me.emmy.core.database.redis.packet.impl.punishment.PunishmentUndoPacket;
import me.emmy.core.feature.punishment.Punishment;
import me.emmy.core.feature.punishment.PunishmentService;
import me.emmy.core.feature.punishment.enums.EnumPunishmentType;
import me.emmy.core.profile.Profile;
import me.emmy.core.profile.ProfileService;
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
 * @since 06/04/2025
 */
public class UnBanCommand extends BaseCommand {
    @CommandData(name = "unban", permission = "flash.command.unban", description = "Unbans a player.", usage = "/unban <player>", aliases = "pardon", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cUsage: /unban <player> <reason>"));
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

        Punishment punishment = targetProfile.getPunishments().stream()
                .filter(pun -> pun.getType() == EnumPunishmentType.BAN && pun.isActive())
                .findFirst()
                .orElse(null);

        if (punishment == null) {
            sender.sendMessage(CC.translate("&cThat player is not banned."));
            return;
        }

        String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        String issuer = sender instanceof Player ? sender.getName() : "Console";

        boolean silent = false;
        if (args[args.length - 1].equalsIgnoreCase("silent")) {
            silent = true;
            reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length - 1));
        }

        punishment.setRemovalSilent(silent);

        PunishmentService punishmentService = this.flash.getServiceRepository().getService(PunishmentService.class);
        punishmentService.deactivatePunishment(punishment, issuer, targetProfile, reason);

        PunishmentUndoPacket packet = new PunishmentUndoPacket(punishment, targetPlayer.getName());
        this.flash.getServiceRepository().getService(RedisService.class).sendPacket(packet);

        if (sender instanceof Player) {
            Profile senderProfile = profileService.getProfile(((Player) sender).getUniqueId());
            if (senderProfile != null) {
                senderProfile.getPunishmentData().addIssuedPunishment(punishment);
            }
            ActionBarUtil.sendMessage((Player) sender, "&7&oYou have unbanned &e" + targetProfile.getUsername() + "&7&o.", 20);
        } else {
            sender.sendMessage(CC.translate("&7&oYou have unbanned &e" + targetProfile.getUsername() + "&7&o."));
        }
    }
}
