package me.emmy.core.feature.punishment;

import me.emmy.core.Flash;
import me.emmy.core.api.service.IService;
import me.emmy.core.feature.punishment.enums.EnumPunishmentType;
import me.emmy.core.profile.Profile;
import me.emmy.core.util.CC;
import me.emmy.core.util.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 05/04/2025
 */
public class PunishmentService implements IService {
    protected final Flash plugin;

    /**
     * Constructor for PunishmentService.
     *
     * @param plugin The plugin instance of Flash.
     */
    public PunishmentService(Flash plugin) {
        this.plugin = plugin;
    }

    /**
     * Method to add a punishment to a profile.
     *
     * @param punishment    the punishment to add
     * @param targetProfile the profile to which the punishment will be added
     */
    public void addActivePunishment(Punishment punishment, Profile targetProfile) {
        List<Punishment> punishments = targetProfile.getPunishments();
        punishments = punishments == null ? new ArrayList<>() : new ArrayList<>(punishments);
        punishments.add(punishment);

        targetProfile.setPunishments(punishments);
        targetProfile.saveProfile();

        Player player = Bukkit.getPlayer(targetProfile.getUuid());
        if (player != null) {
            player.kickPlayer(CC.translate("&cYou have been &4&l" + punishment.getType().getAction().toUpperCase() + "&c!"));
        }
    }

    /**
     * Deactivate a punishment for a profile.
     *
     * @param punishment    the punishment to remove
     * @param remover       the player removing the punishment
     * @param targetProfile the profile from which the punishment will be removed
     * @param removalReason the reason for removing the punishment
     */
    public void deactivatePunishment(Punishment punishment, String remover, Profile targetProfile, String removalReason) {
        punishment.setRemover(remover);
        punishment.setRemovedAt(System.currentTimeMillis());
        punishment.setRemovalReason(removalReason);
        punishment.setActive(false);

        targetProfile.saveProfile();
    }

    /**
     * Remove a punishment from a profile.
     *
     * @param punishment    the punishment to remove
     * @param targetProfile the profile from which the punishment will be removed
     */
    public void removePunishment(Punishment punishment, Profile targetProfile) {
        List<Punishment> punishments = targetProfile.getPunishments();
        punishments.remove(punishment);

        targetProfile.setPunishments(punishments);
        targetProfile.saveProfile();
    }

    /**
     * Create a new punishment.
     *
     * @param issuer   the issuer of the punishment
     * @param duration the duration of the punishment
     * @param reason   the reason for the punishment
     * @param type     the type of punishment
     * @param server   the server where the punishment was issued
     * @param silent   whether the punishment is silent
     * @return the created punishment
     */
    public Punishment createPunishment(String issuer, String duration, String reason, EnumPunishmentType type, String server, boolean silent) {
        Punishment punishment = new Punishment();
        punishment.setType(type);
        punishment.setIssuer(issuer);

        boolean isPermanent = this.isDurationPermanent(duration);
        punishment.setPermanent(isPermanent);

        punishment.setActive(true);
        punishment.setSilent(silent);
        punishment.setServer(server);

        punishment.setDuration(isPermanent ? -1 : DateUtils.parseTime(duration));

        punishment.setAddedAt(System.currentTimeMillis());
        punishment.setReason(reason);

        return punishment;
    }


    /**
     * Check if the duration is permanent
     *
     * @param duration the duration to check
     * @return if the duration is permanent
     */
    private boolean isDurationPermanent(String duration) {
        return duration.equalsIgnoreCase("permanent") || duration.equalsIgnoreCase("perm");
    }

    @Override
    public void initialize() {

    }

    @Override
    public void closure() {

    }
}