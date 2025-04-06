package me.emmy.core.feature.punishment.enums;

import com.avaje.ebean.validation.NotNull;
import lombok.Getter;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 05/04/2025
 */
@Getter
public enum EnumPunishmentType {
    BLACKLIST("Staff member was mad", "Blacklisted", "Unblacklisted"),
    WARN("Staff member was mad", "Warned", null),
    MUTE("Staff member was mad", "Muted", "Unmuted"),
    KICK("Staff member was mad", "Kicked", null),
    BAN("Staff member was mad", "Banned", "Unbanned"),

    //TODO: add punishment priority or something like that so that if for example a player is blacklisted and banned, on join the blacklist applies first.

    ;

    private final String defaultPunishmentReason;
    private final String action;
    private final String pardonAction;
    
    /**
     * Constructor for the EnumPunishmentType enum.
     *
     * @param defaultPunishmentReason The default punishment reason.
     * @param action The action associated with the punishment type.
     * @param pardonAction The action associated with pardoning the punishment type.
     */
    EnumPunishmentType(String defaultPunishmentReason, String action, String pardonAction) {
        this.defaultPunishmentReason = defaultPunishmentReason;
        this.action = action;
        this.pardonAction = pardonAction;
    }

    /**
     * Retrieves the pardon action string associated with the punishment type.
     *
     * @return The action associated with the punishment type.
     */
    public @NotNull String getPardonAction() {
        if (this == WARN || this == KICK) {
            return null;
        }

        return this.pardonAction;
    }

    /**
     * Retrieves the pardon action string associated with the punishment type.
     *
     * @return The action associated with the punishment type.
     */
    public @NotNull String getPardonActionUpperCase() {
        if (this == WARN || this == KICK) {
            return null;
        }

        return this.pardonAction.toUpperCase();
    }
}