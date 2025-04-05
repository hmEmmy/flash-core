package me.emmy.core.profile.data;

import lombok.Getter;
import me.emmy.core.feature.punishment.Punishment;
import me.emmy.core.feature.punishment.enums.EnumPunishmentType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 05/04/2025
 */
@Getter
public class PunishmentData {
    private final List<Punishment> issuedPunishments;

    public PunishmentData() {
        this.issuedPunishments = new ArrayList<>();
    }

    /**
     * Retrieves the list of issued punishments.
     *
     * @param punishment The punishment to be added.
     */
    public void addIssuedPunishment(Punishment punishment) {
        this.issuedPunishments.add(punishment);
    }

    /**
     * Removes a punishment from the list of issued punishments.
     *
     * @param punishment The punishment to be removed.
     */
    public void removeIssuedPunishment(Punishment punishment) {
        this.issuedPunishments.remove(punishment);
    }

    /**
     * Retrieves the list of all issued, active punishments.
     *
     * @return A list of all issued punishments.
     */
    public Punishment getActivePunishments() {
        return this.issuedPunishments.stream()
                .filter(punishment -> punishment.isActive() && !punishment.hasExpired())
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves the list of all issued, inactive punishments.
     *
     * @return A list of all issued punishments.
     */
    public Punishment getInactivePunishments() {
        return this.issuedPunishments.stream()
                .filter(punishment -> !punishment.isActive() || punishment.hasExpired())
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves the list of issued punishments of a specific type.
     *
     * @param type The type of punishment to filter by.
     * @return A list of issued punishments of the specified type.
     */
    public Punishment getActivePunishmentsOfType(EnumPunishmentType type) {
        return this.issuedPunishments.stream()
                .filter(punishment -> punishment.isActive() && !punishment.hasExpired() && punishment.getType() == type)
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves the list of issued punishments of a specific type.
     *
     * @param type The type of punishment to filter by.
     * @return A list of issued punishments of the specified type.
     */
    public Punishment getInactivePunishmentsOfType(EnumPunishmentType type) {
        return this.issuedPunishments.stream()
                .filter(punishment -> !punishment.isActive() || punishment.hasExpired() && punishment.getType() == type)
                .findFirst()
                .orElse(null);
    }
}