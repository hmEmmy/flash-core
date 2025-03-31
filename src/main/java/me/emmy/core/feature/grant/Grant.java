package me.emmy.core.feature.grant;

import lombok.Getter;
import lombok.Setter;
import me.emmy.core.Flash;
import me.emmy.core.feature.rank.Rank;
import me.emmy.core.feature.rank.RankService;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 31/03/2025
 */
@Getter
@Setter
public class Grant {
    private String rankName;

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

    public Rank getRank() {
        return Flash.getInstance().getServiceRepository().getService(RankService.class).getRank(this.rankName);
    }
}