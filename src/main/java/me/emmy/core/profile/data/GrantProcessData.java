package me.emmy.core.profile.data;

import lombok.Getter;
import lombok.Setter;
import me.emmy.core.feature.rank.Rank;
import org.bukkit.OfflinePlayer;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 31/03/2025
 */
@Getter
@Setter
public class GrantProcessData {
    private final OfflinePlayer target;
    private Rank rank;
    private String reason;

    /**
     * Constructor for the GrantProcessData class.
     *
     * @param target The target player for the grant process.
     * @param rank   The rank to be granted.
     */
    public GrantProcessData(OfflinePlayer target, Rank rank) {
        this.target = target;
        this.rank = rank;
        this.reason = "";
    }
}