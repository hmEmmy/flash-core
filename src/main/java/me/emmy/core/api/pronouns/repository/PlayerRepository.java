package me.emmy.core.api.pronouns.repository;

import lombok.Getter;
import me.emmy.core.api.pronouns.enums.EnumPlayerPronouns;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author Emmy
 * @project PronounsAPI
 * @since 05/04/2025
 */
@Getter
public class PlayerRepository {
    private final Map<UUID, EnumPlayerPronouns> playerPronounsMap;

    public PlayerRepository() {
        this.playerPronounsMap = new HashMap<>();
    }

    /**
     * Gets the pronouns of a player.
     *
     * @param player The player whose pronouns are to be retrieved.
     * @return The pronouns of the player.
     */
    public EnumPlayerPronouns getPronouns(Player player) {
        return this.playerPronounsMap.get(player.getUniqueId());
    }

    /**
     * Adds a player to the repository.
     *
     * @param uuid The UUID of the player.
     * @param playerPronouns The pronouns of the player.
     */
    public void addPlayer(UUID uuid, EnumPlayerPronouns playerPronouns) {
        this.playerPronounsMap.put(uuid, playerPronouns);
    }

    /**
     * Removes a player from the repository.
     *
     * @param uuid The UUID of the player.
     */
    public void removePlayer(UUID uuid) {
        this.playerPronounsMap.remove(uuid);
    }
}