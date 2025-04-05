package me.emmy.core.feature.punishment.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 05/04/2025
 */
@Getter
public enum EnumPunishmentType {
    BLACKLIST("Staff member was mad", "Blacklisted"),
    WARN("Staff member was mad", "Warned"),
    MUTE("Staff member was mad", "Muted"),
    KICK("Staff member was mad", "Kicked"),
    BAN("Staff member was mad", "Banned"),

    //TODO: add punishment priority or something like that so that if for example a player is blacklisted and banned, on join the blacklist applies first.

    ;

    private final String defaultPunishmentReason;
    private final String action;
    
    /**
     * Constructor for EnumPunishmentType.
     *
     * @param defaultPunishmentReason The default punishment reason.
     * @param action The action associated with the punishment type.
     */
    EnumPunishmentType(String defaultPunishmentReason, String action) {
        this.defaultPunishmentReason = defaultPunishmentReason;
        this.action = action;
    }
}