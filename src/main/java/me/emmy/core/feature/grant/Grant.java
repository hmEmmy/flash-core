package me.emmy.core.feature.grant;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 31/03/2025
 */
@Getter
@Setter
public class Grant {
    private String rank;

    private String server;
    private String issuer;
    private String reason;

    private String remover;
    private String removalReason;

    private long duration;
    private long addedAt;

    private long removedAt;

    private boolean permanent;
    private boolean active;

    /**
     * Checks if the grant has expired.
     *
     * @return true if the grant has expired, false otherwise.
     */
    public boolean hasExpired() {
        return !this.permanent && System.currentTimeMillis() >= this.addedAt + this.duration;
    }
}