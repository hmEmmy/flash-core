package me.emmy.core.feature.punishment;

import lombok.Getter;
import lombok.Setter;
import me.emmy.core.feature.punishment.enums.EnumPunishmentType;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 05/04/2025
 */
@Getter
@Setter
public class Punishment {
    private EnumPunishmentType type;

    private String issuer;
    private String server;
    private String reason;

    private String remover;
    private String removalReason;

    private long duration;
    private long addedAt;

    private long removedAt;

    private boolean permanent;
    private boolean silent;
    private boolean active;

    private boolean removalSilent;

    /**
     * Checks if the punishment has expired.
     *
     * @return true if the punishment has expired, false otherwise.
     */
    public boolean hasExpired() {
        return !this.permanent && System.currentTimeMillis() >= this.addedAt + this.duration;
    }
}